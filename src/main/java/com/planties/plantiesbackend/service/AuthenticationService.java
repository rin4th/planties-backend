package com.planties.plantiesbackend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.planties.plantiesbackend.configuration.CustomException;
import com.planties.plantiesbackend.model.entity.Oxygen;
import com.planties.plantiesbackend.model.entity.Token;
import com.planties.plantiesbackend.model.entity.Users;
import com.planties.plantiesbackend.model.request.LoginRequest;
import com.planties.plantiesbackend.model.request.RegisterRequest;
import com.planties.plantiesbackend.model.response.AuthenticationResponse;
import com.planties.plantiesbackend.repository.OxygenRepository;
import com.planties.plantiesbackend.repository.TokenRepository;
import com.planties.plantiesbackend.repository.UsersRepository;
import com.planties.plantiesbackend.service.interfaces.AuthenticationInterface;
import com.planties.plantiesbackend.utils.TokenType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AuthenticationService implements AuthenticationInterface {

    private final UsersRepository usersRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;
    private final OxygenRepository oxygenRepository;
    private final OxygenService oxygenService;


    public AuthenticationResponse register(RegisterRequest request) {
        if (request.getUsername().isEmpty()  || request.getFullname().isEmpty() || request.getPassword().isEmpty()){
            throw new CustomException.BadRequestException("tidak dapat membuat user baru karena properti yang dibutuhkan tidak ada");
        }

        Optional<Users> existUsername = usersRepository.findByUsername(request.getUsername());
        if (existUsername.isPresent()){
            throw new CustomException.UsernameTakenException("Tidak dapat membuat user baru dengan username yang telah dipakai");
        }

        var user = Users.builder()
                .username(request.getUsername())
                .fullname(request.getFullname())
                .password(passwordEncoder.encode(request.getPassword()))
                .url_image("https://rpl-pbo-sister.s3.ap-southeast-1.amazonaws.com/pbo/profile.png") // profile default
                .role("client")
                .build();
        var savedUser = usersRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(savedUser, jwtToken);
        Oxygen oxygenEntity = Oxygen.builder()
                .user_id(user.getId())
                .oxygen(0)
                .username(user.getUsername())
                .rank(999)
                .build();
        oxygenRepository.save(oxygenEntity);


        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .role(user.getRole())
                .build();
    }

    public AuthenticationResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        var user = usersRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new CustomException.WrongCredentialException("Username atau Passowrd salah"));


        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .role(user.getRole())
                .build();
    }

    private void saveUserToken(Users user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(Users user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userUsername;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);
        userUsername = jwtService.extractUsername(refreshToken);
        if (userUsername != null) {
            var user = this.usersRepository.findByUsername(userUsername)
                    .orElseThrow();
            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    public void logout(
            HttpServletRequest authorization
    )throws IOException{
        final String authHeader = authorization.getHeader(HttpHeaders.AUTHORIZATION);
        final String jwt;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            return;
        }
        jwt = authHeader.substring(7);
        var storedToken = tokenRepository.findByToken(jwt)
                .orElse(null);
        if (storedToken != null) {
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            tokenRepository.save(storedToken);
            SecurityContextHolder.clearContext();
        }
    }

}
