package com.example.sns.repository;

import com.example.sns.mapper.AdminMapper;
import org.springframework.stereotype.Repository;

@Repository
public class AdminRepository {
    private final AdminMapper mapper;

    public AdminRepository(AdminMapper mapper) {
        this.mapper = mapper;
    }
    public Integer countUsers() {
        return mapper.countUsers();
    }
    public Integer countPosts() {
        return mapper.countPosts();
    }
    public Integer countComments() {
        return mapper.countComments();
    }
    public Integer countLikes() {
        return mapper.countLikes();
    }
}
