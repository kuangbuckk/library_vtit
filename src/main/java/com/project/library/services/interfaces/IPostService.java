package com.project.library.services.interfaces;

import com.project.library.dtos.PostDTO;
import com.project.library.entities.Post;
import com.project.library.responses.PostPageResponse;
import com.project.library.responses.PostResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface IPostService {
    PostPageResponse getAllPosts(Pageable pageable);
    PostResponse getPostByCode(UUID code);
    PostResponse addPost(PostDTO postDTO);
    PostResponse updatePost(PostDTO postDTO, UUID code);
    void deletePost(UUID code);
}
