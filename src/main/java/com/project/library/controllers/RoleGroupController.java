package com.project.library.controllers;

import com.project.library.dtos.RoleGroupDTO;
import com.project.library.entities.RoleGroup;
import com.project.library.services.interfaces.IRoleGroupService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/role_groups")
@AllArgsConstructor
public class RoleGroupController {
    private final IRoleGroupService roleGroupService;

    @GetMapping("/")
    public ResponseEntity<List<RoleGroup>> getRoleGroups() {
        return ResponseEntity.ok(roleGroupService.getRoleGroups());
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> getRoleGroup(@PathVariable String code) {
        try {
            RoleGroup existingRoleGroup = roleGroupService.getRoleGroupByCode(code);
            return ResponseEntity.ok(existingRoleGroup);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/")
    public ResponseEntity<?> addNewRoleGroup(
            @RequestBody @Valid RoleGroupDTO roleGroupDTO,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        RoleGroup newRoleGroup = roleGroupService.createRoleGroup(roleGroupDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newRoleGroup);
    }

    @PutMapping("/{code}")
    public ResponseEntity<?> updateRoleGroup(
            @RequestBody @Valid RoleGroupDTO roleGroupDTO,
            @PathVariable String code,
            BindingResult result
    ) {
        try {
            if (result.hasErrors()) {
                List<String> errors = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
            }
            RoleGroup updatedRoleGroup = roleGroupService.updateRoleGroup(roleGroupDTO, code);
            return ResponseEntity.ok(updatedRoleGroup);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<?> deleteRoleGroup(
            @PathVariable String code
    ) {
        try {
            roleGroupService.deleteRoleGroup(code);
            return ResponseEntity.ok("Deleted role group successfully with code " + code);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
