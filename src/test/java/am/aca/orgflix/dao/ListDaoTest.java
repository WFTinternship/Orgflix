package am.aca.orgflix.dao;

import am.aca.orgflix.BaseIntegrationTest;
import am.aca.orgflix.dao.impljdbc.JdbcListDAO;
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

        jdbcFilmDAO.addFilm(film);
        filmId = film.getId();
        //setUp User and User DAO

        userId = jdbcUserDAO.add(new User("MrSmith", "John Smith", "JhonSmith@gmail.com", "pass"));

    }

    /**
     * Rolls back all changes applied to the test DB resulted from tests
     */
    @After
    public void tearDown() throws SQLException, IOException, PropertyVetoException {
        helper.emptyTable(new String[]{"lists", "films", "users"});
    }

    /**
     * @see JdbcListDAO#insertWatched(int, int, boolean)
     */
    @Test
    public void insertWatched_Success() {
        listDaoJdbc.insertWatched(filmId, userId, true);

        boolean status = listDaoJdbc.isWatched(filmId, userId);
        Assert.assertTrue(status);
    }

    /**
     * @see JdbcListDAO#insertPlanned(int, int, boolean)
     */
    @Test
    public void insertPlanned_Success() {
        listDaoJdbc.insertPlanned(filmId, userId, true);

        boolean status = listDaoJdbc.isPlanned(filmId, userId);
        Assert.assertTrue(status);
    }

    /**
     * @see JdbcListDAO#showOwnWatched(int, int)
     */
    @Test
    public void showOwnWatched_Success() {
        listDaoJdbc.insertWatched(filmId, userId, false);

        Film actualFilm = listDaoJdbc.showOwnWatched(userId, 0).get(0);
        Assert.assertEquals(film, actualFilm);
    }

    /**
     * @see JdbcListDAO#showOwnWatched(int, int)
     */
    @Test
    public void showOwnWatched_Partial() {
        listDaoJdbc.insertWatched(filmId, userId, false);
        listDaoJdbc.insertWatched(filmId, userId, true);

        int size = listDaoJdbc.showOwnWatched(userId, 0).size();
        Assert.assertEquals(2, size);
    }

    /**
     * @see JdbcListDAO#showOwnWatched(int, int)
     */
    @Test
    public void showOwnWatched_Fail() {
        boolean status = listDaoJdbc.showOwnWatched(userId, 0).isEmpty();
        Assert.assertTrue(status);
    }

    /**
     * @see JdbcListDAO#showOwnPlanned(int, int)
     */
    @Test
    public void showOwnPlanned_Success() {
        listDaoJdbc.insertPlanned(filmId, userId, false);

        Film actualFilm = listDaoJdbc.showOwnPlanned(userId, 0).get(0);
        Assert.assertEquals(film, actualFilm);
    }

    /**
     * @see JdbcListDAO#showOwnPlanned(int, int)
     */
    @Test
    public void showOwnPlanned_Partial() {
        listDaoJdbc.insertPlanned(filmId, userId, false);
        listDaoJdbc.insertPlanned(filmId, userId, true);

        int size = listDaoJdbc.showOwnPlanned(userId, 0).size();
        Assert.assertEquals(2, size);
    }

    /**
     * @see JdbcListDAO#showOwnPlanned(int, int)
     */
    @Test
    public void showOwnPlanned_Fail() {
        boolean status = listDaoJdbc.showOwnPlanned(userId, 0).isEmpty();
        Assert.assertTrue(status);
    }

    /**
     * @see JdbcListDAO#showOthersWatched(int, int)
     */
    @Test
    public void showOthersWatched_Success() {
        listDaoJdbc.insertWatched(filmId, userId, true);
        listDaoJdbc.insertWatched(filmId, userId, true);

        int size = listDaoJdbc.showOthersWatched(userId, 0).size();
        Assert.assertEquals(2, size);
    }

    /**
     * @see JdbcListDAO#showOthersWatched(int, int)
     */
    @Test
    public void showOthersWatched_Partial() {
        listDaoJdbc.insertWatched(filmId, userId, true);
        listDaoJdbc.insertWatched(filmId, userId, false);

        int size = listDaoJdbc.showOthersWatched(userId, 0).size();
        Assert.assertEquals(1, size);
    }

    /**
     * @see JdbcListDAO#showOthersWatched(int, int)
     */
    @Test
    public void showOthersWatched_Fail() {
        //setUp List
        listDaoJdbc.insertWatched(filmId, userId, false);

        int size = listDaoJdbc.showOthersWatched(userId, 0).size();
        Assert.assertEquals(0, size);
    }

    /**
     * @see JdbcListDAO#showOthersPlanned(int, int)
     */
    @Test
    public void showOthersPlanned_Success() {
        listDaoJdbc.insertPlanned(filmId, userId, true);
        listDaoJdbc.insertPlanned(filmId, userId, true);

        int size = listDaoJdbc.showOthersPlanned(userId, 0).size();
        Assert.assertEquals(2, size);
    }

    /**
     * @see JdbcListDAO#showOthersPlanned(int, int)
     */
    @Test
    public void showOthersPlanned_Partial() {
        listDaoJdbc.insertPlanned(filmId, userId, true);
        listDaoJdbc.insertPlanned(filmId, userId, false);

        int size = listDaoJdbc.showOthersPlanned(userId, 0).size();
        Assert.assertEquals(1, size);
    }

    /**
     * @see JdbcListDAO#showOthersPlanned(int, int)
     */
    @Test
    public void showOthersPlanned_Fail() {
        //setUp List
        listDaoJdbc.insertPlanned(filmId, userId, false);

        int size = listDaoJdbc.showOthersPlanned(userId, 0).size();
        Assert.assertEquals(0, size);
    }

    /**
     * @see JdbcListDAO#updateWatched(int, int)
     */
    @Test
    public void updateWatched_Success() {
        listDaoJdbc.insertPlanned(filmId, userId, true);
        listDaoJdbc.updateWatched(filmId, userId);

        boolean status = listDaoJdbc.isWatched(filmId, userId);
        Assert.assertTrue(status);
    }

    /**
     * @see JdbcListDAO#updateWatched(int, int)
     */
    @Test
    public void updateWatched_Fail() {
        listDaoJdbc.updateWatched(filmId, userId);

        boolean status = listDaoJdbc.isWatched(filmId, userId);
        Assert.assertFalse(status);
    }

    /**
     * @see JdbcListDAO#updatePlanned(int, int)
     */
    @Test
    public void updatePlanned_Success() {
        listDaoJdbc.insertWatched(filmId, userId, true);
        listDaoJdbc.updatePlanned(filmId, userId);

        boolean status = listDaoJdbc.isPlanned(filmId, userId);
        Assert.assertTrue(status);
    }

    /**
     * @see JdbcListDAO#updatePlanned(int, int)
     */
    @Test
    public void updatePlanned_Fail() {
        listDaoJdbc.updatePlanned(filmId, userId);

        boolean status = listDaoJdbc.isPlanned(filmId, userId);
        Assert.assertFalse(status);
    }

    /**
     * @see JdbcListDAO#changePrivacy(am.aca.orgflix.entity.Film, int, boolean)
     */
    @Test
    public void changePrivacy_Success() {
        listDaoJdbc.insertPlanned(filmId, userId, true);

        boolean status = listDaoJdbc.changePrivacy(film, userId, false);
        Assert.assertTrue(status);
    }

    /**
     * @see JdbcListDAO#changePrivacy(am.aca.orgflix.entity.Film, int, boolean)
     */
    @Test
    public void changePrivacy_ToPrivate_Success() {
        listDaoJdbc.insertWatched(filmId, userId, true);
        listDaoJdbc.changePrivacy(film, userId, false);

        boolean status = listDaoJdbc.showOthersWatched(userId, 0).isEmpty();
        Assert.assertTrue(status);
    }

    /**
     * @see JdbcListDAO#changePrivacy(am.aca.orgflix.entity.Film, int, boolean)
     */
    @Test
    public void changePrivacy_ToPublic_Success() {
        listDaoJdbc.insertWatched(filmId, userId, false);
        listDaoJdbc.changePrivacy(film, userId, true);

        boolean status = listDaoJdbc.showOthersWatched(userId, 0).isEmpty();
        Assert.assertFalse(status);
    }

    /**
     * @see JdbcListDAO#changePrivacy(am.aca.orgflix.entity.Film, int, boolean)
     */
    @Test
    public void changePrivacy_Fail() {
        boolean status = listDaoJdbc.changePrivacy(film, userId, false);
        Assert.assertFalse(status);
    }

    /**
     * @see JdbcListDAO#resetWatched(int, int)
     */
    @Test
    public void resetWatched_Success() {
        listDaoJdbc.insertWatched(film.getId(), userId, true);
        listDaoJdbc.resetWatched(filmId, userId);

        boolean status = listDaoJdbc.isWatched(filmId, userId);
        Assert.assertFalse(status);
    }

    /**
     * @see JdbcListDAO#resetPlanned(int, int)
     */
    @Test
    public void resetPlanned_Success() {
        listDaoJdbc.insertPlanned(filmId, userId, false);
        listDaoJdbc.resetPlanned(filmId, userId);

        boolean status = listDaoJdbc.isPlanned(filmId, userId);
        Assert.assertFalse(status);
    }

    /**
     * @see JdbcListDAO#removeFilm(int, int)
     */
    @Test
    public void remove_Success() {
        listDaoJdbc.insertWatched(filmId, userId, true);
        listDaoJdbc.removeFilm(filmId, userId);

        boolean status = listDaoJdbc.isWatched(filmId, userId);
        Assert.assertFalse(status);
    }

    /**
     * @see JdbcListDAO#removeFilm(int, int)
     */
    @Test
    public void remove_One_Success() {
        listDaoJdbc.insertWatched(filmId, userId, true);
        listDaoJdbc.insertPlanned(filmId, userId, true);
        listDaoJdbc.removeFilm(filmId, userId);

        boolean status = listDaoJdbc.isWatched(filmId, userId);
        Assert.assertFalse(status);
    }

    /**
     * @see JdbcListDAO#areRelated(int, int)
     */
    @Test
    public void areRelated_Success() {
        listDaoJdbc.insertWatched(filmId, userId, true);

        boolean status = listDaoJdbc.areRelated(filmId, userId);
        Assert.assertTrue(status);
    }

    /**
     * @see JdbcListDAO#areRelated(int, int)
     */
    @Test
    public void areRelated_Fail() {
        boolean status = listDaoJdbc.areRelated(filmId, userId);
        Assert.assertFalse(status);
    }

    /**
     * @see JdbcListDAO#isWatched(int, int)
     */
    @Test
    public void isWatched_Success() {
        listDaoJdbc.insertWatched(filmId, userId, true);

        boolean status = listDaoJdbc.isWatched(filmId, userId);
        Assert.assertTrue(status);
    }

    /**
     * @see JdbcListDAO#isWatched(int, int)
     */
    @Test
    public void isWatched_Fail() {
        boolean status = listDaoJdbc.isWatched(filmId, userId);
        Assert.assertFalse(status);
    }

    /**
     * @see JdbcListDAO#isPlanned(int, int)
     */
    @Test
    public void isPlanned_Success() {
        listDaoJdbc.insertPlanned(filmId, userId, true);

        boolean status = listDaoJdbc.isPlanned(filmId, userId);
        Assert.assertTrue(status);
    }

    /**
     * @see JdbcListDAO#isPlanned(int, int)
     */
    @Test
    public void isPlanned_Fail() {
        boolean status = listDaoJdbc.isPlanned(filmId, userId);
        Assert.assertFalse(status);
    }

    /**
     * @see JdbcListDAO#totalNumberOfWatched(int)
     */
    @Test
    public void totalNumberWatched_Multiple_Success() {
        listDaoJdbc.insertWatched(film.getId(), userId, true);
        listDaoJdbc.insertWatched(film.getId(), userId, true);
        listDaoJdbc.insertWatched(film.getId(), userId, true);
        listDaoJdbc.insertWatched(film.getId(), userId, true);
        int size = listDaoJdbc.totalNumberOfWatched(userId);
        Assert.assertEquals(4, size);
    }

    /**
     * @see JdbcListDAO#totalNumberOfWatched(int)
     */
    @Test
    public void totalNumberWatched_Empty() {
        int size = listDaoJdbc.totalNumberOfWatched(userId);
        Assert.assertEquals(0, size);
    }

    /**
     * @see JdbcListDAO#totalNumberOfPlanned(int)
     */
    @Test
    public void totalNumberPlanned_Multiple_Success() {
        listDaoJdbc.insertPlanned(film.getId(), userId, true);
        listDaoJdbc.insertPlanned(film.getId(), userId, true);
        listDaoJdbc.insertPlanned(film.getId(), userId, true);
        listDaoJdbc.insertPlanned(film.getId(), userId, true);
        listDaoJdbc.insertPlanned(film.getId(), userId, true);
        int size = listDaoJdbc.totalNumberOfPlanned(userId);
        Assert.assertEquals(5, size);
    }

    /**
     * @see JdbcListDAO#totalNumberOfPlanned(int)
     */
    @Test
    public void totalNumberPlanned_Empty() {
        int size = listDaoJdbc.totalNumberOfPlanned(userId);
        Assert.assertEquals(0, size);
    }
}
