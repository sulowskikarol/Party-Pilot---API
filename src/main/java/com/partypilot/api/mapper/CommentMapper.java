package com.partypilot.api.mapper;

import com.partypilot.api.dto.CommentDto;
import com.partypilot.api.exception.AppException;
import com.partypilot.api.model.Comment;
import com.partypilot.api.model.Event;
import com.partypilot.api.model.User;
import com.partypilot.api.repository.EventRepository;
import com.partypilot.api.repository.UserRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

@Mapper(componentModel = "spring")
public abstract class CommentMapper {
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected EventRepository eventRepository;

    @Mapping(source = "user_id", target = "user")
    @Mapping(source = "event_id", target = "event")
    @Mapping(source = "commentContent", target = "content")
    public abstract Comment dtoToComment(CommentDto commentDto);

    @Mapping(source = "user.id", target = "user_id")
    @Mapping(source = "event.id", target = "event_id")
    @Mapping(source = "user.profilePhotoPath", target = "userPhoto")
    @Mapping(source = "user.firstName", target = "userFirstName")
    @Mapping(source = "content", target = "commentContent")
    public abstract CommentDto commentToDto(Comment comment);

    protected User mapUser(Long user_id) {
        return userRepository.findById(user_id)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));
    }

    protected Event mapEvent(Long event_id) {
        return eventRepository.findById(event_id)
                .orElseThrow(() -> new AppException("Event not found", HttpStatus.NOT_FOUND));
    }

}
