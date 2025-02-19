package com.project.library.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    @JsonProperty(value = "content")
    @NotBlank(message = "Comment content must not blank")
    @Size(min = 3, max = 255)
    private String content;

    @JsonProperty(value = "user_code")
    @NotBlank(message = "Comment must belong to a user")
    private String userCode;

    @JsonProperty(value = "post_code")
    @NotBlank(message = "Comment must belong to a post")
    private String postCode;
}
