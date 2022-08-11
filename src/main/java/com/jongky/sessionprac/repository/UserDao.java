package com.jongky.sessionprac.repository;

import com.jongky.sessionprac.model.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class UserDao {
    private final JdbcTemplate jdbcTemplate;

    public UserDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int join(User user) {
        String joinQuery = "insert into user(username, email,password, role) values(?,?,?,?)";
        Object[] joinParams = new Object[]{user.getUsername(), user.getEmail(),user.getPassword(), user.getRole()};

        return this.jdbcTemplate.update(joinQuery, joinParams);
    }

    public User findByEmail(String email) {
        String findByEmailQuery = "select id, username,email,password,role from user where email = ?";

        try {
            return this.jdbcTemplate.queryForObject(findByEmailQuery,
                    (rs, rowNum) -> new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getString("role")
                    ),email);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
