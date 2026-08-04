package com.example.sns.form;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CommentForm {
    private long postId;
    private String content;
    private MultipartFile image;
}
