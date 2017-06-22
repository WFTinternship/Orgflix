package am.aca.service.impl;

import am.aca.dao.*;
import am.aca.dao.jdbc.FilmDAO;
import am.aca.dao.jdbc.ListDao;
import am.aca.dao.jdbc.impljdbc.JdbcCastDAO;
import am.aca.dao.jdbc.impljdbc.JdbcFilmDAO;
import am.aca.dao.jdbc.impljdbc.ListDaoJdbc;
import am.aca.entity.Film;
import am.aca.service.ListService;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

import java.util.List;

/**
 * Created by David on 6/7/2017
 */
public class ListServiceImpl implements ListService {

    private static final Logger LOGGER = Logger.getLogger(ListServiceImpl.class);
    ApplicationContext ctx = null;
    private ListDao listDao;

    public ListServiceImpl(ApplicationContext ctx) {
        this.ctx = ctx;
        listDao = new ListDaoJdbc();
    }

    @Override
    public boolean addToWatched(Film film, boolean isPublic, int user_ID) {
        boolean state = false;
        try {
            FilmDAO filmDAO = ctx.getBean("JdbcFilmtDAO", JdbcFilmDAO.class);

            //case: the film does not exist
            if (filmDAO.getFilmById(film.getId()) == null) {
                filmDAO.addFilm(film);
            }

            //case: any
            state = listDao.addToWatched(film, isPublic, user_ID);
        } catch (DaoException e) {
            LOGGER.warn(e.getMessage());
        }
        return state;
    }

    @Override
    public boolean addToWished(Film film, boolean isPublic, int user_ID) {
        boolean state = false;
        try {
            FilmDAO filmDAO = ctx.getBean("JdbcFilmtDAO", JdbcFilmDAO.class);

            //case: the film does not exist
            if (filmDAO.getFilmById(film.getId()) == null) {
                filmDAO.addFilm(film);
            }

            //case: any
            state = listDao.addToWished(film, isPublic, user_ID);
        } catch (DaoException e) {
            LOGGER.warn(e.getMessage());
        }
        return state;
    }

    @Override
    public boolean removeFromWatched(Film film, int user_ID) {
        boolean state = false;
        try {
            state = listDao.removeFromWatched(film, user_ID);
        } catch (DaoException e) {
            LOGGER.warn(e.getMessage());
        }
        return state;
    }

    @Override
    public boolean removeFromWished(Film film, int user_ID) {
        boolean state = false;
        try {
            state = listDao.removeFromWished(film, user_ID);
        } catch (DaoException e) {
            LOGGER.warn(e.getMessage());
        }
        return state;
    }

    @Override
    public List<Film> showOwnWatched(int user_ID) {
        List<Film> list = null;
        try {
            list = listDao.showOwnWatched(user_ID);
        }catch (DaoException e){
            LOGGER.warn(e.getMessage());
        }
        return list;
    }

    @Override
    public List<Film> showOwnWished(int user_ID) {
        List<Film> list = null;
        try {
            list = listDao.showOwnWished(user_ID);
        }catch (DaoException e){
            LOGGER.warn(e.getMessage());
        }
        return list;
    }

    @Override
    public List<Film> showOthersWatched(int user_ID) {
        List<Film> list = null;
        try {
            list = listDao.showOthersWatched(user_ID);
        }catch (DaoException e){
            LOGGER.warn(e.getMessage());
        }
        return list;
    }

    @Override
    public List<Film> showOthersWished(int user_ID) {
        List<Film> list = null;
        try {
            list = listDao.showOthersWished(user_ID);
        }catch (DaoException e){
            LOGGER.warn(e.getMessage());
        }
        return list;
    }
}
