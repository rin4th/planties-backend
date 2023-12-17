package com.planties.plantiesbackend.utils;

import com.planties.plantiesbackend.model.entity.ImageGarden;
import com.planties.plantiesbackend.model.entity.ImagePlant;
import com.planties.plantiesbackend.model.request.ImageRequest;
import com.planties.plantiesbackend.repository.ImageGardenRepository;
import com.planties.plantiesbackend.repository.ImagePlantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;


import java.io.ByteArrayInputStream;
import java.nio.file.Path;
import java.util.Base64;
import java.util.UUID;


@Component
@RequiredArgsConstructor
public class ImageProcess {

    private final ImagePlantRepository imagePlantRepository;
    private final ImageGardenRepository imageGardenRepository;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    @Autowired
    private S3Client amazonS3;

    public String uploadImage(String imageBase64, String type, UUID foreign_key) {
        byte[] imageBytes = Base64.getDecoder().decode(imageBase64);

        ImageRequest image = new ImageRequest();
        image.setBytes(imageBytes);
        UUID id = UUID.randomUUID();
        String path = "pbo/"+id.toString()+".jpg";

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(path)
                .build();

        amazonS3.putObject(request, RequestBody.fromBytes(image.getBytes()));

        String imageUrl = amazonS3.utilities().getUrl(GetUrlRequest.builder()
                        .bucket(bucketName)
                        .key(path)
                        .build())
                        .toExternalForm();

        if (type.equals("plant")) {
            ImagePlant imagePlant = ImagePlant.builder()
                    .id(id)
                    .url(imageUrl)
                    .plant_id(foreign_key)
                    .build();
            imagePlantRepository.save(imagePlant);

        }else if(type.equals("garden")){
            ImageGarden imageGarden = ImageGarden.builder()
                    .id(id)
                    .url(imageUrl)
                    .garden_id(foreign_key)
                    .build();
            imageGardenRepository.save(imageGarden);
        }
        return imageUrl;
    }



}
