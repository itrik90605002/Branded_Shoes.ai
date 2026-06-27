package com.storeagent.agent.llm;

import org.springframework.stereotype.Service;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class FallbackLlmService implements LlmService {

    @Override
    public boolean isAvailable() {
        return true; // Fallback is always available locally
    }

    @Override
    public String parseQuery(String userQuery) {
        String query = userQuery.toLowerCase();

        String intent = "SEARCH_PRODUCT";
        String brand = null;
        String color = null;
        Integer size = null;
        String orderId = null;

        // Extract Order ID (e.g. ORD-1002)
        Pattern orderPattern = Pattern.compile("ORD-\\d+", Pattern.CASE_INSENSITIVE);
        Matcher orderMatcher = orderPattern.matcher(query);
        if (orderMatcher.find()) {
            orderId = orderMatcher.group().toUpperCase();
        }

        // Determine Intent
        if (query.contains("cheaper") || query.contains("alternative") || query.contains("better option")) {
            intent = "CHEAPER_ALTERNATIVE";
        } else if (orderId != null || query.contains("status") || query.contains("where") || query.contains("track") || query.contains("delivery")) {
            intent = "TRACK_ORDER";
        } else if (query.contains("recommend") || query.contains("trending") || query.contains("popular") || query.contains("best") || query.contains("rated") || query.contains("positive")) {
            intent = "RECOMMEND";
        }

        // Extract Brand
        if (query.contains("nike")) {
            brand = "Nike";
        } else if (query.contains("puma")) {
            brand = "Puma";
        } else if (query.contains("campus")) {
            brand = "Campus";
        } else if (query.contains("skechers") || query.contains("sketchers")) {
            brand = "Skechers";
        } else if (query.contains("sparx") || query.contains("sparks")) {
            brand = "Sparx";
        }

        // Extract Color
        if (query.contains("black")) {
            color = "Black";
        } else if (query.contains("white")) {
            color = "White";
        } else if (query.contains("red")) {
            color = "Red";
        } else if (query.contains("blue")) {
            color = "Blue";
        } else if (query.contains("grey") || query.contains("gray")) {
            color = "Grey";
        }

        // Extract Size (looking for a number like 7, 8, 9, 10, or "size 8")
        // We look for "size \d+" or general numbers that are between 5 and 15 (realistic shoe sizes)
        Pattern sizePattern = Pattern.compile("\\b(size\\s+)?([5-9]|1[0-5])\\b");
        Matcher sizeMatcher = sizePattern.matcher(query);
        if (sizeMatcher.find()) {
            try {
                // If it matched group(2), it's the actual number
                String numStr = sizeMatcher.group(2);
                size = Integer.parseInt(numStr);
            } catch (NumberFormatException ignored) {}
        }

        // Format as JSON
        return String.format(
                "{\"intent\":\"%s\",\"brand\":%s,\"color\":%s,\"size\":%s,\"orderId\":%s}",
                intent,
                brand != null ? "\"" + brand + "\"" : "null",
                color != null ? "\"" + color + "\"" : "null",
                size != null ? size : "null",
                orderId != null ? "\"" + orderId + "\"" : "null"
        );
    }

    @Override
    public String synthesizeResponse(String userQuery, String parsedIntent, String toolExecutionOutcome) {
        // Build a robust local response based on the intent
        StringBuilder response = new StringBuilder();
        
        if ("TRACK_ORDER".equalsIgnoreCase(parsedIntent)) {
            if (toolExecutionOutcome.contains("not found")) {
                response.append("I checked our warehouse logs, but I couldn't find any order matching that ID. Please check the spelling or Order ID and try again.");
            } else if (toolExecutionOutcome.contains("Security Restriction")) {
                response.append("I apologize, but for privacy reasons, I can only search for orders placed under your own account. Let me know if there is anything else I can help you with.");
            } else {
                response.append("I looked up your order. Here is the current status directly from our shipping logs:\n\n")
                        .append(toolExecutionOutcome)
                        .append("\n\nIs there anything else I can help you check?");
            }
        } 
        else if ("CHEAPER_ALTERNATIVE".equalsIgnoreCase(parsedIntent)) {
            if (toolExecutionOutcome.contains("not found")) {
                response.append("I couldn't locate that order to find a cheaper alternative. Make sure you entered the correct Order ID.");
            } else if (toolExecutionOutcome.contains("No cheaper alternative")) {
                response.append("I searched our database, but it looks like you already bought the cheapest option available in that category! Kudos to you for finding the best deal.");
            } else {
                response.append("Yes, I can help you save some money! Here is what I found on our shelves:\n\n")
                        .append(toolExecutionOutcome)
                        .append("\n\nWould you like me to reserve a pair for you?");
            }
        } 
        else if ("RECOMMEND".equalsIgnoreCase(parsedIntent)) {
            response.append("Welcome! As the shopkeeper here, I always recommend our top-sellers. Here are our most popular and positive-reviewed brand shoes from the database:\n\n")
                    .append(toolExecutionOutcome)
                    .append("\n\nLet me know if any of these catch your eye!");
        } 
        else if ("SEARCH_PRODUCT".equalsIgnoreCase(parsedIntent)) {
            if (toolExecutionOutcome.contains("No exact matches")) {
                response.append("I don't have that exact combination in stock right now. However, let me recommend some of our highest-rated trending options that are currently in stock:\n\n")
                        .append(toolExecutionOutcome);
            } else {
                response.append("I checked our inventory shelves! Here is the availability matching your search:\n\n")
                        .append(toolExecutionOutcome)
                        .append("\n\nI can put these aside for you if you'd like to make a purchase!");
            }
        } 
        else {
            response.append("I'm not sure I understand that query. As a shoe store assistant, you can ask me to track orders (e.g., 'where is my order ORD-1002'), search for specific shoe availability (e.g., 'show black Puma shoes size 9'), or recommend our most trending shoes!");
        }

        return response.toString();
    }
}
