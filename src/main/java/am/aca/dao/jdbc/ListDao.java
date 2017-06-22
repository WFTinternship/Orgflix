package am.aca.dao.jdbc;

import am.aca.entity.Film;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created by karine on 5/31/2017
 */

public interface ListDao {
    boolean areRelated(Film film, int userId);

    void updateWatched(Film film, int userId);

    void insertWatched(Film film, int userId, boolean isPublic);

    boolean addToWatched(Film film, boolean isPublic, int User_ID);

    void updatePlanned(Film film, int userId);

    void insertPlanned(Film film, int userId, boolean isPublic);

    boolean isWatched(Film film, int userId);

    boolean isPlanned(Film film, int userId);

    void resetWatched(Film film, int userId);

    void removeFilm(Film film, int userId);

    boolean addToWished(Film film, boolean isPublic, int User_ID);

    void resetPlanned(Film film, int userId);

    boolean removeFromWatched(Film film, int User_ID);

    boolean removeFromWished(Film film, int User_ID);

    List showOwnWatched(int User_ID);

    List showOwnWished(int User_ID);

    List showOthersWatched(int User_ID);

    List showOthersWished(int User_ID);

    void setDataSource(DataSource dataSource);
}
