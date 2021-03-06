package am.aca.orgflix.service;

import am.aca.orgflix.entity.Film;
import am.aca.orgflix.entity.Genre;

import java.util.List;

/**
 * Interface for film service layer
 */
public interface FilmService {

    /**
     * Creates the given film
     *
     * @param film the Film object to be created
     * @return true if the given film is successfully created, false otherwise
     */
    boolean add(Film film);

    /**
     * Shows the List object of all films already added, with pagination
     *
     * @param startIndex   the index corresponding to the desired page
     * @param itemsPerPage the number of films per page
     * @return List object of films starting from given index
     */
    List<Film> getAll(int startIndex, int itemsPerPage);

    /**
     * Retrieves the film having the given ID
     *
     * @param id the ID of the desired film
     * @return the Film object having the given ID, NULL if not found
     */
    Film getById(int id);

    /**
     * Retrieves films starring the given actor
     *
     * @param castId the ID of the given actor
     * @return List object of all films starring the given actor
     */
    List<Film> getByCast(int castId);

    /**
     * Retrieves films of the given genre
     *
     * @param genre the genre to filter films by
     * @return List object of all films of the given genre
     */
    List<Film> getByGenre(Genre genre);

    /**
     * Retrieves the user rating of the given film
     *
     * @param filmId the ID of the given film
     * @return the rating of the given film, 0 if the film is not rated by users
     */
    double getRating(int filmId);

    /**
     * Calculates and retrieves average ratings for all films
     *
     * @param startIndex the index corresponding to the currently displayed page
     * @return an array of avarage ratings for all films in order as in getAll() method
     */
    String[] getAllRatings(int startIndex, int itemsPerPage);

    /**
     * Retrieves the total number of all films already created
     *
     * @return the number of all films already created
     */
    int getTotalNumber();

    /**
     * Retrieves List object of film filtered by the given parameters
     *
     * @param title      the full title or a keyword in the title of the desired film
     * @param startYear  the oldest approximate production year of the desired film
     * @param finishYear the most recent approximate production year of the desired film
     * @param hasOscar   the Oscar winner status of the desired film
     * @param director   the director of the desired film
     * @param cast       the full name or a keyword in the desired film's cast member
     * @param genreId      a genre of the desired film
     * @return List object of all films satisfying the given filter parameters
     */
    List<Film> getFilteredFilms(String title, int startYear, int finishYear,
                                boolean hasOscar, String director, int cast, int genreId);

    /**
     * Updates the selected film to the given film
     *
     * @param film the film to be set as the updated version
     * @return true if the update was performed successfully, false otherwise
     */
    boolean edit(Film film);

    /**
     * Adds the given rating to the given film
     * by incrementing the corresponding starType's number in the given film
     *
     * @param filmId   the ID of the film to be rated
     * @param starType the rating to be set to the film
     * @return true if the rating is successfully updated, false otherwise
     */
    boolean rate(int filmId, int starType);
}
