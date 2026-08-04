package com.example.sns.controller;

import com.example.sns.entity.Comment;
import com.example.sns.form.CommentForm;
import com.example.sns.security.CustomUserDetails;
import com.example.sns.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Controller
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/comment")
    public String createComment(
            @AuthenticationPrincipal CustomUserDetails loginUser,
            @ModelAttribute CommentForm commentForm,
            HttpServletRequest request) throws IOException {
        Comment comment = new Comment();
        comment.setPostId(commentForm.getPostId());
        comment.setUserId(loginUser.getUser().getUserId());
        comment.setContent(commentForm.getContent());

        MultipartFile image = commentForm.getImage();
        if (image != null && !image.isEmpty()) {
            String originalName = image.getOriginalFilename();
            String extension = getExtension(originalName).toLowerCase();

            List<String> allowedExtensions = List.of(
                    "jpg", "jpeg", "png", "gif", "webp",
                    "mp4", "mov", "webm"
            );
            if (!allowedExtensions.contains(extension)) {
                throw new IllegalArgumentException("対応していないファイル形式です。");
            }

            String fileName = UUID.randomUUID() + "_" + originalName;
            Path path = Paths.get("uploads", fileName);
            Files.createDirectories(path.getParent());
            image.transferTo(path);
            comment.setImagePath(fileName);
        }

        commentService.create(comment);

        String referer = request.getHeader("Referer");
        if (referer != null && !referer.isBlank()) {
            return "redirect:" + referer;
        }
        return "redirect:/timeline";
    }

    @GetMapping("/api/comments/{postId}")
    @ResponseBody
    public List<Comment> getComments(@PathVariable long postId) {
        return commentService.findByPostId(postId);
    }

    private String getExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}