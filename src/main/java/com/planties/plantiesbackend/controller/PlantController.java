package com.planties.plantiesbackend.controller;

import com.planties.plantiesbackend.model.response.PlantResponse;
import com.planties.plantiesbackend.model.response.ResponseHandler;
import com.planties.plantiesbackend.service.PlantService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
