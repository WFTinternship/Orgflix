package am.aca.util;

import am.aca.dao.UserDao;
import org.apache.commons.dbcp2.BasicDataSource;
import com.mchange.v2.c3p0.*;
import org.apache.log4j.Logger;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Created by David on 6/2/2017.
 */
public class DbManager {
    private static final Logger LOGGER = Logger.getLogger( UserDao.class.getName() );

    private static DbManager datasource;
    private ComboPooledDataSource cpds;
    private PropertyManager propManager = new PropertyManager("connection.properties");

    private DbManager() {
        try {
            cpds = new ComboPooledDataSource();
            cpds.setDriverClass(propManager.getProperties().getProperty("DriverClass")); //loads the jdbc driver
            cpds.setJdbcUrl(propManager.getProperties().getProperty("JdbcUrl"));
            cpds.setUser(propManager.getProperties().getProperty("user"));
            cpds.setPassword(propManager.getProperties().getProperty("password"));

            cpds.setInitialPoolSize(Integer.valueOf(propManager.getProperties().getProperty("InitialPoolSize")));
            cpds.setMinPoolSize(Integer.valueOf(propManager.getProperties().getProperty("MinPoolSize")));
            cpds.setAcquireIncrement(Integer.valueOf(propManager.getProperties().getProperty("AcquireIncrement")));
            cpds.setMaxPoolSize(Integer.valueOf(propManager.getProperties().getProperty("MaxPoolSize")));
            cpds.setMaxStatements(Integer.valueOf(propManager.getProperties().getProperty("MaxStatements")));
        }catch(Exception e){
            LOGGER.warn(e.getMessage());
        }
    }

    public static DbManager getInstance() {
        if (datasource == null) {
            datasource = new DbManager();
            return datasource;
        } else {
            return datasource;
        }
    }

    public enum ConnectionType {
        POOL, BASIC
    }

    public Connection getConnection() throws SQLException{
        return getConnection(ConnectionType.POOL);
    }

    public Connection getConnection(ConnectionType type) throws SQLException {
        if(type==ConnectionType.POOL)
            return this.cpds.getConnection();
        else {
            Connection connection = null;
           try {
                Class.forName(propManager.getProperties().getProperty("DriverClass"));
                connection = DriverManager.getConnection(propManager.getProperties().getProperty("JdbcUrl"));
           } catch (Exception e) {
               LOGGER.warn(e.getMessage());
           }
           return connection;
        }
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
