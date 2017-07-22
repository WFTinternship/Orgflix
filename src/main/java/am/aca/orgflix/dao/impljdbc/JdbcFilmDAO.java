package am.aca.orgflix.dao.impljdbc;

import am.aca.orgflix.dao.FilmDAO;
import am.aca.orgflix.dao.impljdbc.mapper.FilmRowMapper;
import am.aca.orgflix.entity.Cast;
import am.aca.orgflix.entity.Film;
import am.aca.orgflix.entity.Genre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * DAO layer for Film entity
 */
@Component
public class JdbcFilmDAO extends NamedParameterJdbcDaoSupport implements FilmDAO {

    @Autowired
    public JdbcFilmDAO(DataSource dataSource) {
        this.setDataSource(dataSource);
    }

    // CREATE

    /**
     * @see FilmDAO#add(am.aca.orgflix.entity.Film)
     */
    @Override
    public boolean add(Film film) {
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
        final String query = "INSERT INTO FILM_TO_GENRE(GENRE_ID,FILM_ID) VALUES (? , ? ) ";
        return getJdbcTemplate().update(query, genre.getValue(), filmId) == 1;
    }

    @Override
    public boolean addCastToFilm(Cast cast, int filmId) {
        final String query = "INSERT INTO FILM_TO_CAST(ACTOR_ID,FILM_ID) VALUES (? , ? ) ";
        return getJdbcTemplate().update(query, cast.getId(), filmId) == 1;
    }

    /**
     * @see FilmDAO#rate(int, int)
     */
    @Override
    public boolean rate(int filmId, int starType) {
        final String starTypeQuery = "RATE_" + starType + "STAR";
        final String query = "UPDATE FILMS SET " + starTypeQuery + " = " + starTypeQuery + " + 1 WHERE ID = ? ";
        return getJdbcTemplate().update(query, filmId) == 1;
    }

    // READ

    /**
     * @see FilmDAO#getById(int)
     */
    @Override
    public Film getById(int id) {
        final String getQuery = "SELECT * FROM FILMS WHERE ID = ? LIMIT 1";
        return getJdbcTemplate().queryForObject(getQuery, new Object[]{id}, new FilmRowMapper());
    }

    /**
     * @see FilmDAO#getAll(int, int)
     */
    @Override
    public List<Film> getAll(int startIndex, int pageSize) {
        final String query = "SELECT * FROM FILMS LIMIT ? , ? ";
        return getJdbcTemplate().query(query,
                new Object[]{startIndex, pageSize},
                new FilmRowMapper());
    }

    /**
     * @see FilmDAO#getByGenre(am.aca.orgflix.entity.Genre)
     */
    @Override
    public List<Film> getByGenre(Genre genre) {
        final String filmQuery = "SELECT FILMS.ID, FILMS.TITLE, FILMS.DIRECTOR, FILMS.HAS_OSCAR, " +
                "FILMS.image_ref, FILMS.PROD_YEAR, FILMS.RATE_1STAR, FILMS.RATE_2STAR, " +
                "FILMS.RATE_3STAR, FILMS.RATE_4STAR, FILMS.RATE_5STAR " +
                "FROM FILM_TO_GENRE" +
                " LEFT JOIN FILMS ON FILM_TO_GENRE.FILM_ID = FILMS.ID " +
                " WHERE GENRE_ID = ?";

        return getJdbcTemplate().query(filmQuery, new Object[]{genre.getValue()}, new FilmRowMapper());
    }

