package com.project.library.controllers;

import com.project.library.components.LocalizationUtils;
import com.project.library.dtos.RoleGroupDTO;
import com.project.library.entities.RoleGroup;
import com.project.library.responses.GenericResponse;
import com.project.library.responses.RoleGroupResponse;
import com.project.library.services.interfaces.IRoleGroupService;
import com.project.library.utils.MessageKeys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PostMapping("/create")
//    @PreAuthorize("hasRole('ADMIN')")
    @PreAuthorize("@customSecurityExpression.fileRole(#httpServletRequest)")
    public ResponseEntity<?> addNewRoleGroup(
            @RequestBody @Valid RoleGroupDTO roleGroupDTO,
            HttpServletRequest httpServletRequest
    ) {
        RoleGroupResponse newRoleGroup = roleGroupService.createRoleGroup(roleGroupDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(GenericResponse.success(
                MessageKeys.INSERT_ROLE_GROUP_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.INSERT_ROLE_GROUP_SUCCESSFULLY),
                newRoleGroup));
    }

    @PutMapping("/update/{code}")
//    @PreAuthorize("hasRole('ADMIN')")
    @PreAuthorize("@customSecurityExpression.fileRole(#httpServletRequest)")
    public ResponseEntity<?> updateRoleGroup(
            @RequestBody @Valid RoleGroupDTO roleGroupDTO,
            @PathVariable UUID code,
            HttpServletRequest httpServletRequest
    ) {
        RoleGroupResponse updatedRoleGroup = roleGroupService.updateRoleGroup(roleGroupDTO, code);
        return ResponseEntity.ok(GenericResponse.success(
                MessageKeys.UPDATE_ROLE_GROUP_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.UPDATE_ROLE_GROUP_SUCCESSFULLY),
                updatedRoleGroup)
        );
    }

    @DeleteMapping("/destroy/{code}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteRoleGroup(
            @PathVariable UUID code
    ) {
        roleGroupService.deleteRoleGroup(code);
        return ResponseEntity.ok(GenericResponse.success(code));
    }
}
