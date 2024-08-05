package com.backend.banking_app.controller;

import com.backend.banking_app.controller.vm.ErrorResponse;
import com.backend.banking_app.controller.vm.JwtAuthResponse;
import com.backend.banking_app.controller.vm.LoginResponseVM;
import com.backend.banking_app.controller.vm.SuccessResponse;
import com.backend.banking_app.dto.LoginDTO;
import com.backend.banking_app.service.AuthService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("api/auth")
public class AuthController {

    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity login(@Valid @RequestBody LoginDTO loginDTO){
        log.debug("REQUEST to login user {}",loginDTO.getEmail());

        String token = authService.login(loginDTO);

        SuccessResponse successResponse =  new SuccessResponse(
                "Login Successfully",
                HttpStatus.OK.value(),
                System.currentTimeMillis(),
                "SUCCESS"
        );

        return ResponseEntity.ok().
                header(HttpHeaders.AUTHORIZATION,"Bearer " + token)
                .body(successResponse);

    }


}
