package com.board.board.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class BoardEntity {
    private final Long id;
    private final String name;
    private final String title;
    private final String content;
    private final Long groupId;
    private final int sortKey;
    private final int depth;
    private final LocalDateTime createDate;
    private final LocalDateTime updateDate;
}
