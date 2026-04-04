package com.example.b8j2ee.controller;

import com.example.b8j2ee.entity.Category;
import com.example.b8j2ee.entity.Product;
import com.example.b8j2ee.service.CategoryService;
import com.example.b8j2ee.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class AdminProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    // Hiển thị danh sách sản phẩm quản trị (kèm phân trang, lọc, sắp xếp như trang khách)
    @GetMapping
    public String listProducts(Model model,
                               @RequestParam(required = false) String keyword,
                               @RequestParam(required = false) Long categoryId,
                               @RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "desc") String sortDir) {

        Page<Product> productPage = productService.getProducts(keyword, categoryId, page, sortDir);
        List<Category> categories = categoryService.findAll();

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("categories", categories);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");

        return "admin/product/list";
    }

    // Giao diện thêm mới
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("pageTitle", "Thêm Sản Phẩm Mới");
        return "admin/product/form";
    }

    // Xử lý thêm/sửa
    @PostMapping("/save")
    public String saveProduct(@ModelAttribute("product") Product product, RedirectAttributes redirectAttributes) {
        Product saved = productService.save(product);
        redirectAttributes.addFlashAttribute("successMessage", "Lưu sản phẩm thành công: " + saved.getName());
        return "redirect:/admin/products";
    }

    // Giao diện sửa
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        Product product = productService.findById(id).orElse(null);
        if (product == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Sản phẩm không tồn tại!");
            return "redirect:/admin/products";
        }
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("pageTitle", "Cập Nhật Sản Phẩm (ID: " + id + ")");
        return "admin/product/form";
    }

    // Xử lý xóa
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Đã xóa sản phẩm thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không thể xóa sản phẩm do có phụ thuộc dữ liệu (Đã có trong Đơn Hàng)!");
        }
        return "redirect:/admin/products";
    }
}
