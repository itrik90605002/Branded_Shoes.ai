package com.storeagent.agent.tools;

import com.storeagent.entity.Product;
import com.storeagent.repository.ProductRepository;
import com.storeagent.service.ProductSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
@SuppressWarnings("all")
public class ProductSearchTool implements Tool {

    private final ProductRepository productRepository;
    private final ProductSyncService productSyncService;

    public ProductSearchTool(ProductRepository productRepository, ProductSyncService productSyncService) {
        this.productRepository = productRepository;
        this.productSyncService = productSyncService;
    }

    // We will use a thread-local list or return it in the result so the orchestrator can capture syncing logs
    private static final ThreadLocal<List<String>> syncLogsThreadLocal = ThreadLocal.withInitial(ArrayList::new);

    public static List<String> getAndClearSyncLogs() {
        List<String> logs = new ArrayList<>(syncLogsThreadLocal.get());
        syncLogsThreadLocal.get().clear();
        return logs;
    }

    @Override
    public String getName() {
        return "ProductSearchTool";
    }

    @Override
    public String getDescription() {
        return "Search available shoes in the catalog by Brand (Campus, Nike, Skechers, Puma, Sparx), Color, and Size.";
    }

    @Override
    public ToolResult execute(Map<String, Object> parameters) {
        syncLogsThreadLocal.get().clear();

        String brand = (String) parameters.get("brand");
        String color = (String) parameters.get("color");
        Object sizeObj = parameters.get("size");

        if (brand == null || color == null || sizeObj == null) {
            return ToolResult.builder()
                    .success(false)
                    .output("Missing query parameters. Searching requires: brand, color, and size.")
                    .build();
        }

        Integer size;
        try {
            if (sizeObj instanceof Number) {
                size = ((Number) sizeObj).intValue();
            } else {
                size = Integer.parseInt(sizeObj.toString());
            }
        } catch (NumberFormatException e) {
            return ToolResult.builder()
                    .success(false)
                    .output("Invalid shoe size format: " + sizeObj)
                    .build();
        }

        List<Product> products = productRepository.findByBrandIgnoreCaseAndColorIgnoreCaseAndSize(brand, color, size);

        if (products.isEmpty()) {
            return ToolResult.builder()
                    .success(true)
                    .output(String.format("No exact matches found for brand '%s', color '%s', size %d in the inventory.", brand, color, size))
                    .data(Collections.emptyList())
                    .build();
        }

        // Simulating live scraping syncing for found products to keep database up to date!
        StringBuilder output = new StringBuilder("Found available matches in stock:\n");
        for (Product product : products) {
            // Live sync data
            List<String> logs = productSyncService.syncProductWithExternalPlatforms(product);
            syncLogsThreadLocal.get().addAll(logs);

            output.append(String.format("- ID: %s | %s %s (Size %d) | Price: ₹%.2f | Stock: %d left | Rating: %.1f stars | Trending Score: %d%%\n",
                    product.getProductId(), product.getBrand(), product.getName(), product.getSize(),
                    product.getPrice(), product.getStockCount(), product.getRating(), product.getTrendingScore()));
        }

        return ToolResult.builder()
                .success(true)
                .output(output.toString())
                .data(products)
                .build();
    }
}
