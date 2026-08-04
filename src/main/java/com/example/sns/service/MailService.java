package com.example.sns.service;

import com.example.sns.entity.User;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    private final JavaMailSender mailSender;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    public void sendVerificationMail(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("SNS メール認証");
        message.setText(
                "以下のURLをクリックしてください。\n\n"
                + "http://localhost:8080/verify?token="
                + user.getVerificationToken());
        mailSender.send(message);
    }
    public void sendPasswordResetMail(User user) {
        String resetUrl = "http://localhost:8080/reset-password?token=" + user.getResetToken();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("【SNS】パスワード再設定のご案内");
        message.setText(
                user.getUsername() + "様\n\n"
                + "パスワード再設定のリクエストを受け付けました。\n"
                + "以下のURLをクリックして、新しいパスワードを設定してください(有効期限:30分)。\n\n"
                + resetUrl + "\n\n"
                + "このリクエストに心当たりが無い場合は、本メールを破棄してください。");
        mailSender.send(message);
    }
}
