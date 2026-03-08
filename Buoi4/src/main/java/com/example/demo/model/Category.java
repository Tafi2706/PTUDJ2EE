package com.example.demo.model;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * Model đại diện cho danh mục sản phẩm
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    private int id;

    // Validation: tên danh mục không được để trống
    @NotBlank(message = "Tên danh mục không được để trống")
    private String name;
}
