package com.planties.plantiesbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;
import com.planties.plantiesbackend.model.entity.Users;
import org.springframework.stereotype.Repository;

public interface UsersRepository extends JpaRepository<Users, UUID> {

    Optional<Users> findByEmail(String email);

    Optional<Users> findByUsername(String username);
}
