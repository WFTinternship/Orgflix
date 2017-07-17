package am.aca.orgflix.dao.impljdbc;

import am.aca.orgflix.dao.BaseDAO;
import am.aca.orgflix.dao.ListDao;
import am.aca.orgflix.dao.impljdbc.mapper.FilmRowMapper;
import am.aca.orgflix.entity.Film;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

/**
 * DAO layer for list of film related methods
 */
@Component
public class JdbcListDAO extends BaseDAO implements ListDao {

    @Autowired
    public JdbcListDAO(DataSource dataSource) {
        super(JdbcUserDAO.class);
        this.setDataSource(dataSource);
    }

    // CREATE

    /**
     * @see am.aca.orgflix.dao.ListDao#insertWatched(int, int, boolean)
     */
    @Override
    public boolean insertWatched(int filmId, int userId, boolean isPublic) {
        final String insertQuery = "INSERT INTO LISTS(USER_ID, FILM_ID, IS_WATCHED, IS_PUBLIC) " +
                "VALUES (?, ?, TRUE, ?)";
        return (getJdbcTemplate().update(insertQuery, userId, filmId, isPublic) == 1);
    }

    /**
     * @see ListDao#insertPlanned(int, int, boolean)
     */
    @Override
    public boolean insertPlanned(int filmId, int userId, boolean isPublic) {
        final String insertQuery = "INSERT INTO LISTS(USER_ID, FILM_ID, IS_WISHED, IS_PUBLIC) " +
                "VALUES (?, ?, TRUE, ?)";
        return (getJdbcTemplate().update(insertQuery, userId, filmId, isPublic) == 1);
    }

    // RETRIEVE

    /**
     * @see ListDao#showOwnWatched(int, int, int)
     */
    @Override
    public List<Film> showOwnWatched(int userId, int page, int itemsPerPage) {
        final String query = "SELECT FILMS.ID, FILMS.TITLE, FILMS.DIRECTOR, FILMS.HAS_OSCAR, " +
                "FILMS.IMAGE_REF, FILMS.PROD_YEAR, FILMS.RATE_1STAR, FILMS.RATE_2STAR, " +
                "FILMS.RATE_3STAR, FILMS.RATE_4STAR, FILMS.RATE_5STAR " +
                "FROM LISTS INNER JOIN FILMS " +
                "ON LISTS.FILM_ID = FILMS.ID " +
                "WHERE LISTS.USER_ID = ? AND IS_WATCHED = TRUE LIMIT ? , ? ";
        return getJdbcTemplate().query(query, new Object[]{userId, page, itemsPerPage}, new FilmRowMapper());
    }

    /**
     * @see ListDao#showOwnPlanned(int, int, int)
     */
    @Override
    public List<Film> showOwnPlanned(int userId, int page, int itemsPerPage) {
        final String query = "SELECT FILMS.ID, FILMS.TITLE, FILMS.DIRECTOR, FILMS.HAS_OSCAR, " +
                "FILMS.IMAGE_REF, FILMS.PROD_YEAR, FILMS.RATE_1STAR, FILMS.RATE_2STAR, " +
                "FILMS.RATE_3STAR, FILMS.RATE_4STAR, FILMS.RATE_5STAR " +
                "FROM LISTS INNER JOIN FILMS " +
                "ON LISTS.FILM_ID = FILMS.ID " +
                "WHERE LISTS.USER_ID = ? AND IS_WISHED = TRUE LIMIT ? , ? ";
        return getJdbcTemplate().query(query, new Object[]{userId, page, itemsPerPage}, new FilmRowMapper());
    }

    /**
     * @see ListDao#showOthersWatched(int, int, int)
     */
    @Override
    public List<Film> showOthersWatched(int userId, int page, int itemsPerPage) {
        final String query = "SELECT FILMS.ID, FILMS.TITLE, FILMS.DIRECTOR, FILMS.HAS_OSCAR, " +
                "FILMS.IMAGE_REF, FILMS.PROD_YEAR, FILMS.RATE_1STAR, FILMS.RATE_2STAR, " +
                "FILMS.RATE_3STAR, FILMS.RATE_4STAR, FILMS.RATE_5STAR FROM LISTS INNER JOIN FILMS " +
                "ON LISTS.FILM_ID = FILMS.ID " +
                "WHERE LISTS.USER_ID = ? AND IS_WATCHED = TRUE AND IS_PUBLIC = TRUE LIMIT ? , ? ";
        return getJdbcTemplate().query(query, new Object[]{userId, page, itemsPerPage}, new FilmRowMapper());
    }

    /**
     * @see ListDao#showOthersPlanned(int, int, int)
     */
    @Override
    public List<Film> showOthersPlanned(int userId, int page, int itemsPerPage) {
        final String query = "SELECT FILMS.ID, FILMS.TITLE, FILMS.DIRECTOR, FILMS.HAS_OSCAR, " +
                "FILMS.IMAGE_REF, FILMS.PROD_YEAR, FILMS.RATE_1STAR, FILMS.RATE_2STAR, " +
                "FILMS.RATE_3STAR, FILMS.RATE_4STAR, FILMS.RATE_5STAR FROM LISTS INNER JOIN FILMS " +
                "ON LISTS.FILM_ID = FILMS.ID " +
                "WHERE LISTS.USER_ID = ? AND IS_WISHED = TRUE AND IS_PUBLIC = TRUE LIMIT ? , ? ";
        return getJdbcTemplate().query(query, new Object[]{userId, page, itemsPerPage}, new FilmRowMapper());
    }

