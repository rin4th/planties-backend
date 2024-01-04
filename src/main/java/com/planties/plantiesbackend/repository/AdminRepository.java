package com.planties.plantiesbackend.repository;

import com.planties.plantiesbackend.model.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface AdminRepository extends JpaRepository<Users, UUID> {

    @Query("SELECT COUNT(id) FROM Users")
    int getTotalUser();

    @Query("SELECT COUNT(u.id) FROM Users u Join Oxygen o On u.id = o.user_id Where o.rank = 999")
    int getTotalNewUser();

    @Query("SELECT COUNT(id) FROM Plant p WHERE p.type LIKE 'Tanaman Berbunga'")
    int getTotalBerbunga();

    @Query("SELECT COUNT(id) FROM Plant p WHERE p.type LIKE 'Tanaman Daun Hijau'")
    int getTotalDaunHijau();

    @Query("SELECT COUNT(id) FROM Plant p WHERE p.type LIKE 'Tanaman Air'")
    int getTotalAir();

    @Query("SELECT COUNT(id) FROM Plant p WHERE p.type LIKE 'Tanaman Buah'")
    int getTotalBuah();
}
