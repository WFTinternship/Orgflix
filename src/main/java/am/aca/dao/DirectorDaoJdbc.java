package am.aca.dao;

import am.aca.entity.Director;
import am.aca.util.DbManager;
import org.apache.log4j.Logger;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 5/28/2017.
 */
public class DirectorDaoJdbc implements DirectorDao {
    private static final Logger LOGGER = Logger.getLogger( UserDao.class.getName() );

    public Director addDirector(String name, boolean hasOscar){

        final String query = "INSERT INTO directors (Director_Name,HasOscar) VALUES( ?, ? ) ";
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        int id = -1;
        try {
            connection = DbManager.getInstance().getConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1, name);
            statement.setBoolean(2, hasOscar);
            statement.executeUpdate();

//        ResultSet generatedKeys = statm.getGeneratedKeys();
//        int id = generatedKeys.getInt(1);

            if (statement.executeUpdate() == 1) {
                String selQuery = "SELECT ID FROM directors ORDER BY ID DESC LIMIT 1;";
                statement = connection.prepareStatement(selQuery);
                resultSet = statement.executeQuery();
                if (resultSet.next()) id = resultSet.getInt("ID");
            }

        } catch(SQLException e){
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        } finally {
            try{
                if(statement != null)
                    statement.close();
                if(resultSet != null)
                    resultSet.close();
                if(connection != null)
                    connection.close();
            }catch (SQLException e){
                LOGGER.warn(e.getMessage());
                throw new DaoException(e.getMessage());
            }
        }

        Director director = new Director();
        director.setName(name);
        director.setHasOscar(hasOscar);
        director.setId(id);

        return director;
    }

    @Override
    public Director addDirector(String director) {
        return addDirector(director, false);
    }

    @Override
    public List<Director> listDirectors(){
        List<Director> listDirector = new ArrayList<>();
        Connection connection = null;
        ResultSet resultSet = null;
        try {
            connection = DbManager.getInstance().getConnection();
            resultSet = connection.createStatement().executeQuery("SELECT * from directors");

            while (resultSet.next()) {
                Director director = new Director();
                director.setId(resultSet.getInt("ID"));
                director.setName(resultSet.getString("Director_Name"));
                director.setHasOscar(resultSet.getBoolean("HasOscar"));
                listDirector.add(director);
            }
        } catch(SQLException e){
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        } finally {
            try{
                if(resultSet != null)
                    resultSet.close();
                if(connection != null)
                    connection.close();
            }catch (SQLException e){
                LOGGER.warn(e.getMessage());
                throw new DaoException(e.getMessage());
            }
        }
        return listDirector;
    }

    @Override
    public int[] listFilmsIdByDirector(int directorId){

        int[] filmsByDirector = null;

        final String query = "SELECT directors.ID AS Id, film_to_director.Film_ID AS film " +
                " FROM directors JOIN film_to_director " +
                " ON directors.ID = film_to_director.Director_ID " +
                " WHERE directors.ID = ? " +
                " ORDER BY film_to_director.Film_ID DESC ";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DbManager.getInstance().getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, directorId);

            resultSet = statement.executeQuery();
            int num = resultSet.getFetchSize();
            filmsByDirector = new int[num];
            int i = 0;
            while (resultSet.next()) {
                filmsByDirector[i] = resultSet.getInt("Film");
                ++i;
            }
        } catch(SQLException e){
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        } finally {
            try{
                if(statement != null)
                    statement.close();
                if(resultSet != null)
                    resultSet.close();
                if(connection != null)
                    connection.close();
            }catch (SQLException e){
                LOGGER.warn(e.getMessage());
                throw new DaoException(e.getMessage());
            }
        }
        return filmsByDirector;
    }

    @Override
    public boolean editDirector(Director director) {
        boolean state = false;
        Connection connection = null;
        PreparedStatement statement = null;
        final String query = "UPDATE directors SET Director_Name = ? and HasOscar = ? WHERE ID = ?";

        try {
            connection = DbManager.getInstance().getConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1,director.getName());
            statement.setBoolean(2,director.isHasOscar());
            statement.setInt(3,director.getId());
            state = (statement.executeUpdate() == 1);
        } catch(SQLException e){
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        } finally {
            try {
                if (statement != null)
                    statement.close();
                if (connection != null)
                    connection.close();
            } catch (SQLException e) {
                LOGGER.warn(e.getMessage());
                throw new DaoException(e.getMessage());
            }
        }
        return state;
    }
}
