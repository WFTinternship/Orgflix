package am.aca.dao;

import am.aca.entity.*;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by David on 5/27/2017.
 */
public interface UserDao {

    int addUser(User user) throws SQLException;

    User getUser(int id) throws SQLException;

    User getUser(String email) throws SQLException;

    boolean editUser(User user) throws SQLException;

    // User authenticate(String email, String pass) throws SQLException;

}
