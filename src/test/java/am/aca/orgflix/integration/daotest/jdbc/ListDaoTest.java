package am.aca.orgflix.integration.daotest.jdbc;

import am.aca.dao.jdbc.FilmDAO;
import am.aca.dao.jdbc.ListDao;
import am.aca.dao.jdbc.UserDAO;
import am.aca.entity.Film;
import am.aca.entity.User;
import am.aca.orgflix.BaseIntegrationTest;
import am.aca.util.TestHelper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Test for List DAO methods
 */
public class ListDaoTest extends BaseIntegrationTest {

    @Autowired
    private UserDAO jdbcUserDAO;

    @Autowired
    private FilmDAO jdbcFilmDAO;

    @Autowired
    private ListDao listDaoJdbc;

    @Autowired
    private TestHelper helper;

    private Film film;
    private int filmId;
    private int userId;

    @Before
    public void setUp() {

        //setup Cast

        film = new Film();
        film.setTitle("Scarface");
        film.setProdYear(1983);

        jdbcFilmDAO.addFilm(film);
        filmId = film.getId();
        //setup User and User DAO

        userId = jdbcUserDAO.add(new User("MrSmith","John Smith","JhonSmith@gmail.com","pass"));

    }

    @After
    public void revert() throws SQLException, IOException, PropertyVetoException {
        helper.emptyTable(new String[]{ "lists", "films", "users"});
    }

    @Test
    public void insertWatched_Success() {
        listDaoJdbc.insertWatched(filmId, userId, true);
        Assert.assertEquals(true, listDaoJdbc.isWatched(filmId, userId));
    }

    @Test
    public void insertPlanned_Success() {
        listDaoJdbc.insertPlanned(filmId, userId, true);
        Assert.assertEquals(true, listDaoJdbc.isPlanned(filmId, userId));
    }

    @Test
    public void showOwnWatched_Success() {
        listDaoJdbc.insertWatched(filmId, userId, false);
        Assert.assertEquals(film, listDaoJdbc.showOwnWatched(userId,0).get(0));
    }

    @Test
    public void showOwnWatched_Partial() {
        listDaoJdbc.insertWatched(filmId, userId, false);
        listDaoJdbc.insertWatched(filmId, userId, true);
        Assert.assertEquals(2, listDaoJdbc.showOwnWatched(userId,0).size());
    }

    @Test
    public void showOwnWatched_Fail() {
        Assert.assertTrue(listDaoJdbc.showOwnWatched(userId,0).isEmpty());
    }

    @Test
    public void showOwnPlanned_Success() {
        listDaoJdbc.insertPlanned(filmId, userId, false);
        Assert.assertEquals(film, listDaoJdbc.showOwnPlanned(userId,0).get(0));
    }

    @Test
    public void showOwnPlanned_Partial() {
        listDaoJdbc.insertPlanned(filmId, userId, false);
        listDaoJdbc.insertPlanned(filmId, userId, true);
        Assert.assertEquals(2, listDaoJdbc.showOwnPlanned(userId,0).size());
    }

    @Test
    public void showOwnPlanned_Fail() {
        Assert.assertTrue(listDaoJdbc.showOwnPlanned(userId,0).isEmpty());
    }

    @Test
    public void showOthersWatched_Success() {
        listDaoJdbc.insertWatched(filmId, userId, true);
        listDaoJdbc.insertWatched(filmId, userId, true);
        Assert.assertEquals(2, listDaoJdbc.showOthersWatched(userId,0).size());
    }

    @Test
    public void showOthersWatched_Partial() {
        listDaoJdbc.insertWatched(filmId, userId, true);
        listDaoJdbc.insertWatched(filmId, userId, false);
        Assert.assertEquals(1, listDaoJdbc.showOthersWatched(userId,0).size());
    }

    @Test
    public void showOthersWatched_Fail() {
        //setup List
        listDaoJdbc.insertWatched(filmId, userId, false);

        Assert.assertEquals(0, listDaoJdbc.showOthersWatched(userId,0).size());
    }

    @Test
    public void showOthersPlanned_Success() {
        listDaoJdbc.insertPlanned(filmId, userId, true);
        listDaoJdbc.insertPlanned(filmId, userId, true);
        Assert.assertEquals(2, listDaoJdbc.showOthersPlanned(userId,0).size());
    }

    @Test
    public void showOthersPlanned_Partial() {
        listDaoJdbc.insertPlanned(filmId, userId, true);
        listDaoJdbc.insertPlanned(filmId, userId, false);
        Assert.assertEquals(1, listDaoJdbc.showOthersPlanned(userId,0).size());
    }

