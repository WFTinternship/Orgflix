package am.aca.orgflix.service.impl;

import am.aca.orgflix.dao.ListDao;
import am.aca.orgflix.entity.Film;
import am.aca.orgflix.service.CastService;
import am.aca.orgflix.service.FilmService;
import am.aca.orgflix.service.ListService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private ListDao listDao;
    @Autowired
    private FilmService filmService;
    @Autowired
    private CastService castService;

    @Autowired
    public void setFilmService(FilmService filmService) {
        this.filmService = filmService;
    }

    @Autowired
    public void setListDao(ListDao listDao) {
        this.listDao = listDao;
    }

    @Autowired
    public void setCastService(CastService castService) {
        this.castService = castService;
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

    @Transactional
    @Override
    public List<Film> showOwnWatched(int userId, int page) {
        List<Film> list = null;
        try {
            list = listDao.showOwnWatched(userId,page);
            for(Film film : list){
                film.setCasts(castService.getCastsByFilm(film.getId()));
            }
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
        }
        return list;
    }

    @Transactional
    @Override
    public List<Film> showOwnPlanned(int userId,int page) {
        List<Film> list = null;
        try {
            list = listDao.showOwnPlanned(userId,page);
            for(Film film : list){
                film.setCasts(castService.getCastsByFilm(film.getId()));
            }
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
        }
        return list;
    }

    @Transactional
    @Override
    public List<Film> showOthersWatched(int userId,int page) {
        List<Film> list = null;
        try {
            list = listDao.showOthersWatched(userId,page);
            for(Film film : list){
                film.setCasts(castService.getCastsByFilm(film.getId()));
            }
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
        }
        return list;
    }

    @Transactional
    @Override
    public List<Film> showOthersPlanned(int userId, int page) {
        List<Film> list = null;
        try {
            list = listDao.showOthersPlanned(userId, page);
            for(Film film : list){
                film.setCasts(castService.getCastsByFilm(film.getId()));
            }
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

    @Override
    public int totalNumberOfWatched(int userId, boolean isWatched) {
        int total = 0;
        try {
            if(isWatched) {
                total = listDao.totalNumberOfWatched(userId);
            } else {
                total = listDao.totalNumberOfWished(userId);
            }
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
        }
        return total;
    }
}
