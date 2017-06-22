package am.aca.service;

import am.aca.entity.Cast;
import am.aca.entity.Film;

import java.util.List;

/**
 * Created by David on 5/27/2017
 */
public interface CastService {
    // Create
//    Cast addCast(String cast);

//    Cast addCast(String cast, boolean hasOscar);

    boolean addCast(Cast cast);

    // Retrieve
    List<Cast> listCasts();

    List<Film> listFilmsByCast(int castId);

    // Update
    boolean editCast(Cast cast);

    boolean addCastToFilm(Cast cast, Film film);

    boolean addCastToFilm(int castId, int filmId);
}
