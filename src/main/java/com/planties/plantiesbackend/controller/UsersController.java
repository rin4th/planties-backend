package com.planties.plantiesbackend.controller;


import com.planties.plantiesbackend.model.entity.Users;
import com.planties.plantiesbackend.model.response.ResponseHandler;
import com.planties.plantiesbackend.model.response.UsersResponse;
import com.planties.plantiesbackend.service.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "/profile")
@RequiredArgsConstructor
public class UsersController {

    private final UsersService service;
    private final UsersResponse response;

    @GetMapping()
    public ResponseEntity<Object> getProfile(
            HttpServletRequest authorization
    ) {
        Map<String, Object> user = service.getProfile(authorization);
        return ResponseHandler.generateResponse("success", "Success get users", response.generateJson(user), HttpStatus.OK);
    }

}
