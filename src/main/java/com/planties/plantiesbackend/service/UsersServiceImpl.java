package com.planties.plantiesbackend.service;

import com.planties.plantiesbackend.model.entity.Users;
import com.planties.plantiesbackend.model.request.LoginRequest;
import com.planties.plantiesbackend.model.request.RegisterRequest;
import com.planties.plantiesbackend.model.response.AuthenticationResponse;
import com.planties.plantiesbackend.repository.UsersRepository;
import com.planties.plantiesbackend.service.intefaces.UsersService;
import org.springframework.security.crypto.password.PasswordEncoder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {

    @Override
    public ProfileViewResponse profile(Member member) {
        return null;
    }

    @Override
    public ProfileViewResponse profile(Users user) {
        return null;
    }

    @Override
    public ProfileViewResponse profileUpdate(Users user, ProfileUpdateRequest profileUpdateRequest) {
        return null;
    }

    @Override
    public ProfileViewResponse profileImage(Users user, HttpServletRequest request) {
        return null;
    }
}
