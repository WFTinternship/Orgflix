package am.aca.dao.impljdbc;

import am.aca.dao.*;
import am.aca.entity.Director;
import am.aca.entity.Film;
import am.aca.util.DbManager;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 5/28/2017
 */
public class DirectorDaoJdbc implements DirectorDao {
    private static final Logger LOGGER = Logger.getLogger(DirectorDao.class);

    public Director addDirector(String name, boolean hasOscar) {

        final String query = "INSERT INTO directors (Director_Name,HasOscar) VALUES( ?, ? ) ";
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        int id = -1;
        try {
            connection = DbManager.getInstance().getConnection();
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, name);
            statement.setBoolean(2, hasOscar);

            if (statement.executeUpdate() == 1) {
                resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) id = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            LOGGER.warn(e.toString());
            throw new DaoException(e.toString());
        } finally {
            DbManager.closeConnections(new Object[]{resultSet, statement, connection});
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
    public boolean addDirectorToFilm(Director director, Film film) {
        return addDirectorToFilm(director.getId(), film.getId());
    }

    @Override
    public boolean addDirectorToFilm(int directorId, int filmId) {
        final String query = "INSERT INTO film_to_director(Director_ID,Film_ID) VALUES (? , ? ) ";
        boolean state = false;
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = DbManager.getInstance().getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, directorId);
            statement.setInt(2, filmId);
            state = statement.execute();
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        } finally {
            DbManager.closeConnections(new Object[]{statement, connection});
        }
        return state;
    }

    @Override
    public List<Director> listDirectors() {
        List<Director> listDirector = new ArrayList<>();
        Connection connection = null;
        ResultSet resultSet = null;
        try {
            connection = DbManager.getInstance().getConnection();
            resultSet = connection.createStatement().executeQuery("SELECT * FROM directors");

            while (resultSet.next()) {
                Director director = new Director();
                director.setId(resultSet.getInt("ID"));
                director.setName(resultSet.getString("Director_Name"));
                director.setHasOscar(resultSet.getBoolean("HasOscar"));
                listDirector.add(director);
            }
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        } finally {
            DbManager.closeConnections(new Object[]{resultSet, connection});
        }
        return listDirector;
    }

    @Override
    public List<Integer> listFilmsIdByDirector(int directorId) {

        List<Integer> filmsByDirector = new ArrayList<>();

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

            while (resultSet.next()) {
                filmsByDirector.add(resultSet.getInt("Film"));
            }
        } catch (SQLException e) {
            LOGGER.warn(e.toString());
            throw new DaoException(e.toString());
        } finally {
            DbManager.closeConnections(new Object[]{resultSet, statement, connection});
        }
        return filmsByDirector;
    }

    @Override
    public boolean editDirector(Director director) {
        boolean state = false;
        Connection connection = null;
        PreparedStatement statement = null;
        final String query = "UPDATE directors SET Director_Name = ?, HasOscar = ? WHERE ID = ?";
        try {
            connection = DbManager.getInstance().getConnection();
            statement = connection.prepareStatement(query);
            statement.setString(1, director.getName());
            statement.setBoolean(2, director.isHasOscar());
            statement.setInt(3, director.getId());
            state = statement.executeUpdate() == 1;
        } catch (SQLException e) {
            LOGGER.warn(e.toString());
            throw new DaoException(e.toString());
        } finally {
            DbManager.closeConnections(new Object[]{statement, connection});
        }
        return state;
    }
}
