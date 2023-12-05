package com.planties.plantiesbackend.controller;

import com.planties.plantiesbackend.model.entity.Garden;
import com.planties.plantiesbackend.model.request.GardenRequest;
import com.planties.plantiesbackend.model.response.ResponseHandler;
import com.planties.plantiesbackend.service.GardenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(path = "/gardens")
@RequiredArgsConstructor
public class GardenController {

    private final GardenService service;

    @GetMapping()
    public ResponseEntity<Object> getAllGardens(
            HttpServletRequest header
            )  {

        return ResponseHandler.generateResponse("success", "Success get All Gardens", service.getAllGardens(header), HttpStatus.OK);
    }

    @GetMapping("/{gardenId}")
    public ResponseEntity<Object> getGardenById(
            @PathVariable UUID gardenId,
            HttpServletRequest header
    )  {
        Optional<Garden> garden = service.getGardenById(gardenId, header);
        if (garden.isPresent()) {
            return ResponseHandler.generateResponse("success", "Success get Garden by ID", garden, HttpStatus.OK);
        } else {
            return ResponseHandler.generateResponse("error", "Garden not found", null, HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping()
    public ResponseEntity<Object> addNewGarden(
            HttpServletRequest header,
            @RequestBody Garden request
    ){
        return ResponseHandler.generateResponse("success", "Success add New Garden", service.addNewGarden(header, request), HttpStatus.OK);
    }


    @DeleteMapping("/{gardenId}")
    public ResponseEntity<Object> deleteGarden(
            @PathVariable UUID gardenId,
            HttpServletRequest header
    )  {
        Optional<Garden> deletedGarden = service.deleteGardenById(gardenId, header);
        if (deletedGarden.isPresent()) {
            return ResponseHandler.generateResponse("success", "Garden deleted successfully", null, HttpStatus.OK);
        } else {
            return ResponseHandler.generateResponse("error", "Garden not found", null, HttpStatus.NOT_FOUND);
        }
    }
}
