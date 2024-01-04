package com.planties.plantiesbackend.model.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ResponseHandler {
    public static ResponseEntity<Object> generateResponse(String status, String message, Object responseObj, HttpStatus httpStatus) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("message", message);
        if (responseObj != null){
            map.put("data", responseObj);
        }
        map.put("status", status);

        return new ResponseEntity<Object>(map, httpStatus);
    }
}

