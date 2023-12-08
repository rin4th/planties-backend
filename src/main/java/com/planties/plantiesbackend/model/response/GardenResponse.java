package com.planties.plantiesbackend.model.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class GardenResponse {

    public static Map<String, Object> generateJson(Object responseObj) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("gardens", responseObj);

        return map;
    }
}
