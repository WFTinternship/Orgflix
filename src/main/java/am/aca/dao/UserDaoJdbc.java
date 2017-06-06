package am.aca.dao;

import am.aca.entity.*;
import am.aca.util.DbManager;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.*;
import org.apache.log4j.Logger;

/**
 * Created by David on 5/27/2017.
 */
public class UserDaoJdbc implements UserDao {
    private static final Logger LOGGER = Logger.getLogger( UserDao.class.getName() );

    @Override
    public int addUser(User user) {
        // Assures that no null or empty record will be passed for NOT NULL fields
        if( !user.checkFileds() ) return -1;
        // default return value which means nothing added
        int id = -1;

        ResultSet resultSet = null;
        PreparedStatement statm = null;
        Statement selStatm = null;
        Connection connection = null;
        try {
            connection = DbManager.getInstance().getConnection();
            final String query = "INSERT INTO users (Nick,User_Name,Email,User_Pass) " +
                    " VALUES( ? , ? , ? , ? ) ";

            statm = connection.prepareStatement(query);
            statm.setString(1, user.getNick());
            statm.setString(2, user.getUserName());
            statm.setString(3, user.getEmail());
            statm.setString(4, user.getPass());

            if (statm.executeUpdate() == 1) {
                String selQuery = "SELECT LAST_INSERT_ID() AS last_id FROM users;";
                selStatm = connection.createStatement();
                resultSet = selStatm.executeQuery(selQuery);
                if (resultSet.next()) id = resultSet.getInt("last_id");
            }
        }catch(SQLException e){
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        } finally {
            try{
                if(statm != null)
                    statm.close();
                if(selStatm != null)
                    selStatm.close();
                if(resultSet != null)
                    resultSet.close();
                if(connection != null)
                    connection.close();
            }catch (SQLException e){
                LOGGER.warn(e.getMessage());
                throw new DaoException(e.getMessage());
            }
        }
        user.setId(id);
        return id;
    }

    @Override
    public User getUser(int id){
        User user = null;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DbManager.getInstance().getConnection();
            final String query = "SELECT Nick,User_Name,Email,User_Pass FROM users WHERE ID = ? ";

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
        }catch(SQLException e){
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        } finally {
            try{
                if(statement != null)
                    statement.close();
                if(resultSet != null)
                    resultSet.close();
                if(connection != null)
                    connection.close();
            }catch(SQLException e){
                LOGGER.warn(e.getMessage());
                throw new DaoException(e.getMessage());
            }
        }
        return user;
    }

    @Override
    public User getUser(String email){
        User user = null;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DbManager.getInstance().getConnection();
            final String query = "SELECT ID,Nick,User_Name,User_Pass FROM users WHERE Email = ? ";

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
        }catch(SQLException e){
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        } finally{
            try{
                if(statement != null)
                    statement.close();
                if(resultSet != null)
                resultSet.close();
                if(connection != null)
                    connection.close();
            }catch(SQLException e){
                LOGGER.info(e.getMessage());
                throw new DaoException(e.getMessage());
            }
        }
        return user;
    }

    @Override
    public boolean editUser(User user){
        // Assures that no null or empty record will be passed for NOT NULL fields
        if (!user.checkFileds()) return false;
        boolean state = false;

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DbManager.getInstance().getConnection();

            final String query = "UPDATE users SET Nick = ?,User_Name = ?,Email = ?, User_Pass = ? " +
                    " WHERE ID = ? ";

            statement = connection.prepareStatement(query);
            statement.setString(1, user.getNick());
            statement.setString(2, user.getUserName());
            statement.setString(3, user.getEmail());
            statement.setString(4, user.getPass());
            statement.setInt(5, user.getId());

            state = statement.execute();
        }catch(SQLException e){
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        } finally {
            try{
                if(statement != null)
                    statement.close();
                if(connection != null)
                    connection.close();
            }catch(SQLException e){
                LOGGER.warn(e.getMessage());
                throw new DaoException(e.getMessage());
            }
        }
        return state;
    }
/*
    @Override
    public User authenticate(String email, String pass) throws SQLException {
        final String query = "SELECT id,Nick,User_Name FROM users WHERE Email = ? and User_Pass = ? LIMIT 1;";

        PreparedStatement statm = connection.prepareStatement(query);
        statm.setString(1, email );
        statm.setString(2, pass );
        ResultSet result = statm.executeQuery();

        User user = null;
        if (result.next()) {
            user = new User();
            user.setId( result.getInt("id") );
            user.setNick( result.getString("Nick") );
            user.setUserName( result.getString("User_Name") );
            user.setEmail( email );
            user.setPass( pass );
        }
        return user;
    }
*/
}
