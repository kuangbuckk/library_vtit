package com.project.library.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
//    @JsonProperty(value = "user_id")
//    @NotBlank(message = "User id must not blank")
//    private Long userId;

    @JsonProperty(value = "book_id")
    @NotBlank(message = "User id must not blank")
    private Long bookId;

    @JsonProperty(value = "borrow_amount")
    @Min(value = 1, message = "Must borrow at least 1 book")
    @Max(value = 20, message = "Must not borrow above 20 books")
    private int borrowAmount;

//    @JsonProperty(value = "borrow_at")
//    @NotBlank(message = "Borrow at must not blank")
//    private LocalDateTime borrowAt;

//    @JsonProperty(value = "return_at")
//    @NotBlank(message = "Borrow at must not blank")
//    private LocalDateTime returnAt;
//
    @JsonProperty(value = "status")
    private String status;
}
