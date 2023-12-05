package com.planties.plantiesbackend.model.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class PlantResponse {
    public static ResponseEntity<Object> generateResponse(String status, String message, Object responseObj, HttpStatus httpStatus) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("plants", responseObj);

        return new ResponseEntity<Object>(map, httpStatus);
    }
}
