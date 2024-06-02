package com.partypilot.api.service;

import com.partypilot.api.dto.CommentDto;
import com.partypilot.api.exception.AppException;
import com.partypilot.api.mapper.CommentMapper;
import com.partypilot.api.model.Comment;
import com.partypilot.api.model.Event;
import com.partypilot.api.repository.CommentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final EventService eventService;

    public CommentService(CommentRepository commentRepository, CommentMapper commentMapper, EventService eventService) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
        this.eventService = eventService;
    }

    public List<CommentDto> getCommentsByEventId(Long eventId) {
        Event event = eventService.getEventById(eventId)
                .orElseThrow(() -> new AppException("Event not found.", HttpStatus.NOT_FOUND));
        List<Comment> comments = commentRepository.findByEvent(event);
        return comments.stream().map(commentMapper::commentToDto).collect(Collectors.toList());
    }

    public CommentDto saveComment(CommentDto commentDto) {
        Comment comment = commentMapper.dtoToComment(commentDto);
        comment.getEvent().addComment(comment);
        return commentMapper.commentToDto(commentRepository.save(comment));
    }

    public void deleteComment(Long commentId) {
        commentRepository.findById(commentId)
                        .map(comment -> {
                            comment.getEvent().removeComment(comment);
                            return null;
                        });
        commentRepository.deleteById(commentId);
    }

}
