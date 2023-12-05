package com.planties.plantiesbackend.service;

import com.planties.plantiesbackend.model.entity.Garden;
import com.planties.plantiesbackend.model.entity.Users;
import com.planties.plantiesbackend.model.request.GardenRequest;
import com.planties.plantiesbackend.model.response.GardenResponse;
import com.planties.plantiesbackend.repository.GardenRepository;
import com.planties.plantiesbackend.repository.UsersRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class GardenService {

    private final JwtService jwtService;
    private final GardenRepository gardenRepository;
    private final UsersRepository usersRepository;


    public List<Garden> getAllGardens(HttpServletRequest authorization){
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
                return gardenRepository.findAllGardensByUserId(userID);

            }
        }

        throw new IllegalStateException("Invalid Token");
    }

    public Garden addNewGarden(
            HttpServletRequest authorization,
            Garden request
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
                Optional<Garden> gardenByNameAndType = gardenRepository.findGardenByNameAndType(userID, request.getName(), request.getType());

                if (gardenByNameAndType.isPresent()){
                    throw new IllegalStateException("name taken");
                }
                Garden garden = Garden.builder()
                        .name(request.getName())
                        .type(request.getType())
                        .user_id(userID)
                        .build();
                gardenRepository.save(garden);
                return garden;
            }
        }
        throw new IllegalStateException("Invalid Token");
    }

    public Optional<Garden> getGardenById(
            UUID gardenId,
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

                Optional<Garden> garden = gardenRepository.findById(gardenId);
                if (garden.isPresent()){
                    return garden;
                }
            }
        }

        throw new IllegalStateException("Invalid Token");
    }


    public Optional<Garden> deleteGardenById(
            UUID gardenId,
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
                Optional<Garden> garden = gardenRepository.findById(gardenId);
                if (garden.isPresent()){
                    gardenRepository.deleteById(gardenId);
                }
            }
        }

        throw new IllegalStateException("Invalid Token");
    }
}
