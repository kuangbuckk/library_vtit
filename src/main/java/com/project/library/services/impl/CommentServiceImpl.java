package com.project.library.services.impl;

import com.project.library.dtos.CommentDTO;
import com.project.library.entities.Comment;
import com.project.library.entities.Post;
import com.project.library.entities.User;
import com.project.library.exceptions.DataNotFoundException;
import com.project.library.repositories.CommentRepository;
import com.project.library.repositories.PostRepository;
import com.project.library.repositories.UserRepository;
import com.project.library.responses.CommentResponse;
import com.project.library.services.CommentService;
import com.project.library.utils.MessageKeys;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {
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
    public CommentResponse getCommentById(Long id) {
        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(()->
                        new DataNotFoundException(MessageKeys.COMMENT_NOT_FOUND, id));
        return CommentResponse.fromComment(existingComment);
    }

    @Override
    @Transactional
    public CommentResponse addComment(CommentDTO commentDTO) {
        User existingUser = userRepository.findById(commentDTO.getUserCode())
                .orElseThrow(()->
                        new DataNotFoundException(MessageKeys.USER_NOT_FOUND, commentDTO.getUserCode()));
        Post existingPost = postRepository.findById(commentDTO.getPostCode())
                .orElseThrow(() ->
                        new DataNotFoundException(MessageKeys.POST_NOT_FOUND, commentDTO.getPostCode()));
        Comment newComment = Comment.builder()
                .content(commentDTO.getContent())
                .post(existingPost)
                .user(existingUser)
                .build();
        commentRepository.save(newComment);
        return CommentResponse.fromComment(newComment);
    }

    @Override
    @Transactional
    public CommentResponse updateComment(CommentDTO commentDTO, Long id) {
        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException(MessageKeys.COMMENT_NOT_FOUND, id));
        existingComment.setContent(commentDTO.getContent());
        return CommentResponse.fromComment(existingComment);
    }

    @Override
    @Transactional
    public CommentResponse deleteComment(Long id) {
        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException(MessageKeys.COMMENT_NOT_FOUND, id));
        existingComment.setIsDeleted(true);
        commentRepository.save(existingComment);
        return CommentResponse.fromComment(existingComment);
    }

    @Override
    @Transactional
    public void destroyComment(Long id) {
        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException(MessageKeys.COMMENT_NOT_FOUND, id));
        commentRepository.delete(existingComment);
    }
}
