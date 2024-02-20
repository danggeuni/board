package com.board.board.domain.repository;

import com.board.board.domain.entity.BoardEntity;
import com.board.board.domain.entity.FileEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BoardRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BoardRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<BoardEntity> getArticles(int pageSize, int pageNumber, String search) {
        int offset = (pageNumber - 1) * pageSize;

        if (search == null) {
            return jdbcTemplate.query("SELECT * FROM BOARD ORDER BY GROUP_ID DESC, SORT_KEY LIMIT ? OFFSET ?",
                    new Object[]{pageSize, offset}, boardEntityRowMapper());
        } else {
            return jdbcTemplate.query("SELECT * FROM BOARD WHERE TITLE LIKE ? ORDER BY GROUP_ID DESC, SORT_KEY LIMIT ? OFFSET ?",
                    new Object[]{"%" + search + "%", pageSize, offset}, boardEntityRowMapper());
        }
    }

    public int getTotalPage(int pageSize) {
        int totalArticles = jdbcTemplate.queryForObject("SELECT COUNT(ID) FROM BOARD", Integer.class);
        return (int) Math.ceil((double) totalArticles / pageSize);
    }

    public BoardEntity insertArticle(BoardEntity entity) {
        if (entity.getGroupId() == null) {
            jdbcTemplate.update("INSERT INTO BOARD (NAME, TITLE, CONTENTS, SORT_KEY, DEPTH, WRITE_DATE) VALUES (?, ?, ?, ?, ?, ?)",
                    entity.getName(), entity.getTitle(), entity.getContent(), entity.getSortKey(), entity.getDepth(), entity.getCreateDate());

            jdbcTemplate.update("UPDATE BOARD SET GROUP_ID = ID WHERE ID = LAST_INSERT_ID()");
        } else {
            jdbcTemplate.update("INSERT INTO BOARD (NAME, TITLE, CONTENTS, GROUP_ID, SORT_KEY, DEPTH, WRITE_DATE) VALUES (?, ?, ?, ?, ?, ?, ?)",
                    entity.getName(), entity.getTitle(), entity.getContent(), entity.getGroupId(), entity.getSortKey(), entity.getDepth(), entity.getCreateDate());
        }

        return entity;
    }

    public void insertFile(String path, String originFileName, String downFileName, Long id){
        jdbcTemplate.update("INSERT INTO FILE (PATH, ORIGIN_NAME, DOWN_NAME, BOARD_ID) VALUES (?, ?, ?, ?)",
                path, originFileName, downFileName, id);
    }

    public BoardEntity findArticleById(Long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM BOARD WHERE ID = ?", new Object[]{id}, boardEntityRowMapper());
    }

    public BoardEntity getLastArticle() {
        return jdbcTemplate.queryForObject("SELECT * FROM BOARD WHERE ID = LAST_INSERT_ID()", boardEntityRowMapper());
    }

    public FileEntity getFileById(Long id){
        return jdbcTemplate.queryForObject("SELECT * FROM BOARD LEFT OUTER JOIN FILE ON BOARD.ID = FILE.BOARD_ID WHERE BOARD.ID = ?", new Object[]{id}, fileEntityRowMapper());
    }

    RowMapper<BoardEntity> boardEntityRowMapper() {
        return (rs, rowNum) -> new BoardEntity(
                rs.getLong("ID"),
                rs.getString("NAME"),
                rs.getString("TITLE"),
                rs.getString("CONTENTS"),
                rs.getLong("GROUP_ID"),
                rs.getInt("SORT_KEY"),
                rs.getInt("DEPTH"),
                rs.getTimestamp("WRITE_DATE").toLocalDateTime(),
                rs.getTimestamp("UPDATE_DATE").toLocalDateTime());
    }

    RowMapper<FileEntity> fileEntityRowMapper(){
        return (rs, rowNum) -> new FileEntity(
                rs.getLong("ID"),
                rs.getString("PATH"),
                rs.getString("ORIGIN_NAME"),
                rs.getString("DOWN_NAME"),
                rs.getLong("BOARD_ID")
        );
    }
}
