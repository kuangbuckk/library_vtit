package com.project.library.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.library.entities.Borrow;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class BorrowResponse {
    private Long id;

    @JsonProperty(value = "user_id")
    private Long userId;

    @JsonProperty(value = "user_name")
    private String userName;

    @JsonProperty(value = "book_id")
    private Long bookId;

    @JsonProperty(value = "book_name")
    private String bookName;

    @JsonProperty(value = "borrow_amount")
    private int borrowAmount;

    @JsonProperty(value = "borrow_at")
    private LocalDateTime borrowAt;

    @JsonProperty(value = "return_at")
    private LocalDateTime returnAt;

    @JsonProperty(value = "created_at")
    private LocalDateTime createdAt;

    @JsonProperty(value = "updated_at")
    private LocalDateTime updatedAt;

    private String status;

    public static BorrowResponse fromBorrow(Borrow borrow) {
        return BorrowResponse.builder()
                .id(borrow.getId())
                .userId(borrow.getUser().getId())
                .userName(borrow.getUser().getUsername())
                .bookId(borrow.getBook().getId())
                .bookName(borrow.getBook().getTitle())
                .borrowAmount(borrow.getBorrowAmount())
                .borrowAt(borrow.getReturnAt())
                .returnAt(borrow.getReturnAt())
                .createdAt(borrow.getCreatedAt())
                .updatedAt(borrow.getUpdatedAt())
                .status(borrow.getStatus().toString())
                .build();
    }
}
