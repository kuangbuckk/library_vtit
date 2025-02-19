package com.project.library.services;

import com.project.library.dtos.RoleGroupDTO;
import com.project.library.entities.RoleGroup;
import com.project.library.exceptions.DataNotFoundException;
import com.project.library.repositories.RoleGroupRepository;
import com.project.library.services.interfaces.IRoleGroupService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RoleGroupServiceImpl implements IRoleGroupService {
    private final RoleGroupRepository roleGroupRepository;

    @Override
    public List<RoleGroup> getRoleGroups() {
        return roleGroupRepository.findAll();
    }

    @Override
    public RoleGroup getRoleGroupByCode(String code) {
        RoleGroup roleGroup = roleGroupRepository
                .findById(UUID.fromString(code))
                .orElseThrow(() -> new DataNotFoundException("Cant find role group by code " + code));
        return roleGroup;
    }

    @Override
    public RoleGroup createRoleGroup(RoleGroupDTO roleGroupDTO) {
//        List<Function> functions
        RoleGroup newRoleGroup = RoleGroup.builder()
                .roleGroupName(roleGroupDTO.getRoleGroupName())
                .description(roleGroupDTO.getDescription())
                .build();
        return roleGroupRepository.save(newRoleGroup);
    }

    @Override
    public RoleGroup updateRoleGroup(RoleGroupDTO roleGroupDTO, String code) {
        RoleGroup existingRoleGroup = getRoleGroupByCode(code);
        existingRoleGroup.setRoleGroupName(roleGroupDTO.getRoleGroupName());
        existingRoleGroup.setDescription(roleGroupDTO.getDescription());

        return roleGroupRepository.save(existingRoleGroup);
    }

    @Override
    public void deleteRoleGroup(String code) {
        roleGroupRepository.deleteById(UUID.fromString(code));
    }
}
