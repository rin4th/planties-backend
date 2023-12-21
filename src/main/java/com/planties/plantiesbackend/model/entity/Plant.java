package com.planties.plantiesbackend.model.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Plant {
    @Id
    private UUID id;
    private String name;

    private String banner;

    private ArrayList<String> url_image;

    private UUID garden_id;

    private double oxygen;

    private String type;

    private LocalDate date;

    private UUID user_id;
}
