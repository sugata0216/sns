package com.example.sns.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Follow {
    private long followId;
    private long followerId;
    private long followingId;
    private LocalDateTime createdAt;
}
