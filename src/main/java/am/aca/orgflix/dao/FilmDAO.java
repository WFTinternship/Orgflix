package am.aca.orgflix.dao;

import am.aca.orgflix.entity.Cast;
import am.aca.orgflix.entity.Film;
import am.aca.orgflix.entity.Genre;

import java.util.List;

/**
 * Interface for Film DAO
 */
public interface FilmDAO {
    // Create

    /**
     * Add new film to DB, the production year is expected to be
     * after 1888 and and before 7 years ahead of current time
     *
     * @param film the film object to be added
     * @return true if the film was added, otherwise false
     */
    boolean add(Film film);

    /**
     * Add an association of genre with film in DB
     *
     * @param genre  the genre with which the provided film will be associated
     * @param filmId the id of film which will be associated with provided genre
     * @return true if the new association of genre to film was successful, otherwise false
     */
    boolean addGenreToFilm(Genre genre, int filmId);

    /**
     * Increment by one the selected scale (from 1 to 5) of the film provided ID
     *
     * @param filmId   the id of the film subject to be rated
     * @param starType the selected scale of rates to be incremented by one
     * @return true if the rating was successful, otherwise false
     */
    boolean rate(int filmId, int starType);

    // READ

    /**
     * Finds and returns the film by provided id from DB
     *
     * @param id the id of the requested film
     * @return the film matching the provided id
     */
    Film getById(int id);

    /**
     * Get one page of lists of films from DB, each page contains 12 films
     *
     * @param startIndex 0 based page index
     * @param pageSize maximum number of films per page
     * @return a list of films for the requested page
     */
    List<Film> getAll(int startIndex, int pageSize);

    /**
     * Return the list of all films associated with provided genre
     *
     * @param genre the genre object which films should be returned
     * @return A list of all films associated to the provided genre
     */
    List<Film> getByGenre(Genre genre);

    /**
     * Return the list of all films associated with provided actors id
     *
     * @param actorId the id of actor who's films should be returned
     * @return A list of all films associated to the actor with provided id
     */
    List<Film> getByCast(int actorId);

    /**
     * Get the overall current rating of the provided film, each scale of 1 to 5 has appropriate
     * weight, for example one 5 star and two 4 stars give rate (1*5+2*4)/(1+2)
     *
     * @param filmId the id of film which rate is requested
     * @return the current overall rate of the film
     */
    int[] getRating(int filmId);

    /**
     * Provide the current total number of films in DB
     *
     * @return the current total number of films in DB
     */
    int getTotalNumber();

    /**
     * Provide films filtered by user's desired parameters
     *
     * @param title      a keyword in desired film's title
     * @param startYear  the lower bound of the desired film's release date
     * @param finishYear the upper bound of the desired film's release date
     * @param hasOscar   indicator whether or not the desired film has Oscar for Best Picture
     * @param director   the full or partial name of the desired film's director
     * @param castId     the desired film's cast Id
     * @param genreId    the id of the desired film's genre
     * @return list of all films satisfying all filter and search conditions given by the user
     */
    List<Film> getFilteredFilms(String title, int startYear, int finishYear, boolean hasOscar,
                                String director, int castId, int genreId);

    // UPDATE

    /**
     * Update the fields of the provided film in DB
     *
     * @param film the film which fields will be updated in DB
     * @return true if the update was successful, otherwise false
     */
    boolean edit(Film film);

    // DELETE

    /**
     * Remove the all relations of provided film with any actor
     *
     * @param film the film which relations with actors are being removed
     * @return true if the relation was removed, otherwise false
     */
    boolean removeCasts(Film film);

    /**
     * Remove the all relations of provided film with any genre
     *
     * @param film the film which relations with genres are being removed
     * @return true if the relation was removed, otherwise false
     */
    boolean removeGenres(Film film);

    /**
     * Remove the provided film from DB
     *
     * @param film the film which are being removed
     * @return true if the film was removed, otherwise false
     */
    boolean remove(Film film);
}
