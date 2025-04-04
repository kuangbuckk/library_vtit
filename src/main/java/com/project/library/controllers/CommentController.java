package com.project.library.controllers;

import com.project.library.utils.LocalizationUtils;
import com.project.library.dtos.CommentDTO;
import com.project.library.responses.CommentResponse;
import com.project.library.responses.GenericResponse;
import com.project.library.services.CommentService;
import com.project.library.utils.MessageKeys;
import com.project.library.utils.ResponseUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/comments")
@AllArgsConstructor
public class CommentController {
    private final CommentService commentService;
    private final LocalizationUtils localizationUtils;

    @GetMapping("/")
    @PreAuthorize("@customSecurityExpression.fileRole(#request)")
    public ResponseEntity<?> getAllComments() {
        return ResponseUtil.success(
                MessageKeys.GET_COMMENT_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.GET_COMMENT_SUCCESSFULLY),
                commentService.getAllComment()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCommentByCode(@PathVariable("id") Long id) {
        return ResponseUtil.success(
                MessageKeys.GET_COMMENT_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.GET_COMMENT_SUCCESSFULLY),
                commentService.getCommentById(id)
        );
    }

    @PostMapping("/create")
    @PreAuthorize("@customSecurityExpression.fileRole(#request)")
    public ResponseEntity<?> addComment(
            @RequestBody @Valid CommentDTO commentDTO,
            Authentication authentication,
            HttpServletRequest request
    ) {
        return ResponseUtil.success(
                MessageKeys.INSERT_COMMENT_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.INSERT_COMMENT_SUCCESSFULLY),
                commentService.addComment(authentication, commentDTO)
        );
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("@customSecurityExpression.fileRole(#request)")
    public ResponseEntity<?> updateComment(
            @PathVariable("id") Long id,
            @RequestBody @Valid CommentDTO commentDTO,
            Authentication authentication,
            HttpServletRequest request
    ) {
        return ResponseUtil.success(
                MessageKeys.UPDATE_COMMENT_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.UPDATE_COMMENT_SUCCESSFULLY),
                commentService.updateComment(authentication, commentDTO, id)
        );
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("@customSecurityExpression.fileRole(#request)")
    public ResponseEntity<?> deleteComment(
            @PathVariable("id") Long id,
            Authentication authentication,
            HttpServletRequest request
    ) {
        return ResponseUtil.success(
                MessageKeys.DELETE_COMMENT_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.DELETE_COMMENT_SUCCESSFULLY),
                commentService.deleteComment(authentication, id)
        );
    }

    @DeleteMapping("/destroy/{id}")
    @PreAuthorize("hasRole('ADMIN') OR hasRole('MANAGER')")
    public ResponseEntity<?> destroyComment(@PathVariable("id") Long id) {
        return ResponseUtil.success(
                MessageKeys.DESTROY_COMMENT_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.DESTROY_COMMENT_SUCCESSFULLY),
                commentService.destroyComment(id)
        );
    }
}
