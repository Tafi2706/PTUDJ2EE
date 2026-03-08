package com.example.demo.model;

import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;

/**
 * Model đại diện cho sản phẩm.
 * Sử dụng Bean Validation (jakarta.validation) để kiểm tra dữ liệu đầu vào.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    private int id;

    // Validation: tên sản phẩm không được để trống
    @NotBlank(message = "Tên sản phẩm không được để trống")
    private String name;

    // Validation: tên file ảnh không quá 200 ký tự
    @Length(max = 200, message = "Tên hình ảnh không quá 200 kí tự")
    private String image;

    // Validation: giá phải có giá trị, tối thiểu 1 và tối đa 9.999.999
    @NotNull(message = "Giá sản phẩm không được để trống")
    @Min(value = 1, message = "Giá sản phẩm không được nhỏ hơn 1")
    @Max(value = 9999999, message = "Giá sản phẩm không được lớn hơn 9999999")
    private Long price;

    // Quan hệ nhiều-một với Category
    private Category category;
}
