package com.project.library.services;

import com.project.library.dtos.PostDTO;
import com.project.library.dtos.PostSearchDTO;
import com.project.library.entities.Book;
import com.project.library.entities.Post;
import com.project.library.entities.User;
import com.project.library.exceptions.DataNotFoundException;
import com.project.library.repositories.BookRepository;
import com.project.library.repositories.PostRepository;
import com.project.library.repositories.UserRepository;
import com.project.library.responses.PostPageResponse;
import com.project.library.responses.PostResponse;
import com.project.library.services.interfaces.IPostService;
import com.project.library.utils.MessageKeys;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PostServiceImpl implements IPostService {
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
                .map(post -> PostResponse.fromPost(post))
                .toList();
        return PostPageResponse.builder()
                .postResponseList(postResponseList)
                .totalPages(totalPages)
                .build();
    }

    @Override
    public PostResponse getPostByCode(UUID code) {
        Post existingPost = postRepository.findById((code))
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.POST_NOT_FOUND, code));
        return PostResponse.fromPost(existingPost);
    }

    @Override
    @Transactional
    public PostResponse addPost(PostDTO postDTO) {
        Book existingBook = bookRepository
                .findById(postDTO.getBookCode())
                .orElseThrow(() ->
                        new DataNotFoundException(MessageKeys.BOOK_NOT_FOUND, postDTO.getBookCode()));
        User user = userRepository.findById(postDTO.getUserCode())
                .orElseThrow(() ->
                        new DataNotFoundException(MessageKeys.USER_NOT_FOUND, postDTO.getUserCode()));
        Post newPost = Post.builder()
                .title(postDTO.getTitle())
                .content(postDTO.getContent())
                .book(existingBook)
                .user(user)
                .build();
        postRepository.save(newPost);
        return PostResponse.fromPost(newPost);
    }

    @Override
    @Transactional
    public PostResponse updatePost(PostDTO postDTO, UUID code) {

        Post existingPost = postRepository.findById(code)
                .orElseThrow(()-> new DataNotFoundException(MessageKeys.POST_NOT_FOUND, code));
        if (SecurityContextHolder.getContext().getAuthentication().getCredentials() != existingPost.getCreatedBy().getUsername()) {

        }
        existingPost.setTitle(postDTO.getTitle());
        existingPost.setContent(postDTO.getContent());
        postRepository.save(existingPost);
        return PostResponse.fromPost(existingPost);
    }

    @Override
    @Transactional
    public PostResponse deletePost(UUID code) {
        Post existingPost = postRepository.findById(code)
                .orElseThrow(()-> new DataNotFoundException(MessageKeys.POST_NOT_FOUND, code));
        existingPost.setIsDeleted(true);
        postRepository.save(existingPost);
        return PostResponse.fromPost(existingPost);
    }

    @Override
    @Transactional
    public void destroyPost(UUID code) {
        Post existingPost = postRepository.findById(code)
                .orElseThrow(()-> new DataNotFoundException(MessageKeys.POST_NOT_FOUND, code));
        postRepository.delete(existingPost);
    }
}
