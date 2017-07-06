package am.aca.orgflix.dao;

import am.aca.orgflix.entity.Film;

import java.util.List;

/**
 * Interface for list of films DAO
 */

public interface ListDao {
    // CREATE

    /**
     * Marks the film with the given ID as watched by the user
     * with the given ID with the given privacy
     *
     * @param filmId   ID of the given film
     * @param userId   ID of the given user
     * @param isPublic the privacy of the listed item
     * @return true if the association is successfully created, false otherwise
     */
    boolean insertWatched(int filmId, int userId, boolean isPublic);

    /**
     * Marks the film with the given ID as planned by the user
     * with the given ID with the given privacy
     *
     * @param filmId   ID of the given film
     * @param userId   ID of the given user
     * @param isPublic the privacy of the listed item
     * @return true if the association is successfully created, false otherwise
     */
    boolean insertPlanned(int filmId, int userId, boolean isPublic);

    // RETRIEVE

    /**
     * Retrieves the list of all films marked as watched by the current user with pagination
     *
     * @param userId current user's ID
     * @param page   index from desired pagination
     * @return the list of all films marked as watched by the current user
     */
    List<Film> showOwnWatched(int userId, int page);

    /**
     * Retrieves the list of all films marked as planned by the current user with pagination
     *
     * @param userId current user's ID
     * @param page   index from desired pagination
     * @return the list of all films marked as planned by the current user
     */
    List<Film> showOwnPlanned(int userId, int page);

    /**
     * Retrieves the list of all films marked as watched by the selected user with pagination
     *
     * @param userId selected user's ID
     * @param page   index from desired pagination
     * @return the list of all films marked as watched by the selected user
     */
    List<Film> showOthersWatched(int userId, int page);

    /**
     * Retrieves the list of all films marked as planned by the selected user with pagination
     *
     * @param userId selected user's ID
     * @param page   index from desired pagination
     * @return the list of all films marked as planned by the selected user
     */
    List<Film> showOthersPlanned(int userId, int page);

    // UPDATE

    /**
     * Marks the film already associated as planned with the given user also as watched
     *
     * @param filmId the ID of the selected film
     * @param userId the ID of the selected user
     * @return true if the association is successfully created, false otherwise
     */
    boolean updateWatched(int filmId, int userId);

    /**
     * Marks the film already associated as watched with the given user also as planned
     *
     * @param filmId the ID of the selected film
     * @param userId the ID of the selected user
     * @return true if the association is successfully created, false otherwise
     */
    boolean updatePlanned(int filmId, int userId);

    /**
     * Changes the privacy of the association of the given film and the given user to the given privacy
     *
     * @param film     the film to be updated
     * @param userId   the ID of the user whose list is to be updated
     * @param isPublic the new status which the association is to be updated to
     * @return true if the association is successfully updated, false otherwise
     */
    boolean changePrivacy(Film film, int userId, boolean isPublic);

    // DELETE

    /**
     * Sets the "watched" association of the given film and the given user as false in DB
     * but does not delete the association
     *
     * @param filmId the ID of the given film
     * @param userId the ID of the given user
     * @return true if the association is successfully updated, false otherwise
     */
    boolean resetWatched(int filmId, int userId);

    /**
     * Sets the "planned" association of the given film and the given user as false in DB
     * but does not delete the association
     *
     * @param filmId the ID of the given film
     * @param userId the ID of the given user
     * @return true if the association is successfully updated, false otherwise
     */
    boolean resetPlanned(int filmId, int userId);

    /**
     * Deletes all associations of the given user and the given film
     *
     * @param filmId the ID of the given film
     * @param userId the ID of the given user
     * @return true if the association is successfully deleted, false otherwise
     */
    boolean removeFilm(int filmId, int userId);

    // SUPPORT METHODS

    /**
     * Checks whether or not the given film is associated with the user in as "watched" or "planned"
     *
     * @param filmId the ID of the given film
     * @param userId the ID of the given user
     * @return true if the desired association exists, false otherwise
     */
    boolean areRelated(int filmId, int userId);

    /**
     * Checks whether or not the given film is associated with the user in as "watched"
     *
     * @param filmId the ID of the given film
     * @param userId the ID of the given user
     * @return true if the desired association exists, false otherwise
     */
    boolean isWatched(int filmId, int userId);

    /**
     * Checks whether or not the given film is associated with the user in as "planned"
     *
     * @param filmId the ID of the given film
     * @param userId the ID of the given user
     * @return true if the desired association exists, false otherwise
     */
    boolean isPlanned(int filmId, int userId);

    /**
     * Provide the current total number of watched films of the selected user
     *
     * @param userId id of the current user
     * @return the current total number of watched films
     */
    int totalNumberOfWatched(int userId);

    /**
     * Provide the current total number of wished films of the selected user
     *
     * @param userId id of the current user
     * @return the current total number of wished films
     */
    int totalNumberOfPlanned(int userId);

}
