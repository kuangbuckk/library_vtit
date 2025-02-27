package com.project.library.services;

import com.project.library.components.JwtTokenUtils;
import com.project.library.dtos.UserDTO;
import com.project.library.entities.RoleGroup;
import com.project.library.entities.User;
import com.project.library.events.UserRegisterEvent;
import com.project.library.exceptions.DataNotFoundException;
import com.project.library.repositories.RoleGroupRepository;
import com.project.library.repositories.UserRepository;
import com.project.library.responses.UserPageResponse;
import com.project.library.responses.UserResponse;
import com.project.library.services.interfaces.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserServiceImpl implements IUserService {
    private final UserRepository userRepository;
    private final RoleGroupRepository roleGroupRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final JwtTokenUtils jwtTokenUtils;

    @Override
    public UserPageResponse getUsers(int pageNumber, int size, String keyword) {
        Pageable pageable = PageRequest.of(pageNumber, size);
        Page<User> users = userRepository.findAll(pageable, keyword);
        int totalPages = users.getTotalPages();
        List<UserResponse> userResponses = users.getContent()
                .stream()
                .map(user -> UserResponse.fromUser(user))
                .toList();
        return UserPageResponse.builder()
                .totalPages(totalPages)
                .userResponses(userResponses)
                .build();
    }

    @Override
    public UserResponse getUserByCode(UUID code) {
        User existingUser = userRepository.findById(code)
                .orElseThrow(()-> new DataNotFoundException("User not found with code" + code));
        return UserResponse.fromUser(existingUser);
    }

    @Override
    public UserResponse createUser(UserDTO userDTO) {
        if (userDTO.getRoleGroupCodes().isEmpty()) {
            throw new DataNotFoundException("Role group code is empty");
        }
        List<RoleGroup> roleGroups = roleGroupRepository.findAllById(userDTO.getRoleGroupCodes());
        applicationEventPublisher.publishEvent(new UserRegisterEvent(userDTO.getUsername()));
        User newUser = User.builder()
                .fullName(userDTO.getFullName())
                .username(userDTO.getUsername())
                .email(userDTO.getEmail())
                .phoneNumber(userDTO.getPhoneNumber())
                .dateOfBirth(userDTO.getDateOfBirth())
                .address(userDTO.getAddress())
                .roleGroups(roleGroups)
                .build();
        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());
        newUser.setPassword(encodedPassword);
        userRepository.save(newUser);
        return UserResponse.fromUser(newUser);
    }

    @Override
    public UserResponse updateUser(UserDTO User, UUID code) {
        User existingUser = userRepository.findById(code)
                .orElseThrow(()-> new DataNotFoundException("User not found with code" + code));
        existingUser.setFullName(User.getFullName());
        existingUser.setUsername(User.getUsername());
        existingUser.setEmail(User.getEmail());
        existingUser.setPhoneNumber(User.getPhoneNumber());
        existingUser.setDateOfBirth(User.getDateOfBirth());
        existingUser.setAddress(User.getAddress());
        userRepository.save(existingUser);
        return UserResponse.fromUser(existingUser);
    }

    @Override
    public void deleteUser(UUID code) {
        userRepository.deleteById(code);
    }

    @Override
    public String login(String username, String password) {
        User existingUser = userRepository.findByUsername(username)
                .orElseThrow(()-> new BadCredentialsException("Username/password is not correct"));
        if (!passwordEncoder.matches(password, existingUser.getPassword())) {
            throw new BadCredentialsException("Wrong password");
        }
        if (existingUser.getRoleGroups().isEmpty()) {
            throw new DataNotFoundException("Role group code is empty");
        }
        return jwtTokenUtils.generateToken(existingUser);
    }
}
