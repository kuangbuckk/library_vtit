package com.project.library.controllers;

import com.project.library.components.LocalizationUtils;
import com.project.library.dtos.RoleGroupDTO;
import com.project.library.entities.RoleGroup;
import com.project.library.responses.GenericResponse;
import com.project.library.responses.RoleGroupResponse;
import com.project.library.services.interfaces.IRoleGroupService;
import com.project.library.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("${api.prefix}/role_groups")
@AllArgsConstructor
public class RoleGroupController {
    private final IRoleGroupService roleGroupService;
    private final LocalizationUtils localizationUtils;

    @GetMapping("/")
    public ResponseEntity<?> getRoleGroups() {
        return ResponseEntity.ok(GenericResponse.success(roleGroupService.getRoleGroups()));
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> getRoleGroup(@PathVariable UUID code) {
        RoleGroupResponse existingRoleGroup = roleGroupService.getRoleGroupByCode(code);
        return ResponseEntity.ok(GenericResponse.success(existingRoleGroup));
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(GenericResponse.error(MessageKeys.INSERT_ROLE_GROUP_FAILED, errors.toString()));
        }
        RoleGroupResponse newRoleGroup = roleGroupService.createRoleGroup(roleGroupDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(GenericResponse.success(
                MessageKeys.INSERT_ROLE_GROUP_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.INSERT_ROLE_GROUP_SUCCESSFULLY),
                newRoleGroup));
    }

    @PutMapping("/{code}")
    public ResponseEntity<?> updateRoleGroup(
            @RequestBody @Valid RoleGroupDTO roleGroupDTO,
            @PathVariable UUID code,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(GenericResponse.error(MessageKeys.UPDATE_ROLE_GROUP_FAILED, errors.toString()));
        }
        RoleGroupResponse updatedRoleGroup = roleGroupService.updateRoleGroup(roleGroupDTO, code);
        return ResponseEntity.ok(GenericResponse.success(
                MessageKeys.UPDATE_ROLE_GROUP_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.UPDATE_ROLE_GROUP_SUCCESSFULLY),
                updatedRoleGroup)
        );
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<?> deleteRoleGroup(
            @PathVariable UUID code
    ) {
        roleGroupService.deleteRoleGroup(code);
        return ResponseEntity.ok(GenericResponse.success(code));
    }
}
