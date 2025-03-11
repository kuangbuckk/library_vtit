package com.project.library.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.library.entities.BaseEntity;
import com.project.library.entities.Book;
import com.project.library.entities.Category;
import com.project.library.entities.User;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookResponse {
    private UUID code;
    private String author;
    private String title;
    private int amount;

    @JsonProperty("category_names")
    private List<String> categoryNames;

    @JsonProperty("auditor")
    private AuditorResponse auditorResponse;


    public static BookResponse fromBook(Book book) {
        List<String> categoryNames = book.getCategories().stream().map(Category::getCategoryName).toList();
        return BookResponse.builder()
                .code(book.getCode())
                .author(book.getAuthor())
                .title(book.getTitle())
                .amount(book.getAmount())
                .categoryNames(categoryNames)
                .auditorResponse(AuditorResponse.builder()
                        .createdAt(book.getCreatedAt())
                        .updatedAt(book.getUpdatedAt())
                        .createdBy(book.getCreatedBy().getUsername())
                        .updatedBy(book.getUpdatedBy().getUsername())
                        .isActive(book.getIsActive())
                        .isDeleted(book.getIsDeleted())
                        .build())
                .build();
    }

}
