package com.project.library.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    @JsonProperty("category_name")
    @Size(max = 255, message = "Category's name max is 255 characters")
    @NotBlank(message = "Category's name can't be empty")
    private String categoryName;
}
