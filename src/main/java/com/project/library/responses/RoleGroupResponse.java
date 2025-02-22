package com.project.library.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.library.entities.Function;
import com.project.library.entities.RoleGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class RoleGroupResponse {
    private UUID code;

    @JsonProperty(value = "role_group_name")
    private String roleGroupName;

    private String description;

    @JsonProperty(value = "function_names")
    private List<String> functionNames;

    public static RoleGroupResponse fromRoleGroup(final RoleGroup roleGroup) {
        List<String> functionNames = roleGroup.getFunctions()
                .stream()
                .map(Function::getFunctionName)
                .toList();

        return RoleGroupResponse.builder()
                .code(roleGroup.getCode())
                .roleGroupName(roleGroup.getRoleGroupName())
                .description(roleGroup.getDescription())
                .functionNames(functionNames)
                .build();
    }
}