    @Test
    public void showOthersPlanned_Fail() {
        //setup List
        listDaoJdbc.insertPlanned(filmId, userId, false);

        Assert.assertEquals(0, listDaoJdbc.showOthersPlanned(userId,0).size());
    }

    @Test
    public void updateWatched_Success() {
        listDaoJdbc.insertPlanned(filmId, userId, true);
        listDaoJdbc.updateWatched(filmId, userId);
        Assert.assertEquals(true, listDaoJdbc.isWatched(filmId, userId));
    }

    @Test
    public void updateWatched_Fail() {
        listDaoJdbc.updateWatched(filmId, userId);
        Assert.assertEquals(false, listDaoJdbc.isWatched(filmId, userId));
    }

    @Test
    public void updatePlanned_Success() {
        listDaoJdbc.insertWatched(filmId, userId, true);
        listDaoJdbc.updatePlanned(filmId, userId);
        Assert.assertEquals(true, listDaoJdbc.isPlanned(filmId, userId));
    }

    @Test
    public void updatePlanned_Fail() {
        listDaoJdbc.updatePlanned(filmId, userId);
        Assert.assertEquals(false, listDaoJdbc.isPlanned(filmId, userId));
    }

    @Test
    public void changePrivacy_Success() {
        listDaoJdbc.insertPlanned(filmId, userId, true);
        Assert.assertTrue(listDaoJdbc.changePrivacy(film, userId, false));
    }

    @Test
    public void changePrivacyToPrivate_Success() {
        listDaoJdbc.insertWatched(filmId, userId, true);
        listDaoJdbc.changePrivacy(film, userId, false);
        Assert.assertTrue(listDaoJdbc.showOthersWatched(userId,0).isEmpty());
    }

    @Test
    public void changePrivacyToPublic_Success() {
        listDaoJdbc.insertWatched(filmId, userId, false);
        listDaoJdbc.changePrivacy(film, userId, true);
        Assert.assertFalse(listDaoJdbc.showOthersWatched(userId,0).isEmpty());
    }

    @Test
    public void changePrivacy_Fail() {
        Assert.assertFalse(listDaoJdbc.changePrivacy(film, userId, false));
    }

    @Test
    public void resetWatched_Success() {
        listDaoJdbc.insertWatched(film, userId, true);
        listDaoJdbc.resetWatched(filmId, userId);
        Assert.assertEquals(false, listDaoJdbc.isWatched(filmId, userId));
    }

    @Test
    public void resetPlanned_Success() {
        listDaoJdbc.insertPlanned(filmId, userId, false);
        listDaoJdbc.resetPlanned(filmId, userId);
        Assert.assertEquals(false, listDaoJdbc.isPlanned(filmId, userId));
    }

    @Test
    public void remove_Success() {
        listDaoJdbc.insertWatched(filmId, userId, true);
        listDaoJdbc.removeFilm(filmId, userId);
        Assert.assertEquals(false, listDaoJdbc.isWatched(filmId, userId));
    }

    @Test
    public void removeOne_Success() {
        listDaoJdbc.insertWatched(filmId, userId, true);
        listDaoJdbc.insertPlanned(filmId, userId, true);
        listDaoJdbc.removeFilm(filmId, userId);
        Assert.assertEquals(false, listDaoJdbc.isWatched(filmId, userId));
    }

    @Test
    public void areRelated_Success() {
        listDaoJdbc.insertWatched(filmId, userId, true);
        Assert.assertEquals(true, listDaoJdbc.areRelated(filmId, userId));
    }

    @Test
    public void areRelated_Fail() {
        Assert.assertEquals(false, listDaoJdbc.areRelated(filmId, userId));
    }

    @Test
    public void isWatched_Success() {
        listDaoJdbc.insertWatched(filmId, userId, true);
        Assert.assertEquals(true, listDaoJdbc.isWatched(filmId, userId));
    }

    @Test
    public void isWatched_Fail() {
        Assert.assertEquals(false, listDaoJdbc.isWatched(filmId, userId));
    }

    @Test
    public void isPlanned_Success() {
        listDaoJdbc.insertPlanned(filmId, userId, true);
        Assert.assertEquals(true, listDaoJdbc.isPlanned(filmId, userId));
    }

    @Test
    public void isPlanned_Fail() {
        Assert.assertEquals(false, listDaoJdbc.isPlanned(filmId, userId));
    }
}
