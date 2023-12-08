package com.planties.plantiesbackend.controller;

import com.planties.plantiesbackend.model.request.GardenRequest;
import com.planties.plantiesbackend.model.request.PlantRequest;
import com.planties.plantiesbackend.model.response.PlantResponse;
import com.planties.plantiesbackend.repository.PlantRepository;
import com.planties.plantiesbackend.service.PlantService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "/gardens")
@RequiredArgsConstructor
public class PlantController {

    private final PlantService service;

    @GetMapping(path = "/{gardenId}/plants")
    public ResponseEntity<Object> getAllGardens(
            HttpServletRequest authorization,
            @PathVariable UUID gardenId
            )  {

        return PlantResponse.generateResponse("success", "Success get All Gardens", service.getAllPlants(authorization, gardenId), HttpStatus.OK);
    }

    @PostMapping(path = "/{gardenId}/plants")
    public ResponseEntity<Object> addNewPlant(
            HttpServletRequest authorization,
            @PathVariable UUID gardenId,
            @RequestBody PlantRequest plant
    ){
        return PlantResponse.generateResponse("success", "Success add new Plant", service.addNewPlant(authorization, gardenId, plant), HttpStatus.OK);
    }

//    @PutMapping(path = "/{gardenId}/plants/{plantId}")
//    public ResponseEntity<Object> editPlant(
//            HttpServletRequest authorization,
//            @PathVariable UUID gardenId,
//            @PathVariable UUID plantId,
//            @RequestBody PlantRequest plant
//    ){
//        return PlantResponse.generateResponse("success", "Success edit Plant", service.editPlant(authorization, gardenId, plant), HttpStatus.OK);
//    }

    @GetMapping(path = "{gardenId}/plants/{plantId}")
    public ResponseEntity<Object> findPlantById(
            HttpServletRequest authorization,
            @PathVariable UUID plantId,
            @PathVariable UUID gardenId
    ){
        return PlantResponse.generateResponse("success", "Success delete Plant", service.getPlantById(gardenId, plantId, authorization), HttpStatus.OK);
    }
    @DeleteMapping(path = "{gardenId}/plants/{plantId}")
    public ResponseEntity<Object> deletePlant(
            HttpServletRequest authorization,
            @PathVariable UUID plantId
    ){
        return PlantResponse.generateResponse("success", "Success delete Plant", service.deletePlantById(plantId, authorization), HttpStatus.OK);
    }
}
