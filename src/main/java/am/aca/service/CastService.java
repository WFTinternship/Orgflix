package am.aca.service;

import am.aca.entity.Cast;
import am.aca.entity.Film;

import java.util.List;

/**
 * Created by David on 5/27/2017
 */
public interface CastService {
    Cast addCast(String cast, boolean hasOscar);

    Cast addCast(String cast);

    boolean addCastToFilm(Cast cast, Film film);

    boolean addCastToFilm(int castId, int filmId);

    boolean editCast(Cast cast);

    List listCasts();

    List listFilmsIdByCast(int castId);
}
