package com.ntt.buoi6.config;

import com.ntt.buoi6.model.AppUser;
import com.ntt.buoi6.model.Category;
import com.ntt.buoi6.model.Product;
import com.ntt.buoi6.model.Role;
import com.ntt.buoi6.repository.CategoryRepository;
import com.ntt.buoi6.repository.ProductRepository;
import com.ntt.buoi6.repository.RoleRepository;
import com.ntt.buoi6.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        // ========================
        // 1. Seed Roles (nếu chưa có)
        // ========================
        if (roleRepository.count() == 0) {
            Role adminRole = roleRepository.save(new Role(null, "ROLE_ADMIN"));
            Role userRole  = roleRepository.save(new Role(null, "ROLE_USER"));

            // ========================
            // 2. Seed Users
            // ========================
            AppUser admin = new AppUser();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEnabled(true);
            admin.setRoles(Set.of(adminRole));
            userRepository.save(admin);

            AppUser user = new AppUser();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setEnabled(true);
            user.setRoles(Set.of(userRole));
            userRepository.save(user);

            System.out.println("✅ Roles và Users mẫu đã được khởi tạo.");
        }

        // ========================
        // 3. Seed Categories (nếu chưa có)
        // ========================
        if (categoryRepository.count() == 0) {
            List<Category> categories = categoryRepository.saveAll(List.of(
                    new Category(null, "Điện tử"),
                    new Category(null, "Thời trang"),
                    new Category(null, "Gia dụng"),
                    new Category(null, "Thực phẩm"),
                    new Category(null, "Đồ chơi")
            ));

            // ========================
            // 4. Seed Products (nếu chưa có)
            // ========================
            if (productRepository.count() == 0) {
                productRepository.saveAll(List.of(
                        new Product(null, "iPhone 15 Pro", 29990000.0,
                                "Điện thoại Apple iPhone 15 Pro 256GB", categories.get(0)),
                        new Product(null, "Samsung Galaxy S24", 21990000.0,
                                "Samsung Galaxy S24 Ultra 512GB", categories.get(0)),
                        new Product(null, "Áo Polo Nam", 350000.0,
                                "Áo Polo nam cao cấp, nhiều màu", categories.get(1)),
                        new Product(null, "Nồi Cơm Điện Panasonic", 1290000.0,
                                "Nồi cơm điện Panasonic 1.8L", categories.get(2)),
                        new Product(null, "Sữa TH True Milk", 45000.0,
                                "Sữa tươi tiệt trùng TH True Milk 1L", categories.get(3))
                ));
                System.out.println("✅ Danh mục và sản phẩm mẫu đã được khởi tạo.");
            }
        }
    }
}
