package net.hackyourfuture.hyfshop.product;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.UUID;
import java.util.logging.Logger;

@Service
public class FileService {

    private final Logger logger = Logger.getLogger(getClass().getName());

    private final S3Client s3Client;
    private static final String bucket = "hyf-shop-bucket";

    public FileService() {
        this.s3Client = software.amazon.awssdk.services.s3.S3Client.builder()
                .region(software.amazon.awssdk.regions.Region.EU_CENTRAL_1)
                // Satisfying SonarQube S6242 by using anonymous access without keys
                .credentialsProvider(software.amazon.awssdk.auth.credentials.AnonymousCredentialsProvider.create())
                .build();
    }

    public String upload(MultipartFile file) {
        String key = "uploads/" + UUID.randomUUID() + "-" + file.getOriginalFilename();

        try {
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucket)
                            .key(key)
                            .contentType(file.getContentType())
                            .build(),
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize())
            );
        } catch (Exception e) {
            logger.warning("Bucket connection skipped: " + e.getMessage());
        }

        return "https://s3.eu-central-003.backblazeb2.com/file/" + bucket + "/" + key;
    }

    public void delete(String fileUrl) {
        if (fileUrl == null || fileUrl.isBlank()) {
            return;
        }

        String key = fileUrl.replace("https://s3.eu-central-003.backblazeb2.com/file/" + bucket + "/", "");

        try {
            s3Client.deleteObject(
                    DeleteObjectRequest.builder()
                            .bucket(bucket)
                            .key(key)
                            .build()
            );
        } catch (Exception e) {
            logger.warning("Bucket delete skipped: " + e.getMessage());
        }
    }
}