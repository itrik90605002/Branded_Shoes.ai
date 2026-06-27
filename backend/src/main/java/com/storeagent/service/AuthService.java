package com.storeagent.service;

import com.storeagent.dto.LoginRequest;
import com.storeagent.dto.LoginResponse;
import com.storeagent.dto.RegisterRequest;
import com.storeagent.entity.Role;
import com.storeagent.entity.User;
import com.storeagent.repository.UserRepository;
import com.storeagent.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.Random;

@Service
@SuppressWarnings("all")
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.emailService = emailService;
    }

    private final Random random = new Random();

    @Transactional
    public String registerUser(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        Role role = Role.CUSTOMER;
        if (request.getRole() != null) {
            try {
                role = Role.valueOf(request.getRole().toUpperCase());
            } catch (Exception e) {
                // Keep default CUSTOMER role
            }
        }

        // Generate verification codes
        String emailVerificationCode = "EMAIL-" + (100000 + random.nextInt(900000));
        String mobileOtp = String.valueOf(100000 + random.nextInt(900000));

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .contactNumber(request.getContactNumber())
                .emailVerified(false)
                .mobileVerified(false)
                .emailVerificationCode(emailVerificationCode)
                .mobileOtp(mobileOtp)
                .role(role)
                .build();

        userRepository.save(user);

        // Simulation logs in System Console for developers / emulator
        System.out.println("=================================================");
        System.out.println("[Verification Log] USER SIGN-UP: " + user.getUsername());
        System.out.println("[Email Gateway] Verification Code: " + emailVerificationCode);
        System.out.println("[SMS Gateway] Mobile OTP: " + mobileOtp + " sent to: " + user.getContactNumber());
        System.out.println("=================================================");

        // Send registration email safely in a background thread
        final String finalEmail = user.getEmail();
        final String finalUsername = user.getUsername();
        final String finalRole = user.getRole().name();
        final String finalCode = emailVerificationCode;
        
        new Thread(() -> {
            String subject = "Verify your SHOE.AI Account";
            String body = "Hello " + finalUsername + ",\n\n" +
                    "Thank you for registering at SHOE.AI as a " + finalRole + ".\n" +
                    "Your email verification code is: " + finalCode + "\n\n" +
                    "Please enter this code on the verification screen to complete your registration.\n\n" +
                    "Happy Shopping,\nSHOE.AI Team";
            emailService.sendCustomEmail(finalEmail, subject, body);
        }).start();

        return "User registered successfully. [EMAIL_CODE: " + emailVerificationCode + ", MOBILE_OTP: " + mobileOtp + "]";
    }

    public LoginResponse loginUser(LoginRequest request) {
        Optional<User> userOpt = userRepository.findByUsername(request.getUsername());
        if (userOpt.isEmpty() || !passwordEncoder.matches(request.getPassword(), userOpt.get().getPassword())) {
            throw new IllegalArgumentException("Invalid username or password");
        }

        User user = userOpt.get();
        if (!user.isEmailVerified()) {
            throw new IllegalArgumentException("Account verification pending. Please complete email verification first.");
        }

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());

        return new LoginResponse(token, user.getUsername(), user.getEmail(), user.getRole().name());
    }

    @Transactional
    public boolean verifyEmail(String username, String code) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getEmailVerificationCode() != null && user.getEmailVerificationCode().equals(code)) {
                user.setEmailVerified(true);
                user.setMobileVerified(true);
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }

    @Transactional
    public boolean verifyMobile(String username, String otp) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getMobileOtp() != null && user.getMobileOtp().equals(otp)) {
                user.setMobileVerified(true);
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }

    @Transactional
    public String resendVerificationCodes(String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        User user = userOpt.get();
        
        // Generate new verification codes
        String emailVerificationCode = "EMAIL-" + (100000 + random.nextInt(900000));
        String mobileOtp = String.valueOf(100000 + random.nextInt(900000));

        user.setEmailVerificationCode(emailVerificationCode);
        user.setMobileOtp(mobileOtp);
        userRepository.save(user);

        System.out.println("=================================================");
        System.out.println("[Verification Log] RESEND REQUEST: " + user.getUsername());
        System.out.println("[Email Gateway] Verification Code: " + emailVerificationCode);
        System.out.println("[SMS Gateway] Mobile OTP: " + mobileOtp + " sent to: " + user.getContactNumber());
        System.out.println("=================================================");

        final String finalEmail = user.getEmail();
        final String finalUsername = user.getUsername();
        final String finalCode = emailVerificationCode;

        new Thread(() -> {
            String subject = "Verify your SHOE.AI Account - Resend";
            String body = "Hello " + finalUsername + ",\n\n" +
                    "Your new email verification code is: " + finalCode + "\n\n" +
                    "Please enter this code on the verification screen to complete your registration.\n\n" +
                    "Happy Shopping,\nSHOE.AI Team";
            emailService.sendCustomEmail(finalEmail, subject, body);
        }).start();

        return "Verification codes resent successfully. [EMAIL_CODE: " + emailVerificationCode + ", MOBILE_OTP: " + mobileOtp + "]";
    }

    @Transactional
    public LoginResponse googleLogin(String email, String name) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        User user;
        if (userOpt.isEmpty()) {
            // Auto-register google user
            String baseUsername = email.split("@")[0];
            String username = baseUsername;
            int suffix = 1;
            while (userRepository.existsByUsername(username)) {
                username = baseUsername + suffix;
                suffix++;
            }
            
            user = User.builder()
                    .username(username)
                    .password(passwordEncoder.encode(String.valueOf(random.nextLong())))
                    .email(email)
                    .contactNumber("Google Verified")
                    .emailVerified(true)
                    .mobileVerified(true)
                    .role(Role.CUSTOMER)
                    .build();
            userRepository.save(user);
            System.out.println("[Google Auth] Auto-registered new user: " + username + " with email: " + email);
        } else {
            user = userOpt.get();
        }
        
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());
        return new LoginResponse(token, user.getUsername(), user.getEmail(), user.getRole().name());
    }

    @Transactional
    public LoginResponse updateProfile(String username, String email, String contactNumber) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        
        User user = userOpt.get();
        user.setEmail(email);
        user.setContactNumber(contactNumber);
        userRepository.save(user);
        
        System.out.println("[Profile Sync] Updated profile details for user: " + username);
        
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());
        return new LoginResponse(token, user.getUsername(), user.getEmail(), user.getRole().name());
    }

    public User getProfile(String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }
        return userOpt.get();
    }

    @Transactional
    public String deleteAccount(String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        userRepository.delete(userOpt.get());
        return "Account successfully deleted";
    }
}
