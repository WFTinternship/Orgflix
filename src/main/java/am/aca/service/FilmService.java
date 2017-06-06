package am.aca.service;

import am.aca.entity.Director;
import am.aca.entity.Film;
import am.aca.entity.Genre;

import java.util.List;

/**
 * Created by David on 5/28/2017
 */
public interface FilmService {
    boolean addFilm(Film film);
    boolean editFilm(Film film);
    Film getFilmById(int id);
    List<Film> getFilmsByDirector(Director director);
    List<Film> getFilmsByDirector(int directorId);
    boolean rateFilm(int filmId, int starType);
    boolean addGenreToFilm(Genre genre, Film film);
    boolean addGenreToFilm(Genre genre, int filmId);
}
