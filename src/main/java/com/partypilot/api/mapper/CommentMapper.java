package com.partypilot.api.mapper;

import com.partypilot.api.dto.CommentDto;
import com.partypilot.api.exception.AppException;
import com.partypilot.api.model.Comment;
import com.partypilot.api.model.Event;
import com.partypilot.api.model.User;
import com.partypilot.api.repository.UserRepository;
import com.partypilot.api.service.EventService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@Mapper(componentModel = "spring")
public abstract class CommentMapper {
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected EventService eventService;

    @Mapping(source = "user_id", target = "user")
    @Mapping(source = "event_id", target = "event")
    public abstract Comment dtoToComment(CommentDto commentDto);

    protected User mapUser(Long user_id) {
        return userRepository.findById(user_id)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));
    }

    protected Event mapEvent(Long event_id) {
        return eventService.getEventById(event_id)
                .orElseThrow(() -> new AppException("Event not found", HttpStatus.NOT_FOUND));
    }
}
