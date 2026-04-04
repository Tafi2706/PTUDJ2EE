package com.example.b8j2ee.repository;

import com.example.b8j2ee.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByAccountIdOrderByOrderDateDesc(Long accountId);
}
