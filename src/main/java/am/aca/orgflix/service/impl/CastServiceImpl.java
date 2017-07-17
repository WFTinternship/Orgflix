package am.aca.orgflix.service.impl;

import am.aca.orgflix.dao.CastDAO;
import am.aca.orgflix.dao.FilmDAO;
import am.aca.orgflix.entity.Cast;
import am.aca.orgflix.entity.Film;
import am.aca.orgflix.service.CastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Service layer for actor related methods
 */
@Transactional(readOnly = true)
@Service
public class CastServiceImpl extends BaseService implements CastService {

    private CastDAO castDAO;
    private FilmDAO filmDAO;

    public CastServiceImpl() {
        // class name to include in logging
        super(CastServiceImpl.class);
    }

    @Autowired
    public void setCastDAO(CastDAO castDAO) {
        this.castDAO = castDAO;
    }

    @Autowired
    public void setFilmDAO(FilmDAO filmDAO) {
        this.filmDAO = filmDAO;
    }

    /**
     * @see CastService#addCast(am.aca.orgflix.entity.Cast)
     */
    @Transactional
    @Override
    public boolean addCast(Cast cast) {
        checkRequiredFields(cast.getName());
        boolean result;
        try {
            result = castDAO.addCast(cast);
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            return false;
        }
        return result;
    }

    /**
     * @see CastService#listCasts()
     */
    @Transactional
    @Override
    public boolean addCastToFilm(Cast cast, int filmId) {
        boolean state;
        try {
            if (castDAO.isStarringIn(cast.getId(), filmId))
                return false;

            state = castDAO.addCastToFilm(cast, filmId);
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            return false;
        }
        return state;
    }

    /**
     * @see CastService#listFilmsByCast(int)
     */
    @Transactional
    @Override
    public boolean editCast(Cast cast) {
        checkRequiredFields(cast.getName());
        boolean state;
        try {
            if (!castDAO.exists(cast))
                return false;
            state = castDAO.editCast(cast);
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            return false;
        }
        return state;
    }

    /**
     * @see CastService#getCastsByFilm(int)
     */
    @Override
    public List<Cast> listCasts() {
        List<Cast> list = new ArrayList<>();
        try {
            list = castDAO.listCast();
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            return list;
        }
        return list;
    }

    /**
     * @see CastService#editCast(am.aca.orgflix.entity.Cast)
     */
    @Override
    public List<Film> listFilmsByCast(int castId) {
        List<Film> list = new ArrayList<>();
        try {
            list = filmDAO.getFilmsByCast(castId);
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            return list;
        }
        return list;
    }

    /**
     * @see CastService#getCastsByFilm(int)
     */
    @Override
    public List<Cast> getCastsByFilm(int filmId) {
        List<Cast> list = new ArrayList<>();
        try {
            list = castDAO.getCastsByFilm(filmId);
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            return list;
        }
        return list;
    }

    /**
     * @see CastService#getCastById(int)
     */
    @Override
    public Cast getCastById(int castId) {
        Cast cast;
        try {
            cast = castDAO.getCastById(castId);
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            return null;
        }
        return cast;
    }
}