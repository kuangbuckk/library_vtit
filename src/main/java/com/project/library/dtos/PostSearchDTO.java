package com.project.library.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class PostSearchDTO {
    private UUID code;
    private String title;
    private String content;
    @JsonProperty(value = "book_code")
    private UUID bookCode;
    @JsonProperty(value = "user_code")
    private UUID userCode;
}
