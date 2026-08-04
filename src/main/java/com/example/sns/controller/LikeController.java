package com.example.sns.controller;

import com.example.sns.security.CustomUserDetails;
import com.example.sns.service.LikeService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LikeController {
    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }
    @PostMapping("/like/{postId}")
    public String toggleLike(
            @PathVariable long postId,
            @AuthenticationPrincipal CustomUserDetails loginUser,
            HttpServletRequest request) {
        likeService.toggle(
                postId,
                loginUser.getUser().getUserId()
        );
        String referer = request.getHeader("Referer");
        if (referer != null && !referer.isBlank()) {
            return "redirect:" + referer;
        }
        return "redirect:/timeline";
    }
}