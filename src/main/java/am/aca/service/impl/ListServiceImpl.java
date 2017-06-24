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
    public void addToWatched(Film film, boolean isPublic, int userId) {
        try {
            FilmDAO filmDao = ctx.getBean("jdbcFilmDAO", JdbcFilmDAO.class);

            //case: the film does not exist
            if (filmDao.getFilmById(film.getId()) == null) {
                filmDao.addFilm(film);
            }

            //case: any
            if (listDao.areRelated(film, userId)){
                listDao.updateWatched(film, userId);
            }
            else listDao.insertWatched(film, userId, isPublic);
//            state = listDao.addToWatched(film, isPublic, userId);
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
        }
    }

    @Override
    public void addToPlanned(Film film, boolean isPublic, int userId) {
        try {
            FilmDAO filmDao = ctx.getBean("jdbcFilmDAO", JdbcFilmDAO.class);
            //case: the film does not exist
            if (filmDao.getFilmById(film.getId()) == null) {
                filmDao.addFilm(film);
            }

            //case: any
//            state = listDao.addToPlanned(film, isPublic, userId);
            if (listDao.areRelated(film, userId))
                listDao.updatePlanned(film, userId);
            else listDao.insertPlanned(film, userId, isPublic);
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
        }
    }

    @Override
    public void removeFromWatched(Film film, int userId) {
        try {
            if (!listDao.isWatched(film, userId))
                return;
            if (listDao.isPlanned(film, userId)) {
                listDao.resetWatched(film, userId);
            }
            else listDao.removeFilm(film, userId);
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
        }
    }

    @Override
    public void removeFromPlanned(Film film, int userId) {
        try {
            if (!listDao.isPlanned(film, userId))
                return;
            if (listDao.isWatched(film, userId))
                listDao.resetPlanned(film, userId);
            else listDao.removeFilm(film, userId);
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
        }
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
    public void makePrivate(int userId, Film film) {
        try {
            if (!listDao.areRelated(film, userId))
                return;
            listDao.changePrivacy(film, userId, false);
        }catch (RuntimeException e){
            LOGGER.warn(e.getMessage());
        }
    }

    @Override
    public void makePublic(int userId, Film film) {
        try {
            if (!listDao.areRelated(film, userId))
                return;
            listDao.changePrivacy(film, userId, true);
        }catch (RuntimeException e){
            LOGGER.warn(e.getMessage());
        }
    }
}
