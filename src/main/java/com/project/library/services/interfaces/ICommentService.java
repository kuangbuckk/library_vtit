package com.project.library.services.interfaces;

import com.project.library.dtos.CommentDTO;
import com.project.library.entities.Comment;

import java.util.List;

public interface ICommentService {
    List<Comment> getAllComment();
    Comment getCommentByCode(String code);
    Comment addComment(CommentDTO CommentDTO);
    Comment updateComment(CommentDTO CommentDTO, String code);
    void deleteComment(String code);
}
