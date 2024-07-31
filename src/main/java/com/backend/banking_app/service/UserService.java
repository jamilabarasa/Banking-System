package com.backend.banking_app.service;

import com.backend.banking_app.dto.UserDTO;
import com.backend.banking_app.model.User;

import java.util.List;

public interface UserService {

    UserDTO getUserById(Long id);

    UserDTO getUserByEmail(String email);

    User createUser(User user);

    UserDTO updateUser(UserDTO userDTO, Long id);

    void deleteUser(Long id);

    List<UserDTO> users();

    //log in user

}
