package com.storeagent.service;

import com.storeagent.entity.Product;
import com.storeagent.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@SuppressWarnings("all")
public class ProductSyncService {

    private final ExternalPlatformService externalPlatformService;
    private final ProductRepository productRepository;

    public ProductSyncService(ExternalPlatformService externalPlatformService, ProductRepository productRepository) {
        this.externalPlatformService = externalPlatformService;
        this.productRepository = productRepository;
    }

    @Transactional
    public List<String> syncProductWithExternalPlatforms(Product product) {
        List<String> syncLogs = new ArrayList<>();
        
        syncLogs.add(String.format("[Platform Sync] Initiating synchronization for: %s (Brand: %s, Color: %s, Size: %d)",
                product.getName(), product.getBrand(), product.getColor(), product.getSize()));

        // Scrape platforms
        Map<String, Object> scrapedData = externalPlatformService.scrapePlatformData(
                product.getBrand(), product.getName(), product.getColor(), product.getSize());

        double amazonPrice = (double) scrapedData.get("amazonPrice");
        double amazonRating = (double) scrapedData.get("amazonRating");
        int amazonReviews = (int) scrapedData.get("amazonReviews");
        double flipkartPrice = (double) scrapedData.get("flipkartPrice");
        int myntraStock = (int) scrapedData.get("myntraStock");
        int redditTrendingScore = (int) scrapedData.get("redditTrendingScore");
        @SuppressWarnings("unchecked")
        List<String> redditReviews = (List<String>) scrapedData.get("redditRecentReviews");

        syncLogs.add(String.format("[Platform Sync] Amazon Scraper: Found price ₹%.2f, rating %.1f (%d reviews)",
                amazonPrice, amazonRating, amazonReviews));
        syncLogs.add(String.format("[Platform Sync] Flipkart Scraper: Found discount price ₹%.2f", flipkartPrice));
        syncLogs.add(String.format("[Platform Sync] Myntra Inventory Feed: Live stock count is %d", myntraStock));
        syncLogs.add(String.format("[Platform Sync] Reddit Sentiment Analyzer: Detected %d mentions, Trending Score: %d%%",
                scrapedData.get("redditMentions"), redditTrendingScore));
        
        for (String review : redditReviews) {
            syncLogs.add(String.format("[Platform Sync] Reddit Discussion Parse: \"%s\"", review));
        }

        // Sync database entity
        // We will average out the prices or use the cheapest one (e.g. Flipkart) for competitive shopkeeping!
        double localOldPrice = product.getPrice();
        double newPrice = Math.min(amazonPrice, flipkartPrice);
        
        product.setPrice(newPrice);
        product.setRating(amazonRating);
        product.setReviewCount(amazonReviews);
        product.setStockCount(myntraStock);
        product.setTrendingScore(redditTrendingScore);

        productRepository.save(product);

        syncLogs.add(String.format("[Database Update] Merged e-commerce records. Price synced: ₹%.2f -> ₹%.2f. Stock: %d. Rating: %.1f",
                localOldPrice, newPrice, myntraStock, amazonRating));

        return syncLogs;
    }
}
