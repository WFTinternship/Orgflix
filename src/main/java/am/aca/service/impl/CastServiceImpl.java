package am.aca.service.impl;

import am.aca.dao.DaoException;
import am.aca.dao.jdbc.CastDAO;
import am.aca.dao.jdbc.FilmDAO;
import am.aca.dao.jdbc.impljdbc.JdbcCastDAO;
import am.aca.dao.jdbc.impljdbc.JdbcFilmDAO;
import am.aca.entity.Cast;
import am.aca.entity.Film;
import am.aca.service.CastService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by David on 6/7/2017
 */
@Service
public class CastServiceImpl implements CastService {

    private static final Logger LOGGER = Logger.getLogger(CastServiceImpl.class);
    private ApplicationContext ctx = null;
    private CastDAO castDAO;
    private FilmDAO filmDAO;

    @Autowired
    public CastServiceImpl(ApplicationContext ctx) {
        this.ctx = ctx;
        castDAO = ctx.getBean("jdbcCastDAO", JdbcCastDAO.class);
        filmDAO = ctx.getBean("jdbcFilmDAO", JdbcFilmDAO.class);
    }

//    @Override
//    public Cast addCast(String cast, boolean hasOscar) {
//        Cast castObj = null;
//        try {
//            castObj = castDAO.addCast(cast, hasOscar);
//        } catch (DaoException e) {
//            LOGGER.warn(e.toString());
//        }
//        return castObj;
//    }

//    @Override
//    public Cast addCast(String cast) {
//        return castDAO.addCast(cast);
//    }

    @Override
    public boolean addCast(Cast cast) {
        boolean result = false;
        try {
            result = castDAO.addCast(cast);
        }
        catch (RuntimeException e){
            LOGGER.warn(e.toString());
        }
        return result;
    }

    @Override
    public boolean addCastToFilm(Cast cast, Film film) {
        return castDAO.addCastToFilm(cast, film);
    }

    @Override
    public boolean addCastToFilm(Cast cast, int filmId) {
        boolean state = false;
            try {
                if (castDAO.isStarringIn(cast.getId(), filmId))
                    return false;
            }
            catch (RuntimeException e){
                LOGGER.warn(e.getMessage());
            }
        try {
                state = castDAO.addCastToFilm(cast, filmId);
        }catch(DaoException e){
            LOGGER.warn(e.getMessage());
        }
        return state;
    }

    @Override
    public boolean editCast(Cast cast) {
        boolean state = false;
        if (!castDAO.exists(cast))
            return false;
        try {
            state = castDAO.editCast(cast);
        }catch (DaoException e){
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
        }catch (DaoException e){
            LOGGER.warn(e.getMessage());
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Film> listFilmsByCast(int castId) {
        List list = null;
        try {
            list = filmDAO.getFilmsByCast(castId);
        }catch (DaoException e){
            LOGGER.warn(e.toString());
        }
        return list;
    }
}