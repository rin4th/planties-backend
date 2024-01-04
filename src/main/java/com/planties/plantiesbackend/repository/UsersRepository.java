package com.planties.plantiesbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

import com.planties.plantiesbackend.model.entity.Users;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, UUID> {

    @Query("SELECT u FROM Users u WHERE u.username = :username ")
    Optional<Users> findByUsername(@Param("username") String username);

    @Query("SELECT u.id FROM Users u WHERE u.username = :username")
    Optional<UUID> findIdByUsername(@Param("username") String username);


    @Query("Select u From Users u Where u.username = :username And u.password = :password")
    Optional<Users> login(@Param("username") String username, @Param("password") String password);
}
