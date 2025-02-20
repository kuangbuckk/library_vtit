package com.project.library.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookPageResponse {
    @JsonProperty("book_list")
    private List<BookResponse> bookResponses;

    @JsonProperty("total_pages")
    private int totalPages;
}
