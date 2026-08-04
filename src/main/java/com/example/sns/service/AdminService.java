package com.example.sns.service;

import com.example.sns.repository.AdminRepository;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    private final AdminRepository repository;

    public AdminService(AdminRepository repository) {
        this.repository = repository;
    }
    public Integer countUsers() {
        return repository.countUsers();
    }
    public Integer countPosts() {
        return repository.countPosts();
    }
    public Integer countComments() {
        return repository.countComments();
    }
    public Integer countLikes() {
        return repository.countLikes();
    }
}
