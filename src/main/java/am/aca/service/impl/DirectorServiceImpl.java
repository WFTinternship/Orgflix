package am.aca.service.impl;

import am.aca.dao.CastDao;
import am.aca.dao.impljdbc.CastDaoJdbc;
import am.aca.entity.Cast;
import am.aca.entity.Film;
import am.aca.service.DirectorService;

import java.util.List;

/**
 * Created by David on 6/7/2017
 */
public class DirectorServiceImpl implements DirectorService {
    private CastDao CastDao;

    public DirectorServiceImpl() {
        CastDao = new CastDaoJdbc();
    }

    @Override
    public Cast addDirector(String director, boolean hasOscar) {
        return CastDao.addCast(director,hasOscar);
    }

    @Override
    public Cast addDirector(String director) {
        return CastDao.addCast(director);
    }

    @Override
    public boolean addDirectorToFilm(Cast cast, Film film) {
        return CastDao.addCastToFilm(cast,film);
    }

    @Override
    public boolean addDirectorToFilm(int directorId, int filmId) {
        return CastDao.addCastToFilm(directorId,filmId);
    }

    @Override
    public boolean editDirector(Cast cast) {
        return CastDao.editCast(cast);
    }

    @Override
    public List<Cast> listDirectors() {
        return CastDao.listCast();
    }

    @Override
    public List<Integer> listFilmsIdByDirector(int directorId) {
        return CastDao.listFilmsIdByCast(directorId);
    }
}
