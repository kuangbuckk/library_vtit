package com.project.library.dtos.search;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.library.constants.BorrowStatus;
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
    private Long id;

    @JsonProperty(value = "user_id")
    private Long userId;

    @JsonProperty(value = "book_id")
    private Long bookId;

    @JsonProperty(value = "borrow_amount")
    private int borrowAmount;

    @JsonProperty(value = "borrow_at")
    private LocalDateTime borrowAt;

    @JsonProperty(value = "return_at")
    private LocalDateTime returnAt;

    @JsonProperty(value = "status")
    private BorrowStatus status;
}
