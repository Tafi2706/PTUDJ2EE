package com.example.b8j2ee.repository;

import com.example.b8j2ee.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // Tìm kiếm theo tên (Câu 1)
    Page<Product> findByNameContainingIgnoreCase(String keyword, Pageable pageable);

    // Lọc theo category (Câu 4)
    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);

    // Tìm kiếm + lọc theo category (kết hợp)
    Page<Product> findByNameContainingIgnoreCaseAndCategoryId(String keyword, Long categoryId, Pageable pageable);

    // Tìm kiếm (không phân trang)
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    java.util.List<Product> searchByName(@Param("keyword") String keyword);
}
