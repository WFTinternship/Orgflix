package daotest;

import am.aca.util.*;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by David on 5/30/2017
 */
public class TestHelper {
    public static void emptyTable(String[] tables) throws SQLException{
        for(String table : tables) {
            final String query = "DELETE FROM  " + table;
            DbManager.getInstance().getConnection().prepareStatement(query).executeUpdate();
        }
    }
}
