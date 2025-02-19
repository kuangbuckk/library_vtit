package com.project.library.services;

import com.project.library.dtos.UserDTO;
import com.project.library.entities.RoleGroup;
import com.project.library.entities.User;
import com.project.library.exceptions.DataNotFoundException;
import com.project.library.repositories.RoleGroupRepository;
import com.project.library.repositories.UserRepository;
import com.project.library.services.interfaces.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserServiceImpl implements IUserService {
    private final UserRepository userRepository;
    private final RoleGroupRepository roleGroupRepository;

    @Override
    public List<User> getUsers() {
        List<User> users = userRepository.findAll();
        return users;
    }

    @Override
    public User getUserByCode(String code) {
        User existingUser = userRepository.findById(UUID.fromString(code))
                .orElseThrow(()-> new DataNotFoundException("User not found with code" + code));
        return existingUser;
    }

    @Override
    public User createUser(UserDTO userDTO) {
        if (userDTO.getRoleGroupCodes().isEmpty()) {
            throw new DataNotFoundException("Role group code is empty");
        }
        List<RoleGroup> roleGroups = roleGroupRepository.findAllById(userDTO.getRoleGroupCodes());

        User newUser = User.builder()
                .fullName(userDTO.getFullName())
                .username(userDTO.getUsername())
                .email(userDTO.getEmail())
                .phoneNumber(userDTO.getPhoneNumber())
                .dateOfBirth(userDTO.getDateOfBirth())
                .address(userDTO.getAddress())
                .roleGroups(roleGroups)
                .password(userDTO.getPassword())
                .build();
        return userRepository.save(newUser);
    }

    @Override
    public User updateUser(UserDTO User, String code) {
        return null;
    }

    @Override
    public void deleteUser(String code) {

    }

    @Override
    public String login(String username, String password) {
        return "";
    }
}
