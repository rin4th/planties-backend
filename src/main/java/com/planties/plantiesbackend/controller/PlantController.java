package com.planties.plantiesbackend.controller;

import com.planties.plantiesbackend.model.entity.Plant;
import com.planties.plantiesbackend.model.request.GardenRequest;
import com.planties.plantiesbackend.model.request.PlantRequest;
import com.planties.plantiesbackend.model.response.PlantResponse;
import com.planties.plantiesbackend.model.response.ResponseHandler;
import com.planties.plantiesbackend.repository.PlantRepository;
import com.planties.plantiesbackend.service.PlantService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/")
@RequiredArgsConstructor
public class PlantController {

    private final PlantService service;
    private final PlantResponse response;

    @GetMapping(path = "plants")
    public ResponseEntity<Object> getAllPlantsSort(
            HttpServletRequest authorization,
            @RequestParam(name = "sorting", defaultValue = "ASC") String sorting
    ){
        List<Plant> plants = service.getAllPlants(authorization, sorting);
        return ResponseHandler.generateResponse("success", "Success get All Gardens", response.generateJson(plants), HttpStatus.OK);
    }

    @GetMapping(path = "plants/search")
    public ResponseEntity<Object> getSearchPlants(
            HttpServletRequest authorization,
            @RequestBody String plantName
    ){
        List<Plant> plants = service.getPlantByName(authorization, plantName);
        return ResponseHandler.generateResponse("success", "Success get All Gardens", response.generateJson(plants), HttpStatus.OK);
    }

    @GetMapping(path = "gardens/{gardenId}/plants")
    public ResponseEntity<Object> getAllPlantByGardenId(
            HttpServletRequest authorization,
            @PathVariable UUID gardenId,
            @RequestParam(name = "sorting", defaultValue = "ASC") String sorting
            )  {
        List<Plant> plants = service.getAllPlantsByGardenId(authorization, gardenId, sorting);
        return ResponseHandler.generateResponse("success", "Success get All Gardens", response.generateJson(plants), HttpStatus.OK);
    }

    @PostMapping(path = "gardens/{gardenId}/plants")
    public ResponseEntity<Object> addNewPlant(
            HttpServletRequest authorization,
            @PathVariable UUID gardenId,
            @RequestBody PlantRequest request
    ){
        Plant plant = service.addNewPlant(authorization, gardenId, request);
        return ResponseHandler.generateResponse("success", "Success add new Plant", response.generateJson(plant), HttpStatus.OK);
    }

    @PutMapping(path = "gardens/{gardenId}/plants/{plantId}")
    public ResponseEntity<Object> editPlant(
            HttpServletRequest authorization,
            @PathVariable UUID gardenId,
            @PathVariable UUID plantId,
            @RequestBody PlantRequest request
    ){
        Plant plant = service.updatePlantById(gardenId, plantId, request, authorization);
        return ResponseHandler.generateResponse("success", "Success edit Plant", response.generateJson(plant), HttpStatus.OK);
    }

    @GetMapping(path = "gardens/{gardenId}/plants/{plantId}")
    public ResponseEntity<Object> findPlantById(
            HttpServletRequest authorization,
            @PathVariable UUID plantId,
            @PathVariable UUID gardenId

    ){
        Plant plant = service.getPlantById(gardenId, plantId, authorization);
        return ResponseHandler.generateResponse("success", "Success delete Plant", response.generateJson(plant), HttpStatus.OK);
    }
    @DeleteMapping(path = "gardens/{gardenId}/plants/{plantId}")
    public ResponseEntity<Object> deletePlant(
            HttpServletRequest authorization,
            @PathVariable UUID plantId
    ){
        Plant plant = service.deletePlantById(plantId, authorization);
        return ResponseHandler.generateResponse("success", "Success delete Plant", response.generateJson(plant), HttpStatus.OK);
    }
}
