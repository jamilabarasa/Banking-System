package com.backend.banking_app.controller;

import com.backend.banking_app.controller.vm.SuccessResponse;
import com.backend.banking_app.dto.UserDTO;
import com.backend.banking_app.model.User;
import com.backend.banking_app.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    //create user
    @PostMapping
    public ResponseEntity<SuccessResponse> createUser(@Valid @RequestBody User user){
        log.debug("REST request to register user {}",user.getEmail());
        userService.createUser(user);
        SuccessResponse successResponse = new SuccessResponse("User created successfully",201);
        return new ResponseEntity<>(successResponse, HttpStatus.CREATED);
    }

    //get user by email
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email){
        log.debug("REST request to get user with email : {}",email);
        return new ResponseEntity<>(userService.getUserByEmail(email),HttpStatus.OK);
    }

    //get user by id
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id){
        log.debug("REST request to get user with ID : {}",id);
        return new ResponseEntity<>(userService.getUserById(id),HttpStatus.OK);
    }

    //Delete user by id
    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse> deleteUser(@PathVariable Long id){
        log.debug("REST request to delete user by ID {}",id);
        userService.deleteUser(id);
        SuccessResponse successResponse = new SuccessResponse(
                "User deleted successfully",
                204
        );
        return new ResponseEntity<>(successResponse,HttpStatus.NO_CONTENT);
    }

    //update user
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO,@PathVariable Long id){
        log.debug("REST Request to update user with ID :",id);
        return new ResponseEntity<>(userService.updateUser(userDTO,id),HttpStatus.OK);
    }

    //get all users
    @GetMapping
    public ResponseEntity<List<UserDTO>> getUsers(){
        log.debug("REST Request to get all users");
        return new ResponseEntity<>(userService.users(),HttpStatus.OK);
    }



}
