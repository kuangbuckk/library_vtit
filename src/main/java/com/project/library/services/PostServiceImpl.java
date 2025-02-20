package com.project.library.services;

import com.project.library.dtos.PostDTO;
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
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PostServiceImpl implements IPostService {
    private final PostRepository postRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    @Override
    public PostPageResponse getAllPosts(Pageable pageable) {
        Page<Post> posts = postRepository.findAll(pageable);
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
                .orElseThrow(() -> new DataNotFoundException("Post with code " + code + " not found"));
        return PostResponse.fromPost(existingPost);
    }

    @Override
    public PostResponse addPost(PostDTO postDTO) {
        Book existingBook = bookRepository
                .findById(postDTO.getBookCode())
                .orElseThrow(() ->
                        new DataNotFoundException("Book with code " + postDTO.getBookCode() + " not found"));
        User user = userRepository.findById(postDTO.getUserCode())
                .orElseThrow(() ->
                        new DataNotFoundException("User with code " + postDTO.getUserCode() + " not found"));
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
    public PostResponse updatePost(PostDTO postDTO, UUID code) {
        Post existingPost = postRepository.findById(code)
                .orElseThrow(()-> new DataNotFoundException("Post with code " + code + " not found"));
        existingPost.setTitle(postDTO.getTitle());
        existingPost.setContent(postDTO.getContent());
        postRepository.save(existingPost);
        return PostResponse.fromPost(existingPost);
    }

    @Override
    public void deletePost(UUID code) {
        postRepository.deleteById(code);
    }
}
