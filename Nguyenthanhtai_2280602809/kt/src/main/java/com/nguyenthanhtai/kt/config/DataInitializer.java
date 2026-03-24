package com.nguyenthanhtai.kt.config;

import com.nguyenthanhtai.kt.entity.Patient;
import com.nguyenthanhtai.kt.entity.Role;
import com.nguyenthanhtai.kt.repository.PatientRepository;
import com.nguyenthanhtai.kt.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final PatientRepository patientRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        // 1. Tạo role ADMIN nếu chưa có
        Role adminRole = roleRepository.findByName("ADMIN").orElseGet(() -> {
            Role r = new Role();
            r.setName("ADMIN");
            return roleRepository.save(r);
        });

        // 2. Tạo role PATIENT nếu chưa có
        Role patientRole = roleRepository.findByName("PATIENT").orElseGet(() -> {
            Role r = new Role();
            r.setName("PATIENT");
            return roleRepository.save(r);
        });

        // 3. Tạo hoặc cập nhật tài khoản admin
        Patient admin = patientRepository.findByUsername("admin").orElseGet(() -> {
            Patient p = new Patient();
            p.setUsername("admin");
            p.setEmail("admin@clinic.com");
            return p;
        });
        // Luôn encode lại password để đảm bảo đúng
        admin.setPassword(passwordEncoder.encode("123456"));
        admin.setRoles(Set.of(adminRole));
        patientRepository.save(admin);

        // 4. Tạo hoặc cập nhật patient1
        Patient patient1 = patientRepository.findByUsername("patient1").orElseGet(() -> {
            Patient p = new Patient();
            p.setUsername("patient1");
            p.setEmail("patient1@gmail.com");
            return p;
        });
        patient1.setPassword(passwordEncoder.encode("123456"));
        patient1.setRoles(Set.of(patientRole));
        patientRepository.save(patient1);

        // 5. Tạo hoặc cập nhật patient2
        Patient patient2 = patientRepository.findByUsername("patient2").orElseGet(() -> {
            Patient p = new Patient();
            p.setUsername("patient2");
            p.setEmail("patient2@gmail.com");
            return p;
        });
        patient2.setPassword(passwordEncoder.encode("123456"));
        patient2.setRoles(Set.of(patientRole));
        patientRepository.save(patient2);

        System.out.println("✅ DataInitializer: Tài khoản đã được khởi tạo/cập nhật thành công!");
        System.out.println("   admin/123456 -> ADMIN");
        System.out.println("   patient1/123456 -> PATIENT");
        System.out.println("   patient2/123456 -> PATIENT");
    }
}
