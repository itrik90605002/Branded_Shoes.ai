package com.storeagent.agent.tools;

import com.storeagent.entity.Order;
import com.storeagent.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.Optional;

@Component
@SuppressWarnings("all")
public class OrderTool implements Tool {

    private final OrderRepository orderRepository;

    public OrderTool(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public String getName() {
        return "OrderTool";
    }

    @Override
    public String getDescription() {
        return "Get details of an order including status, product ID, and expected delivery date. Requires Order ID.";
    }

    @Override
    public ToolResult execute(Map<String, Object> parameters) {
        String orderId = (String) parameters.get("orderId");
        String username = (String) parameters.get("username");
        String role = (String) parameters.get("role");

        if (orderId == null) {
            return ToolResult.builder()
                    .success(false)
                    .output("Missing Order ID parameter.")
                    .build();
        }

        // Standardize input ID
        orderId = orderId.trim().toUpperCase();

        Optional<Order> orderOpt = orderRepository.findByOrderId(orderId);
        if (orderOpt.isEmpty()) {
            return ToolResult.builder()
                    .success(true)
                    .output(String.format("Order with ID '%s' was not found in our database.", orderId))
                    .build();
        }

        Order order = orderOpt.get();

        // Security check: Customer can only view their own orders. Admin can view any order.
        if (!"ADMIN".equals(role) && !order.getUsername().equalsIgnoreCase(username)) {
            return ToolResult.builder()
                    .success(false)
                    .output(String.format("Security Restriction: You do not have permission to access Order details for '%s'.", orderId))
                    .build();
        }

        String output = String.format("Order Status for '%s':\n- Product ID: %s\n- Shipping Status: %s\n- Payment Status: %s\n- Expected Delivery: %s\n- Customer Username: %s",
                order.getOrderId(), order.getProductId(), order.getStatus(), order.getPaymentStatus(), order.getDeliveryDate(), order.getUsername());

        return ToolResult.builder()
                .success(true)
                .output(output)
                .data(order)
                .build();
    }
}
