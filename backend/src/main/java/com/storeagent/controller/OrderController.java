package com.storeagent.controller;

import com.storeagent.entity.Order;
import com.storeagent.entity.Product;
import com.storeagent.repository.OrderRepository;
import com.storeagent.repository.ProductRepository;
import com.storeagent.dto.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
@SuppressWarnings("all")
public class OrderController {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderController(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @GetMapping
    public ResponseEntity<?> getMyOrders() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Order> orders = orderRepository.findByUsername(username);
        return ResponseEntity.ok(orders);
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestParam String productId, @RequestParam(defaultValue = "PAID (VERIFIED)") String paymentStatus) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Product not found."));
        }

        if (product.getStockCount() <= 0) {
            return ResponseEntity.badRequest().body(new MessageResponse("Product is out of stock."));
        }

        // Decrement stock count
        product.setStockCount(product.getStockCount() - 1);
        productRepository.save(product);

        // Generate dynamic order details
        Random random = new Random();
        int orderNum = 1000 + random.nextInt(9000);
        String orderId = "ORD-" + orderNum;

        // Create the order with "Processing" status and specified paymentStatus
        Order order = Order.builder()
                .orderId(orderId)
                .productId(productId)
                .status("Processing") // Live tracking starts as "Processing"
                .paymentStatus(paymentStatus)
                .deliveryDate(LocalDate.now().plusDays(4)) // 4 days delivery
                .username(username)
                .build();

        orderRepository.save(order);

        System.out.println("[Order Pipeline] Manual purchase processed. User: " + username + ", Order ID: " + orderId + ", Payment Status: " + paymentStatus);

        return ResponseEntity.ok(order);
    }
}
