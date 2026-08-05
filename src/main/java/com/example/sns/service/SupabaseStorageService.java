package com.example.sns.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class SupabaseStorageService {

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.service-key}")
    private String serviceKey;

    @Value("${supabase.storage.bucket}")
    private String postsBucket;

    @Value("${supabase.storage.avatar-bucket}")
    private String avatarBucket;

    private final RestClient restClient = RestClient.create();

    // 投稿用（画像・動画）
    public String upload(MultipartFile file) throws IOException {
        return uploadToBucket(file, postsBucket);
    }

    // アバター用
    public String uploadAvatar(MultipartFile file) throws IOException {
        return uploadToBucket(file, avatarBucket);
    }

    private String uploadToBucket(MultipartFile file, String bucket) throws IOException {
        String extension = getExtension(file.getOriginalFilename());
        String fileName  = UUID.randomUUID() + "." + extension;

        restClient.post()
                .uri(supabaseUrl + "storage/v1/object/" + bucket + "/" + fileName)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + serviceKey)
                .header("x-upsert", "true")
                .contentType(MediaType.parseMediaType(
                        file.getContentType() != null ? file.getContentType() : "application/octet-stream"))
                .body(file.getBytes())
                .retrieve()
                .toBodilessEntity();

        return supabaseUrl + "storage/v1/object/public/" + bucket + "/" + fileName;
    }

    private String getExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) return "bin";
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}