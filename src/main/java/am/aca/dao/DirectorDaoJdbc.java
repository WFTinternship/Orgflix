package am.aca.dao;

import am.aca.entity.Director;
import am.aca.util.DbManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 5/28/2017.
 */
public class DirectorDaoJdbc implements DirectorDao {

    public Director addDirector(String name, boolean hasOscar) throws SQLException{
        Connection connection = DbManager.getConnection();
        final String query = "INSERT INTO directors (Director_Name,HasOscar) VALUES( ?, ? ) ";

        PreparedStatement statm = connection.prepareStatement(query);
        statm.setString(1, name );
        statm.setBoolean(2, hasOscar );

        int id = -1;
        if( statm.executeUpdate() == 1 ){
            String selQuery = "SELECT ID FROM directors ORDER BY ID DESC LIMIT 1;";
            Statement selStatm = connection.createStatement();
            ResultSet resultSet = selStatm.executeQuery(selQuery);
            if( resultSet.next() ) id = resultSet.getInt("ID");
        }
        connection.close();

        Director director = new Director();
        director.setName(name);
        director.setHasOscar(hasOscar);
        director.setId(id);

        return director;
    }

    @Override
    public List<Director> listDirectors() throws SQLException {
        Connection connection = DbManager.getConnection();
        ResultSet result = connection.createStatement().executeQuery("SELECT * from directors");
        List<Director> listDirector = new ArrayList<>();
        while (result.next())
        {
            Director director = new Director();
            director.setId( result.getInt("ID") );
            director.setName( result.getString("Director_Name") );
            director.setHasOscar( result.getBoolean("HasOscar") );
            listDirector.add( director );
        }
        connection.close();
        return listDirector;
    }

    @Override
    public boolean editDirector(int directorId) throws SQLException {
        Connection connection = DbManager.getConnection();
        connection.close();
        return false;
    }
}
