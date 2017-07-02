package am.aca.orgflix.dao.impljdbc.mapper;


import am.aca.orgflix.entity.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * RowMapper for User entity
 */
public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("ID"));
        user.setNick(rs.getString("Nick"));
        user.setUserName(rs.getString("User_Name"));
        user.setPass(rs.getString("User_Pass"));
        user.setEmail(rs.getString("Email"));
        return user;
    }
}
