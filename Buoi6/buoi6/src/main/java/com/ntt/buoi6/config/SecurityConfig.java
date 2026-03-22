package com.ntt.buoi6.config;

import com.ntt.buoi6.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;

    // ========================
    // Password Encoder (BCrypt)
    // ========================
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ========================
    // Authentication Provider
    // ========================
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // ========================
    // Security Filter Chain
    // ========================
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authenticationProvider(authenticationProvider())
            .authorizeHttpRequests(auth -> auth
                // Trang login: cho tất cả truy cập
                .requestMatchers("/login", "/css/**", "/js/**", "/images/**").permitAll()
                // Xem danh sách: cần đăng nhập (ROLE_USER hoặc ROLE_ADMIN)
                .requestMatchers("/products").hasAnyRole("USER", "ADMIN")
                // Thêm/Sửa/Xóa: chỉ ADMIN
                .requestMatchers("/products/add", "/products/edit/**",
                                 "/products/save", "/products/delete/**").hasRole("ADMIN")
                // Tất cả route còn lại cần xác thực
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")          // Trang login tùy chỉnh
                .loginProcessingUrl("/login") // Spring Security xử lý POST login
                .defaultSuccessUrl("/products", true) // Sau login thành công
                .failureUrl("/login?error=true")      // Sau login thất bại
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .exceptionHandling(ex -> ex
                .accessDeniedPage("/access-denied") // Trang 403
            );

        return http.build();
    }
}
