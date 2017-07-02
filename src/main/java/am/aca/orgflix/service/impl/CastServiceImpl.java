package am.aca.orgflix.service.impl;

import am.aca.orgflix.dao.CastDAO;
import am.aca.orgflix.dao.FilmDAO;
import am.aca.orgflix.entity.Cast;
import am.aca.orgflix.entity.Film;
import am.aca.orgflix.service.CastService;
import am.aca.orgflix.service.ServiceException;
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

    private CastDAO castDAO;
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
        boolean result;
        try {
            result = castDAO.addCast(cast);
        } catch (RuntimeException e) {
            throw new ServiceException(e.toString());
        }
        return result;
    }

    @Transactional
    @Override
    public boolean addCastToFilm(Cast cast, int filmId) {
        boolean state;
        try {
            if (castDAO.isStarringIn(cast.getId(), filmId))
                return false;
        } catch (RuntimeException e) {
            throw new ServiceException(e.getMessage());
        }
        try {
            state = castDAO.addCastToFilm(cast, filmId);
        } catch (RuntimeException e) {
            throw new ServiceException(e.getMessage());
        }
        return state;
    }

    @Transactional
    @Override
    public boolean editCast(Cast cast) {
        boolean state;
        try {
            if (!castDAO.exists(cast))
                return false;
        }
        catch (RuntimeException e) {
            throw new ServiceException(e.getMessage());
        }
        try {
            state = castDAO.editCast(cast);
        } catch (RuntimeException e) {
            throw new ServiceException(e.getMessage());
        }
        return state;
    }

    @Override
    public List<Cast> listCasts() {
        List<Cast> list;
        try {
            list = castDAO.listCast();
        } catch (RuntimeException e) {
            throw new ServiceException(e.getMessage());
        }
        return list;
    }

    @Override
    public List<Film> listFilmsByCast(int castId) {
        List<Film> list;
        try {
            list = filmDAO.getFilmsByCast(castId);
        } catch (RuntimeException e) {
            throw new ServiceException(e.getMessage());
        }
        return list;
    }

    @Override
    public List<Cast> getCastsByFilm(int filmId) {
        List<Cast> list;
        try {
            list = castDAO.getCastsByFilm(filmId);
        } catch (RuntimeException e) {
            throw new ServiceException(e.getMessage());
        }
        return list;
    }
}