package com.planties.plantiesbackend.service;


import com.planties.plantiesbackend.configuration.CustomException;
import com.planties.plantiesbackend.model.entity.Garden;
import com.planties.plantiesbackend.model.entity.Plant;
import com.planties.plantiesbackend.model.entity.Users;
import com.planties.plantiesbackend.model.request.GardenRequest;
import com.planties.plantiesbackend.model.request.PlantRequest;
import com.planties.plantiesbackend.model.response.PlantResponse;
import com.planties.plantiesbackend.repository.GardenRepository;
import com.planties.plantiesbackend.repository.PlantRepository;
import com.planties.plantiesbackend.repository.UsersRepository;
import com.planties.plantiesbackend.utils.ImageProcess;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlantService {

    private final UsersService usersService;
    private final PlantRepository plantRepository;
    private final GardenRepository gardenRepository;
    private final ImageProcess imageProcess;
    private final OxygenService oxgenService;


    public List<Plant> getAllPlants(
            HttpServletRequest authorization
    ){
        Users user = usersService.getProfile(authorization);

        var userID = user.getId();
        if (userID == null){
            throw new CustomException.UsernameNotFoundException("User tidak ditemukan");
        }
        return plantRepository.findAllPlantByUserId(userID);
    }

    public List<Plant> getAllPlantsByGardenId(
            HttpServletRequest authorization,
            UUID gardenId
    ){
        Users user = usersService.getProfile(authorization);

        var userID = user.getId();
        if (userID == null){
            throw new CustomException.UsernameNotFoundException("User tidak ditemukan");
        }
        Optional<Garden> optionalGarden = gardenRepository.findById(gardenId);
        if (optionalGarden.isEmpty() ){
            throw new CustomException.InvalidIdException("Garden tidak ditemukan");
        }
        Garden garden = optionalGarden.get();
        if (!garden.getUser_id().equals(userID)){
            throw new CustomException.InvalidIdException("Anda bukan pemilik garden ini");
        }
        return plantRepository.findAllPlantByGardenId(garden.getId());
    }

    public Plant addNewPlant(
            HttpServletRequest authorization,
            UUID gardenId,
            PlantRequest request
    ){
        Users user = usersService.getProfile(authorization);

        var userID = user.getId();
        if (userID == null){
            throw new CustomException.UsernameNotFoundException("User tidak ditemukan");
        }
        Optional<Garden> optionalGarden = gardenRepository.findById(gardenId);
        if (optionalGarden.isEmpty() ){
            throw new CustomException.InvalidIdException("Garden tidak ditemukan");
        }
        Garden garden = optionalGarden.get();
        if (!garden.getUser_id().equals(userID)){
            throw new CustomException.InvalidIdException("Anda bukan pemilik garden ini");
        }
        UUID plantId = UUID.randomUUID();
        ArrayList<String> urlImages = new ArrayList<String>();
        for (String base64Image : request.getImageBase64()) {
            urlImages.add(imageProcess.uploadImage(base64Image, "plant", plantId));
        }

        // Initialize Oxygen
        LocalDate date = LocalDate.parse(request.getDate());
        double oxygen = oxgenService.InitialOxygen(request.getType(), date, userID);

        Plant plant = Plant.builder()
                .id(plantId)
                .name(request.getName())
                .banner(request.getBanner())
                .url_image(urlImages)
                .garden_id(garden.getId())
                .date(date)
                .oxygen(oxygen)
                .user_id(userID)
                .build();
        plantRepository.save(plant);
        return plant;
    }

    public Plant getPlantById(
            UUID gardenId,
            UUID plantId,
            HttpServletRequest authorization
    ) {
        Users user = usersService.getProfile(authorization);

        var userID = user.getId();
        if (userID == null){
            throw new CustomException.UsernameNotFoundException("User tidak ditemukan");
        }
        Optional<Garden> optionalGarden = gardenRepository.findById(gardenId);
        if (optionalGarden.isEmpty() ){
            throw new CustomException.InvalidIdException("Garden tidak ditemukan");
        }
        Garden garden = optionalGarden.get();
        if (!garden.getUser_id().equals(userID)){
            throw new CustomException.InvalidIdException("Anda bukan pemilik garden ini");
        }
        Optional<Plant> optionalPlant = plantRepository.findById(plantId);
        if (optionalPlant.isEmpty()){
            throw new CustomException.InvalidIdException("Plant tidak ditemukan");
        }
        Plant plant = optionalPlant.get();
        if (!plant.getUser_id().equals(userID)){
            throw new CustomException.InvalidIdException("Anda bukan pemiliki tanaman ini");
        }
        return plantRepository.findPlantByIdByGardenId(plantId, garden.getId());
    }

    @Transactional
    public Plant updatePlantById(
            UUID gardenId,
            UUID plantId,
            PlantRequest request,
            HttpServletRequest authorization
    ){
        Users user = usersService.getProfile(authorization);

        var userID = user.getId();
        if (userID == null){
            throw new CustomException.UsernameNotFoundException("User tidak ditemukan");
        }
        Optional<Garden> optionalGarden = gardenRepository.findById(gardenId);
        if (optionalGarden.isEmpty() ){
            throw new CustomException.InvalidIdException("Garden tidak ditemukan");
        }
        Garden garden = optionalGarden.get();
        if (!garden.getUser_id().equals(userID)){
            throw new CustomException.InvalidIdException("Anda bukan pemilik garden ini");
        }
        Optional<Plant> optionalPlant = plantRepository.findById(plantId);
        if (optionalPlant.isEmpty()){
            throw new CustomException.InvalidIdException("Plant tidak ditemukan");
        }
        Plant plant = optionalPlant.get();
        if (!plant.getUser_id().equals(userID)) {
            throw new CustomException.InvalidIdException("Anda bukan pemiliki tanaman ini");
        }
        if (request.getName() == null) {
            throw new CustomException.BadRequestException("Nama tanaman tidak boleh kosong");
        }
        if (request.getBanner() == null) {
            throw new CustomException.BadRequestException("Banner tidak boleh kosong");
        }
        plant.setName(request.getName());
        plant.setBanner(request.getBanner());
        plant.setUrl_image(request.getImageBase64()); // temporarya
        return plant;
    }

    public Plant deletePlantById(
            UUID plantId,
            HttpServletRequest authorization
    ){
        Users user = usersService.getProfile(authorization);

        var userID = user.getId();
        if (userID == null){
            throw new CustomException.UsernameNotFoundException("User tidak ditemukan");
        }
        Optional<Plant> optionalPlant = plantRepository.findById(plantId);
        if (optionalPlant.isEmpty()){
            throw new CustomException.InvalidIdException("Plant tidak ditemukan");
        }
        Plant plant = optionalPlant.get();
        if (!plant.getUser_id().equals(userID)) {
            throw new CustomException.InvalidIdException("Anda bukan pemiliki tanaman ini");
        }
        plantRepository.deleteById(plantId);
         return plant;
    }
}
