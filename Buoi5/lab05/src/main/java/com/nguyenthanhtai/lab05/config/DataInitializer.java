package com.nguyenthanhtai.lab05.config;

import com.nguyenthanhtai.lab05.model.Category;
import com.nguyenthanhtai.lab05.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    @Override
    public void run(String... args) throws Exception {
        // Only seed if table is empty
        if (categoryRepository.count() == 0) {
            categoryRepository.saveAll(List.of(
                    new Category(null, "Science & Technology"),
                    new Category(null, "Literature & Fiction"),
                    new Category(null, "History"),
                    new Category(null, "Self-Help"),
                    new Category(null, "Programming"),
                    new Category(null, "Business & Economics")
            ));
            System.out.println("✅ Sample categories seeded into database.");
        }
    }
}
