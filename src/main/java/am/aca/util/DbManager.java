package am.aca.util;

import am.aca.dao.DaoException;
import com.mchange.v2.c3p0.*;
import org.apache.log4j.Logger;


import java.sql.*;
import java.util.Properties;

/**
 * Created by David on 6/2/2017
 */
public class DbManager {
    private static final Logger LOGGER = Logger.getLogger(DbManager.class);

    private static DbManager datasource;
    private static DbManager testDatasource;
    private ComboPooledDataSource cpds;

    private Properties properties = null;

    private DbManager(ConnType connType) {
        String base = "TestJdbcUrl";
        try {
            properties = new PropertyManager("connection.properties").getProperties();
            cpds = new ComboPooledDataSource();

            if (connType == ConnType.PRODUCTION) base = "JdbcUrl";

            cpds.setJdbcUrl(properties.getProperty(base));
            cpds.setDriverClass(properties.getProperty("DriverClass")); //loads the jdbc driver
            cpds.setUser(properties.getProperty("user"));
            cpds.setPassword(properties.getProperty("password"));

            cpds.setInitialPoolSize(Integer.valueOf(properties.getProperty("InitialPoolSize")));
            cpds.setMinPoolSize(Integer.valueOf(properties.getProperty("MinPoolSize")));
            cpds.setAcquireIncrement(Integer.valueOf(properties.getProperty("AcquireIncrement")));
            cpds.setMaxPoolSize(Integer.valueOf(properties.getProperty("MaxPoolSize")));
            cpds.setMaxStatements(Integer.valueOf(properties.getProperty("MaxStatements")));
        } catch (Exception e) {
            LOGGER.warn(e.toString());
        }
    }

    public static DbManager getInstance() {
        if (datasource == null) {
            datasource = new DbManager(ConnType.PRODUCTION);
            return datasource;
        } else {
            return datasource;
        }
    }

    public static DbManager getInstanceTest() {
        if (testDatasource == null) {
            testDatasource = new DbManager(ConnType.TEST);
            return testDatasource;
        } else {
            return testDatasource;
        }
    }



    public enum ConnectionType {
        POOL, BASIC;
    }

    public Connection getConnection() throws SQLException {
        return getConnection(ConnectionType.POOL);
    }

    public Connection getConnection(ConnectionType type) throws SQLException {
        Connection connection;
        if (type == ConnectionType.POOL) {
            connection = this.cpds.getConnection();
            LOGGER.info("Connection opened: Number of busy connections: " + this.cpds.getNumBusyConnectionsAllUsers());
            return connection;

        } else {
            connection = null;
            try {
                Class.forName(properties.getProperty("DriverClass"));
                connection = DriverManager.getConnection(properties.getProperty("JdbcUrl"));
                LOGGER.info("connection opened");
            } catch (Exception e) {
                LOGGER.warn(e.toString());
            }
            return connection;
        }
    }

    public void closeConnections(Object[] resources) {
        try {
            for (Object resource : resources) {
                if (resource != null) {
                    if (resource instanceof PreparedStatement) {
                        PreparedStatement statement = (PreparedStatement) resource;
                        statement.close();
                    } else if (resource instanceof Statement) {
                        Statement statement = (Statement) resource;
                        statement.close();
                    } else if (resource instanceof ResultSet) {
                        ResultSet resultSet = (ResultSet) resource;
                        resultSet.close();
                    } else if (resource instanceof Connection) {
                        Connection connection = (Connection) resource;
                        connection.close();
                        LOGGER.info("Connection closed: Number of busy connections: " + this.cpds.getNumBusyConnectionsAllUsers());
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.warn(e.toString());
            throw new DaoException(e.toString());
        }
    }

    public static void emptyTestTables(String[] tables) {
        Connection connection = null;
        PreparedStatement statement = null;
        for (String table : tables) {
            try {
                final String query = "DELETE FROM  " + table;
                connection = datasource.getConnection();
                statement = connection.prepareStatement(query);
                statement.executeUpdate();
            } catch (Exception e) {
                LOGGER.warn(e.toString());
                throw new DaoException(e.toString());
            } finally {
                datasource.closeConnections(new Object[]{statement, connection});
            }
        }
    }

    public static void connectionRollback(Connection connection) {
        try {
            connection.rollback();
            LOGGER.info("Transaction rolled back successfully");
        } catch (SQLException e) {
            LOGGER.warn(e.toString());
        }
    }

    public ComboPooledDataSource getDataSource() {
        return this.cpds;
    }
}
