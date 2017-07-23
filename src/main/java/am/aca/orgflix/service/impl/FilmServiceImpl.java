package am.aca.orgflix.service.impl;

import am.aca.orgflix.dao.FilmDAO;
import am.aca.orgflix.entity.Cast;
import am.aca.orgflix.entity.Film;
import am.aca.orgflix.entity.Genre;
import am.aca.orgflix.service.CastService;
import am.aca.orgflix.service.FilmService;
import am.aca.orgflix.exception.ServiceException;
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
     * @see FilmService#add(am.aca.orgflix.entity.Film)
     */
    @Transactional
    @Override
    public boolean add(Film film) {
        checkRequiredFields(film.getTitle());
        validateYear(film.getProdYear());

        try {
            return filmDao.add(film) && optimizeRelations(film);
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            return false;
        }
    }

    /**
     * @see FilmService#getById(int)
     */
    @Override
    public Film getById(int id) {
        Film film;
        try {
            film = filmDao.getById(id);
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            return null;
        }
        return film;
    }

    /**
     * @see FilmService#getByCast(int)
     */
    @Override
    public List<Film> getByCast(int castId) {
        List<Film> list = new ArrayList<>();
        try {
            list = filmDao.getByCast(castId);
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            return list;
        }
        return list;
    }

    /**
     * @see FilmService#getAll(int, int)
     */
    @Transactional
    @Override
    public List<Film> getAll(int startIndex, int itemsPerPage) {
        List<Film> list = new ArrayList<>();
        try {
            list = filmDao.getAll(startIndex, 12);
            for (Film film : list) {
                film.setCasts(castService.getByFilm(film.getId()));
            }
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            return list;
        }
        return list;
    }

    /**
     * @see FilmService#getByGenre(Genre)
     */
    @Override
    public List<Film> getByGenre(Genre genre) {
        List<Film> list = new ArrayList<>();
        try {
            list = filmDao.getByGenre(genre);
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            return list;
        }
        return list;
    }

    /**
     * @see FilmService#rate(int, int)
     */
    @Transactional
    @Override
    public boolean rate(int filmId, int starType) {
        validateRating(starType);

        boolean state;
        try {
            state = filmDao.rate(filmId, starType);
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            return false;
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
            int[] ratings = filmDao.getRating(filmId);
            for (int i = 0; i <= 4; i++) {
                ratingCount += ratings[i];
                ratingSum += ratings[i] * (i + 1);
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
     * @see FilmService#getAllRatings(int, int)
     */
    @Override
    public String[] getAllRatings(int startIndex, int itemsPerPage) {
        String[] ratings;
        try {
            List<Film> filmList = getAll(startIndex * itemsPerPage, itemsPerPage);
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
     * @see FilmService#getTotalNumber()
     */
    @Override
    public int getTotalNumber() {
        int total;
        try {
            total = filmDao.getTotalNumber();
        } catch (RuntimeException e) {
            LOGGER.warn(e.toString());
            return 0;
        }
        return total;
    }

    /**
     * @see FilmService#getFilteredFilms(String, int, int, boolean, String, int, int)
     */
    @Override
    public List<Film> getFilteredFilms(String title, int startYear, int finishYear,
                                       boolean hasOscar, String director, int cast, int genre) {
        List<Film> films = new ArrayList<>();
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
     * @see FilmService#edit(Film)
     */
    @Transactional
    @Override
    public boolean edit(Film film) {
        checkRequiredFields(film.getTitle());
        validateYear(film.getProdYear());

        try {
            if (!filmDao.edit(film))
                return false;

            filmDao.removeGenres(film);
            filmDao.removeCasts(film);

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
    @Transactional
    private boolean optimizeRelations(Film film) {
        try {
            for (Genre genre : film.getGenres()) {
                if (!filmDao.addGenreToFilm(genre, film.getId()))
                    return false;
            }

            for (Cast cast : film.getCasts()) {
                if (!castService.addToFilm(cast, film.getId()))
                    return false;
            }
        } catch (RuntimeException e) {
            LOGGER.warn(e.toString());
            return false;
        }
        return true;
    }
}
