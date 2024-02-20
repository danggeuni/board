package com.board.board.domain.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FileEntity {

    private final Long id;
    private final String path;
    private final String originName;
    private final String saveName;
    private final Long articleId;
}
