package com.planties.plantiesbackend.model.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlantRequest {
    private String name;
    private String banner;
    private ArrayList<String> imageBase64;
}
