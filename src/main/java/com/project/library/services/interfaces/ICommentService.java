package com.project.library.services.interfaces;

import com.project.library.dtos.CommentDTO;
import com.project.library.entities.Comment;
import com.project.library.responses.CommentResponse;

import java.util.List;
import java.util.UUID;

public interface ICommentService {
    List<CommentResponse> getAllComment();
    CommentResponse getCommentByCode(UUID code);
    CommentResponse addComment(CommentDTO CommentDTO);
    CommentResponse updateComment(CommentDTO CommentDTO, UUID code);
    void deleteComment(UUID code);
}
