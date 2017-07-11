package am.aca.orgflix.service;

import am.aca.orgflix.entity.Cast;
import am.aca.orgflix.entity.Film;

import java.util.List;

/**
 * Interface for actor service layer
 */
public interface CastService {

    /**
     * Creates a cast member
     *
     * @param cast the object to be created
     * @return true if the given cast is successfully created, false otherwise
     */
    boolean addCast(Cast cast);

    /**
     * Shows all actors already created
     *
     * @return the List object of all actors already created
     */
    List<Cast> listCasts();


    /**
     * Show all films starring the given actor
     *
     * @param castId the ID of the given actor
     * @return the List object of all films starring the given actor
     */
    List<Film> listFilmsByCast(int castId);

    /**
     * Show all actors starring in the given film
     *
     * @param filmId the ID of the given film
     * @return the List object of all actors starring in the given film
     */
    List<Cast> getCastsByFilm(int filmId);

    /**
     * Updates features of the given actor
     *
     * @param cast the object to replace the previous version
     * @return true if successfully updated, false otherwise
     */
    Cast getCastById(int castId);

    boolean editCast(Cast cast);

    /**
     * Add the given actor to the given film's cast
     *
     * @param filmId the ID of the film to be updated
     * @param cast   the cast to be added to the given film
     * @return true if the film is successfully updated, false otherwise
     */
    boolean addCastToFilm(Cast cast, int filmId);
}
