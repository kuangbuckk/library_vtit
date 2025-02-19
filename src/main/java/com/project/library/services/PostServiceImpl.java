package com.project.library.services;

import com.project.library.dtos.PostDTO;
import com.project.library.entities.Book;
import com.project.library.entities.Post;
import com.project.library.entities.User;
import com.project.library.exceptions.DataNotFoundException;
import com.project.library.repositories.BookRepository;
import com.project.library.repositories.PostRepository;
import com.project.library.repositories.UserRepository;
import com.project.library.services.interfaces.IPostService;
import lombok.AllArgsConstructor;
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
    public List<Post> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        return posts;
    }

    @Override
    public Post getPostByCode(String code) {
        Post existingPost = postRepository.findById(UUID.fromString(code))
                .orElseThrow(() -> new DataNotFoundException("Post with code " + code + " not found"));
        return existingPost;
    }

    @Override
    public Post addPost(PostDTO postDTO) {
        Book existingBook = bookRepository
                .findById(postDTO.getBookCode())
                .orElseThrow(() ->
                        new DataNotFoundException("Book with code " + postDTO.getBookCode() + " not found"));
        User user = userRepository.findById(postDTO.getUserCode())
                .orElseThrow(() ->
                        new DataNotFoundException("User with code " + postDTO.getUserCode() + " not found"));
        Post post = Post.builder()
                .title(postDTO.getTitle())
                .content(postDTO.getContent())
                .book(existingBook)
                .user(user)
                .build();
        return postRepository.save(post);
    }

    @Override
    public Post updatePost(PostDTO postDTO, String code) {
        Post existingPost = getPostByCode(code);
        existingPost.setTitle(postDTO.getTitle());
        existingPost.setContent(postDTO.getContent());

        return postRepository.save(existingPost);
    }

    @Override
    public void deletePost(String code) {
        postRepository.deleteById(UUID.fromString(code));
    }
}
