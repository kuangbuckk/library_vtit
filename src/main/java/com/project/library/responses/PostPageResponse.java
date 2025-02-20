package com.project.library.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class PostPageResponse {
    @JsonProperty(value = "post_list")
    private List<PostResponse> postResponseList;

    @JsonProperty(value = "total_pages")
    private int totalPages;
}
