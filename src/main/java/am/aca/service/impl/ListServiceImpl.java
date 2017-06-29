package am.aca.service.impl;

import am.aca.dao.jdbc.FilmDAO;
import am.aca.dao.jdbc.ListDao;
import am.aca.dao.jdbc.impljdbc.JdbcFilmDAO;
import am.aca.dao.jdbc.impljdbc.JdbcListDAO;
import am.aca.entity.Film;
import am.aca.service.FilmService;
import am.aca.service.ListService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service layer for methods related to list of films
 */

@Transactional(readOnly = true)
@Service
public class ListServiceImpl implements ListService {

    private static final Logger LOGGER = Logger.getLogger(ListServiceImpl.class);
    private ListDao listDao;
    private FilmService filmService;

    @Autowired
    public void setFilmService(FilmService filmService) {
        this.filmService = filmService;
    }

    @Autowired
    public void setListDao(ListDao listDao) {
        this.listDao = listDao;
    }

    @Transactional
    @Override
    public boolean addToWatched(int filmId, boolean isPublic, int userId) {
        boolean state = false;
        try {
            //case: any
            if (listDao.areRelated(filmId, userId)){
                 state = listDao.updateWatched(filmId, userId);
            }
            else state =  listDao.insertWatched(filmId, userId, isPublic);
            if (listDao.areRelated(filmId, userId)) {
                state = listDao.updateWatched(filmId, userId);
            } else state = listDao.insertWatched(filmId, userId, isPublic);
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
        }
        return state;
    }

    @Transactional
    @Override
    public boolean addToPlanned(int filmId, boolean isPublic, int userId) {
        boolean state = false;
        try {
            //case: any
            if (listDao.areRelated(filmId, userId))
                state = listDao.updatePlanned(filmId, userId);
            else state = listDao.insertPlanned(filmId, userId, isPublic);
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
        }
        return state;
    }

    @Transactional
    @Override
    public boolean removeFromWatched(int filmId, int userId) {
        boolean state = false;
        try {
            if (!listDao.isWatched(filmId, userId))
                return false;
            if (listDao.isPlanned(filmId, userId)) {
                state = listDao.resetWatched(filmId, userId);
            } else state = listDao.removeFilm(filmId, userId);
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
        }
        return state;
    }

    @Transactional
    @Override
    public boolean removeFromPlanned(int filmId, int userId) {
        boolean state = false;
        try {
            if (!listDao.isPlanned(filmId, userId))
                return false;
            if (listDao.isWatched(filmId, userId))
                state = listDao.resetPlanned(filmId, userId);
            else state = listDao.removeFilm(filmId, userId);
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
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
        }
        return list;
    }

    @Override
    public List showOwnPlanned(int userId) {
        List list = null;
        try {
            list = listDao.showOwnPlanned(userId);
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
        }
        return list;
    }

    @Override
    public List showOthersWatched(int userId) {
        List list = null;
        try {
            list = listDao.showOthersWatched(userId);
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
        }
        return list;
    }

    @Override
    public List showOthersPlanned(int userId) {
        List list = null;
        try {
            list = listDao.showOthersPlanned(userId);
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
        }
        return list;
    }

    @Transactional
    @Override
    public boolean makePrivate(int userId, Film film) {
        boolean state = false;
        try {
            if (!listDao.areRelated(film.getId(), userId))
                return false;
            state = listDao.changePrivacy(film, userId, false);
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
        }
        return state;
    }

    @Transactional
    @Override
    public boolean makePublic(int userId, Film film) {
        boolean state = false;
        try {
            if (!listDao.areRelated(film.getId(), userId))
                return false;
            state = listDao.changePrivacy(film, userId, true);
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
        }
        return state;
    }
}
