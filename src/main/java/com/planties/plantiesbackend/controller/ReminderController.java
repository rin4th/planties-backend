package com.planties.plantiesbackend.controller;


import com.planties.plantiesbackend.model.entity.Reminder;
import com.planties.plantiesbackend.model.request.ReminderRequest;
import com.planties.plantiesbackend.model.response.PlantResponse;
import com.planties.plantiesbackend.model.response.ReminderResponse;
import com.planties.plantiesbackend.model.response.ResponseHandler;
import com.planties.plantiesbackend.repository.ReminderRepository;
import com.planties.plantiesbackend.service.PlantService;
import com.planties.plantiesbackend.service.ReminderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(path = "/gardens")
@RequiredArgsConstructor
public class ReminderController {
    private final ReminderService service;
    private final ReminderResponse response;

    @GetMapping(path = "/{gardenId}/reminders")
    public ResponseEntity<Object> getAllRemindersByGardenId(
            HttpServletRequest authorization,
            @PathVariable UUID gardenId
    ){
        List<Reminder> reminders = service.getAllRemindersByGardenId(authorization, gardenId);
        return ResponseHandler.generateResponse("success", "Success get All Reminders", response.generateJson(reminders), HttpStatus.OK);
    }

    @GetMapping(path = "/{gardenId}/reminders/{reminderId}")
    public ResponseEntity<Object> getReminder(
            HttpServletRequest authorization,
            @PathVariable UUID gardenId,
            @PathVariable UUID reminderId
    ){
        Optional<Reminder> reminder = service.getReminder(authorization, gardenId, reminderId);
        return ResponseHandler.generateResponse("success", "Success get Reminder", response.generateJson(reminder), HttpStatus.OK);
    }

    @PostMapping(path = "/{gardenId}/reminders")
    public ResponseEntity<Object> addNewReminder(
            HttpServletRequest authorization,
            @PathVariable UUID gardenId,
            @RequestBody ReminderRequest request
    ){
        Reminder reminder = service.addNewReminder(authorization, gardenId, request);
        return ResponseHandler.generateResponse("success", "Success add New Reminder", response.generateJson(reminder), HttpStatus.CREATED);
    }


    @DeleteMapping(path = "/{gardenId}/reminders/{reminderId}")
    public ResponseEntity<Object> deleteReminder(
            HttpServletRequest authorization,
            @PathVariable UUID gardenId,
            @PathVariable UUID reminderId
    ){
        Reminder reminder = service.deleteReminder(authorization, gardenId, reminderId);
        return ResponseHandler.generateResponse("success", "Success delete Reminder", response.generateJson(reminder), HttpStatus.OK);
    }
}
