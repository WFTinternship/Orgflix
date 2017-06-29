package am.aca.service;

import am.aca.entity.Film;

import java.util.List;

/**
 * Interface for film lists service layer
 */
public interface ListService {
    boolean addToWatched(int filmId, boolean isPublic, int userId);

    boolean addToPlanned(int filmId, boolean isPublic, int userId);

    boolean removeFromWatched(int filmId, int userId);

    boolean removeFromPlanned(int filmId, int userId);

    List<Film> showOwnWatched(int userId);

    List<Film> showOwnPlanned(int userId);

    List<Film> showOthersWatched(int userId);

    List<Film> showOthersPlanned(int userId);

    boolean makePrivate (int userId, Film film);

    boolean makePublic (int userId, Film film);
}
