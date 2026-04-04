package com.example.b8j2ee.service;

import com.example.b8j2ee.dto.CartItem;
import com.example.b8j2ee.entity.Account;
import com.example.b8j2ee.entity.Order;
import com.example.b8j2ee.entity.OrderDetail;
import com.example.b8j2ee.entity.Product;
import com.example.b8j2ee.repository.OrderRepository;
import com.example.b8j2ee.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Order placeOrder(Account account, Map<Integer, CartItem> cartItems, String name, String phone, String address) {
        if (cartItems == null || cartItems.isEmpty()) {
            throw new RuntimeException("Giỏ hàng trống!");
        }

        Order order = new Order();
        order.setAccount(account);
        order.setCustomerName(name);
        order.setCustomerPhone(phone);
        order.setShippingAddress(address);
        order.setStatus("PROCESSING");
        order.setOrderDate(LocalDateTime.now());
        
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CartItem item : cartItems.values()) {
            Product product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setProduct(product);
            detail.setQuantity(item.getQuantity());
            detail.setPrice(product.getPrice());

            order.getOrderDetails().add(detail);
            totalAmount = totalAmount.add(detail.getSubtotal());
        }

        order.setTotalAmount(totalAmount);
        
        return orderRepository.save(order);
    }
}
