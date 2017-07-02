package am.aca.orgflix.service;

import am.aca.orgflix.entity.Film;
import am.aca.orgflix.entity.Genre;

import java.util.List;

/**
 * Interface for film service layer
 */
public interface FilmService {

    boolean addFilm(Film film);

    List<Film> getFilmsList(int startIndex);

    Film getFilmById(int id);

    List<Film> getFilmsByCast(int castId);

    List<Film> getFilmsByGenre(Genre genre);

    double getRating(int filmId);

    int totalNumberOfFilms();

    List<Film> getFilteredFilms(String title, int startYear, int finishYear, boolean hasOscar, String director, int castId, Genre genre);

    boolean editFilm(Film film);

    boolean rateFilm(int filmId, int starType);

    boolean addGenreToFilm(Genre genre, int filmId);
}
