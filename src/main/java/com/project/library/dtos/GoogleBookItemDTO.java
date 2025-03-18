package com.project.library.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.project.library.dtos.GoogleBookItemVolumeInfoDTO;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Setter
@Getter
public class GoogleBookItemDTO {
    private GoogleBookItemVolumeInfoDTO volumeInfo;
}