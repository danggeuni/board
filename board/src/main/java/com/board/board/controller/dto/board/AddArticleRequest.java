package com.board.board.controller.dto.board;

import com.board.board.domain.entity.BoardEntity;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AddArticleRequest {
    private String name;
    private String title;
    private String content;

    public BoardEntity toEntity(){
        return new BoardEntity(null, name, title, content, null, 0, 0, LocalDateTime.now(), null);
    }
}
