package com.project.library.services;

import com.project.library.dtos.CommentDTO;
import com.project.library.entities.Comment;
import com.project.library.entities.Post;
import com.project.library.entities.User;
import com.project.library.exceptions.DataNotFoundException;
import com.project.library.repositories.CommentRepository;
import com.project.library.repositories.PostRepository;
import com.project.library.repositories.UserRepository;
import com.project.library.services.interfaces.ICommentService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements ICommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    public List<Comment> getAllComment() {
        List<Comment> comments = commentRepository.findAll();
        return comments;
    }

    @Override
    public Comment getCommentByCode(String code) {
        Comment existingComment = commentRepository.findById(UUID.fromString(code))
                .orElseThrow(()->
                        new DataNotFoundException("Comment not found with code " + code));
        return existingComment;
    }

    @Override
    public Comment addComment(CommentDTO commentDTO) {
        User existingUser = userRepository.findById(UUID.fromString(commentDTO.getUserCode()))
                .orElseThrow(()->
                        new DataNotFoundException("User not found with code " + commentDTO.getUserCode()));
        Post existingPost = postRepository.findById(UUID.fromString(commentDTO.getPostCode()))
                .orElseThrow(() ->
                        new DataNotFoundException("Post not found with code " + commentDTO.getPostCode()));
        Comment newComment = Comment.builder()
                .content(commentDTO.getContent())
                .post(existingPost)
                .user(existingUser)
                .build();
        return commentRepository.save(newComment);
    }

    @Override
    public Comment updateComment(CommentDTO commentDTO, String code) {
        Comment existingComment = getCommentByCode(code);
        existingComment.setContent(commentDTO.getContent());
        return existingComment;
    }

    @Override
    public void deleteComment(String code) {
        commentRepository.deleteById(UUID.fromString(code));
    }
}
