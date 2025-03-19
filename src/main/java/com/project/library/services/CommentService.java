package com.project.library.services;

import com.project.library.dtos.CommentDTO;
import com.project.library.responses.CommentResponse;

import java.util.List;
import java.util.UUID;

public interface CommentService {
    List<CommentResponse> getAllComment();
    CommentResponse getCommentByCode(UUID code);
    CommentResponse addComment(CommentDTO CommentDTO);
    CommentResponse updateComment(CommentDTO CommentDTO, UUID code);
    CommentResponse deleteComment(UUID code);
    void destroyComment(UUID code);
}
