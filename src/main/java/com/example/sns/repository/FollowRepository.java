package com.example.sns.repository;

import com.example.sns.entity.Follow;
import com.example.sns.mapper.FollowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class FollowRepository {
    private final FollowMapper mapper;
    private final FollowMapper followMapper;

    public FollowRepository(FollowMapper mapper, FollowMapper followMapper) {
        this.mapper = mapper;
        this.followMapper = followMapper;
    }
    public void insert(Follow follow) {
        mapper.insert(follow);
    }
    public void delete(long followerId, long followingId) {
        mapper.delete(followerId, followingId);
    }
    public long countFollowing(Long userId) {
        return followMapper.countFollowing(userId);
    }
    public long countFollowers(Long userId) {
        return followMapper.countFollows(userId);
    }
    public boolean isFollowing(Long followerId, Long followingId) {
        return followMapper.countFollowRelation(followerId, followingId) > 0;
    }
}
