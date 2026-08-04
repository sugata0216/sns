package com.example.sns.service;

import com.example.sns.entity.Like;
import com.example.sns.repository.LikeRepository;
import com.example.sns.repository.PostRepository;
import org.springframework.stereotype.Service;

@Service
public class LikeService {
    private final LikeRepository likeRepository;
    private final NotificationService notificationService;
    private final PostRepository postRepository;

    public LikeService(LikeRepository likeRepository, NotificationService notificationService, PostRepository postRepository) {
        this.likeRepository = likeRepository;
        this.notificationService = notificationService;
        this.postRepository = postRepository;
    }
    public void toggle(long postId, long userId) {
        if (likeRepository.exists(postId, userId)) {
            likeRepository.delete(postId, userId);
        } else {
            Like like = new Like();
            like.setPostId(postId);
            like.setUserId(userId);
            likeRepository.insert(like);
            Long postOwnerId = postRepository.findUserIdByPostId(postId);
            notificationService.createLikeNotification(
                    postOwnerId,
                    userId,
                    postId
            );
        }
    }
    public int count(long postId) {
        return likeRepository.countByPostId(postId);
    }
}
