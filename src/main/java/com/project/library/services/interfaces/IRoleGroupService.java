package com.project.library.services.interfaces;

import com.project.library.dtos.RoleGroupDTO;
import com.project.library.entities.RoleGroup;

import java.util.List;
import java.util.UUID;

public interface IRoleGroupService {
    List<RoleGroup> getRoleGroups();
    RoleGroup getRoleGroupByCode(String code);
    RoleGroup createRoleGroup(RoleGroupDTO roleGroup);
    RoleGroup updateRoleGroup(RoleGroupDTO roleGroup, String code);
    void deleteRoleGroup(String code);
}
