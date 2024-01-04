package com.planties.plantiesbackend.service;

import com.planties.plantiesbackend.model.entity.ImageGarden;
import com.planties.plantiesbackend.model.entity.ImagePlant;
import com.planties.plantiesbackend.model.request.ImageRequest;
import com.planties.plantiesbackend.repository.ImageGardenRepository;
import com.planties.plantiesbackend.repository.ImagePlantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;


import java.util.Base64;
import java.util.UUID;


@Component
@RequiredArgsConstructor
public abstract class Image {


    @Value("${aws.s3.bucketName}")
    protected String bucketName;

    @Autowired
    protected S3Client amazonS3;

    public abstract String uploadImage(String imageBase64, UUID foreign_key);

}
