package am.aca.orgflix.service.impl;

import am.aca.orgflix.dao.ListDao;
import am.aca.orgflix.entity.Film;
import am.aca.orgflix.service.CastService;
import am.aca.orgflix.service.ListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Service layer for methods related to list of films
 */

@Transactional(readOnly = true)
@Service
public class ListServiceImpl extends BaseService implements ListService {

    private ListDao listDao;
    private CastService castService;

    public ListServiceImpl() {
        // class name to include in logging
        super(ListServiceImpl.class);
    }

    @Autowired
    public void setListDao(ListDao listDao) {
        this.listDao = listDao;
    }

    @Autowired
    public void setCastService(CastService castService) {
        this.castService = castService;
    }

    /**
     * @see ListService#addToWatched(int, boolean, int)
     */
    @Transactional
    @Override
    public boolean addToWatched(int filmId, boolean isPublic, int userId) {
        boolean state;
        try {
            //case: planned
            if (listDao.areRelated(filmId, userId)) {
                state = listDao.setFilmAsWatched(filmId, userId);
            }
            //case: not planned
            else state = listDao.insertWatched(filmId, userId, isPublic);
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            return false;
        }
        return state;
    }

    /**
     * @see ListService#addToPlanned(int, boolean, int)
     */
    @Transactional
    @Override
    public boolean addToPlanned(int filmId, boolean isPublic, int userId) {
        boolean state;
        try {
            //case: watched
            if (listDao.areRelated(filmId, userId))
                state = listDao.setFilmAsPlanned(filmId, userId);

                //case: not watched
            else state = listDao.insertPlanned(filmId, userId, isPublic);
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            return false;
        }
        return state;
    }

    /**
     * @see ListService#showOwnWatched(int, int)
     */
    @Transactional
    @Override
    public List<Film> showOwnWatched(int userId, int page) {
        List<Film> list = new ArrayList<>();
        try {
            list = listDao.getOwnWatched(userId, page, 12);
            for (Film film : list) {
                film.setCasts(castService.getByFilm(film.getId()));
            }
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            return list;
        }
        return list;
    }

    /**
     * @see ListService#showOwnPlanned(int, int)
     */
    @Transactional
    @Override
    public List<Film> showOwnPlanned(int userId, int page) {
        List<Film> list = new ArrayList<>();
        try {
            list = listDao.getOwnPlanned(userId, page, 12);
            for (Film film : list) {
                film.setCasts(castService.getByFilm(film.getId()));
            }
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            return list;
        }
        return list;
    }

    /**
     * @see ListService#showOthersWatched(int, int)
     */
    @Transactional
    @Override
    public List<Film> showOthersWatched(int userId, int page) {
        List<Film> list = new ArrayList<>();
        try {
            list = listDao.getOthersWatched(userId, page, 12);
            for (Film film : list) {
                film.setCasts(castService.getByFilm(film.getId()));
            }
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            return list;
        }
        return list;
    }

    /**
     * @see ListService#showOthersPlanned(int, int)
     */
    @Transactional
    @Override
    public List<Film> showOthersPlanned(int userId, int page) {
        List<Film> list = new ArrayList<>();
        try {
            list = listDao.getOthersPlanned(userId, page, 12);
            for (Film film : list) {
                film.setCasts(castService.getByFilm(film.getId()));
            }
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            return list;
        }
        return list;
    }

    /**
     * @see ListService#makePrivate(int, am.aca.orgflix.entity.Film)
     */
    @Transactional
    @Override
    public boolean makePrivate(int userId, Film film) {
        boolean state;
        try {
            if (!listDao.areRelated(film.getId(), userId))
                return false;
            state = listDao.setFilmPrivacy(film, userId, false);
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            return false;
        }
        return state;
    }

    /**
     * @see ListService#makePublic(int, am.aca.orgflix.entity.Film)
     */
    @Transactional
    @Override
    public boolean makePublic(int userId, Film film) {
        boolean state;
        try {
            if (!listDao.areRelated(film.getId(), userId))
                return false;
            state = listDao.setFilmPrivacy(film, userId, true);
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            return false;
        }
        return state;
    }

    /**
     * @see ListService#totalNumberOfFilmsInAList(int, boolean)
     */
    @Override
    public int[] totalNumberOfFilmsInAList(int userId, boolean isWatched) {
        int[] privacyList;
        try {
            if (isWatched) {
                privacyList = converterIntegerToIntArr(listDao.watchedFilmPrivacyList(userId));
            } else {
                privacyList = converterIntegerToIntArr(listDao.plannedFilmPrivacyList(userId));
            }
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            return new int[0];
        }
        return privacyList;
    }

    /**
     * @see ListService#removeFromWatched(int, int)
     */
    @Transactional
    @Override
    public boolean removeFromWatched(int filmId, int userId) {
        boolean state;
        try {
            if (!listDao.isWatched(filmId, userId))
                return false;
            if (listDao.isPlanned(filmId, userId)) {
                state = listDao.setFilmAsNotWatched(filmId, userId);
            } else state = listDao.removeFilm(filmId, userId);
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            return false;
        }
        return state;
    }

    /**
     * @see ListService#removeFromPlanned(int, int)
     */
    @Transactional
    @Override
    public boolean removeFromPlanned(int filmId, int userId) {
        boolean state;
        try {
            if (!listDao.isPlanned(filmId, userId))
                return false;
            if (listDao.isWatched(filmId, userId))
                state = listDao.setFilmAsNotPlanned(filmId, userId);
            else state = listDao.removeFilm(filmId, userId);
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            return false;
        }
        return state;
    }

    private int[] converterIntegerToIntArr(List<Integer> list) {
        int[] ret = new int[list.size()];
        for (int i = 0; i < ret.length; i++)
            ret[i] = list.get(i);
        return ret;
    }
}
