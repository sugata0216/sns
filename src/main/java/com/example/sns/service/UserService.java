package com.example.sns.service;

import com.example.sns.entity.Role;
import com.example.sns.entity.User;
import com.example.sns.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    public UserService(UserRepository userRepository, RoleService roleService, PasswordEncoder passwordEncoder, MailService mailService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
    }
    public User login(String email) {
        return userRepository.findByEmail(email);
    }
    public String register(User user) {
        if (user.getUsername() == null || user.getUsername().isBlank()) {
            return "ユーザー名を入力してください。";
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            return "メールアドレスを入力してください。";
        }
        if (user.getPassword() == null || user.getPassword().isBlank()) {
            return "パスワードを入力してください。";
        }
        // メールアドレスの重複チェック
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("既に登録されているメールアドレスです。");
        }
        Role role = roleService.findByName("ROLE_USER");
        user.setRoleId(role.getRoleId());
        user.setHandle(generateUniqueHandle());
        userRepository.insert(user);
        return null;
    }
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public User findById(long userId) {
        return userRepository.findById(userId);
    }
    public void updateProfile(User user) {
        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new IllegalArgumentException("ユーザー名を入力してください");
        }
        userRepository.updateProfile(user);
    }
    public List<User> searchByUsername(String keyword, long loginUserId) {
        if (keyword == null) {
            keyword = "";
        }
        return userRepository.searchByUsername(keyword, loginUserId);
    }
    public List<User> findAll() {
        return userRepository.findAll();
    }
    public void delete(long userId) {
        userRepository.delete(userId);
    }
    public void updateRole(User user) {
        userRepository.updateRole(user);
    }
    public boolean verify(String token) {
        User user = userRepository.findByVerificationToken(token);
        if (user == null) {
            return false;
        }
        userRepository.verify(user.getUserId());
        return true;
    }
    public String requestPasswordReset(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            // メールアドレスの存在意義を外部から判別できないようにするため、ここでエラーを返さず処理を続ける(セキュリティ上の配慮)
            return null;
        }
        String token = UUID.randomUUID().toString();
        userRepository.updateResetToken(email, token, LocalDateTime.now().plusMinutes(30));
        user.setResetToken(token);
        mailService.sendPasswordResetMail(user);
        return null;
    }
    public String resetPassword(String token, String newPassword) {
        User user = userRepository.findByResetToken(token);
        if (user == null) {
            return "無効なリンクです。もう一度パスワード再設定をお試しください。";
        }
        if (user.getResetTokenExpiry() == null || user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            return "リンクの有効期限が切れています。もう一度パスワード再設定をお試しください。";
        }
        if (newPassword == null || newPassword.isBlank()) {
            return "新しいパスワードを入力してください。";
        }
        userRepository.updatePassword(user.getUserId(), passwordEncoder.encode(newPassword));
        return null;
    }
    public void updateAvatar(long userId, String avatarPath) {
        userRepository.updateAvatar(userId, avatarPath);
    }
    private String generateUniqueHandle() {
        String handle;
        do {
            handle = "user_" + UUID.randomUUID().toString().substring(0, 8);
        } while (userRepository.countByHandle(handle) > 0);
        return handle;
    }
}
