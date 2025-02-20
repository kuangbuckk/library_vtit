package com.project.library.services;

import com.project.library.dtos.CommentDTO;
import com.project.library.entities.Comment;
import com.project.library.entities.Post;
import com.project.library.entities.User;
import com.project.library.exceptions.DataNotFoundException;
import com.project.library.repositories.CommentRepository;
import com.project.library.repositories.PostRepository;
import com.project.library.repositories.UserRepository;
import com.project.library.responses.CommentResponse;
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
    public List<CommentResponse> getAllComment() {
        List<CommentResponse> comments = commentRepository.findAll()
                .stream()
                .map(comment -> CommentResponse.fromComment(comment))
                .toList();
        return comments;
    }

    @Override
    public CommentResponse getCommentByCode(UUID code) {
        Comment existingComment = commentRepository.findById(code)
                .orElseThrow(()->
                        new DataNotFoundException("Comment not found with code " + code));
        return CommentResponse.fromComment(existingComment);
    }

    @Override
    public CommentResponse addComment(CommentDTO commentDTO) {
        User existingUser = userRepository.findById(commentDTO.getUserCode())
                .orElseThrow(()->
                        new DataNotFoundException("User not found with code " + commentDTO.getUserCode()));
        Post existingPost = postRepository.findById(commentDTO.getPostCode())
                .orElseThrow(() ->
                        new DataNotFoundException("Post not found with code " + commentDTO.getPostCode()));
        Comment newComment = Comment.builder()
                .content(commentDTO.getContent())
                .post(existingPost)
                .user(existingUser)
                .build();
        commentRepository.save(newComment);
        return CommentResponse.fromComment(newComment);
    }

    @Override
    public CommentResponse updateComment(CommentDTO commentDTO, UUID code) {
        Comment existingComment = commentRepository.findById(code)
                .orElseThrow(()-> new DataNotFoundException("Comment not found with code " + code));
        existingComment.setContent(commentDTO.getContent());
        return CommentResponse.fromComment(existingComment);
    }

    @Override
    public void deleteComment(UUID code) {
        commentRepository.deleteById(code);
    }
}
