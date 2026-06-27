package com.storeagent.controller;

import com.storeagent.agent.AgentOrchestrator;
import com.storeagent.dto.AgentRequest;
import com.storeagent.dto.AgentResponse;
import com.storeagent.dto.MessageResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/agent")
@CrossOrigin(origins = "*")
@SuppressWarnings("all")
public class AgentController {

    private final AgentOrchestrator agentOrchestrator;
    private final com.storeagent.repository.ProductRepository productRepository;

    public AgentController(AgentOrchestrator agentOrchestrator, com.storeagent.repository.ProductRepository productRepository) {
        this.agentOrchestrator = agentOrchestrator;
        this.productRepository = productRepository;
    }

    @PostMapping("/query")
    public ResponseEntity<?> queryAgent(@Valid @RequestBody AgentRequest request) {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .iterator().next().getAuthority().replace("ROLE_", "");

        try {
            AgentResponse response = agentOrchestrator.processQuery(request.getQuery(), username, role);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse("Agent Execution Error: " + e.getMessage()));
        }
    }

    @PostMapping("/clear-history")
    public ResponseEntity<?> clearHistory() {
        String username = (String) org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        agentOrchestrator.clearUserHistory(username);
        return ResponseEntity.ok(new MessageResponse("Chat history cleared successfully."));
    }

    @GetMapping("/products")
    public ResponseEntity<?> getAllProducts() {
        return ResponseEntity.ok(productRepository.findAll());
    }

    @GetMapping("/filters")
    public ResponseEntity<?> getCatalogFilters() {
        java.util.Map<String, Object> filters = new java.util.HashMap<>();
        filters.put("brands", productRepository.findDistinctBrands());
        filters.put("colors", productRepository.findDistinctColors());
        filters.put("sizes", productRepository.findDistinctSizes());
        return ResponseEntity.ok(filters);
    }
}
