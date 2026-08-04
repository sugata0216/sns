package com.example.sns.service;

import com.example.sns.entity.Follow;
import com.example.sns.repository.FollowRepository;
import org.springframework.stereotype.Service;

@Service
public class FollowService {
    private final FollowRepository repository;
    private final NotificationService notificationService;

    public FollowService(FollowRepository repository, NotificationService notificationService) {
        this.repository = repository;
        this.notificationService = notificationService;
    }

    public void toggle(long followerId, long followingId) {
        if (followerId == followingId) {
            throw new IllegalArgumentException("自分自身をフォローすることはできません。");
        }
        if (repository.isFollowing(followerId, followingId)) {
            repository.delete(followerId, followingId);
        } else {
            Follow follow = new Follow();
            follow.setFollowerId(followerId);
            follow.setFollowingId(followingId);
            repository.insert(follow);
            notificationService.createFollowNotification(followingId, followerId);
        }
    }

    public boolean isFollowing(long followerId, long followingId) {
        return repository.isFollowing(followerId, followingId);
    }

    public long countFollowing(Long userId) {
        return repository.countFollowing(userId);
    }

    public long countFollowers(Long userId) {
        return repository.countFollowers(userId);
    }
}