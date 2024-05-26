package com.partypilot.api.controller;

import com.partypilot.api.dto.CommentDto;
import com.partypilot.api.model.Comment;
import com.partypilot.api.repository.CommentRepository;
import com.partypilot.api.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }
//    @GetMapping
//    public List<Comment> getComments() {
//        return commentRepository.findAll();
//    }
//
//    @PostMapping
//    public Comment addComment(@RequestBody Comment comment) {
//        return commentRepository.save(comment);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Comment> getComment(@PathVariable Long id) {
//        return commentRepository.findById(id)
//                .map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.notFound().build());
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deleteComment(@PathVariable Long id) {
//        return commentRepository.findById(id)
//                .map(comment -> {
//                    commentRepository.delete(comment);
//                    return ResponseEntity.ok().build();
//                }).orElseGet(() -> ResponseEntity.notFound().build());
//    }

    @PostMapping
    public ResponseEntity<CommentDto> addComment(@RequestBody CommentDto commentDto) {
        CommentDto savedComment = commentService.saveComment(commentDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedComment);

    }

    @GetMapping("/{eventId}")
    public ResponseEntity<List<CommentDto>> getComment(@PathVariable Long eventId) {
        return ResponseEntity.ok(commentService.getCommentsByEventId(eventId));
    }
}
