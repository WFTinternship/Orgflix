package am.aca.dao.oldjdbc.impljdbc;

import am.aca.dao.DaoException;
import am.aca.dao.oldjdbc.impljdbc.DaoJdbc;
import am.aca.dao.oldjdbc.UserDao;
import am.aca.entity.*;

import java.sql.*;

import am.aca.util.ConnType;

/**
 * Created by David on 5/27/2017
 */
public class UserDaoJdbc extends DaoJdbc implements UserDao {

    public UserDaoJdbc() {
        super();
    }

    public UserDaoJdbc(ConnType connType) {
        super(connType);
    }

    @Override
    public int addUser(User user) {
        // Assures that no null or empty record will be passed for NOT NULL fields
        if (!user.checkFields()) return -1;
        // default return value which means nothing added
        int id = -1;

        ResultSet resultSet = null;
        PreparedStatement statement = null;
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            final String query = "INSERT INTO users (Nick,User_Name,Email,User_Pass) " +
                    " VALUES( ? , ? , ? , ? ) ";

            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getNick());
            statement.setString(2, user.getUserName());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getPass());

            if (statement.executeUpdate() == 1) {
                resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) id = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        } finally {
            dataSource.closeConnections(new Object[]{statement, resultSet, connection});
        }
        user.setId(id);
        return id;
    }

    @Override
    public User getUser(int id) {
        User user = null;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            final String query = "SELECT Nick,User_Name,Email,User_Pass FROM users WHERE ID = ? LIMIT 1";

            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                user = new User();
                user.setId(id);
                user.setNick(resultSet.getString("Nick"));
                user.setUserName(resultSet.getString("User_Name"));
                user.setEmail(resultSet.getString("Email"));
                user.setPass(resultSet.getString("User_Pass"));
            }
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        } finally {
            dataSource.closeConnections(new Object[]{resultSet, statement, connection});
        }
        return user;
    }

    @Override
    public User getUser(String email) {
        User user = null;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            final String query = "SELECT ID,Nick,User_Name,User_Pass FROM users WHERE Email = ? LIMIT 1";

            statement = connection.prepareStatement(query);
            statement.setString(1, email);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                user = new User();
                user.setId(resultSet.getInt("id"));
                user.setNick(resultSet.getString("Nick"));
                user.setUserName(resultSet.getString("User_Name"));
                user.setEmail(email);
                user.setPass(resultSet.getString("User_Pass"));
            }
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        } finally {
            dataSource.closeConnections(new Object[]{resultSet, statement, connection});
        }
        return user;
    }

    @Override
    public boolean editUser(User user) {
        // Assures that no null or empty record will be passed for NOT NULL fields
        if (!user.checkFields()) return false;
        boolean state;

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = dataSource.getConnection();

            final String query = "UPDATE users SET Nick = ?,User_Name = ?,Email = ?, User_Pass = ? " +
                    " WHERE ID = ? ";

            statement = connection.prepareStatement(query);
            statement.setString(1, user.getNick());
            statement.setString(2, user.getUserName());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getPass());
            statement.setInt(5, user.getId());

            state = statement.execute();
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        } finally {
            dataSource.closeConnections(new Object[]{statement, connection});
        }
        return state;
    }
}