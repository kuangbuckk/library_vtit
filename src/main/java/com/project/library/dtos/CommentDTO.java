package com.project.library.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDTO {
    @JsonProperty(value = "content")
    @NotBlank(message = "Comment content must not blank")
    @Size(min = 3, max = 255)
    private String content;

    @JsonProperty(value = "post_id")
    @NotNull(message = "Comment must belong to a post")
    private Long postCode;
}
