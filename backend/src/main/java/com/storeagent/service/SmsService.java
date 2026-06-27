package com.storeagent.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class SmsService {

    @Value("${app.twilio.account.sid:}")
    private String accountSid;

    @Value("${app.twilio.auth.token:}")
    private String authToken;

    @Value("${app.twilio.phone.number:}")
    private String fromPhoneNumber;

    public void sendSms(String toPhoneNumber, String messageBody) {
        System.out.println("========== SIMULATED SMS OUTBOUND ==========");
        System.out.println("To: " + toPhoneNumber);
        System.out.println("Message: " + messageBody);
        System.out.println("==============================================");

        if (accountSid == null || accountSid.isEmpty() || authToken == null || authToken.isEmpty()) {
            System.out.println("Twilio credentials not configured. Skipping real SMS.");
            return;
        }

        try {
            String formattedTo = toPhoneNumber.trim();
            if (!formattedTo.startsWith("+")) {
                if (formattedTo.length() == 10) {
                    formattedTo = "+91" + formattedTo; // Default to India country code
                } else {
                    formattedTo = "+" + formattedTo;
                }
            }

            String url = "https://api.twilio.com/2010-04-01/Accounts/" + accountSid + "/Messages.json";
            String form = "To=" + URLEncoder.encode(formattedTo, StandardCharsets.UTF_8) +
                          "&From=" + URLEncoder.encode(fromPhoneNumber, StandardCharsets.UTF_8) +
                          "&Body=" + URLEncoder.encode(messageBody, StandardCharsets.UTF_8);

            String auth = accountSid + ":" + authToken;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Authorization", "Basic " + encodedAuth)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(form))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                System.out.println("SMS sent successfully via Twilio! Status Code: " + response.statusCode());
            } else {
                System.err.println("Twilio SMS send failed. Status: " + response.statusCode() + ", Response: " + response.body());
            }
        } catch (Exception e) {
            System.err.println("Error sending Twilio SMS: " + e.getMessage());
        }
    }
}
