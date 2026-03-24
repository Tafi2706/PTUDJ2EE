package com.nguyenthanhtai.kt.config;

import com.nguyenthanhtai.kt.service.PatientDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final PatientDetailsService patientDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @SuppressWarnings("deprecation")
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(patientDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authenticationProvider(authProvider())
            .authorizeHttpRequests(auth -> auth

                // ── Câu 4: Phân quyền theo yêu cầu đề bài ───────────────────

                // /admin/** chỉ ADMIN truy cập
                .requestMatchers("/admin/**").hasRole("ADMIN")

                // /courses cho TẤT CẢ người dùng (kể cả chưa đăng nhập)
                .requestMatchers("/courses").permitAll()

                // /enroll/** chỉ PATIENT được phép
                .requestMatchers("/enroll/**").hasRole("PATIENT")

                // ── Các route công khai ───────────────────────────────────────
                .requestMatchers(
                    "/", "/home",
                    "/register", "/login",
                    "/css/**", "/js/**", "/images/**",
                    "/access-denied"
                ).permitAll()

                // ── Lịch hẹn (PATIENT) ───────────────────────────────────────
                .requestMatchers("/appointments/book/**").hasRole("PATIENT")
                .requestMatchers("/appointments/my").hasRole("PATIENT")

                // Còn lại cần đăng nhập
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/home", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/home")
                .permitAll()
            )
            .exceptionHandling(ex -> ex
                .accessDeniedPage("/access-denied")
            );

        return http.build();
    }
}
