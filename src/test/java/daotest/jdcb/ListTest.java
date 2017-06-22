package daotest.jdcb;

import am.aca.dao.jdbc.CastDAO;
import am.aca.dao.jdbc.FilmDAO;
import am.aca.dao.jdbc.ListDao;
import am.aca.dao.jdbc.UserDAO;
import am.aca.dao.jdbc.impljdbc.JdbcCastDAO;
import am.aca.dao.jdbc.impljdbc.JdbcFilmDAO;
import am.aca.dao.jdbc.impljdbc.JdbcUserDAO;
import am.aca.dao.jdbc.impljdbc.JdbcListDAO;
import am.aca.entity.Cast;
import am.aca.entity.Film;
import am.aca.entity.Genre;
import am.aca.util.TestHelper;
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

    private ListDao listDao = ctx.getBean("ListDaoJdbc", JdbcListDAO.class);

    private Film film;
    private int userId;
    private ArrayList<Cast> actors = new ArrayList<>();

    private TestHelper testHelper = ctx.getBean("TestHelper", TestHelper.class);
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

        userId = userDao.add("MrSmith","John Smith","JhonSmith@gmail.com","pass");

    }

    @After
    public void revert() throws SQLException, IOException, PropertyVetoException {
        testHelper.emptyTable(new String[]{"genre_to_film", "film_to_cast", "lists", "casts", "films", "users"});
    }

    @Test
    public void areRelated_Success() {
        listDao.insertWatched(film, userId, true);
        Assert.assertEquals(true, listDao.areRelated(film, userId));
    }

    @Test
    public void areRelated_Fail() {
        Assert.assertEquals(false, listDao.areRelated(film, userId));
    }

    @Test
    public void updateWatched_Success() {
        listDao.insertPlanned(film, userId, true);
        listDao.updateWatched(film, userId);
        Assert.assertEquals(true, listDao.isWatched(film, userId));
    }

    @Test
    public void updateWatched_Fail() {
        listDao.updateWatched(film, userId);
        Assert.assertEquals(false, listDao.isWatched(film, userId));
    }

    @Test
    public void updatePlanned_Success() {
        listDao.insertWatched(film, userId, true);
        listDao.updatePlanned(film, userId);
        Assert.assertEquals(true, listDao.isPlanned(film, userId));
    }

    @Test
    public void updatePlanned_Fail() {
        listDao.updatePlanned(film, userId);
        Assert.assertEquals(false, listDao.isPlanned(film, userId));
    }

    @Test
    public void insertWatched_Success() {
        listDao.insertWatched(film, userId, true);
        Assert.assertEquals(true, listDao.isWatched(film, userId));
    }

    @Test
    public void insertPlanned_Success() {
        listDao.insertPlanned(film, userId, true);
        Assert.assertEquals(true, listDao.isPlanned(film, userId));
    }

    @Test
    public void isWatched_Success() {
        listDao.insertWatched(film, userId, true);
        Assert.assertEquals(true, listDao.isWatched(film, userId));
    }

    @Test
    public void isWatched_Fail() {
        Assert.assertEquals(false, listDao.isWatched(film, userId));
    }

    @Test
    public void isPlanned_Success() {
        listDao.insertPlanned(film, userId, true);
        Assert.assertEquals(true, listDao.isPlanned(film, userId));
    }

    @Test
    public void isPlanned_Fail() {
        Assert.assertEquals(false, listDao.isPlanned(film, userId));
    }

    @Test
    public void resetWatched_Success() {
        listDao.insertWatched(film, userId, true);
        listDao.resetWatched(film, userId);
        Assert.assertEquals(false, listDao.isWatched(film, userId));
    }

    @Test
    public void resetPlanned_Success() {
        listDao.insertPlanned(film, userId, false);
        listDao.resetPlanned(film, userId);
        Assert.assertEquals(false, listDao.isPlanned(film, userId));
    }

    @Test
    public void remove_Succedd() {
        listDao.insertWatched(film, userId, true);
        listDao.removeFilm(film, userId);
        Assert.assertEquals(false, listDao.isWatched(film, userId));
    }

    @Test
    public void removeBoth_Success() {
        listDao.insertWatched(film, userId, true);
        listDao.insertPlanned(film, userId, true);
        listDao.removeFilm(film, userId);
        Assert.assertEquals(false, listDao.isWatched(film, userId));
    }

    @Test
    public void addNotWishedToWatchedSucceeded() {
        listDao.addToWatched(film, true, userId);
        Assert.assertEquals(1, listDao.showOwnWatched(userId).size());
    }

    @Test
    public void addWishedToWatchedSucceeded() {
        //setup List
        listDao.addToWished(film, true, userId);

        listDao.addToWatched(film, true, userId);
        Assert.assertEquals(1, listDao.showOwnWatched(userId).size());
    }

    @Test
    public void addNotWatchedToWishedSucceeded() {
        listDao.addToWished(film, true, userId);
        Assert.assertEquals(1, listDao.showOwnPlanned(userId).size());
    }

    @Test
    public void addWatchedToWishedSucceeded() {
        //setup List
        listDao.addToWatched(film, true, userId);

        listDao.addToWished(film, true, userId);
        Assert.assertEquals(1, listDao.showOwnWatched(userId).size());
    }

    @Test
    public void removeNotWatchedFromWishedSucceeded() {
        //setup List
        listDao.addToWished(film, true, userId);

        listDao.removeFromPlanned(film, userId);
        Assert.assertEquals(0,listDao.showOwnPlanned(userId).size());
    }

    @Test
    public void removeWatchedFromWishedSucceeded() {
        //setup List
        listDao.addToWished(film, true, userId);
        listDao.addToWatched(film, true, userId);

        listDao.removeFromPlanned(film, userId);
        Assert.assertEquals(1, listDao.showOwnWatched(userId).size());
    }

    @Test
    public void removeNotWishedFromWatchedSucceeded() {
        //setup List
        listDao.addToWatched(film, true, userId);

        listDao.removeFromWatched(film, userId);
        Assert.assertEquals(listDao.showOwnWatched(userId).size(), 0);
    }

    @Test
    public void removeWishedFromWatchedSucceeded() {
        //setup List
        listDao.addToWished(film, true, userId);
        listDao.addToWatched(film, true, userId);

        listDao.removeFromWatched(film, userId);
        Assert.assertEquals(listDao.showOwnPlanned(userId).size(), 1);
    }

    @Test
    public void removeNotWatchedFromWishedFailed() {
        //List setup, empty list

        Assert.assertFalse(listDao.removeFromPlanned(film, userId));
    }

    @Test
    public void removeWatchedFromWishedFailed() {
        //setup List
        listDao.addToWatched(film, true, userId);

        Assert.assertFalse(listDao.removeFromPlanned(film, userId));
    }

    @Test
    public void removeNotWishedFromWatchedFailed() {
        //List setup, empty list

        Assert.assertFalse(listDao.removeFromWatched(film, userId));
    }

    @Test
    public void removeWishedFromWatchedFailed() {
        //setup List
        listDao.addToWished(film, true, userId);

        Assert.assertFalse(listDao.removeFromWatched(film, userId));
    }

    @Test
    public void showOthersWatchedFailed() {
        //setup List
        listDao.addToWished(film, false, userId);

        Assert.assertEquals(0, listDao.showOthersPlanned(userId).size());
    }

    @Test
    public void showOthersWishedFailed() {
        //setup List
        listDao.addToWatched(film, false, userId);

        Assert.assertEquals(0, listDao.showOthersWatched(userId).size());
    }
}