    // UPDATE

    /**
     * @see ListDao#updateWatched(int, int)
     */
    @Override
    public boolean updateWatched(int filmId, int userId) {
        final String updateQuery = "UPDATE LISTS SET IS_WATCHED = TRUE " +
                "WHERE USER_ID = ? AND FILM_ID = ?";
        return (getJdbcTemplate().update(updateQuery, userId, filmId) == 1);
    }

    /**
     * @see ListDao#updatePlanned(int, int)
     */
    @Override
    public boolean updatePlanned(int filmId, int userId) {
        final String updateQuery = "UPDATE LISTS SET IS_WISHED = TRUE " +
                "WHERE USER_ID = ? AND FILM_ID = ?";
        return (getJdbcTemplate().update(updateQuery, userId, filmId) == 1);
    }

    /**
     * @see ListDao#changePrivacy(am.aca.orgflix.entity.Film, int, boolean)
     */
    @Override
    public boolean changePrivacy(Film film, int userId, boolean isPublic) {
        final String query = "UPDATE LISTS SET IS_PUBLIC = ? WHERE USER_ID = ? AND FILM_ID = ?;";
        return (getJdbcTemplate().update(query, isPublic, userId, film.getId()) == 1);
    }

    // DELETE

    /**
     * @see ListDao#resetWatched(int, int)
     */
    @Override
    public boolean resetWatched(int filmId, int userId) {
        final String updateQuery = "UPDATE LISTS SET IS_WATCHED = FALSE WHERE USER_ID = ? AND FILM_ID = ?";
        return (getJdbcTemplate().update(updateQuery, userId, filmId) == 1);
    }

    /**
     * @see ListDao#resetPlanned(int, int)
     */
    @Override
    public boolean resetPlanned(int filmId, int userId) {
        final String updateQuery = "UPDATE LISTS SET IS_WISHED = FALSE WHERE USER_ID = ? AND FILM_ID = ?";
        return (getJdbcTemplate().update(updateQuery, userId, filmId) == 1);
    }

    /**
     * @see ListDao#removeFilm(int, int)
     */
    @Override
    public boolean removeFilm(int filmId, int userId) {
        final String deleteQuery = "DELETE FROM LISTS WHERE USER_ID = ? AND FILM_ID = ?";
        return (getJdbcTemplate().update(deleteQuery, userId, filmId) == 1);
    }

    // SUPPORT METHODS

    /**
     * @see ListDao#areRelated(int, int)
     */
    @Override
    public boolean areRelated(int filmId, int userId) {
        final String checkQuery = "SELECT COUNT(*) FROM LISTS WHERE USER_ID = ? AND FILM_ID = ?";
        int count = getJdbcTemplate().queryForObject(checkQuery, new Object[]{
                userId, filmId
        }, Integer.class);
        return (count == 1);
    }

    /**
     * @see ListDao#isWatched(int, int)
     */
    @Override
    public boolean isWatched(int filmId, int userId) {
        final String watchedCheckQuery = "SELECT COUNT(*) FROM LISTS WHERE USER_ID = ? " +
                "AND FILM_ID = ? AND IS_WATCHED = TRUE";
        int watchedCount = getJdbcTemplate().queryForObject(watchedCheckQuery, new Object[]{
                userId, filmId
        }, Integer.class);
        return (watchedCount == 1);
    }

    /**
     * @see ListDao#isPlanned(int, int)
     */
    @Override
    public boolean isPlanned(int filmId, int userId) {
        final String plannedCheckQuery = "SELECT COUNT(*) FROM LISTS WHERE USER_ID = ? " +
                "AND FILM_ID = ? AND IS_WISHED = TRUE ";
        int plannedCount = getJdbcTemplate().queryForObject(plannedCheckQuery, new Object[]{
                userId, filmId
        }, Integer.class);
        return (plannedCount == 1);
    }

    /**
     * @see ListDao#totalNumberOfWatched(int)
     */
    @Override
    public int totalNumberOfWatched(int userId) {
        final String query = "SELECT COUNT(FILM_ID) AS total FROM LISTS " +
                "WHERE USER_ID = ? AND IS_WATCHED = TRUE";
        return getJdbcTemplate().queryForObject(query, new Object[]{userId}, Integer.class);
    }

    /**
     * @see ListDao#totalNumberOfPlanned(int)
     */
    @Override
    public int totalNumberOfPlanned(int userId) {
        final String query = "SELECT COUNT(FILM_ID) AS total FROM LISTS " +
                "WHERE USER_ID = ? AND IS_WISHED = TRUE";
        return getJdbcTemplate().queryForObject(query, new Object[]{userId}, Integer.class);
    }
}