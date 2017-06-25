package am.aca.dao.jdbc;

import am.aca.entity.Film;

import java.util.List;

/**
 * Interface for list of films DAO
 */

public interface ListDao {
    // CREATE

    boolean insertWatched(Film film, int userId, boolean isPublic);

    boolean insertPlanned(Film film, int userId, boolean isPublic);

    // RETRIEVE

    List<Film> showOwnWatched(int userId);

    List<Film> showOwnPlanned(int userId);

    List<Film> showOthersWatched(int userId);

    List<Film> showOthersPlanned(int userId);

    // UPDATE

    boolean updateWatched(Film film, int userId);

    boolean updatePlanned(Film film, int userId);

    boolean changePrivacy (Film film, int userId, boolean isPublic);

    // DELETE

    boolean resetWatched(Film film, int userId);

    boolean resetPlanned(Film film, int userId);

    boolean removeFilm(Film film, int userId);

    // SUPPORT METHODS

    boolean areRelated(Film film, int userId);

    boolean isWatched(Film film, int userId);

    boolean isPlanned(Film film, int userId);

}
