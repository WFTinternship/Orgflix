package am.aca.orgflix.service;

import am.aca.orgflix.entity.Cast;

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
    boolean add(Cast cast);

    /**
     * Add all casts in the array to the film
     * @param filmId the id of film to witch the casts will be added
     * @param actorIds array of ids for each cast to be added
     * @return list of cast objects added to the film
     */
    List<Cast> addCastsToFilm(int filmId, int[] actorIds);

    /**
     * Shows all actors already created
     *
     * @return the List object of all actors already created
     */
    List<Cast> getAll();

    /**
     * Show all actors starring in the given film
     *
     * @param filmId the ID of the given film
     * @return the List object of all actors starring in the given film
     */
    List<Cast> getByFilm(int filmId);

    /**
     * Retrieves the cast having the given ID
     *
     * @param castId the ID of the desired cast
     * @return the cast object having the given ID, null if not found
     */
    Cast getById(int castId);

    /**
     * Updates features of the given actor
     *
     * @param cast the object to replace the previous version
     * @return true if successfully updated, false otherwise
     */
    boolean edit(Cast cast);

    /**
     * Add the given actor to the given film's cast
     *
     * @param filmId the ID of the film to be updated
     * @param cast   the cast to be added to the given film
     * @return true if the film is successfully updated, false otherwise
     */
    boolean addToFilm(Cast cast, int filmId);
}
