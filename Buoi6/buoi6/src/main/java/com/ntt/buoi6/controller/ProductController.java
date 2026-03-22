package com.ntt.buoi6.controller;

import com.ntt.buoi6.model.Category;
import com.ntt.buoi6.model.Product;
import com.ntt.buoi6.service.CategoryService;
import com.ntt.buoi6.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    // ========================
    // GET /products — Danh sách
    // ========================
    @GetMapping
    public String listProducts(Model model) {
        List<Product> products = productService.findAll();
        model.addAttribute("products", products);
        model.addAttribute("pageTitle", "Danh sách sản phẩm");
        return "products/list";
    }

    // ========================
    // GET /products/add — Form thêm mới
    // ========================
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("pageTitle", "Thêm sản phẩm mới");
        return "products/form";
    }

    // ========================
    // GET /products/edit/{id} — Form sửa
    // ========================
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model,
                               RedirectAttributes redirectAttributes) {
        try {
            Product product = productService.findById(id);
            model.addAttribute("product", product);
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("pageTitle", "Sửa sản phẩm: " + product.getName());
            return "products/form";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/products";
        }
    }

    // ========================
    // POST /products/save — Lưu (thêm hoặc cập nhật)
    // ========================
    @PostMapping("/save")
    public String saveProduct(@Valid @ModelAttribute("product") Product product,
                              BindingResult result,
                              @RequestParam("categoryId") Long categoryId,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("pageTitle",
                    (product.getId() == null) ? "Thêm sản phẩm mới" : "Sửa sản phẩm");
            return "products/form";
        }
        try {
            Category category = categoryService.findById(categoryId);
            product.setCategory(category);
            productService.save(product);
            String msg = (product.getId() == null)
                    ? "Thêm sản phẩm thành công!"
                    : "Cập nhật sản phẩm thành công!";
            redirectAttributes.addFlashAttribute("successMessage", msg);
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/products";
    }

    // ========================
    // GET /products/delete/{id} — Xóa
    // ========================
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa sản phẩm thành công!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Không thể xóa sản phẩm. " + e.getMessage());
        }
        return "redirect:/products";
    }
}
