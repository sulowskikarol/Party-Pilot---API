package com.partypilot.api.repository;

import com.partypilot.api.model.Comment;
import com.partypilot.api.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByEvent(Event event);
}
