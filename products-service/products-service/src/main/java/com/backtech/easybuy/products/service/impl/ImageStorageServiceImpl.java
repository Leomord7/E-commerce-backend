package com.backtech.easybuy.products.service.impl;

import com.backtech.easybuy.products.exception.InvalidRequestException;
import com.backtech.easybuy.products.exception.InvalidStateException;
import com.backtech.easybuy.products.service.ImageStorageService;
import io.imagekit.client.ImageKitClient;
import io.imagekit.client.okhttp.ImageKitOkHttpClient;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import io.imagekit.models.files.FileUploadParams;
import io.imagekit.models.files.FileUploadResponse;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Transactional
public class ImageStorageServiceImpl implements ImageStorageService {

    private final String folder;
    private final String url;
    private final String publicKey;
    private final String privateKey;

    public ImageStorageServiceImpl(
            @Value("${imagekit.privateKey:}") String privateKey,
            @Value("${imagekit.folder:}") String folder,
            @Value("${imagekit.publicKey:}") String publicKey,
            @Value("${imagekit.url:}") String url

    ){
        this.privateKey = privateKey;
        this.folder = folder;
        this.publicKey = publicKey;
        this.url = url;
    }


    @Override
    public String uploadImage(MultipartFile imageFile) {
        if (imageFile == null || imageFile.isEmpty()) {
            throw new InvalidRequestException("Image file is cannot be empty");
        }
        // for upload we can use image kid doc for java also include dep in porm
        validateConfig();
        try {
            ImageKitClient client = ImageKitOkHttpClient.builder()
                    .privateKey(privateKey)
                    .build();

            FileUploadParams params = FileUploadParams.builder()
                    .file(imageFile.getBytes())
                    .fileName(resolveFileName(imageFile))
                    .folder(folder)
                    .build();
            FileUploadResponse response = client.files().upload(params);

            return response.url().orElseThrow(() -> new InvalidStateException("ImageKit failed to upload image"));
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to upload product image to ImageKit", ex);
        }
    }

    private String resolveFileName(MultipartFile imageFile) {
        String imageName = imageFile.getOriginalFilename();
        if(imageName == null || imageName.isEmpty()){
            return UUID.randomUUID() + ".jpg";
        }
        return imageName;
    }

    private void validateConfig() {
        if(privateKey.isBlank()){
            throw new InvalidRequestException("ImageKit credencials are not configured");
        }
    }
}
