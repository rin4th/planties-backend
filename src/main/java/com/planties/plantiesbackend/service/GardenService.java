package com.planties.plantiesbackend.service;

import com.planties.plantiesbackend.configuration.CustomException;
import com.planties.plantiesbackend.model.entity.Garden;
import com.planties.plantiesbackend.model.entity.Users;
import com.planties.plantiesbackend.model.request.GardenRequest;
import com.planties.plantiesbackend.repository.GardenRepository;
import com.planties.plantiesbackend.repository.PlantRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;


@Service
@RequiredArgsConstructor
public class GardenService {

    private final UsersService usersService;
    private final GardenRepository gardenRepository;
    private final ImageGardenService image;


    public List<Garden> getAllGardens(
            HttpServletRequest authorization
    ){
        Users user = usersService.checkUsers(authorization);

        UUID userID = user.getId();

        return gardenRepository.findAllGardensByUserId(userID);
    }

    public List<Garden> getAllGardensAndSorting(
            HttpServletRequest authorization,
            String type,
            String sorting
    ){
        Users user = usersService.checkUsers(authorization);

        UUID userID = user.getId();

        if (sorting.equals("ASC")){
            return gardenRepository.findAllGardensByTypeASC(userID, type);
        }else{
            return gardenRepository.findAllGardensByTypeDESC(userID, type);
        }
    }

    public Garden addNewGarden(
            HttpServletRequest authorization,
            GardenRequest request
            ){
        Users user = usersService.checkUsers(authorization);

        var userID = user.getId();
        if (userID == null){
            throw new CustomException.UsernameNotFoundException("User tidak ditemukan");
        }

        Optional<Garden> gardenByNameAndType = gardenRepository.findGardenByNameAndType(userID, request.getName(), request.getType());
        if (gardenByNameAndType.isPresent()){
            throw new CustomException.NameOrTypeTakenException("name taken");
        }

        // image processing
        UUID gardenId = UUID.randomUUID();
        ArrayList<String> urlImages = new ArrayList<String>();
        for (String base64Image : request.getImageBase64()) {
            urlImages.add(image.uploadImage(base64Image, gardenId));
        }

        LocalDate date = LocalDate.now();

        Garden garden = Garden.builder()
                .id(gardenId)
                .name(request.getName())
                .type(request.getType())
                .url_image(urlImages)
                .date(date)
                .user_id(userID)
                .build();
        gardenRepository.save(garden);
        return garden;
    }

    public Optional<Garden> getGardenById(
            UUID gardenId,
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


        return optionalGarden;
    }

    @Transactional
    public Garden updateGardenById(
            UUID gardenId,
            GardenRequest request,
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
        if (request.getName() != null) {
            garden.setName(request.getName());
        }
        if (request.getType() != null) {
            garden.setType(request.getType());
        }
        if (request.getImageBase64() != null){
            ArrayList<String> url_image = garden.getUrl_image();

            // image processing
            ArrayList<String> newImage = new ArrayList<String>();
            for (String base64Image : request.getImageBase64()) {
                newImage.add(image.uploadImage(base64Image, gardenId));
            }

            url_image.addAll(newImage);
            garden.setUrl_image(url_image);
        }

        return garden;
    }


    public Garden deleteGardenById(
            UUID gardenId,
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
        gardenRepository.deleteById(gardenId);
        return garden;
    }
}
