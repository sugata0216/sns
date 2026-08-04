package com.example.sns.config;

import com.example.sns.security.CustomUserDetails;
import com.example.sns.service.NotificationService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAdvice {
    private final NotificationService notificationService;
    public GlobalModelAdvice(NotificationService notificationService) {
        this.notificationService = notificationService;
    }
    @ModelAttribute
    public void addNotificationCount(Model model) {
        Object principal = SecurityContextHolder.getContext().getAuthentication() != null
                ? SecurityContextHolder.getContext().getAuthentication().getPrincipal()
                : null;
        if (principal instanceof CustomUserDetails userDetails) {
            long userId = userDetails.getUserId();
            model.addAttribute("notificationCount", notificationService.countUnread(userId));
        }
    }
}