package am.aca.service;

import am.aca.entity.Director;
import am.aca.entity.Film;

import java.util.List;

/**
 * Created by David on 5/27/2017
 */
public interface DirectorService {
    Director addDirector(String director, boolean hasOscar);
    Director addDirector(String director);
    boolean addDirectorToFilm(Director director, Film film);
    boolean addDirectorToFilm(int directorId, int filmId);
    boolean editDirector(Director director);
    List<Director> listDirectors();
    List<Integer> listFilmsIdByDirector(int directorId);
}
