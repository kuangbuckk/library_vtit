package com.project.library.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class BorrowPageResponse {
    @JsonProperty(value = "borrow_list")
    private List<BorrowResponse> borrowResponseList;
    private int totalPages;
}
