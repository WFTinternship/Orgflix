package am.aca.dao.jdbc.impljdbc;

import am.aca.dao.jdbc.BaseDAO;
import am.aca.dao.jdbc.ListDao;
import am.aca.dao.jdbc.impljdbc.mapper.FilmRowMapper;
import am.aca.entity.Film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

/**
 * DAO layer for list of film related methods
 */
//@Repository
public class JdbcListDAO extends BaseDAO implements ListDao {

    @Autowired
    public JdbcListDAO(DataSource dataSource) {
        super(JdbcUserDAO.class);
        this.setDataSource(dataSource);
    }

    // CREATE

    @Override
    public boolean insertWatched(int filmId, int userId, boolean isPublic) {
        final String insertQuery = "INSERT INTO Lists(User_ID, Film_ID, Is_watched, Is_public) VALUES (?, ?, TRUE, ?)";
        return (getJdbcTemplate().update(insertQuery, userId, filmId, isPublic) == 1);
    }

    @Override
    public boolean insertWatched(Film film, int userId, boolean isPublic) {
        return insertWatched(film.getId(), userId, isPublic);
    }

    @Override
    public boolean insertPlanned(int filmId, int userId, boolean isPublic) {
        final String insertQuery = "INSERT INTO Lists(User_ID, Film_ID, Is_wished, Is_public) VALUES (?, ?, TRUE, ?)";
        return (getJdbcTemplate().update(insertQuery, userId, filmId, isPublic) == 1);
    }

    @Override
    public boolean insertPlanned(Film film, int userId, boolean isPublic) {
        return insertPlanned(film.getId(),userId,isPublic);
    }
    // RETRIEVE

    @SuppressWarnings("unchecked")
    @Override
    public List<Film> showOwnWatched(int userId) {
        final String query = "SELECT films.ID, films.Title, films.Director, films.HasOscar, " +
                "films.image_ref, films.Prod_Year, films.Rate_1star, films.Rate_2star, films.Rate_3star, films.Rate_4star, films.Rate_5star FROM lists INNER JOIN films " +
                "ON lists.Film_ID = films.ID WHERE lists.User_ID = ? AND Is_watched = TRUE ";
        return getJdbcTemplate().query(query, new Object[]{userId}, new FilmRowMapper());
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Film> showOwnPlanned(int userId) {
        final String query = "SELECT films.ID, films.Title, films.Director, films.HasOscar, " +
                "films.image_ref, films.Prod_Year, films.Rate_1star, films.Rate_2star, films.Rate_3star, films.Rate_4star, films.Rate_5star FROM lists INNER JOIN films " +
                "ON lists.Film_ID = films.ID WHERE lists.User_ID = ? AND Is_wished = TRUE ";
        return getJdbcTemplate().query(query, new Object[]{userId}, new FilmRowMapper());
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Film> showOthersWatched(int userId) {
        final String query = "SELECT films.ID, films.Title, films.Director, films.HasOscar, " +
                "films.image_ref, films.Prod_Year, films.Rate_1star, films.Rate_2star, films.Rate_3star, films.Rate_4star, films.Rate_5star FROM lists INNER JOIN films " +
                "ON lists.Film_ID = films.ID WHERE lists.User_ID = ? AND Is_watched = TRUE AND Is_public = TRUE ";
        return getJdbcTemplate().query(query, new Object[]{userId}, new FilmRowMapper());
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Film> showOthersPlanned(int userId) {
        final String query = "SELECT films.ID, films.Title, films.Director, films.HasOscar, " +
                "films.image_ref, films.Prod_Year, films.Rate_1star, films.Rate_2star, films.Rate_3star, films.Rate_4star, films.Rate_5star FROM lists INNER JOIN films " +
                "ON lists.Film_ID = films.ID WHERE lists.User_ID = ? AND Is_wished = TRUE AND Is_public = TRUE ";
        return getJdbcTemplate().query(query, new Object[]{userId}, new FilmRowMapper());
    }

    // UPDATE

    @Override
    public boolean updateWatched(int filmId, int userId) {
        final String updateQuery = "UPDATE lists SET Is_watched = TRUE " +
                "WHERE User_ID = ? AND Film_ID = ?";
        return (getJdbcTemplate().update(updateQuery, userId, filmId) == 1);
    }

    @Override
    public boolean updatePlanned(int filmId, int userId) {
        final String updateQuery = "UPDATE lists SET Is_wished = TRUE " +
                "WHERE User_ID = ? AND Film_ID = ?";
        return (getJdbcTemplate().update(updateQuery, userId, filmId) == 1);
    }

    @Override
    public boolean changePrivacy(Film film, int userId, boolean isPublic) {
        final String query = "UPDATE lists SET Is_public = ? WHERE User_ID = ? AND Film_ID = ?;";
        return (getJdbcTemplate().update(query, isPublic, userId, film.getId()) == 1);
    }

    // DELETE

    @Override
    public boolean resetWatched(int filmId, int userId) {
        final String updateQuery = "UPDATE lists SET Is_watched = FALSE WHERE User_ID = ? AND Film_ID = ?";
        return (getJdbcTemplate().update(updateQuery, userId, filmId) == 1);
    }

    @Override
    public boolean resetPlanned(int filmId, int userId) {
        final String updateQuery = "UPDATE lists SET Is_wished = FALSE WHERE User_ID = ? AND Film_ID = ?";
        return (getJdbcTemplate().update(updateQuery, userId, filmId) == 1);
    }

    @Override
    public boolean removeFilm(int filmId, int userId) {
        final String deleteQuery = "DELETE FROM lists WHERE User_ID = ? AND Film_ID = ?";
        return (getJdbcTemplate().update(deleteQuery, userId, filmId) == 1);
    }

    // SUPPORT METHODS

    @Override
    public boolean areRelated(int filmId, int userId) {
        final String checkQuery = "SELECT COUNT(*) FROM lists WHERE User_ID = ? AND Film_ID = ?";
        int count = getJdbcTemplate().queryForObject(checkQuery, new Object[]{
                userId, filmId
        }, Integer.class);
        return (count == 1);
    }

    @Override
    public boolean isWatched(int filmId, int userId) {
        final String watchedCheckQuery = "SELECT COUNT(*) FROM lists WHERE User_ID = ? AND Film_ID = ? AND Is_watched = TRUE";
        int watchedCount = getJdbcTemplate().queryForObject(watchedCheckQuery, new Object[]{
                userId, filmId
        }, Integer.class);
        return (watchedCount == 1);
    }

    @Override
    public boolean isPlanned(int filmId, int userId) {
        final String plannedCheckQuery = "SELECT COUNT(*) FROM Lists WHERE User_ID = ? AND Film_ID = ? AND Is_wished = TRUE ";
        int plannedCount = getJdbcTemplate().queryForObject(plannedCheckQuery, new Object[]{
                userId, filmId
        }, Integer.class);
        return (plannedCount == 1);
    }
}