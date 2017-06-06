package am.aca.service.impl;

import am.aca.dao.FilmDao;
import am.aca.dao.impljdbc.FilmDaoJdbc;
import am.aca.entity.Director;
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
        return filmDao.addFilm(film);
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
    public List<Film> getFilmsByDirector(Director director) {
        return filmDao.getFilmsByDirector(director);
    }

    @Override
    public List<Film> getFilmsByDirector(int directorId) {
        return filmDao.getFilmsByDirector(directorId);
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
}
