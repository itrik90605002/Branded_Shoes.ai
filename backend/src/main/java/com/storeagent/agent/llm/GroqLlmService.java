package com.storeagent.agent.llm;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import jakarta.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@SuppressWarnings("all")
public class GroqLlmService implements LlmService {

    @Value("${app.groq.api.key:}")
    private String apiKey;

    @Value("${app.groq.model}")
    private String modelName;

    private static final String GROQ_URL = "https://api.groq.com/openai/v1/chat/completions";
    private final RestTemplate restTemplate = new RestTemplate();


    // Rotating Secure API Key Pool managed strictly on the backend
    private final List<String> apiKeysPool = new CopyOnWriteArrayList<>();
    private final Random random = new Random();

    // Dynamic ThreadLocal to inject chat memory context
    private static final ThreadLocal<List<Map<String, String>>> threadHistory = new ThreadLocal<>();

    public void setThreadHistory(List<Map<String, String>> history) {
        threadHistory.set(history);
    }

    public void clearThreadHistory() {
        threadHistory.remove();
    }

    @PostConstruct
    public void initKeysPool() {
        apiKeysPool.clear();
        if (apiKey != null && !apiKey.trim().isEmpty()) {
            for (String key : apiKey.split(",")) {
                if (!key.trim().isEmpty()) {
                    apiKeysPool.add(key.trim());
                }
            }
        }
        // Seed fallback placeholder keys in the pool if none are provided
        if (apiKeysPool.isEmpty()) {
            apiKeysPool.add("gsk_placeholderVaultRotateKeyA111");
            apiKeysPool.add("gsk_placeholderVaultRotateKeyB222");
            apiKeysPool.add("gsk_placeholderVaultRotateKeyC333");
        }
        System.out.println("[Security Vault] Groq API Key rotation pool initialized with " + apiKeysPool.size() + " keys.");
    }

    private String getRotatedApiKey() {
        if (apiKeysPool.isEmpty()) {
            return null;
        }
        // Random distribution rotation to mitigate rate limits and prevent credential tracing
        int index = random.nextInt(apiKeysPool.size());
        return apiKeysPool.get(index);
    }

    private boolean isMockKey(String key) {
        if (key == null) return true;
        String k = key.trim();
        return k.startsWith("gsk_placeholder") || k.contains("VaultRotationKey") || k.contains("mock");
    }

    @Override
    public boolean isAvailable() {
        if (apiKeysPool.isEmpty()) {
            return false;
        }
        for (String key : apiKeysPool) {
            if (key != null && !key.trim().isEmpty() && !isMockKey(key)) {
                return true;
            }
        }
        return false;
    }

    private String callGroq(String systemPrompt, String userPrompt, boolean forceJson) {
        String activeKey = getRotatedApiKey();
        if (activeKey == null || isMockKey(activeKey)) {
            return null;
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(activeKey);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", modelName);
            requestBody.put("temperature", 0.1);

            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(Map.of("role", "system", "content", systemPrompt));
            
            // Inject conversation history dynamically
            List<Map<String, String>> history = threadHistory.get();
            if (history != null && !history.isEmpty()) {
                messages.addAll(history);
            }
            
            messages.add(Map.of("role", "user", "content", userPrompt));
            requestBody.put("messages", messages);

            if (forceJson) {
                requestBody.put("response_format", Map.of("type", "json_object"));
            }

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> response = restTemplate.postForEntity(GROQ_URL, entity, Map.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List choices = (List) response.getBody().get("choices");
                if (choices != null && !choices.isEmpty()) {
                    Map firstChoice = (Map) choices.get(0);
                    Map message = (Map) firstChoice.get("message");
                    if (message != null) {
                        return (String) message.get("content");
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("[Groq API Error] Failed calling Groq completions: " + e.getMessage());
        }
        return null;
    }

    @Override
    public String parseQuery(String userQuery) {
        String systemPrompt = 
                "You are an expert query parser for an AI Shoe Store Agent. " +
                "You must extract the user's intent, brand (Campus, Nike, Skechers, Puma, Sparx), shoe color, shoe size, and orderId if mentioned. " +
                "You MUST return a JSON object with these EXACT keys: 'intent', 'brand', 'color', 'size', 'orderId'.\n" +
                "Guidelines for keys:\n" +
                "- 'intent' MUST be one of: 'TRACK_ORDER', 'SEARCH_PRODUCT', 'CHEAPER_ALTERNATIVE', 'RECOMMEND', or 'UNKNOWN'.\n" +
                "- If the user asks for alternatives, better deals, or cheaper versions of their order, classify intent as 'CHEAPER_ALTERNATIVE'.\n" +
                "- If the user asks for recommendations, popular, best-rated, or trending items, classify intent as 'RECOMMEND'.\n" +
                "- 'brand' should be the capitalized brand name (Campus, Nike, Skechers, Puma, Sparx) or null.\n" +
                "- 'color' should be capitalized color name or null.\n" +
                "- 'size' should be an integer or null.\n" +
                "- 'orderId' should be extracted as ORD-xxxx (e.g. ORD-1002) or null.\n" +
                "Return ONLY the JSON string. Do not include markdown code block characters.";

        String result = callGroq(systemPrompt, userQuery, true);
        if (result == null) {
            return "{\"intent\":\"UNKNOWN\",\"brand\":null,\"color\":null,\"size\":null,\"orderId\":null}";
        }
        return result.trim();
    }

    @Override
    public String synthesizeResponse(String userQuery, String parsedIntent, String toolExecutionOutcome) {
        String systemPrompt = 
                "You are a professional, helpful, and to-the-point AI Shoe Store Shopkeeper. " +
                "You run a business selling branded shoes: Campus, Nike, Skechers, Puma, and Sparx. " +
                "Based on the customer's question and the real-time database query results, write a concise response.\n" +
                "Guidelines:\n" +
                "- Be extremely to-the-point and avoid generic welcoming boilerplate unless appropriate.\n" +
                "- Provide clear lists of availability (pricing, size, stock, ratings) based ONLY on the database query output provided.\n" +
                "- If the exact item requested is not available (e.g., out of stock or not matching brand/size/color), " +
                "explain the unavailability briefly and recommend the alternate options found in the query output (e.g. trending or top-rated alternatives).\n" +
                "- Act like a real business owner who knows their catalog and wants to make a successful sale.\n" +
                "- Do not refer to 'tools', 'database', 'JPA', or technical internal workings. Present findings as your live shop availability.";

        String userPrompt = String.format(
                "Customer Question: \"%s\"\n" +
                "Detected Intent: %s\n" +
                "Real-time Inventory Output:\n%s",
                userQuery, parsedIntent, toolExecutionOutcome
        );

        String response = callGroq(systemPrompt, userPrompt, false);
        if (response == null) {
            return "I am experiencing connectivity issues checking our shelves. Let me check: " + toolExecutionOutcome;
        }
        return response.trim();
    }
}
