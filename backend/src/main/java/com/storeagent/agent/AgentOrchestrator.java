package com.storeagent.agent;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.storeagent.agent.llm.FallbackLlmService;
import com.storeagent.agent.llm.GroqLlmService;
import com.storeagent.agent.llm.LlmService;
import com.storeagent.agent.tools.*;
import com.storeagent.dto.AgentResponse;
import com.storeagent.entity.Order;
import com.storeagent.entity.Product;
import com.storeagent.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@SuppressWarnings("all")
public class AgentOrchestrator {

    private final GroqLlmService groqLlmService;
    private final FallbackLlmService fallbackLlmService;
    private final OrderTool orderTool;
    private final ProductTool productTool;
    private final ProductSearchTool productSearchTool;
    private final RecommendationTool recommendationTool;
    private final ProductRepository productRepository;

    public AgentOrchestrator(
            GroqLlmService groqLlmService,
            FallbackLlmService fallbackLlmService,
            OrderTool orderTool,
            ProductTool productTool,
            ProductSearchTool productSearchTool,
            RecommendationTool recommendationTool,
            ProductRepository productRepository) {
        this.groqLlmService = groqLlmService;
        this.fallbackLlmService = fallbackLlmService;
        this.orderTool = orderTool;
        this.productTool = productTool;
        this.productSearchTool = productSearchTool;
        this.recommendationTool = recommendationTool;
        this.productRepository = productRepository;
    }

    private final ObjectMapper objectMapper = new ObjectMapper();

    // Contextual chat memory map
    private final Map<String, List<Map<String, String>>> userMemoryMap = new java.util.concurrent.ConcurrentHashMap<>();

    public void clearUserHistory(String username) {
        userMemoryMap.remove(username);
    }

