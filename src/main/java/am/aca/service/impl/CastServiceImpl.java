package am.aca.service.impl;

import am.aca.dao.CastDao;
import am.aca.dao.impljdbc.CastDaoJdbc;
import am.aca.entity.Cast;
import am.aca.entity.Film;
import am.aca.service.CastService;

import java.util.List;

/**
 * Created by David on 6/7/2017
 */
public class CastServiceImpl implements CastService {
    private CastDao CastDao;

    public CastServiceImpl() {
        CastDao = new CastDaoJdbc();
    }

    @Override
    public Cast addCast(String cast, boolean hasOscar) {
        return CastDao.addCast(cast,hasOscar);
    }

    @Override
    public Cast addCast(String cast) {
        return CastDao.addCast(cast);
    }

    @Override
    public boolean addCastToFilm(Cast cast, Film film) {
        return CastDao.addCastToFilm(cast,film);
    }

    @Override
    public boolean addCastToFilm(int castId, int filmId) {
        return CastDao.addCastToFilm(castId,filmId);
    }

    @Override
    public boolean editCast(Cast cast) {
        return CastDao.editCast(cast);
    }

    @Override
    public List<Cast> listCasts() {
        return CastDao.listCast();
    }

    @Override
    public List<Integer> listFilmsIdByCast(int castId) {
        return CastDao.listFilmsIdByCast(castId);
    }
}
