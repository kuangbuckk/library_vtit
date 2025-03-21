package com.project.library.dtos.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class PostSearchDTO {
    private Long id;
    private String title;
    private String content;
    @JsonProperty(value = "book_id")
    private Long bookId;
    @JsonProperty(value = "user_id")
    private Long userId;
}
