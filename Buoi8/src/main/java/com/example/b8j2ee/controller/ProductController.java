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

import java.util.List;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping
    public String listProducts(Model model,
                               @RequestParam(required = false) String keyword,
                               @RequestParam(required = false) Long categoryId,
                               @RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "asc") String sortDir) {

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

        return "product/list";
    }
}
