package com.example.sns.repository;

import com.example.sns.entity.Like;
import com.example.sns.mapper.LikeMapper;
import org.springframework.stereotype.Repository;

@Repository
public class LikeRepository {
    private final LikeMapper likeMapper;

    public LikeRepository(LikeMapper likeMapper) {
        this.likeMapper = likeMapper;
    }
    public void insert(Like like) {
        likeMapper.insert(like);
    }
    public void delete(long postId, long userId) {
        likeMapper.delete(postId, userId);
    }
    public int countByPostId(long postId) {
        return likeMapper.CountByPostId(postId);
    }
    public boolean exists(long postId, long userId) {
        return likeMapper.exists(postId, userId) > 0;
    }
}
