package com.project.library.services.impl;

import com.project.library.utils.JwtTokenUtils;
import com.project.library.dtos.UserDTO;
import com.project.library.dtos.search.UserSearchDTO;
import com.project.library.entities.RoleGroup;
import com.project.library.entities.User;
import com.project.library.events.UserRegisterEvent;
import com.project.library.exceptions.DataNotFoundException;
import com.project.library.repositories.RoleGroupRepository;
import com.project.library.repositories.UserRepository;
import com.project.library.responses.LoginResponse;
import com.project.library.responses.UserPageResponse;
import com.project.library.responses.UserResponse;
import com.project.library.services.TokenService;
import com.project.library.services.UserService;
import com.project.library.utils.MessageKeys;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleGroupRepository roleGroupRepository;
    private final TokenService tokenService;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final JwtTokenUtils jwtTokenUtils;

    @Override
    public UserPageResponse getUsers(int pageNumber, int size, UserSearchDTO userSearchDTO) {
        Pageable pageable = PageRequest.of(pageNumber, size);
        Page<User> users = userRepository.findAll(pageable, userSearchDTO);
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
    public UserResponse getUserByCode(Long id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException(MessageKeys.USER_NOT_FOUND, id));
        return UserResponse.fromUser(existingUser);
    }

    @Override
    @Transactional
    public UserResponse createUser(UserDTO userDTO) {
        if (userDTO.getRoleGroupIds().isEmpty()) {
            throw new BadCredentialsException(MessageKeys.ROLE_GROUP_NOT_FOUND);
        }
        List<RoleGroup> roleGroups = roleGroupRepository.findAllById(userDTO.getRoleGroupIds());
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
    @Transactional
    public UserResponse updateUser(UserDTO User, Long id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException(MessageKeys.USER_NOT_FOUND, id));
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
    @Transactional
    public UserResponse deleteUser(Long id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException(MessageKeys.USER_NOT_FOUND, id));
        existingUser.setIsDeleted(true);
        existingUser.setIsActive(false);
        userRepository.save(existingUser);
        return UserResponse.fromUser(existingUser);
    }

    @Override
    @Transactional
    public void destroyUser(Long id) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException(MessageKeys.USER_NOT_FOUND, id));
        userRepository.delete(existingUser);
    }

    @Override
    public LoginResponse login(String username, String password) {
        User existingUser = userRepository.findByUsername(username)
                .orElseThrow(()-> new BadCredentialsException(MessageKeys.LOGIN_FAILED));
        if (!passwordEncoder.matches(password, existingUser.getPassword())) {
            throw new BadCredentialsException(MessageKeys.LOGIN_FAILED);
        }
        if (existingUser.getRoleGroups().isEmpty()) {
            throw new BadCredentialsException(MessageKeys.ROLE_GROUP_NOT_FOUND);
        }
        existingUser.setIsActive(true);
        userRepository.save(existingUser);

        String accessToken = jwtTokenUtils.generateAccessToken(existingUser);
        String refreshToken = tokenService.generateRefreshTokenAfterLogin(existingUser);

        return LoginResponse.builder()
                .token(accessToken)
                .refreshToken(refreshToken)
                .username(existingUser.getUsername())
                .roles(existingUser.getRoleGroups().stream().map(RoleGroup::getRoleGroupName).toList())
                .build();
    }

    @Override
    public LoginResponse refreshToken(String refreshToken) throws Exception {
        String username = jwtTokenUtils.getUsernameFromToken(refreshToken);
        User existingUser = userRepository.findByUsername(username)
                .orElseThrow(()-> new BadCredentialsException(MessageKeys.LOGIN_FAILED));
        if (jwtTokenUtils.validateToken(refreshToken, existingUser) && tokenService.isTokenExist(refreshToken)) {
            return tokenService.refreshToken(refreshToken, existingUser);
        }
        else {
            throw new BadCredentialsException(MessageKeys.LOGIN_FAILED);
        }
    }

    @Override
    public void logout(String refreshToken) {
//        String username = jwtTokenUtils.getUsernameFromToken(refreshToken);
        tokenService.invalidateRefreshToken(refreshToken);
    }
}
