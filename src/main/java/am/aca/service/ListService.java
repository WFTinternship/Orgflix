package am.aca.service;

import am.aca.entity.Film;

import java.util.List;

/**
 * Created by karine on 5/31/2017
 */
public interface ListService {
    boolean addToWatched(Film film, boolean isPublic, int User_ID);

    boolean addToWished(Film film, boolean isPublic, int User_ID);

    boolean removeFromWatched(Film film, int User_ID);

    boolean removeFromWished(Film film, int User_ID);

    List<Film> showOwnWatched(int User_ID);

    List<Film> showOwnWished(int User_ID);

    List<Film> showOthersWatched(int User_ID);

    List<Film> showOthersWished(int User_ID);
}
