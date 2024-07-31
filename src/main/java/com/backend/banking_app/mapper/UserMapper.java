package com.backend.banking_app.mapper;

import com.backend.banking_app.dto.UserDTO;
import com.backend.banking_app.model.User;

public class UserMapper {

    public static UserDTO mapToDTO(User user) {
        UserDTO userDTO = new UserDTO(
                user.getId(),
                user.getPhoneNumber(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getDateOfBirth(),
                user.getAddress(),
                user.getUserType()
        );

        return userDTO;
    }


}
