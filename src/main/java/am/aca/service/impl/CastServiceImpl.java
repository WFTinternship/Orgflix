package am.aca.service.impl;

import am.aca.dao.jdbc.CastDAO;
import am.aca.dao.DaoException;
import am.aca.dao.jdbc.impljdbc.JdbcCastDAO;
import am.aca.dao.jdbc.impljdbc.JdbcUserDAO;
import am.aca.entity.Cast;
import am.aca.entity.Film;
import am.aca.service.CastService;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import java.util.List;

/**
 * Created by David on 6/7/2017
 */
public class CastServiceImpl implements CastService {

    private static final Logger LOGGER = Logger.getLogger(CastServiceImpl.class);
    ApplicationContext ctx = null;
    private CastDAO CastDAO;

    public CastServiceImpl(ApplicationContext ctx) {
        this.ctx = ctx;
        CastDAO = ctx.getBean("JdbcCastDAO", JdbcCastDAO.class);
    }

    @Override
    public Cast addCast(String cast, boolean hasOscar) {
        Cast castObj = null;
        try {
            castObj = CastDAO.addCast(cast, hasOscar);
        } catch (DaoException e) {
            LOGGER.warn(e.toString());
        }
        return castObj;
    }

    @Override
    public Cast addCast(String cast) {
        return CastDAO.addCast(cast);
    }

    @Override
    public boolean addCastToFilm(Cast cast, Film film) {
        return CastDAO.addCastToFilm(cast, film);
    }

    @Override
    public boolean addCastToFilm(int castId, int filmId) {
        boolean state = false;
        try {
            state = CastDAO.addCastToFilm(castId, filmId);
        }catch(DaoException e){
            LOGGER.warn(e.getMessage());
        }
        return state;
    }

    @Override
    public boolean editCast(Cast cast) {
        boolean state = false;
        try {
            state = CastDAO.editCast(cast);
        }catch (DaoException e){
            LOGGER.warn(e.toString());
        }
        return state;
    }

    @Override
    public List<Cast> listCasts() {
        List<Cast> list = null;
        try {
            list = CastDAO.listCast();
        }catch (DaoException e){
            LOGGER.warn(e.getMessage());
        }
        return list;
    }

    @Override
    public List<Integer> listFilmsIdByCast(int castId) {
        List<Integer> list = null;
        try {
            list = CastDAO.listFilmsByCast(castId);
        }catch (DaoException e){
            LOGGER.warn(e.toString());
        }
        return list;
    }
}