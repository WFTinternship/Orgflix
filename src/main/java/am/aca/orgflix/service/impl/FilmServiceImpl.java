package am.aca.orgflix.service.impl;

import am.aca.orgflix.dao.FilmDAO;
import am.aca.orgflix.entity.Cast;
import am.aca.orgflix.entity.Film;
import am.aca.orgflix.entity.Genre;
import am.aca.orgflix.service.CastService;
import am.aca.orgflix.service.FilmService;
import am.aca.orgflix.service.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Service layer for film related methods
 */
@Transactional(readOnly = true)
@Service
public class FilmServiceImpl extends BaseService implements FilmService {

    private FilmDAO filmDao;
    private CastService castService;

    public FilmServiceImpl() {
        // class name to include in logging
        super(FilmServiceImpl.class);
    }

    @Autowired
    public void setFilmDao(FilmDAO filmDao) {
        this.filmDao = filmDao;
    }

    @Autowired
    public void setCastService(CastService castService) {
        this.castService = castService;
    }

    /**
     * @see FilmService#addFilm(am.aca.orgflix.entity.Film)
     */
    @Transactional
    @Override
    public boolean addFilm(Film film) {
        checkRequiredFields(film.getTitle());
        validateYear(film.getProdYear());

        try {
            return filmDao.addFilm(film) && optimizeRelations(film);
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            return false;
        }
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
            LOGGER.warn(e.getMessage());
            return null;
        }
        return film;
    }

    /**
     * @see FilmService#getFilmsByCast(int)
     */
    @Override
    public List<Film> getFilmsByCast(int castId) {
        List<Film> list = new ArrayList<>();
        try {
            list = filmDao.getFilmsByCast(castId);
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            return list;
        }
        return list;
    }

    /**
     * @see FilmService#getFilmsList(int, int)
     */
    @Transactional
    @Override
    public List<Film> getFilmsList(int startIndex, int itemsPerPage) {
        List<Film> list = new ArrayList<>();
        try {
            list = filmDao.getFilmsList(startIndex, 12);
            for (Film film : list) {
                film.setCasts(castService.getCastsByFilm(film.getId()));
            }
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            return list;
        }
        return list;
    }

    /**
     * @see FilmService#getFilmsByGenre(Genre)
     */
    @Override
    public List<Film> getFilmsByGenre(Genre genre) {
        List<Film> list = new ArrayList<>();
        try {
            list = filmDao.getFilmsByGenre(genre);
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            return list;
        }
        return list;
    }

    /**
     * @see FilmService#rateFilm(int, int)
     */
    @Transactional
    @Override
    public boolean rateFilm(int filmId, int starType) {
        validateRating(starType);

        boolean state;
        try {
            state = filmDao.rateFilm(filmId, starType);
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            return false;
        }
        return state;
    }

    /**
     * @see FilmService#addGenreToFilm(Genre, int)
     */
    @Transactional
    @Override
    public boolean addGenreToFilm(Genre genre, int filmId) {
        boolean state = false;
        try {
            state = filmDao.addGenreToFilm(genre, filmId);
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
//            return false;
        }
        return state;
    }

    /**
     * @see FilmService#getRating(int)
     */
    @Override
    public double getRating(int filmId) {
        int ratingSum = 0;
        int ratingCount = 0;

        try {
            for (int i = 1; i <= 5; i++) {
                ratingCount += filmDao.getRating(filmId, i);
                ratingSum += filmDao.getRating(filmId, i) * i;
            }

            if (ratingCount == 0)
                return 0;
            else {
                double result = (double) ratingSum / ratingCount;

                int scale = (int) Math.pow(10, 1);
                return (double) Math.round(result * scale) / scale;
            }
        } catch (RuntimeException e) {
            LOGGER.warn(e.toString());
            return 0;
        }
    }

    /**
     * @see FilmService#getAllRatings(int)
     */
    @Override
    public String[] getAllRatings(int startIndex) {
        String[] ratings;
        try {
            List<Film> filmList = getFilmsList(startIndex * 12, 12);
            ratings = new String[filmList.size()];
            for (int i = 0; i < filmList.size(); ++i) {
                ratings[i] = String.format("%.1f", getRating(filmList.get(i).getId()));
            }
        } catch (RuntimeException e) {
            LOGGER.warn(e.toString());
            throw new ServiceException(e.getMessage());
        }
        return ratings;
    }

    /**
     * @see FilmService#totalNumberOfFilms()
     */
    @Override
    public int totalNumberOfFilms() {
        int total;
        try {
            total = filmDao.totalNumberOfFilms();
        } catch (RuntimeException e) {
            LOGGER.warn(e.toString());
            return 0;
        }
        return total;
    }

    /**
     * @see FilmService#getFilteredFilms(String, int, int, boolean, String, String, int)
     */
    @Override
    public List<Film> getFilteredFilms(String title, int startYear, int finishYear,
                                       boolean hasOscar, String director, String cast, int genre) {
        List<Film> films = new ArrayList<>();
        if (title == null)
            title = "";
        try {
            films = filmDao.getFilteredFilms(title, startYear,
                    finishYear, hasOscar, director, cast, genre);
        } catch (RuntimeException e) {
            LOGGER.warn(e.toString());
            return films;
        }
        return films;
    }

    /**
     * @see FilmService#editFilm(Film)
     */
    @Transactional
    @Override
    public boolean editFilm(Film film) {
        checkRequiredFields(film.getTitle());
        validateYear(film.getProdYear());

        try {
            if (!filmDao.editFilm(film))
                return false;

            filmDao.resetRelationGenres(film);
            filmDao.resetRelationCasts(film);

        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            return false;
        }
        return optimizeRelations(film);
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
                if (!filmDao.addGenreToFilm(genre, film.getId()))
                    return false;
            }

            for (Cast cast : film.getCasts()) {
                if (!castService.addCastToFilm(cast, film.getId()))
                    return false;
            }
        } catch (RuntimeException e) {
            LOGGER.warn(e.toString());
            return false;
        }
        return true;
    }
}
