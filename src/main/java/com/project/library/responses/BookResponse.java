package com.project.library.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.library.entities.Book;
import com.project.library.entities.Category;
import com.project.library.entities.Post;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.validator.cfg.defs.UUIDDef;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
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

    @JsonProperty("category_codes")
    private List<UUID> categoryCodes;

//    @JsonProperty("posts_codes")
//    private List<UUID> postsCodes;

    public static BookResponse fromBook(Book book) {
        List<UUID> categoryCodes = book.getCategories().stream().map(Category::getCode).toList();
        List<UUID> postCodes = book.getPosts().stream().map(Post::getCode).toList();

        BookResponse bookResponse = BookResponse.builder()
                .code(book.getCode())
                .author(book.getAuthor())
                .title(book.getTitle())
                .amount(book.getAmount())
                .categoryCodes(categoryCodes)
//                .postsCodes(postCodes)
                .build();
        return bookResponse;
    }

}
