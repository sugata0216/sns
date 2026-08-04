package com.example.sns.controller;

import com.example.sns.form.PostForm;
import com.example.sns.security.CustomUserDetails;
import com.example.sns.service.PostService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TimelineController {
    private final PostService postService;

    public TimelineController(PostService postService) {
        this.postService = postService;
    }
    @GetMapping("/timeline")
    public String timeline(
            @AuthenticationPrincipal CustomUserDetails loginUser,
            @RequestParam(defaultValue = "recommended") String tab,
            Model model) {
        long userId = loginUser.getUser().getUserId();
        if ("following".equals(tab)) {
            model.addAttribute("posts", postService.findByFollowing(userId, userId));
        } else {
            model.addAttribute("posts", postService.findAllWithLikeStatus(userId));
        }
        model.addAttribute("tab", tab);
        model.addAttribute("postForm", new PostForm());
        model.addAttribute("username", loginUser.getUsername());
        model.addAttribute("avatarPath", loginUser.getUser().getAvatarPath());
        model.addAttribute("loginUserId", userId);
        return "timeline";
    }
}