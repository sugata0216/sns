package com.example.sns.controller;

import com.example.sns.entity.Post;
import com.example.sns.form.PostForm;
import com.example.sns.security.CustomUserDetails;
import com.example.sns.service.PostService;
import com.example.sns.service.SupabaseStorageService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Controller
public class PostController {
    private final PostService postService;
    private final SupabaseStorageService storageService; // ★追加

    public PostController(PostService postService,
                          SupabaseStorageService storageService) {
        this.postService     = postService;
        this.storageService  = storageService;
    }

    @PostMapping("/post")
    public String post(
            @AuthenticationPrincipal CustomUserDetails loginUser,
            @ModelAttribute PostForm postForm,
            Model model) throws IOException {
        System.out.println("=== /post に到達 content=" + postForm.getContent()
                + " image=" + (postForm.getImage() != null ? postForm.getImage().getOriginalFilename() : "null")
                + " size=" + (postForm.getImage() != null ? postForm.getImage().getSize() : 0));
        if (postForm.getContent() == null || postForm.getContent().isBlank()) {
            model.addAttribute("postError", "投稿内容を入力してください。");
            model.addAttribute("posts", postService.findAllWithLikeStatus(loginUser.getUser().getUserId()));
            model.addAttribute("username", loginUser.getUsername());
            model.addAttribute("avatarPath", loginUser.getUser().getAvatarPath());
            model.addAttribute("loginUserId", loginUser.getUser().getUserId());
            model.addAttribute("tab", "recommended");
            return "timeline";
        }

        Post post = new Post();
        post.setUserId(loginUser.getUser().getUserId());
        post.setContent(postForm.getContent());

        MultipartFile image = postForm.getImage();
        if (image != null && !image.isEmpty()) {
            // ★ローカル保存 → Supabase Storage にアップロード
            String publicUrl = storageService.upload(image);
            post.setImagePath(publicUrl);
        }

        postService.create(post);
        return "redirect:/timeline";
    }

    @PostMapping("/post/edit/{postId}")
    public String editPost(
            @PathVariable long postId,
            @AuthenticationPrincipal CustomUserDetails loginUser,
            @RequestParam String content,
            @RequestParam(required = false) MultipartFile image,
            @RequestParam(required = false) boolean removeImage,
            HttpServletRequest request) throws IOException {

        long userId = loginUser.getUser().getUserId();
        Post existing = postService.findById(postId);
        String imagePath = existing.getImagePath();

        if (removeImage) {
            imagePath = null;
        } else if (image != null && !image.isEmpty()) {
            // ★Supabase Storage にアップロード
            imagePath = storageService.upload(image);
        }

        postService.update(postId, userId, content, imagePath);
        return redirectBack(request);
    }

    // 以下は変更なし
    @PostMapping("/post/delete/{postId}")
    public String delete(
            @PathVariable long postId,
            @AuthenticationPrincipal CustomUserDetails loginUser,
            HttpServletRequest request) {
        postService.delete(postId, loginUser.getUser().getUserId());
        return redirectBack(request);
    }

    private String redirectBack(HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        if (referer != null && !referer.isBlank()) {
            return "redirect:" + referer;
        }
        return "redirect:/timeline";
    }
}