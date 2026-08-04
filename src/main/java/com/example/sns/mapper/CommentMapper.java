package com.example.sns.mapper;

import com.example.sns.entity.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CommentMapper {
    @Insert("""
        INSERT INTO comments (
            post_id,
            user_id,
            content,
            image_path
        )
        VALUES (
            #{postId},
            #{userId},
            #{content},
            #{imagePath}
        )
        """)
    int insert(Comment comment);
    @Select("""
    SELECT 
        c.comment_id,
        c.post_id,
        c.user_id,
        c.content,
        c.created_at,
        u.username
    FROM comments c
    JOIN users u
    ON c.user_id = u.user_id
    ORDER BY c.created_at DESC
    """)
    List<Comment> findAll();
    @Select("""
    SELECT 
        c.comment_id,
        c.post_id,
        c.user_id,
        c.content,
        c.created_at,
        u.username,
        u.handle,
        u.avatar_path,
        c.image_path
    FROM comments c
    JOIN users u
    ON c.user_id = u.user_id
    WHERE c.post_id = #{postId}
    ORDER BY c.created_at ASC
    """)
    List<Comment> findByPostId(long postId);
}
