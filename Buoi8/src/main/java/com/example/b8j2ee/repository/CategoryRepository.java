package com.example.b8j2ee.repository;

import com.example.b8j2ee.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
