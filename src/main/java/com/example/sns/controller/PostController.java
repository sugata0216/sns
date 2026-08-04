package com.example.sns.controller;

import com.example.sns.entity.Post;
import com.example.sns.form.PostForm;
import com.example.sns.security.CustomUserDetails;
import com.example.sns.service.PostService;
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

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/post")
    public String post(
            @AuthenticationPrincipal CustomUserDetails loginUser,
            @ModelAttribute PostForm postForm,
            Model model) throws IOException {
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
        if (!image.isEmpty()) {
            String fileName = UUID.randomUUID() + "_" + image.getOriginalFilename();
            Path path = Paths.get("uploads", fileName);
            Files.createDirectories(path.getParent());
            image.transferTo(path);
            post.setImagePath(fileName);
        }
        postService.create(post);
        return "redirect:/timeline";
    }

    @PostMapping("/post/delete/{postId}")
    public String delete(
            @PathVariable long postId,
            @AuthenticationPrincipal CustomUserDetails loginUser,
            HttpServletRequest request) {
        postService.delete(postId, loginUser.getUser().getUserId());
        return redirectBack(request);
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
            String originalName = image.getOriginalFilename();
            String extension = getExtension(originalName).toLowerCase();
            List<String> allowedExtensions = List.of(
                    "jpg", "jpeg", "png", "gif", "webp", "mp4", "mov", "webm"
            );
            if (!allowedExtensions.contains(extension)) {
                throw new IllegalArgumentException("対応していないファイル形式です。");
            }
            String fileName = UUID.randomUUID() + "_" + originalName;
            Path path = Paths.get("uploads", fileName);
            Files.createDirectories(path.getParent());
            image.transferTo(path);
            imagePath = fileName;
        }

        postService.update(postId, userId, content, imagePath);
        return redirectBack(request);
    }

    private String redirectBack(HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        if (referer != null && !referer.isBlank()) {
            return "redirect:" + referer;
        }
        return "redirect:/timeline";
    }

    private String getExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}