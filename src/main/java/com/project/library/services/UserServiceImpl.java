package com.project.library.services;

import com.project.library.components.JwtTokenUtils;
import com.project.library.dtos.UserDTO;
import com.project.library.entities.RoleGroup;
import com.project.library.entities.User;
import com.project.library.events.UserRegisterEvent;
import com.project.library.exceptions.DataNotFoundException;
import com.project.library.repositories.RoleGroupRepository;
import com.project.library.repositories.UserRepository;
import com.project.library.responses.LoginResponse;
import com.project.library.responses.UserPageResponse;
import com.project.library.responses.UserResponse;
import com.project.library.services.interfaces.ITokenService;
import com.project.library.services.interfaces.IUserService;
import com.project.library.utils.MessageKeys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.token.TokenService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserServiceImpl implements IUserService {
    private final UserRepository userRepository;
    private final RoleGroupRepository roleGroupRepository;
    private final ITokenService tokenService;
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
                .orElseThrow(()-> new DataNotFoundException(MessageKeys.USER_NOT_FOUND, code));
        return UserResponse.fromUser(existingUser);
    }

    @Override
    @Transactional
    public UserResponse createUser(UserDTO userDTO) {
        if (userDTO.getRoleGroupCodes().isEmpty()) {
            throw new BadCredentialsException(MessageKeys.ROLE_GROUP_NOT_FOUND);
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
    @Transactional
    public UserResponse updateUser(UserDTO User, UUID code) {
        User existingUser = userRepository.findById(code)
                .orElseThrow(()-> new DataNotFoundException(MessageKeys.USER_NOT_FOUND, code));
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
    public UserResponse deleteUser(UUID code) {
        User existingUser = userRepository.findById(code)
                .orElseThrow(()-> new DataNotFoundException(MessageKeys.USER_NOT_FOUND, code));
        existingUser.setIsDeleted(true);
        existingUser.setIsActive(false);
        userRepository.save(existingUser);
        return UserResponse.fromUser(existingUser);
    }

    @Override
    @Transactional
    public void destroyUser(UUID code) {
        User existingUser = userRepository.findById(code)
                .orElseThrow(()-> new DataNotFoundException(MessageKeys.USER_NOT_FOUND, code));
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
        String refreshToken = tokenService.generateRefreshToken(existingUser);
        return LoginResponse.builder()
                .token(accessToken)
                .refreshToken(refreshToken)
                .username(existingUser.getUsername())
                .roles(existingUser.getRoleGroups().stream().map(RoleGroup::getRoleGroupName).toList())
                .build();
    }

    @Override
    public String refreshToken(String refreshToken) {
        String username = jwtTokenUtils.getUsernameFromToken(refreshToken);
        User existingUser = userRepository.findByUsername(username)
                .orElseThrow(()-> new BadCredentialsException(MessageKeys.LOGIN_FAILED));
        if (jwtTokenUtils.validateToken(refreshToken, existingUser) && tokenService.isTokenExist(refreshToken)) {
            return jwtTokenUtils.generateAccessToken(existingUser);
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
