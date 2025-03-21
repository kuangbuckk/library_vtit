package com.project.library.services;

import com.project.library.dtos.CommentDTO;
import com.project.library.responses.CommentResponse;

import java.util.List;

public interface CommentService {
    List<CommentResponse> getAllComment();
    CommentResponse getCommentById(Long id);
    CommentResponse addComment(CommentDTO CommentDTO);
    CommentResponse updateComment(CommentDTO CommentDTO, Long id);
    CommentResponse deleteComment(Long id);
    void destroyComment(Long id);
}
