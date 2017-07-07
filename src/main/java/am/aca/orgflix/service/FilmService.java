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
    boolean addFilm(Film film);

    /**
     * Shows the List object of all films already added, with pagination
     *
     * @param startIndex the index corresponding to the desired page
     * @return List object of films starting from given index
     */
    List<Film> getFilmsList(int startIndex);

    /**
     * Retrieves the film having the given ID
     *
     * @param id the ID of the desired film
     * @return the Film object having the given ID, NULL if not found
     */
    Film getFilmById(int id);

    /**
     * Retrieves films starring the given actor
     *
     * @param castId the ID of the given actor
     * @return List object of all films starring the given actor
     */
    List<Film> getFilmsByCast(int castId);

    /**
     * Retrieves films of the given genre
     *
     * @param genre the genre to filter films by
     * @return List object of all films of the given genre
     */
    List<Film> getFilmsByGenre(Genre genre);

    /**
     * Retrieves the user rating of the given film
     *
     * @param filmId the ID of the given film
     * @return the rating of the given film, 0 if the film is not rated by users
     */
    double getRating(int filmId);

    /**
     * Retrieves the total number of all films already created
     *
     * @return the number of all films already created
     */
    int totalNumberOfFilms();

    /**
     * Retrieves List object of film filtered by the given parameters
     *
     * @param title      the full title or a keyword in the title of the desired film
     * @param startYear  the oldest approximate production year of the desired film
     * @param finishYear the most recent approximate production year of the desired film
     * @param hasOscar   the Oscar winner status of the desired film
     * @param director   the director of the desired film
     * @param castId     the ID of the desired film's cast member
     * @param genre      a genre of the desired film
     * @return List object of all films satisfying the given filter parameters
     */
    List<Film> getFilteredFilms(String title, int startYear, int finishYear,
                                boolean hasOscar, String director, int castId, Genre genre);

    /**
     * Updates the selected film to the given film
     *
     * @param film the film to be set as the updated version
     * @return true if the update was performed successfully, false otherwise
     */
    boolean editFilm(Film film);

    /**
     * Adds the given rating to the given film
     * by incrementing the corresponding starType's number in the given film
     *
     * @param filmId   the ID of the film to be rated
     * @param starType the rating to be set to the film
     * @return true if the rating is successfully updated, false otherwise
     */
    boolean rateFilm(int filmId, int starType);

    /**
     * Assigns new genre to the given film
     *
     * @param filmId the ID of the film to be updated
     * @param genre  the genre to be added to the film
     * @return true if the film is successfully updated, false otherwise
     */
    boolean addGenreToFilm(Genre genre, int filmId);
}
