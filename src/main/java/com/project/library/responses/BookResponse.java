package com.project.library.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.library.entities.Book;
import com.project.library.entities.Category;
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

//    @JsonProperty("posts_codes")
//    private List<UUID> postsCodes;

    public static BookResponse fromBook(Book book) {
        List<String> categoryNames = book.getCategories().stream().map(Category::getCategoryName).toList();
//        List<UUID> postCodes = book.getPosts().stream().map(Post::getCode).toList();

        return BookResponse.builder()
                .code(book.getCode())
                .author(book.getAuthor())
                .title(book.getTitle())
                .amount(book.getAmount())
                .categoryNames(categoryNames)
//                .postsCodes(postCodes)
                .build();
    }

}
