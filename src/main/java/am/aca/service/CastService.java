package am.aca.service;

import am.aca.entity.Cast;
import am.aca.entity.Film;

import java.util.List;

/**
 * Interface for actor service layer
 */
public interface CastService {

    boolean addCast(Cast cast);

    List<Cast> listCasts();

    List<Film> listFilmsByCast(int castId);

    boolean editCast(Cast cast);

    boolean addCastToFilm(Cast cast, int filmId);
}
