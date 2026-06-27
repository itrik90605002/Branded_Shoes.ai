package com.storeagent.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @Column(name = "order_id", length = 50)
    private String orderId;

    @Column(name = "product_id", nullable = false, length = 50)
    private String productId;

    @Column(nullable = false, length = 50)
    private String status;

    @Column(name = "payment_status", length = 50)
    private String paymentStatus;

    @Column(name = "delivery_date", nullable = false)
    private LocalDate deliveryDate;

    @Column(nullable = false, length = 50)
    private String username;

    public Order() {}

    public Order(String orderId, String productId, String status, String paymentStatus, LocalDate deliveryDate, String username) {
        this.orderId = orderId;
        this.productId = productId;
        this.status = status;
        this.paymentStatus = paymentStatus;
        this.deliveryDate = deliveryDate;
        this.username = username;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public static OrderBuilder builder() {
        return new OrderBuilder();
    }

    public static class OrderBuilder {
        private String orderId;
        private String productId;
        private String status;
        private String paymentStatus;
        private LocalDate deliveryDate;
        private String username;

        public OrderBuilder orderId(String orderId) {
            this.orderId = orderId;
            return this;
        }

        public OrderBuilder productId(String productId) {
            this.productId = productId;
            return this;
        }

        public OrderBuilder status(String status) {
            this.status = status;
            return this;
        }

        public OrderBuilder paymentStatus(String paymentStatus) {
            this.paymentStatus = paymentStatus;
            return this;
        }

        public OrderBuilder deliveryDate(LocalDate deliveryDate) {
            this.deliveryDate = deliveryDate;
            return this;
        }

        public OrderBuilder username(String username) {
            this.username = username;
            return this;
        }

        public Order build() {
            return new Order(orderId, productId, status, paymentStatus, deliveryDate, username);
        }
    }
}
