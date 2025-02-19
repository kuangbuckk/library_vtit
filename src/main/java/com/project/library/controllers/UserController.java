package com.project.library.controllers;

import com.project.library.dtos.UserDTO;
import com.project.library.entities.User;
import com.project.library.responses.GenericResponse;
import com.project.library.services.interfaces.IUserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {
    private final IUserService userService;
    
    @GetMapping("/")
    public ResponseEntity<GenericResponse<List<User>>> getAllUsers() {
        List<User> users = userService.getUsers();
        return ResponseEntity.ok(GenericResponse.success(users));
    }
    
    @GetMapping("/{code}")
    public ResponseEntity<GenericResponse<User>> getUserByCode(@PathVariable String code) {
        User existingUser = userService.getUserByCode(code);
        return ResponseEntity.ok(GenericResponse.success(existingUser));
    }

    @PostMapping("/")
    public ResponseEntity<?> createUser(
            @RequestBody @Valid UserDTO userDTO,
            BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream().map(FieldError::getDefaultMessage).toList();
            return ResponseEntity.badRequest().body(GenericResponse.error(errors.toString()));
        }
        User user = userService.createUser(userDTO);
        return ResponseEntity.ok(GenericResponse.success(user));
    }

    //TODO: add update and delete
}
