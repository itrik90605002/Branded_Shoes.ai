package com.storeagent.service;

import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class ExternalPlatformService {

    private final Random random = new Random();

    public Map<String, Object> scrapePlatformData(String brand, String name, String color, int size) {
        Map<String, Object> result = new HashMap<>();
        
        // Simulating network delays (e.g. 100ms)
        try { Thread.sleep(100); } catch (InterruptedException ignored) {}

        // Mock Amazon Scraping
        double amazonBasePrice = 1200 + random.nextInt(8000);
        double amazonRating = 4.0 + (random.nextInt(10) / 10.0);
        int amazonReviews = 50 + random.nextInt(1000);
        
        // Mock Flipkart Scraping
        double flipkartPrice = amazonBasePrice - (100 + random.nextInt(400));
        
        // Mock Myntra stock
        int myntraStock = 2 + random.nextInt(20);
        
        // Mock Reddit sentiment analyzer
        int redditMentions = 10 + random.nextInt(200);
        double positiveRatio = 0.6 + (random.nextInt(40) / 100.0); // 60% to 100% positive
        int trendingScore = (int) (positiveRatio * 100);

        List<String> mockRedditReviews = Arrays.asList(
                "Love the fit of these " + brand + " shoes! Size " + size + " fits perfectly.",
                "The color " + color + " looks sick. Highly recommend it.",
                "Quality of " + name + " is amazing for the price.",
                "A bit tight at size " + size + ", maybe size up. But cushioning is awesome.",
                "Cheaper on Flipkart than Amazon but definitely worth buying."
        );
        Collections.shuffle(mockRedditReviews);

        result.put("amazonPrice", amazonBasePrice);
        result.put("amazonRating", amazonRating);
        result.put("amazonReviews", amazonReviews);
        
        result.put("flipkartPrice", flipkartPrice);
        
        result.put("myntraStock", myntraStock);
        
        result.put("redditMentions", redditMentions);
        result.put("redditTrendingScore", trendingScore);
        result.put("redditRecentReviews", mockRedditReviews.subList(0, 2));

        return result;
    }
}
