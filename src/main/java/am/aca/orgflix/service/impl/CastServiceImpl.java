package am.aca.orgflix.service.impl;

import am.aca.orgflix.dao.CastDAO;
import am.aca.orgflix.entity.Cast;
import am.aca.orgflix.service.CastService;
import am.aca.orgflix.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Service layer for actor related methods
 */
@Transactional(readOnly = true)
@Service
public class CastServiceImpl extends BaseService implements CastService {

    private CastDAO castDAO;

    public CastServiceImpl() {
        // class name to include in logging
        super(CastServiceImpl.class);
    }

    @Autowired
    public void setCastDAO(CastDAO castDAO) {
        this.castDAO = castDAO;
    }

    /**
     * @see CastService#add(am.aca.orgflix.entity.Cast)
     */
    @Transactional
    @Override
    public boolean add(Cast cast) {
        checkRequiredFields(cast.getName());
        boolean result;
        try {
            if (castDAO.exists(cast.getName()))
                throw new ServiceException("Actor with the same name already exists");
            result = castDAO.add(cast);
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            return false;
        }
        return result;
    }


    /**
     * @see CastService#addCastsToFilm(int, int[])
     */
    @Transactional
    @Override
    public List<Cast> addCastsToFilm(int filmId, int[] actorIds) {
        List<Cast> castList = new ArrayList<>();
        for (int id : actorIds) {
            Cast cast = getById(id);
            castList.add(cast);
            addToFilm(cast, filmId);
        }
        return castList;
    }

    /**
     * @see CastService#addToFilm(Cast, int)
     */
    @Transactional
    @Override
    public boolean addToFilm(Cast cast, int filmId) {
        boolean state;
        try {
            if (castDAO.isRelatedToFilm(cast.getId(), filmId))
                return false;

            state = castDAO.addToFilm(cast, filmId);
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            return false;
        }
        return state;
    }

    /**
     * @see CastService#getAll()
     */
    @Override
    public List<Cast> getAll() {
        List<Cast> list = new ArrayList<>();
        try {
            list = castDAO.getAll();
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            return list;
        }
        return list;
    }

    /**
     * @see CastService#getByFilm(int)
     */
    @Override
    public List<Cast> getByFilm(int filmId) {
        List<Cast> list = new ArrayList<>();
        try {
            list = castDAO.getByFilm(filmId);
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            return list;
        }
        return list;
    }

    /**
     * @see CastService#getById(int)
     */
    @Override
    public Cast getById(int castId) {
        Cast cast;
        try {
            cast = castDAO.getById(castId);
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            return null;
        }
        return cast;
    }

    /**
     * @see CastService#edit(Cast)
     */
    @Transactional
    @Override
    public boolean edit(Cast cast) {
        checkRequiredFields(cast.getName());
        boolean state;
        try {
            if (!castDAO.exists(cast))
                return false;
            state = castDAO.edit(cast);
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            return false;
        }
        return state;
    }
}