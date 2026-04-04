package com.example.b8j2ee.controller;

import com.example.b8j2ee.dto.CartItem;
import com.example.b8j2ee.entity.Account;
import com.example.b8j2ee.entity.Order;
import com.example.b8j2ee.service.AccountService;
import com.example.b8j2ee.service.OrderService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Map;

@Controller
@RequestMapping("/checkout")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final AccountService accountService;

    @GetMapping
    public String showCheckoutPage(HttpSession session, Model model, Principal principal) {
        Map<Integer, CartItem> cart = (Map<Integer, CartItem>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            return "redirect:/cart";
        }

        BigDecimal totalAmount = cart.values().stream()
                .map(CartItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (principal != null) {
            accountService.findByUsername(principal.getName()).ifPresent(account -> {
                model.addAttribute("name", account.getFullName());
                model.addAttribute("phone", "");
                model.addAttribute("address", "");
            });
        }

        model.addAttribute("cartItems", cart.values());
        model.addAttribute("totalAmount", totalAmount);

        return "order/checkout";
    }

    @PostMapping
    public String processCheckout(@RequestParam("name") String name,
                                  @RequestParam("phone") String phone,
                                  @RequestParam("address") String address,
                                  HttpSession session,
                                  Principal principal,
                                  RedirectAttributes redirectAttributes) {

        Map<Integer, CartItem> cart = (Map<Integer, CartItem>) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Giỏ hàng trống!");
            return "redirect:/cart";
        }

        Account account = accountService.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Phải đăng nhập để đặt hàng"));

        try {
            Order order = orderService.placeOrder(account, cart, name, phone, address);
            session.removeAttribute("cart");
            redirectAttributes.addFlashAttribute("successMessage", "Đặt hàng thành công! Mã đơn: #" + order.getId());
            return "redirect:/products";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi: " + e.getMessage());
            return "redirect:/checkout";
        }
    }
}
