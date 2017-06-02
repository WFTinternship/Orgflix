package daotest;

import am.aca.util.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by David on 5/30/2017
 */
public class TestHelper {
    public static void emptyTable(String[] tables, Connection connection) throws SQLException {
        for(String table : tables) {
            final String query = "DELETE FROM  " + table;
            connection.prepareStatement(query).executeUpdate();
        }
    }
}
