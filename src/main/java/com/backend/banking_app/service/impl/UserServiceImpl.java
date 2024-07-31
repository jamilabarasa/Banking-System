package com.backend.banking_app.service.impl;

import com.backend.banking_app.dto.UserDTO;
import com.backend.banking_app.exceptions.BadRequestException;
import com.backend.banking_app.exceptions.ResourceNotFoundException;
import com.backend.banking_app.mapper.UserMapper;
import com.backend.banking_app.model.User;
import com.backend.banking_app.repository.UserRepository;
import com.backend.banking_app.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

//    @Autowired
//    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDTO getUserById(Long id) {
        log.debug("About to get user with ID {}", id);
        User user = userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("User not found with ID: " + id));
        return UserMapper.mapToDTO(user);
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        log.debug("About to get user with email {}", email);
        User user = userRepository.findByEmail(email).orElseThrow(() ->
                new ResourceNotFoundException("User not found with email: " + email));
        return UserMapper.mapToDTO(user);
    }

    @Override
    public User createUser(User user) {
        log.debug("About to register user {}", user.getEmail());

        //check if email exists
        if (userRepository.existsByEmail(user.getEmail())) {
            log.warn("User exists with email {}", user.getEmail());
            throw new BadRequestException("User exists with email: " + user.getEmail());
        }

        //hash password
        // Hash the password
//        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }


    @Override
    public UserDTO updateUser(UserDTO userDTO, Long id) {
        log.debug("About to update user with ID: {}", id);

        User user = userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("User not found with ID:" + id));

        //check if email exists
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            log.debug("Email {} exists ", userDTO.getEmail());
            throw new BadRequestException("Email already exists");
        }

        if (userDTO.getFirstName() != null && !userDTO.getFirstName().isEmpty()) {
            user.setFirstName(userDTO.getFirstName());
        }
        if (userDTO.getLastName() != null && userDTO.getLastName().isEmpty()) {
            user.setLastName(userDTO.getLastName());
        }
        if (userDTO.getEmail() != null && userDTO.getEmail().isEmpty()) {
            user.setEmail(userDTO.getEmail());
        }
        if (userDTO.getAddress() != null && userDTO.getAddress().isEmpty()) {
            user.setAddress(user.getAddress());
        }

        User savedUser = userRepository.save(user);
        return UserMapper.mapToDTO(savedUser);
    }

    @Override
    public void deleteUser(Long id) {
        log.debug("About to delete user with ID {}", id);
        //check if user exists
        if (!userRepository.existsById(id)) {
            log.warn("User not found with ID: {}", id);
            throw new ResourceNotFoundException("User not found with ID: " + id);
        }

        userRepository.deleteById(id);
    }

    @Override
    public List<UserDTO> users() {

        log.debug("Request to get all user");

        List<User> users = userRepository.findAll();

        List<UserDTO> userDTOS = users.stream()
                .map(UserMapper::mapToDTO)
                .collect(Collectors.toList());

        return userDTOS;
    }


}
