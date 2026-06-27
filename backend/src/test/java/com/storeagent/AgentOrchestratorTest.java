package com.storeagent;

import com.storeagent.agent.AgentOrchestrator;
import com.storeagent.dto.AgentResponse;
import com.storeagent.dto.LoginRequest;
import com.storeagent.dto.RegisterRequest;
import com.storeagent.entity.User;
import com.storeagent.repository.UserRepository;
import com.storeagent.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("dev")
public class AgentOrchestratorTest {

    @Autowired
    private AgentOrchestrator orchestrator;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testOrderTrackingIntentFallback() {
        // Query checking status of seed order ORD-1002
        AgentResponse response = orchestrator.processQuery("where is my order ORD-1002?", "customer", "CUSTOMER");
        
        assertNotNull(response);
        assertEquals("TRACK_ORDER", response.getIntent());
        assertNotNull(response.getLogs());
        assertTrue(response.getReply().contains("ORD-1002"));
        // Logs should trace selected tools
        assertTrue(response.getLogs().stream().anyMatch(l -> l.contains("OrderTool")));
    }

    @Test
    public void testSearchShoesIntent() {
        // Search Nike shoes
        AgentResponse response = orchestrator.processQuery("Do you have Nike shoes?", "customer", "CUSTOMER");
        
        assertNotNull(response);
        assertEquals("SEARCH_PRODUCT", response.getIntent());
        assertTrue(response.getDetectedParams().get("brand").toString().equalsIgnoreCase("Nike"));
    }

    @Test
    public void testCheaperAlternativePlan() {
        // Save on order ORD-1002
        AgentResponse response = orchestrator.processQuery("Is there a cheaper alternative to my order ORD-1002?", "customer", "CUSTOMER");
        
        assertNotNull(response);
        assertEquals("CHEAPER_ALTERNATIVE", response.getIntent());
        assertTrue(response.getLogs().stream().anyMatch(l -> l.contains("ProductTool")));
    }

    @Test
    public void testUserVerificationFlow() {
        String username = "test_verify_user";
        RegisterRequest registerReq = new RegisterRequest(
                username, "password123", "verify@example.com", "+919876543211", "CUSTOMER"
        );

        // Register user
        String regResult = authService.registerUser(registerReq);
        assertNotNull(regResult);

        // Verify unverified login fails
        LoginRequest loginReq = new LoginRequest(username, "password123");
        assertThrows(IllegalArgumentException.class, () -> authService.loginUser(loginReq));

        // Fetch user from repository to get code and otp
        Optional<User> userOpt = userRepository.findByUsername(username);
        assertTrue(userOpt.isPresent());
        User user = userOpt.get();
        assertFalse(user.isEmailVerified());
        assertFalse(user.isMobileVerified());

        // Perform verification
        boolean emailVerified = authService.verifyEmail(username, user.getEmailVerificationCode());
        assertTrue(emailVerified);
        boolean mobileVerified = authService.verifyMobile(username, user.getMobileOtp());
        assertTrue(mobileVerified);

        // Verify login succeeds now
        assertNotNull(authService.loginUser(loginReq));

        // Delete account
        String delResult = authService.deleteAccount(username);
        assertEquals("Account successfully deleted", delResult);
        assertFalse(userRepository.findByUsername(username).isPresent());
    }
}