    public AgentResponse processQuery(String userQuery, String username, String role) {
        List<String> logs = new ArrayList<>();
        long startTime = System.currentTimeMillis();

        logs.add("[Orchestrator] Received user query: \"" + userQuery + "\"");

        // 1. Choose LLM Engine
        LlmService llm = groqLlmService;
        if (groqLlmService.isAvailable()) {
            logs.add("[LLM Selector] Groq API configuration found. Using Groq Llama-3 completions.");
        } else {
            llm = fallbackLlmService;
            logs.add("[LLM Selector] No Groq API key set. Falling back to Local Rule-Engine Parser.");
        }

        // Fetch context memory
        List<Map<String, String>> memory = userMemoryMap.computeIfAbsent(username, k -> new ArrayList<>());
        groqLlmService.setThreadHistory(memory);

        try {
            // 2. Parse Query (Intent & Parameter Extraction)
            logs.add("[LLM Parse] Extrapolating intent and entities...");
            String jsonParse = llm.parseQuery(userQuery);
            logs.add("[LLM Parse] Extracted Entity JSON: " + jsonParse);

            Map<String, Object> params = new HashMap<>();
            try {
                params = objectMapper.readValue(jsonParse, new TypeReference<Map<String, Object>>() {});
            } catch (Exception e) {
                logs.add("[Error] Failed parsing LLM extraction JSON: " + e.getMessage());
                params.put("intent", "UNKNOWN");
            }

            String intent = (String) params.getOrDefault("intent", "UNKNOWN");
            String brand = (String) params.get("brand");
            String color = (String) params.get("color");
            Object sizeObj = params.get("size");
            String orderId = (String) params.get("orderId");

            // standardizing parameters map for execution
            Map<String, Object> toolParams = new HashMap<>();
            toolParams.put("username", username);
            toolParams.put("role", role);

            String toolOutcome = "";

            // 3. Formulate Execution Plan & Run Tools
            if ("TRACK_ORDER".equalsIgnoreCase(intent)) {
                logs.add("[Planner] Intent Classified: TRACK_ORDER. Executing Order Search Plan.");
                if (orderId == null) {
                    logs.add("[Planner] Warning: Missing Order ID parameter. Attempting to parse manually.");
                    toolOutcome = "No Order ID provided. Unable to check status.";
                } else {
                    toolParams.put("orderId", orderId);
                    logs.add(String.format("[Plan Execution] Step 1: Running OrderTool for order '%s' under user '%s'.", orderId, username));
                    
                    long toolStart = System.currentTimeMillis();
                    ToolResult result = orderTool.execute(toolParams);
                    logs.add(String.format("[Tool Outcome] OrderTool finished in %d ms. Status: %b.", 
                            (System.currentTimeMillis() - toolStart), result.isSuccess()));
                    toolOutcome = result.getOutput();
                }
            } 
            else if ("CHEAPER_ALTERNATIVE".equalsIgnoreCase(intent)) {
                logs.add("[Planner] Intent Classified: CHEAPER_ALTERNATIVE. Executing Price Comparison Plan.");
                if (orderId == null) {
                    toolOutcome = "Please specify the Order ID to find a cheaper alternative (e.g. 'Is there a cheaper option for ORD-1002?').";
                    logs.add("[Planner] Cancelled: Missing Order ID.");
                } else {
                    // Step 1: Fetch Order Details
                    toolParams.put("orderId", orderId);
                    logs.add("[Plan Execution] Step 1: Querying Order Details using OrderTool...");
                    ToolResult orderResult = orderTool.execute(toolParams);
                    
                    if (!orderResult.isSuccess() || orderResult.getData() == null) {
                        logs.add("[Plan Execution] Step 1 Failed: Order not found or unauthorized access.");
                        toolOutcome = orderResult.getOutput();
                    } else {
                        Order order = (Order) orderResult.getData();
                        String boughtProductId = order.getProductId();
                        logs.add(String.format("[Plan Execution] Step 1 Success: Order contains Product '%s'.", boughtProductId));

                        // Step 2: Fetch Product Details
                        logs.add(String.format("[Plan Execution] Step 2: Fetching details for Product '%s' using ProductTool...", boughtProductId));
                        toolParams.put("productId", boughtProductId);
                        ToolResult productResult = productTool.execute(toolParams);

                        if (!productResult.isSuccess() || productResult.getData() == null) {
                            logs.add("[Plan Execution] Step 2 Failed: Product specs not found.");
                            toolOutcome = "Error loading original product details.";
                        } else {
                            Product boughtProduct = (Product) productResult.getData();
                            double boughtPrice = boughtProduct.getPrice();
                            String productCategory = boughtProduct.getCategory();
                            Integer productSize = boughtProduct.getSize();
                            
                            logs.add(String.format("[Plan Execution] Step 2 Success: Product Price is ₹%.2f, Category: %s, Size: %d", 
                                    boughtPrice, productCategory, productSize));

                            // Step 3: Find Cheaper Alternatives in Category & Size
                            logs.add(String.format("[Plan Execution] Step 3: Querying database for cheaper alternative products in Category '%s', Size %d...", 
                                    productCategory, productSize));
                            
                            // We will query products of same category and size
                            List<Product> allProducts = productRepository.findByCategoryIgnoreCase(productCategory);
                            List<Product> cheaperAlternatives = new ArrayList<>();
                            
                            for (Product p : allProducts) {
                                if (p.getSize().equals(productSize) && p.getPrice() < boughtPrice && p.getStockCount() > 0) {
                                    cheaperAlternatives.add(p);
                                }
                            }

                            if (cheaperAlternatives.isEmpty()) {
                                logs.add("[Plan Execution] Step 3: No cheaper alternative matching size and category found in database.");
                                toolOutcome = String.format("No cheaper alternative found in Category '%s' for Size %d (Current Price: ₹%.2f).", 
                                        productCategory, productSize, boughtPrice);
                            } else {
                                cheaperAlternatives.sort(Comparator.comparing(Product::getPrice));
                                
                                // Live Sync top recommendation
                                logs.add(String.format("[Plan Execution] Step 4: Syncing competitive live platform prices for target candidate: %s...", 
                                        cheaperAlternatives.get(0).getName()));
                                
                                // Trigger sync to get latest prices
                                ProductSearchTool.getAndClearSyncLogs(); // clear any previous logs
                                
                                // Run search tool explicitly for parameters of candidate to trigger sync
                                Map<String, Object> candidateParams = Map.of(
                                        "brand", cheaperAlternatives.get(0).getBrand(),
                                        "color", cheaperAlternatives.get(0).getColor(),
                                        "size", cheaperAlternatives.get(0).getSize()
                                );
                                productSearchTool.execute(candidateParams);
                                logs.addAll(ProductSearchTool.getAndClearSyncLogs());

                                // Refresh product details after sync
                                productRepository.findById(cheaperAlternatives.get(0).getProductId());

                                logs.add("[Plan Execution] Step 3 Success: Cheaper alternatives compiled.");
                                StringBuilder output = new StringBuilder("Cheaper alternatives found for your order:\n");
                                for (Product p : cheaperAlternatives) {
                                    output.append(String.format("- %s %s (Size %d) for ₹%.2f (Save ₹%.2f) - Stock: %d left, Rating: %.1f\n",
                                            p.getBrand(), p.getName(), p.getSize(), p.getPrice(), (boughtPrice - p.getPrice()), p.getStockCount(), p.getRating()));
                                }
                                toolOutcome = output.toString();
                            }
                        }
                    }
                }
            } 
            else if ("RECOMMEND".equalsIgnoreCase(intent)) {
                logs.add("[Planner] Intent Classified: RECOMMEND. Executing Recommendation Plan.");
                toolParams.put("brand", brand);
                // Default to highest reviewed (rating) if user asks for "best reviewed"
                String recType = "trending";
                if (userQuery.toLowerCase().contains("review") || userQuery.toLowerCase().contains("rate") || userQuery.toLowerCase().contains("positive")) {
                    recType = "rating";
                }
                toolParams.put("type", recType);
                
                logs.add(String.format("[Plan Execution] Step 1: Querying RecommendationTool (Criteria: %s, Brand: %s)...", recType, brand));
                ToolResult result = recommendationTool.execute(toolParams);
                logs.add("[Plan Execution] Success.");
                toolOutcome = result.getOutput();
            } 
            else { // Default to SEARCH_PRODUCT
                logs.add("[Planner] Intent Classified: SEARCH_PRODUCT. Executing Database Search Plan.");
                if (brand == null || color == null || sizeObj == null) {
                    logs.add("[Planner] Warning: Missing explicit filters. Running general catalog recommendations instead.");
                    toolParams.put("type", "trending");
                    ToolResult result = recommendationTool.execute(toolParams);
                    toolOutcome = "I didn't capture specific search filters. Here is our trending stock:\n" + result.getOutput();
                } else {
                    toolParams.put("brand", brand);
                    toolParams.put("color", color);
                    toolParams.put("size", sizeObj);
                    
                    logs.add(String.format("[Plan Execution] Step 1: Querying ProductSearchTool for '%s', Color '%s', Size %s...", brand, color, sizeObj));
                    ToolResult result = productSearchTool.execute(toolParams);
                    
                    // Add sync logs from search execution
                    List<String> syncLogs = ProductSearchTool.getAndClearSyncLogs();
                    logs.addAll(syncLogs);

                    // Check if search was empty. If empty, fall back to trending recommendations as requested!
                    List<?> dataList = (List<?>) result.getData();
                    if (dataList == null || dataList.isEmpty()) {
                        logs.add("[Plan Execution] Step 1: Search returned no results. Triggering FALLBACK recommendation plan.");
                        logs.add(String.format("[Plan Execution] Step 2: Searching trending alternatives of brand '%s'...", brand));
                        
                        Map<String, Object> recParams = new HashMap<>();
                        recParams.put("brand", brand);
                        recParams.put("type", "trending");
                        ToolResult recResult = recommendationTool.execute(recParams);
                        
                        toolOutcome = result.getOutput() + "\n\nBut here are the most trending shoes from " + brand + " in our database:\n" + recResult.getOutput();
                    } else {
                        logs.add("[Plan Execution] Step 1: Found matching inventory records.");
                        toolOutcome = result.getOutput();
                    }
                }
            }

            // 4. Synthesize Final Reply
            logs.add("[LLM Synthesize] Formulating final response with shopkeeper persona...");
            String reply = llm.synthesizeResponse(userQuery, intent, toolOutcome);
            
            // Append exchange to conversational memory
            memory.add(Map.of("role", "user", "content", userQuery));
            memory.add(Map.of("role", "assistant", "content", reply));
            
            // Restrict memory to last 10 turns to avoid token limits
            while (memory.size() > 10) {
                memory.remove(0);
            }

            long totalDuration = System.currentTimeMillis() - startTime;
            logs.add(String.format("[Orchestrator] Completed in %d ms.", totalDuration));

            // Package params for frontend metadata
            Map<String, Object> detectedParams = new HashMap<>();
            detectedParams.put("intent", intent);
            detectedParams.put("brand", brand);
            detectedParams.put("color", color);
            detectedParams.put("size", sizeObj);
            detectedParams.put("orderId", orderId);

            return AgentResponse.builder()
                    .reply(reply)
                    .logs(logs)
                    .intent(intent)
                    .detectedParams(detectedParams)
                    .build();
        } finally {
            // Guarantee context cleanup
            groqLlmService.clearThreadHistory();
        }
    }
}
