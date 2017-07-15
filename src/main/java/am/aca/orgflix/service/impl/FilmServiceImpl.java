package am.aca.orgflix.service.impl;

import am.aca.orgflix.dao.FilmDAO;
import am.aca.orgflix.entity.Cast;
import am.aca.orgflix.entity.Film;
import am.aca.orgflix.service.BaseServiceImpl;
import am.aca.orgflix.service.FilmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service layer for film related methods
 */
//@Transactional(readOnly = true)
@Service
public class FilmServiceImpl extends BaseServiceImpl implements FilmService {

    private FilmDAO filmDao;

    @Autowired
    public void setFilmDao(FilmDAO filmDao) {
        this.filmDao = filmDao;
    }

    /**
     * @see FilmService#addFilm(am.aca.orgflix.entity.Film)
     */
//    @Transactional
    @Override
    public boolean addFilm(Film film) {
        checkRequiredFields(film.getTitle());
        validateYear(film.getProdYear());
        return filmDao.addFilm(film);
    }

    /**
     * @see FilmService#getFilmsList(int)
     */
//    @Transactional
    @Override
    public boolean editFilm(Film film) {
        checkRequiredFields(film.getTitle());
        validateYear(film.getProdYear());
        return filmDao.editFilm(film);
    }

    /**
     * @see FilmService#getFilmById(int)
     */
    @Override
    public Film getFilmById(int id) {
        return filmDao.getFilmById(id);
    }

    /**
     * @see FilmService#getFilmsByCast(int)
     */
    @Override
    public List<Film> getFilmsByCast(int castId) {
        return filmDao.getFilmsByCast(castId);
    }

//    /**
//     * @see FilmService#getFilmsByGenre(am.aca.orgflix.entity.Genre)
//     */
//    @Transactional
    @Override
    public List<Film> getFilmsList(int startIndex) {
        List<Film> list;
        list = filmDao.getFilmsList(startIndex);
        for (Film film : list) {
            try {
                film.setCasts(filmDao.getCastsByFilm(film.getId()));
            } catch (RuntimeException ignored) {
            }
        }
        return list;
    }

//    /**
//     * @see FilmService#getRating(int)
//     */
//    @Override
//    public List<Film> getFilmsByGenre(Genre genre) {
//        return filmDao.getFilmsByGenre(genre);
//    }

    /**
     * @see FilmService#totalNumberOfFilms()
     */
//    @Transactional
    @Override
    public boolean rateFilm(int filmId, int starType) {
        return filmDao.rateFilm(filmId, starType);
    }

    /**
     * @see FilmService#getFilteredFilms(String, int, int, boolean, String, int, int)
     */
//    @Transactional
//    @Override
//    public boolean addGenreToFilm(Genre genre, int filmId) {
//        boolean state;
//        try {
//            state = filmDao.addGenreToFilm(genre, filmId);
//        } catch (RuntimeException e) {
//            throw new ServiceException(e.getMessage());
//        }
//        return state;
//    }
    @Override
    public List<Cast> getCastsByFilm(int filmId) {
        return filmDao.getCastsByFilm(filmId);
    }

    /**
     * @see FilmService#editFilm(am.aca.orgflix.entity.Film)
     */
    @Override
    public double getRating(int filmId) {
        int ratingCount = filmDao.getRating1(filmId) + filmDao.getRating2(filmId)
                + filmDao.getRating3(filmId) + filmDao.getRating4(filmId) + filmDao.getRating5(filmId);
        if (ratingCount == 0)
            return 0;

        int ratingSum = filmDao.getRating1(filmId) + 2 * filmDao.getRating2(filmId)
                + 3 * filmDao.getRating3(filmId) + 4 * filmDao.getRating4(filmId) + 5 * filmDao.getRating5(filmId);
        return ratingSum / ratingCount;
    }

    /**
     * @see FilmService#getAllRatings(int)
     */
    @Override
    public String[] getAllRatings(int startIndex) {
        List<Film> filmList = getFilmsList(startIndex);
        String[] ratings = new String[filmList.size()];
        for (int i = 0; i < filmList.size(); ++i) {
            ratings[i] = String.format("%.1f", getRating(filmList.get(i).getId()));
        }
        return ratings;
    }

    /**
     * @see FilmService#rateFilm(int, int)
     */
    @Override
    public int totalNumberOfFilms() {
        return filmDao.totalNumberOfFilms();
    }

    //    /**
//     * @see FilmService#addGenreToFilm(am.aca.orgflix.entity.Genre, int)
//     */
    @Override
    public List<Film> getFilteredFilms(String title, int startYear, int finishYear, boolean hasOscar, String director, int castId, int genreId) {
        return filmDao.getFilteredFilms(title, startYear, finishYear, hasOscar, director, castId, genreId);
    }


//    /**
//     * Creates associations of casts and genres of the given film
//     *
//     * @param film the film to be associated with its own cast and genres
//     * @return true if relations are successfully associated, false otherwise
//     */
//    private boolean optimizeRelations(Film film) {
//        try {
//            for (Genre genre : film.getGenres()) {
//                if (!addGenreToFilm(genre, film.getId()))
//                    return false;
//            }
//        } catch (RuntimeException e) {
//            throw new ServiceException(e.getMessage());
//        }
//
//        try {
//            for (Cast cast : film.getCasts()) {
//                if (!castDao.addCast(cast) || !castDao.addCastToFilm(cast, film.getId()))
//                    return false;
//            }
//        } catch (RuntimeException e) {
//            throw new ServiceException(e.getMessage());
//        }
//
//        return true;
//    }
}
