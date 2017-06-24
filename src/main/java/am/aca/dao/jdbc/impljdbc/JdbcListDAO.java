package am.aca.dao.jdbc.impljdbc;

import am.aca.dao.jdbc.BaseDAO;
import am.aca.dao.jdbc.ListDao;
import am.aca.entity.Film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

/**
 * DAO layer for list of film related methods
 */
@Repository
public class JdbcListDAO extends BaseDAO implements ListDao {

    @Autowired
    public JdbcListDAO(DataSource dataSource) {
        super(JdbcUserDAO.class);
        this.setDataSource(dataSource);
    }

    @Override
    public boolean areRelated(Film film, int userId) {
        final String checkQuery = "SELECT COUNT(*) FROM lists WHERE User_ID = ? AND Film_ID = ?";
        int count = getJdbcTemplate().queryForObject(checkQuery, new Object[] {
                userId, film.getId()
        }, Integer.class);
        return (count == 1);
    }

    @Override
    public boolean updateWatched(Film film, int userId) {
        final String updateQuery = "UPDATE lists SET Is_watched = TRUE " +
                "WHERE User_ID = ? AND Film_ID = ?";
        return (getJdbcTemplate().update(updateQuery, userId, film.getId()) == 1);
    }

    @Override
    public boolean updatePlanned (Film film, int userId) {
        final String updateQuery = "UPDATE lists SET Is_wished = TRUE " +
                "WHERE User_ID = ? AND Film_ID = ?";
        return (getJdbcTemplate().update(updateQuery, userId, film.getId()) == 1);
    }

    @Override
    public boolean insertPlanned (Film film, int userId, boolean isPublic) {
        final String insertQuery = "INSERT INTO Lists(User_ID, Film_ID, Is_wished, Is_public) VALUES (?, ?, TRUE, ?)";
        return (getJdbcTemplate().update(insertQuery, userId, film.getId(), isPublic) == 1);
    }

    @Override
    public boolean insertWatched(Film film, int userId, boolean isPublic) {
        final String insertQuery = "INSERT INTO Lists(User_ID, Film_ID, Is_watched, Is_public) VALUES (?, ?, TRUE, ?)";
        return (getJdbcTemplate().update(insertQuery, userId, film.getId(), isPublic) == 1);
    }


    @Override
    public boolean addToWatched(Film film, boolean isPublic, int userId){
        if (areRelated(film, userId)){
            updateWatched(film, userId);
        }
        else insertWatched(film, userId, isPublic);


        return true; /////////////////////////////////CHANGE THIS
    }






    @Override
    public boolean addToWished(Film film, boolean isPublic, int userId) {
        if (areRelated(film, userId))
            updatePlanned(film, userId);
        else insertPlanned(film, userId, isPublic);
        return true;
    }


    @Override
    public boolean isWatched (Film film, int userId) {
        final String watchedCheckQuery = "SELECT COUNT(*) FROM lists WHERE User_ID = ? AND Film_ID = ? AND Is_watched = TRUE";
        int watchedCount = getJdbcTemplate().queryForObject(watchedCheckQuery, new Object[] {
                userId, film.getId()
        }, Integer.class);
        return (watchedCount == 1);
    }

    @Override
    public boolean isPlanned (Film film, int userId) {
        final String plannedCheckQuery = "SELECT COUNT(*) FROM Lists WHERE User_ID = ? AND Film_ID = ? AND Is_wished = TRUE ";
        int plannedCount = getJdbcTemplate().queryForObject(plannedCheckQuery, new Object[] {
                userId, film.getId()
        }, Integer.class);
        return (plannedCount == 1);
    }

    @Override
    public boolean resetWatched (Film film, int userId) {
        final String updateQuery = "UPDATE lists SET Is_watched = FALSE WHERE User_ID = ? AND Film_ID = ?";
        return (getJdbcTemplate().update(updateQuery, userId, film.getId()) == 1);
    }

    @Override
    public boolean resetPlanned (Film film, int userId) {
        final String updateQuery = "UPDATE lists SET Is_wished = FALSE WHERE User_ID = ? AND Film_ID = ?";
        return (getJdbcTemplate().update(updateQuery, userId, film.getId()) == 1);
    }

    @Override
    public boolean removeFilm (Film film, int userId) {
        final String deleteQuery = "DELETE FROM lists WHERE User_ID = ? AND Film_ID = ?";
        return (getJdbcTemplate().update(deleteQuery, userId, film.getId()) == 1);
    }
    @Override
    public boolean removeFromWatched(Film film, int userId) {
        if (!isWatched(film, userId))
            return false;
        if (isPlanned(film, userId)) {
            resetWatched(film, userId);
        }
        else removeFilm(film, userId);
        return true;
    }


    @Override
    public boolean removeFromPlanned(Film film, int userId) {
        if (!isPlanned(film, userId))
            return false;
        if (isWatched(film, userId))
            resetPlanned(film, userId);
        else removeFilm(film, userId);
        return true;
    }

    @Override
    public List showOwnWatched(int userId) {
        final String query = "SELECT lists.Film_ID FROM lists INNER JOIN films " +
                "ON lists.Film_ID = films.ID WHERE lists.User_ID = ? AND Is_watched = TRUE ";
        return getJdbcTemplate().queryForList(query, userId);
    }


    @Override
    public List showOwnPlanned(int userId) {
        final String query = "SELECT lists.Film_ID FROM lists INNER JOIN films " +
                "ON lists.Film_ID = films.ID WHERE lists.User_ID = ? AND Is_wished = TRUE ";
        return getJdbcTemplate().queryForList(query, userId);
    }

    @Override
    public List showOthersWatched(int userId) {
        final String query = "SELECT lists.Film_ID FROM lists INNER JOIN films " +
                "ON lists.Film_ID = films.ID WHERE lists.User_ID = ? AND Is_watched = TRUE AND Is_public = TRUE ";
        return getJdbcTemplate().queryForList(query, userId);
    }

    @Override
    public List showOthersPlanned(int userId) {
        final String query = "SELECT lists.Film_ID FROM lists INNER JOIN films " +
                "ON lists.Film_ID = films.ID WHERE lists.User_ID = ? AND Is_wished = TRUE AND Is_public = TRUE ";
        return getJdbcTemplate().queryForList(query, userId);
    }

    @Override
    public boolean changePrivacy(Film film, int userId, boolean isPublic) {
        final String query = "UPDATE lists SET Is_public = ? WHERE User_ID = ? AND Film_ID = ?;";
        return (getJdbcTemplate().update(query, isPublic, userId, film.getId()) == 1);
    }


}
