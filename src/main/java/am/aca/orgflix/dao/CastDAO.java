package am.aca.orgflix.dao;

import am.aca.orgflix.entity.Cast;

import java.util.List;

/**
 * Interface for actor DAO
 */
public interface CastDAO {
    // CREATE

    /**
     * Add new actor to DB
     *
     * @param cast the actor object to be added
     * @return true if actor is added, otherwise false
     */
    boolean addCast(Cast cast);

    /**
     * Add an association of actor with film in DB
     *
     * @param cast   the cast with which the provided film will be associated
     * @param filmId the id of film which will be associated with provided cast
     * @return true if the new association of cast to film was successful, otherwise false
     */
    boolean addCastToFilm(Cast cast, int filmId);

    // READ

    /**
     * List all the casts currently in DB
     *
     * @return List of all casts currently in DB
     */
    List<Cast> listCast();

    /**
     * List all the casts assigned to the film with provided id
     *
     * @param filmId the id
     * @return List of all casts assigned to the specific film
     */
    List<Cast> getCastsByFilm(int filmId);

    // UPDATE

    /**
     * Update the fields of the provided actor object in DB
     *
     * @param cast the actor which fields will be updated in DB
     * @return true if the update was successful, otherwise false
     */
    boolean editCast(Cast cast);

    // DELETE

    /**
     * Remove the provided actor from DB
     *
     * @param cast the actor which are being removed
     * @return true if the actor was removed, otherwise false
     */
    boolean remove(Cast cast);

    // Support methods

    /**
     * Check whether the given film has actors with the given actors in their cast
     *
     * @param actorId the ID of the given cast member
     * @param filmId  the ID of the given film
     * @return true if actor with the given ID is present in the cast list of the
     * given film, false otherwise
     */
    boolean isStarringIn(int actorId, int filmId);

    /**
     * Check if the given cast is present in the DB
     *
     * @param cast the cast to be found in the DB
     * @return true if the given cast is in the DB, false otherwise
     */
    boolean exists(Cast cast);
}
