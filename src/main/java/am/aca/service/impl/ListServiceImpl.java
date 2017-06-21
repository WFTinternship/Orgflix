package am.aca.service.impl;

import am.aca.dao.*;
import am.aca.dao.jdbc.FilmDao;
import am.aca.dao.jdbc.ListDao;
import am.aca.dao.jdbc.impljdbc.FilmDaoJdbc;
import am.aca.dao.jdbc.impljdbc.ListDaoJdbc;
import am.aca.entity.Film;
import am.aca.service.ListService;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Created by David on 6/7/2017
 */
public class ListServiceImpl implements ListService {

    private static final Logger LOGGER = Logger.getLogger(ListServiceImpl.class);
    private ListDao listDao;

    public ListServiceImpl() {
        listDao = new ListDaoJdbc();
    }

    @Override
    public boolean addToWatched(Film film, boolean isPublic, int user_ID) {
        boolean state = false;
        try {
            FilmDao filmDao = new FilmDaoJdbc();

            //case: the film does not exist
            if (filmDao.getFilmById(film.getId()) == null) {
                filmDao.addFilm(film);
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
            FilmDao filmDao = new FilmDaoJdbc();

            //case: the film does not exist
            if (filmDao.getFilmById(film.getId()) == null) {
                filmDao.addFilm(film);
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
