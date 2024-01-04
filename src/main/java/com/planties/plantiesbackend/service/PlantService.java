package com.planties.plantiesbackend.service;


import com.planties.plantiesbackend.configuration.CustomException;
import com.planties.plantiesbackend.model.entity.Garden;
import com.planties.plantiesbackend.model.entity.Plant;
import com.planties.plantiesbackend.model.entity.Users;
import com.planties.plantiesbackend.model.request.PlantRequest;
import com.planties.plantiesbackend.repository.GardenRepository;
import com.planties.plantiesbackend.repository.PlantRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
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
    private final ImagePlantService image;
    private final OxygenService oxgenService;


    public List<Plant> getAllPlants(
            HttpServletRequest authorization,
            String sorting
    ){
        Users user = usersService.checkUsers(authorization);

        var userID = user.getId();
        if (userID == null){
            throw new CustomException.UsernameNotFoundException("User tidak ditemukan");
        }
        if (sorting.equals("ASC")){
            return plantRepository.findAllPlantByUserIdASC(userID);
        }else{
            return plantRepository.findAllPlantByUserIdDESC(userID);
        }

    }


    public List<Plant> getAllPlantsByGardenId(
            HttpServletRequest authorization,
            UUID gardenId,
            String sorting
    ){
        Users user = usersService.checkUsers(authorization);

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
        if (sorting.equals("ASC")){
            return plantRepository.findAllPlantByGardenIdASC(garden.getId());
        }else{
            return plantRepository.findAllPlantByGardenIdDESC(garden.getId());
        }
    }

    public Plant addNewPlant(
            HttpServletRequest authorization,
            UUID gardenId,
            PlantRequest request
    ){
        Users user = usersService.checkUsers(authorization);

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

        // image processing
        UUID plantId = UUID.randomUUID();
        ArrayList<String> urlImages = new ArrayList<String>();
        for (String base64Image : request.getImageBase64()) {
            urlImages.add(image.uploadImage(base64Image, plantId));
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
                .type(request.getType())
                .oxygen(oxygen)
                .user_id(userID)
                .build();
        plantRepository.save(plant);
        return plant;
    }

    public List<Plant> getPlantByName(
            HttpServletRequest authorization,
            String plantName
    ){
        Users user = usersService.checkUsers(authorization);

        List<Plant> searchedPlants = plantRepository.findPlantByName(plantName );
        if (searchedPlants.isEmpty()){
            throw new CustomException.BadRequestException( "Tanaman tidak ditemukan");
        }
        return searchedPlants;
    }

    public Plant getPlantById(
            UUID gardenId,
            UUID plantId,
            HttpServletRequest authorization
    ) {
        Users user = usersService.checkUsers(authorization);

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
        Users user = usersService.checkUsers(authorization);

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
        if (request.getName() != null) {
            plant.setName(request.getName());
        }
        if (request.getBanner() != null) {
            plant.setBanner(request.getBanner());
        }
        if (request.getImageBase64() != null) {
            ArrayList<String> url_image = plant.getUrl_image();

            // image processing
            ArrayList<String> newImage = new ArrayList<String>();
            for (String base64Image : request.getImageBase64()) {
                newImage.add(image.uploadImage(base64Image, plantId));
            }

            url_image.addAll(newImage);
            plant.setUrl_image(url_image);

        }

        return plant;
    }

    public Plant deletePlantById(
            UUID plantId,
            HttpServletRequest authorization
    ){
        Users user = usersService.checkUsers(authorization);

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
