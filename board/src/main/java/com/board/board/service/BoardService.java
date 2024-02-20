package com.board.board.service;

import com.board.board.controller.dto.board.AddArticleRequest;
import com.board.board.controller.dto.board.PagingResponse;
import com.board.board.domain.entity.BoardEntity;
import com.board.board.domain.entity.FileEntity;
import com.board.board.domain.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BoardService {
    private final BoardRepository boardRepository;

    @Autowired
    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public PagingResponse<BoardEntity> getArticles(int pageSize, int pageNumber, String search){
        List<BoardEntity> data = boardRepository.getArticles(pageSize, pageNumber, search);
        int totalPages = boardRepository.getTotalPage(pageSize);

        return new PagingResponse<>(data, totalPages);
    }

    public BoardEntity findArticleById(Long id){
        return boardRepository.findArticleById(id);
    }

    public BoardEntity insertArticle(AddArticleRequest request, Long parentId) {
        if (parentId != null) {
            BoardEntity parentData = boardRepository.findArticleById(parentId);

            return boardRepository.insertArticle(new BoardEntity(
                    null, request.getName(), request.getTitle(), request.getContent(), parentData.getGroupId(),
                    parentData.getSortKey() + 1, parentData.getDepth() + 1, LocalDateTime.now(), null));
        }

        return boardRepository.insertArticle(request.toEntity());
    }

    public void insertFile(String path, String originFileName, String downFileName, Long id){
        boardRepository.insertFile(path, originFileName, downFileName, id);
    }

    public BoardEntity getLastArticle(){
        return boardRepository.getLastArticle();
    }

    public FileEntity getFileById(Long id){
        return boardRepository.getFileById(id);
    }
}
