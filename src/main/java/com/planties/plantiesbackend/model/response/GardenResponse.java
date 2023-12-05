package com.planties.plantiesbackend.model.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class GardenResponse {

    public static ResponseEntity<Object> generateResponse(String status, String message, Object responseObj, HttpStatus httpStatus) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("gardens", responseObj);

        return new ResponseEntity<Object>(map, httpStatus);
    }
}