    /**
     * @see FilmDAO#getByCast(int)
     */
    @Override
    public List<Film> getByCast(int actorId) {
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
    public int[] getRating(int filmId) {
        int[] ratings = new int[5];
        final String ratingQuery = "SELECT * FROM FILMS WHERE ID = ?";
        SqlRowSet sqlRowSet = getJdbcTemplate().queryForRowSet(ratingQuery, filmId);
        while (sqlRowSet.next()) {
            for (int i = 0; i <= 4; i++) {
                ratings[i] = sqlRowSet.getInt("RATE_" + (i + 1) + "STAR");
            }
        }
        return ratings;
    }

    /**
     * @see FilmDAO#getTotalNumber()
     */
    @Override
    public int getTotalNumber() {
        final String query = "SELECT COUNT(ID) AS total FROM FILMS ";
        return getJdbcTemplate().queryForObject(query, Integer.class);
    }

    /**
     * @see FilmDAO#getFilteredFilms(String, int, int, boolean, String, int, int)
     */
    @Override
    public List<Film> getFilteredFilms(String title, int startYear, int finishYear,
                                       boolean hasOscar, String director, int castId, int genreId) {

        StringBuilder queryBuilder = new StringBuilder();

        List<Object> paramsList = new ArrayList<>();

        queryBuilder.append("SELECT FILMS.ID, FILMS.TITLE, FILMS.PROD_YEAR, " +
                "FILMS.HAS_OSCAR, FILMS.IMAGE_REF, FILMS.DIRECTOR, " +
                "FILMS.RATE_1STAR, FILMS.RATE_2STAR, FILMS.RATE_3STAR, " +
                "FILMS.RATE_4STAR, FILMS.RATE_5STAR FROM FILMS " +
                "LEFT JOIN FILM_TO_CAST " +
                "ON FILMS.ID = FILM_TO_CAST.FILM_ID " +
                "LEFT JOIN FILM_TO_GENRE " +
                "ON FILM_TO_GENRE.FILM_ID = FILMS.ID " +
                "WHERE FILMS.TITLE LIKE ? ");

        paramsList.add("%" + title + "%");

        if (startYear != 0) {
            queryBuilder.append("AND FILMS.PROD_YEAR >= ? ");
            paramsList.add(startYear);
        }

        if (finishYear != 0) {
            queryBuilder.append("AND FILMS.PROD_YEAR <= ? ");
            paramsList.add(finishYear);
        }

        if (hasOscar)
            queryBuilder.append("AND FILMS.HAS_OSCAR = TRUE ");

        if (!Objects.equals(director, "")) {
            queryBuilder.append("AND FILMS.DIRECTOR LIKE ? ");
            paramsList.add("%" + director + "%");
        }

        if (castId > 0) {
            queryBuilder.append("AND FILM_TO_CAST.ACTOR_ID = ? ");
            paramsList.add(castId);
        }

        if (genreId > 0) {
            queryBuilder.append("AND FILM_TO_GENRE.GENRE_ID = ? ");
            paramsList.add(genreId);
        }

        queryBuilder.append("GROUP BY FILMS.ID");

        Object[] params = paramsList.toArray(new Object[0]);
        final String query = queryBuilder.toString();

        return getJdbcTemplate().query(query, params, new FilmRowMapper());
    }


    //UPDATE

    /**
     * @see FilmDAO#edit(am.aca.orgflix.entity.Film)
     */
    @Override
    public boolean edit(Film film) {
        final String query = "UPDATE FILMS SET TITLE = ?,PROD_YEAR = ?,HAS_OSCAR = ?, image_ref = ?, RATE_1STAR = ? " +
                ",RATE_2STAR = ?, RATE_3STAR = ?,RATE_4STAR = ?,RATE_5STAR = ?, DIRECTOR = ? " +
                " WHERE ID = ? ";

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
     * @see FilmDAO#removeCasts(am.aca.orgflix.entity.Film)
     */
    @Override
    public boolean removeCasts(Film film) {
        final String query = "DELETE FROM FILM_TO_CAST WHERE FILM_ID = ?";
        return getJdbcTemplate().update(query, film.getId()) >= 1;
    }

    /**
     * @see FilmDAO#removeGenres(am.aca.orgflix.entity.Film)
     */
    @Override
    public boolean removeGenres(Film film) {
        final String query = "DELETE FROM FILM_TO_GENRE WHERE FILM_ID = ?";
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
