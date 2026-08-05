package com.example.sns.controller;

import com.example.sns.entity.Post;
import com.example.sns.entity.User;
import com.example.sns.form.ProfileForm;
import com.example.sns.security.CustomUserDetails;
import com.example.sns.service.FollowService;
import com.example.sns.service.PostService;
import com.example.sns.service.SupabaseStorageService;
import com.example.sns.service.UserService;
import org.springframework.boot.webmvc.autoconfigure.WebMvcProperties;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Controller
public class ProfileController {
    private final PostService postService;
    private final UserService userService;
    private final FollowService followService;
    private final SupabaseStorageService storageService;
    public ProfileController(PostService postService, UserService userService, FollowService followService, SupabaseStorageService storageService) {
        this.postService = postService;
        this.userService = userService;
        this.followService = followService;
        this.storageService = storageService;
    }
    @GetMapping("/profile")
    public String profile(
            @AuthenticationPrincipal CustomUserDetails loginUser,
            Model model) {
        User user = userService.findById(loginUser.getUser().getUserId());
        ProfileForm profileForm = new ProfileForm();
        profileForm.setUsername(user.getUsername());
        profileForm.setBio(user.getBio());
        model.addAttribute("user", user);
        model.addAttribute("profileForm", profileForm);
        model.addAttribute("posts", postService.findByUserId(user.getUserId(), user.getUserId()));
        model.addAttribute("username", user.getUsername());
        model.addAttribute("avatarPath", user.getAvatarPath());
        return "profile";
    }
    @PostMapping("/profile/edit")
    public String updateProfile(
            @AuthenticationPrincipal CustomUserDetails loginUser,
            @ModelAttribute ProfileForm profileForm) throws IOException {

        User user = new User();
        user.setUserId(loginUser.getUser().getUserId());
        user.setUsername(profileForm.getUsername());
        user.setBio(profileForm.getBio());
        userService.updateProfile(user);

        MultipartFile avatar = profileForm.getAvatar();
        if (avatar != null && !avatar.isEmpty()) {
            // ★ローカル保存 → Supabase Storage にアップロード
            String publicUrl = storageService.upload(avatar);
            userService.updateAvatar(loginUser.getUser().getUserId(), publicUrl);
        }
        return "redirect:/profile";
    }
    @GetMapping("/user/{userId}")
    public String userProfile(
            @PathVariable Long userId,
            Model model,
            @AuthenticationPrincipal CustomUserDetails loginUser) {
        if (userId == loginUser.getUser().getUserId()) {
            return "redirect:/profile";
        }
        User user = userService.findById(userId);
        List<Post> posts = postService.findByUserId(userId, loginUser.getUser().getUserId());
        boolean isFollowing = followService.isFollowing(loginUser.getUser().getUserId(), userId);
        long followingCount = followService.countFollowing(userId);
        long followerCount = followService.countFollowers(userId);
        model.addAttribute("user", user);
        model.addAttribute("posts", posts);
        model.addAttribute("isFollowing", isFollowing);
        model.addAttribute("followingCount", followingCount);
        model.addAttribute("followerCount", followerCount);
        model.addAttribute("username", loginUser.getUsername());
        model.addAttribute("avatarPath", loginUser.getUser().getAvatarPath());
        return "user-profile";
    }
}
