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

    @JsonProperty(value = "user_code")
    private UUID userCode;

    @JsonProperty(value = "user_name")
    private String userName;

    @JsonProperty(value = "post_code")
    private UUID postCode;

    public static CommentResponse fromComment(Comment comment) {
        return CommentResponse.builder()
                .code(comment.getCode())
                .content(comment.getContent())
                .userCode(comment.getUser().getCode())
                .userName(comment.getUser().getUsername())
                .postCode(comment.getPost().getCode())
                .build();
    }
}
