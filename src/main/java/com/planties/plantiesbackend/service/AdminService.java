package com.planties.plantiesbackend.service;


import com.planties.plantiesbackend.configuration.CustomException;
import com.planties.plantiesbackend.model.entity.Users;
import com.planties.plantiesbackend.repository.AdminRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final UsersService usersService;

    public int getTotalUsers(
            HttpServletRequest authorization
    ){
        Users user = usersService.checkUsers(authorization);

        String role = user.getRole();
        if (!role.equals("admin")){
            throw new CustomException.AccessDeniedException("Anda Bukan Admin");
        }
        return adminRepository.getTotalUser();
    }

    public Map<String, Object> getPopularPlant(
            HttpServletRequest authorization
    ){
        Users user = usersService.checkUsers(authorization);

        String role = user.getRole();
        if (!role.equals("admin")){
            throw new CustomException.AccessDeniedException("Anda Bukan Admin");
        }

        int tnmBuah = adminRepository.getTotalBuah();
        int tnmBunga = adminRepository.getTotalBerbunga();
        int tnmAir = adminRepository.getTotalAir();
        int tnmDaunHijau = adminRepository.getTotalDaunHijau();
        int totalPlant = tnmBuah + tnmBunga + tnmAir + tnmDaunHijau;

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("Tanaman Buah", ((double) tnmBuah / totalPlant) * 100);
        map.put("Tanaman Berbunga",  ((double) tnmBunga / totalPlant) * 100);
        map.put("Tanaman Air", ((double) tnmAir / totalPlant) * 100);
        map.put("Tanaman Daun Hijau", ((double) tnmDaunHijau / totalPlant) * 100);
        map.put("Total Tanaman", totalPlant);

        return map;
    }

    public int getNewUser(
            HttpServletRequest authorization
    ){
        Users user = usersService.checkUsers(authorization);

        String role = user.getRole();
        if (!role.equals("admin")){
            throw new CustomException.AccessDeniedException("Anda Bukan Admin");
        }

        return adminRepository.getTotalNewUser();
    }

}
