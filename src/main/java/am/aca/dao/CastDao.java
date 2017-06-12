package am.aca.dao;

import am.aca.entity.Cast;
import am.aca.entity.Film;

import java.util.List;

/**
 * Created by David on 5/27/2017
 */
public interface CastDao {
    Cast addCast(String name, boolean hasOscar);

    Cast addCast(String name);

    boolean addCastToFilm(Cast cast, Film film);

    boolean addCastToFilm(int actorId, int filmId);

    boolean editCast(Cast cast);

    List<Cast> listCast();

    List<Integer> listFilmsIdByCast(int actorId);
}
