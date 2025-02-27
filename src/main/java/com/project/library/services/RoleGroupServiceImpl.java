package com.project.library.services;

import com.project.library.dtos.RoleGroupDTO;
import com.project.library.entities.Function;
import com.project.library.entities.RoleGroup;
import com.project.library.exceptions.DataNotFoundException;
import com.project.library.repositories.FunctionRepository;
import com.project.library.repositories.RoleGroupRepository;
import com.project.library.responses.RoleGroupResponse;
import com.project.library.services.interfaces.IRoleGroupService;
import com.project.library.utils.MessageKeys;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RoleGroupServiceImpl implements IRoleGroupService {
    private final RoleGroupRepository roleGroupRepository;
    private final FunctionRepository functionRepository;

    @Override
    public List<RoleGroupResponse> getRoleGroups() {
        return roleGroupRepository.findAll()
                .stream()
                .map(RoleGroupResponse::fromRoleGroup)
                .toList();
    }

    @Override
    public RoleGroupResponse getRoleGroupByCode(UUID code) {
        RoleGroup roleGroup = roleGroupRepository
                .findById(code)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.ROLE_GROUP_NOT_FOUND, code));
        return RoleGroupResponse.fromRoleGroup(roleGroup);
    }

    @Override
    public RoleGroupResponse createRoleGroup(RoleGroupDTO roleGroupDTO) {
        List<Function> functions = functionRepository.findAllById(roleGroupDTO.getFunctionCodes());
        RoleGroup newRoleGroup = RoleGroup.builder()
                .roleGroupName(roleGroupDTO.getRoleGroupName())
                .description(roleGroupDTO.getDescription())
                .functions(functions)
                .build();
        roleGroupRepository.save(newRoleGroup);
        return RoleGroupResponse.fromRoleGroup(newRoleGroup);
    }

    @Override
    public RoleGroupResponse updateRoleGroup(RoleGroupDTO roleGroupDTO, UUID code) {
        List<Function> functions = functionRepository.findAllById(roleGroupDTO.getFunctionCodes());
        RoleGroup existingRoleGroup = roleGroupRepository.findById(code)
                .orElseThrow(() -> new DataNotFoundException(MessageKeys.ROLE_GROUP_NOT_FOUND, code));

        existingRoleGroup.setRoleGroupName(roleGroupDTO.getRoleGroupName());
        existingRoleGroup.setDescription(roleGroupDTO.getDescription());
        existingRoleGroup.setFunctions(functions);

        roleGroupRepository.save(existingRoleGroup);
        return RoleGroupResponse.fromRoleGroup(existingRoleGroup);
    }

    @Override
    public void deleteRoleGroup(UUID code) {
        roleGroupRepository.deleteById(code);
    }
}
