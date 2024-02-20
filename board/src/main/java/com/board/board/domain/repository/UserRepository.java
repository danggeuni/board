package com.board.board.domain.repository;

import com.board.board.domain.entity.UserEntity;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public UserEntity registerUser(UserEntity entity) {
        jdbcTemplate.update("INSERT INTO USERS (USERID, NAME, PASSWORD) VALUES (?, ?, ?)",
                entity.getUserId(), entity.getName(), entity.getPassword());

        return entity;
    }

    public UserEntity findById(String id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM USERS WHERE USERID = ?", new Object[]{id}, userEntityRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    RowMapper<UserEntity> userEntityRowMapper(){
        return (rs, rowNum) -> new UserEntity(
                rs.getString("USERID"),
                rs.getString("NAME"),
                rs.getString("PASSWORD")
        );
    }
}
