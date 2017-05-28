package am.aca.DAO;

import java.sql.*;

/**
 * Created by David on 5/27/2017.
 */
public class DaoJdbc {
    private static final String driverClass = "com.mysql.jdbc.Driver";
    private static final String urlJDBC = "jdbc:mysql://localhost:3306/project";
    private static Connection connection = null;

    public static Connection getConnection(String user, String pass){
        if( connection == null) {
            try {
                Class.forName(driverClass);
                connection = DriverManager.getConnection(urlJDBC, user, pass);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return connection;
    }
}
