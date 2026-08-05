package com.example.sns.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
public class ResendMailService {

    @Value("${resend.api-key}")
    private String apiKey;

    @Value("${resend.from-email}")
    private String fromEmail;

    private final RestClient restClient = RestClient.create();

    public void send(String toEmail, String subject, String htmlBody) {
        Map<String, Object> body = Map.of(
                "from",    fromEmail,
                "to",      new String[]{toEmail},
                "subject", subject,
                "html",    htmlBody
        );

        restClient.post()
                .uri("https://api.resend.com/emails")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .toBodilessEntity();
    }
}