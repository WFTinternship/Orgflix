package am.aca.dao.springjdbc.implspringjdbc.mapper;


import am.aca.entity.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by David on 6/20/2017
 */
public class UserRowMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("ID"));
        user.setNick(rs.getString("Nick"));
        user.setUserName(rs.getString("User_Name"));
        user.setPass(rs.getString("User_Pass"));
        user.setEmail(rs.getString("Email"));
        return user;
    }
}
