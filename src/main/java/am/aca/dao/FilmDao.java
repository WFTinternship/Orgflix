package am.aca.dao;

import am.aca.entity.*;

import java.util.List;

/**
 * Created by David on 5/28/2017
 */
public interface FilmDao {
    boolean addFilm(Film film);

    boolean editFilm(Film film);

    Film getFilmById(int id);

    List<Film> getFilmsByCast(Cast cast);

    List<Film> getFilmsByCast(int directorId);

    List<Film> getFilmsList(int startIndex);

    boolean rateFilm(int filmId, int starType);

    boolean addGenreToFilm(Genre genre, Film film);

    boolean addGenreToFilm(Genre genre, int filmId);

    boolean addCastToFilm(Cast cast, Film film);

    boolean addCastToFilm(Cast cast, int filmId);
}
