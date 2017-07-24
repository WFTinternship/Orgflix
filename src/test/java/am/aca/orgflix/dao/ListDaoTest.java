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
import java.util.List;

/**
 * Integration tests for List DAO methods
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

        jdbcFilmDAO.add(film);
        filmId = film.getId();
        //setUp User and User DAO

        userId = jdbcUserDAO.add(new User("MrSmith", "John Smith", "JhonSmith@gmail.com", "pass"));

    }

    /**
     * Rolls back all changes applied to the test DB resulted from tests
     */
    @After
    public void tearDown() throws SQLException, IOException, PropertyVetoException {
        helper.emptyTable(new String[]{"LISTS", "FILMS", "USERS"});
    }

    /**
     * @see ListDao#insertWatched(int, int, boolean)
     */
    @Test
    public void insertWatched_ValidInput_Success() {
        boolean status = listDaoJdbc.insertWatched(filmId, userId, true);
        Assert.assertTrue(status);

        status = listDaoJdbc.isWatched(filmId, userId);
        Assert.assertTrue(status);
    }

    /**
     * @see ListDao#insertWatched(int, int, boolean)
     */
    @Test
    public void insertWatched_InvalidFilm_Fail() {
        try {
            listDaoJdbc.insertWatched(0, userId, true);
        } catch (RuntimeException e) {
            Assert.assertTrue(e instanceof RuntimeException);
        }
    }

    /**
     * @see ListDao#insertWatched(int, int, boolean)
     */
    @Test
    public void insertWatched_InvalidUser_Fail() {
        try {
            listDaoJdbc.insertWatched(filmId, 0, true);
        } catch (RuntimeException e) {
            Assert.assertTrue(e instanceof RuntimeException);
        }
    }

    /**
     * @see ListDao#insertWatched(int, int, boolean)
     */
    @Test
    public void insertWatched_Duplicate_Fail() {
        try {
            listDaoJdbc.insertWatched(filmId, userId, true);
            listDaoJdbc.insertWatched(filmId, userId, true);
        } catch (RuntimeException e) {
            Assert.assertTrue(e instanceof RuntimeException);
        }
    }

    /**
     * @see ListDao#insertPlanned(int, int, boolean)
     */
    @Test
    public void insertPlanned_ValidInput_Success() {
        boolean status = listDaoJdbc.insertPlanned(filmId, userId, true);
        Assert.assertTrue(status);

        status = listDaoJdbc.isPlanned(filmId, userId);
        Assert.assertTrue(status);
    }

    /**
     * @see ListDao#insertPlanned(int, int, boolean)
     */
    @Test
    public void insertPlanned_InvalidUser_Fail() {
        try {
            listDaoJdbc.insertPlanned(filmId, 0, false);
        } catch (RuntimeException e) {
            Assert.assertTrue(e instanceof RuntimeException);
        }
    }

    /**
     * @see ListDao#insertPlanned(int, int, boolean)
     */
    @Test
    public void insertPlanned_InvalidFilm_Fail() {
        try {
            listDaoJdbc.insertPlanned(0, userId, false);
        } catch (RuntimeException e) {
            Assert.assertTrue(e instanceof RuntimeException);
        }
    }

    /**
     * @see ListDao#insertPlanned(int, int, boolean)
     */
    @Test
    public void insertPlanned_Duplicate_Fail() {
        try {
            listDaoJdbc.insertPlanned(filmId, userId, false);
            listDaoJdbc.insertPlanned(filmId, userId, false);
        } catch (RuntimeException e) {
            Assert.assertTrue(e instanceof RuntimeException);
        }
    }

    /**
     * @see ListDao#getOwnWatched(int, int, int)
     */
    @Test
    public void showOwnWatched_ValidUser_Success() {
        List<Film> actualFilms = listDaoJdbc.getOwnWatched(userId, 0, 12);
        Assert.assertTrue(actualFilms.isEmpty());

        listDaoJdbc.insertWatched(filmId, userId, false);

        actualFilms = listDaoJdbc.getOwnWatched(userId, 0, 12);
        Assert.assertEquals(1, actualFilms.size());
        Assert.assertEquals(film, actualFilms.get(0));

        jdbcFilmDAO.add(film);
        listDaoJdbc.insertWatched(film.getId(), userId, true);
        actualFilms = listDaoJdbc.getOwnWatched(userId, 0, 12);
        Assert.assertEquals(2, actualFilms.size());

        actualFilms = listDaoJdbc.getOwnWatched(userId, 2, 12);
        Assert.assertEquals(0, actualFilms.size());
    }

    /**
     * @see ListDao#getOwnWatched(int, int, int)
     */
    @Test
    public void showOwnWatched_InvalidUser_Fail() {
        List<Film> actualFilms = listDaoJdbc.getOwnWatched(0, 0, 12);
        Assert.assertTrue(actualFilms.isEmpty());
    }

    /**
     * @see ListDao#getOwnPlanned(int, int, int)
     */
    @Test
    public void showOwnPlanned_ValidUser_Success() {
        List<Film> actualFilms = listDaoJdbc.getOwnPlanned(userId, 0, 12);
        Assert.assertTrue(actualFilms.isEmpty());

        listDaoJdbc.insertPlanned(filmId, userId, false);

        actualFilms = listDaoJdbc.getOwnPlanned(userId, 0, 12);
        Assert.assertEquals(1, actualFilms.size());
        Assert.assertEquals(film, actualFilms.get(0));

        jdbcFilmDAO.add(film);
        listDaoJdbc.insertPlanned(film.getId(), userId, true);
        actualFilms = listDaoJdbc.getOwnPlanned(userId, 0, 12);
        Assert.assertEquals(2, actualFilms.size());

        actualFilms = listDaoJdbc.getOwnPlanned(userId, 2, 12);
        Assert.assertEquals(0, actualFilms.size());
    }

    /**
     * @see ListDao#getOwnPlanned(int, int, int)
     */
    @Test
    public void showOwnPlanned_InvalidUser_Fail() {
        List<Film> actualFilms = listDaoJdbc.getOwnPlanned(0, 0, 12);
        Assert.assertTrue(actualFilms.isEmpty());
    }

    /**
     * @see ListDao#getOthersWatched(int, int, int)
     */
    @Test
    public void showOthersWatched_ValidUser_Success() {
        listDaoJdbc.insertWatched(filmId, userId, true);

        List<Film> actualFilms = listDaoJdbc.getOthersWatched(userId, 0, 12);
        Assert.assertEquals(1, actualFilms.size());

        jdbcFilmDAO.add(film);
        listDaoJdbc.insertWatched(film.getId(), userId, false);

        actualFilms = listDaoJdbc.getOthersWatched(userId, 0, 12);
        Assert.assertEquals(1, actualFilms.size());

        jdbcFilmDAO.add(film);
        listDaoJdbc.insertWatched(film.getId(), userId, true);

        actualFilms = listDaoJdbc.getOthersWatched(userId, 0, 12);
        Assert.assertEquals(2, actualFilms.size());

        actualFilms = listDaoJdbc.getOthersWatched(userId, 12, 12);
        Assert.assertEquals(0, actualFilms.size());
    }

    /**
     * @see ListDao#getOthersWatched(int, int, int)
     */
    @Test
    public void showOthersWatched_InvalidUser_Fail() {
        listDaoJdbc.insertWatched(filmId, userId, false);

        List<Film> actualFilms = listDaoJdbc.getOthersWatched(userId, 0, 12);
        Assert.assertTrue(actualFilms.isEmpty());
    }

    /**
     * @see ListDao#getOthersPlanned(int, int, int)
     */
    @Test
    public void showOthersPlanned_ValidUser_Success() {
        listDaoJdbc.insertPlanned(filmId, userId, true);

        List<Film> actualFilms = listDaoJdbc.getOthersPlanned(userId, 0, 12);
        Assert.assertEquals(1, actualFilms.size());

        jdbcFilmDAO.add(film);
        listDaoJdbc.insertPlanned(film.getId(), userId, false);

        actualFilms = listDaoJdbc.getOthersPlanned(userId, 0, 12);
        Assert.assertEquals(1, actualFilms.size());

        jdbcFilmDAO.add(film);
        listDaoJdbc.insertPlanned(film.getId(), userId, true);

        actualFilms = listDaoJdbc.getOthersPlanned(userId, 0, 12);
        Assert.assertEquals(2, actualFilms.size());

        actualFilms = listDaoJdbc.getOthersPlanned(userId, 12, 12);
        Assert.assertEquals(0, actualFilms.size());
    }

    /**
     * @see ListDao#getOthersPlanned(int, int, int)
     */
    @Test
    public void showOthersPlanned_InvalidUser_Fail() {
        List<Film> actualFilms = listDaoJdbc.getOthersPlanned(userId, 0, 12);
        Assert.assertTrue(actualFilms.isEmpty());
    }

    /**
     * @see ListDao#setFilmAsWatched(int, int)
     */
    @Test
    public void updateWatched_Success() {
        listDaoJdbc.insertPlanned(filmId, userId, true);
        listDaoJdbc.setFilmAsWatched(filmId, userId);

        boolean status = listDaoJdbc.isWatched(filmId, userId);
        Assert.assertTrue(status);
    }

    /**
     * @see ListDao#setFilmAsWatched(int, int)
     */
    @Test
    public void updateWatched_Fail() {
        listDaoJdbc.setFilmAsWatched(filmId, userId);

        boolean status = listDaoJdbc.isWatched(filmId, userId);
        Assert.assertFalse(status);
    }

    /**
     * @see ListDao#setFilmAsPlanned(int, int)
     */
    @Test
    public void updatePlanned_Success() {
        listDaoJdbc.insertWatched(filmId, userId, true);
        listDaoJdbc.setFilmAsPlanned(filmId, userId);

        boolean status = listDaoJdbc.isPlanned(filmId, userId);
        Assert.assertTrue(status);
    }

    /**
     * @see ListDao#setFilmAsPlanned(int, int)
     */
    @Test
    public void updatePlanned_Fail() {
        listDaoJdbc.setFilmAsPlanned(filmId, userId);

        boolean status = listDaoJdbc.isPlanned(filmId, userId);
        Assert.assertFalse(status);
    }

    /**
     * @see ListDao#setFilmPrivacy(am.aca.orgflix.entity.Film, int, boolean)
     */
    @Test
    public void changePrivacy_Success() {
        listDaoJdbc.insertPlanned(filmId, userId, true);
        listDaoJdbc.setFilmAsWatched(filmId, userId);

        boolean status = listDaoJdbc.setFilmPrivacy(film, userId, false);
        Assert.assertTrue(status);

        List<Film> actualFilms = listDaoJdbc.getOthersPlanned(userId, 0, 12);
        Assert.assertTrue(actualFilms.isEmpty());

        actualFilms = listDaoJdbc.getOthersWatched(userId, 0, 12);
        Assert.assertTrue(actualFilms.isEmpty());

        status = listDaoJdbc.setFilmPrivacy(film, userId, true);
        Assert.assertTrue(status);

        actualFilms = listDaoJdbc.getOthersPlanned(userId, 0, 12);
        Assert.assertEquals(1, actualFilms.size());

        actualFilms = listDaoJdbc.getOthersWatched(userId, 0, 12);
        Assert.assertEquals(1, actualFilms.size());
    }

    /**
     * @see ListDao#setFilmPrivacy(am.aca.orgflix.entity.Film, int, boolean)
     */
    @Test
    public void changePrivacy_InvalidInput_Fail() {
        boolean status = listDaoJdbc.setFilmPrivacy(film, userId, false);
        Assert.assertFalse(status);
    }

    /**
     * @see ListDao#setFilmAsNotWatched(int, int)
     */
    @Test
    public void resetWatched_ValidInput_Success() {
        listDaoJdbc.insertWatched(filmId, userId, true);
        listDaoJdbc.setFilmAsPlanned(filmId, userId);

        boolean status = listDaoJdbc.setFilmAsNotWatched(filmId, userId);
        Assert.assertTrue(status);

        status = listDaoJdbc.isWatched(filmId, userId);
        Assert.assertFalse(status);

        status = listDaoJdbc.isPlanned(filmId, userId);
        Assert.assertTrue(status);

        List<Film> actualFilms = listDaoJdbc.getOwnWatched(userId, 0, 12);
        Assert.assertTrue(actualFilms.isEmpty());

        actualFilms = listDaoJdbc.getOwnPlanned(userId, 0, 12);
        Assert.assertEquals(1, actualFilms.size());
    }

    /**
     * @see ListDao#setFilmAsNotWatched(int, int)
     */
    @Test
    public void resetWatched_InvalidInput_Fail() {
        boolean status = listDaoJdbc.setFilmAsNotWatched(0, 0);
        Assert.assertFalse(status);
    }

    /**
     * @see ListDao#setFilmAsNotPlanned(int, int)
     */
    @Test
    public void resetPlanned_ValidInput_Success() {
        listDaoJdbc.insertPlanned(filmId, userId, false);
        listDaoJdbc.setFilmAsWatched(filmId, userId);

        boolean status = listDaoJdbc.setFilmAsNotPlanned(filmId, userId);
        Assert.assertTrue(status);

        status = listDaoJdbc.isPlanned(filmId, userId);
        Assert.assertFalse(status);

        status = listDaoJdbc.isWatched(filmId, userId);
        Assert.assertTrue(status);

        List<Film> actualFilms = listDaoJdbc.getOwnPlanned(userId, 0, 12);
        Assert.assertTrue(actualFilms.isEmpty());

        actualFilms = listDaoJdbc.getOwnWatched(userId, 0, 12);
        Assert.assertEquals(1, actualFilms.size());
    }

    /**
     * @see ListDao#setFilmAsNotPlanned(int, int)
     */
    @Test
    public void resetPlanned_InvalidInput_Fail() {
        boolean status = listDaoJdbc.setFilmAsNotPlanned(filmId, userId);
        Assert.assertFalse(status);
    }

    /**
     * @see ListDao#removeFilm(int, int)
     */
    @Test
    public void remove_ValidInput_Success() {
        listDaoJdbc.insertWatched(filmId, userId, true);
        listDaoJdbc.setFilmAsPlanned(filmId, userId);

        boolean status = listDaoJdbc.removeFilm(filmId, userId);
        Assert.assertTrue(status);

        status = listDaoJdbc.isWatched(filmId, userId);
        Assert.assertFalse(status);

        status = listDaoJdbc.isPlanned(filmId, userId);
        Assert.assertFalse(status);

        List<Film> actualFilms = listDaoJdbc.getOwnWatched(userId, 0, 12);
        Assert.assertTrue(actualFilms.isEmpty());

        actualFilms = listDaoJdbc.getOwnPlanned(userId, 0, 12);
        Assert.assertTrue(actualFilms.isEmpty());
    }

    /**
     * @see ListDao#areRelated(int, int)
     */
    @Test
    public void areRelated_ValidInput_Success() {
        listDaoJdbc.insertWatched(filmId, userId, true);

        boolean status = listDaoJdbc.areRelated(filmId, userId);
        Assert.assertTrue(status);

        status = listDaoJdbc.areRelated(0, 0);
        Assert.assertFalse(status);
    }

    /**
     * @see ListDao#isWatched(int, int)
     */
    @Test
    public void isWatched_ValidInput_Success() {
        listDaoJdbc.insertWatched(filmId, userId, true);

        boolean status = listDaoJdbc.isWatched(filmId, userId);
        Assert.assertTrue(status);

        listDaoJdbc.removeFilm(filmId, userId);

        status = listDaoJdbc.isWatched(filmId, userId);
        Assert.assertFalse(status);

        listDaoJdbc.insertPlanned(filmId, userId, true);

        status = listDaoJdbc.isWatched(filmId, userId);
        Assert.assertFalse(status);

    }

    /**
     * @see ListDao#isWatched(int, int)
     */
    @Test
    public void isWatched_InvalidInput_Fail() {
        boolean status = listDaoJdbc.isWatched(0, 0);
        Assert.assertFalse(status);
    }

    /**
     * @see ListDao#isPlanned(int, int)
     */
    @Test
    public void isPlanned_ValidInput_Success() {
        listDaoJdbc.insertPlanned(filmId, userId, true);

        boolean status = listDaoJdbc.isPlanned(filmId, userId);
        Assert.assertTrue(status);

        listDaoJdbc.removeFilm(filmId, userId);

        status = listDaoJdbc.isPlanned(filmId, userId);
        Assert.assertFalse(status);

        listDaoJdbc.insertWatched(filmId, userId, true);

        status = listDaoJdbc.isPlanned(filmId, userId);
        Assert.assertFalse(status);
    }

    /**
     * @see ListDao#isPlanned(int, int)
     */
    @Test
    public void isPlanned_InvalidInput_Fail() {
        boolean status = listDaoJdbc.isPlanned(filmId, userId);
        Assert.assertFalse(status);
    }

    /**
     * @see ListDao#watchedFilmPrivacyList(int)
     */
    @Test
    public void totalNumberWatched_Multiple_Success() {
        for (int i = 1; i <= 7; i++) {
            jdbcFilmDAO.add(film);
            listDaoJdbc.insertWatched(film.getId(), userId, true);
        }

        int size = listDaoJdbc.watchedFilmPrivacyList(userId).size();
        Assert.assertEquals(7, size);
    }

    /**
     * @see ListDao#watchedFilmPrivacyList(int) (int)
     */
    @Test
    public void totalNumberWatched_Empty() {
        int size = listDaoJdbc.watchedFilmPrivacyList(userId).size();
        Assert.assertEquals(0, size);
    }

    /**
     * @see ListDao#plannedFilmPrivacyList(int) (int)
     */
    @Test
    public void totalNumberPlanned_Multiple_Success() {
        for (int i = 1; i <= 5; i++) {
            jdbcFilmDAO.add(film);
            listDaoJdbc.insertPlanned(film.getId(), userId, true);
        }

        int size = listDaoJdbc.plannedFilmPrivacyList(userId).size();
        Assert.assertEquals(5, size);
    }

    /**
     * @see ListDao#plannedFilmPrivacyList(int)
     */
    @Test
    public void totalNumberPlanned_Empty() {
        int size = listDaoJdbc.plannedFilmPrivacyList(userId).size();
        Assert.assertEquals(0, size);
    }
}
