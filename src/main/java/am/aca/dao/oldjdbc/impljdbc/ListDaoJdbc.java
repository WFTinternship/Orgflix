package am.aca.dao.jdbc.impljdbc;

<<<<<<< Updated upstream:src/main/java/am/aca/dao/impljdbc/ListDaoJdbc.java
import am.aca.dao.ListDao;
=======
import am.aca.dao.DaoException;
import am.aca.dao.jdbc.ListDao;
>>>>>>> Stashed changes:src/main/java/am/aca/dao/jdbc/impljdbc/ListDaoJdbc.java
import am.aca.entity.Film;
import am.aca.util.ConnType;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created by karine on 6/3/2017
 */
public class ListDaoJdbc extends DaoJdbc implements ListDao {

    private JdbcTemplate jdbcTemplate;

    public ListDaoJdbc(){
        super();
    }

    public ListDaoJdbc(ConnType connType){
        super(connType);
    }

    @Override
    public boolean areRelated(Film film, int userId) {
        final String checkQuery = "SELECT COUNT(*) FROM lists WHERE User_ID = ? AND Film_ID = ?";
        int count = jdbcTemplate.queryForInt(checkQuery, new Object[] {
                userId, film.getId()
        });
        return (count == 1);
    }

    @Override
    public void updateWatched(Film film, int userId) {
        final String updateQuery = "UPDATE lists SET Is_watched = TRUE " +
                "WHERE User_ID = ? AND Film_ID = ?";
        jdbcTemplate.update(updateQuery, new Object[] {
                userId, film.getId()
        });
    }

    @Override
    public void updatePlanned (Film film, int userId) {
        final String updateQuery = "UPDATE lists SET Is_wished = TRUE " +
                "WHERE User_ID = ? AND Film_ID = ?";
        jdbcTemplate.update(updateQuery, new Object[] {
                userId, film.getId()
        });
    }

    @Override
    public void insertPlanned (Film film, int userId, boolean isPublic) {
        final String insertQuery = "INSERT INTO Lists(User_ID, Film_ID, Is_wished, Is_public) VALUES (?, ?, TRUE, ?)";
        jdbcTemplate.update(insertQuery, new Object[] {
                userId, film.getId(), isPublic
        });
    }

    @Override
    public void insertWatched(Film film, int userId, boolean isPublic) {
        final String insertQuery = "INSERT INTO Lists(User_ID, Film_ID, Is_watched, Is_public) VALUES (?, ?, TRUE, ?)";
        jdbcTemplate.update(insertQuery, new Object[] {
                userId, film.getId(), isPublic
        });
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
        int watchedCount = jdbcTemplate.queryForInt(watchedCheckQuery, new Object[] {
                userId, film.getId()
        });
        return (watchedCount == 1);
    }

    @Override
    public boolean isPlanned (Film film, int userId) {
        final String plannedCheckQuery = "SELECT COUNT(*) FROM Lists WHERE User_ID = ? AND Film_ID = ? AND Is_wished = TRUE ";
        int plannedCount = jdbcTemplate.queryForInt(plannedCheckQuery, new Object[] {
                userId, film.getId()
        });
        return (plannedCount == 1);
    }

    @Override
    public void resetWatched (Film film, int userId) {
        final String updateQuery = "UPDATE lists SET Is_watched = FALSE WHERE User_ID = ? AND Film_ID = ?";
        jdbcTemplate.update(updateQuery, new Object[] {
                userId, film.getId()
        });
    }

    @Override
    public void resetPlanned (Film film, int userId) {
        final String updateQuery = "UPDATE lists SET Is_wished = FALSE WHERE User_ID = ? AND Film_ID = ?";
        jdbcTemplate.update(updateQuery, new Object[] {
                userId, film.getId()
        });
    }

    @Override
    public void removeFilm (Film film, int userId) {
        final String deleteQuery = "DELETE FROM lists WHERE User_ID = ? AND Film_ID = ?";
        jdbcTemplate.update(deleteQuery, new Object[] {
                userId, film.getId()
        });
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
    public boolean removeFromWished(Film film, int userId) {
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
        return jdbcTemplate.queryForList(query, new Object[] {
                userId
        });
    }


    @Override
    public List showOwnWished(int userId) {
        final String query = "SELECT lists.Film_ID FROM lists INNER JOIN films " +
                "ON lists.Film_ID = films.ID WHERE lists.User_ID = ? AND Is_wished = TRUE ";
        return jdbcTemplate.queryForList(query, new Object[] {
                userId
        });
    }

    @Override
    public List showOthersWatched(int userId) {
        final String query = "SELECT lists.Film_ID FROM lists INNER JOIN films " +
                "ON lists.Film_ID = films.ID WHERE lists.User_ID = ? AND Is_watched = TRUE AND Is_public = TRUE ";
        return jdbcTemplate.queryForList(query, new Object[] {
                userId
        });
    }

    @Override
    public List showOthersWished(int userId) {
        final String query = "SELECT lists.Film_ID FROM lists INNER JOIN films " +
                "ON lists.Film_ID = films.ID WHERE lists.User_ID = ? AND Is_wished = TRUE AND Is_public = TRUE ";
        return jdbcTemplate.queryForList(query, new Object[] {
                userId
        });
    }

    @Override
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

}
