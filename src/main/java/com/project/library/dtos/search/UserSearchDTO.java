package com.project.library.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSearchDTO {
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

    private String address;

    @JsonProperty("is_active")
    private Boolean isActive;

    @JsonProperty(value = "role_group_codes")
    private List<UUID> roleGroupCodes;
}
