package com.project.library.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookSearchDTO {
    private UUID code;
    private String author;
    private String title;
    private int amount;
    @JsonProperty("published_at")
    private LocalDateTime publishedAt;

    @JsonProperty("category_names")
    private List<String> categoryNames;
}
