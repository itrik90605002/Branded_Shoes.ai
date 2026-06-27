package com.storeagent.agent.tools;

import com.storeagent.entity.Product;
import com.storeagent.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.Optional;

@Component
@SuppressWarnings("all")
public class ProductTool implements Tool {

    private final ProductRepository productRepository;

    public ProductTool(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public String getName() {
        return "ProductTool";
    }

    @Override
    public String getDescription() {
        return "Get detailed specifications of a specific shoe by Product ID (e.g. category, name, price, stock).";
    }

    @Override
    public ToolResult execute(Map<String, Object> parameters) {
        String productId = (String) parameters.get("productId");

        if (productId == null) {
            return ToolResult.builder()
                    .success(false)
                    .output("Missing Product ID parameter.")
                    .build();
        }

        productId = productId.trim().toUpperCase();

        Optional<Product> productOpt = productRepository.findById(productId);
        if (productOpt.isEmpty()) {
            return ToolResult.builder()
                    .success(true)
                    .output(String.format("Product with ID '%s' was not found in our catalog.", productId))
                    .build();
        }

        Product p = productOpt.get();
        String output = String.format("Product Details for '%s':\n- Name: %s\n- Brand: %s\n- Price: ₹%.2f\n- Color: %s\n- Size: %d\n- Stock: %d left\n- Rating: %.1f/5 stars (%d reviews)\n- Description: %s",
                p.getProductId(), p.getName(), p.getBrand(), p.getPrice(), p.getColor(), p.getSize(), p.getStockCount(), p.getRating(), p.getReviewCount(), p.getDescription());

        return ToolResult.builder()
                .success(true)
                .output(output)
                .data(p)
                .build();
    }
}
