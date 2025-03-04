package com.project.library.controllers;

import com.project.library.components.LocalizationUtils;
import com.project.library.dtos.LoginDTO;
import com.project.library.dtos.UserDTO;
import com.project.library.entities.User;
import com.project.library.responses.GenericResponse;
import com.project.library.responses.UserPageResponse;
import com.project.library.responses.UserResponse;
import com.project.library.services.interfaces.IUserService;
import com.project.library.utils.MessageKeys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/users")
@AllArgsConstructor
public class UserController {
    private final IUserService userService;
    private final LocalizationUtils localizationUtils;

    @GetMapping("/")
    @PreAuthorize("@customSecurityExpression.fileRole(#httpServletRequest)")
    public ResponseEntity<GenericResponse> getAllUsers(
            @RequestParam("page_number") int pageNumber,
            @RequestParam("size") int size,
            @RequestParam("keyword") String keyword,
            HttpServletRequest httpServletRequest
    ) {
        UserPageResponse userPageResponse = userService.getUsers(pageNumber, size, keyword);
        return ResponseEntity.ok(GenericResponse.success(userPageResponse));
    }
    
    @GetMapping("/{code}")
    @PreAuthorize("@customSecurityExpression.fileRole(#httpServletRequest)")
    public ResponseEntity<GenericResponse> getUserByCode(
            @PathVariable UUID code,
            HttpServletRequest httpServletRequest
    ) {
        UserResponse userResponse = userService.getUserByCode(code);
        return ResponseEntity.ok(GenericResponse.success(userResponse));
    }

    @PostMapping("/register")
    public ResponseEntity<?> createUser(
            @RequestBody @Valid UserDTO userDTO) {
        UserResponse userResponse = userService.createUser(userDTO);
        return ResponseEntity.ok(GenericResponse.success(userResponse));
    }

    @PostMapping("/login")
    public ResponseEntity<GenericResponse> loginUser(
            @RequestBody @Valid LoginDTO loginDTO
    ) {
        String token = userService.login(loginDTO.getUsername(), loginDTO.getPassword());
        return ResponseEntity.ok(GenericResponse.success(
                MessageKeys.LOGIN_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.LOGIN_SUCCESSFULLY),
                token));
    }

    @PutMapping("/update/{code}")
    @PreAuthorize("@customSecurityExpression.fileRole(#httpServletRequest)")
    public ResponseEntity<GenericResponse> updateUser(
            @RequestBody @Valid UserDTO userDTO,
            @PathVariable UUID code,
            HttpServletRequest httpServletRequest
    ) {

        UserResponse userResponse = userService.updateUser(userDTO, code);
        return ResponseEntity.ok(GenericResponse.success(userResponse));
    }

    @DeleteMapping("/{code}")
    @PreAuthorize("@customSecurityExpression.fileRole(#httpServletRequest)")
    public ResponseEntity<GenericResponse> deleteUser(
            @PathVariable UUID code,
            HttpServletRequest httpServletRequest
    ) {
        userService.deleteUser(code);
        return ResponseEntity.ok(GenericResponse.success(code));
    }

    @DeleteMapping("/destroy/{code}")
    @PreAuthorize("@customSecurityExpression.fileRole(#httpServletRequest)")
    public ResponseEntity<GenericResponse> destroyUser(
            @PathVariable UUID code,
            HttpServletRequest httpServletRequest
    ) {
        userService.destroyUser(code);
        return ResponseEntity.ok(GenericResponse.success(code));
    }
}
