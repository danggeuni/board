package com.board.board.controller.dto.user;

import com.board.board.domain.entity.UserEntity;
import lombok.Data;

@Data
public class AddUserRequest {

    private String userId;
    private String name;
    private String password;

    public UserEntity toEntity(){
        return new UserEntity(userId, name, password);
    }
}
