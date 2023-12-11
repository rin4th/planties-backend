package com.planties.plantiesbackend.model.response;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ReminderResponse {
    public static Map<String, Object> generateJson(Object responseObj) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("reminder", responseObj);

        return map;
    }
}
