package com.example.sns.service;

import com.example.sns.entity.Post;
import com.example.sns.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }
    public void create(Post post) {
        if (post.getContent() == null || post.getContent().isBlank()) {
            throw new IllegalArgumentException("投稿内容を入力してください。");
        }
        postRepository.insert(post);
    }
    public List<Post> findAll() {
        return postRepository.findAll();
    }
    public List<Post> findAllWithLikeStatus(long loginUserId) {
        return postRepository.findAllWithLikeStatus(loginUserId);
    }
    public void delete(long postId, long userId) {
        postRepository.deleteById(postId, userId);
    }
    public List<Post> findByUserId(long targetUserId, long loginUserId) {
        return postRepository.findByUserId(targetUserId, loginUserId);
    }
    public List<Post> searchByContent(String keyword) {
        return postRepository.searchByContent(keyword);
    }
    public void deleteByAdmin(long postId) {
        Long ownerId = postRepository.findUserIdByPostId(postId);
        if (ownerId == null) {
            throw new IllegalArgumentException("投稿が見つかりません。");
        }
        postRepository.deleteById(postId, ownerId);
    }
    public List<Post> findByFollowing(long targetUserId, long loginUserId) {
        return postRepository.findByFollowing(targetUserId, loginUserId);
    }
    public void update(long postId, long userId, String content, String imagePath) {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("投稿内容を入力してください。");
        }
        postRepository.update(postId, userId, content, imagePath);
    }
    public Post findById(long postId) {
        return postRepository.findById(postId);
    }
}
