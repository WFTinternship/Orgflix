package am.aca.DAO;

import am.aca.main.*;

import java.sql.*;

/**
 * Created by David on 5/28/2017.
 */
public class DirectorDaoJdbc implements DirectorDao {

    private Connection connection;

    public DirectorDaoJdbc(Connection connection) {
        this.connection = connection;
    }

    public int addDirector(String name, boolean hasOscar) throws SQLException{
        final String query = "INSERT INTO directors (Director_Name,HasOscar) VALUES( ?, ? ) ";

        PreparedStatement statm = connection.prepareStatement(query);
        statm.setString(1, name );
        statm.setBoolean(2, hasOscar );

        int id = -1;
        if( statm.executeUpdate() == 1 ){
            final String selectQuery = "SELECT ID FROM directors WHERE Director_Name = ? AND HasOscar = ? LIMIT 1";

            PreparedStatement selectStatm = connection.prepareStatement(selectQuery);
            selectStatm.setString(1, name);
            selectStatm.setBoolean(2, hasOscar);

            ResultSet result = selectStatm.executeQuery();
            if( result.next() ) id = result.getInt("ID");
        }
        return id;
    }

    @Override
    public String listDirectors() throws SQLException {
        StringBuilder sb = new StringBuilder();
        ResultSet result = connection.createStatement().executeQuery("SELECT ID, Director_Name from directors");
        while (result.next())
        {
            sb.append( result.getString("ID") );
            sb.append(". - ");
            sb.append( result.getString("Director_Name") );
            sb.append("\n");
        }

        return sb.toString();
    }

    @Override
    public boolean editDirector(int directorId) throws SQLException {
        return false;
    }

    @Override
    public boolean removeDirector(int directorId) throws SQLException {
        return false;
    }
}
