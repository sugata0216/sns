package com.example.sns.mapper;

import com.example.sns.entity.Post;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface PostMapper {
    @Insert("""
        INSERT INTO posts
        (
         user_id,
         content,
         image_path
        )
        VALUES 
        (
         #{userId},
         #{content},
         #{imagePath}
        )
        """)
    int insert(Post post);
    @Delete("""
        DELETE FROM posts
        WHERE post_id = #{postId}
        AND user_id = #{userId}
    """)
    int delete(long postId, long userId);
    @Select("""
        SELECT 
            post_id,
            user_id,
            content,
            image_path,
            created_at,
            updated_at,
            (SELECT COUNT(*) FROM comments c WHERE c.post_id = posts.post_id) AS comment_count,
            (SELECT COUNT(*) FROM likes l WHERE l.post_id = posts.post_id) AS like_count,
            EXISTS(
                SELECT 1 FROM likes l2
                WHERE l2.post_id = posts.post_id AND l2.user_id = #{loginUserId}
            ) AS liked_by_me
        FROM posts
        WHERE user_id = #{targetUserId}
        ORDER BY created_at DESC
    """)
    List<Post> findByUserId(long targetUserId, long loginUserId);
    @Select("""
    SELECT 
        p.post_id,
        p.user_id,
        p.content,
        p.image_path,
        p.created_at,
        u.username,
        u.handle,
        u.avatar_path,
        (SELECT COUNT(*) FROM comments c WHERE c.post_id = p.post_id) AS comment_count,
        (SELECT COUNT(*) FROM likes l WHERE l.post_id = p.post_id) AS like_count
    FROM posts p 
    JOIN users u 
    ON p.user_id = u.user_id
    WHERE p.content ILIKE CONCAT('%', #{keyword}, '%')
    ORDER BY p.created_at DESC
    """)
    List<Post> searchByContent(String keyword);
    @Select("""
    SELECT user_id,
           (SELECT COUNT(*) FROM comments c WHERE c.post_id = p.post_id) AS comment_count
    FROM posts p
    WHERE post_id = #{postId}
    """)
    Long findUserIdByPostId(Long postId);
    @Select("""
        SELECT
            p.post_id,
            p.user_id,
            p.content,
            p.image_path,
            p.created_at,
            p.updated_at,
            u.username,
            u.handle,
            u.avatar_path,
            (SELECT COUNT(*) FROM comments c WHERE c.post_id = p.post_id) AS comment_count,
            (SELECT COUNT(*) FROM likes l WHERE l.post_id = p.post_id) AS like_count,
            EXISTS(
                SELECT 1 FROM likes l2
                WHERE l2.post_id = p.post_id AND l2.user_id = #{loginUserId}
            ) AS liked_by_me
        FROM posts p
        INNER JOIN users u
            ON p.user_id = u.user_id
        INNER JOIN follows f
            ON f.following_id = p.user_id
        WHERE f.follower_id = #{targetUserId}
        ORDER BY p.created_at DESC
    """)
    List<Post> findByFollowing(long targetUserId, long loginUserId);
    @Select("""
        SELECT
            p.post_id,
            p.user_id,
            p.content,
            p.image_path,
            p.created_at,
            p.updated_at,
            u.username,
            u.handle,
            u.avatar_path,
            (SELECT COUNT(*) FROM comments c WHERE c.post_id = p.post_id) AS comment_count,
            (SELECT COUNT(*) FROM likes l WHERE l.post_id = p.post_id) AS like_count
        FROM posts p 
        INNER JOIN users u 
            ON p.user_id = u.user_id
        ORDER BY p.created_at DESC
    """)
    List<Post> findAll();
    @Select("""
        SELECT
            p.post_id,
            p.user_id,
            p.content,
            p.image_path,
            p.created_at,
            p.updated_at,
            u.username,
            u.handle,
            u.avatar_path,
            (SELECT COUNT(*) FROM comments c WHERE c.post_id = p.post_id) AS comment_count,
            (SELECT COUNT(*) FROM likes l WHERE l.post_id = p.post_id) AS like_count,
            EXISTS(
                SELECT 1 FROM likes l2
                WHERE l2.post_id = p.post_id AND l2.user_id = #{loginUserId}
            ) AS liked_by_me
        FROM posts p 
        INNER JOIN users u 
            ON p.user_id = u.user_id
        ORDER BY p.created_at DESC
    """)
    List<Post> findAllWithLikeStatus(long loginUserId);
    @Update("""
    UPDATE posts
    SET content = #{content},
        image_path = #{imagePath},
        updated_at = CURRENT_TIMESTAMP
    WHERE post_id = #{postId}
      AND user_id = #{userId}
    """)
    int update(long postId,
               long userId,
               String content,
               String imagePath);
    @Select("""
    SELECT post_id, user_id, content, image_path, created_at, updated_at
    FROM posts
    WHERE post_id = #{postId}
    """)
    Post findById(long postId);
}
