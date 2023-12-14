package com.planties.plantiesbackend.service;

import com.planties.plantiesbackend.configuration.CustomException;
import com.planties.plantiesbackend.model.entity.Users;
import com.planties.plantiesbackend.repository.UsersRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsersService {

    private final JwtService jwtService;
    private final UsersRepository usersRepository;

    public Users getProfile(HttpServletRequest authorization) {
        final String authHeader = authorization.getHeader(HttpHeaders.AUTHORIZATION);
        final String token;
        final String userUsername;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            throw new CustomException.InvalidTokenException("Not a Bearer token");
        }
        token = authHeader.substring(7);
        userUsername = jwtService.extractUsername(token);

        if (userUsername == null){
            throw new CustomException.InvalidTokenException("Invalid Token");
        }
        Users user = this.usersRepository.findByUsername(userUsername)
                .orElseThrow(() -> new CustomException.UsernameNotFoundException("User tidak ditemukan"));

        var userID = user.getId();
        if (userID == null){
            throw new CustomException.UsernameNotFoundException("User tidak ditemukan");
        }
        return user;
    }


}
