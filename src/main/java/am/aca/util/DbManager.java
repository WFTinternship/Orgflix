package am.aca.util;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by David on 6/2/2017.
 */
public final class DbManager {

    private static final BasicDataSource dataSource = new BasicDataSource();

    static {
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/project");
        dataSource.setUsername("root");
        dataSource.setPassword("");
    }

    private DbManager() {
        //
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
