package com.project.library.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.library.entities.Book;
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
    private Long id;
    private String title;
    private String content;

    @JsonProperty(value = "book_id")
    private Long bookId;

    @JsonProperty(value = "book_title")
    private String bookTitle;

    @JsonProperty(value = "comments")
    private List<CommentResponse> comments;

    @JsonProperty(value = "auditor")
    private AuditorResponse auditor;

    public static PostResponse fromPost(Post post) {
        List<CommentResponse> comments = new ArrayList<>();
        if (post.getComments() != null) {
            comments = post.getComments()
                    .stream()
                    .map(CommentResponse::fromComment)
                    .toList();
        }
        Book existingBook = post.getBook();
        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .bookId(existingBook.getId())
                .bookTitle(existingBook.getTitle())
                .comments(comments)
                .auditor(AuditorResponse.builder()
                        .createdAt(post.getCreatedAt())
                        .updatedAt(post.getUpdatedAt())
                        .createdBy(post.getCreatedBy().getUsername())
                        .updatedBy(post.getUpdatedBy().getUsername())
                        .isActive(post.getIsActive())
                        .isDeleted(post.getIsDeleted())
                        .build()
                )
                .build();
    }
}
