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
        final String insertQuery = "INSERT INTO lists(User_ID, Film_ID, Is_watched, Is_public) " +
                "VALUES (?, ?, TRUE, ?)";
        return (getJdbcTemplate().update(insertQuery, userId, filmId, isPublic) == 1);
    }

    /**
     * @see ListDao#insertPlanned(int, int, boolean)
     */
    @Override
    public boolean insertPlanned(int filmId, int userId, boolean isPublic) {
        final String insertQuery = "INSERT INTO lists(User_ID, Film_ID, Is_wished, Is_public) " +
                "VALUES (?, ?, TRUE, ?)";
        return (getJdbcTemplate().update(insertQuery, userId, filmId, isPublic) == 1);
    }

    // RETRIEVE

    /**
     * @see ListDao#showOwnWatched(int, int)
     */
    @Override
    public List<Film> showOwnWatched(int userId, int page) {
        final String query = "SELECT films.ID, films.Title, films.Director, films.HasOscar, " +
                "films.image_ref, films.Prod_Year, films.Rate_1star, films.Rate_2star, " +
                "films.Rate_3star, films.Rate_4star, films.Rate_5star " +
                "FROM lists INNER JOIN films " +
                "ON lists.Film_ID = films.ID " +
                "WHERE lists.User_ID = ? AND Is_watched = TRUE LIMIT ? , 12 ";
        return getJdbcTemplate().query(query, new Object[]{userId, page}, new FilmRowMapper());
    }

    /**
     * @see ListDao#showOwnPlanned(int, int)
     */
    @Override
    public List<Film> showOwnPlanned(int userId, int page) {
        final String query = "SELECT films.ID, films.Title, films.Director, films.HasOscar, " +
                "films.image_ref, films.Prod_Year, films.Rate_1star, films.Rate_2star, " +
                "films.Rate_3star, films.Rate_4star, films.Rate_5star " +
                "FROM lists INNER JOIN films " +
                "ON lists.Film_ID = films.ID " +
                "WHERE lists.User_ID = ? AND Is_wished = TRUE LIMIT ? , 12 ";
        return getJdbcTemplate().query(query, new Object[]{userId, page}, new FilmRowMapper());
    }

    /**
     * @see ListDao#showOthersWatched(int, int)
     */
    @Override
    public List<Film> showOthersWatched(int userId, int page) {
        final String query = "SELECT films.ID, films.Title, films.Director, films.HasOscar, " +
                "films.image_ref, films.Prod_Year, films.Rate_1star, films.Rate_2star, " +
                "films.Rate_3star, films.Rate_4star, films.Rate_5star FROM lists INNER JOIN films " +
                "ON lists.Film_ID = films.ID " +
                "WHERE lists.User_ID = ? AND Is_watched = TRUE AND Is_public = TRUE LIMIT ? , 12 ";
        return getJdbcTemplate().query(query, new Object[]{userId, page}, new FilmRowMapper());
    }

    /**
     * @see ListDao#showOthersPlanned(int, int)
     */
    @Override
    public List<Film> showOthersPlanned(int userId, int page) {
        final String query = "SELECT films.ID, films.Title, films.Director, films.HasOscar, " +
                "films.image_ref, films.Prod_Year, films.Rate_1star, films.Rate_2star, " +
                "films.Rate_3star, films.Rate_4star, films.Rate_5star FROM lists INNER JOIN films " +
                "ON lists.Film_ID = films.ID " +
                "WHERE lists.User_ID = ? AND Is_wished = TRUE AND Is_public = TRUE LIMIT ? , 12 ";
        return getJdbcTemplate().query(query, new Object[]{userId, page}, new FilmRowMapper());
    }

    // UPDATE

    /**
     * @see ListDao#updateWatched(int, int)
     */
    @Override
    public boolean updateWatched(int filmId, int userId) {
        final String updateQuery = "UPDATE lists SET Is_watched = TRUE " +
                "WHERE User_ID = ? AND Film_ID = ?";
        return (getJdbcTemplate().update(updateQuery, userId, filmId) == 1);
    }

    /**
     * @see ListDao#updatePlanned(int, int)
     */
    @Override
    public boolean updatePlanned(int filmId, int userId) {
        final String updateQuery = "UPDATE lists SET Is_wished = TRUE " +
                "WHERE User_ID = ? AND Film_ID = ?";
        return (getJdbcTemplate().update(updateQuery, userId, filmId) == 1);
    }

    /**
     * @see ListDao#changePrivacy(am.aca.orgflix.entity.Film, int, boolean)
     */
    @Override
    public boolean changePrivacy(Film film, int userId, boolean isPublic) {
        final String query = "UPDATE lists SET Is_public = ? WHERE User_ID = ? AND Film_ID = ?;";
        return (getJdbcTemplate().update(query, isPublic, userId, film.getId()) == 1);
    }

    // DELETE

    /**
     * @see ListDao#resetWatched(int, int)
     */
    @Override
    public boolean resetWatched(int filmId, int userId) {
        final String updateQuery = "UPDATE lists SET Is_watched = FALSE WHERE User_ID = ? AND Film_ID = ?";
        return (getJdbcTemplate().update(updateQuery, userId, filmId) == 1);
    }

    /**
     * @see ListDao#resetPlanned(int, int)
     */
    @Override
    public boolean resetPlanned(int filmId, int userId) {
        final String updateQuery = "UPDATE lists SET Is_wished = FALSE WHERE User_ID = ? AND Film_ID = ?";
        return (getJdbcTemplate().update(updateQuery, userId, filmId) == 1);
    }

    /**
     * @see ListDao#removeFilm(int, int)
     */
    @Override
    public boolean removeFilm(int filmId, int userId) {
        final String deleteQuery = "DELETE FROM lists WHERE User_ID = ? AND Film_ID = ?";
        return (getJdbcTemplate().update(deleteQuery, userId, filmId) == 1);
    }

    // SUPPORT METHODS

    /**
     * @see ListDao#areRelated(int, int)
     */
    @Override
    public boolean areRelated(int filmId, int userId) {
        final String checkQuery = "SELECT COUNT(*) FROM lists WHERE User_ID = ? AND Film_ID = ?";
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
        final String watchedCheckQuery = "SELECT COUNT(*) FROM lists WHERE User_ID = ? " +
                "AND Film_ID = ? AND Is_watched = TRUE";
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
        final String plannedCheckQuery = "SELECT COUNT(*) FROM lists WHERE User_ID = ? " +
                "AND Film_ID = ? AND Is_wished = TRUE ";
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
        final String query = "SELECT count(Film_ID) AS total FROM lists " +
                "WHERE User_ID = ? AND Is_watched = TRUE";
        return getJdbcTemplate().queryForObject(query, new Object[]{userId}, Integer.class);
    }

    /**
     * @see ListDao#totalNumberOfPlanned(int)
     */
    @Override
    public int totalNumberOfPlanned(int userId) {
        final String query = "SELECT count(Film_ID) AS total FROM lists " +
                "WHERE User_ID = ? AND Is_wished = TRUE";
        return getJdbcTemplate().queryForObject(query, new Object[]{userId}, Integer.class);
    }
}