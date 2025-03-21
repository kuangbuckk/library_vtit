package com.project.library.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.library.entities.Comment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResponse {
    private Long id;
    @JsonProperty(value = "content")
    private String content;

    @JsonProperty(value = "post_id")
    private Long postId;

    @JsonProperty(value = "auditor")
    private AuditorResponse auditor;

    public static CommentResponse fromComment(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .postId(comment.getPost().getId())
                .auditor(AuditorResponse.builder()
                        .createdBy(comment.getCreatedBy().getUsername())
                        .updatedBy(comment.getUpdatedBy().getUsername())
                        .createdAt(comment.getCreatedAt())
                        .updatedAt(comment.getUpdatedAt())
                        .build())
                .build();
    }
}
