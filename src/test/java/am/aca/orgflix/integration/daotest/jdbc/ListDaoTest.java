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
import org.springframework.beans.factory.annotation.Qualifier;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Test for List DAO methods
 */
public class ListDaoTest extends BaseIntegrationTest {

    @Autowired
    private UserDAO userDao;

    @Autowired
    private FilmDAO filmDAO;

    @Autowired
    private ListDao listDao;

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

        filmDAO.addFilm(film);
        filmId = film.getId();
        //setup User and User DAO

        userId = userDao.add(new User("MrSmith","John Smith","JhonSmith@gmail.com","pass"));

    }

    @After
    public void revert() throws SQLException, IOException, PropertyVetoException {
        helper.emptyTable(new String[]{ "lists", "films", "users"});
    }

    @Test
    public void insertWatched_Success() {
        listDao.insertWatched(filmId, userId, true);
        Assert.assertEquals(true, listDao.isWatched(filmId, userId));
    }

    @Test
    public void insertPlanned_Success() {
        listDao.insertPlanned(filmId, userId, true);
        Assert.assertEquals(true, listDao.isPlanned(filmId, userId));
    }

    @Test
    public void showOwnWatched_Success() {
        listDao.insertWatched(filmId, userId, false);
        Assert.assertEquals(film, listDao.showOwnWatched(userId).get(0));
    }

    @Test
    public void showOwnWatched_Partial() {
        listDao.insertWatched(filmId, userId, false);
        listDao.insertWatched(filmId, userId, true);
        Assert.assertEquals(2, listDao.showOwnWatched(userId).size());
    }

    @Test
    public void showOwnWatched_Fail() {
        Assert.assertTrue(listDao.showOwnWatched(userId).isEmpty());
    }

    @Test
    public void showOwnPlanned_Success() {
        listDao.insertPlanned(filmId, userId, false);
        Assert.assertEquals(film, listDao.showOwnPlanned(userId).get(0));
    }

    @Test
    public void showOwnPlanned_Partial() {
        listDao.insertPlanned(filmId, userId, false);
        listDao.insertPlanned(filmId, userId, true);
        Assert.assertEquals(2, listDao.showOwnPlanned(userId).size());
    }

    @Test
    public void showOwnPlanned_Fail() {
        Assert.assertTrue(listDao.showOwnPlanned(userId).isEmpty());
    }

    @Test
    public void showOthersWatched_Success() {
        listDao.insertWatched(filmId, userId, true);
        listDao.insertWatched(filmId, userId, true);
        Assert.assertEquals(2, listDao.showOthersWatched(userId).size());
    }

    @Test
    public void showOthersWatched_Partial() {
        listDao.insertWatched(filmId, userId, true);
        listDao.insertWatched(filmId, userId, false);
        Assert.assertEquals(1, listDao.showOthersWatched(userId).size());
    }

    @Test
    public void showOthersWatched_Fail() {
        //setup List
        listDao.insertWatched(filmId, userId, false);

        Assert.assertEquals(0, listDao.showOthersWatched(userId).size());
    }

    @Test
    public void showOthersPlanned_Success() {
        listDao.insertPlanned(filmId, userId, true);
        listDao.insertPlanned(filmId, userId, true);
        Assert.assertEquals(2, listDao.showOthersPlanned(userId).size());
    }

    @Test
    public void showOthersPlanned_Partial() {
        listDao.insertPlanned(filmId, userId, true);
        listDao.insertPlanned(filmId, userId, false);
        Assert.assertEquals(1, listDao.showOthersPlanned(userId).size());
    }

    @Test
    public void showOthersPlanned_Fail() {
        //setup List
        listDao.insertPlanned(filmId, userId, false);

        Assert.assertEquals(0, listDao.showOthersPlanned(userId).size());
    }

    @Test
    public void updateWatched_Success() {
        listDao.insertPlanned(filmId, userId, true);
        listDao.updateWatched(filmId, userId);
        Assert.assertEquals(true, listDao.isWatched(filmId, userId));
    }

    @Test
    public void updateWatched_Fail() {
        listDao.updateWatched(filmId, userId);
        Assert.assertEquals(false, listDao.isWatched(filmId, userId));
    }

    @Test
    public void updatePlanned_Success() {
        listDao.insertWatched(filmId, userId, true);
        listDao.updatePlanned(filmId, userId);
        Assert.assertEquals(true, listDao.isPlanned(filmId, userId));
    }

    @Test
    public void updatePlanned_Fail() {
        listDao.updatePlanned(filmId, userId);
        Assert.assertEquals(false, listDao.isPlanned(filmId, userId));
    }

    @Test
    public void changePrivacy_Success() {
        listDao.insertPlanned(filmId, userId, true);
        Assert.assertTrue(listDao.changePrivacy(film, userId, false));
    }

    @Test
    public void changePrivacyToPrivate_Success() {
        listDao.insertWatched(filmId, userId, true);
        listDao.changePrivacy(film, userId, false);
        Assert.assertTrue(listDao.showOthersWatched(userId).isEmpty());
    }

    @Test
    public void changePrivacyToPublic_Success() {
        listDao.insertWatched(filmId, userId, false);
        listDao.changePrivacy(film, userId, true);
        Assert.assertFalse(listDao.showOthersWatched(userId).isEmpty());
    }

    @Test
    public void changePrivacy_Fail() {
        Assert.assertFalse(listDao.changePrivacy(film, userId, false));
    }

    @Test
    public void resetWatched_Success() {
        listDao.insertWatched(film, userId, true);
        listDao.resetWatched(filmId, userId);
        Assert.assertEquals(false, listDao.isWatched(filmId, userId));
    }

    @Test
    public void resetPlanned_Success() {
        listDao.insertPlanned(filmId, userId, false);
        listDao.resetPlanned(filmId, userId);
        Assert.assertEquals(false, listDao.isPlanned(filmId, userId));
    }

    @Test
    public void remove_Success() {
        listDao.insertWatched(filmId, userId, true);
        listDao.removeFilm(filmId, userId);
        Assert.assertEquals(false, listDao.isWatched(filmId, userId));
    }

    @Test
    public void removeOne_Success() {
        listDao.insertWatched(filmId, userId, true);
        listDao.insertPlanned(filmId, userId, true);
        listDao.removeFilm(filmId, userId);
        Assert.assertEquals(false, listDao.isWatched(filmId, userId));
    }

    @Test
    public void areRelated_Success() {
        listDao.insertWatched(filmId, userId, true);
        Assert.assertEquals(true, listDao.areRelated(filmId, userId));
    }

    @Test
    public void areRelated_Fail() {
        Assert.assertEquals(false, listDao.areRelated(filmId, userId));
    }

    @Test
    public void isWatched_Success() {
        listDao.insertWatched(filmId, userId, true);
        Assert.assertEquals(true, listDao.isWatched(filmId, userId));
    }

    @Test
    public void isWatched_Fail() {
        Assert.assertEquals(false, listDao.isWatched(filmId, userId));
    }

    @Test
    public void isPlanned_Success() {
        listDao.insertPlanned(filmId, userId, true);
        Assert.assertEquals(true, listDao.isPlanned(filmId, userId));
    }

    @Test
    public void isPlanned_Fail() {
        Assert.assertEquals(false, listDao.isPlanned(filmId, userId));
    }
}
