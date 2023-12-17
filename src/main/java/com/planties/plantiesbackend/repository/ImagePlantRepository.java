package com.planties.plantiesbackend.repository;

import com.planties.plantiesbackend.model.entity.ImagePlant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ImagePlantRepository extends JpaRepository<ImagePlant, UUID> {

}
