package com.project.library.services;

import com.project.library.dtos.RoleGroupDTO;
import com.project.library.responses.RoleGroupResponse;

import java.util.List;

public interface RoleGroupService {
    List<RoleGroupResponse> getRoleGroups();
    RoleGroupResponse getRoleGroupByCode(Long id);
    RoleGroupResponse createRoleGroup(RoleGroupDTO roleGroup);
    RoleGroupResponse updateRoleGroup(RoleGroupDTO roleGroup, Long id);
    void deleteRoleGroup(Long id);
}
