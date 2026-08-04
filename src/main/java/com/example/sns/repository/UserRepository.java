package com.example.sns.repository;

import com.example.sns.entity.User;
import com.example.sns.mapper.UserMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class UserRepository {
    private final UserMapper userMapper;

    public UserRepository(UserMapper userMapper) {
        this.userMapper = userMapper;
    }
    public User findByEmail(String email) {
        return userMapper.findByEmail(email);
    }
    public int insert(User user) {
        return userMapper.insert(user);
    }
    public User findById(long userId) {
        return userMapper.findById(userId);
    }
    public void updateProfile(User user) {
        userMapper.updateProfile(user);
    }
    public List<User> searchByUsername(String keyword, long loginUserId) {
        return userMapper.searchByUsername(keyword, loginUserId);
    }
    public List<User> findAll() {
        return userMapper.findAll();
    }
    public void delete(long userId) {
        userMapper.delete(userId);
    }
    public void updateRole(User user) {
        userMapper.updateRole(user);
    }
    public User findByVerificationToken(String token) {
        return userMapper.findByVerificationToken(token);
    }
    public void verify(long userId) {
        userMapper.verify(userId);
    }
    public int updateResetToken(String email, String resetToken, LocalDateTime resetTokenExpiry) {
        return userMapper.updateResetToken(email, resetToken, resetTokenExpiry);
    }
    public User findByResetToken(String token) {
        return userMapper.findByResetToken(token);
    }
    public int updatePassword(long userId, String password) {
        return userMapper.updatePassword(userId, password);
    }
    public void updateAvatar(long userId, String avatarPath) {
        userMapper.updateAvatar(userId, avatarPath);
    }
    public int countByHandle(String handle) {
        return userMapper.countByHandle(handle);
    }
}
