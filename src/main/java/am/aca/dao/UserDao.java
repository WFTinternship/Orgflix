package am.aca.dao;

import am.aca.entity.*;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by David on 5/27/2017.
 */
public interface UserDao {

    int addUser(User user, Connection connection) throws SQLException;

    User getUser(int id, Connection connection) throws SQLException;

    User getUser(String email, Connection connection) throws SQLException;

    boolean editUser(User user, Connection connection) throws SQLException;

    // User authenticate(String email, String pass) throws SQLException;

}
