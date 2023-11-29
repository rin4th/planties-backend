package com.planties.plantiesbackend.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;



@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    private String username;
    String password;
}
