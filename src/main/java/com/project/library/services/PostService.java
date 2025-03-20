package com.project.library.services;

import com.project.library.dtos.PostDTO;
import com.project.library.dtos.search.PostSearchDTO;
import com.project.library.responses.PostPageResponse;
import com.project.library.responses.PostResponse;


public interface PostService {
    PostPageResponse getAllPosts(int pageNumber, int size, PostSearchDTO postSearchDTO);
    PostResponse getPostById(Long id);
    PostResponse addPost(PostDTO postDTO);
    PostResponse updatePost(PostDTO postDTO, Long id);
    PostResponse deletePost(Long id);
    void destroyPost(Long id);
}
