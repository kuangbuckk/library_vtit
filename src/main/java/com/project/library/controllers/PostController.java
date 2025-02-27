package com.project.library.controllers;

import com.project.library.components.LocalizationUtils;
import com.project.library.dtos.PostDTO;
import com.project.library.responses.GenericResponse;
import com.project.library.responses.PostPageResponse;
import com.project.library.responses.PostResponse;
import com.project.library.services.interfaces.IPostService;
import com.project.library.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
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
    private final LocalizationUtils localizationUtils;

    @GetMapping("/")
    public ResponseEntity<GenericResponse> getAllPosts(
            @RequestParam("page_number") int pageNumber,
            @RequestParam("size") int size,
            @RequestParam("keyword") String keyword
    ) {
        PostPageResponse postPageResponse = postService.getAllPosts(pageNumber, size, keyword);
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
            return ResponseEntity.badRequest()
                    .body(GenericResponse.error(
                            MessageKeys.INSERT_POST_FAILED,
                            errors.toString())
                    );
        }
        return ResponseEntity.ok(GenericResponse.success(
                MessageKeys.INSERT_POST_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.INSERT_POST_SUCCESSFULLY),
                postService.addPost(postDTO)));
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
            return ResponseEntity.badRequest()
                    .body(GenericResponse.error(MessageKeys.UPDATE_POST_FAILED, errors.toString()));
        }
        return ResponseEntity.ok(GenericResponse.success(
                MessageKeys.UPDATE_POST_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.UPDATE_POST_SUCCESSFULLY),
                postService.updatePost(postDTO, code)));
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<GenericResponse> deletePost(@PathVariable("code") UUID code) {
        postService.deletePost(code);
        return ResponseEntity.ok().body(GenericResponse.success(code));
    }
}
