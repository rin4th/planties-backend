package com.planties.plantiesbackend.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReminderRequest {
    private String name;
    private String type;
    private Integer duration;
}
