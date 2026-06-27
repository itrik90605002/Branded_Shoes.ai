package com.storeagent.repository;

import com.storeagent.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, String> {
    Optional<Order> findByOrderId(String orderId);
    List<Order> findByUsername(String username);
}
