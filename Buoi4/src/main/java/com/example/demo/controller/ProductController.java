package com.example.demo.controller;

import com.example.demo.model.Category;
import com.example.demo.model.Product;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Controller xử lý các yêu cầu liên quan đến sản phẩm.
 * Được map tại đường dẫn /products.
 */
@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    /**
     * GET /products
     * Hiển thị danh sách tất cả sản phẩm
     */
    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productService.getAll());
        return "product/products";
    }

    /**
     * GET /products/create
     * Hiển thị form tạo sản phẩm mới.
     * Truyền đối tượng Product rỗng và danh sách categories xuống view.
     */
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAll());
        return "product/create";
    }

    /**
     * POST /products/create
     * Xử lý tạo sản phẩm mới:
     * - @Valid kích hoạt Bean Validation cho đối tượng Product
     * - BindingResult chứa kết quả validation (lỗi nếu có)
     * - MultipartFile nhận file ảnh từ form
     */
    @PostMapping("/create")
    public String createProduct(
            @Valid @ModelAttribute("product") Product product,
            BindingResult result,
            @RequestParam("imageProduct") MultipartFile imageProduct,
            Model model) {

        // Nếu có lỗi validation, trả lại form với thông báo lỗi
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getAll());
            return "product/create";
        }

        // Xử lý upload ảnh: lưu file và cập nhật thuộc tính image của Product
        productService.updateImage(product, imageProduct);

        // Lấy đối tượng Category đầy đủ từ id mà form gửi lên
        if (product.getCategory() != null) {
            Category fullCategory = categoryService.get(product.getCategory().getId());
            product.setCategory(fullCategory);
        }

        // Thêm sản phẩm vào danh sách
        productService.add(product);

        // Redirect về trang danh sách sau khi thành công
        return "redirect:/products";
    }

    /**
     * GET /products/edit/{id}
     * Hiển thị form chỉnh sửa sản phẩm.
     * Truyền sản phẩm hiện tại và danh sách categories xuống view.
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Product product = productService.get(id);
        if (product == null) {
            return "redirect:/products";
        }
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAll());
        return "product/edit";
    }

    /**
     * POST /products/edit
     * Xử lý cập nhật sản phẩm:
     * - Tương tự create nhưng gọi productService.update() thay vì add()
     * - Giữ lại ảnh cũ nếu người dùng không upload ảnh mới
     */
    @PostMapping("/edit")
    public String updateProduct(
            @Valid @ModelAttribute("product") Product product,
            BindingResult result,
            @RequestParam("imageProduct") MultipartFile imageProduct,
            @RequestParam(value = "existingImage", required = false) String existingImage,
            Model model) {

        // Nếu có lỗi validation, trả lại form với thông báo lỗi
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.getAll());
            // Giữ lại tên ảnh cũ trong trường hợp lỗi
            product.setImage(existingImage);
            return "product/edit";
        }

        // Nếu không upload ảnh mới thì giữ lại ảnh cũ
        if (imageProduct == null || imageProduct.isEmpty()) {
            product.setImage(existingImage);
        } else {
            // Xử lý upload ảnh mới
            productService.updateImage(product, imageProduct);
        }

        // Lấy đối tượng Category đầy đủ từ id
        if (product.getCategory() != null) {
            Category fullCategory = categoryService.get(product.getCategory().getId());
            product.setCategory(fullCategory);
        }

        // Cập nhật sản phẩm trong danh sách
        productService.update(product);

        return "redirect:/products";
    }
}
