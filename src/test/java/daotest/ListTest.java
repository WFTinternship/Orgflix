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

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by karine on 6/3/2017
 */
public class ListTest {

    private ListDao listDao;
    private Film film;
    private User user;
//    Director director;
//    private DirectorDao directorDao;

    @Before
    public void setUp() throws SQLException {

        //setup Director
//        directorDao
//        director = new Director();
//        director.setHasOscar(false);
//        director.setName("Brian De Palma");

        ArrayList<Director> dirs = new ArrayList<>();
        dirs.add(new DirectorDaoJdbc().addDirector("Brian De Palma", false));

        //setup Film and Film DAO
        film = new Film();
        film.setTitle("Scarface");
        film.addGeners(Genre.ACTION);
        film.setProdYear(1983);
//        film.setHasOscar(false);
//        film.setRate_1star(0);
//        film.setRate_2star(0);
//        film.setRate_3star(0);
//        film.setRate_4star(0);
        film.setRate_5star(1);
        film.setDirectors(dirs);

        FilmDao filmDao = new FilmDaoJdbc();

        filmDao.addFilm(film);

        //setup User and User DAO
        user = new User("gago","Gagik Petrosyan","gagik@gmail.com","pass");

        UserDao userDao =  new UserDaoJdbc();
        userDao.addUser(user);

        //setup List DAO
        listDao = new ListDaoJdbc();
    }

    @After
    public void revert() throws SQLException {
        TestHelper.emptyTable(new String[]{"genre_to_film", "film_to_director", "lists", "directors", "films", "users"});
    }

    @Test
    public void addNotWishedToWatchedSucceeded() throws SQLException {
        listDao.addToWatched(film, true, user.getId());
        Assert.assertEquals(1, listDao.showOwnWatched(user.getId()).size());
    }

    @Test
    public void AddWishedToWatchedSucceeded() throws SQLException {
        //setup List
        listDao.addToWished(film, true, user.getId());

        listDao.addToWatched(film, true, user.getId());
        Assert.assertEquals(1, listDao.showOwnWatched(user.getId()).size());
    }

    @Test
    public void addNotWatchedToWishedSucceeded() throws SQLException {
        listDao.addToWished(film, true, user.getId());
        Assert.assertEquals(1, listDao.showOwnWished(user.getId()).size());
    }

    @Test
    public void addWatchedToWishedSucceeded() throws SQLException {
        //setup List
        listDao.addToWatched(film, true, user.getId());

        listDao.addToWished(film, true, user.getId());
        Assert.assertEquals(1, listDao.showOwnWatched(user.getId()).size());
    }

    @Test
    public void removeNotWatchedFromWishedSucceeded() throws SQLException {
        //setup List
        listDao.addToWished(film, true, user.getId());

        listDao.removeFromWished(film, user.getId());
        Assert.assertEquals(listDao.showOwnWished(user.getId()).size(), 0);
    }

    @Test
    public void removeWatchedFromWishedSucceeded() throws SQLException {
        //setup List
        listDao.addToWished(film, true, user.getId());
        listDao.addToWatched(film, true, user.getId());

        listDao.removeFromWished(film, user.getId());
        Assert.assertEquals(1, listDao.showOwnWatched(user.getId()).size());
    }

    @Test
    public void removeNotWishedFromWatchedSucceeded() throws SQLException {
        //setup List
        listDao.addToWatched(film, true, user.getId());

        listDao.removeFromWatched(film, user.getId());
        Assert.assertEquals(listDao.showOwnWished(user.getId()).size(), 0);
    }

    @Test
    public void removeWishedFromWatchedSucceeded() throws SQLException {
        //setup List
        listDao.addToWished(film, true, user.getId());
        listDao.addToWatched(film, true, user.getId());

        listDao.removeFromWatched(film, user.getId());
        Assert.assertEquals(listDao.showOwnWished(user.getId()).size(), 1);
    }

    @Test
    public void removeNotWatchedFromWishedFailed() throws SQLException {
        //List setup, empty list

        Assert.assertFalse(listDao.removeFromWished(film, user.getId()));
    }

    @Test
    public void removeWatchedFromWishedFailed() throws SQLException {
        //setup List
        listDao.addToWatched(film, true, user.getId());

        Assert.assertFalse(listDao.removeFromWished(film, user.getId()));
    }

    @Test
    public void removeNotWishedFromWatchedFailed() throws SQLException {
        //List setup, empty list

        Assert.assertFalse(listDao.removeFromWatched(film, user.getId()));
    }

    @Test
    public void removeWishedFromWatchedFailed() throws SQLException {
        //setup List
        listDao.addToWished(film, true, user.getId());

        Assert.assertFalse(listDao.removeFromWatched(film, user.getId()));
    }

    @Test
    public void showOthersWatchedFailed() throws SQLException {
        //setup List
        listDao.addToWished(film, false, user.getId());

        Assert.assertEquals(0, listDao.showOthersWished(user.getId()).size());
    }

    @Test
    public void showOthersWishedFailed() throws SQLException {
        //setup List
        listDao.addToWatched(film, false, user.getId());

        Assert.assertEquals(0, listDao.showOthersWatched(user.getId()).size());
    }
}
