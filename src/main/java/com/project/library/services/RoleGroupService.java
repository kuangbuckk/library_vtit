package com.project.library.services;

import com.project.library.dtos.RoleGroupDTO;
import com.project.library.responses.RoleGroupResponse;

import java.util.List;
import java.util.UUID;

public interface RoleGroupService {
    List<RoleGroupResponse> getRoleGroups();
    RoleGroupResponse getRoleGroupByCode(UUID code);
    RoleGroupResponse createRoleGroup(RoleGroupDTO roleGroup);
    RoleGroupResponse updateRoleGroup(RoleGroupDTO roleGroup, UUID code);
    void deleteRoleGroup(UUID code);
}
