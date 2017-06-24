package am.aca.service.impl;

import am.aca.dao.jdbc.FilmDAO;
import am.aca.dao.jdbc.ListDao;
import am.aca.dao.jdbc.impljdbc.JdbcFilmDAO;
import am.aca.dao.jdbc.impljdbc.JdbcListDAO;
import am.aca.entity.Film;
import am.aca.service.ListService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service layer for methods related to list of films
 */
@Service
public class ListServiceImpl implements ListService {

    private static final Logger LOGGER = Logger.getLogger(ListServiceImpl.class);
    private ListDao listDao;
    private ApplicationContext ctx;

    @Autowired
    public ListServiceImpl(ApplicationContext ctx) {
        this.ctx = ctx;
        listDao = ctx.getBean("jdbcListDAO", JdbcListDAO.class);
    }

    @Override
    public boolean addToWatched(Film film, boolean isPublic, int userId) {
        boolean state = false;
        try {
            FilmDAO filmDao = ctx.getBean("jdbcFilmDAO", JdbcFilmDAO.class);

            //case: the film does not exist
            if (filmDao.getFilmById(film.getId()) == null) {
                filmDao.addFilm(film);
            }

            //case: any
            if (listDao.areRelated(film, userId)){
                 state = listDao.updateWatched(film, userId);
            }
            else state =  listDao.insertWatched(film, userId, isPublic);
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
        }
        return state;
    }

    @Override
    public boolean addToPlanned(Film film, boolean isPublic, int userId) {
        boolean state = false;
        try {
            FilmDAO filmDao = ctx.getBean("jdbcFilmDAO", JdbcFilmDAO.class);
            //case: the film does not exist
            if (filmDao.getFilmById(film.getId()) == null) {
                filmDao.addFilm(film);
            }

            //case: any
            if (listDao.areRelated(film, userId))
                state = listDao.updatePlanned(film, userId);
            else state = listDao.insertPlanned(film, userId, isPublic);
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
        }
        return state;
    }

    @Override
    public boolean removeFromWatched(Film film, int userId) {
        boolean state = false;
        try {
            if (!listDao.isWatched(film, userId))
                return false;
            if (listDao.isPlanned(film, userId)) {
                state = listDao.resetWatched(film, userId);
            }
            else state = listDao.removeFilm(film, userId);
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
        }
        return state;
    }

    @Override
    public boolean removeFromPlanned(Film film, int userId) {
        boolean state = false;
        try {
            if (!listDao.isPlanned(film, userId))
                return false;
            if (listDao.isWatched(film, userId))
                state = listDao.resetPlanned(film, userId);
            else state = listDao.removeFilm(film, userId);
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
        }
        return state;
    }

    @Override
    public List showOwnWatched(int userId) {
        List list = null;
        try {
            list = listDao.showOwnWatched(userId);
        }catch (RuntimeException e){
            LOGGER.warn(e.getMessage());
        }
        return list;
    }

    @Override
    public List showOwnPlanned(int userId) {
        List list = null;
        try {
            list = listDao.showOwnPlanned(userId);
        }catch (RuntimeException e){
            LOGGER.warn(e.getMessage());
        }
        return list;
    }

    @Override
    public List showOthersWatched(int userId) {
        List list = null;
        try {
            list = listDao.showOthersWatched(userId);
        }catch (RuntimeException e){
            LOGGER.warn(e.getMessage());
        }
        return list;
    }

    @Override
    public List showOthersPlanned(int userId) {
        List list = null;
        try {
            list = listDao.showOthersPlanned(userId);
        }catch (RuntimeException e){
            LOGGER.warn(e.getMessage());
        }
        return list;
    }

    @Override
    public boolean makePrivate(int userId, Film film) {
        boolean state = false;
        try {
            if (!listDao.areRelated(film, userId))
                return false;
            state = listDao.changePrivacy(film, userId, false);
        }catch (RuntimeException e){
            LOGGER.warn(e.getMessage());
        }
        return state;
    }

    @Override
    public boolean makePublic(int userId, Film film) {
        boolean state = false;
        try {
            if (!listDao.areRelated(film, userId))
                return false;
            state = listDao.changePrivacy(film, userId, true);
        }catch (RuntimeException e){
            LOGGER.warn(e.getMessage());
        }
        return state;
    }
}
