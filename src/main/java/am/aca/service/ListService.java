package am.aca.service;

import am.aca.entity.Film;

import java.util.List;

/**
 * Created by karine on 5/31/2017
 */
public interface ListService {
    void addToWatched(Film film, boolean isPublic, int userId);

    void addToPlanned(Film film, boolean isPublic, int userId);

    void removeFromWatched(Film film, int userId);

    void removeFromPlanned(Film film, int userId);

    List showOwnWatched(int userId);

    List showOwnPlanned(int userId);

    List showOthersWatched(int userId);

    List showOthersPlanned(int userId);

    void makePrivate (int userId, Film film);

    void makePublic (int userId, Film film);
}
