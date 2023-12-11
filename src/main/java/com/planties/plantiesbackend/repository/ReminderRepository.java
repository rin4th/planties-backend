package com.planties.plantiesbackend.repository;


import com.planties.plantiesbackend.model.entity.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, UUID> {

    @Query("""
            Select r From Reminder r\s
            Join Garden g On r.garden_id = g.id\s
            where r.garden_id = :gardenId
            """)
    public List<Reminder> findAllReminderByGardenId(UUID gardenId);

}
