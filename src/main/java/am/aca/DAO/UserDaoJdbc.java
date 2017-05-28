package am.aca.DAO;

import am.aca.main.*;

import java.sql.*;

/**
 * Created by David on 5/27/2017.
 */
public class UserDaoJdbc implements UserDao {

    private Connection connection;

    public UserDaoJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean addUser(User user) throws SQLException {
        final String query = "INSERT INTO users (Nick,User_Name,Email,User_Pass) " +
                    " VALUES( ? , ? , ? , ? ) ";

        PreparedStatement statm = connection.prepareStatement(query);
        statm.setString(1, user.getNick() );
        statm.setString(2, user.getUserName() );
        statm.setString(3, user.getEmail() );
        statm.setString(4, user.getPass() );

        return statm.executeUpdate() == 1;
    }

    @Override
    public boolean editUser(User user) throws SQLException {
        final String query = "UPDATE User SET nick = ?,userName = ?,email = ? " +
                    " WHERE id = ? ";

        PreparedStatement statm = connection.prepareStatement(query);
        statm.setString(1, user.getNick() );
        statm.setString(2, user.getUserName() );
        statm.setString(3, user.getEmail() );
        statm.setInt(4, user.getId() );

        return statm.executeUpdate() == 1;
    }

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
}
