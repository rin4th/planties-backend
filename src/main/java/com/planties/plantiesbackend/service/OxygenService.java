package com.planties.plantiesbackend.service;

import com.planties.plantiesbackend.repository.UsersRepository;
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
import java.util.*;

@Service
@RequiredArgsConstructor
public class OxygenService {

    private final PlantRepository plantRepository;
    private final OxygenRepository oxygenRepository;
    private final UsersService usersService;
    private final UsersRepository usersRepository;


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
                    .rank(999)
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

    @Transactional
    @Scheduled(cron = "0 0 0 * * *")
    public void updateLeaderboard(){
        Random random = new Random();
        int rank = 1;
        for (Oxygen oxy : oxygenRepository.findAllOrderByOxygen()){
            oxy.setRank(rank);
            rank++;
        }
    }

    public List<Oxygen> getLeaderboards(HttpServletRequest authorization){
        Users user = usersService.checkUsers(authorization);
        return oxygenRepository.getLeaderboard();
    }

    public Oxygen getRank(
            HttpServletRequest authorization,
            UUID userId
    ){
        Users user = usersService.checkUsers(authorization);
        if (!user.getId().equals(userId)){
            throw new CustomException.InvalidIdException("Jwt dengan UserID berbeda");
        }
        return oxygenRepository.findById(user.getId()).get();
    }

}