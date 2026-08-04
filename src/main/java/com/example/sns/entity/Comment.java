package com.example.sns.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Comment {
    private long commentId;
    private long postId;
    private long userId;
    private String username;
    private String content;
    private String avatarPath;
    private String handle;
    private String imagePath;
    private LocalDateTime createdAt;
}
