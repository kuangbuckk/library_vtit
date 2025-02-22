package com.project.library.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.library.entities.Function;
import com.project.library.entities.RoleGroup;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FunctionResponse {
    private UUID code;

    @JsonProperty(value = "function_name")
    private String functionName;

    @JsonProperty(value = "description")
    private String description;

    @JsonProperty(value = "role_group_names")
    private List<String> roleGroupNames;

    public static FunctionResponse fromFunction(Function function) {
        List<String> roleGroupName = function.getRoleGroups()
                .stream()
                .map(RoleGroup::getRoleGroupName)
                .toList();

        return FunctionResponse.builder()
                .code(function.getCode())
                .functionName(function.getFunctionName())
                .description(function.getDescription())
                .roleGroupNames(roleGroupName)
                .build();
    }
}
