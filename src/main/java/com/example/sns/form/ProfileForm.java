package com.example.sns.form;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProfileForm {
    private String username;
    private String bio;
    private MultipartFile avatar;
}
