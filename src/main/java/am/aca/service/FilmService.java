package am.aca.service;

import am.aca.entity.Cast;
import am.aca.entity.Film;
import am.aca.entity.Genre;

import java.util.List;

/**
 * Created by David on 5/28/2017
 */
public interface FilmService {
    // Create
    boolean addFilm(Film film);

    // Retrieve
    List<Film> getFilmsList(int startIndex);

    Film getFilmById(int id);

    List<Film> getFilmsByCast(Cast cast);

    List<Film> getFilmsByCast(int castId);

    List<Film> getFilmsByGenre(Genre genre);

    double getRating(int filmId);

    double getRating(Film film);

    int totalNumberOfFilms();

    // Update
    void editFilm(Film film);

    boolean rateFilm(int filmId, int starType);

    boolean addGenreToFilm(Genre genre, Film film);

    boolean addGenreToFilm(Genre genre, int filmId);

    boolean addCastToFilm(Cast cast, Film film);

    boolean addCastToFilm(Cast cast, int filmId);
}
