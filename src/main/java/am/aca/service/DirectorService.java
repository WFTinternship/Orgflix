package am.aca.service;

import am.aca.entity.Cast;
import am.aca.entity.Film;

import java.util.List;

/**
 * Created by David on 5/27/2017
 */
public interface DirectorService {
    Cast addDirector(String director, boolean hasOscar);
    Cast addDirector(String director);
    boolean addDirectorToFilm(Cast cast, Film film);
    boolean addDirectorToFilm(int directorId, int filmId);
    boolean editDirector(Cast cast);
    List<Cast> listDirectors();
    List<Integer> listFilmsIdByDirector(int directorId);
}
