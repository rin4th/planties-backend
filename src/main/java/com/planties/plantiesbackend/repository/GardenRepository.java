package com.planties.plantiesbackend.repository;

import com.planties.plantiesbackend.model.entity.Garden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;



public interface GardenRepository extends JpaRepository<Garden, UUID> {

    @Query("SELECT g FROM Garden g join Users u on g.user_id = u.id where g.user_id = :id")
    List<Garden> findAllGardensByUserId(UUID id);


    @Query("""
            Select g From Garden g join Users u\s
            on g.user_id = u.id where u.id = :id\s
            and (g.name = :name and g.type = :type)
            """)
    Optional<Garden> findGardenByNameAndType(UUID id, String name, String type);
}
