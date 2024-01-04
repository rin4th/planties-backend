package com.planties.plantiesbackend.service;

import com.planties.plantiesbackend.configuration.CustomException;
import com.planties.plantiesbackend.model.entity.Oxygen;
import com.planties.plantiesbackend.model.entity.Users;
import com.planties.plantiesbackend.repository.GardenRepository;
import com.planties.plantiesbackend.repository.OxygenRepository;
import com.planties.plantiesbackend.repository.PlantRepository;
import com.planties.plantiesbackend.repository.UsersRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final JwtService jwtService;
    private final UsersRepository usersRepository;
    private final GardenRepository gardenRepository;
    private final PlantRepository plantRepository;
    private final OxygenRepository oxygenRepository;

    public Users checkUsers(HttpServletRequest authorization) {
        final String authHeader = authorization.getHeader(HttpHeaders.AUTHORIZATION);
        final String token;
        final String userUsername;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            throw new CustomException.InvalidTokenException("Not a Bearer token");
        }
        token = authHeader.substring(7);
        userUsername = jwtService.extractUsername(token);

        if (userUsername == null){
            throw new CustomException.InvalidTokenException("Invalid Token");
        }
        Users user = this.usersRepository.findByUsername(userUsername)
                .orElseThrow(() -> new CustomException.UsernameNotFoundException("User tidak ditemukan"));

        var userID = user.getId();
        if (userID == null){
            throw new CustomException.UsernameNotFoundException("User tidak ditemukan");
        }
        return user;
    }

    public Map<String, Object> getProfile(HttpServletRequest authorization) {
        final String authHeader = authorization.getHeader(HttpHeaders.AUTHORIZATION);
        final String token;
        final String userUsername;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            throw new CustomException.InvalidTokenException("Not a Bearer token");
        }
        token = authHeader.substring(7);
        userUsername = jwtService.extractUsername(token);

        if (userUsername == null){
            throw new CustomException.InvalidTokenException("Invalid Token");
        }
        Users user = this.usersRepository.findByUsername(userUsername)
                .orElseThrow(() -> new CustomException.UsernameNotFoundException("User tidak ditemukan"));

        var userID = user.getId();
        if (userID == null){
            throw new CustomException.UsernameNotFoundException("User tidak ditemukan");
        }

        int totalGarden = gardenRepository.countGardenOwned(user.getId());
        int totalPlant = plantRepository.countPlantOwned(user.getId());
        int rank = oxygenRepository.findById(userID).get().getRank();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", user.getId());
        map.put("name", user.getUsername());
        map.put("full_name", user.getFullname());
        map.put("url_image", user.getUrl_image());
        map.put("role", user.getRole());
        map.put("total_garden", totalGarden);
        map.put("total_plant", totalPlant);
        map.put("rank", rank);

        return map;
    }




}
