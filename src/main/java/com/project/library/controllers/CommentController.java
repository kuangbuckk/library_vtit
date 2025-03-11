package com.project.library.controllers;

import com.project.library.components.LocalizationUtils;
import com.project.library.dtos.CommentDTO;
import com.project.library.entities.Comment;
import com.project.library.responses.CommentResponse;
import com.project.library.responses.GenericResponse;
import com.project.library.services.interfaces.ICommentService;
import com.project.library.utils.MessageKeys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final LocalizationUtils localizationUtils;

    @GetMapping("/")
    @PreAuthorize("@customSecurityExpression.fileRole(#request)")
    public ResponseEntity<?> getAllComments() {
        return ResponseEntity.ok(GenericResponse.success(commentService.getAllComment()));
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> getCommentByCode(@PathVariable("code") UUID code) {
        CommentResponse commentResponse = commentService.getCommentByCode(code);
        return ResponseEntity.ok(GenericResponse.success(commentResponse));
    }

    @PostMapping("/create")
    @PreAuthorize("@customSecurityExpression.fileRole(#request)")
    public ResponseEntity<?> addComment(@RequestBody @Valid CommentDTO commentDTO, HttpServletRequest request) {
        return ResponseEntity.ok(GenericResponse.success(
                MessageKeys.INSERT_COMMENT_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.INSERT_COMMENT_SUCCESSFULLY),
                commentService.addComment(commentDTO)));
    }

    @PutMapping("/update/{code}")
    @PreAuthorize("@customSecurityExpression.fileRole(#request) AND @customSecurityExpression.isCommentOwner(#code)")
    public ResponseEntity<?> updateComment(
            @PathVariable("code") UUID code,
            @RequestBody @Valid CommentDTO commentDTO,
            HttpServletRequest request
    ) {
        return ResponseEntity.ok(GenericResponse.success(
                MessageKeys.UPDATE_COMMENT_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.UPDATE_COMMENT_SUCCESSFULLY),
                commentService.updateComment(commentDTO, code))
        );
    }

    @DeleteMapping("/{code}")
    @PreAuthorize("@customSecurityExpression.fileRole(#request) AND @customSecurityExpression.isCommentOwner(#code)")
    public ResponseEntity<?> deleteComment(@PathVariable("code") UUID code, HttpServletRequest request) {
        commentService.deleteComment(code);
        return ResponseEntity.ok(GenericResponse.success(
                MessageKeys.DELETE_COMMENT_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.DELETE_COMMENT_SUCCESSFULLY),
                code));
    }

    @DeleteMapping("/destroy/{code}")
    @PreAuthorize("hasRole('ADMIN') OR hasRole('MANAGER')")
    public ResponseEntity<?> destroyComment(@PathVariable("code") UUID code) {
        commentService.destroyComment(code);
        return ResponseEntity.ok(GenericResponse.success(
                MessageKeys.DELETE_BOOK_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.DELETE_BOOK_SUCCESSFULLY),
                code));
    }
}
