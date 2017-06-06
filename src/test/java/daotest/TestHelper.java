package daotest;

import am.aca.dao.DaoException;
import am.aca.util.*;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by David on 5/30/2017
 */
public class TestHelper {
    private static final Logger LOGGER = Logger.getLogger( TestHelper.class );
    public static void emptyTable(String[] tables){
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            for (String table : tables) {
                final String query = "DELETE FROM  " + table;
                connection = DbManager.getInstance().getConnection();
                statement =connection.prepareStatement(query);
                statement.executeUpdate();
            }
        }catch (Exception e){
            LOGGER.warn(e.toString());
            throw new DaoException(e.toString());
        } finally {
            DbManager.closeConnections(new Object[]{statement,connection});
        }
    }
}
