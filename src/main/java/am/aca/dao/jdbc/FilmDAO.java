package am.aca.dao.jdbc;

import am.aca.entity.Cast;
import am.aca.entity.Film;
import am.aca.entity.Genre;

import java.util.List;

/**
 * Created by David on 5/28/2017
 */
public interface FilmDAO {
    // Create
    boolean addFilm(Film film);

    //Retrieve

    Film getFilmById(int id);

    List<Film> getFilmsList(int startIndex);

    List getFilmsByCast(Cast cast);

    List getFilmsByCast(int actorId);

    List getFilmsByGenre(Genre genre);

    double getRating(int filmId);

    double getRating(Film film);

    int totalNumberOfFilms();

    // Update
    boolean editFilm(Film film);

    boolean rateFilm(int filmId, int starType);

    boolean addGenreToFilm(Genre genre, Film film);

    boolean addGenreToFilm(Genre genre, int filmId);

    boolean addCastToFilm(Cast cast, Film film);

    boolean addCastToFilm(Cast cast, int filmId);

    void resetRelationCasts(Film film);

    void resetRelationGenres(Film film);
}
