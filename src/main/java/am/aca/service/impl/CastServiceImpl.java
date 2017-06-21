package am.aca.service.impl;

import am.aca.dao.jdbc.CastDao;
import am.aca.dao.DaoException;
import am.aca.dao.jdbc.impljdbc.CastDaoJdbc;
import am.aca.entity.Cast;
import am.aca.entity.Film;
import am.aca.service.CastService;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Created by David on 6/7/2017
 */
public class CastServiceImpl implements CastService {

    private static final Logger LOGGER = Logger.getLogger(CastServiceImpl.class);
    private CastDao CastDao;

    public CastServiceImpl() {
        CastDao = new CastDaoJdbc();
    }

    @Override
    public Cast addCast(String cast, boolean hasOscar) {
        Cast castObj = null;
        try {
            castObj = CastDao.addCast(cast, hasOscar);
        } catch (DaoException e) {
            LOGGER.warn(e.toString());
        }
        return castObj;
    }

    @Override
    public Cast addCast(String cast) {
        return CastDao.addCast(cast);
    }

    @Override
    public boolean addCastToFilm(Cast cast, Film film) {
        return CastDao.addCastToFilm(cast, film);
    }

    @Override
    public boolean addCastToFilm(int castId, int filmId) {
        boolean state = false;
        try {
            state = CastDao.addCastToFilm(castId, filmId);
        }catch(DaoException e){
            LOGGER.warn(e.getMessage());
        }
        return state;
    }

    @Override
    public boolean editCast(Cast cast) {
        boolean state = false;
        try {
            state = CastDao.editCast(cast);
        }catch (DaoException e){
            LOGGER.warn(e.toString());
        }
        return state;
    }

    @Override
    public List<Cast> listCasts() {
        List<Cast> list = null;
        try {
            list = CastDao.listCast();
        }catch (DaoException e){
            LOGGER.warn(e.getMessage());
        }
        return list;
    }

    @Override
    public List<Integer> listFilmsIdByCast(int castId) {
        List<Integer> list = null;
        try {
            list = CastDao.listFilmsIdByCast(castId);
        }catch (DaoException e){
            LOGGER.warn(e.toString());
        }
        return list;
    }
}