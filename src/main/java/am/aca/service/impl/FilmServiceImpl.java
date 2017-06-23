package am.aca.service.impl;

import am.aca.dao.DaoException;
import am.aca.dao.jdbc.CastDAO;
import am.aca.dao.jdbc.FilmDAO;
import am.aca.dao.jdbc.impljdbc.JdbcCastDAO;
import am.aca.dao.jdbc.impljdbc.JdbcFilmDAO;
import am.aca.entity.Cast;
import am.aca.entity.Film;
import am.aca.entity.Genre;
import am.aca.service.FilmService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by David on 6/7/2017
 */
@Service
public class FilmServiceImpl implements FilmService {

    private static final Logger LOGGER = Logger.getLogger(FilmServiceImpl.class);
    private ApplicationContext ctx;
    private FilmDAO filmDao;
    private CastDAO castDao;

    @Autowired
    public FilmServiceImpl(ApplicationContext ctx) {
        this.ctx = ctx;
        filmDao = ctx.getBean("jdbcFilmDAO", JdbcFilmDAO.class);
        castDao = ctx.getBean("jdbcCastDAO", JdbcCastDAO.class);
    }

    @Override
    public boolean addFilm(Film film) {
        try {
            boolean result = filmDao.addFilm(film);
            if (!result)
                return false;
        } catch (DaoException e) {
            LOGGER.warn(e.toString());
            return false;
        }

        optimizeRelations(film);

        return true;

    }

    @Override
    public boolean editFilm(Film film) {
        try {
            if (!filmDao.editFilm(film))
                return false;
        } catch (DaoException e) {
            LOGGER.warn(e.toString());
            return false;
        }
        try {
            filmDao.resetRelationGenres(film);
            filmDao.resetRelationCasts(film);
        } catch (DaoException e) {
            LOGGER.warn(e.toString());
        }

        optimizeRelations(film);

        return true;
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
    public List<Film> getFilmsByCast(int castId) {
        List<Film> list = null;
        try {
            list = filmDao.getFilmsByCast(castId);
        } catch (DaoException e) {
            LOGGER.warn(e.toString());
        }
        return list;
    }

    @Override
    public List<Film> getFilmsByCast(Cast cast) {
        return filmDao.getFilmsByCast(cast.getId());
    }

    @Override
    public List<Film> getFilmsList(int startIndex) {
        List<Film> list = null;
        try {
            list = filmDao.getFilmsList(startIndex);
        } catch (DaoException e) {
            LOGGER.warn(e.toString());
        }
        return list;
    }

    @Override
    public List<Film> getFilmsByGenre(Genre genre) {
        List<Film> list = null;
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

//    @Override
//    public boolean addCastToFilm(Cast cast, int filmId) {
//        boolean state = false;
//        try {
//            state = castDao.addCastToFilm(cast, filmId);
//        } catch (DaoException e) {
//            LOGGER.warn(e.toString());
//        }
//        return state;
//    }
//
//    @Override
//    public boolean addCastToFilm(Cast cast, Film film) {
//        return castDao.addCastToFilm(cast, film.getId());
//    }

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

    private void optimizeRelations(Film film) {
        try {
            for (Genre genre : film.getGenres()) {
                addGenreToFilm(genre, film);
            }
        } catch (DaoException e) {
            LOGGER.warn(e.toString());
        }

        try {
            for (Cast cast : film.getCasts()) {
                castDao.addCast(cast);
                castDao.addCastToFilm(cast, film);
            }
        } catch (DaoException e) {
            LOGGER.warn(e.toString());
        }
    }
}
