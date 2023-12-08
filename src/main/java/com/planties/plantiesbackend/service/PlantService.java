package com.planties.plantiesbackend.service;


import com.planties.plantiesbackend.model.entity.Garden;
import com.planties.plantiesbackend.model.entity.Plant;
import com.planties.plantiesbackend.model.entity.Users;
import com.planties.plantiesbackend.model.request.GardenRequest;
import com.planties.plantiesbackend.model.request.PlantRequest;
import com.planties.plantiesbackend.model.response.PlantResponse;
import com.planties.plantiesbackend.repository.GardenRepository;
import com.planties.plantiesbackend.repository.PlantRepository;
import com.planties.plantiesbackend.repository.UsersRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlantService {

    private final JwtService jwtService;
    private final PlantRepository plantRepository;
    private final UsersRepository usersRepository;
    private final GardenRepository gardenRepository;

    public List<Plant> getAllPlants(
            HttpServletRequest authorization,
            UUID gardenId
    ){
        final String authHeader = authorization.getHeader(HttpHeaders.AUTHORIZATION);
        final String token;
        final String userUsername;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return null;
        }
        token = authHeader.substring(7);
        userUsername = jwtService.extractUsername(token);
        if (userUsername != null){
            Users user = this.usersRepository.findByUsername(userUsername)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            var userID = user.getId();
            if (userID != null){
                Optional<Garden> gardenOptional = gardenRepository.findById(gardenId);
                if (gardenOptional.isPresent()){
                    Garden garden = gardenOptional.get();
                    if (garden.getUser_id().equals(userID)){
                        return plantRepository.findAllPlantByGardenId(garden.getId());
                    }
                }
            }
        }
        throw new IllegalStateException("Invalid Token");
    }

    public Plant addNewPlant(
            HttpServletRequest authorization,
            UUID gardenId,
            PlantRequest request
    ){
        final String authHeader = authorization.getHeader(HttpHeaders.AUTHORIZATION);
        final String token;
        final String userUsername;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return null;
        }
        token = authHeader.substring(7);
        userUsername = jwtService.extractUsername(token);
        if (userUsername != null){
            Users user = this.usersRepository.findByUsername(userUsername)
                    .orElseThrow();
            var userID = user.getId();
            if (userID != null){
                Optional<Garden> gardenOptional = gardenRepository.findById(gardenId);
                if (gardenOptional.isPresent()) {
                    Garden garden = gardenOptional.get();
                    if (garden.getUser_id().equals(userID)) {
                        Plant plant = Plant.builder()
                                .name(request.getName())
                                .banner(request.getBanner())
                                .url_image(request.getImageBase64()) // temporary
                                .garden_id(garden.getId())
                                .user_id(userID)
                                .build();
                        plantRepository.save(plant);
                        return plant;
                    }
                }
            }
        }
        throw new IllegalStateException("Invalid Token");
    }

    public Plant getPlantById(
            UUID gardenId,
            UUID plantId,
            HttpServletRequest authorization
    ) {
        final String authHeader = authorization.getHeader(HttpHeaders.AUTHORIZATION);
        final String token;
        final String userUsername;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return null;
        }
        token = authHeader.substring(7);
        userUsername = jwtService.extractUsername(token);
        if (userUsername != null){
            Users user = this.usersRepository.findByUsername(userUsername)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            var userID = user.getId();
            if (userID != null){

                Optional<Garden> gardenOptional = gardenRepository.findById(gardenId);
                if (gardenOptional.isPresent()){
                    Garden garden = gardenOptional.get();
                    if (garden.getUser_id().equals(userID)){
                        return plantRepository.findPlantByIdByGardenId(plantId, garden.getId());
                    }
                }
            }
        }

        throw new IllegalStateException("Invalid Token");
    }

    public Optional<Plant> deletePlantById(
            UUID plantId,
            HttpServletRequest authorization
    ){
        final String authHeader = authorization.getHeader(HttpHeaders.AUTHORIZATION);
        final String token;
        final String userUsername;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return null;
        }
        token = authHeader.substring(7);
        userUsername = jwtService.extractUsername(token);
        if (userUsername != null){
            Users user = this.usersRepository.findByUsername(userUsername)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            var userID = user.getId();
            if (userID != null){
                Optional<Plant> plant = plantRepository.findById(plantId);
                if (plant.isPresent()){
                    gardenRepository.deleteById(plantId);
                    return plant;
                }
            }
        }

        throw new IllegalStateException("Invalid Token");
    }

}
