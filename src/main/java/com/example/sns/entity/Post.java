package com.example.sns.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Post {
    private long postId;
    private long userId;
    private String username;
    private String content;
    private String imagePath;
    private String avatarPath;
    private String handle;
    private long commentCount;
    private long likeCount;
    private boolean likedByMe;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
