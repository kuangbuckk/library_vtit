package com.project.library.controllers;

import com.project.library.dtos.PostDTO;
import com.project.library.services.interfaces.IPostService;
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
@AllArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {
    private final IPostService postService;

    @GetMapping("/")
    public ResponseEntity<?> getAllComments() {
        return ResponseUtil.success(HttpStatus.OK, "", postService.getAllPosts());
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> getCommentByCode(@PathVariable("code") String code) {
        return ResponseUtil.success(HttpStatus.OK, "", postService.getPostByCode(code));
    }

    @PostMapping("/")
    public ResponseEntity<?> addComment(
            @RequestBody @Valid PostDTO postDTO,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseUtil.error(HttpStatus.BAD_REQUEST, "", errors);
        }
        return ResponseUtil.success(HttpStatus.OK, "", postService.addPost(postDTO));
    }

    //TODO: add put and delete
}
