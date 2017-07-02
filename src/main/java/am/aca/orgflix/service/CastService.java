package am.aca.orgflix.service;

import am.aca.orgflix.entity.Cast;
import am.aca.orgflix.entity.Film;

import java.util.List;

/**
 * Interface for actor service layer
 */
public interface CastService {

    boolean addCast(Cast cast);

    List<Cast> listCasts();

    List<Film> listFilmsByCast(int castId);
    List<Cast> getCastsByFilm(int filmId);

    boolean editCast(Cast cast);

    boolean addCastToFilm(Cast cast, int filmId);
}
