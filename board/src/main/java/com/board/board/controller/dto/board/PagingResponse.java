package com.board.board.controller.dto.board;

import lombok.Data;

import java.util.List;

@Data
public class PagingResponse<T> {
    private List<T> data;
    private int totalPages;

    public PagingResponse(List<T> data, int totalPages){
        this.data = data;
        this.totalPages = totalPages;
    }
}
