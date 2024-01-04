package com.planties.plantiesbackend.service;


import com.planties.plantiesbackend.configuration.CustomException;
import com.planties.plantiesbackend.model.entity.Garden;
import com.planties.plantiesbackend.model.entity.Plant;
import com.planties.plantiesbackend.model.entity.Reminder;
import com.planties.plantiesbackend.model.entity.Users;
import com.planties.plantiesbackend.model.request.ReminderRequest;
import com.planties.plantiesbackend.repository.GardenRepository;
import com.planties.plantiesbackend.repository.PlantRepository;
import com.planties.plantiesbackend.repository.ReminderRepository;
import com.planties.plantiesbackend.repository.UsersRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.sql.Time;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReminderService {
    private final ReminderRepository reminderRepository;
    private final GardenRepository gardenRepository;
    private final UsersService usersService;

    public List<Reminder> getAllRemindersByGardenId(
            HttpServletRequest authorization,
            UUID gardenId
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
        return reminderRepository.findAllReminderByGardenId(gardenId);
    }

    public Optional<Reminder> getReminder(
            HttpServletRequest authorization,
            UUID gardenId,
            UUID reminderId
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
        Optional<Reminder> optionalReminder =  reminderRepository.findById(reminderId);
        if (optionalReminder.isEmpty() ){
            throw new CustomException.InvalidIdException("Reminder tidak ditemukan");
        }
        return optionalReminder;
    }

    public Reminder addNewReminder(
            HttpServletRequest authorization,
            UUID gardenId,
            ReminderRequest request
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
        Reminder reminder = Reminder.builder()
                .name(request.getName())
                .type(request.getType())
                .original(request.getDuration())
                .duration(request.getDuration())
                .garden_id(gardenId)
                .build();
        reminderRepository.save(reminder);
        updateReminder();
        return reminder;
    }

    @Transactional
    @Scheduled(fixedDelay = 100000)
    public void updateReminder(
    ){
        for (Reminder reminder : reminderRepository.findAll()){
            Integer duration = reminder.getDuration();
            if (duration == 0){
                reminder.setDuration(reminder.getOriginal());
            }else{
                reminder.setDuration(duration - 10);
            }
        }
    }

    public Reminder deleteReminder(
            HttpServletRequest authorization,
            UUID gardenId,
            UUID reminderId
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
        Optional<Reminder> optionalReminder = reminderRepository.findById(reminderId);
        if (optionalReminder.isEmpty()){
            throw new CustomException.InvalidIdException("Reminder tidak ditemukan");
        }
        Reminder reminder = optionalReminder.get();
        reminderRepository.deleteById(reminderId);
        return reminder;
    }

}
