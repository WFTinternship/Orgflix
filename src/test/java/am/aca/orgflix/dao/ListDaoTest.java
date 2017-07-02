package am.aca.orgflix.dao;

import am.aca.orgflix.BaseIntegrationTest;
import am.aca.orgflix.entity.Film;
import am.aca.orgflix.entity.User;
import am.aca.orgflix.util.TestHelper;
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

        //setUp Cast

        film = new Film();
        film.setTitle("Scarface");
        film.setProdYear(1983);

        jdbcFilmDAO.addFilm(film);
        filmId = film.getId();
        //setUp User and User DAO

        userId = jdbcUserDAO.add(new User("MrSmith", "John Smith", "JhonSmith@gmail.com", "pass"));

    }

    @After
    public void tearDown() throws SQLException, IOException, PropertyVetoException {
        helper.emptyTable(new String[]{"lists", "films", "users"});
    }

    @Test
    public void insertWatched_Success() {
        listDaoJdbc.insertWatched(filmId, userId, true);

        boolean status = listDaoJdbc.isWatched(filmId, userId);
        Assert.assertTrue(status);
    }

    @Test
    public void insertPlanned_Success() {
        listDaoJdbc.insertPlanned(filmId, userId, true);

        boolean status = listDaoJdbc.isPlanned(filmId, userId);
        Assert.assertTrue(status);
    }

    @Test
    public void showOwnWatched_Success() {
        listDaoJdbc.insertWatched(filmId, userId, false);

        Film actualFilm = listDaoJdbc.showOwnWatched(userId, 0).get(0);
        Assert.assertEquals(film, actualFilm);
    }

    @Test
    public void showOwnWatched_Partial() {
        listDaoJdbc.insertWatched(filmId, userId, false);
        listDaoJdbc.insertWatched(filmId, userId, true);

        int size = listDaoJdbc.showOwnWatched(userId, 0).size();
        Assert.assertEquals(2, size);
    }

    @Test
    public void showOwnWatched_Fail() {
        boolean status = listDaoJdbc.showOwnWatched(userId, 0).isEmpty();
        Assert.assertTrue(status);
    }

    @Test
    public void showOwnPlanned_Success() {
        listDaoJdbc.insertPlanned(filmId, userId, false);

        Film actualFilm = listDaoJdbc.showOwnPlanned(userId, 0).get(0);
        Assert.assertEquals(film, actualFilm);
    }

    @Test
    public void showOwnPlanned_Partial() {
        listDaoJdbc.insertPlanned(filmId, userId, false);
        listDaoJdbc.insertPlanned(filmId, userId, true);

        int size = listDaoJdbc.showOwnPlanned(userId, 0).size();
        Assert.assertEquals(2, size);
    }

    @Test
    public void showOwnPlanned_Fail() {
        boolean status = listDaoJdbc.showOwnPlanned(userId, 0).isEmpty();
        Assert.assertTrue(status);
    }

    @Test
    public void showOthersWatched_Success() {
        listDaoJdbc.insertWatched(filmId, userId, true);
        listDaoJdbc.insertWatched(filmId, userId, true);

        int size = listDaoJdbc.showOthersWatched(userId, 0).size();
        Assert.assertEquals(2, size);
    }

    @Test
    public void showOthersWatched_Partial() {
        listDaoJdbc.insertWatched(filmId, userId, true);
        listDaoJdbc.insertWatched(filmId, userId, false);

        int size = listDaoJdbc.showOthersWatched(userId, 0).size();
        Assert.assertEquals(1, size);
    }

    @Test
    public void showOthersWatched_Fail() {
        //setUp List
        listDaoJdbc.insertWatched(filmId, userId, false);

        int size = listDaoJdbc.showOthersWatched(userId, 0).size();
        Assert.assertEquals(0, size);
    }

    @Test
    public void showOthersPlanned_Success() {
        listDaoJdbc.insertPlanned(filmId, userId, true);
        listDaoJdbc.insertPlanned(filmId, userId, true);

        int size = listDaoJdbc.showOthersPlanned(userId, 0).size();
        Assert.assertEquals(2, size);
    }

    @Test
    public void showOthersPlanned_Partial() {
        listDaoJdbc.insertPlanned(filmId, userId, true);
        listDaoJdbc.insertPlanned(filmId, userId, false);

        int size = listDaoJdbc.showOthersPlanned(userId, 0).size();
        Assert.assertEquals(1, size);
    }

    @Test
    public void showOthersPlanned_Fail() {
        //setUp List
        listDaoJdbc.insertPlanned(filmId, userId, false);

        int size = listDaoJdbc.showOthersPlanned(userId, 0).size();
        Assert.assertEquals(0, size);
    }

    @Test
    public void updateWatched_Success() {
        listDaoJdbc.insertPlanned(filmId, userId, true);
        listDaoJdbc.updateWatched(filmId, userId);

        boolean status = listDaoJdbc.isWatched(filmId, userId);
        Assert.assertTrue(status);
    }

    @Test
    public void updateWatched_Fail() {
        listDaoJdbc.updateWatched(filmId, userId);

        boolean status = listDaoJdbc.isWatched(filmId, userId);
        Assert.assertFalse(status);
    }

    @Test
    public void updatePlanned_Success() {
        listDaoJdbc.insertWatched(filmId, userId, true);
        listDaoJdbc.updatePlanned(filmId, userId);

        boolean status = listDaoJdbc.isPlanned(filmId, userId);
        Assert.assertTrue(status);
    }

    @Test
    public void updatePlanned_Fail() {
        listDaoJdbc.updatePlanned(filmId, userId);

        boolean status = listDaoJdbc.isPlanned(filmId, userId);
        Assert.assertFalse(status);
    }

    @Test
    public void changePrivacy_Success() {
        listDaoJdbc.insertPlanned(filmId, userId, true);

        boolean status = listDaoJdbc.changePrivacy(film, userId, false);
        Assert.assertTrue(status);
    }

    @Test
    public void changePrivacy_ToPrivate_Success() {
        listDaoJdbc.insertWatched(filmId, userId, true);
        listDaoJdbc.changePrivacy(film, userId, false);

        boolean status = listDaoJdbc.showOthersWatched(userId, 0).isEmpty();
        Assert.assertTrue(status);
    }

    @Test
    public void changePrivacy_ToPublic_Success() {
        listDaoJdbc.insertWatched(filmId, userId, false);
        listDaoJdbc.changePrivacy(film, userId, true);

        boolean status = listDaoJdbc.showOthersWatched(userId, 0).isEmpty();
        Assert.assertFalse(status);
    }

    @Test
    public void changePrivacy_Fail() {
        boolean status = listDaoJdbc.changePrivacy(film, userId, false);
        Assert.assertFalse(status);
    }

    @Test
    public void resetWatched_Success() {
        listDaoJdbc.insertWatched(film.getId(), userId, true);
        listDaoJdbc.resetWatched(filmId, userId);

        boolean status = listDaoJdbc.isWatched(filmId, userId);
        Assert.assertFalse(status);
    }

    @Test
    public void resetPlanned_Success() {
        listDaoJdbc.insertPlanned(filmId, userId, false);
        listDaoJdbc.resetPlanned(filmId, userId);

        boolean status = listDaoJdbc.isPlanned(filmId, userId);
        Assert.assertFalse(status);
    }

    @Test
    public void remove_Success() {
        listDaoJdbc.insertWatched(filmId, userId, true);
        listDaoJdbc.removeFilm(filmId, userId);

        boolean status = listDaoJdbc.isWatched(filmId, userId);
        Assert.assertFalse(status);
    }

    @Test
    public void remove_One_Success() {
        listDaoJdbc.insertWatched(filmId, userId, true);
        listDaoJdbc.insertPlanned(filmId, userId, true);
        listDaoJdbc.removeFilm(filmId, userId);

        boolean status = listDaoJdbc.isWatched(filmId, userId);
        Assert.assertFalse(status);
    }

    @Test
    public void areRelated_Success() {
        listDaoJdbc.insertWatched(filmId, userId, true);

        boolean status = listDaoJdbc.areRelated(filmId, userId);
        Assert.assertTrue(status);
    }

    @Test
    public void areRelated_Fail() {
        boolean status = listDaoJdbc.areRelated(filmId, userId);
        Assert.assertFalse(status);
    }

    @Test
    public void isWatched_Success() {
        listDaoJdbc.insertWatched(filmId, userId, true);

        boolean status = listDaoJdbc.isWatched(filmId, userId);
        Assert.assertTrue(status);
    }

    @Test
    public void isWatched_Fail() {
        boolean status = listDaoJdbc.isWatched(filmId, userId);
        Assert.assertFalse(status);
    }

    @Test
    public void isPlanned_Success() {
        listDaoJdbc.insertPlanned(filmId, userId, true);

        boolean status = listDaoJdbc.isPlanned(filmId, userId);
        Assert.assertTrue(status);
    }

    @Test
    public void isPlanned_Fail() {
        boolean status = listDaoJdbc.isPlanned(filmId, userId);
        Assert.assertFalse(status);
    }
}
