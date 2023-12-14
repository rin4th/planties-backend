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
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
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
        return reminderRepository.findAllReminderByGardenId(gardenId);
    }

    public Optional<Reminder> getReminder(
            HttpServletRequest authorization,
            UUID gardenId,
            UUID reminderId
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
        Reminder reminder = Reminder.builder()
                .name(request.getName())
                .type(request.getType())
                .duration(request.getDuration())
                .garden_id(gardenId)
                .build();
        reminderRepository.save(reminder);
        return reminder;
    }

    @Transactional
    public Reminder updateReminder(
            HttpServletRequest authorization,
            UUID gardenId,
            UUID reminderId,
            ReminderRequest request
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
        Optional<Reminder> optionalReminder = reminderRepository.findById(reminderId);
        if (optionalReminder.isEmpty()){
            throw new CustomException.InvalidIdException("Reminder tidak ditemukan");
        }
        Reminder reminder = optionalReminder.get();
        if (request.getName() == null) {
            throw new CustomException.BadRequestException("Nama remindet tidak boleh kosong");
        }
        if (request.getType() == null) {
            throw new CustomException.BadRequestException("Type reminder tidak boleh kosong");
        }
        if (request.getDuration() == null){
            throw new CustomException.BadRequestException("Duration reminder tidak boleh kosong");
        }
        reminder.setName(request.getName());
        reminder.setType(request.getType());
        reminder.setDuration(request.getDuration());
        return reminder;
    }

    public Reminder deleteReminder(
            HttpServletRequest authorization,
            UUID gardenId,
            UUID reminderId
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
        Optional<Reminder> optionalReminder = reminderRepository.findById(reminderId);
        if (optionalReminder.isEmpty()){
            throw new CustomException.InvalidIdException("Reminder tidak ditemukan");
        }
        Reminder reminder = optionalReminder.get();
        reminderRepository.deleteById(reminderId);
        return reminder;
    }

}
