package am.aca.dao.impljdbc;

import am.aca.dao.DaoException;
import am.aca.dao.FilmDao;
import am.aca.entity.*;
import am.aca.util.ConnType;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 5/28/2017
 */
public class FilmDaoJdbc extends DaoJdbc implements FilmDao {

    public FilmDaoJdbc() {
        super();
    }

    public FilmDaoJdbc(ConnType connType) {
        super(connType);
    }

    @Override
    public boolean addFilm(Film film) {
        boolean state = false;

        if (film.getTitle() == null)
            return false;

        final String query = "INSERT INTO films (Title, Prod_Year, HasOscar, Rate_1star, Rate_2star, Rate_3star, Rate_4star, Rate_5star, director) " +
                " VALUES( ? , ? , ?, ?, ?, ?, ?, ?, ?) ";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, film.getTitle());
            statement.setInt(2, film.getProdYear());
            statement.setBoolean(3, film.isHasOscar());
            statement.setInt(4, film.getRate_1star());
            statement.setInt(5, film.getRate_2star());
            statement.setInt(6, film.getRate_3star());
            statement.setInt(7, film.getRate_4star());
            statement.setInt(8, film.getRate_5star());
            statement.setString(9, film.getDirector());

            if (statement.executeUpdate() == 1) {
                resultSet = statement.getGeneratedKeys();
                if (resultSet.next())
                    film.setId(resultSet.getInt(1));

                final String updateImageQuery = "UPDATE films SET image_ref = ? WHERE id = ?";
                PreparedStatement updateImageStatement = connection.prepareStatement(updateImageQuery);
                updateImageStatement.setString(1, "..\\..\\..\\..\\..\\..\\webapp\\images\\" + Integer.toString(film.getId()) + ".jpg");
                updateImageStatement.setInt(2, film.getId());
                updateImageStatement.execute();

                connection.commit();

                state = true; // film insert statement is successful
            }
        } catch (SQLException e) {
            throw new DaoException(e.toString());
        } finally {
            dataSource.closeConnections(new Object[]{resultSet, statement, connection});
        }
        return state;
    }


    @Override
    public boolean editFilm(Film film) {
        boolean state = false;

        final String query = "UPDATE films SET Title = ?,Prod_Year = ?,HasOscar = ?,Rate_1star = ? " +
                ",Rate_2star = ?, Rate_3star = ?,Rate_4star = ?,Rate_5star = ?, director = ? " +
                " WHERE id = ? ";

        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = dataSource.getConnection();
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
            statement.setString(9, film.getDirector());
            statement.setInt(10, film.getId());

            if (statement.executeUpdate() == 1) {
                final String deleteQuery = "DELETE FROM film_to_cast WHERE  Film_ID = ? ";
                statement = connection.prepareStatement(deleteQuery);
                statement.setInt(1, film.getId());
                statement.executeUpdate();

                final String nextQuery = "INSERT INTO film_to_cast(ACTOR_ID,Film_ID) VALUES (? , ? ) ";
                for (Cast cast : film.getCasts()) {
                    statement = connection.prepareStatement(nextQuery);
                    statement.setInt(1, cast.getId());
                    statement.setInt(2, film.getId());
                    statement.executeUpdate();
                }
                connection.commit();
                state = true; // film update statement is successful
            }
        } catch (SQLException e) {
            dataSource.connectionRollback(connection);
            throw new DaoException(e.toString());
        } finally {
            dataSource.closeConnections(new Object[]{statement, connection});
        }

        return state;
    }

    @Override
    public Film getFilmById(int id) {
        Film film = null;
        List<Cast> castList;

        final String getQuery = "SELECT * FROM films WHERE ID = ? LIMIT 1";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(getQuery);
            statement.setInt(1, id);

            resultSet = statement.executeQuery();

            if (!resultSet.next()) return null;

            else {
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
                film.setDirector(resultSet.getString("director"));
                film.setImage(resultSet.getString("image_ref"));
            }

            // filling cast list
            final String castQuery = "SELECT * FROM film_to_cast" +
                    " LEFT JOIN casts ON film_to_cast.Actor_ID = casts.ID " +
                    " WHERE Film_ID = ?";

            statement = connection.prepareStatement(castQuery);
            statement.setInt(1, id);

            resultSet = statement.executeQuery();
            castList = new ArrayList<>();
            while (resultSet.next()) {
                Cast cast = new Cast();
                cast.setId(resultSet.getInt("Actor_ID"));
                cast.setName(resultSet.getString("Actor_Name"));
                cast.setHasOscar(resultSet.getBoolean("HasOscar"));
                castList.add(cast);
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
            throw new DaoException(e.toString());
        } finally {
            dataSource.closeConnections(new Object[]{resultSet, statement, connection});
        }
        film.setCasts(castList);

        return film;
    }

    @Override
    public List<Film> getFilmsByCast(int actorId) {
        List<Film> films = new ArrayList<>();

        final String filmQuery = "SELECT * FROM film_to_cast" +
                " LEFT JOIN casts ON film_to_cast.Actor_ID = casts.ID " +
                " WHERE Actor_ID = ?";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(filmQuery);
            statement.setInt(1, actorId);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                films.add(getFilmById(resultSet.getInt("Film_ID")));
            }
        } catch (SQLException e) {
            throw new DaoException(e.toString());
        } finally {
            dataSource.closeConnections(new Object[]{resultSet, statement, connection});
        }

        return films;
    }

    @Override
    public List<Film> getFilmsByCast(Cast cast) {
        return getFilmsByCast(cast.getId());
    }

    @Override
    public List<Film> getFilmsList(int startIndex) {
        List<Film> films = new ArrayList<>();

        final String filmQuery = "SELECT * FROM films LIMIT ? , 12 ";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(filmQuery);
            statement.setInt(1, startIndex);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                films.add(getFilmById(resultSet.getInt("ID")));
            }
        } catch (SQLException e) {
            throw new DaoException(e.toString());
        } finally {
            dataSource.closeConnections(new Object[]{resultSet, statement, connection});
        }

        return films;
    }

    @Override
    public List<Film> getFilmsByGenre(Genre genre) {
        List<Film> films = new ArrayList<>();

        final String filmQuery = "SELECT * FROM genre_to_film" +
                " LEFT JOIN genre ON genre_to_film.Genre_ID = genre.ID " +
                " WHERE Genre_ID = ?";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(filmQuery);
            statement.setInt(1, genre.getValue());

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                films.add(getFilmById(resultSet.getInt("Film_ID")));
            }
        } catch (SQLException e) {
            throw new DaoException(e.toString());
        } finally {
            dataSource.closeConnections(new Object[]{resultSet, statement, connection});
        }

        return films;
    }

    @Override
    public boolean rateFilm(int filmId, int starType) {
        boolean state;

        final String query = "UPDATE films set Rate_" + starType + "star = Rate_" + starType +
                "star + 1 WHERE ID = ? ";

        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, filmId);

            state = (statement.executeUpdate() == 1);

        } catch (SQLException e) {
            throw new DaoException(e.toString());
        } finally {
            dataSource.closeConnections(new Object[]{statement, connection});
        }
        return state;
    }

    @Override
    public boolean addGenreToFilm(Genre genre, int filmId) {
        boolean state;

        final String query = "INSERT INTO genre_to_film(Genre_ID,Film_ID) VALUES (? , ? ) ";

        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, genre.getValue());
            statement.setInt(2, filmId);

            state = (statement.executeUpdate() == 1);
        } catch (SQLException e) {
            throw new DaoException(e.toString());
        } finally {
            dataSource.closeConnections(new Object[]{statement, connection});
        }
        return state;
    }

    @Override
    public boolean addCastToFilm(Cast cast, int filmId) {
        boolean result;

        final String query = "INSERT INTO film_to_cast(Actor_ID,Film_ID) VALUES (? , ? ) ";

        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, cast.getId());
            statement.setInt(2, filmId);

            result = (statement.executeUpdate() == 1);
        } catch (SQLException e) {
            throw new DaoException(e.toString());
        } finally {
            dataSource.closeConnections(new Object[]{statement, connection});
        }
        return result;
    }

    @Override
    public double getRating(int filmId) {
        int ratingSum = 0;
        int ratingCount = 0;
        double result;

        final String ratingQuery = "SELECT * FROM films WHERE ID = ?";
        try {
            PreparedStatement ratingSumStatement = dataSource.getConnection().prepareStatement(ratingQuery);
            ratingSumStatement.setInt(1, filmId);
            ResultSet ratingSumSet = ratingSumStatement.executeQuery();

            if (ratingSumSet.next()) {

                for (int i = 1; i <= 5; i++) {
                    ratingSum += ratingSumSet.getInt("Rate_" + i + "star") * i;
                    ratingCount += ratingSumSet.getInt("Rate_" + i + "star");
                }
            }
        } catch (SQLException e) {
            throw new DaoException(e.toString());
        }

        if (ratingSum == 0)
            return 0;
        return (double) ratingSum/ratingCount;
    }

    @Override
    public int totalNumberOfFilms() {
        int totalNumber = 0;

        final String query = "SELECT count(ID) AS total FROM films ";

        Connection connection = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            resultSet = connection.prepareStatement(query).executeQuery();

            if (resultSet.next()) {
                totalNumber = resultSet.getInt("total");
            }
        } catch (SQLException e) {
            throw new DaoException(e.toString());
        } finally {
            dataSource.closeConnections(new Object[]{resultSet, connection});
        }

        return totalNumber;
    }
}
