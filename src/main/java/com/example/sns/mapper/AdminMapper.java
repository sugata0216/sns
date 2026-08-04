package com.example.sns.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AdminMapper {
    @Select("""
        SELECT COUNT(*)
        FROM users
    """)
    Integer countUsers();
    @Select("""
        SELECT COUNT(*)
        FROM posts
    """)
    Integer countPosts();
    @Select("""
        SELECT COUNT(*)
        FROM comments
    """)
    Integer countComments();
    @Select("""
        SELECT COUNT(*)
        FROM likes
    """)
    Integer countLikes();
}
