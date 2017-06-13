package am.aca.service.impl;

import am.aca.dao.FilmDao;
import am.aca.dao.impljdbc.FilmDaoJdbc;
import am.aca.entity.Cast;
import am.aca.entity.Film;
import am.aca.entity.Genre;
import am.aca.service.FilmService;

import java.util.List;

/**
 * Created by David on 6/7/2017
 */
public class FilmServiceImpl implements FilmService {
    private FilmDao filmDao;

    public FilmServiceImpl() {
        filmDao = new FilmDaoJdbc();
    }

    @Override
    public boolean addFilm(Film film) {
        boolean result = filmDao.addFilm(film);
        if (!result)
            return false;

        for (Genre genre : film.getGenres()) {
            filmDao.addGenreToFilm(genre, film);
        }

        for (Cast cast : film.getCasts()) {
            filmDao.addCastToFilm(cast, film);
        }

        return true;
    }

    @Override
    public boolean editFilm(Film film) {
        return filmDao.editFilm(film);
    }

    @Override
    public Film getFilmById(int id) {
        return filmDao.getFilmById(id);
    }

    @Override
    public List<Film> getFilmsByCast(Cast cast) {
        return filmDao.getFilmsByCast(cast);
    }

    @Override
    public List<Film> getFilmsByCast(int castId) {
        return filmDao.getFilmsByCast(castId);
    }

    @Override
    public List<Film> getFilmsList(int startIndex) {
        return filmDao.getFilmsList(startIndex);
    }

    @Override
    public boolean rateFilm(int filmId, int starType) {
        return filmDao.rateFilm(filmId,starType);
    }

    @Override
    public boolean addGenreToFilm(Genre genre, Film film) {
        return filmDao.addGenreToFilm(genre,film);
    }

    @Override
    public boolean addGenreToFilm(Genre genre, int filmId) {
        return filmDao.addGenreToFilm(genre,filmId);
    }

    @Override
    public int totalNumberOfFilms(){
        return filmDao.totalNumberOfFilms();
    };
}
