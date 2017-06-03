package am.aca.dao;

import am.aca.entity.*;
import am.aca.util.DbManager;

import java.sql.*;

/**
 * Created by David on 5/27/2017.
 */
public class UserDaoJdbc implements UserDao {

    @Override
    public int addUser(User user) throws SQLException {
        Connection connection = DbManager.getConnection();
        // Assures that no null or empty record will be passed for NOT NULL fields
        if( !user.checkFileds() ) return -1;
        // default return value which means nothing added
        int id = -1;

        final String query = "INSERT INTO users (Nick,User_Name,Email,User_Pass) " +
                    " VALUES( ? , ? , ? , ? ) ";

        PreparedStatement statm = connection.prepareStatement(query);
        statm.setString(1, user.getNick() );
        statm.setString(2, user.getUserName() );
        statm.setString(3, user.getEmail() );
        statm.setString(4, user.getPass() );

        if( statm.executeUpdate() == 1 ) {
            String selQuery = "SELECT LAST_INSERT_ID() as last_id FROM users;";
            Statement selStatm = connection.createStatement();
            ResultSet resultSet = selStatm.executeQuery(selQuery);
            if( resultSet.next() ) id = resultSet.getInt("last_id");
        }
        user.setId(id);
        connection.close();
        return id;
    }

    @Override
    public User getUser(int id) throws SQLException {
        Connection connection = DbManager.getConnection();
        final String query = "SELECT Nick,User_Name,Email,User_Pass FROM users WHERE ID = ? ";

        PreparedStatement statm = connection.prepareStatement(query);
        statm.setInt(1, id );
        ResultSet result = statm.executeQuery();

        User user = null;
        if (result.next()) {
            user = new User();
            user.setId( id );
            user.setNick( result.getString("Nick") );
            user.setUserName( result.getString("User_Name") );
            user.setEmail( result.getString("Email") );
            user.setPass( result.getString("User_Pass") );
        }
        connection.close();
        return user;
    }

    @Override
    public User getUser(String email) throws SQLException {
        Connection connection = DbManager.getConnection();
        final String query = "SELECT ID,Nick,User_Name,User_Pass FROM users WHERE Email = ? ";

        PreparedStatement statm = connection.prepareStatement(query);
        statm.setString(1, email );
        ResultSet result = statm.executeQuery();

        User user = null;
        if (result.next()) {
            user = new User();
            user.setId( result.getInt("id") );
            user.setNick( result.getString("Nick") );
            user.setUserName( result.getString("User_Name") );
            user.setEmail( email );
            user.setPass( result.getString("User_Pass") );
        }
        connection.close();
        return user;
    }

    @Override
    public boolean editUser(User user) throws SQLException {
        Connection connection = DbManager.getConnection();
        // Assures that no null or empty record will be passed for NOT NULL fields
        if( !user.checkFileds() ) return false;

        final String query = "UPDATE users SET Nick = ?,User_Name = ?,Email = ?, User_Pass = ? " +
                    " WHERE ID = ? ";

        PreparedStatement statm = connection.prepareStatement(query);
        statm.setString(1, user.getNick() );
        statm.setString(2, user.getUserName() );
        statm.setString(3, user.getEmail() );
        statm.setString(4, user.getPass() );
        statm.setInt(5, user.getId() );

        boolean state = statm.execute();
        connection.close();
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
