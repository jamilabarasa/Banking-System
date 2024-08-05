package com.backend.banking_app.controller.vm;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseVM {
    private SuccessResponse successResponse;
    private JwtAuthResponse jwtAuthResponse;

}
