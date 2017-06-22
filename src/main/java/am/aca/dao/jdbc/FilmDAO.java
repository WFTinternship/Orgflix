package am.aca.dao.jdbc;

import am.aca.entity.Cast;
import am.aca.entity.Film;
import am.aca.entity.Genre;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created by David on 5/28/2017
 */
public interface FilmDAO {
    boolean addFilm(Film film);

    boolean editFilm(Film film);

    Film getFilmById(int id);

    List getFilmsList(int startIndex);

    List getFilmsByCast(Cast cast);

    List getFilmsByCast(int actorId);

    List getFilmsByGenre(Genre genre);

    boolean rateFilm(int filmId, int starType);

    boolean addGenreToFilm(Genre genre, Film film);

    boolean addGenreToFilm(Genre genre, int filmId);

    boolean addCastToFilm(Cast cast, Film film);

    boolean addCastToFilm(Cast cast, int filmId);

    double getRating(int filmId);

    double getRating(Film film);

    int totalNumberOfFilms();

    void resetRelationCasts(Film film);

    void resetRelationGenres(Film film);
}
