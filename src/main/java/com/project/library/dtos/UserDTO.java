package com.project.library.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    @JsonProperty(value = "user_name")
    @NotBlank(message = "Username must not null")
    @Size(min = 3, max = 30)
    private String username;

    @JsonProperty(value = "email")
    @NotBlank(message = "Email must not null")
    @Size(min = 6, max = 50)
    @Email
    private String email;

    @JsonProperty(value = "phone_number")
    @Size(min = 9, max = 11)
    private String phoneNumber;


    @JsonProperty(value = "full_name")
    @NotBlank(message = "Fullname must not null")
    @Size(min = 6, max = 50)
    private String fullName;

    @JsonProperty(value= "date_of_birth")
    @NotNull(message = "DOB is required")
    private LocalDate dateOfBirth;

    @JsonProperty(value = "password")
    @NotBlank(message = "Password is required")
    @Size(min = 6, max = 50)
    private String password;

    @JsonProperty(value = "retype_password")
    @NotBlank(message = "Retyped password is required")
    @Size(min = 6, max = 50)
    private String retypePassword;

    @JsonProperty(value = "address")
    @NotBlank(message = "Address is required")
    private String address;

    @JsonProperty(value = "role_group_codes")
    @NotEmpty(message = "Role is required")
    @Size(min = 1, message = "Must contain at least one role")
    private List<UUID> roleGroupCodes;
}
