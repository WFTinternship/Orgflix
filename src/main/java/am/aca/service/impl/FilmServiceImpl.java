package am.aca.service.impl;

import am.aca.dao.DaoException;
import am.aca.dao.jdbc.impljdbc.JdbcFilmDAO;
import am.aca.dao.jdbc.FilmDAO;
import am.aca.dao.oldjdbc.FilmDao;
import am.aca.entity.Cast;
import am.aca.entity.Film;
import am.aca.entity.Genre;
import am.aca.service.FilmService;
import am.aca.util.ConnType;
import am.aca.util.DbManager;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import java.util.List;

/**
 * Created by David on 6/7/2017
 */
public class FilmServiceImpl implements FilmService {

    private static final Logger LOGGER = Logger.getLogger(FilmServiceImpl.class);
    private ApplicationContext ctx;
    private FilmDAO filmDao;

    public FilmServiceImpl(ApplicationContext ctx) {
        this.ctx = ctx;
        filmDao = ctx.getBean("JdbcFilmDAO", JdbcFilmDAO.class);
    }

    @Override
    public void addFilm(Film film) {
        try {
            boolean result = filmDao.addFilm(film);
            if (!result)
                return;
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

    }

    @Override
    public void editFilm(Film film) {
        try {
            if (!filmDao.editFilm(film))
                return;
        } catch (DaoException e) {
            LOGGER.warn(e.toString());
        }
//        try {
//
//            filmDao.resetRelationGenres(film);
//            filmDao.resetRelationCasts(film);
//        } catch (DaoException e) {
//            LOGGER.warn(e.toString());
//        }
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
    }

    @Override
    public Film getFilmById(int id) {
        Film film = null;
        try {
            film = filmDao.getFilmById(id);
        } catch (DaoException e) {
            LOGGER.warn(e.toString());
        }
        return film;
    }

    @Override
    public List getFilmsByCast(int castId) {
        List list = null;
        try {
            list = filmDao.getFilmsByCast(castId);
        } catch (DaoException e) {
            LOGGER.warn(e.toString());
        }
        return list;
    }

    @Override
    public List getFilmsByCast(Cast cast) {
        return filmDao.getFilmsByCast(cast);
    }

    @Override
    public List getFilmsList(int startIndex) {
        List list = null;
        try {
            list = filmDao.getFilmsList(startIndex);
        } catch (DaoException e) {
            LOGGER.warn(e.toString());
        }
        return list;
    }

    @Override
    public List getFilmsByGenre(Genre genre) {
        List list = null;
        try {
            list = filmDao.getFilmsByGenre(genre);
        } catch (DaoException e) {
            LOGGER.warn(e.toString());
        }
        return list;
    }

    @Override
    public boolean rateFilm(int filmId, int starType) {
        boolean state = false;
        try {
            state = filmDao.rateFilm(filmId, starType);
        } catch (DaoException e) {
            LOGGER.warn(e.toString());
        }
        return state;
    }

    @Override
    public boolean addGenreToFilm(Genre genre, int filmId) {
        boolean state = false;
        try {
            state = filmDao.addGenreToFilm(genre, filmId);
        } catch (DaoException e) {
            LOGGER.warn(e.toString());
        }
        return state;
    }

    @Override
    public boolean addGenreToFilm(Genre genre, Film film) {
        return filmDao.addGenreToFilm(genre, film.getId());
    }

    @Override
    public boolean addCastToFilm(Cast cast, int filmId) {
        boolean state = false;
        try {
            state = filmDao.addCastToFilm(cast, filmId);
        } catch (DaoException e) {
            LOGGER.warn(e.toString());
        }
        return state;
    }

    @Override
    public boolean addCastToFilm(Cast cast, Film film) {
        return filmDao.addCastToFilm(cast, film.getId());
    }

    @Override
    public double getRating(int filmId) {
        double rate = 0.0;
        try {
            rate = filmDao.getRating(filmId);
        } catch (DaoException e) {
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
        try {
            total = filmDao.totalNumberOfFilms();
        } catch (DaoException e) {
            LOGGER.warn(e.toString());
        }
        return total;
    }
}
