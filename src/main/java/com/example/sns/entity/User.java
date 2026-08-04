package com.example.sns.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {
    private long userId;
    private String username;
    private String email;
    private String password;
    private long roleId;
    //JOIN用
    private String roleName;
    private String bio;
    private boolean verified;
    private String verificationToken;
    private String resetToken;
    private LocalDateTime resetTokenExpiry;
    private String avatarPath;
    private String handle;
    private boolean followedByMe;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
