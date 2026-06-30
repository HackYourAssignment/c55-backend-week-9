package net.hackyourfuture.hyfshop.product;

import java.time.Duration;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.beans.factory.annotation.Value;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

@Service
public class FileService {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;

    @Value("${b2.bucket}")
    private String bucket;

    @Value("${b2.endpoint}")
    private String endpoint;

    public FileService(S3Client s3Client, S3Presigner s3Presigner) {
        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
    }

    // Upload — returns the public URL to save in your database
    public String upload(MultipartFile file) throws Exception {
        String key = "uploads/" + UUID.randomUUID() + "-" + file.getOriginalFilename();
        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucket).key(key)
                        .contentType(file.getContentType()).build(),
                RequestBody.fromInputStream(file.getInputStream(), file.getSize())
        );
        // https://s3.us-west-004.backblazeb2.com/file/restapitest/uploads/620fab18-34fc-46c6-9292-ee57f7bebbfc-example.svg
        return endpoint + "/file/" + bucket + "/" + key;
    }

    // Presigned URL — expires after given minutes
    public String getLink(String key, int minutes) {
        return s3Presigner.presignGetObject(
                GetObjectPresignRequest.builder()
                        .signatureDuration(Duration.ofMinutes(minutes))
                        .getObjectRequest(r -> r.bucket(bucket).key(key))
                        .build()
        ).url().toString();
    }

    // Delete — accepts the stored public URL (or a raw key)
    public void delete(String urlOrKey) {
        String prefix = endpoint + "/file/" + bucket + "/";
        String key = urlOrKey.startsWith(prefix) ? urlOrKey.substring(prefix.length()) : urlOrKey;
        s3Client.deleteObject(
                DeleteObjectRequest.builder()
                        .bucket(bucket).key(key).build()
        );
    }
}

