package com.example.sns.form;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class PostForm {
    private String content;
    private MultipartFile image;
}
