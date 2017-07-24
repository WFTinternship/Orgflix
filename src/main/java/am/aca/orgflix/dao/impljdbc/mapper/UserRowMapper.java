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
        user.setNick(rs.getString("NICK"));
        user.setUserName(rs.getString("USER_NAME"));
        user.setPass(rs.getString("USER_PASS"));
        user.setEmail(rs.getString("EMAIL"));
        return user;
    }
}
