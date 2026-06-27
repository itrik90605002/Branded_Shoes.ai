package com.storeagent.controller;

import com.storeagent.dto.MessageResponse;
import com.storeagent.entity.Product;
import com.storeagent.repository.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/products")
@CrossOrigin(origins = "*")
@SuppressWarnings("all")
public class AdminController {

    private final ProductRepository productRepository;

    public AdminController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @PostMapping
    public ResponseEntity<?> addProduct(@Valid @RequestBody Product product) {
        if (product.getProductId() == null || product.getProductId().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Product ID is required."));
        }
        
        // Sanitize product details
        product.setProductId(product.getProductId().trim().toUpperCase());
        product.setBrand(product.getBrand().trim());
        product.setName(product.getName().trim());
        product.setColor(product.getColor().trim());
        product.setCategory("Shoes"); // Always shoes in this store

        // Set default metrics if omitted
        if (product.getRating() == null) product.setRating(4.0);
        if (product.getReviewCount() == null) product.setReviewCount(0);
        if (product.getTrendingScore() == null) product.setTrendingScore(50);
        if (product.getStockCount() == null) product.setStockCount(5);

        if (productRepository.existsById(product.getProductId())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Product ID already exists in inventory."));
        }

        productRepository.save(product);
        return ResponseEntity.ok(new MessageResponse(String.format("Successfully added '%s %s' to inventory.", 
                product.getBrand(), product.getName())));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") String id) {
        String prodId = id.trim().toUpperCase();
        Optional<Product> productOpt = productRepository.findById(prodId);
        
        if (productOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponse(String.format("Product with ID '%s' not found.", prodId)));
        }

        productRepository.delete(productOpt.get());
        return ResponseEntity.ok(new MessageResponse(String.format("Product '%s' has been successfully removed from inventory.", prodId)));
    }
}
