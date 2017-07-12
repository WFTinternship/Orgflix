package am.aca.orgflix.service.impl;

import am.aca.orgflix.dao.CastDAO;
import am.aca.orgflix.dao.FilmDAO;
import am.aca.orgflix.entity.Cast;
import am.aca.orgflix.entity.Film;
import am.aca.orgflix.entity.Genre;
import am.aca.orgflix.service.FilmService;
import am.aca.orgflix.service.ServiceException;
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

    /**
     * @see FilmService#addFilm(am.aca.orgflix.entity.Film)
     */
    @Transactional
    @Override
    public boolean addFilm(Film film) {
        try {
            if (!filmDao.addFilm(film))
                return false;
            else
                return  optimizeRelations(film);
        } catch (RuntimeException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @see FilmService#getFilmsList(int)
     */
    @Transactional
    @Override
    public boolean editFilm(Film film) {
        try {
            if (!filmDao.editFilm(film))
                return false;
        } catch (RuntimeException e) {
            throw new ServiceException(e.getMessage());
        }
        try {
            filmDao.resetRelationGenres(film);
            filmDao.resetRelationCasts(film);
        } catch (RuntimeException e) {
            throw new ServiceException(e.getMessage());
        }

        return optimizeRelations(film);
    }

    /**
     * @see FilmService#getFilmById(int)
     */
    @Override
    public Film getFilmById(int id) {
        Film film;
        try {
            film = filmDao.getFilmById(id);
        } catch (RuntimeException e) {
            throw new ServiceException(e.getMessage());
        }
        return film;
    }

    /**
     * @see FilmService#getFilmsByCast(int)
     */
    @Override
    public List<Film> getFilmsByCast(int castId) {
        List<Film> list;
        try {
            list = filmDao.getFilmsByCast(castId);
        } catch (RuntimeException e) {
            throw new ServiceException(e.getMessage());
        }
        return list;
    }

    /**
     * @see FilmService#getFilmsByGenre(am.aca.orgflix.entity.Genre)
     */
    @Transactional
    @Override
    public List<Film> getFilmsList(int startIndex) {
        List<Film> list;
        try {
            list = filmDao.getFilmsList(startIndex);
        } catch (RuntimeException e) {
            throw new ServiceException(e.getMessage());
        }
        for (Film film : list) {
            try {

                film.setCasts(castDao.getCastsByFilm(film.getId()));
            } catch (RuntimeException e) {
                throw new ServiceException(e.getMessage());
            }
        }
        return list;
    }

    /**
     * @see FilmService#getRating(int)
     */
    @Override
    public List<Film> getFilmsByGenre(Genre genre) {
        List<Film> list;
        try {
            list = filmDao.getFilmsByGenre(genre);
        } catch (RuntimeException e) {
            throw new ServiceException(e.getMessage());
        }
        return list;
    }

    /**
     * @see FilmService#totalNumberOfFilms()
     */
    @Transactional
    @Override
    public boolean rateFilm(int filmId, int starType) {
        boolean state = false;
        try {
            state = filmDao.rateFilm(filmId, starType);
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
        }
        return state;
    }

    /**
     * @see FilmService#getFilteredFilms(java.lang.String, int, int,
     * boolean, java.lang.String, int, am.aca.orgflix.entity.Genre)
     */
    @Transactional
    @Override
    public boolean addGenreToFilm(Genre genre, int filmId) {
        boolean state;
        try {
            state = filmDao.addGenreToFilm(genre, filmId);
        } catch (RuntimeException e) {
            throw new ServiceException(e.getMessage());
        }
        return state;
    }

    /**
     * @see FilmService#editFilm(am.aca.orgflix.entity.Film)
     */
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

    /**
     *
     * @see FilmService#getAllRatings(int)
     */
    @Override
    public String[] getAllRatings(int startIndex) {
        List<Film> filmList = getFilmsList(startIndex);
        String[] ratings = new String[filmList.size()];
        for(int i=0;i<filmList.size();++i){
            ratings[i]=String.format("%.1f",getRating( filmList.get(i).getId()));
        }
        return ratings;
    }

    /**
     * @see FilmService#rateFilm(int, int)
     */
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

    /**
     * @see FilmService#addGenreToFilm(am.aca.orgflix.entity.Genre, int)
     */
    @Override
    public List<Film> getFilteredFilms(String title, int startYear, int finishYear, boolean hasOscar, String director, int castId, Genre genre) {
        List<Film> films;
        try {
            films = filmDao.getFilteredFilms(title, startYear, finishYear, hasOscar ? "1" : "%", director, String.valueOf(castId), String.valueOf(genre.getValue()));
        } catch (RuntimeException e) {
            throw new ServiceException(e.getMessage());
        }
        return films;
    }


    /**
     * Creates associations of casts and genres of the given film
     *
     * @param film the film to be associated with its own cast and genres
     * @return true if relations are successfully associated, false otherwise
     */
    private boolean optimizeRelations(Film film) {
        try {
            for (Genre genre : film.getGenres()) {
                if (!addGenreToFilm(genre, film.getId()))
                    return false;
            }
        } catch (RuntimeException e) {
            throw new ServiceException(e.getMessage());
        }

        try {
            for (Cast cast : film.getCasts()) {
                if (!castDao.addCast(cast) || !castDao.addCastToFilm(cast, film.getId()))
                    return false;
            }
        } catch (RuntimeException e) {
            throw new ServiceException(e.getMessage());
        }

        return true;
    }
}
