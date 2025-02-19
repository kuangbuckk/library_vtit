package com.project.library.services.interfaces;

import com.project.library.dtos.UserDTO;
import com.project.library.entities.User;

import java.util.List;

public interface IUserService {
    //CRUD
    List<User> getUsers();
    User getUserByCode(String code);
    User createUser(UserDTO User);
    User updateUser(UserDTO User, String code);
    void deleteUser(String code);

    //Actions
    String login(String username, String password);
}
