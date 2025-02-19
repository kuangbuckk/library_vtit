package com.project.library.services.interfaces;

import com.project.library.dtos.PostDTO;
import com.project.library.entities.Post;

import java.util.List;

public interface IPostService {
    List<Post> getAllPosts();
    Post getPostByCode(String code);
    Post addPost(PostDTO postDTO);
    Post updatePost(PostDTO postDTO, String code);
    void deletePost(String code);
}
