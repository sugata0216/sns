package com.example.sns.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Notification {
    private long notificationId;
    private long senderId;
    private long receiverId;
    private Long postId;
    private String type;
    private Boolean isRead;
    private String avatarPath;
    private LocalDateTime createdAt;
    //JOIN用
    private String senderName;
    private String postContent;
}
