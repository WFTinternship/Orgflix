package am.aca.orgflix.dao;

import am.aca.orgflix.entity.Film;
import am.aca.orgflix.entity.Genre;

import java.util.List;

/**
 * Interface for Film DAO
 */
public interface FilmDAO {
    // Create
    boolean addFilm(Film film);

    boolean addGenreToFilm(Genre genre, int filmId);

    boolean rateFilm(int filmId, int starType);

    // READ
    Film getFilmById(int id);

    List<Film> getFilmsList(int startIndex);

    List<Film> getFilmsByGenre(Genre genre);

    List<Film> getFilmsByCast(int actorId);

    double getRating(int filmId);

    int totalNumberOfFilms();

    List<Film> getFilteredFilms(String title, int startYear, int finishYear, String hasOscar, String director, String castId, String genreId);

    // UPDATE
    boolean editFilm(Film film);

    // DELETE
    boolean resetRelationCasts(Film film);

    boolean resetRelationGenres(Film film);

    boolean remove(Film film);
}
