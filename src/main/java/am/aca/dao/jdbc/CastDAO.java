package am.aca.dao.jdbc;

import am.aca.entity.Cast;
import am.aca.entity.Film;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created by David on 5/27/2017
 */
public interface CastDAO {
    // Create
    Cast addCast(String name, boolean hasOscar);
    Cast addCast(String name);
    boolean addCastToFilm(Cast cast, Film film);
    boolean addCastToFilm(int actorId, int filmId);

    //Read
    List listCast();
    List listFilmsByCast(int actorId);

    // Update
    boolean editCast(Cast cast);
}
