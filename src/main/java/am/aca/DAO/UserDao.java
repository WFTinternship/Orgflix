package am.aca.DAO;

import am.aca.main.*;

import java.sql.SQLException;

/**
 * Created by David on 5/27/2017.
 */
public interface UserDao {
    boolean addUser(User user) throws SQLException;
    boolean editUser(User user) throws SQLException;
    User authenticate(String email, String pass) throws SQLException;
}
