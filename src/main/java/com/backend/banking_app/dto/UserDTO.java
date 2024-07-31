package com.backend.banking_app.dto;

import com.backend.banking_app.model.enumerations.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class UserDTO {

    private Long id;

    private String phoneNumber;

    private String firstName;

    private String lastName;

    private String email;

    private Date dateOfBirth;

    private String address;

    private UserType userType;
}
