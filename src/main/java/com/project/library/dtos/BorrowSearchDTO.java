package com.project.library.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BorrowSearchDTO {
    private UUID code;

    @JsonProperty(value = "user_code")
    private UUID userCode;

    @JsonProperty(value = "book_code")
    private String bookCode;

    @JsonProperty(value = "borrow_amount")
    private int borrowAmount;

    @JsonProperty(value = "borrow_at")
    private LocalDateTime borrowAt;

    @JsonProperty(value = "return_at")
    private LocalDateTime returnAt;

    @JsonProperty(value = "status")
    private String status;
}
