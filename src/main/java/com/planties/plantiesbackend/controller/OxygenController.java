package com.planties.plantiesbackend.controller;


import com.planties.plantiesbackend.model.entity.Oxygen;
import com.planties.plantiesbackend.model.response.OxygenResponse;
import com.planties.plantiesbackend.model.response.ResponseHandler;
import com.planties.plantiesbackend.service.OxygenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(path = "/leaderboards")
@RequiredArgsConstructor
public class OxygenController {

    private final OxygenService service;
    private final OxygenResponse response;

    @GetMapping()
    public ResponseEntity<Object> getLeaderboards(
            HttpServletRequest authorization
    ) {
        List<Oxygen> oxygens = service.getLeaderboards(authorization);
        return ResponseHandler.generateResponse("success", "Success get users", response.generateJson(oxygens), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getRank(
            HttpServletRequest authorization,
            @PathVariable UUID userId
    ){
        Oxygen oxygen = service.getRank(authorization, userId);
        return ResponseHandler.generateResponse("success", "Success get users", response.generateJson(oxygen), HttpStatus.OK);
    }
}
