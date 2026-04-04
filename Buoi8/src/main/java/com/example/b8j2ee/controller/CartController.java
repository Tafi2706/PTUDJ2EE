package com.example.b8j2ee.controller;

import com.example.b8j2ee.dto.CartItem;
import com.example.b8j2ee.entity.Product;
import com.example.b8j2ee.service.ProductService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final ProductService productService;

    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        Map<Integer, CartItem> cart = (Map<Integer, CartItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
        }
        
        BigDecimal totalAmount = cart.values().stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        model.addAttribute("cartItems", cart.values());
        model.addAttribute("totalAmount", totalAmount);
        return "cart/cart";
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam("productId") Long productId,
                            @RequestParam(value = "quantity", defaultValue = "1") int quantity,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        
        Product product = productService.findById(productId).orElse(null);
        if (product != null) {
            Map<Integer, CartItem> cart = (Map<Integer, CartItem>) session.getAttribute("cart");
            if (cart == null) {
                cart = new HashMap<>();
            }

            int id = product.getId().intValue();
            if (cart.containsKey(id)) {
                CartItem item = cart.get(id);
                item.increaseQuantity(quantity);
            } else {
                CartItem item = new CartItem(product.getId(), product.getName(), product.getPrice(), quantity, product.getImage());
                cart.put(id, item);
            }
            session.setAttribute("cart", cart);
            redirectAttributes.addFlashAttribute("successMessage", "Đã thêm " + product.getName() + " vào giỏ hàng");
        }
        return "redirect:/products";
    }

    @PostMapping("/remove/{id}")
    public String removeFromCart(@PathVariable("id") Integer id, HttpSession session) {
        Map<Integer, CartItem> cart = (Map<Integer, CartItem>) session.getAttribute("cart");
        if (cart != null) {
            cart.remove(id);
            session.setAttribute("cart", cart);
        }
        return "redirect:/cart";
    }
}
