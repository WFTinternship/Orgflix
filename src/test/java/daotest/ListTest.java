package daotest;

import am.aca.dao.*;
import am.aca.dao.impljdbc.*;
import am.aca.entity.Cast;
import am.aca.entity.Film;
import am.aca.entity.Genre;
import am.aca.entity.User;
import am.aca.util.DbManager;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by karine on 6/3/2017
 */
public class ListTest {

    private ListDao listDao;
    private Film film;
    private User user;
    private ArrayList<Cast> dirs = new ArrayList<>();

    @Before
    public void setUp() {

        //setup Cast
        dirs.add(new CastDaoJdbc().addCast("Brian De Palma", false));

        //setup Film and Film DAO
        film = new Film();
        film.setTitle("Scarface");
        film.addGeners(Genre.ACTION);
        film.setProdYear(1983);
        film.setRate_5star(1);
        film.setCasts(dirs);

        FilmDao filmDao = new FilmDaoJdbc();

        filmDao.addFilm(film);

        //setup User and User DAO
        user = new User("MrSmith","John Smith","JhonSmith@gmail.com","pass");

        UserDao userDao =  new UserDaoJdbc();
        userDao.addUser(user);

        //setup List DAO
        listDao = new ListDaoJdbc();
    }

    @After
    public void revert() {
        DbManager.emptyTable(new String[]{"genre_to_film", "film_to_cast", "lists", "casts", "films", "users"});
    }

    @Test
    public void addNotWishedToWatchedSucceeded() {
        listDao.addToWatched(film, true, user.getId());
        Assert.assertEquals(1, listDao.showOwnWatched(user.getId()).size());
    }

    @Test
    public void AddWishedToWatchedSucceeded() {
        //setup List
        listDao.addToWished(film, true, user.getId());

        listDao.addToWatched(film, true, user.getId());
        Assert.assertEquals(1, listDao.showOwnWatched(user.getId()).size());
    }

    @Test
    public void addNotWatchedToWishedSucceeded() {
        listDao.addToWished(film, true, user.getId());
        Assert.assertEquals(1, listDao.showOwnWished(user.getId()).size());
    }

    @Test
    public void addWatchedToWishedSucceeded() {
        //setup List
        listDao.addToWatched(film, true, user.getId());

        listDao.addToWished(film, true, user.getId());
        Assert.assertEquals(1, listDao.showOwnWatched(user.getId()).size());
    }

    @Test
    public void removeNotWatchedFromWishedSucceeded() {
        //setup List
        listDao.addToWished(film, true, user.getId());

        listDao.removeFromWished(film, user.getId());
        Assert.assertEquals(0,listDao.showOwnWished(user.getId()).size());
    }

    @Test
    public void removeWatchedFromWishedSucceeded() {
        //setup List
        listDao.addToWished(film, true, user.getId());
        listDao.addToWatched(film, true, user.getId());

        listDao.removeFromWished(film, user.getId());
        Assert.assertEquals(1, listDao.showOwnWatched(user.getId()).size());
    }

    @Test
    public void removeNotWishedFromWatchedSucceeded() {
        //setup List
        listDao.addToWatched(film, true, user.getId());

        listDao.removeFromWatched(film, user.getId());
        Assert.assertEquals(listDao.showOwnWatched(user.getId()).size(), 0);
    }

    @Test
    public void removeWishedFromWatchedSucceeded() {
        //setup List
        listDao.addToWished(film, true, user.getId());
        listDao.addToWatched(film, true, user.getId());

        listDao.removeFromWatched(film, user.getId());
        Assert.assertEquals(listDao.showOwnWished(user.getId()).size(), 1);
    }

    @Test
    public void removeNotWatchedFromWishedFailed() {
        //List setup, empty list

        Assert.assertFalse(listDao.removeFromWished(film, user.getId()));
    }

    @Test
    public void removeWatchedFromWishedFailed() {
        //setup List
        listDao.addToWatched(film, true, user.getId());

        Assert.assertFalse(listDao.removeFromWished(film, user.getId()));
    }

    @Test
    public void removeNotWishedFromWatchedFailed() {
        //List setup, empty list

        Assert.assertFalse(listDao.removeFromWatched(film, user.getId()));
    }

    @Test
    public void removeWishedFromWatchedFailed() {
        //setup List
        listDao.addToWished(film, true, user.getId());

        Assert.assertFalse(listDao.removeFromWatched(film, user.getId()));
    }

    @Test
    public void showOthersWatchedFailed() {
        //setup List
        listDao.addToWished(film, false, user.getId());

        Assert.assertEquals(0, listDao.showOthersWished(user.getId()).size());
    }

    @Test
    public void showOthersWishedFailed() {
        //setup List
        listDao.addToWatched(film, false, user.getId());

        Assert.assertEquals(0, listDao.showOthersWatched(user.getId()).size());
    }
}
