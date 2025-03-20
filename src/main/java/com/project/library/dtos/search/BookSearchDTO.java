package com.project.library.dtos.search;

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
    private Long id;
    private String author;
    private String title;
    private int amount;
    private String language;
    private String description;
    @JsonProperty("published_at")
    private LocalDateTime publishedAt;

    @JsonProperty("category_names")
    private List<String> categoryNames;
}
