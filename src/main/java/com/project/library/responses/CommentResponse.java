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
    private UUID code;
    @JsonProperty(value = "content")
    private String content;

    @JsonProperty(value = "post_code")
    private UUID postCode;

    @JsonProperty(value = "auditor")
    private AuditorResponse auditor;

    public static CommentResponse fromComment(Comment comment) {
        return CommentResponse.builder()
                .code(comment.getCode())
                .content(comment.getContent())
                .postCode(comment.getPost().getCode())
                .auditor(AuditorResponse.builder()
                        .createdBy(comment.getCreatedBy().getUsername())
                        .updatedBy(comment.getUpdatedBy().getUsername())
                        .createdAt(comment.getCreatedAt())
                        .updatedAt(comment.getUpdatedAt())
                        .build())
                .build();
    }
}
