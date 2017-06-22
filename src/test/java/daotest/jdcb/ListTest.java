package daotest.jdcb;

import am.aca.dao.jdbc.FilmDAO;
import am.aca.dao.jdbc.ListDao;
import am.aca.dao.jdbc.UserDAO;
import am.aca.dao.jdbc.impljdbc.*;
import am.aca.dao.jdbc.CastDAO;
import am.aca.dao.jdbc.impljdbc.JdbcCastDAO;
import am.aca.entity.Cast;
import am.aca.entity.Film;
import am.aca.entity.Genre;
import am.aca.entity.User;
import am.aca.util.ConnType;
import am.aca.util.DbManager;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by karine on 6/3/2017
 */
public class ListTest {

    ApplicationContext ctx =
            new ClassPathXmlApplicationContext("applicationContext-persistance-test.xml");

    private UserDAO userDao = ctx.getBean("JdbcUserDAO",JdbcUserDAO.class);
    private CastDAO castDAO = ctx.getBean("JdbcCastDAO",JdbcCastDAO.class);
    private FilmDAO filmDAO = ctx.getBean("JdbcFilmDAO", JdbcFilmDAO.class);

    private ListDao listDao = new ListDaoJdbc(ConnType.PRODUCTION);

    private Film film;
    private User user;
    private ArrayList<Cast> actors = new ArrayList<>();

    @Before
    public void setUp() {

        //setup Cast
        actors.add(castDAO.addCast("Brian De Palma", false));

        //setup Film and Film DAO
        film = new Film();
        film.setTitle("Scarface");
        film.addGeners(Genre.ACTION);
        film.setProdYear(1983);
        film.setRate_5star(1);
        film.setCasts(actors);

        filmDAO.addFilm(film);

        //setup User and User DAO
        user = new User("MrSmith","John Smith","JhonSmith@gmail.com","pass");

        userDao.add(user);

        //setup List DAO
        listDao.setDataSource(DbManager.getInstance().getDataSource());
    }

    @After
    public void revert() throws SQLException, IOException, PropertyVetoException {
        DbManager.emptyTestTables(new String[]{"genre_to_film", "film_to_cast", "lists", "casts", "films", "users"});
    }

    @Test
    public void areRelated_Success() {
        listDao.insertWatched(film, user.getId(), true);
        Assert.assertEquals(true, listDao.areRelated(film, user.getId()));
    }

    @Test
    public void areRelated_Fail() {
        Assert.assertEquals(false, listDao.areRelated(film, user.getId()));
    }

    @Test
    public void updateWatched_Success() {
        listDao.insertPlanned(film, user.getId(), true);
        listDao.updateWatched(film, user.getId());
        Assert.assertEquals(true, listDao.isWatched(film, user.getId()));
    }

    @Test
    public void updateWatched_Fail() {
        listDao.updateWatched(film, user.getId());
        Assert.assertEquals(false, listDao.isWatched(film, user.getId()));
    }

    @Test
    public void updatePlanned_Success() {
        listDao.insertWatched(film, user.getId(), true);
        listDao.updatePlanned(film, user.getId());
        Assert.assertEquals(true, listDao.isPlanned(film, user.getId()));
    }

    @Test
    public void updatePlanned_Fail() {
        listDao.updatePlanned(film, user.getId());
        Assert.assertEquals(false, listDao.isPlanned(film, user.getId()));
    }

    @Test
    public void insertWatched_Success() {
        listDao.insertWatched(film, user.getId(), true);
        Assert.assertEquals(true, listDao.isWatched(film, user.getId()));
    }

    @Test
    public void insertPlanned_Success() {
        listDao.insertPlanned(film, user.getId(), true);
        Assert.assertEquals(true, listDao.isPlanned(film, user.getId()));
    }

    @Test
    public void isWatched_Success() {
        listDao.insertWatched(film, user.getId(), true);
        Assert.assertEquals(true, listDao.isWatched(film, user.getId()));
    }

    @Test
    public void isWatched_Fail() {
        Assert.assertEquals(false, listDao.isWatched(film, user.getId()));
    }

    @Test
    public void isPlanned_Success() {
        listDao.insertPlanned(film, user.getId(), true);
        Assert.assertEquals(true, listDao.isPlanned(film, user.getId()));
    }

    @Test
    public void isPlanned_Fail() {
        Assert.assertEquals(false, listDao.isPlanned(film, user.getId()));
    }

    @Test
    public void resetWatched_Success() {
        listDao.insertWatched(film, user.getId(), true);
        listDao.resetWatched(film, user.getId());
        Assert.assertEquals(false, listDao.isWatched(film, user.getId()));
    }

    @Test
    public void resetPlanned_Success() {
        listDao.insertPlanned(film, user.getId(), false);
        listDao.resetPlanned(film, user.getId());
        Assert.assertEquals(false, listDao.isPlanned(film, user.getId()));
    }

    @Test
    public void remove_Succedd() {
        listDao.insertWatched(film, user.getId(), true);
        listDao.removeFilm(film, user.getId());
        Assert.assertEquals(false, listDao.isWatched(film, user.getId()));
    }

    @Test
    public void removeBoth_Success() {
        listDao.insertWatched(film, user.getId(), true);
        listDao.insertPlanned(film, user.getId(), true);
        listDao.removeFilm(film, user.getId());
        Assert.assertEquals(false, listDao.isWatched(film, user.getId()));
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
