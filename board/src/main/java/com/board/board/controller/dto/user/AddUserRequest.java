package com.board.board.controller.dto.user;

import com.board.board.domain.entity.UserEntity;
import lombok.Getter;

@Getter
public class AddUserRequest {

    private final String userId;
    private final String name;
    private final String password;

    public AddUserRequest(String userId, String name, String password) {
        this.userId = userId;
        this.name = name;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public UserEntity toEntity(){
        return new UserEntity(userId, name, password);
    }
}
