package com.project.library.controllers;

import com.project.library.dtos.CommentDTO;
import com.project.library.entities.Comment;
import com.project.library.responses.CommentResponse;
import com.project.library.responses.GenericResponse;
import com.project.library.services.interfaces.ICommentService;
import com.project.library.utils.ResponseUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/comments")
@AllArgsConstructor
public class CommentController {
    private final ICommentService commentService;

    @GetMapping("/")
    public ResponseEntity<?> getAllComments() {
        return ResponseEntity.ok(GenericResponse.success(commentService.getAllComment()));
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> getCommentByCode(@PathVariable("code") UUID code) {
        CommentResponse commentResponse = commentService.getCommentByCode(code);
        return ResponseEntity.ok(GenericResponse.success(commentResponse));
    }

    @PostMapping("/")
    public ResponseEntity<?> addComment(@RequestBody @Valid CommentDTO commentDTO, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(GenericResponse.error(errors.toString()));
        }
        return ResponseEntity.ok(GenericResponse.success(commentService.addComment(commentDTO)));
    }

    @PutMapping("/{code}")
    public ResponseEntity<?> updateComment(
            @PathVariable("code") UUID code,
            @RequestBody @Valid CommentDTO commentDTO,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(GenericResponse.error(errors.toString()));
        }
        return ResponseEntity.ok(commentService.updateComment(commentDTO, code));
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<?> deleteComment(@PathVariable("code") UUID code) {
        commentService.deleteComment(code);
        return ResponseEntity.ok(GenericResponse.success(code));
    }
}
