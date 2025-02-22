package com.project.library.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@Builder
public class LoginDTO {
    @NotBlank(message = "Username cant be null")
    @Length(min = 3, max = 30)
    private String username;

    @NotBlank(message = "Password is required")
    @Length(min = 3, max = 30)
    private String password;
}
