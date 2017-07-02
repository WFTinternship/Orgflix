package am.aca.orgflix.dao;

import am.aca.orgflix.entity.Film;

import java.util.List;

/**
 * Interface for list of films DAO
 */

public interface ListDao {
    // CREATE

    boolean insertWatched(Film film, int userId, boolean isPublic);

    boolean insertWatched(int filmId, int userId, boolean isPublic);

    boolean insertPlanned(Film film, int userId, boolean isPublic);

    boolean insertPlanned(int filmId, int userId, boolean isPublic);

    // RETRIEVE

    List<Film> showOwnWatched(int userId, int page);

    List<Film> showOwnPlanned(int userId, int page);

    List<Film> showOthersWatched(int userId, int page);

    List<Film> showOthersPlanned(int userId, int page);

    // UPDATE

    boolean updateWatched(int filmId, int userId);

    boolean updatePlanned(int filmId, int userId);

    boolean changePrivacy (Film film, int userId, boolean isPublic);

    // DELETE

    boolean resetWatched(int filmId, int userId);

    boolean resetPlanned(int filmId, int userId);

    boolean removeFilm(int filmId, int userId);

    // SUPPORT METHODS

    boolean areRelated(int filmId, int userId);

    boolean isWatched(int filmId, int userId);

    boolean isPlanned(int filmId, int userId);

    int totalNumberOfWatched(int userId);

    int totalNumberOfWished(int userId);

}
