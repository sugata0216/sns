package com.example.sns.service;

import com.example.sns.entity.Comment;
import com.example.sns.repository.CommentRepository;
import com.example.sns.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final NotificationService notificationService;

    public CommentService(
            CommentRepository commentRepository,
            PostRepository postRepository,
            NotificationService notificationService) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.notificationService = notificationService;
    }
    public void create(Comment comment) {
        if (comment.getContent() == null || comment.getContent().isBlank()) {
            throw new IllegalArgumentException("コメントを入力してください。");
        }
        commentRepository.insert(comment);
        Long postOwnerId = postRepository.findUserIdByPostId(comment.getPostId());
        notificationService.createCommentNotification(
                postOwnerId,
                comment.getUserId(),
                comment.getPostId()
        );
    }
    public List<Comment> findAll() {
        return commentRepository.findAll();
    }
    public List<Comment> findByPostId(long postId) {
        return commentRepository.findByPostId(postId);
    }
}