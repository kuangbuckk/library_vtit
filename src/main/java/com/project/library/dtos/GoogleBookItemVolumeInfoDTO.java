package com.project.library.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Setter
@Getter
public class GoogleBookItemVolumeInfoDTO {
    private String title;
    private List<String> authors;
    private String language;
    private String description;
    private int pageCount;
    private List<String> categories;
}