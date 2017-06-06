package daotest;

import am.aca.dao.*;
import am.aca.entity.Director;
import am.aca.entity.Film;
import am.aca.entity.Genre;
import am.aca.entity.User;
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
    private ArrayList<Director> dirs = new ArrayList<>();

    @Before
    public void setUp() throws SQLException {

        //setup Director
        dirs.add(new DirectorDaoJdbc().addDirector("Brian De Palma", false));

        //setup Film and Film DAO
        film = new Film();
        film.setTitle("Scarface");
        film.addGeners(Genre.ACTION);
        film.setProdYear(1983);
        film.setRate_5star(1);
        film.setDirectors(dirs);

        FilmDao filmDao = new FilmDaoJdbc();

        try {
            filmDao.addFilm(film);
        } catch (IOException | PropertyVetoException e) {
            e.printStackTrace();
        }

        //setup User and User DAO
        user = new User("MrSmith","John Smith","JhonSmith@gmail.com","pass");

        UserDao userDao =  new UserDaoJdbc();
        userDao.addUser(user);

        //setup List DAO
        listDao = new ListDaoJdbc();
    }

    @After
    public void revert() throws SQLException, IOException, PropertyVetoException {
        TestHelper.emptyTable(new String[]{"genre_to_film", "film_to_director", "lists", "directors", "films", "users"});
    }

    @Test
    public void addNotWishedToWatchedSucceeded() throws SQLException, IOException, PropertyVetoException {
        listDao.addToWatched(film, true, user.getId());
        Assert.assertEquals(1, listDao.showOwnWatched(user.getId()).size());
    }

    @Test
    public void AddWishedToWatchedSucceeded() throws SQLException, IOException, PropertyVetoException {
        //setup List
        listDao.addToWished(film, true, user.getId());

        listDao.addToWatched(film, true, user.getId());
        Assert.assertEquals(1, listDao.showOwnWatched(user.getId()).size());
    }

    @Test
    public void addNotWatchedToWishedSucceeded() throws SQLException, IOException, PropertyVetoException {
        listDao.addToWished(film, true, user.getId());
        Assert.assertEquals(1, listDao.showOwnWished(user.getId()).size());
    }

    @Test
    public void addWatchedToWishedSucceeded() throws SQLException, IOException, PropertyVetoException {
        //setup List
        listDao.addToWatched(film, true, user.getId());

        listDao.addToWished(film, true, user.getId());
        Assert.assertEquals(1, listDao.showOwnWatched(user.getId()).size());
    }

    @Test
    public void removeNotWatchedFromWishedSucceeded() throws SQLException, IOException, PropertyVetoException {
        //setup List
        listDao.addToWished(film, true, user.getId());

        listDao.removeFromWished(film, user.getId());
        Assert.assertEquals(listDao.showOwnWished(user.getId()).size(), 0);
    }

    @Test
    public void removeWatchedFromWishedSucceeded() throws SQLException, IOException, PropertyVetoException {
        //setup List
        listDao.addToWished(film, true, user.getId());
        listDao.addToWatched(film, true, user.getId());

        listDao.removeFromWished(film, user.getId());
        Assert.assertEquals(1, listDao.showOwnWatched(user.getId()).size());
    }

    @Test
    public void removeNotWishedFromWatchedSucceeded() throws SQLException, IOException, PropertyVetoException {
        //setup List
        listDao.addToWatched(film, true, user.getId());

        listDao.removeFromWatched(film, user.getId());
        Assert.assertEquals(listDao.showOwnWished(user.getId()).size(), 0);
    }

    @Test
    public void removeWishedFromWatchedSucceeded() throws SQLException, IOException, PropertyVetoException {
        //setup List
        listDao.addToWished(film, true, user.getId());
        listDao.addToWatched(film, true, user.getId());

        listDao.removeFromWatched(film, user.getId());
        Assert.assertEquals(listDao.showOwnWished(user.getId()).size(), 1);
    }

    @Test
    public void removeNotWatchedFromWishedFailed() throws SQLException, IOException, PropertyVetoException {
        //List setup, empty list

        Assert.assertFalse(listDao.removeFromWished(film, user.getId()));
    }

    @Test
    public void removeWatchedFromWishedFailed() throws SQLException, IOException, PropertyVetoException {
        //setup List
        listDao.addToWatched(film, true, user.getId());

        Assert.assertFalse(listDao.removeFromWished(film, user.getId()));
    }

    @Test
    public void removeNotWishedFromWatchedFailed() throws SQLException, IOException, PropertyVetoException {
        //List setup, empty list

        Assert.assertFalse(listDao.removeFromWatched(film, user.getId()));
    }

    @Test
    public void removeWishedFromWatchedFailed() throws SQLException, IOException, PropertyVetoException {
        //setup List
        listDao.addToWished(film, true, user.getId());

        Assert.assertFalse(listDao.removeFromWatched(film, user.getId()));
    }

    @Test
    public void showOthersWatchedFailed() throws SQLException, IOException, PropertyVetoException {
        //setup List
        listDao.addToWished(film, false, user.getId());

        Assert.assertEquals(0, listDao.showOthersWished(user.getId()).size());
    }

    @Test
    public void showOthersWishedFailed() throws SQLException, IOException, PropertyVetoException {
        //setup List
        listDao.addToWatched(film, false, user.getId());

        Assert.assertEquals(0, listDao.showOthersWatched(user.getId()).size());
    }
}
