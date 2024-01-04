package com.planties.plantiesbackend.service;

import com.planties.plantiesbackend.model.entity.ImageGarden;
import com.planties.plantiesbackend.model.entity.ImagePlant;
import com.planties.plantiesbackend.model.request.ImageRequest;
import com.planties.plantiesbackend.repository.ImageGardenRepository;
import com.planties.plantiesbackend.repository.ImagePlantRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.util.Base64;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Data
@Service
public class ImagePlantService extends Image{

    private final ImagePlantRepository imagePlantRepository;

    public String uploadImage(String imageBase64, UUID foreign_key) {
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


        ImagePlant imagePlant = ImagePlant.builder()
                .id(id)
                .url(imageUrl)
                .plant_id(foreign_key)
                .build();
        imagePlantRepository.save(imagePlant);

        return imageUrl;
    }
}
