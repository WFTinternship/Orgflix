package am.aca.service.impl;

import am.aca.dao.jdbc.CastDAO;
import am.aca.dao.jdbc.FilmDAO;
import am.aca.entity.Cast;
import am.aca.entity.Film;
import am.aca.entity.Genre;
import am.aca.service.FilmService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service layer for film related methods
 */
@Transactional(readOnly = true)
@Service
public class FilmServiceImpl implements FilmService {

    private static final Logger LOGGER = Logger.getLogger(FilmServiceImpl.class);
    private FilmDAO filmDao;
    private CastDAO castDao;

    @Autowired
    public void setFilmDao(FilmDAO filmDao) {
        this.filmDao = filmDao;
    }

    @Autowired
    public void setCastDao(CastDAO castDao) {
        this.castDao = castDao;
    }

    @Transactional
    @Override
    public boolean addFilm(Film film) {
        try {
            boolean result = filmDao.addFilm(film);
            if (!result)
                return false;
        } catch (RuntimeException e) {
            LOGGER.warn(e.toString());
            return false;
        }

        return optimizeRelations(film);

    }

    @Transactional
    @Override
    public boolean editFilm(Film film) {
        try {
            if (!filmDao.editFilm(film))
                return false;
        } catch (RuntimeException e) {
            LOGGER.warn(e.toString());
            return false;
        }
        try {
            filmDao.resetRelationGenres(film);
            filmDao.resetRelationCasts(film);
        } catch (RuntimeException e) {
            LOGGER.warn(e.toString());
        }

        return optimizeRelations(film);
    }

    @Override
    public Film getFilmById(int id) {
        Film film = null;
        try {
            film = filmDao.getFilmById(id);
        } catch (RuntimeException e) {
            LOGGER.warn(e.toString());
        }
        return film;
    }

    @Override
    public List<Film> getFilmsByCast(int castId) {
        List<Film> list = null;
        try {
            list = filmDao.getFilmsByCast(castId);
        } catch (RuntimeException e) {
            LOGGER.warn(e.toString());
        }
        return list;
    }

    @Transactional
    @Override
    public List<Film> getFilmsList(int startIndex) {
        List<Film> list = null;
        try {
            list = filmDao.getFilmsList(startIndex);
            for(Film film : list){
                film.setCasts(castDao.getCastsByFilm(film.getId()));
            }
        } catch (RuntimeException e) {
            LOGGER.warn(e.toString());
        }
        return list;
    }

    @Override
    public List<Film> getFilmsByGenre(Genre genre) {
        List<Film> list = null;
        try {
            list = filmDao.getFilmsByGenre(genre);
        } catch (RuntimeException e) {
            LOGGER.warn(e.toString());
        }
        return list;
    }

    @Transactional
    @Override
    public boolean rateFilm(int filmId, int starType) {
        boolean state = false;
        try {
            state = filmDao.rateFilm(filmId, starType);
        } catch (RuntimeException e) {
            LOGGER.warn(e.toString());
        }
        return state;
    }

    @Transactional
    @Override
    public boolean addGenreToFilm(Genre genre, int filmId) {
        boolean state = false;
        try {
            state = filmDao.addGenreToFilm(genre, filmId);
        } catch (RuntimeException e) {
            LOGGER.warn(e.toString());
        }
        return state;
    }

    @Override
    public double getRating(int filmId) {
        double rate = 0.0;
        try {
            rate = filmDao.getRating(filmId);
        } catch (RuntimeException e) {
            LOGGER.warn(e.toString());
        }
        return rate;
    }

    @Override
    public int totalNumberOfFilms() {
        int total = 0;
        try {
            total = filmDao.totalNumberOfFilms();
        } catch (RuntimeException e) {
            LOGGER.warn(e.toString());
        }
        return total;
    }

    @Override
    public List<Film> getFilteredFilms(String title, int startYear, int finishYear, boolean hasOscar, String director, int castId, Genre genre) {
        List<Film> films = null;
        try {
            films = filmDao.getFilteredFilms(title, startYear, finishYear, hasOscar ? "1" : "%", director, String.valueOf(castId), String.valueOf(genre.getValue()));
        }
        catch (RuntimeException e) {
            LOGGER.warn(e.toString());
        }
        return films;
    }


    private boolean optimizeRelations(Film film) {
        try {
            for (Genre genre : film.getGenres()) {
                if (!addGenreToFilm(genre, film.getId()))
                    return false;
            }
        } catch (RuntimeException e) {
            LOGGER.warn(e.toString());
        }

        try {
            for (Cast cast : film.getCasts()) {
                if (!castDao.addCast(cast) || !castDao.addCastToFilm(cast, film.getId()))
                    return false;
            }
        } catch (RuntimeException e) {
            LOGGER.warn(e.toString());
        }

        return true;
    }
}
