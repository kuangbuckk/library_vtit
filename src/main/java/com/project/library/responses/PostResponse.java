package com.project.library.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.library.entities.Post;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private UUID code;
    private String title;
    private String content;

    @JsonProperty(value = "book_code")
    private UUID bookCode;

    @JsonProperty(value = "book_title")
    private String bookTitle;

    @JsonProperty(value = "user_code")
    private UUID userCode;

    @JsonProperty(value = "user_name")
    private String userName;

    @JsonProperty(value = "comments")
    private List<CommentResponse> comments;

    public static PostResponse fromPost(Post post) {
        List<CommentResponse> comments = post.getComments()
                .stream()
                .map(comment -> CommentResponse.fromComment(comment))
                .toList();
        return PostResponse.builder()
                .code(post.getCode())
                .title(post.getTitle())
                .content(post.getContent())
                .bookCode(post.getBook().getCode())
                .bookTitle(post.getBook().getTitle())
                .userCode(post.getUser().getCode())
                .userName(post.getUser().getUsername())
                .comments(comments)
                .build();
    }
}
