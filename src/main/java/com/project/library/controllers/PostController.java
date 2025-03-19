package com.project.library.controllers;

import com.project.library.components.LocalizationUtils;
import com.project.library.dtos.PostDTO;
import com.project.library.dtos.search.PostSearchDTO;
import com.project.library.responses.GenericResponse;
import com.project.library.responses.PostPageResponse;
import com.project.library.services.PostService;
import com.project.library.utils.MessageKeys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("${api.prefix}/posts")
public class PostController {
    private final PostService postService;
    private final LocalizationUtils localizationUtils;

    @GetMapping("/")
    public ResponseEntity<GenericResponse> getAllPosts(
            @RequestParam(value = "page_number", defaultValue = "0") int pageNumber,
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestBody PostSearchDTO postSearchDTO
            ) {
        PostPageResponse postPageResponse = postService.getAllPosts(pageNumber, size, postSearchDTO);
        return ResponseEntity.ok(GenericResponse.success(postPageResponse));
    }

//    @GetMapping("/{code}")
//    public ResponseEntity<GenericResponse> getPostByCode(@PathVariable("code") UUID code) {
//        PostResponse postResponse = postService.getPostByCode(code);
//        return ResponseEntity.ok(GenericResponse.success(postResponse));
//    }

    @PostMapping("/create")
    @PreAuthorize("@customSecurityExpression.fileRole(#httpServletRequest)")
    public ResponseEntity<GenericResponse> addPost(
            @RequestBody @Valid PostDTO postDTO,
            HttpServletRequest httpServletRequest
    ) {
        return ResponseEntity.ok(GenericResponse.success(
                MessageKeys.INSERT_POST_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.INSERT_POST_SUCCESSFULLY),
                postService.addPost(postDTO)));
    }

    @PutMapping("/update/{code}")
    @PreAuthorize("@customSecurityExpression.fileRole(#httpServletRequest) " +
            "AND @customSecurityExpression.isPostOwner(#code)")
    public ResponseEntity<GenericResponse> updatePost(
            @RequestBody @Valid PostDTO postDTO,
            @PathVariable("code") UUID code,
            HttpServletRequest httpServletRequest
    ) {
        return ResponseEntity.ok(GenericResponse.success(
                MessageKeys.UPDATE_POST_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.UPDATE_POST_SUCCESSFULLY),
                postService.updatePost(postDTO, code)));
    }

    @DeleteMapping("/{code}")
    @PreAuthorize("@customSecurityExpression.fileRole(#httpServletRequest) " +
            "AND @customSecurityExpression.isPostOwner(#code)")
    public ResponseEntity<GenericResponse> deletePost(
            @PathVariable("code") UUID code,
            HttpServletRequest httpServletRequest
    ) {
        postService.deletePost(code);
        return ResponseEntity.ok().body(GenericResponse.success(code));
    }

    @DeleteMapping("/destroy/{code}")
    @PreAuthorize("@customSecurityExpression.fileRole(#httpServletRequest) " +
            "AND @customSecurityExpression.isPostOwner(#code)")
    public ResponseEntity<GenericResponse> destroyPost(@PathVariable("code") UUID code) {
        postService.destroyPost(code);
        return ResponseEntity.ok(GenericResponse.success(
                MessageKeys.DELETE_POST_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.DELETE_POST_SUCCESSFULLY),
                code));
    }
}
