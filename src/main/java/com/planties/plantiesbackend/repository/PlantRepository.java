package com.planties.plantiesbackend.repository;

import com.planties.plantiesbackend.model.entity.Plant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;
import java.util.List;

public interface PlantRepository extends JpaRepository<Plant, UUID> {

    @Query("""
            Select p From Plant p\s
            Join Garden g On p.garden_id = g.id\s
            where p.garden_id = :gardenId\s
            Order By p.date ASC
            """)
    List<Plant> findAllPlantByGardenIdASC(@Param("gardenId") UUID gardenId);

    @Query("""
            Select p From Plant p\s
            Join Garden g On p.garden_id = g.id\s
            where p.garden_id = :gardenId\s
            Order By p.date DESC
            """)
    List<Plant> findAllPlantByGardenIdDESC(@Param("gardenId") UUID gardenId);

    @Query("""
            Select p From Plant p\s
            Join Garden g On p.garden_id = g.id\s
            where p.id = :plantId And p.garden_id = :gardenId
            """)
    Plant findPlantByIdByGardenId(@Param("plantId") UUID plantId, @Param("gardenId") UUID gardenId);

    @Query("""
            Select p From Plant p\s
            Join Users u On p.user_id = u.id\s
            where p.user_id = :userId\s
            Order By p.date ASC
            """)
    List<Plant> findAllPlantByUserIdASC(@Param("userId") UUID userId);

    @Query("""
            Select p From Plant p\s
            Join Users u On p.user_id = u.id\s
            where p.user_id = :userId\s
            Order By p.date DESC
            """)
    List<Plant> findAllPlantByUserIdDESC(@Param("userId") UUID userId);

    @Query("""
            Select p From Plant p\s
            Join Users u On p.user_id = u.id \s
            where p.name LIKE %:plantName%
            """)
    List<Plant> findPlantByName(@Param("plantName") String plantName);

//    @Query("""
//            Select p From Plant p\s
//            Join Users u On p.user_id = u.id\s
//            where p.user_id = :userId\s
//            CASE WHEN :sort = 'ASC' THEN p.date END ASC,\s
//            CASE WHEN :sort = 'DESC' THEN p.date END DESC
//            """)
//    List<Plant> findAllPlantByUserIdSortBy(@Param("userId") UUID userId, @Param("sort") String sort);
}
