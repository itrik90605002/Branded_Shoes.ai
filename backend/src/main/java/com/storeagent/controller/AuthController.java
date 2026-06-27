package com.storeagent.controller;

import com.storeagent.dto.LoginRequest;
import com.storeagent.dto.LoginResponse;
import com.storeagent.dto.MessageResponse;
import com.storeagent.dto.RegisterRequest;
import com.storeagent.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@SuppressWarnings("all")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            String result = authService.registerUser(request);
            return ResponseEntity.ok(new MessageResponse(result));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            LoginResponse response = authService.loginUser(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @GetMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam String username, @RequestParam String code) {
        boolean verified = authService.verifyEmail(username, code);
        if (verified) {
            return ResponseEntity.ok(new MessageResponse("Email verified successfully"));
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid email verification code"));
        }
    }

    @PostMapping("/verify-mobile")
    public ResponseEntity<?> verifyMobile(@RequestParam String username, @RequestParam String otp) {
        boolean verified = authService.verifyMobile(username, otp);
        if (verified) {
            return ResponseEntity.ok(new MessageResponse("Mobile number verified successfully"));
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid mobile OTP code"));
        }
    }

    @PostMapping("/resend-codes")
    public ResponseEntity<?> resendCodes(@RequestParam String username) {
        try {
            String result = authService.resendVerificationCodes(username);
            return ResponseEntity.ok(new MessageResponse(result));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteAccount() {
        try {
            String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if ("admin".equals(username) || "customer".equals(username)) {
                return ResponseEntity.badRequest().body(new MessageResponse("Seeded demo accounts cannot be deleted."));
            }
            String result = authService.deleteAccount(username);
            return ResponseEntity.ok(new MessageResponse(result));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PostMapping("/google-login")
    public ResponseEntity<?> googleLogin(@RequestParam String email, @RequestParam String name) {
        try {
            LoginResponse response = authService.googleLogin(email, name);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile() {
        try {
            String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return ResponseEntity.ok(authService.getProfile(username));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestParam String email, @RequestParam String contactNumber) {
        try {
            String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if ("admin".equals(username) || "customer".equals(username)) {
                return ResponseEntity.badRequest().body(new MessageResponse("Seeded demo account details cannot be modified."));
            }
            LoginResponse response = authService.updateProfile(username, email, contactNumber);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }
}
