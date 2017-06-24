package am.aca.dao.jdbc;

import am.aca.entity.Cast;
import am.aca.entity.Film;

import java.util.List;

/**
 * Interface for actor DAO
 */
public interface CastDAO {
    // CREATE
    boolean addCast(Cast cast);
    boolean addCastToFilm(Cast cast, Film film);
    boolean addCastToFilm(Cast cast, int filmId);

    // READ
    List<Cast> listCast();

    // UPDATE
    boolean editCast(Cast cast);

    // DELETE
    boolean remove(Cast cast);

    // Support methods
    boolean isStarringIn (int actorId, int filmId);
    boolean exists(Cast cast);
}
