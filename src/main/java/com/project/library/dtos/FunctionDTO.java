package com.project.library.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FunctionDTO {
    @JsonProperty(value = "function_name")
    @NotBlank(message = "Function name can't be empty")
    @Length(min = 5, max = 60)
    private String functionName;

    @JsonProperty(value = "description")
    @Length(max = 120)
    private String description;
}
