package com.project.library.services.impl;

import com.project.library.dtos.CommentDTO;
import com.project.library.entities.Comment;
import com.project.library.entities.Post;
import com.project.library.entities.User;
import com.project.library.exceptions.DataNotFoundException;
import com.project.library.exceptions.InvalidOwnerException;
import com.project.library.repositories.CommentRepository;
import com.project.library.repositories.PostRepository;
import com.project.library.repositories.UserRepository;
import com.project.library.responses.CommentResponse;
import com.project.library.services.CommentService;
import com.project.library.utils.MessageKeys;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
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
    public CommentResponse addComment(Authentication authentication, CommentDTO commentDTO) {
        User existingUser = this.getCurrentAuthenticatedUser(authentication);
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
    public CommentResponse updateComment(Authentication authentication, CommentDTO commentDTO, Long id) {
        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException(MessageKeys.COMMENT_NOT_FOUND, id));
        if (isCurrentOwner(authentication, existingComment)) {
            existingComment.setContent(commentDTO.getContent());
            return CommentResponse.fromComment(existingComment);
        } else {
            throw new InvalidOwnerException(MessageKeys.COMMENT_INVALID_OWNER);
        }

    }

    @Override
    @Transactional
    public CommentResponse deleteComment(Authentication authentication, Long id) {
        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException(MessageKeys.COMMENT_NOT_FOUND, id));
        if (isCurrentOwner(authentication, existingComment)) {
            existingComment.setIsDeleted(true);
            commentRepository.save(existingComment);
            return CommentResponse.fromComment(existingComment);
        } else {
            throw new InvalidOwnerException(MessageKeys.COMMENT_INVALID_OWNER);
        }
    }

    @Override
    @Transactional
    public void destroyComment(Long id) {
        Comment existingComment = commentRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException(MessageKeys.COMMENT_NOT_FOUND, id));
        commentRepository.delete(existingComment);
    }

    private User getCurrentAuthenticatedUser(Authentication authentication){
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.USER_NOT_FOUND));
    }

    private boolean isCurrentOwner(Authentication authentication, Comment existingComment) {
        return Objects.equals(authentication.getName(), existingComment.getCreatedBy().getUsername());
    }
}
