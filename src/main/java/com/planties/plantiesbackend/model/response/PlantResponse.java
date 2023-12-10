package com.planties.plantiesbackend.model.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PlantResponse {
    public static Map<String, Object> generateJson(Object responseObj) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("plants", responseObj);

        return map;
    }
}
