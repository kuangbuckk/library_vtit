package com.project.library.services.interfaces;

import com.project.library.dtos.RoleGroupDTO;
import com.project.library.entities.RoleGroup;
import com.project.library.responses.RoleGroupResponse;

import java.util.List;
import java.util.UUID;

public interface IRoleGroupService {
    List<RoleGroupResponse> getRoleGroups();
    RoleGroupResponse getRoleGroupByCode(UUID code);
    RoleGroupResponse createRoleGroup(RoleGroupDTO roleGroup);
    RoleGroupResponse updateRoleGroup(RoleGroupDTO roleGroup, UUID code);
    void deleteRoleGroup(UUID code);
}
