package com.project.library.services.interfaces;

import com.project.library.dtos.UserDTO;
import com.project.library.entities.User;
import com.project.library.responses.LoginResponse;
import com.project.library.responses.UserPageResponse;
import com.project.library.responses.UserResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface IUserService {
    //CRUD
    UserPageResponse getUsers(int pageNumber, int size, UserSearchDTO userSearchDTO);
    UserResponse getUserByCode(UUID code);
    UserResponse createUser(UserDTO User);
    UserResponse updateUser(UserDTO User, UUID code);
    UserResponse deleteUser(UUID code);
    void destroyUser(UUID code);

    //Actions
    LoginResponse login(String username, String password);
    String refreshToken(String token);
    void logout(String token);
}
