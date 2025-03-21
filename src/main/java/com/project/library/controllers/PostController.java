package com.project.library.controllers;

import com.project.library.utils.LocalizationUtils;
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

//    @GetMapping("/{id}")
//    public ResponseEntity<GenericResponse> getPostByCode(@PathVariable("id") Long id) {
//        PostResponse postResponse = postService.getPostByCode(id);
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

    @PutMapping("/update/{id}")
    @PreAuthorize("@customSecurityExpression.fileRole(#httpServletRequest) " +
            "AND @customSecurityExpression.isPostOwner(#id)")
    public ResponseEntity<GenericResponse> updatePost(
            @RequestBody @Valid PostDTO postDTO,
            @PathVariable("id") Long id,
            HttpServletRequest httpServletRequest
    ) {
        return ResponseEntity.ok(GenericResponse.success(
                MessageKeys.UPDATE_POST_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.UPDATE_POST_SUCCESSFULLY),
                postService.updatePost(postDTO, id)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@customSecurityExpression.fileRole(#httpServletRequest) " +
            "AND @customSecurityExpression.isPostOwner(#id)")
    public ResponseEntity<GenericResponse> deletePost(
            @PathVariable("id") Long id,
            HttpServletRequest httpServletRequest
    ) {
        postService.deletePost(id);
        return ResponseEntity.ok().body(GenericResponse.success(id));
    }

    @DeleteMapping("/destroy/{id}")
    @PreAuthorize("@customSecurityExpression.fileRole(#httpServletRequest) " +
            "AND @customSecurityExpression.isPostOwner(#id)")
    public ResponseEntity<GenericResponse> destroyPost(@PathVariable("id") Long id) {
        postService.destroyPost(id);
        return ResponseEntity.ok(GenericResponse.success(
                MessageKeys.DELETE_POST_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.DELETE_POST_SUCCESSFULLY),
                id));
    }
}
