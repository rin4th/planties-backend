package com.planties.plantiesbackend.controller;

import com.planties.plantiesbackend.model.request.LoginRequest;
import com.planties.plantiesbackend.model.request.RegisterRequest;
import com.planties.plantiesbackend.model.response.AuthenticationResponse;
import com.planties.plantiesbackend.model.response.ResponseHandler;
import com.planties.plantiesbackend.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class AuthenticationController {


    private final AuthenticationService service;

    @PostMapping("users")
    public ResponseEntity<Object> register(
            @RequestBody RegisterRequest request
    ) {
        AuthenticationResponse returnRegister =  service.register(request);
        return ResponseHandler.generateResponse("success", "Success register new User", returnRegister, HttpStatus.OK);
    }

    @PostMapping("authentications")
    public ResponseEntity<Object> authenticate(
            @RequestBody LoginRequest request
    ) {
        AuthenticationResponse token = service.login(request);
        return ResponseHandler.generateResponse("success", "Success login", token, HttpStatus.OK);
    }

    @PutMapping("authentications")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        service.refreshToken(request, response);
    }

    @DeleteMapping("authentications")
    public ResponseEntity<Object> logout(
            HttpServletRequest authorization
    ) throws IOException {
        service.logout(authorization);
        return ResponseHandler.generateResponse("success", "Success logout", null, HttpStatus.OK);
    }
}
