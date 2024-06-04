package com.partypilot.api.repository;

import com.partypilot.api.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("SELECT e FROM Event e LEFT JOIN FETCH e.comments WHERE e.id = :id")
    Optional<Event> findByIdWithComments(@Param("id") Long id);

    List<Event> findByEndTimeAfter(LocalDateTime now);
}
