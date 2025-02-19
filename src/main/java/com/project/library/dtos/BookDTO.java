package com.project.library.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.library.entities.Category;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {
    @NotBlank(message = "Book's author can't be empty")
    @Size(max = 255, message = "255 characters max")
    private String author;

    @NotBlank(message = "Book's title can't be empty")
    @Size(max = 255, message = "255 characters max")
    private String title;

    @Min(value = 0, message = "Amount can't be less than 0")
    private int amount;

    @JsonProperty("category_codes")
    private List<String> categoryCodes;
}
