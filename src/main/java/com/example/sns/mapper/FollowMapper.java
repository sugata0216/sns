package com.example.sns.mapper;

import com.example.sns.entity.Follow;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface FollowMapper {
    @Insert("""
        INSERT INTO follows(
            follower_id,
            following_id
        )
        VALUES(
            #{followerId},
            #{followingId}
        )
    """)
    int insert(Follow follow);
    @Delete("""
        DELETE
        FROM follows
        WHERE follower_id = #{followerId}
        AND following_id = #{followingId}
    """)
    int delete(long followerId, long followingId);
    @Select("""
        SELECT COUNT(*)
        FROM follows
        WHERE follower_id = #{followerId}
        AND following_id = #{followingId}
    """)
    int countFollowRelation(Long followerId, Long followingId);
    @Select("""
    SELECT COUNT(*)
    FROM follows
    WHERE follower_id = #{userId}
    """)
    long countFollowing(Long userId);
    @Select("""
    SELECT COUNT(*)
    FROM follows
    WHERE following_id = #{userId}
    """)
    long countFollows(Long userId);
}
