package com.project.library.services;

import com.project.library.dtos.UserDTO;
import com.project.library.dtos.search.UserSearchDTO;
import com.project.library.responses.LoginResponse;
import com.project.library.responses.UserPageResponse;
import com.project.library.responses.UserResponse;

public interface UserService {
    //CRUD
    UserPageResponse getUsers(int pageNumber, int size, UserSearchDTO userSearchDTO);
    UserResponse getUserByCode(Long id);
    UserResponse createUser(UserDTO User);
    UserResponse updateUser(UserDTO User, Long id);
    UserResponse deleteUser(Long id);
    void destroyUser(Long id);

    //Actions
    LoginResponse login(String username, String password);
    LoginResponse refreshToken(String token) throws Exception;
    void logout(String token);
}
