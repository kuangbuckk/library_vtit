package com.project.library.services.interfaces;

import com.project.library.dtos.PostDTO;
import com.project.library.dtos.search.PostSearchDTO;
import com.project.library.responses.PostPageResponse;
import com.project.library.responses.PostResponse;

import java.util.UUID;

public interface IPostService {
    PostPageResponse getAllPosts(int pageNumber, int size, PostSearchDTO postSearchDTO);
    PostResponse getPostByCode(UUID code);
    PostResponse addPost(PostDTO postDTO);
    PostResponse updatePost(PostDTO postDTO, UUID code);
    PostResponse deletePost(UUID code);
    void destroyPost(UUID code);
}
