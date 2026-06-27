package com.storeagent.agent.tools;

import com.storeagent.entity.Product;
import com.storeagent.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.*;
import java.util.stream.Collectors;

@Component
@SuppressWarnings("all")
public class RecommendationTool implements Tool {

    private final ProductRepository productRepository;

    public RecommendationTool(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public String getName() {
        return "RecommendationTool";
    }

    @Override
    public String getDescription() {
        return "Fetch recommended shoes from our database based on high reviews (ratings) or trending scores. Optional brand filter can be applied.";
    }

    @Override
    public ToolResult execute(Map<String, Object> parameters) {
        String type = (String) parameters.getOrDefault("type", "trending"); // "trending" or "rating"
        String brand = (String) parameters.get("brand"); // Optional filter

        List<Product> products;
        
        if ("rating".equalsIgnoreCase(type)) {
            products = productRepository.findTop5ByOrderByRatingDesc();
        } else {
            products = productRepository.findTop5ByOrderByTrendingScoreDesc();
        }

        // Apply brand filter if specified
        if (brand != null && !brand.trim().isEmpty()) {
            products = products.stream()
                    .filter(p -> p.getBrand().equalsIgnoreCase(brand.trim()))
                    .collect(Collectors.toList());
            
            // If filtering leaves it empty, get brand specific shoes directly
            if (products.isEmpty()) {
                products = productRepository.findByBrandIgnoreCase(brand.trim());
                // Sort them manually in code
                if ("rating".equalsIgnoreCase(type)) {
                    products.sort(Comparator.comparing(Product::getRating).reversed());
                } else {
                    products.sort(Comparator.comparing(Product::getTrendingScore).reversed());
                }
                if (products.size() > 5) {
                    products = products.subList(0, 5);
                }
            }
        }

        if (products.isEmpty()) {
            return ToolResult.builder()
                    .success(true)
                    .output("No recommendations found.")
                    .data(Collections.emptyList())
                    .build();
        }

        StringBuilder output = new StringBuilder();
        output.append(String.format("Recommended Branded Shoes (Based on %s):\n", type.toUpperCase()));
        for (Product p : products) {
            output.append(String.format("- %s %s (Size %d, %s) | Price: ₹%.2f | Rating: %.1f/5 (%d reviews) | Trend Rank: %d%%\n",
                    p.getBrand(), p.getName(), p.getSize(), p.getColor(), p.getPrice(), p.getRating(), p.getReviewCount(), p.getTrendingScore()));
        }

        return ToolResult.builder()
                .success(true)
                .output(output.toString())
                .data(products)
                .build();
    }
}
