package am.aca.util;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Created by David on 6/2/2017.
 */
public class DbManager {
    public Connection getPooledConnection(){
        return getConnection();
    }

    public Connection getConnection(){
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }
}
