package com.example.demo.service;

import com.example.demo.model.Category;
import com.example.demo.model.Product;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Service quản lý sản phẩm.
 * Dữ liệu được lưu trữ tạm thời trong bộ nhớ (in-memory List).
 */
@Service
public class ProductService {

    // Bộ đếm ID tự tăng
    private int nextId = 4;

    // Danh sách sản phẩm được khởi tạo sẵn
    private List<Product> products = new ArrayList<>(List.of(
            new Product(1, "iPhone 15 Pro", "iphone15.jpg", 28990000L,
                    new Category(1, "Điện thoại")),
            new Product(2, "MacBook Air M2", "macbook_m2.jpg", 32990000L,
                    new Category(2, "Laptop")),
            new Product(3, "iPad Pro 2024", "ipad_pro.jpg", 20990000L,
                    new Category(3, "Máy tính bảng"))));

    /**
     * Lấy toàn bộ danh sách sản phẩm
     */
    public List<Product> getAll() {
        return products;
    }

    /**
     * Lấy sản phẩm theo id
     */
    public Product get(int id) {
        return products.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElse(null);
    }

    /**
     * Thêm sản phẩm mới vào danh sách
     */
    public void add(Product product) {
        product.setId(nextId++);
        products.add(product);
    }

    /**
     * Cập nhật thông tin sản phẩm (tìm theo id rồi thay thế)
     */
    public void update(Product product) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId() == product.getId()) {
                products.set(i, product);
                return;
            }
        }
    }

    /**
     * Xử lý upload ảnh sản phẩm:
     * - Kiểm tra file có hợp lệ (không rỗng và là ảnh)
     * - Tạo tên file mới bằng UUID để tránh trùng lặp
     * - Lưu file vào thư mục static/images trong classpath
     * - Gán tên file vào thuộc tính image của Product
     *
     * @param newProduct   đối tượng Product cần cập nhật ảnh
     * @param imageProduct file ảnh được upload từ form
     */
    public void updateImage(Product newProduct, MultipartFile imageProduct) {
        // Kiểm tra file có hợp lệ và là file ảnh không
        if (imageProduct != null && !imageProduct.isEmpty()) {
            String contentType = imageProduct.getContentType();
            // Chỉ chấp nhận các file có contentType là image/*
            if (contentType != null && contentType.startsWith("image/")) {
                try {
                    // Lấy phần mở rộng file gốc (ví dụ: .jpg, .png)
                    String originalFilename = imageProduct.getOriginalFilename();
                    String extension = "";
                    if (originalFilename != null && originalFilename.contains(".")) {
                        extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                    }

                    // Tạo tên file mới bằng UUID để đảm bảo tính duy nhất
                    String newFilename = UUID.randomUUID().toString() + extension;

                    // Xác định đường dẫn thư mục lưu file:
                    // target/classes/static/images (nơi Spring Boot phục vụ static resources)
                    Path uploadDir = Paths.get("src/main/resources/static/images");

                    // Tạo thư mục nếu chưa tồn tại
                    Files.createDirectories(uploadDir);

                    // Lưu file vào thư mục
                    Path filePath = uploadDir.resolve(newFilename);
                    try (InputStream inputStream = imageProduct.getInputStream()) {
                        Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                    }

                    // Gán tên file (đã đổi tên) vào thuộc tính image của Product
                    newProduct.setImage(newFilename);

                } catch (IOException e) {
                    throw new RuntimeException("Lỗi khi lưu ảnh: " + e.getMessage(), e);
                }
            }
        }
    }
}
