package com.example.sns.controller;

import com.example.sns.security.CustomUserDetails;
import com.example.sns.service.PostService;
import com.example.sns.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SearchController {
    private final UserService userService;
    private final PostService postService;

    public SearchController(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }
    @GetMapping("/search")
    public String search(
            @AuthenticationPrincipal CustomUserDetails loginUser,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "posts") String tab,
            Model model) {
        long loginUserId = loginUser.getUser().getUserId();
        model.addAttribute("keyword", keyword);
        model.addAttribute("tab", tab);
        model.addAttribute("username", loginUser.getUsername());
        model.addAttribute("avatarPath", loginUser.getUser().getAvatarPath());
        model.addAttribute("loginUserId", loginUserId);
        if (keyword != null && !keyword.isBlank()) {
            if ("accounts".equals(tab)) {
                model.addAttribute("users", userService.searchByUsername(keyword, loginUserId));
                model.addAttribute("posts", List.of());
            } else {
                model.addAttribute("posts", postService.searchByContent(keyword));
                model.addAttribute("users", List.of());
            }
        } else {
            model.addAttribute("posts", List.of());
            model.addAttribute("users", List.of());
        }
        return "search";
    }
}