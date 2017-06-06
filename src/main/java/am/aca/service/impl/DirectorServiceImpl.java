package am.aca.service.impl;

import am.aca.dao.DirectorDao;
import am.aca.dao.impljdbc.DirectorDaoJdbc;
import am.aca.entity.Director;
import am.aca.entity.Film;
import am.aca.service.DirectorService;

import java.util.List;

/**
 * Created by David on 6/7/2017
 */
public class DirectorServiceImpl implements DirectorService {
    private DirectorDao directorDao;

    public DirectorServiceImpl() {
        directorDao = new DirectorDaoJdbc();
    }

    @Override
    public Director addDirector(String director, boolean hasOscar) {
        return directorDao.addDirector(director,hasOscar);
    }

    @Override
    public Director addDirector(String director) {
        return directorDao.addDirector(director);
    }

    @Override
    public boolean addDirectorToFilm(Director director, Film film) {
        return directorDao.addDirectorToFilm(director,film);
    }

    @Override
    public boolean addDirectorToFilm(int directorId, int filmId) {
        return directorDao.addDirectorToFilm(directorId,filmId);
    }

    @Override
    public boolean editDirector(Director director) {
        return directorDao.editDirector(director);
    }

    @Override
    public List<Director> listDirectors() {
        return directorDao.listDirectors();
    }

    @Override
    public List<Integer> listFilmsIdByDirector(int directorId) {
        return directorDao.listFilmsIdByDirector(directorId);
    }
}
