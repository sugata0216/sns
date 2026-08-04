package com.example.sns.repository;

import com.example.sns.entity.Notification;
import com.example.sns.mapper.NotificationMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class NotificationRepository {
    private final NotificationMapper notificationMapper;

    public NotificationRepository(NotificationMapper notificationMapper) {
        this.notificationMapper = notificationMapper;
    }
    public void insert(Notification notification) {
        notificationMapper.insert(notification);
    }
    public List<Notification> findByReceiverId(long userId) {
        return notificationMapper.findByReceiverId(userId);
    }
    public void markAllAsRead(Long userId) {
        notificationMapper.markAllAsRead(userId);
    }
    public int countUnread(long userId) {
        return notificationMapper.countUnread(userId);
    }
}
