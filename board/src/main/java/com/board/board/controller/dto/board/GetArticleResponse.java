package com.board.board.controller.dto.board;

import com.board.board.domain.entity.BoardEntity;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GetArticleResponse {
    private Long id;
    private String name;
    private String title;
    private String content;
    private int depth;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    public GetArticleResponse(BoardEntity entity){
        this.id = entity.getId();
        this.name = entity.getName();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.depth = entity.getDepth();
        this.createDate = entity.getCreateDate();
        this.updateDate = entity.getUpdateDate();
    }
}
