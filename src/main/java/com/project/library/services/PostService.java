package com.project.library.services;

import com.project.library.dtos.PostDTO;
import com.project.library.dtos.search.PostSearchDTO;
import com.project.library.responses.PostPageResponse;
import com.project.library.responses.PostResponse;
import org.springframework.security.core.Authentication;


public interface PostService {
    PostPageResponse getAllPosts(int pageNumber, int size, PostSearchDTO postSearchDTO);
    PostResponse getPostById(Long id);
    PostResponse addPost(Authentication authentication, PostDTO postDTO);
    PostResponse updatePost(Authentication authentication, PostDTO postDTO, Long id);
    PostResponse deletePost(Authentication authentication, Long id);
    void destroyPost(Long id);
}
