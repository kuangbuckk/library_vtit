package com.project.library.services.impl;

import com.project.library.dtos.PostDTO;
import com.project.library.dtos.search.PostSearchDTO;
import com.project.library.entities.Book;
import com.project.library.entities.Post;
import com.project.library.entities.User;
import com.project.library.exceptions.DataNotFoundException;
import com.project.library.exceptions.InvalidOwnerException;
import com.project.library.repositories.BookRepository;
import com.project.library.repositories.PostRepository;
import com.project.library.repositories.UserRepository;
import com.project.library.responses.PostPageResponse;
import com.project.library.responses.PostResponse;
import com.project.library.services.PostService;
import com.project.library.utils.MessageKeys;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Override
    public PostPageResponse getAllPosts(int pageNumber, int size, PostSearchDTO postSearchDTO) {
        Pageable pageable = PageRequest.of(pageNumber, size);
        Page<Post> posts = postRepository.findAll(pageable, postSearchDTO);
        int totalPages = posts.getTotalPages();
        List<PostResponse> postResponseList = posts.getContent()
                .stream()
                .map(PostResponse::fromPost)
                .toList();
        return PostPageResponse.builder()
                .postResponseList(postResponseList)
                .totalPages(totalPages)
                .build();
    }

    @Override
    public PostResponse getPostById(Long id) {
        Post existingPost = postRepository.findById((id))
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.POST_NOT_FOUND, id));
        return PostResponse.fromPost(existingPost);
    }

    @Override
    @Transactional
    public PostResponse addPost(Authentication authentication, PostDTO postDTO) {
        Book existingBook = bookRepository
                .findById(postDTO.getBookId())
                .orElseThrow(() ->
                        new DataNotFoundException(MessageKeys.BOOK_NOT_FOUND, postDTO.getBookId()));
//        User user = userRepository.findById(postDTO.getUserId())
//                .orElseThrow(() ->
//                        new DataNotFoundException(MessageKeys.USER_NOT_FOUND, postDTO.getUserId()));
        User existingUser = this.getCurrentAuthenticatedUser(authentication);
        Post newPost = Post.builder()
                .title(postDTO.getTitle())
                .content(postDTO.getContent())
                .book(existingBook)
                .user(existingUser)
                .build();
        postRepository.save(newPost);
        return PostResponse.fromPost(newPost);
    }

    @Override
    @Transactional
    public PostResponse updatePost(Authentication authentication, PostDTO postDTO, Long id) {
        Post existingPost = postRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException(MessageKeys.POST_NOT_FOUND, id));
        if (isCurrentOwner(authentication, existingPost)) {
            existingPost.setTitle(postDTO.getTitle());
            existingPost.setContent(postDTO.getContent());
            postRepository.save(existingPost);
            return PostResponse.fromPost(existingPost);
        }
        else {
            throw new InvalidOwnerException(MessageKeys.POST_INVALID_OWNER);
        }
    }

    @Override
    @Transactional
    public PostResponse deletePost(Authentication authentication, Long id) {
        Post existingPost = postRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException(MessageKeys.POST_NOT_FOUND, id));
        if (isCurrentOwner(authentication, existingPost)) {
            existingPost.setIsDeleted(true);
            postRepository.save(existingPost);
            return PostResponse.fromPost(existingPost);
        }
        else {
            throw new InvalidOwnerException(MessageKeys.POST_INVALID_OWNER);
        }
    }

    @Override
    @Transactional
    public void destroyPost(Long id) {
        Post existingPost = postRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException(MessageKeys.POST_NOT_FOUND, id));
        postRepository.delete(existingPost);
    }

    private User getCurrentAuthenticatedUser(Authentication authentication){
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.USER_NOT_FOUND));
    }

    private boolean isCurrentOwner(Authentication authentication, Post existingPost) {
        return Objects.equals(authentication.getName(), existingPost.getCreatedBy().getUsername());
    }
}
