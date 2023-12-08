package com.planties.plantiesbackend.controller;

import com.planties.plantiesbackend.model.entity.Garden;
import com.planties.plantiesbackend.model.request.GardenRequest;
import com.planties.plantiesbackend.model.response.GardenResponse;
import com.planties.plantiesbackend.model.response.ResponseHandler;
import com.planties.plantiesbackend.service.GardenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(path = "/gardens")
@RequiredArgsConstructor
public class GardenController {


    private final GardenService service;


    private final GardenResponse gardenResponse;

    @GetMapping()
    public ResponseEntity<Object> getAllGardens(
            HttpServletRequest header
            )  {

        List<Garden> gardens = service.getAllGardens(header);
        return ResponseHandler.generateResponse("success", "Success get All Gardens", gardenResponse.generateJson(gardens), HttpStatus.OK);
    }

    @GetMapping("/{gardenId}")
    public ResponseEntity<Object> getGardenById(
            @PathVariable UUID gardenId,
            HttpServletRequest header
    )  {
        Optional<Garden> garden = service.getGardenById(gardenId, header);
        if (garden.isPresent()) {
            return ResponseHandler.generateResponse("success", "Success get Garden by ID", gardenResponse.generateJson(garden), HttpStatus.CREATED);
        } else {
            return ResponseHandler.generateResponse("error", "Garden not found", null, HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping()
    public ResponseEntity<Object> addNewGarden(
            HttpServletRequest header,
            @RequestBody GardenRequest request
    ){
        Garden garden = service.addNewGarden(header, request);
        return ResponseHandler.generateResponse("success", "Success add New Garden", gardenResponse.generateJson(garden), HttpStatus.OK);
    }


    @DeleteMapping("/{gardenId}")
    public ResponseEntity<Object> deleteGarden(
            @PathVariable String gardenId,
            HttpServletRequest header
    )  {
        Optional<Garden> deletedGarden = service.deleteGardenById(gardenId, header);
        if (deletedGarden.isPresent()) {
            return ResponseHandler.generateResponse("success", "Garden deleted successfully", gardenResponse.generateJson(deletedGarden), HttpStatus.OK);
        } else {
            return ResponseHandler.generateResponse("error", "Garden not found", null, HttpStatus.NOT_FOUND);
        }
    }
}
