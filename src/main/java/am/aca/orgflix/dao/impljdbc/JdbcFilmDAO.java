package am.aca.orgflix.dao.impljdbc;

import am.aca.orgflix.dao.BaseDAO;
import am.aca.orgflix.dao.DaoException;
import am.aca.orgflix.dao.FilmDAO;
import am.aca.orgflix.dao.impljdbc.mapper.FilmRowMapper;
import am.aca.orgflix.entity.Film;
import am.aca.orgflix.entity.Genre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

/**
 * DAO layer for Film entity
 */
@Component
public class JdbcFilmDAO extends BaseDAO implements FilmDAO {

    @Autowired
    public JdbcFilmDAO(DataSource dataSource) {
        super(JdbcFilmDAO.class);
        this.setDataSource(dataSource);
    }

    // CREATE

    /**
     * @see FilmDAO#addFilm(am.aca.orgflix.entity.Film)
     */
    @Override
    public boolean addFilm(Film film) {
        // ensure that all required fields are properly assigned
        if (!checkRequiredFields(film.getTitle()) || film.getProdYear() < 1900)
            throw new DaoException("Illegal argument");

        KeyHolder holder = new GeneratedKeyHolder();

        final String query = "INSERT INTO films (Title, Prod_Year, HasOscar, " +
                "Rate_1star, Rate_2star, Rate_3star, Rate_4star, Rate_5star, director) " +
                " VALUES( ? , ? , ?, ?, ?, ?, ?, ?, ?) ";

        int result = getJdbcTemplate().update(connection -> {
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
        // set new added film's id to its entity object
        film.setId(holder.getKey().intValue());

        return result == 1;
    }

    /**
     * @see FilmDAO#addGenreToFilm(am.aca.orgflix.entity.Genre, int)
     */
    @Override
    public boolean addGenreToFilm(Genre genre, int filmId) {
        final String query = "INSERT INTO genre_to_film(Genre_ID,Film_ID) VALUES (? , ? ) ";
        return getJdbcTemplate().update(query, genre.getValue(), filmId) == 1;
    }

    /**
     * @see FilmDAO#rateFilm(int, int)
     */
    @Override
    public boolean rateFilm(int filmId, int starType) {
        // the scale of rates is from one up to 5 inclusive
        if (starType > 5 || starType < 1)
            throw new DaoException("Illegal argument");

        final String sratType = "Rate_" + starType + "star";
        final String query = "UPDATE films set " + sratType + " = " + sratType + " + 1 WHERE ID = ? ";
        return getJdbcTemplate().update(query, filmId) == 1;
    }

    // READ

    /**
     * @see FilmDAO#getFilmById(int)
     */
    @Override
    public Film getFilmById(int id) {
        final String getQuery = "SELECT * FROM films WHERE ID = ? LIMIT 1";
        try {
            return getJdbcTemplate().queryForObject(getQuery, new Object[]{id}, new FilmRowMapper());
        } catch (EmptyResultDataAccessException e) {
            // no film with such id exists in the DB
            return null;
        }
    }


    /**
     * @see FilmDAO#getFilmsList(int)
     */
    @Override
    public List<Film> getFilmsList(int startIndex) {
        final String query = "SELECT * FROM films LIMIT ? , 12 ";
        return getJdbcTemplate().query(query,
                new Object[]{startIndex},
                new FilmRowMapper());
    }

    /**
     * @see FilmDAO#getFilmsByGenre(am.aca.orgflix.entity.Genre)
     */
    @Override
    public List<Film> getFilmsByGenre(Genre genre) {
        final String filmQuery = "SELECT films.ID, films.Title, films.Director, films.HasOscar, " +
                "films.image_ref, films.Prod_Year, films.Rate_1star, films.Rate_2star, " +
                "films.Rate_3star, films.Rate_4star, films.Rate_5star " +
                "FROM genre_to_film" +
                " LEFT JOIN films ON genre_to_film.Film_ID = films.ID " +
                " WHERE Genre_ID = ?";

        return getJdbcTemplate().query(filmQuery, new Object[]{genre.getValue()}, new FilmRowMapper());
    }

    /**
     * @see FilmDAO#getFilmsByCast(int)
     */
    @Override
    public List<Film> getFilmsByCast(int actorId) {
        final String query = "SELECT ID, Title, Prod_Year,HasOscar, image_ref, Director, " +
                " Rate_1star, Rate_2star, Rate_3star, Rate_4star, Rate_5star " +
                " FROM films INNER JOIN (" +
                "   SELECT film_to_cast.Film_ID AS film " +
                "   FROM casts JOIN film_to_cast " +
                "   ON casts.ID = film_to_cast.Actor_ID " +
                "   WHERE casts.ID = ? " +
                "   ORDER BY film_to_cast.Film_ID DESC " +
                ") AS sel_table " +
                " ON films.ID = sel_table.film";

        return getJdbcTemplate().query(query,
                new FilmRowMapper(),
                actorId);
    }

    /**
     * @see FilmDAO#getRating(int)
     */
    @Override
    public double getRating(int filmId) {
        final String ratingQuery = "SELECT * FROM films WHERE ID = ?";
        int ratingSum = 0;
        int ratingCount = 0;
        SqlRowSet sqlRowSet = getJdbcTemplate().queryForRowSet(ratingQuery, filmId);
        while (sqlRowSet.next()) {
            for (int i = 1; i <= 5; i++) {
                ratingSum += sqlRowSet.getInt("Rate_" + i + "star") * i;
                ratingCount += sqlRowSet.getInt("Rate_" + i + "star");
            }
        }
        // for the case when the film is not rated yet
        if (ratingCount == 0)
            return 0.0;
        // each scale of one to five has appropriate weight effecting the overall rate
        return (double) ratingSum / ratingCount;
    }

    /**
     * @see FilmDAO#totalNumberOfFilms()
     */
    @Override
    public int totalNumberOfFilms() {
        final String query = "SELECT count(ID) AS total FROM films ";
        return getJdbcTemplate().queryForObject(query, Integer.class);
    }

    /**
     * @see FilmDAO#getFilteredFilms(java.lang.String, int, int, java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public List<Film> getFilteredFilms(String title, int startYear, int finishYear, String hasOscar, String director, String castId, String genreId) {
        final String query = "SELECT films.ID, films.Title, films.Prod_Year, " +
                "films.HasOscar, films.image_ref, films.Director, " +
                "films.Rate_1star, films.Rate_2star, films.Rate_3star, " +
                "films.Rate_4star, films.Rate_5star FROM films " +
                "LEFT JOIN film_to_cast " +
                "ON films.ID = film_to_cast.Film_ID " +
                "LEFT JOIN genre_to_film " +
                "ON genre_to_film.Film_ID = films.ID " +
                "WHERE films.Title LIKE ? " +
                "AND films.Prod_Year >= ? " +
                "AND films.Prod_Year <= ? " +
                "AND films.HasOscar LIKE ? " +
                "AND (films.Director LIKE ? " +
                "OR films.Director IS NULL) " +
                "AND (film_to_cast.Actor_ID LIKE ? " +
                "OR film_to_cast.Actor_ID IS NULL)" +
                "AND (genre_to_film.Genre_ID LIKE ? " +
                "OR genre_to_film.Genre_ID IS NULL) ";
        return getJdbcTemplate().query(query, new Object[]{
                "%" + title + "%", startYear, finishYear, hasOscar, director, castId, genreId
        }, new FilmRowMapper());
    }


    //UPDATE

    /**
     * @see FilmDAO#editFilm(am.aca.orgflix.entity.Film)
     */
    @Override
    public boolean editFilm(Film film) {
        // ensure that all required fields are properly assigned
        if (!checkRequiredFields(film.getTitle()) || film.getProdYear() < 1900)
            throw new DaoException("Illegal argument");

        final String query = "UPDATE films SET Title = ?,Prod_Year = ?,HasOscar = ?,Rate_1star = ? " +
                ",Rate_2star = ?, Rate_3star = ?,Rate_4star = ?,Rate_5star = ?, director = ? " +
                " WHERE id = ? ";

        return getJdbcTemplate().update(query,
                film.getTitle(),
                film.getProdYear(),
                film.isHasOscar(),
                film.getRate_1star(),
                film.getRate_2star(),
                film.getRate_3star(),
                film.getRate_4star(),
                film.getRate_5star(),
                film.getDirector(),
                film.getId()
        ) == 1;
    }


    // DELETE

    /**
     * @see FilmDAO#resetRelationCasts(am.aca.orgflix.entity.Film)
     */
    @Override
    public boolean resetRelationCasts(Film film) {
        final String query = "DELETE FROM film_to_cast WHERE Film_ID = ?";
        return getJdbcTemplate().update(query, film.getId()) >= 1;
    }

    /**
     * @see FilmDAO#resetRelationGenres(am.aca.orgflix.entity.Film)
     */
    @Override
    public boolean resetRelationGenres(Film film) {
        final String query = "DELETE FROM genre_to_film WHERE Film_ID = ?";
        return getJdbcTemplate().update(query, film.getId()) >= 1;
    }

    /**
     * @see FilmDAO#remove(am.aca.orgflix.entity.Film)
     */
    @Override
    public boolean remove(Film film) {
        final String query = "DELETE FROM films WHERE ID = ?";
        return getJdbcTemplate().update(query, film.getId()) == 1;
    }

}
