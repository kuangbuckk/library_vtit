package com.project.library.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserPageResponse {
    @JsonProperty(value = "user_list")
    private List<UserResponse> userResponses;

    @JsonProperty(value = "total_pages")
    private int totalPages;
}
