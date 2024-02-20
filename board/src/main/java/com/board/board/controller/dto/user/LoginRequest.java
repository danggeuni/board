package com.board.board.controller.dto.user;

import lombok.Data;

@Data
public class LoginRequest {
    private String userId;
    private String password;
}
