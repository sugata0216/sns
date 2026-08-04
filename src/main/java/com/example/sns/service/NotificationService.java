package com.example.sns.service;

import com.example.sns.entity.Notification;
import com.example.sns.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }
    public void create(Notification notification) {
        notificationRepository.insert(notification);
    }
    public List<Notification> findByReceiverId(long userId) {
        return notificationRepository.findByReceiverId(userId);
    }
    public void markAllAsRead(Long userId) {
        notificationRepository.markAllAsRead(userId);
    }
    public void createLikeNotification(
            Long receiverId,
            Long senderId,
            Long postId) {
        // 自分の投稿に自分でいいねした場合は通知しない
        if (receiverId.equals(senderId)) {
            return;
        }
        Notification notification = new Notification();
        notification.setReceiverId(receiverId);
        notification.setSenderId(senderId);
        notification.setPostId(postId);
        notification.setType("LIKE");
        notification.setIsRead(false);
        notificationRepository.insert(notification);
    }
    public int countUnread(long userId) {
        return notificationRepository.countUnread(userId);
    }
    public void createCommentNotification(
            Long receiverId,
            Long senderId,
            Long postId) {
        // 自分の投稿に自分でコメントした場合は通知しない
        if (receiverId.equals(senderId)) {
            return;
        }
        Notification notification = new Notification();
        notification.setReceiverId(receiverId);
        notification.setSenderId(senderId);
        notification.setPostId(postId);
        notification.setType("COMMENT");
        notification.setIsRead(false);
        notificationRepository.insert(notification);
    }
    public void createFollowNotification(
            Long receiverId,
            Long senderId) {
        // 自分が自分をフォローすることは無いはずだが念のため
        if (receiverId.equals(senderId)) {
            return;
        }
        Notification notification = new Notification();
        notification.setReceiverId(receiverId);
        notification.setSenderId(senderId);
        notification.setPostId(null);
        notification.setType("FOLLOW");
        notification.setIsRead(false);
        notificationRepository.insert(notification);
    }
}
