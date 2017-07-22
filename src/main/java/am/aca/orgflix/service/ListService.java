package am.aca.orgflix.service;

import am.aca.orgflix.entity.Film;

import java.util.List;

/**
 * Interface for film lists service layer
 */
public interface ListService {

    /**
     * Marks the given film as watched by the given user with the given privacy
     *
     * @param filmId   the ID of the film to be associated
     * @param isPublic the privacy of the given association
     * @param userId   the ID of the user to be associated
     * @return true if the association is successfully created, false otherwise
     */
    boolean addToWatched(int filmId, boolean isPublic, int userId);

    /**
     * Marks the given film as planned by the given user with the given privacy
     *
     * @param filmId   the ID of the film to be associated
     * @param isPublic the privacy of the given association
     * @param userId   the ID of the user to be associated
     * @return true if the association is successfully created, false otherwise
     */
    boolean addToPlanned(int filmId, boolean isPublic, int userId);

    /**
     * Removes the "watched" association of the given film and the given user
     *
     * @param filmId the ID of the film to be reset
     * @param userId the ID of the user to reset the film
     * @return true if the association is successfully reset, false otherwise
     */
    boolean removeFromWatched(int filmId, int userId);

    /**
     * Removes the "planned" association of the given film and the given user
     *
     * @param filmId the ID of the film to be reset
     * @param userId the ID of the user to reset the film
     * @return true if the association is successfully reset, false otherwise
     */
    boolean removeFromPlanned(int filmId, int userId);

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
    List<Film> showOthersWatched(int otherUserId, int page);

    /**
     * Retrieves the list of all films marked as planned by the selected user with pagination
     *
     * @param userId selected user's ID
     * @param page   index from desired pagination
     * @return the list of all films marked as planned by the selected user
     */
    List<Film> showOthersPlanned(int userId, int page);

    /**
     * Updates the privacy of the given film in the given user's lists to private
     *
     * @param userId the ID of the user to be updated
     * @param film   the film to be updated in privacy
     * @return true if the privacy is successfully updated, false otherwise
     */
    boolean makePrivate(int userId, Film film);

    /**
     * Updates the privacy of the given film in the given user's lists to public
     *
     * @param userId the ID of the user to be updated
     * @param film   the film to be updated in privacy
     * @return true if the privacy is successfully updated, false otherwise
     */
    boolean makePublic(int userId, Film film);

    /**
     * Retrieves the total number of films associated with the given user in the given way
     *
     * @param userId    the ID of the user whose list is to be retrieved
     * @param isWatched the indicator of the desired list,
     *                  true if watched films are to be retrieved,
     *                  false if planned films are to be retrieved
     * @return the number of films associated in the given way with the given user
     */
    int totalNumberOfFilmsInAList(int userId, boolean isWatched);

}
