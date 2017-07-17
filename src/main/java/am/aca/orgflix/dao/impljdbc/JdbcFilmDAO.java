package am.aca.orgflix.dao.impljdbc;

import am.aca.orgflix.dao.BaseDAO;
import am.aca.orgflix.dao.DaoException;
import am.aca.orgflix.dao.FilmDAO;
import am.aca.orgflix.dao.impljdbc.mapper.FilmRowMapper;
import am.aca.orgflix.entity.Film;
import am.aca.orgflix.entity.Genre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Calendar;
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
        KeyHolder holder = new GeneratedKeyHolder();

        final String query = "INSERT INTO FILMS (TITLE, PROD_YEAR, HAS_OSCAR, image_ref, " +
                " RATE_1STAR, RATE_2STAR, RATE_3STAR, RATE_4STAR, RATE_5STAR, DIRECTOR) " +
                " VALUES( ? , ? , ?, ?, ?, ?, ?, ?, ?, ?) ";

        int result = getJdbcTemplate().update(connection -> {
            PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, film.getTitle());
            statement.setInt(2, film.getProdYear());
            statement.setBoolean(3, film.isHasOscar());
            statement.setString(4, film.getImage());
            statement.setInt(5, film.getRate_1star());
            statement.setInt(6, film.getRate_2star());
            statement.setInt(7, film.getRate_3star());
            statement.setInt(8, film.getRate_4star());
            statement.setInt(9, film.getRate_5star());
            statement.setString(10, film.getDirector());
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
        final String query = "INSERT INTO GENRE_TO_FILM(GENRE_ID,FILM_ID) VALUES (? , ? ) ";
        return getJdbcTemplate().update(query, genre.getValue(), filmId) == 1;
    }

    /**
     * @see FilmDAO#rateFilm(int, int)
     */
    @Override
    public boolean rateFilm(int filmId, int starType) {
        final String starTypeQuery = "RATE_" + starType + "STAR";
        final String query = "UPDATE FILMS SET " + starTypeQuery + " = " + starTypeQuery + " + 1 WHERE ID = ? ";
        return getJdbcTemplate().update(query, filmId) == 1;
    }

    // READ

    /**
     * @see FilmDAO#getFilmById(int)
     */
    @Override
    public Film getFilmById(int id) {
        final String getQuery = "SELECT * FROM FILMS WHERE ID = ? LIMIT 1";
        try {
            return getJdbcTemplate().queryForObject(getQuery, new Object[]{id}, new FilmRowMapper());
        } catch (RuntimeException e) {
            // no film with such id exists in the DB
            return null;
        }
    }

    /**
     * @see FilmDAO#getFilmsList(int)
     */
    @Override
    public List<Film> getFilmsList(int startIndex) {
        final String query = "SELECT * FROM FILMS LIMIT ? , 12 ";
        return getJdbcTemplate().query(query,
                new Object[]{startIndex},
                new FilmRowMapper());
    }

    /**
     * @see FilmDAO#getFilmsByGenre(am.aca.orgflix.entity.Genre)
     */
    @Override
    public List<Film> getFilmsByGenre(Genre genre) {
        final String filmQuery = "SELECT FILMS.ID, FILMS.TITLE, FILMS.DIRECTOR, FILMS.HAS_OSCAR, " +
                "FILMS.image_ref, FILMS.PROD_YEAR, FILMS.RATE_1STAR, FILMS.RATE_2STAR, " +
                "FILMS.RATE_3STAR, FILMS.RATE_4STAR, FILMS.RATE_5STAR " +
                "FROM GENRE_TO_FILM" +
                " LEFT JOIN FILMS ON GENRE_TO_FILM.FILM_ID = FILMS.ID " +
                " WHERE GENRE_ID = ?";

        return getJdbcTemplate().query(filmQuery, new Object[]{genre.getValue()}, new FilmRowMapper());
    }

    /**
     * @see FilmDAO#getFilmsByCast(int)
     */
    @Override
    public List<Film> getFilmsByCast(int actorId) {
        final String query = "SELECT ID, TITLE, PROD_YEAR,HAS_OSCAR, image_ref, DIRECTOR, " +
                " RATE_1STAR, RATE_2STAR, RATE_3STAR, RATE_4STAR, RATE_5STAR " +
                " FROM FILMS INNER JOIN (" +
                "   SELECT FILM_TO_CAST.FILM_ID AS film " +
                "   FROM CASTS JOIN FILM_TO_CAST " +
                "   ON CASTS.ID = FILM_TO_CAST.Actor_ID " +
                "   WHERE CASTS.ID = ? " +
                "   ORDER BY FILM_TO_CAST.FILM_ID DESC " +
                ") AS sel_table " +
                " ON FILMS.ID = sel_table.film";

        return getJdbcTemplate().query(query,
                new FilmRowMapper(),
                actorId);
    }

    /**
     * @see FilmDAO#getRating(int)
     */
    @Override
    public double getRating(int filmId) {
        final String ratingQuery = "SELECT * FROM FILMS WHERE ID = ?";
        int ratingSum = 0;
        int ratingCount = 0;
        SqlRowSet sqlRowSet = getJdbcTemplate().queryForRowSet(ratingQuery, filmId);
        while (sqlRowSet.next()) {
            for (int i = 1; i <= 5; i++) {
                ratingSum += sqlRowSet.getInt("RATE_" + i + "STAR") * i;
                ratingCount += sqlRowSet.getInt("RATE_" + i + "STAR");
            }
        }
        // for the case when the film is not rated yet
        if (ratingCount == 0)
            return 0.0;
        // each scale of one to five has appropriate weight effecting the overall rate
        return (double) ratingSum / ratingCount;
    }

    /**
     * @see FilmDAO#getRating(int, int)
     */
    @Override
    public int getRating(int filmId, int starType) {
        String starTypeQuery = "RATE_" + starType + "STAR";
        final String query = "SELECT" + starTypeQuery + "FROM FILMS WHERE ID = ?";
        return getJdbcTemplate().queryForObject(query, new Object[]{filmId}, Integer.class);
    }

    /**
     * @see FilmDAO#totalNumberOfFilms()
     */
    @Override
    public int totalNumberOfFilms() {
        final String query = "SELECT COUNT(ID) AS total FROM FILMS ";
        return getJdbcTemplate().queryForObject(query, Integer.class);
    }

    /**
     * @see FilmDAO#getFilteredFilms(java.lang.String, int, int, java.lang.String,
     * java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public List<Film> getFilteredFilms(String title, int startYear, int finishYear, String hasOscar, String director, String castId, String genreId) {
        final String query = "SELECT FILMS.ID, FILMS.TITLE, FILMS.PROD_YEAR, " +
                "FILMS.HAS_OSCAR, FILMS.image_ref, FILMS.DIRECTOR, " +
                "FILMS.RATE_1STAR, FILMS.RATE_2STAR, FILMS.RATE_3STAR, " +
                "FILMS.RATE_4STAR, FILMS.RATE_5STAR FROM FILMS " +
                "LEFT JOIN FILM_TO_CAST " +
                "ON FILMS.ID = FILM_TO_CAST.FILM_ID " +
                "LEFT JOIN GENRE_TO_FILM " +
                "ON GENRE_TO_FILM.FILM_ID = FILMS.ID " +
                "WHERE FILMS.TITLE LIKE ? " +
                "AND FILMS.PROD_YEAR >= ? " +
                "AND FILMS.PROD_YEAR <= ? " +
                "AND FILMS.HAS_OSCAR LIKE ? " +
                "AND (FILMS.DIRECTOR LIKE ? " +
                "OR FILMS.DIRECTOR IS NULL) " +
                "AND (FILM_TO_CAST.Actor_ID LIKE ? " +
                "OR FILM_TO_CAST.Actor_ID IS NULL)" +
                "AND (GENRE_TO_FILM.GENRE_ID LIKE ? " +
                "OR GENRE_TO_FILM.GENRE_ID IS NULL) ";
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
        if (!checkRequiredFields(film.getTitle()) || 1888 > film.getProdYear()
                || Calendar.getInstance().get(Calendar.YEAR) + 7 < film.getProdYear())
            throw new DaoException("Illegal argument");

        final String query = "UPDATE FILMS SET TITLE = ?,PROD_YEAR = ?,HAS_OSCAR = ?, image_ref = ?, RATE_1STAR = ? " +
                ",RATE_2STAR = ?, RATE_3STAR = ?,RATE_4STAR = ?,RATE_5STAR = ?, DIRECTOR = ? " +
                " WHERE ID = ? ";
//        final String query = "UPDATE films SET Title = ?,Prod_Year = ?,HasOscar = ?, image_ref = ?, Rate_1star = ? " +
//                ",Rate_2star = ?, Rate_3star = ?,Rate_4star = ?,Rate_5star = ?, director = ? " +
//                " WHERE id = ? ";

        return getJdbcTemplate().update(query,
                film.getTitle(),
                film.getProdYear(),
                film.isHasOscar(),
                film.getImage(),
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
        final String query = "DELETE FROM FILM_TO_CAST WHERE FILM_ID = ?";
        return getJdbcTemplate().update(query, film.getId()) >= 1;
    }

    /**
     * @see FilmDAO#resetRelationGenres(am.aca.orgflix.entity.Film)
     */
    @Override
    public boolean resetRelationGenres(Film film) {
        final String query = "DELETE FROM GENRE_TO_FILM WHERE FILM_ID = ?";
        return getJdbcTemplate().update(query, film.getId()) >= 1;
    }

    /**
     * @see FilmDAO#remove(am.aca.orgflix.entity.Film)
     */
    @Override
    public boolean remove(Film film) {
        final String query = "DELETE FROM FILMS WHERE ID = ?";
        return getJdbcTemplate().update(query, film.getId()) == 1;
    }

}
