package com.project.library.services;

import com.project.library.dtos.CommentDTO;
import com.project.library.responses.CommentResponse;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface CommentService {
    List<CommentResponse> getAllComment();
    CommentResponse getCommentById(Long id);
    CommentResponse addComment(Authentication authentication, CommentDTO CommentDTO);
    CommentResponse updateComment(Authentication authentication, CommentDTO CommentDTO, Long id);
    CommentResponse deleteComment(Authentication authentication, Long id);
    Long destroyComment(Long id);
}
