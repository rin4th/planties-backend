package com.planties.plantiesbackend.controller;


import com.planties.plantiesbackend.model.entity.Oxygen;
import com.planties.plantiesbackend.model.response.AdminResponse;
import com.planties.plantiesbackend.model.response.ResponseHandler;
import com.planties.plantiesbackend.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService service;
    private final AdminResponse response;

    @GetMapping()
    public ResponseEntity<Object> getAdminPage(
            HttpServletRequest authorization
    ) {
        Map<String, Object> adminStatistic = service.getPopularPlantAndTotalUsers(authorization);
        return ResponseHandler.generateResponse("success", "Success get users", response.generateJson(adminStatistic), HttpStatus.OK);
    }

}
