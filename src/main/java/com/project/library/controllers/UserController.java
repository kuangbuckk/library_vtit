package com.project.library.controllers;

import com.project.library.dtos.LoginDTO;
import com.project.library.dtos.UserDTO;
import com.project.library.entities.User;
import com.project.library.responses.GenericResponse;
import com.project.library.responses.UserPageResponse;
import com.project.library.responses.UserResponse;
import com.project.library.services.interfaces.IUserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
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
    
    @GetMapping("/")
    public ResponseEntity<GenericResponse> getAllUsers(
            @RequestParam("page_number") int pageNumber,
            @RequestParam("size") int size
    ) {
        UserPageResponse userPageResponse = userService.getUsers(PageRequest.of(pageNumber, size));
        return ResponseEntity.ok(GenericResponse.success(userPageResponse));
    }
    
    @GetMapping("/{code}")
    public ResponseEntity<GenericResponse> getUserByCode(@PathVariable UUID code) {
        UserResponse userResponse = userService.getUserByCode(code);
        return ResponseEntity.ok(GenericResponse.success(userResponse));
    }

    @PostMapping("/register")
    public ResponseEntity<?> createUser(
            @RequestBody @Valid UserDTO userDTO,
            BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(GenericResponse.error(errors.toString()));
        }
        UserResponse userResponse = userService.createUser(userDTO);
        return ResponseEntity.ok(GenericResponse.success(userResponse));
    }

    @PutMapping("/{code}")
    public ResponseEntity<GenericResponse> updateUser(
            @RequestBody @Valid UserDTO userDTO,
            @PathVariable UUID code,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(GenericResponse.error(errors.toString()));
        }
        UserResponse userResponse = userService.updateUser(userDTO, code);
        return ResponseEntity.ok(GenericResponse.success(userResponse));
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<GenericResponse> deleteUser(@PathVariable UUID code) {
        userService.deleteUser(code);
        return ResponseEntity.ok(GenericResponse.success(code));
    }

    @PostMapping("/login")
    public ResponseEntity<GenericResponse> loginUser(
            @RequestBody @Valid LoginDTO loginDTO,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(GenericResponse.error(errors.toString()));
        }
        String token = userService.login(loginDTO.getUsername(), loginDTO.getPassword());
        return ResponseEntity.ok(GenericResponse.success(token));
    }
}
