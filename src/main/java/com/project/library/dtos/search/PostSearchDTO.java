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
    private UUID code;
    private String title;
    private String content;
    @JsonProperty(value = "book_id")
    private UUID bookId;
    @JsonProperty(value = "user_id")
    private UUID userId;
}
