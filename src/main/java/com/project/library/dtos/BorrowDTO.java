package com.project.library.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BorrowDTO {
    @JsonProperty(value = "user_code")
    @NotBlank(message = "User code must not blank")
    private String userCode;

    @JsonProperty(value = "book_code")
    @NotBlank(message = "User code must not blank")
    private String bookCode;

    @JsonProperty(value = "borrow_at")
    @NotBlank(message = "Borrow at must not blank")
    private LocalDateTime borrowAt;

    @JsonProperty(value = "return_at")
    @NotBlank(message = "Borrow at must not blank")
    private LocalDateTime returnAt;

    @JsonProperty(value = "status")
    private String status;
}
