package com.project.library.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.library.entities.Category;
import jdk.dynalink.beans.StaticClass;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryResponse {
    private UUID code;
    @JsonProperty(value = "category_name")
    private String categoryName;
    @JsonProperty(value = "books")
    private List<BookResponse> bookResponses;

    public static CategoryResponse fromCategory(final Category category) {
        List<BookResponse> bookResponses = new ArrayList<>();
        if (category.getBooks() != null) {
            bookResponses = category.getBooks()
                    .stream()
                    .map(BookResponse::fromBook)
                    .toList();
        }
        return CategoryResponse.builder()
                .code(category.getCode())
                .categoryName(category.getCategoryName())
                .bookResponses(bookResponses)
                .build();
    }
}
