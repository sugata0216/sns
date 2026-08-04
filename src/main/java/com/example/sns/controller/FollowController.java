package com.example.sns.controller;

import com.example.sns.security.CustomUserDetails;
import com.example.sns.service.FollowService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class FollowController {
    private final FollowService followService;

    public FollowController(FollowService followService) {
        this.followService = followService;
    }
    @PostMapping("/follow/{userId}")
    public String follow(
            @PathVariable long userId,
            @AuthenticationPrincipal CustomUserDetails loginUser) {
        followService.toggle(
                loginUser.getUser().getUserId(),
                userId
        );
        return "redirect:/user/" + userId;
    }
}
