package com.example.basicapiproducts.services;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.specialized.BlockBlobClient;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.IOException;


@Service
@Log4j2
public class AzureStorageService {
    private final BlobServiceClient blobServiceClient;

    @Value("${spring.cloud.azure.storage.blob.container-name}")
    private String containerName;

    public AzureStorageService(BlobServiceClient blobServiceClient) {
        this.blobServiceClient = blobServiceClient;
    }

    public String upload(@NonNull MultipartFile file){
        String response = "";
        BlobContainerClient blobContainerClient =  getBlobContainerClient();
        String filename = file.getOriginalFilename();
        BlockBlobClient blockBlobClient =  blobContainerClient.getBlobClient(filename).getBlockBlobClient();
        try{
            // delete file if already exists in that container
            if(blockBlobClient.exists())
                blockBlobClient.delete();

            //upload file to azure blob storage
            blockBlobClient.upload(new BufferedInputStream(file.getInputStream()), file.getSize(), true);
            response = blockBlobClient.getBlobUrl();
        } catch (IOException e) {
            log.error("Error while processing file", e);
        }
        return response;
    }

    private @NonNull BlobContainerClient getBlobContainerClient() {
        // create container if not exists
        BlobContainerClient blobContainerClient = blobServiceClient.getBlobContainerClient(containerName);
        if (!blobContainerClient.exists()) {
            blobContainerClient.create();
        }
        return blobContainerClient;
    }
}
