package com.project.library.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.project.library.entities.RoleGroup;
import com.project.library.entities.User;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserResponse {
    private Long id;

    @JsonProperty(value = "user_name")
    private String username;

    @JsonProperty(value = "email")
    private String email;

    @JsonProperty(value = "phone_number")
    private String phoneNumber;


    @JsonProperty(value = "full_name")
    private String fullName;

    @JsonProperty(value= "date_of_birth")
    private LocalDate dateOfBirth;

    @JsonProperty(value = "address")
    @NotBlank(message = "Address is required")
    private String address;

    @JsonProperty(value = "role_group")
    private List<RoleGroupResponse> roleGroupResponses;

    public static UserResponse fromUser(final User user) {
        List<RoleGroupResponse> roleGroupResponses = new ArrayList<>();
        if (user.getRoleGroups() != null) {
            roleGroupResponses = user.getRoleGroups()
                    .stream()
                    .map(RoleGroupResponse::fromRoleGroup)
                    .toList();
        }
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .fullName(user.getFullName())
                .dateOfBirth(user.getDateOfBirth())
                .address(user.getAddress())
                .roleGroupResponses(roleGroupResponses)
                .build();
    }

}
