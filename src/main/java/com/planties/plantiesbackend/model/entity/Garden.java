package com.planties.plantiesbackend.model.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Garden {
    @Id
    private UUID id;
    private String name;
    private ArrayList<String> url_image;
    private String type;

    private LocalDate date;


    private UUID user_id;

}
