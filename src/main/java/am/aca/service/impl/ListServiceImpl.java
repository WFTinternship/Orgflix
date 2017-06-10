package am.aca.service.impl;

import am.aca.dao.FilmDao;
import am.aca.dao.ListDao;
import am.aca.dao.impljdbc.FilmDaoJdbc;
import am.aca.dao.impljdbc.ListDaoJdbc;
import am.aca.entity.Film;
import am.aca.service.ListService;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by David on 6/7/2017
 */
public class ListServiceImpl implements ListService {
    private ListDao listDao;

    public ListServiceImpl() {
        listDao = new ListDaoJdbc();
    }

    @Override
    public boolean addToWatched(Film film, boolean isPublic, int user_ID) throws SQLException {
        FilmDao filmDao = new FilmDaoJdbc();

        //case: the film does not exist
        if (filmDao.getFilmById(film.getId()) == null){
            filmDao.addFilm(film);
        }

        //case: any
        return listDao.addToWatched(film,isPublic,user_ID);
    }

    @Override
    public boolean addToWished(Film film, boolean isPublic, int user_ID) throws SQLException {
        FilmDao filmDao = new FilmDaoJdbc();

        //case: the film does not exist
        if (filmDao.getFilmById(film.getId()) == null){
            filmDao.addFilm(film);
        }

        //case: any
        return listDao.addToWished(film,isPublic,user_ID);
    }

    @Override
    public boolean removeFromWatched(Film film, int user_ID) throws SQLException {
        return listDao.removeFromWatched(film,user_ID);
    }

    @Override
    public boolean removeFromWished(Film film, int user_ID) throws SQLException {
        return listDao.removeFromWished(film,user_ID);
    }

    @Override
    public List<Film> showOwnWatched(int user_ID) throws SQLException {
        return listDao.showOwnWatched(user_ID);
    }

    @Override
    public List<Film> showOwnWished(int user_ID) throws SQLException {
        return listDao.showOwnWished(user_ID);
    }

    @Override
    public List<Film> showOthersWatched(int user_ID) throws SQLException {
        return listDao.showOthersWatched(user_ID);
    }

    @Override
    public List<Film> showOthersWished(int user_ID) throws SQLException {
        return listDao.showOthersWished(user_ID);
    }
}
