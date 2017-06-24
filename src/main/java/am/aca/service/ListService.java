package am.aca.service;

import am.aca.entity.Film;

import java.util.List;

/**
 * Interface for film lists service layer
 */
public interface ListService {
    boolean addToWatched(Film film, boolean isPublic, int userId);

    boolean addToPlanned(Film film, boolean isPublic, int userId);

    boolean removeFromWatched(Film film, int userId);

    boolean removeFromPlanned(Film film, int userId);

    List showOwnWatched(int userId);

    List showOwnPlanned(int userId);

    List showOthersWatched(int userId);

    List showOthersPlanned(int userId);

    boolean makePrivate (int userId, Film film);

    boolean makePublic (int userId, Film film);
}
