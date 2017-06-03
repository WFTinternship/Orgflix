package daotest;

import am.aca.util.*;
import java.sql.SQLException;

/**
 * Created by David on 5/30/2017
 */
public class TestHelper {
    public static void emptyTable(String[] tables) throws SQLException {
        for(String table : tables) {
            final String query = "DELETE FROM  " + table;
            DbManager.getConnection().prepareStatement(query).executeUpdate();
        }
    }
}
