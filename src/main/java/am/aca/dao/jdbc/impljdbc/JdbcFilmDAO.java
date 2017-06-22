package am.aca.dao.jdbc.impljdbc;


import am.aca.dao.jdbc.BaseDAO;
import am.aca.dao.jdbc.FilmDAO;
import am.aca.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

/**
 * Created by David on 5/28/2017
 */
@Repository
public class JdbcFilmDAO extends BaseDAO implements FilmDAO {

    @Autowired
    public JdbcFilmDAO(DataSource dataSource) {
        super(JdbcFilmDAO.class);
        this.setDataSource(dataSource);
    }

    @Override
    public boolean addFilm(Film film) {
        if (film.getTitle() == null || film.getTitle().equals(""))
            return false;

        KeyHolder holder = new GeneratedKeyHolder();

        final String query = "INSERT INTO films (Title, Prod_Year, HasOscar, Rate_1star, Rate_2star, Rate_3star, Rate_4star, Rate_5star, director) " +
                " VALUES( ? , ? , ?, ?, ?, ?, ?, ?, ?) ";

        getJdbcTemplate().update(connection -> {
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, film.getTitle());
        statement.setInt(2, film.getProdYear());
        statement.setBoolean(3, film.isHasOscar());
        statement.setInt(4, film.getRate_1star());
        statement.setInt(5, film.getRate_2star());
        statement.setInt(6, film.getRate_3star());
        statement.setInt(7, film.getRate_4star());
        statement.setInt(8, film.getRate_5star());
        statement.setString(9, film.getDirector());
            return statement;
        }, holder);

        film.setId(holder.getKey().intValue());
        return true;
    }



    @Override
    public boolean editFilm(Film film) {
        if (film.getTitle() == null || film.getTitle().equals(""))
            return false;

        final String query = "UPDATE films SET Title = ?,Prod_Year = ?,HasOscar = ?,Rate_1star = ? " +
                ",Rate_2star = ?, Rate_3star = ?,Rate_4star = ?,Rate_5star = ?, director = ? " +
                " WHERE id = ? ";
        getJdbcTemplate().update(query, new Object[] {
                film.getTitle(), film.getProdYear(), film.isHasOscar(), film.getRate_1star(), film.getRate_2star(), film.getRate_3star(), film.getRate_4star(), film.getRate_5star(), film.getDirector(), film.getId()
        });
        return true;
    }


    @Override
    public Film getFilmById(int id) {
        final String getQuery = "SELECT * FROM films WHERE ID = ? LIMIT 1";
        final String countQuery = "SELECT COUNT(*) FROM films WHERE ID = ? LIMIT 1";
        if (getJdbcTemplate().queryForObject(countQuery, new Object[] { id },Integer.class) == 0)
            return null;
        return (Film) getJdbcTemplate().queryForObject(getQuery, new Object[]{
                id
        }, (resultSet, i) -> {
            Film film = new Film();
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
            return film;
        });
    }


    @Override
    public List getFilmsByCast(int actorId) {
        final String filmQuery = "SELECT films.ID FROM film_to_cast" +
                " LEFT JOIN films ON film_to_cast.Film_ID = films.ID " +
                " WHERE Actor_ID = ?";
        return getJdbcTemplate().queryForList(filmQuery, new Object[] { actorId }, Integer.class);
    }


    @Override
    public List getFilmsList(int startIndex) {
        final String filmQuery = "SELECT * FROM films LIMIT ? , 12 ";
        return getJdbcTemplate().queryForList(filmQuery, new Object[] {startIndex});
    }

    @Override
    public List getFilmsByCast(Cast cast) {
        return getFilmsByCast(cast.getId());
    }


    @Override
    public List getFilmsByGenre(Genre genre) {
        final String filmQuery = "SELECT films.ID FROM genre_to_film" +
                " LEFT JOIN films ON genre_to_film.Film_ID = films.ID " +
                " WHERE Genre_ID = ?";

        return getJdbcTemplate().queryForList(filmQuery, new Object[] {genre.getValue()}, Integer.class);
    }


    @Override
    public boolean rateFilm(int filmId, int starType) {
        if (starType > 5 || starType < 1)
            return false;
        final String query = "UPDATE films set Rate_" + starType + "star = Rate_" + starType +
                "star + 1 WHERE ID = ? ";
        getJdbcTemplate().update(query, new Object[] {filmId});
        return true;
    }

    @Override
    public boolean addCastToFilm(Cast cast, Film film) {
        return addCastToFilm(cast, film.getId());
    }

    @Override
    public boolean addGenreToFilm(Genre genre, int filmId) {
        final String query = "INSERT INTO genre_to_film(Genre_ID,Film_ID) VALUES (? , ? ) ";
        getJdbcTemplate().update(query, new Object[] {
                genre.getValue(), filmId
        });
        return true;
    }

    @Override
    public boolean addGenreToFilm(Genre genre, Film film) {
        return addGenreToFilm(genre, film.getId());
    }


    @Override
    public boolean addCastToFilm(Cast cast, int filmId) {
        final String query = "INSERT INTO film_to_cast(Actor_ID,Film_ID) VALUES (? , ? ) ";
        getJdbcTemplate().update(query, new Object[] {
                cast.getId(), filmId
        });
        return true;
    }


    @Override
    public double getRating(int filmId) {
        final String ratingQuery = "SELECT * FROM films WHERE ID = ?";
        int ratingSum = 0;
        int ratingCount = 0;
        SqlRowSet sqlRowSet = getJdbcTemplate().queryForRowSet(ratingQuery, new Object[]{filmId});
        while (sqlRowSet.next()) {
            for (int i = 1; i <= 5; i++) {
                ratingSum += sqlRowSet.getInt("Rate_" + i + "star") * i;
                ratingCount += sqlRowSet.getInt("Rate_" + i + "star");
            }

        }
        if (ratingSum == 0)
            return 0d;
        return (double) ratingSum/ratingCount;
    }

    @Override
    public double getRating(Film film) {
        return getRating(film.getId());
    }


    @Override
    public int totalNumberOfFilms() {
        final String query = "SELECT count(ID) AS total FROM films ";
        return getJdbcTemplate().queryForObject(query,Integer.class);
    }

    @Override
    public void resetRelationCasts(Film film) {
        final String query = "DELETE FROM film_to_cast WHERE Film_ID = ?";
        getJdbcTemplate().update(query, new Object[] { film.getId() });
    }

    @Override
    public void resetRelationGenres(Film film) {
        final String query = "DELETE FROM genre_to_film WHERE Film_ID = ?";
        getJdbcTemplate().update(query, new Object[] { film.getId() });
    }
}
