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

    @Value("${app.mail-enabled:false}") // ★追加：デフォルトは無効
    private boolean mailEnabled;

    private final RestClient restClient = RestClient.create();

    public void send(String toEmail, String subject, String htmlBody) {
        if (!mailEnabled) {
            System.out.println("メール送信は無効化されています。宛先: " + toEmail + " / 件名: " + subject);
            return; // ★何もせず終了
        }

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