package am.aca.dao.jdbc;

import am.aca.entity.Film;

import java.util.List;

/**
 * Interface for list of films DAO
 */

public interface ListDao {
    boolean areRelated(Film film, int userId);

    boolean updateWatched(Film film, int userId);

    boolean insertWatched(Film film, int userId, boolean isPublic);

    boolean addToWatched(Film film, boolean isPublic, int userId);

    boolean updatePlanned(Film film, int userId);

    boolean insertPlanned(Film film, int userId, boolean isPublic);

    boolean isWatched(Film film, int userId);

    boolean isPlanned(Film film, int userId);

    boolean resetWatched(Film film, int userId);

    boolean removeFilm(Film film, int userId);

    boolean addToWished(Film film, boolean isPublic, int userId);

    boolean resetPlanned(Film film, int userId);

    boolean removeFromWatched(Film film, int userId);

    boolean removeFromPlanned(Film film, int userId);

    List showOwnWatched(int userId);

    List showOwnPlanned(int userId);

    List showOthersWatched(int userId);

    List showOthersPlanned(int userId);

    boolean changePrivacy (Film film, int userId, boolean isPublic);

}
