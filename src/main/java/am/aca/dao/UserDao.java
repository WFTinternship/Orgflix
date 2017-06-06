package am.aca.dao;

import am.aca.entity.*;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by David on 5/27/2017.
 */
public interface UserDao {

    int addUser(User user);

    User getUser(int id);

    User getUser(String email);

    boolean editUser(User user);

    // User authenticate(String email, String pass) throws SQLException;

}
