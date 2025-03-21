package com.project.library.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostDTO {
    @NotBlank(message = "Title must not blank")
    @Length(min = 3, max = 50)
    private String title;

    @NotBlank(message = "Content must not blank")
    @Length(min = 5, max = 255)
    private String content;

    @JsonProperty(value = "book_id")
    @NotNull(message = "Post must belong to a book")
    private Long bookId;

    @JsonProperty(value = "user_id")
    @NotNull(message = "Post must belong to an user")
    private Long userId;
}
