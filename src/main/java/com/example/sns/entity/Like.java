package com.example.sns.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Like {
    private long likeId;
    private long postId;
    private long userId;
    private LocalDateTime createdAt;
}
