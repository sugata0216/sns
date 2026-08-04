package com.example.sns.repository;

import com.example.sns.entity.Post;
import com.example.sns.mapper.PostMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PostRepository {
    private final PostMapper postMapper;

    public PostRepository(PostMapper postMapper) {
        this.postMapper = postMapper;
    }
    public void insert(Post post) {
        postMapper.insert(post);
    }
    public List<Post> findAll() {
        return postMapper.findAll();
    }
    public List<Post> findAllWithLikeStatus(long loginUserId) {
        return postMapper.findAllWithLikeStatus(loginUserId);
    }
    public void deleteById(long postId, long userId) {
        postMapper.delete(postId, userId);
    }
    public List<Post> findByUserId(long targetUserId, long loginUserId) {
        return postMapper.findByUserId(targetUserId, loginUserId);
    }
    public List<Post> searchByContent(String keyword) {
        return postMapper.searchByContent(keyword);
    }
    public Long findUserIdByPostId(Long postId) {
        return postMapper.findUserIdByPostId(postId);
    }
    public List<Post> findByFollowing(long targetUserId, long loginUserId) {
        return postMapper.findByFollowing(targetUserId, loginUserId);
    }
    public int update(long postId, long userId, String content, String imagePath) {
        return postMapper.update(postId, userId, content, imagePath);
    }
    public Post findById(long postId) {
        return postMapper.findById(postId);
    }
}
