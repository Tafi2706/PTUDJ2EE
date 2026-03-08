package com.example.demo.service;

import com.example.demo.model.Category;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service quản lý danh mục sản phẩm.
 * Dữ liệu được lưu trữ tạm thời trong bộ nhớ (in-memory List).
 */
@Service
public class CategoryService {

    // Danh sách các danh mục được khởi tạo sẵn
    private List<Category> categories = new ArrayList<>(List.of(
            new Category(1, "Điện thoại"),
            new Category(2, "Laptop"),
            new Category(3, "Máy tính bảng"),
            new Category(4, "Phụ kiện")));

    /**
     * Lấy toàn bộ danh sách danh mục
     */
    public List<Category> getAll() {
        return categories;
    }

    /**
     * Lấy danh mục theo id
     * 
     * @param id id của danh mục cần tìm
     * @return Category nếu tìm thấy, null nếu không
     */
    public Category get(int id) {
        return categories.stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }
}
