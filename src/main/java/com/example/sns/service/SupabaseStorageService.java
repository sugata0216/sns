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

    @jakarta.annotation.PostConstruct
    public void checkConfig() {
        System.out.println("SUPABASE_URL = [" + supabaseUrl + "]");
        System.out.println("SERVICE_KEY length = " + (serviceKey != null ? serviceKey.length() : 0));
        System.out.println("posts bucket = " + postsBucket);
        System.out.println("avatar bucket = " + avatarBucket);
    }

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
        String uploadUrl = buildUrl("storage/v1/object/" + bucket + "/" + fileName);

        System.out.println("Upload URL = " + uploadUrl); // ★デバッグ用

        restClient.post()
                .uri(uploadUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + serviceKey)
                .header("apikey", serviceKey)
                .header("x-upsert", "true")
                .contentType(MediaType.parseMediaType(
                        file.getContentType() != null ? file.getContentType() : "application/octet-stream"))
                .body(file.getBytes())
                .retrieve()
                .toBodilessEntity();

        return buildUrl("storage/v1/object/public/" + bucket + "/" + fileName);
    }

    // ★URLを正規化（末尾スラッシュの有無を吸収）
    private String buildUrl(String path) {
        String base = supabaseUrl.endsWith("/")
                ? supabaseUrl.substring(0, supabaseUrl.length() - 1)
                : supabaseUrl;
        return base + "/" + path;
    }

    private String getExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) return "bin";
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}