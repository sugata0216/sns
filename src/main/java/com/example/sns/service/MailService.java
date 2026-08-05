package com.example.sns.service;

import com.example.sns.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private final ResendMailService resendMailService;

    @Value("${app.base-url}")
    private String baseUrl;

    public MailService(ResendMailService resendMailService) {
        this.resendMailService = resendMailService;
    }

    public void sendVerificationMail(User user) {
        String subject = "SNS メール認証";
        String html = "<p>以下のURLをクリックしてください。</p>"
                + "<a href='" + baseUrl + "/verify?token="
                + user.getVerificationToken() + "'>メールアドレスを認証する</a>";

        resendMailService.send(user.getEmail(), subject, html);
    }

    public void sendPasswordResetMail(User user) {
        String resetUrl = baseUrl + "/reset-password?token=" + user.getResetToken();
        String subject = "【SNS】パスワード再設定のご案内";
        String html = "<p>" + user.getUsername() + "様</p>"
                + "<p>パスワード再設定のリクエストを受け付けました。</p>"
                + "<p>以下のURLをクリックして、新しいパスワードを設定してください（有効期限：30分）。</p>"
                + "<a href='" + resetUrl + "'>パスワードを再設定する</a>"
                + "<p>このリクエストに心当たりがない場合は、本メールを破棄してください。</p>";

        resendMailService.send(user.getEmail(), subject, html);
    }
}