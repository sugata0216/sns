package com.example.sns.repository;

import com.example.sns.entity.Comment;
import com.example.sns.mapper.CommentMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommentRepository {
    private final CommentMapper commentMapper;

    public CommentRepository(CommentMapper commentMapper) {
        this.commentMapper = commentMapper;
    }
    public void insert(Comment comment) {
        commentMapper.insert(comment);
    }
    public List<Comment> findAll() {
        return commentMapper.findAll();
    }
    public List<Comment> findByPostId(long postId) {
        return commentMapper.findByPostId(postId);
    }
}
