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

    boolean addToWatched(Film film, boolean isPublic, int userId);

    void updatePlanned(Film film, int userId);

    void insertPlanned(Film film, int userId, boolean isPublic);

    boolean isWatched(Film film, int userId);

    boolean isPlanned(Film film, int userId);

    void resetWatched(Film film, int userId);

    void removeFilm(Film film, int userId);

    boolean addToWished(Film film, boolean isPublic, int userId);

    void resetPlanned(Film film, int userId);

    boolean removeFromWatched(Film film, int userId);

    boolean removeFromPlanned(Film film, int userId);

    List showOwnWatched(int userId);

    List showOwnPlanned(int userId);

    List showOthersWatched(int userId);

    List showOthersPlanned(int userId);

    void changePrivacy (Film film, int userId, boolean isPublic);

}
