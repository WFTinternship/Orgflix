package am.aca.service.impl;

import am.aca.dao.jdbc.CastDAO;
import am.aca.dao.jdbc.FilmDAO;
import am.aca.entity.Cast;
import am.aca.entity.Film;
import am.aca.service.CastService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service layer for actor related methods
 */
@Transactional(readOnly = true)
@Service
public class CastServiceImpl implements CastService {

    private static final Logger LOGGER = Logger.getLogger(CastServiceImpl.class);
    @Autowired
    private CastDAO castDAO;
    @Autowired
    private FilmDAO filmDAO;

    @Autowired
    public void setCastDAO(CastDAO castDAO) {
        this.castDAO = castDAO;
    }

    @Autowired
    public void setFilmDAO(FilmDAO filmDAO) {
        this.filmDAO = filmDAO;
    }

    @Transactional
    @Override
    public boolean addCast(Cast cast) {
        boolean result = false;
        try {
            result = castDAO.addCast(cast);
        } catch (RuntimeException e) {
            LOGGER.warn(e.toString());
        }
        return result;
    }

    @Transactional
    @Override
    public boolean addCastToFilm(Cast cast, int filmId) {
        boolean state = false;
        try {
            if (castDAO.isStarringIn(cast.getId(), filmId))
                return false;
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
        }
        try {
            state = castDAO.addCastToFilm(cast, filmId);
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
        }
        return state;
    }

    @Transactional
    @Override
    public boolean editCast(Cast cast) {
        boolean state = false;
        if (!castDAO.exists(cast))
            return false;
        try {
            state = castDAO.editCast(cast);
        } catch (RuntimeException e) {
            LOGGER.warn(e.toString());
        }
        return state;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Cast> listCasts() {
        List<Cast> list = null;
        try {
            list = castDAO.listCast();
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Film> listFilmsByCast(int castId) {
        List<Film> list = null;
        try {
            list = filmDAO.getFilmsByCast(castId);
        } catch (RuntimeException e) {
            LOGGER.warn(e.toString());
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Cast> getCastsByFilm(int filmId){
        List<Cast> list = null;
        try {
            list = castDAO.getCastsByFilm(filmId);
        } catch (RuntimeException e) {
            LOGGER.warn(e.toString());
        }
        return list;
    }
}