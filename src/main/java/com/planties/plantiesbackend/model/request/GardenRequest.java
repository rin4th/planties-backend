package com.planties.plantiesbackend.model.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GardenRequest {

    private String name;
    private String type;
    private ArrayList<String> imageBase64;
}
