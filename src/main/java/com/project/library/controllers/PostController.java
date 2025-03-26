package com.project.library.controllers;

import com.project.library.entities.Post;
import com.project.library.responses.PostResponse;
import com.project.library.utils.LocalizationUtils;
import com.project.library.dtos.PostDTO;
import com.project.library.dtos.search.PostSearchDTO;
import com.project.library.responses.GenericResponse;
import com.project.library.responses.PostPageResponse;
import com.project.library.services.PostService;
import com.project.library.utils.MessageKeys;
import com.project.library.utils.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("${api.prefix}/posts")
public class PostController {
    private final PostService postService;
    private final LocalizationUtils localizationUtils;

    @GetMapping("/")
    public ResponseEntity<GenericResponse<PostPageResponse>> getAllPosts(
            @RequestParam(value = "page_number", defaultValue = "0") int pageNumber,
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestBody PostSearchDTO postSearchDTO
    ) {
        PostPageResponse postPageResponse = postService.getAllPosts(pageNumber, size, postSearchDTO);
        return ResponseUtil.success(
                MessageKeys.GET_POST_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.GET_POST_SUCCESSFULLY),
                postPageResponse

        );
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<GenericResponse> getPostByCode(@PathVariable("id") Long id) {
//        PostResponse postResponse = postService.getPostByCode(id);
//        return ResponseEntity.ok(GenericResponse.success(postResponse));
//    }

    @PostMapping("/create")
    @PreAuthorize("@customSecurityExpression.fileRole(#httpServletRequest)")
    public ResponseEntity<GenericResponse<PostResponse>> addPost(
            @RequestBody @Valid PostDTO postDTO,
            Authentication authentication,
            HttpServletRequest httpServletRequest
    ) {
        return ResponseUtil.success(
                MessageKeys.INSERT_POST_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.INSERT_POST_SUCCESSFULLY),
                postService.addPost(authentication, postDTO)
        );
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("@customSecurityExpression.fileRole(#httpServletRequest) " +
            "AND @customSecurityExpression.isPostOwner(#id)")
    public ResponseEntity<GenericResponse<PostResponse>> updatePost(
            @RequestBody @Valid PostDTO postDTO,
            Authentication authentication,
            @PathVariable("id") Long id,
            HttpServletRequest httpServletRequest
    ) {
        return ResponseUtil.success(
                MessageKeys.UPDATE_POST_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.UPDATE_POST_SUCCESSFULLY),
                postService.updatePost(authentication, postDTO, id)
        );
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("@customSecurityExpression.fileRole(#httpServletRequest)")
    public ResponseEntity<GenericResponse<PostResponse>> deletePost(
            @PathVariable("id") Long id,
            Authentication authentication,
            HttpServletRequest httpServletRequest
    ) {
        return ResponseUtil.success(
                MessageKeys.DELETE_POST_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.DELETE_POST_SUCCESSFULLY),
                postService.deletePost(authentication, id)
        );
    }

    @DeleteMapping("/destroy/{id}")
    @PreAuthorize("@customSecurityExpression.fileRole(#httpServletRequest)")
    public ResponseEntity<GenericResponse<Long>> destroyPost(@PathVariable("id") Long id) {
        postService.destroyPost(id);
        return ResponseUtil.success(
                MessageKeys.DESTROY_POST_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.DESTROY_POST_SUCCESSFULLY),
                id
        );
    }
}
