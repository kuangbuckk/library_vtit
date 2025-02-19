package com.project.library.controllers;

import com.project.library.dtos.CommentDTO;
import com.project.library.entities.Comment;
import com.project.library.services.interfaces.ICommentService;
import com.project.library.utils.ResponseUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
@AllArgsConstructor
public class CommentController {
    private final ICommentService commentService;

    @GetMapping("/")
    public ResponseEntity<?> getAllComments() {
        return ResponseUtil.success(HttpStatus.OK, "", commentService.getAllComment());
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> getCommentByCode(@PathVariable("code") String code) {
        return ResponseUtil.success(HttpStatus.OK, "", commentService.getCommentByCode(code));
    }

    @PostMapping("/")
    public ResponseEntity<?> addComment(@RequestBody @Valid CommentDTO commentDTO, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
            return ResponseUtil.error(HttpStatus.BAD_REQUEST, "", errors);
        }
        return ResponseUtil.success(HttpStatus.OK, "", commentService.addComment(commentDTO));
    }

    //TODO: add put and delete
}
