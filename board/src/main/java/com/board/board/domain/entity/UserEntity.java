package com.board.board.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserEntity {
    private final String userId;
    private final String name;
    private final String password;
}
