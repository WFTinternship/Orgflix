package am.aca.dao.jdbc.impljdbc;


import am.aca.dao.DaoException;
import am.aca.dao.jdbc.BaseDAO;
import am.aca.dao.jdbc.FilmDAO;
import am.aca.entity.*;
import am.aca.dao.jdbc.impljdbc.mapper.FilmRowMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

/**
 * DAO layer for Film entity
 *
 * Created by David on 5/28/2017
 */
@Repository
public class JdbcFilmDAO extends BaseDAO implements FilmDAO {

    @Autowired
    public JdbcFilmDAO(DataSource dataSource) {
        super(JdbcFilmDAO.class);
        this.setDataSource(dataSource);
    }

    // CREATE

    /**
     * Add new film to DB, the production year is expected to be later then 19000
     *
     * @param film the film object to be added
     * @return true if the film was added, otherwise false
     */
    @Override
    public boolean addFilm(Film film) {
        // ensure that all required fields are properly assigned
        if (!checkRequiredFields(film.getTitle()) || film.getProdYear() < 1900)
            throw new DaoException("Illegal argument");

        KeyHolder holder = new GeneratedKeyHolder();

        final String query = "INSERT INTO films (Title, Prod_Year, HasOscar, Rate_1star, Rate_2star, Rate_3star, Rate_4star, Rate_5star, director) " +
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
     * Add an association of genre with film in DB
     *
     * @param genre the genre with which the provided film will be associated
     * @param filmId the id of film which will be associated with provided genre
     * @return true if the new association of genre to film was successful, otherwise false
     */
    @Override
    public boolean addGenreToFilm(Genre genre, int filmId) {
        final String query = "INSERT INTO genre_to_film(Genre_ID,Film_ID) VALUES (? , ? ) ";
        return getJdbcTemplate().update(query, genre.getValue(), filmId) == 1;
    }

    /**
     * @see JdbcFilmDAO#addGenreToFilm(am.aca.entity.Genre, int)
     *
     * Add an association of genre with film in DB
     *
     * @param genre the genre with which the provided film will be associated
     * @param film the film which will be associated with provided genre
     * @return true if the new association of genre to film was successful, otherwise false
     */
    @Override
    public boolean addGenreToFilm(Genre genre, Film film) {
        return addGenreToFilm(genre, film.getId());
    }


    /**
     * Increment by one the selected scale (from 1 to 5) of the film whitch id is provided
     *
     * @param filmId the id of the film subject to be rated
     * @param starType the selected scale of rates to be incremented by one
     * @return true if the rating was successful, otherwise false
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
     * Finds and returns the film by provided id from DB
     *
     * @param id the id of the requested film
     * @return the film matching the provided id
     */
    @Override
    public Film getFilmById(int id) {
        final String getQuery = "SELECT * FROM films WHERE ID = ? LIMIT 1";
        try {
            return (Film) getJdbcTemplate().queryForObject(getQuery, new Object[]{id}, new FilmRowMapper());
        } catch (EmptyResultDataAccessException e) {
            // no film with such id exists in the DB
            return null;
        }
    }


    /**
     * Get one page of lists of films from DB, each page contains 12 films
     *
     * @param startIndex 0 based page index
     * @return a list of films (up to 12 films) for the requested page
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Film> getFilmsList(int startIndex) {
        final String query = "SELECT * FROM films LIMIT ? , 12 ";
        return getJdbcTemplate().query(query,
                new Object[]{startIndex},
                new FilmRowMapper());
    }

    /**
     * Return the list of all films associated with provided genre
     *
     * @param genre the genre object which films should be returned
     * @return A list of all films associated to the provided genre
     */
    @Override
    public List<Film> getFilmsByGenre(Genre genre) {
        final String filmQuery = "SELECT films.ID, films.Title, films.Director, films.HasOscar, " +
                "films.image_ref, films.Prod_Year, films.Rate_1star, films.Rate_2star, films.Rate_3star, films.Rate_4star, films.Rate_5star FROM genre_to_film" +
                " LEFT JOIN films ON genre_to_film.Film_ID = films.ID " +
                " WHERE Genre_ID = ?";

        return getJdbcTemplate().query(filmQuery, new Object[]{genre.getValue()}, new FilmRowMapper());
    }

    /**
     * Return the list of all films associated with provided actors id
     *
     * @param actorId the id of actor who's films should be returned
     * @return A list of all films associated to the actor with provided id
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Film> getFilmsByCast(int actorId) {
        final String query = "SELECT ID, Title, Prod_Year AS prodYear,HasOscar, image_ref AS image, Director, " +
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
                new BeanPropertyRowMapper(Film.class),
                actorId);
    }

    /**
     * Get the overall current rating of the provided film, each scale of 1 to 5 has appropriate
     * weight, for example one 5 star and two 4 stars give rate (1*5+2*4)/(1+2)
     *
     * @param filmId the id of film which rate is requested
     * @return the current overall rate of the film
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
     * @see JdbcFilmDAO#getRating(int)
     *
     * Get the overall current rating of the provided film, each scale of 1 to 5 has appropriate
     * weight, for example one 5 star and two 4 stars give rate (1*5+2*4)/(1+2)
     *
     * @param film the film which rate is requested
     * @return the current overall rate of the film
     */
    @Override
    public double getRating(Film film) {
        return getRating(film.getId());
    }

    /**
     * Provide the current total number of films in DB
     *
     * @return the current total number of films in DB
     */
    @Override
    public int totalNumberOfFilms() {
        final String query = "SELECT count(ID) AS total FROM films ";
        return getJdbcTemplate().queryForObject(query, Integer.class);
    }


    //UPDATE

    /**
     * Update the fileds of the provided film in DB
     *
     * @param film the film which fields will be updated in DB
     * @return true if the update was successful, otherwise false
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
     * Remove the all relations of provided film with any actor
     *
     * @param film the film which relations with actors are being removed
     * @return true if the relation was removed, otherwise false
     */
    @Override
    public boolean resetRelationCasts(Film film) {
        final String query = "DELETE FROM film_to_cast WHERE Film_ID = ?";
        return getJdbcTemplate().update(query, film.getId()) >= 1;
    }

    /**
     * Remove the all relations of provided film with any genre
     *
     * @param film the film which relations with genres are being removed
     * @return true if the relation was removed, otherwise false
     */
    @Override
    public boolean resetRelationGenres(Film film) {
        final String query = "DELETE FROM genre_to_film WHERE Film_ID = ?";
        return getJdbcTemplate().update(query, film.getId()) >= 1;
    }

    /**
     * Remove the provided film from DB
     *
     * @param film the film which are being removed
     * @return true if the film was removed, otherwise false
     */
    @Override
    public boolean remove(Film film) {
        final String query = "DELETE FROM films WHERE ID = ?";
        return getJdbcTemplate().update(query, film.getId()) == 1;
    }

}
