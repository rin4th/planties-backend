package com.planties.plantiesbackend.service;

import com.planties.plantiesbackend.configuration.CustomException;
import com.planties.plantiesbackend.model.entity.Garden;
import com.planties.plantiesbackend.model.entity.Users;
import com.planties.plantiesbackend.model.request.GardenRequest;
import com.planties.plantiesbackend.repository.GardenRepository;
import com.planties.plantiesbackend.repository.UsersRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class GardenService {

    private final UsersService usersService;
    private final GardenRepository gardenRepository;


    public List<Garden> getAllGardens(HttpServletRequest authorization){
        Users user = usersService.getProfile(authorization);

        UUID userID = user.getId();

        return gardenRepository.findAllGardensByUserId(userID);
    }

    public Garden addNewGarden(
            HttpServletRequest authorization,
            GardenRequest request
            ){
        Users user = usersService.getProfile(authorization);

        var userID = user.getId();
        if (userID == null){
            throw new CustomException.UsernameNotFoundException("User tidak ditemukan");
        }

        Optional<Garden> gardenByNameAndType = gardenRepository.findGardenByNameAndType(userID, request.getName(), request.getType());
        if (gardenByNameAndType.isPresent()){
            throw new CustomException.NameOrTypeTakenException("name taken");
        }
        Garden garden = Garden.builder()
                .name(request.getName())
                .type(request.getType())
                .user_id(userID)
                .build();
        gardenRepository.save(garden);
        return garden;
    }

    public Optional<Garden> getGardenById(
            UUID gardenId,
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
        return optionalGarden;
    }

    @Transactional
    public Garden updateGardenById(
            UUID gardenId,
            GardenRequest request,
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
        if (request.getName() == null) {
            throw new CustomException.BadRequestException("Nama garden tidak boleh kosong");
        }
        if (request.getType() == null) {
            throw new CustomException.BadRequestException("Type tidak boleh kosong");
        }
        garden.setName(request.getName());
        garden.setType(request.getType());
        return garden;
    }


    public Garden deleteGardenById(
            UUID gardenId,
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
        gardenRepository.deleteById(gardenId);
        return garden;
    }
}
