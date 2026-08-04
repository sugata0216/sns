package com.example.sns.mapper;

import com.example.sns.entity.Notification;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface NotificationMapper {
    @Insert("""
        INSERT INTO notifications(
            sender_id,
            receiver_id,
            post_id,
            type,
            is_read
        )
        VALUES (
            #{senderId},
            #{receiverId},
            #{postId},
            #{type},
            #{isRead}
        )
    """)
    int insert(Notification notification);
    @Select("""
        SELECT 
            n.*,
            u.username AS sender_name,
            u.avatar_path,
            p.content AS post_content
        FROM notifications n
        JOIN users u 
            ON n.sender_id = u.user_id
        LEFT JOIN posts p
            ON n.post_id = p.post_id
        WHERE receiver_id = #{userId}
        ORDER BY n.created_at DESC
    """)
    List<Notification> findByReceiverId(long userId);
    @Update("""
    UPDATE notifications
    SET is_read = true
    WHERE receiver_id = #{userId}
    """)
    void markAllAsRead(Long userId);
    @Select("""
    SELECT COUNT(*)
    FROM notifications
    WHERE receiver_id = #{userId}
      AND is_read = false
    """)
    int countUnread(long userId);
}
