package am.aca.service;

import am.aca.entity.*;

import java.util.List;

/**
 * Interface for
 */
public interface FilmService {

    boolean addFilm(Film film);

    List<Film> getFilmsList(int startIndex);

    Film getFilmById(int id);

    List<Film> getFilmsByCast(Cast cast);

    List<Film> getFilmsByCast(int castId);

    List<Film> getFilmsByGenre(Genre genre);

    double getRating(int filmId);

    double getRating(Film film);

    int totalNumberOfFilms();

    boolean editFilm(Film film);

    boolean rateFilm(int filmId, int starType);

    boolean addGenreToFilm(Genre genre, Film film);

    boolean addGenreToFilm(Genre genre, int filmId);
}
