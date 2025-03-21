package com.project.library.controllers;

import com.project.library.utils.LocalizationUtils;
import com.project.library.dtos.RoleGroupDTO;
import com.project.library.responses.GenericResponse;
import com.project.library.responses.RoleGroupResponse;
import com.project.library.services.RoleGroupService;
import com.project.library.utils.MessageKeys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/role_groups")
@AllArgsConstructor
public class RoleGroupController {
    private final RoleGroupService roleGroupService;
    private final LocalizationUtils localizationUtils;

    @GetMapping("/")
    public ResponseEntity<?> getRoleGroups() {
        return ResponseEntity.ok(GenericResponse.success(roleGroupService.getRoleGroups()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRoleGroup(@PathVariable Long id) {
        RoleGroupResponse existingRoleGroup = roleGroupService.getRoleGroupByCode(id);
        return ResponseEntity.ok(GenericResponse.success(existingRoleGroup));
    }

    @PostMapping("/create")
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

    @PutMapping("/update/{id}")
    @PreAuthorize("@customSecurityExpression.fileRole(#httpServletRequest)")
    public ResponseEntity<?> updateRoleGroup(
            @RequestBody @Valid RoleGroupDTO roleGroupDTO,
            @PathVariable Long id,
            HttpServletRequest httpServletRequest
    ) {
        RoleGroupResponse updatedRoleGroup = roleGroupService.updateRoleGroup(roleGroupDTO, id);
        return ResponseEntity.ok(GenericResponse.success(
                MessageKeys.UPDATE_ROLE_GROUP_SUCCESSFULLY,
                localizationUtils.getLocalizedMessage(MessageKeys.UPDATE_ROLE_GROUP_SUCCESSFULLY),
                updatedRoleGroup)
        );
    }

    @DeleteMapping("/destroy/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteRoleGroup(
            @PathVariable Long id
    ) {
        roleGroupService.deleteRoleGroup(id);
        return ResponseEntity.ok(GenericResponse.success(id));
    }
}
