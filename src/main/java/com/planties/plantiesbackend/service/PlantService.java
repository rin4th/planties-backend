package com.planties.plantiesbackend.service;


import com.planties.plantiesbackend.model.entity.Garden;
import com.planties.plantiesbackend.model.entity.Plant;
import com.planties.plantiesbackend.model.entity.Users;
import com.planties.plantiesbackend.model.request.PlantRequest;
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
                Optional<Garden> garden = gardenRepository.findById(gardenId);
                if (garden.isPresent()){
                    if (garden.getId().equals(userID)){
                        return plantRepository.findAllPlantByGardenIdAndUserId(garden.getId(), userId());
                    }
                }
            }
        }
    }

}
