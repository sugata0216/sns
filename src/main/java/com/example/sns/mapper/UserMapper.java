package com.example.sns.mapper;

import com.example.sns.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {
    @Select("""
        SELECT  
            u.*,
            r.role_name
        FROM users u
        JOIN roles r 
        ON u.role_id = r.role_id
        WHERE email = #{email}
        """)
    User findByEmail(String email);
    @Select("""
        SELECT 
            u.user_id,
            u.username,
            u.email,
            u.password,
            u.bio,
            u.avatar_path,
            u.handle,
            u.role_id,
            r.role_name,
            u.created_at,
            u.updated_at
        FROM users u
        JOIN roles r
        ON u.role_id = r.role_id
        WHERE u.user_id = #{userId}
        """)
    User findById(long userId);
    @Insert("""
        INSERT INTO users
        (
         username,
         email,
         password,
         role_id,
         bio,
         verification_token,
         handle
        )
        VALUES 
        (
         #{username},
         #{email},
         #{password},
         1,
         #{bio},
         #{verificationToken},
         #{handle}
        )
        """)
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    int insert(User user);
    @Update("""
        UPDATE users
        SET username = #{username},
            bio = #{bio}
        WHERE user_id = #{userId}
        """)
    int updateProfile(User user);
    @Delete("""
        DELETE FROM users
        WHERE user_id = #{userId}
        """)
    int delete(long userId);
    @Select("""
        SELECT 
            u.user_id,
            u.username,
            u.email,
            u.bio,
            u.role_id,
            u.avatar_path,
            u.handle,
            r.role_name,
            EXISTS(
                SELECT 1 FROM follows f
                WHERE f.follower_id = #{loginUserId} AND f.following_id = u.user_id
            ) AS followed_by_me
        FROM users u 
        JOIN roles r
            ON u.role_id = r.role_id
        WHERE username ILIKE CONCAT('%', #{keyword}, '%')
        ORDER BY u.username
    """)
    List<User> searchByUsername(String keyword, long loginUserId);
    @Select("""
        SELECT 
            u.user_id,
            u.username,
            u.email,
            u.bio,
            u.avatar_path,
            u.created_at,
            r.role_name
        FROM users u 
        JOIN roles r 
        ON u.role_id = r.role_id
        ORDER BY u.user_id
    """)
    List<User> findAll();
    @Update("""
        UPDATE users
        SET role_id = #{roleId}
        WHERE user_id = #{userId}
    """)
    int updateRole(User user);
    @Update("""
    UPDATE users
    SET verification_token = #{verificationToken}
    WHERE user_id = #{userId}
    """)
    int updateVerificationToken(User user);
    @Select("""
    SELECT *
    FROM users
    WHERE verification_token = #{token}
    """)
    User findByVerificationToken(String token);
    @Update("""
    UPDATE users 
    SET verified = true,
        verification_token = NULL
    WHERE user_id = #{userId}
    """)
    int verify(long userId);
    @Update("""
        UPDATE users
        SET reset_token = #{resetToken},
            reset_token_expiry = #{resetTokenExpiry}
        WHERE email = #{email}
        """)
    int updateResetToken(String email, String resetToken, java.time.LocalDateTime resetTokenExpiry);
    @Select("""
        SELECT * FROM users WHERE reset_token = #{token}
        """)
    User findByResetToken(String token);
    @Update("""
        UPDATE users
        SET password = #{password},
            reset_token = NULL,
            reset_token_expiry = NULL
        WHERE user_id = #{userId}
    """)
    int updatePassword(long userId, String password);
    @Update("""
    UPDATE users
    SET avatar_path = #{avatarPath}
    WHERE user_id = #{userId}
    """)
    int updateAvatar(long userId, String avatarPath);
    @Select("""
    SELECT COUNT(*)
    FROM users
    WHERE handle = #{handle}
    """)
    int countByHandle(String handle);
}
