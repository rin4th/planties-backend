package com.planties.plantiesbackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.planties.plantiesbackend.configuration.CustomException;
import com.planties.plantiesbackend.model.entity.Oxygen;
import com.planties.plantiesbackend.model.entity.Plant;
import com.planties.plantiesbackend.model.entity.Users;
import com.planties.plantiesbackend.repository.OxygenRepository;
import com.planties.plantiesbackend.repository.PlantRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OxygenService {

    private final PlantRepository plantRepository;
    private final OxygenRepository oxygenRepository;
    private final UsersService usersService;


    @Transactional
    public double InitialOxygen(String type, LocalDate date, UUID user_id){
        LocalDate currentDate = LocalDate.now();
        long Days = ChronoUnit.DAYS.between(date, currentDate);
        long oxygen = 0;

        switch (type){
            case "Tanaman Air", "Tanaman Daun Hijau", "Tanaman Buah":
                oxygen = Days * 9;
                break;
            case "Tanaman Berbunga":
                oxygen = Days * 8;
                break;
        }
        Optional<Oxygen> oxygenOptional = oxygenRepository.findById(user_id);
        if (oxygenOptional.isEmpty()) {
            Oxygen oxygenEntity = Oxygen.builder()
                    .user_id(user_id)
                    .oxygen(oxygen)
                    .build();
            oxygenRepository.save(oxygenEntity);
        }else{
            Oxygen oxygenEntity = oxygenOptional.get();
            oxygenEntity.setOxygen(oxygenEntity.getOxygen() + oxygen);
        }

        return (double) oxygen;
    }

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void updateAllPlantOxygen(){
        Random random = new Random();
        for (Plant plant : plantRepository.findAll()){
            String type = plant.getType();
            double oxygen = 0;
            int max, min;
            switch(type){
                case "Tanaman Air", "Tanaman Daun Hijau":
                    max = 15;
                    min = 10;
                    oxygen = (random.nextDouble() * (max - min)) + min;
                    plant.setOxygen(plant.getOxygen() + oxygen);
                    break;
                case "Tanaman Berbunga":
                    max = 12;
                    min = 8;
                    oxygen = (random.nextDouble() * (max - min)) + min;
                    plant.setOxygen(plant.getOxygen() + oxygen);
                    break;
                case "Tanaman Buah":
                    max = 30;
                    min = 25;
                    oxygen = (random.nextDouble() * (max - min)) + min;
                    plant.setOxygen(plant.getOxygen() + oxygen);
                    break;
            }
            UUID userID = plant.getUser_id();
            Oxygen oxygenEntity = oxygenRepository.findById(userID).get();
            oxygenEntity.setOxygen(oxygenEntity.getOxygen() + oxygen);
        }
    }

    public List<Oxygen> getLeaderboard(HttpServletRequest authorization){
        Users user = usersService.getProfile(authorization);

        var userID = user.getId();
        if (userID == null){
            throw new CustomException.UsernameNotFoundException("User tidak ditemukan");
        }
        return oxygenRepository.findAllOrderByOxygen();
    }

}