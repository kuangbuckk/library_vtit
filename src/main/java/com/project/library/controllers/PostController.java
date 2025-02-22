package com.project.library.controllers;

import com.project.library.dtos.PostDTO;
import com.project.library.responses.GenericResponse;
import com.project.library.responses.PostPageResponse;
import com.project.library.responses.PostResponse;
import com.project.library.services.interfaces.IPostService;
import com.project.library.utils.ResponseUtil;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("${api.prefix}/posts")
public class PostController {
    private final IPostService postService;

    @GetMapping("/")
    public ResponseEntity<GenericResponse> getAllPosts(
            @RequestParam("page_number") int pageNumber,
            @RequestParam("size") int size
    ) {
        PostPageResponse postPageResponse = postService.getAllPosts(PageRequest.of(pageNumber, size));
        return ResponseEntity.ok(GenericResponse.success(postPageResponse));
    }

    @GetMapping("/{code}")
    public ResponseEntity<GenericResponse> getPostByCode(@PathVariable("code") UUID code) {
        PostResponse postResponse = postService.getPostByCode(code);
        return ResponseEntity.ok(GenericResponse.success(postResponse));
    }

    @PostMapping("/")
    public ResponseEntity<GenericResponse> addPost(
            @RequestBody @Valid PostDTO postDTO,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(GenericResponse.error(errors.toString()));
        }
        return ResponseEntity.ok(GenericResponse.success(postService.addPost(postDTO)));
    }

    @PutMapping("/{code}")
    public ResponseEntity<GenericResponse> updatePost(
            @RequestBody @Valid PostDTO postDTO,
            BindingResult result,
            @PathVariable("code") UUID code
    ) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(GenericResponse.error(errors.toString()));
        }
        return ResponseEntity.ok(GenericResponse.success(postService.updatePost(postDTO, code)));
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<GenericResponse> deletePost(@PathVariable("code") UUID code) {
        postService.deletePost(code);
        return ResponseEntity.ok().body(GenericResponse.success(code));
    }
}
