package com.planties.plantiesbackend.repository;

import com.planties.plantiesbackend.model.entity.Oxygen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface OxygenRepository extends JpaRepository<Oxygen, UUID> {

    @Query("""
            SELECT u FROM Users u\s
            JOIN Oxygen o ON u.id = o.user_id\s
            ORDER BY o.oxygen
            """)
    List<Oxygen> findAllOrderByOxygen();
}
