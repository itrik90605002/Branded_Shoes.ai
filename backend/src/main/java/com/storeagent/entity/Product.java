package com.storeagent.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @Column(name = "product_id", length = 50)
    private String productId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 50)
    private String brand;

    @Column(nullable = false, length = 50)
    private String category;

    @Column(nullable = false, length = 30)
    private String color;

    @Column(nullable = false)
    private Integer size;

    @Column(nullable = false)
    private Double price;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column
    private Double rating;

    @Column(name = "review_count")
    private Integer reviewCount;

    @Column(name = "stock_count")
    private Integer stockCount;

    @Column(name = "trending_score")
    private Integer trendingScore;

    public Product() {}

    public Product(String productId, String name, String brand, String category, String color, Integer size, Double price, String description, Double rating, Integer reviewCount, Integer stockCount, Integer trendingScore) {
        this.productId = productId;
        this.name = name;
        this.brand = brand;
        this.category = category;
        this.color = color;
        this.size = size;
        this.price = price;
        this.description = description;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.stockCount = stockCount;
        this.trendingScore = trendingScore;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Integer getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(Integer reviewCount) {
        this.reviewCount = reviewCount;
    }

    public Integer getStockCount() {
        return stockCount;
    }

    public void setStockCount(Integer stockCount) {
        this.stockCount = stockCount;
    }

    public Integer getTrendingScore() {
        return trendingScore;
    }

    public void setTrendingScore(Integer trendingScore) {
        this.trendingScore = trendingScore;
    }

    public static ProductBuilder builder() {
        return new ProductBuilder();
    }

    public static class ProductBuilder {
        private String productId;
        private String name;
        private String brand;
        private String category;
        private String color;
        private Integer size;
        private Double price;
        private String description;
        private Double rating;
        private Integer reviewCount;
        private Integer stockCount;
        private Integer trendingScore;

        public ProductBuilder productId(String productId) {
            this.productId = productId;
            return this;
        }

        public ProductBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ProductBuilder brand(String brand) {
            this.brand = brand;
            return this;
        }

        public ProductBuilder category(String category) {
            this.category = category;
            return this;
        }

        public ProductBuilder color(String color) {
            this.color = color;
            return this;
        }

        public ProductBuilder size(Integer size) {
            this.size = size;
            return this;
        }

        public ProductBuilder price(Double price) {
            this.price = price;
            return this;
        }

        public ProductBuilder description(String description) {
            this.description = description;
            return this;
        }

        public ProductBuilder rating(Double rating) {
            this.rating = rating;
            return this;
        }

        public ProductBuilder reviewCount(Integer reviewCount) {
            this.reviewCount = reviewCount;
            return this;
        }

        public ProductBuilder stockCount(Integer stockCount) {
            this.stockCount = stockCount;
            return this;
        }

        public ProductBuilder trendingScore(Integer trendingScore) {
            this.trendingScore = trendingScore;
            return this;
        }

        public Product build() {
            return new Product(productId, name, brand, category, color, size, price, description, rating, reviewCount, stockCount, trendingScore);
        }
    }
}
