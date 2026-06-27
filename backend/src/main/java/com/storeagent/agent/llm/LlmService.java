package com.storeagent.agent.llm;

public interface LlmService {
    
    /**
     * Parse user queries to determine Intent, Brand, Color, Size, and Order ID.
     * Returns a JSON string:
     * {
     *   "intent": "TRACK_ORDER" | "SEARCH_PRODUCT" | "CHEAPER_ALTERNATIVE" | "RECOMMEND" | "UNKNOWN",
     *   "brand": "Puma" | "Nike" | ...,
     *   "color": "Black" | ...,
     *   "size": 8 | ...,
     *   "orderId": "ORD-1002" | ...
     * }
     */
    String parseQuery(String userQuery);

    /**
     * Synthesize the final customer-friendly response using the tool outputs.
     */
    String synthesizeResponse(String userQuery, String parsedIntent, String toolExecutionOutcome);
    
    /**
     * Check if the LLM service has active API configurations.
     */
    boolean isAvailable();
}
