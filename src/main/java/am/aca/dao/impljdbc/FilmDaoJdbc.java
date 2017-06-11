package am.aca.dao.impljdbc;

import am.aca.dao.DaoException;
import am.aca.dao.FilmDao;
import am.aca.entity.*;
import am.aca.util.DbManager;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 5/28/2017
 */
public class FilmDaoJdbc implements FilmDao {
    private static final Logger LOGGER = Logger.getLogger(FilmDao.class);

    @Override
    public boolean addFilm(Film film) {
        boolean state = false;

        if (film.getTitle() == null)
            return false;

        final String query = "INSERT INTO films (Title, Prod_Year, HasOscar, Rate_1star, Rate_2star, Rate_3star, Rate_4star, Rate_5star) " +
                " VALUES( ? , ? , ?, ?, ?, ?, ?, ? ) ";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DbManager.getInstance().getConnection();
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, film.getTitle());
            statement.setInt(2, film.getProdYear());
            statement.setBoolean(3, film.isHasOscar());
            statement.setInt(4, film.getRate_1star());
            statement.setInt(5, film.getRate_2star());
            statement.setInt(6, film.getRate_3star());
            statement.setInt(7, film.getRate_4star());
            statement.setInt(8, film.getRate_5star());

            if (statement.executeUpdate() == 1) {
                resultSet = statement.getGeneratedKeys();
                if (resultSet.next())
                    film.setId(resultSet.getInt(1));

                state = true; // film insert statement is successful
            }
        } catch (SQLException e) {
            LOGGER.warn(e.toString());
            throw new DaoException(e.toString());
        } finally {
            DbManager.closeConnections(new Object[]{resultSet, statement, connection});
        }
        return state;
    }

    @Override
    public boolean editFilm(Film film) {
        boolean state = false;

        final String query = "UPDATE films SET Title = ?,Prod_Year = ?,HasOscar = ?,Rate_1star = ? " +
                ",Rate_2star = ?, Rate_3star = ?,Rate_4star = ?,Rate_5star = ? " +
                " WHERE id = ? ";

        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = DbManager.getInstance().getConnection();
            connection.setAutoCommit(false);

            statement = connection.prepareStatement(query);
            statement.setString(1, film.getTitle());
            statement.setInt(2, film.getProdYear());
            statement.setBoolean(3, film.isHasOscar());
            statement.setInt(4, film.getRate_1star());
            statement.setInt(5, film.getRate_2star());
            statement.setInt(6, film.getRate_3star());
            statement.setInt(7, film.getRate_4star());
            statement.setInt(8, film.getRate_5star());
            statement.setInt(9, film.getId());

            if (statement.executeUpdate() == 1) {
                final String deleteQuery = "DELETE FROM film_to_director WHERE  Film_ID = ? ";
                statement = connection.prepareStatement(deleteQuery);
                statement.setInt(1, film.getId());
                statement.executeUpdate();

                final String nextQuery = "INSERT INTO film_to_director(Director_ID,Film_ID) VALUES (? , ? ) ";
                for (Director director : film.getDirectors()) {
                    statement = connection.prepareStatement(nextQuery);
                    statement.setInt(1, director.getId());
                    statement.setInt(2, film.getId());
                    statement.executeUpdate();
                }
                connection.commit();
                state = true; // film update statement is successful
            }
        } catch (SQLException e) {
            LOGGER.warn(e.toString());
            DbManager.connectionRollback(connection);
            throw new DaoException(e.toString());
        } finally {
            DbManager.closeConnections(new Object[]{statement, connection});
        }

        return state;
    }

    @Override
    public Film getFilmById(int id) {
        Film film = null;
        List<Director> directorList = null;

        final String getQuery = "SELECT * FROM films WHERE ID = ? LIMIT 1";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DbManager.getInstance().getConnection();
            statement = connection.prepareStatement(getQuery);
            statement.setInt(1, id);

            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                film = new Film();
                film.setId(id);
                film.setTitle(resultSet.getString("Title"));
                film.setHasOscar(resultSet.getBoolean("HasOscar"));
                film.setProdYear(resultSet.getInt("Prod_Year"));
                film.setImage(resultSet.getString("image_ref"));
                film.setRate_1star(resultSet.getInt("Rate_1star"));
                film.setRate_2star(resultSet.getInt("Rate_2star"));
                film.setRate_3star(resultSet.getInt("Rate_3star"));
                film.setRate_4star(resultSet.getInt("Rate_4star"));
                film.setRate_5star(resultSet.getInt("Rate_5star"));
            }

            // filling directors list
            final String directorQuery = "SELECT * FROM film_to_director" +
                    " LEFT JOIN directors ON film_to_director.Director_ID = directors.ID " +
                    " WHERE Film_ID = ?";

            statement = connection.prepareStatement(directorQuery);
            statement.setInt(1, id);

            resultSet = statement.executeQuery();
            directorList = new ArrayList<>();
            while (resultSet.next()) {
                Director director = new Director();
                director.setId(resultSet.getInt("Director_ID"));
                director.setName(resultSet.getString("Director_Name"));
                director.setHasOscar(resultSet.getBoolean("HasOscar"));
                directorList.add(director);
            }

            // filling genres list
            final String genreQuery = "SELECT * FROM genre_to_film" +
                    " LEFT JOIN genre ON genre_to_film.Genre_ID = genre.ID " +
                    " WHERE Film_ID = ?";

            statement = connection.prepareStatement(genreQuery);
            statement.setInt(1, id);

            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                film.addGeners(Genre.valueOf(resultSet.getString("genre")));
            }
        } catch (SQLException e) {
            LOGGER.warn(e.toString());
            throw new DaoException(e.toString());
        } finally {
            DbManager.closeConnections(new Object[]{resultSet, statement, connection});
        }
        film.setDirectors(directorList);

        return film;
    }

    @Override
    public List<Film> getFilmsByDirector(int directorId) {
        List<Film> films = new ArrayList<>();

        final String filmQuery = "SELECT * FROM film_to_director" +
                " LEFT JOIN directors ON film_to_director.Director_ID = directors.ID " +
                " WHERE Director_ID = ?";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DbManager.getInstance().getConnection();
            statement = connection.prepareStatement(filmQuery);
            statement.setInt(1, directorId);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                films.add(getFilmById(resultSet.getInt("ID")));
            }
        } catch (SQLException e) {
            LOGGER.warn(e.toString());
            throw new DaoException(e.toString());
        } finally {
            DbManager.closeConnections(new Object[]{resultSet, statement, connection});
        }

        return films;
    }

    @Override
    public List<Film> getFilmsList(int startIndex) {
        List<Film> films = new ArrayList<>();

        final String filmQuery = "SELECT * FROM films LIMIT ? , 12 ";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DbManager.getInstance().getConnection();
            statement = connection.prepareStatement(filmQuery);
            statement.setInt(1, startIndex);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                films.add(getFilmById(resultSet.getInt("ID")));
            }
        } catch (SQLException e) {
            LOGGER.warn(e.toString());
            throw new DaoException(e.toString());
        } finally {
            DbManager.closeConnections(new Object[]{resultSet, statement, connection});
        }

        return films;
    }

    @Override
    public List<Film> getFilmsByDirector(Director director) {
        return getFilmsByDirector(director.getId());
    }

    @Override
    public boolean rateFilm(int filmId, int starType) {
        boolean state = false;

        final String query = "UPDATE films set Rate_" + starType + "star = Rate_" + starType +
                "star + 1 WHERE ID = ? ";

        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = DbManager.getInstance().getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, filmId);

            state = (statement.executeUpdate()==1);

        } catch (SQLException e) {
            LOGGER.warn(e.toString());
            throw new DaoException(e.toString());
        } finally {
            DbManager.closeConnections(new Object[]{statement, connection});
        }
        return state;
    }

    @Override
    public boolean addGenreToFilm(Genre genre, int filmId) {
        boolean state = false;

        final String query = "INSERT INTO genre_to_film(Genre_ID,Film_ID) VALUES (? , ? ) ";

        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = DbManager.getInstance().getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, genre.ordinal());
            statement.setInt(2, filmId);

            state = ( statement.executeUpdate()==1 );
        } catch (SQLException e) {
            LOGGER.warn(e.toString());
            throw new DaoException(e.toString());
        } finally {
            DbManager.closeConnections(new Object[]{statement, connection});
        }
        return state;
    }

    @Override
    public boolean addGenreToFilm(Genre genre, Film film) {
        return addGenreToFilm(genre, film.getId());
    }
}
