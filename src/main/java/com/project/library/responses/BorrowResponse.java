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
    private UUID code;

    @JsonProperty(value = "user_code")
    private UUID userCode;

    @JsonProperty(value = "user_name")
    private String userName;

    @JsonProperty(value = "book_code")
    private UUID bookCode;

    @JsonProperty(value = "book_name")
    private String bookName;

    @JsonProperty(value = "borrow_amount")
    private int borrowAmount;

    @JsonProperty(value = "borrow_at")
    private LocalDateTime borrowAt;

    @JsonProperty(value = "return_at")
    private LocalDateTime returnAt;
    private String status;

    public static BorrowResponse fromBorrow(Borrow borrow) {
        return BorrowResponse.builder()
                .code(borrow.getCode())
                .userCode(borrow.getUser().getCode())
                .userName(borrow.getUser().getUsername())
                .bookCode(borrow.getBook().getCode())
                .bookName(borrow.getBook().getTitle())
                .borrowAmount(borrow.getBorrowAmount())
                .borrowAt(borrow.getReturnAt())
                .returnAt(borrow.getReturnAt())
                .status(borrow.getStatus().toString())
                .build();
    }
}
