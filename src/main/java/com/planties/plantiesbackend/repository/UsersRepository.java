package com.planties.plantiesbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

import com.planties.plantiesbackend.model.entity.Users;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, UUID> {

    @Query("SELECT u FROM Users u WHERE u.username = :username ")
    Optional<Users> findByUsername(String username);

    @Query("SELECT u.id FROM Users u WHERE u.username = ?1")
    Optional<UUID> findIdByUsername(String username);

    Optional<Users> findByEmail(String email);

}
