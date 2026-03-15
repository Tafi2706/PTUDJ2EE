package com.nguyenthanhtai.lab05.controller;

import com.nguyenthanhtai.lab05.model.Book;
import com.nguyenthanhtai.lab05.model.Category;
import com.nguyenthanhtai.lab05.service.BookService;
import com.nguyenthanhtai.lab05.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final CategoryService categoryService;

    // ========================
    // GET /books — Show list
    // ========================
    @GetMapping
    public String listBooks(Model model) {
        List<Book> books = bookService.findAll();
        model.addAttribute("books", books);
        return "books/list";
    }

    // ========================
    // GET /books/add — Show add form
    // ========================
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("pageTitle", "Add New Book");
        return "books/add";
    }

    // ========================
    // GET /books/edit/{id} — Show edit form
    // ========================
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model,
                               RedirectAttributes redirectAttributes) {
        try {
            Book book = bookService.findById(id);
            model.addAttribute("book", book);
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("pageTitle", "Edit Book: " + book.getTitle());
            return "books/add";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/books";
        }
    }

    // ========================
    // POST /books/save — Save (create or update)
    // ========================
    @PostMapping("/save")
    public String saveBook(@Valid @ModelAttribute("book") Book book,
                           BindingResult result,
                           @RequestParam("categoryId") Long categoryId,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("categories", categoryService.findAll());
            model.addAttribute("pageTitle",
                    (book.getId() == null) ? "Add New Book" : "Edit Book");
            return "books/add";
        }
        try {
            Category category = categoryService.findById(categoryId);
            book.setCategory(category);
            bookService.save(book);
            String msg = (book.getId() == null)
                    ? "Book added successfully!"
                    : "Book updated successfully!";
            redirectAttributes.addFlashAttribute("successMessage", msg);
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/books";
    }

    // ========================
    // GET /books/delete/{id} — Delete book
    // ========================
    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            bookService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Book deleted successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Cannot delete book. " + e.getMessage());
        }
        return "redirect:/books";
    }
}
