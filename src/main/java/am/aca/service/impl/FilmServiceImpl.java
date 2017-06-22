package am.aca.service.impl;

import am.aca.dao.DaoException;
import am.aca.dao.jdbc.FilmDAO;
import am.aca.dao.jdbc.impljdbc.JdbcCastDAO;
import am.aca.dao.jdbc.impljdbc.JdbcFilmDAO;
import am.aca.entity.Cast;
import am.aca.entity.Film;
import am.aca.entity.Genre;
import am.aca.service.FilmService;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import java.util.List;

/**
 * Created by David on 6/7/2017
 */
public class FilmServiceImpl implements FilmService {

    private static final Logger LOGGER = Logger.getLogger(FilmServiceImpl.class);
    ApplicationContext ctx = null;

    private FilmDAO filmDAO;

    public FilmServiceImpl(ApplicationContext ctx) {
        this.ctx = ctx;
        filmDAO = ctx.getBean("JdbcFilmDAO", JdbcFilmDAO.class);
    }

    @Override
    public boolean addFilm(Film film) {
        try {
            boolean result = filmDAO.addFilm(film);
            if (!result)
                return false;
        } catch (DaoException e) {
            LOGGER.warn(e.toString());
        }

        try {
            for (Genre genre : film.getGenres()) {
                addGenreToFilm(genre, film);
            }
        } catch (DaoException e) {
            LOGGER.warn(e.toString());
        }

        try {
            for (Cast cast : film.getCasts()) {
                addCastToFilm(cast, film);
            }
        } catch (DaoException e) {
            LOGGER.warn(e.toString());
        }

        return true;
    }

    @Override
    public boolean editFilm(Film film) {
        boolean state = false;
        try {
            state = filmDAO.editFilm(film);
        } catch (DaoException e) {
            LOGGER.warn(e.toString());
        }
        return state;
    }

    @Override
    public Film getFilmById(int id) {
        Film film = null;
        try {
            film = filmDAO.getFilmById(id);
        } catch (DaoException e) {
            LOGGER.warn(e.toString());
        }
        return film;
    }

    @Override
    public List<Film> getFilmsByCast(int castId) {
        List<Film> list = null;
        try {
            list = filmDAO.getFilmsByCast(castId);
        } catch (DaoException e) {
            LOGGER.warn(e.toString());
        }
        return list;
    }

    @Override
    public List<Film> getFilmsByCast(Cast cast) {
        return filmDAO.getFilmsByCast(cast);
    }

    @Override
    public List<Film> getFilmsList(int startIndex) {
        List<Film> list = null;
        try {
            list = filmDAO.getFilmsList(startIndex);
        } catch (DaoException e) {
            LOGGER.warn(e.toString());
        }
        return list;
    }

    @Override
    public List<Film> getFilmsByGenre(Genre genre) {
        List<Film> list = null;
        try {
            list = filmDAO.getFilmsByGenre(genre);
        } catch (DaoException e) {
            LOGGER.warn(e.toString());
        }
        return list;
    }

    @Override
    public boolean rateFilm(int filmId, int starType) {
        boolean state = false;
        try {
            state = filmDAO.rateFilm(filmId, starType);
        } catch (DaoException e) {
            LOGGER.warn(e.toString());
        }
        return state;
    }

    @Override
    public boolean addGenreToFilm(Genre genre, int filmId) {
        boolean state = false;
        try {
            state = filmDAO.addGenreToFilm(genre, filmId);
        } catch (DaoException e) {
            LOGGER.warn(e.toString());
        }
        return state;
    }

    @Override
    public boolean addGenreToFilm(Genre genre, Film film) {
        return filmDAO.addGenreToFilm(genre, film.getId());
    }

    @Override
    public boolean addCastToFilm(Cast cast, int filmId) {
        boolean state = false;
        try{
            state = filmDAO.addCastToFilm(cast,filmId);
        }catch (DaoException e){
            LOGGER.warn(e.toString());
        }
        return state;
    }

    @Override
    public boolean addCastToFilm(Cast cast, Film film) {
        return filmDAO.addCastToFilm(cast,film.getId());
    }

    @Override
    public double getRating(int filmId) {
        double rate = 0.0;
        try{
            rate = filmDAO.getRating(filmId);
        }catch (DaoException e){
            LOGGER.warn(e.toString());
        }
        return rate;
    }

    @Override
    public double getRating(Film film) {
        return getRating(film.getId());
    }

    @Override
    public int totalNumberOfFilms() {
        int total = 0;
        try{
            total = filmDAO.totalNumberOfFilms();
        }catch (DaoException e){
            LOGGER.warn(e.toString());
        }
        return total;
    }
}
