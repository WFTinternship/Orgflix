package am.aca.orgflix.service;

import am.aca.orgflix.entity.Film;

import java.util.List;

/**
 * Interface for film lists service layer
 */
public interface ListService {
    boolean addToWatched(int filmId, boolean isPublic, int userId);

    boolean addToPlanned(int filmId, boolean isPublic, int userId);

    boolean removeFromWatched(int filmId, int userId);

    boolean removeFromPlanned(int filmId, int userId);

    List<Film> showOwnWatched(int userId, int page);

    List<Film> showOwnPlanned(int userId, int page);

    List<Film> showOthersWatched(int userId, int page);

    List<Film> showOthersPlanned(int userId, int page);

    boolean makePrivate (int userId, Film film);

    boolean makePublic (int userId, Film film);

    int totalNumberOfWatched(int userId, boolean isWatched);

}
