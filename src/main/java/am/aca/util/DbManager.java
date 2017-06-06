package am.aca.util;

import org.apache.commons.dbcp2.BasicDataSource;
import com.mchange.v2.c3p0.*;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by David on 6/2/2017.
 */
public class DbManager {
    private static DbManager datasource;
    private ComboPooledDataSource cpds;

    private DbManager() throws PropertyVetoException {
        cpds = new ComboPooledDataSource();
        cpds.setDriverClass("com.mysql.jdbc.Driver"); //loads the jdbc driver
        cpds.setJdbcUrl("jdbc:mysql://localhost:3306/project");
        cpds.setUser("root");
        cpds.setPassword("");

        cpds.setInitialPoolSize(50);
        cpds.setMinPoolSize(50);
        cpds.setAcquireIncrement(3);
        cpds.setMaxPoolSize(100);
        cpds.setMaxStatements(100);
    }

    public static DbManager getInstance() throws IOException, SQLException, PropertyVetoException {
        if (datasource == null) {
            datasource = new DbManager();
            return datasource;
        } else {
            return datasource;
        }
    }

    public Connection getConnection() throws SQLException {
        return this.cpds.getConnection();
    }
/*
    private static final BasicDataSource dataSource = new BasicDataSource();

    static {
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/project");
        dataSource.setUsername("root");
        dataSource.setPassword("");
        dataSource.setInitialSize(50);
        dataSource.setTestWhileIdle(true);

    }

    private DbManager() {
        //
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
   */
}
