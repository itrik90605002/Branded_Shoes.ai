package com.storeagent.dto;

import jakarta.validation.constraints.NotBlank;

public class AgentRequest {

    @NotBlank(message = "Query is required")
    private String query;

    private String groqApiKey; // Optional: Temp key input from client UI

    public AgentRequest() {}

    public AgentRequest(String query, String groqApiKey) {
        this.query = query;
        this.groqApiKey = groqApiKey;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getGroqApiKey() {
        return groqApiKey;
    }

    public void setGroqApiKey(String groqApiKey) {
        this.groqApiKey = groqApiKey;
    }
}
