package com.example.sns.mapper;

import com.example.sns.entity.Like;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LikeMapper {
    @Insert("""
        INSERT INTO likes (
            post_id,
            user_id
        )
        VALUES (
            #{postId},
            #{userId}
        )
        """)
    int insert(Like like);
    @Delete("""
        DELETE 
        FROM likes
        WHERE post_id = #{postId}
        AND user_id = #{userId}
        """)
    int delete(
            long postId,
            long userId
    );
    @Select("""
        SELECT COUNT(*)
        FROM likes
        WHERE post_id = #{postId}
        """)
    int CountByPostId(long postId);
    @Select("""
        SELECT COUNT(*)
        FROM likes
        WHERE post_id = #{postId}
        AND user_id = #{userId}
        """)
    int exists(
            long postId,
            long userId
    );
}
