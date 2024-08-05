package com.backend.banking_app.service;

import com.backend.banking_app.dto.LoginDTO;

public interface AuthService {

    String login(LoginDTO loginDTO);
}
