package com.project.library.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.List;
import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleGroupDTO {
    @JsonProperty(value = "role_group_name")
    @NotBlank(message = "Role group name cant be empty")
    @Length(min = 3, max = 30)
    private String roleGroupName;

    @JsonProperty(value = "description")
    @Length(max = 50)
    private String description;

    @JsonProperty(value = "function_ids")
    private List<Long> functionIds;
}
