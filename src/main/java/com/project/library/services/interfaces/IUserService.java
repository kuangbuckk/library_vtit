package com.project.library.services.interfaces;

import com.project.library.dtos.UserDTO;
import com.project.library.entities.User;
import com.project.library.responses.UserPageResponse;
import com.project.library.responses.UserResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface IUserService {
    //CRUD
    UserPageResponse getUsers(Pageable pageable);
    UserResponse getUserByCode(UUID code);
    UserResponse createUser(UserDTO User);
    UserResponse updateUser(UserDTO User, UUID code);
    void deleteUser(UUID code);

    //Actions
    String login(String username, String password);
}
