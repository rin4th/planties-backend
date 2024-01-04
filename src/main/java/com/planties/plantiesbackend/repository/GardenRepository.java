package com.planties.plantiesbackend.repository;

import com.planties.plantiesbackend.model.entity.Garden;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;


@Repository
public interface GardenRepository extends JpaRepository<Garden, UUID> {

    @Query("SELECT g FROM Garden g join Users u on g.user_id = u.id where g.user_id = :id")
    List<Garden> findAllGardensByUserId(@Param("id") UUID id);

    @Query("""
            SELECT g FROM Garden g JOIN Users u\s
            ON g.user_id = u.id\s
            WHERE g.user_id = :id\s
            AND g.type = :type\s
            ORDER BY g.date ASC
            """)
    List<Garden> findAllGardensByTypeASC(@Param("id")UUID id,@Param("type") String type);

    @Query("""
            SELECT g FROM Garden g JOIN Users u
            ON g.user_id = u.id
            WHERE g.user_id = :id
            AND g.type = :type
            ORDER BY g.date DESC
            """)
    List<Garden> findAllGardensByTypeDESC(@Param("id")UUID id,@Param("type") String type);

    @Query("""
            Select g From Garden g join Users u\s
            on g.user_id = u.id where u.id = :id\s
            and (g.name = :name and g.type = :type)
            """)
    Optional<Garden> findGardenByNameAndType(@Param("id") UUID id, @Param("name") String name, @Param("type") String type);

    @Query("Select COUNT(g) From Garden g Where g.user_id = :id")
    int countGardenOwned(@Param("id") UUID id);

}
