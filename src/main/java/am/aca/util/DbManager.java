package am.aca.util;

import am.aca.dao.DaoException;
import com.mchange.v2.c3p0.*;
import org.apache.log4j.Logger;


import java.sql.*;

/**
 * Created by David on 6/2/2017
 */
public class DbManager {
    private static final Logger LOGGER = Logger.getLogger( DbManager.class );

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
            LOGGER.warn(e.toString());
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
        if(type==ConnectionType.POOL) {
            LOGGER.info("connection opened");
            return this.cpds.getConnection();
          //  LOGGER.info(this.cpds.)
        } else {
            Connection connection = null;
           try {
                Class.forName(propManager.getProperties().getProperty("DriverClass"));
                connection = DriverManager.getConnection(propManager.getProperties().getProperty("JdbcUrl"));
                LOGGER.info("connection opened");
           } catch (Exception e) {
               LOGGER.warn(e.toString());
           }
           return connection;
        }
    }

    public static void closeConnections(Object[] resources){
        try {
            for (Object resource : resources) {
                if (resource instanceof PreparedStatement) {
                    PreparedStatement statement = (PreparedStatement) resource;
                    if (statement != null) {
                        statement.close();
                    }
                } else if(resource instanceof Statement){
                    Statement statement = (Statement) resource;
                    if (statement != null) {
                        statement.close();
                    }
                } else if(resource instanceof ResultSet) {
                    ResultSet resultSet = (ResultSet) resource;
                    if (resultSet != null) {
                        resultSet.close();
                    }
                } else if(resource instanceof Connection) {
                    Connection connection = (Connection) resource;
                    if (connection != null) {
                        connection.close();
                        LOGGER.info("Connection closed");
                    }
                }
            }
        }catch (SQLException e){
            LOGGER.warn(e.toString());
            throw new DaoException(e.toString());
        }
    }
    public static void connectionRollback(Connection connection){
        try{
            connection.rollback();
            LOGGER.info("Committed successfully");
        }catch(SQLException e){
            LOGGER.warn(e.toString());
        }
    }
}
