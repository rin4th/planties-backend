package com.planties.plantiesbackend.service.intefaces;

import com.planties.plantiesbackend.model.entity.Users;
import com.planties.plantiesbackend.model.request.RegisterRequest;
import com.planties.plantiesbackend.model.response.AuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthenticationService {

    public AuthenticationResponse register(RegisterRequest request);

    public AuthenticationResponse authenticate(AuthenticationRequest request);

    private void saveUserToken(Users user, String jwtToken);

    private void revokeAllUserTokens(Users user);

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException;
}
