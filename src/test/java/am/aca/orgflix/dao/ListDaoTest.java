package am.aca.orgflix.dao;

import am.aca.orgflix.BaseIntegrationTest;
import am.aca.orgflix.dao.impljdbc.JdbcListDAO;
import am.aca.orgflix.entity.Film;
import am.aca.orgflix.entity.User;
import am.aca.orgflix.util.TestHelper;
import org.junit.*;
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
        helper.emptyTable(new String[]{"LISTS", "FILMS", "USERS"});
    }

    /**
     * @see JdbcListDAO#insertWatched(int, int, boolean)
     */
    @Test
    public void insertWatched_ValidInput_Success() {
        boolean status = listDaoJdbc.insertWatched(filmId, userId, true);
        Assert.assertTrue(status);

        status = listDaoJdbc.isWatched(filmId, userId);
        Assert.assertTrue(status);
    }

    /**
     * @see JdbcListDAO#insertWatched(int, int, boolean)
     */
    @Test(expected = RuntimeException.class)
    public void insertWatched_InvalidFilm_Fail() {
        listDaoJdbc.insertWatched(0, userId, true);
    }

    /**
     * @see JdbcListDAO#insertWatched(int, int, boolean)
     */
    @Test(expected = RuntimeException.class)
    public void insertWatched_InvalidUser_Fail() {
        listDaoJdbc.insertWatched(filmId, 0, true);
    }

    /**
     * @see JdbcListDAO#insertWatched(int, int, boolean)
     */
    @Test(expected = RuntimeException.class)
    public void insertWatched_Duplicate_Fail() {
        listDaoJdbc.insertWatched(filmId, userId, true);
        listDaoJdbc.insertWatched(filmId, userId, true);
    }

    /**
     * @see JdbcListDAO#insertPlanned(int, int, boolean)
     */
    @Test
    public void insertPlanned_ValidInput_Success() {
        boolean status = listDaoJdbc.insertPlanned(filmId, userId, true);
        Assert.assertTrue(status);

        status = listDaoJdbc.isPlanned(filmId, userId);
        Assert.assertTrue(status);
    }

    /**
     * @see JdbcListDAO#insertPlanned(int, int, boolean)
     */
    @Test(expected = RuntimeException.class)
    public void insertPlanned_InvalidUser_Fail() {
        listDaoJdbc.insertPlanned(filmId, 0, false);
    }

    /**
     * @see JdbcListDAO#insertPlanned(int, int, boolean)
     */
    @Test(expected = RuntimeException.class)
    public void insertPlanned_InvalidFilm_Fail() {
        listDaoJdbc.insertPlanned(0, userId, false);
    }

    /**
     * @see JdbcListDAO#insertPlanned(int, int, boolean)
     */
    @Test(expected = RuntimeException.class)
    public void insertPlanned_Duplicate_Fail() {
        listDaoJdbc.insertPlanned(filmId, userId, false);
        listDaoJdbc.insertPlanned(filmId, userId, false);
    }

    /**
     * @see JdbcListDAO#showOwnWatched(int, int, int)
     */
    @Test
    public void showOwnWatched_ValidUser_Success() {
        List<Film> actualFilms = listDaoJdbc.showOwnWatched(userId, 0, 12);
        Assert.assertTrue(actualFilms.isEmpty());

        listDaoJdbc.insertWatched(filmId, userId, false);

        actualFilms = listDaoJdbc.showOwnWatched(userId, 0, 12);
        Assert.assertEquals(1, actualFilms.size());
        Assert.assertEquals(film, actualFilms.get(0));

        jdbcFilmDAO.addFilm(film);
        listDaoJdbc.insertWatched(film.getId(), userId, true);
        actualFilms = listDaoJdbc.showOwnWatched(userId, 0, 12);
        Assert.assertEquals(2, actualFilms.size());

        actualFilms = listDaoJdbc.showOwnWatched(userId, 2, 12);
        Assert.assertEquals(0, actualFilms.size());
    }

    /**
     * @see JdbcListDAO#showOwnWatched(int, int, int)
     */
    @Test
    public void showOwnWatched_InvalidUser_Fail() {
        List<Film> actualFilms = listDaoJdbc.showOwnWatched(0, 0, 12);
        Assert.assertTrue(actualFilms.isEmpty());
    }

    /**
     * @see JdbcListDAO#showOwnPlanned(int, int, int)
     */
    @Test
    public void showOwnPlanned_ValidUser_Success() {
        List<Film> actualFilms = listDaoJdbc.showOwnPlanned(userId, 0, 12);
        Assert.assertTrue(actualFilms.isEmpty());

        listDaoJdbc.insertPlanned(filmId, userId, false);

        actualFilms = listDaoJdbc.showOwnPlanned(userId, 0, 12);
        Assert.assertEquals(1, actualFilms.size());
        Assert.assertEquals(film, actualFilms.get(0));

        jdbcFilmDAO.addFilm(film);
        listDaoJdbc.insertPlanned(film.getId(), userId, true);
        actualFilms = listDaoJdbc.showOwnPlanned(userId, 0, 12);
        Assert.assertEquals(2, actualFilms.size());

        actualFilms = listDaoJdbc.showOwnPlanned(userId, 2, 12);
        Assert.assertEquals(0, actualFilms.size());
    }

    /**
     * @see JdbcListDAO#showOwnPlanned(int, int, int)
     */
    @Test
    public void showOwnPlanned_InvalidUser_Fail() {
        List<Film> actualFilms = listDaoJdbc.showOwnPlanned(0, 0, 12);
        Assert.assertTrue(actualFilms.isEmpty());
    }

    /**
     * @see JdbcListDAO#showOthersWatched(int, int, int)
     */
    @Test
    public void showOthersWatched_ValidUser_Success() {
        listDaoJdbc.insertWatched(filmId, userId, true);

        List<Film> actualFilms = listDaoJdbc.showOthersWatched(userId, 0, 12);
        Assert.assertEquals(1, actualFilms.size());

        jdbcFilmDAO.addFilm(film);
        listDaoJdbc.insertWatched(film.getId(), userId, false);

        actualFilms = listDaoJdbc.showOthersWatched(userId, 0, 12);
        Assert.assertEquals(1, actualFilms.size());

        jdbcFilmDAO.addFilm(film);
        listDaoJdbc.insertWatched(film.getId(), userId, true);

        actualFilms = listDaoJdbc.showOthersWatched(userId, 0, 12);
        Assert.assertEquals(2, actualFilms.size());

        actualFilms = listDaoJdbc.showOthersWatched(userId, 12, 12);
        Assert.assertEquals(0, actualFilms.size());
    }

    /**
     * @see JdbcListDAO#showOthersWatched(int, int, int)
     */
    @Test
    public void showOthersWatched_InvalidUser_Fail() {
        listDaoJdbc.insertWatched(filmId, userId, false);

        List<Film> actualFilms = listDaoJdbc.showOthersWatched(userId, 0, 12);
        Assert.assertTrue(actualFilms.isEmpty());
    }

    /**
     * @see JdbcListDAO#showOthersPlanned(int, int, int)
     */
    @Test
    public void showOthersPlanned_ValidUser_Success() {
        listDaoJdbc.insertPlanned(filmId, userId, true);

        List<Film> actualFilms = listDaoJdbc.showOthersPlanned(userId, 0, 12);
        Assert.assertEquals(1, actualFilms.size());

        jdbcFilmDAO.addFilm(film);
        listDaoJdbc.insertPlanned(film.getId(), userId, false);

        actualFilms = listDaoJdbc.showOthersPlanned(userId, 0, 12);
        Assert.assertEquals(1, actualFilms.size());

        jdbcFilmDAO.addFilm(film);
        listDaoJdbc.insertPlanned(film.getId(), userId, true);

        actualFilms = listDaoJdbc.showOthersPlanned(userId, 0, 12);
        Assert.assertEquals(2, actualFilms.size());

        actualFilms = listDaoJdbc.showOthersPlanned(userId, 12, 12);
        Assert.assertEquals(0, actualFilms.size());
    }

    /**
     * @see JdbcListDAO#showOthersPlanned(int, int, int)
     */
    @Test
    public void showOthersPlanned_InvalidUser_Fail() {
        List<Film> actualFilms = listDaoJdbc.showOthersPlanned(userId, 0, 12);
        Assert.assertTrue(actualFilms.isEmpty());
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
        listDaoJdbc.updateWatched(filmId, userId);

        boolean status = listDaoJdbc.changePrivacy(film, userId, false);
        Assert.assertTrue(status);

        List<Film> actualFilms = listDaoJdbc.showOthersPlanned(userId, 0, 12);
        Assert.assertTrue(actualFilms.isEmpty());

        actualFilms = listDaoJdbc.showOthersWatched(userId, 0, 12);
        Assert.assertTrue(actualFilms.isEmpty());

        status = listDaoJdbc.changePrivacy(film, userId, true);
        Assert.assertTrue(status);

        actualFilms = listDaoJdbc.showOthersPlanned(userId, 0, 12);
        Assert.assertEquals(1, actualFilms.size());

        actualFilms = listDaoJdbc.showOthersWatched(userId, 0, 12);
        Assert.assertEquals(1, actualFilms.size());
    }

    /**
     * @see JdbcListDAO#changePrivacy(am.aca.orgflix.entity.Film, int, boolean)
     */
    @Test
    public void changePrivacy_InvalidInput_Fail() {
        boolean status = listDaoJdbc.changePrivacy(film, userId, false);
        Assert.assertFalse(status);
    }

    /**
     * @see JdbcListDAO#resetWatched(int, int)
     */
    @Test
    public void resetWatched_ValidInput_Success() {
        listDaoJdbc.insertWatched(filmId, userId, true);
        listDaoJdbc.updatePlanned(filmId, userId);

        boolean status = listDaoJdbc.resetWatched(filmId, userId);
        Assert.assertTrue(status);

        status = listDaoJdbc.isWatched(filmId, userId);
        Assert.assertFalse(status);

        status = listDaoJdbc.isPlanned(filmId, userId);
        Assert.assertTrue(status);

        List<Film> actualFilms = listDaoJdbc.showOwnWatched(userId, 0, 12);
        Assert.assertTrue(actualFilms.isEmpty());

        actualFilms = listDaoJdbc.showOwnPlanned(userId, 0, 12);
        Assert.assertEquals(1, actualFilms.size());
    }

    /**
     * @see JdbcListDAO#resetWatched(int, int)
     */
    @Test
    public void resetWatched_InvalidInput_Fail() {
        boolean status = listDaoJdbc.resetWatched(0, 0);
        Assert.assertFalse(status);
    }

    /**
     * @see JdbcListDAO#resetPlanned(int, int)
     */
    @Test
    public void resetPlanned_ValidInput_Success() {
        listDaoJdbc.insertPlanned(filmId, userId, false);
        listDaoJdbc.updateWatched(filmId, userId);

        boolean status = listDaoJdbc.resetPlanned(filmId, userId);
        Assert.assertTrue(status);

        status = listDaoJdbc.isPlanned(filmId, userId);
        Assert.assertFalse(status);

        status = listDaoJdbc.isWatched(filmId, userId);
        Assert.assertTrue(status);

        List<Film> actualFilms = listDaoJdbc.showOwnPlanned(userId, 0, 12);
        Assert.assertTrue(actualFilms.isEmpty());

        actualFilms = listDaoJdbc.showOwnWatched(userId, 0, 12);
        Assert.assertEquals(1, actualFilms.size());
    }

    /**
     * @see JdbcListDAO#resetPlanned(int, int)
     */
    @Test
    public void resetPlanned_InvalidInput_Fail() {
        boolean status = listDaoJdbc.resetPlanned(filmId, userId);
        Assert.assertFalse(status);
    }

    /**
     * @see JdbcListDAO#removeFilm(int, int)
     */
    @Test
    public void remove_ValidInput_Success() {
        listDaoJdbc.insertWatched(filmId, userId, true);
        listDaoJdbc.updatePlanned(filmId, userId);

        boolean status = listDaoJdbc.removeFilm(filmId, userId);
        Assert.assertTrue(status);

        status = listDaoJdbc.isWatched(filmId, userId);
        Assert.assertFalse(status);

        status = listDaoJdbc.isPlanned(filmId, userId);
        Assert.assertFalse(status);

        List<Film> actualFilms = listDaoJdbc.showOwnWatched(userId, 0, 12);
        Assert.assertTrue(actualFilms.isEmpty());

        actualFilms = listDaoJdbc.showOwnPlanned(userId, 0, 12);
        Assert.assertTrue(actualFilms.isEmpty());
    }

    /**
     * @see JdbcListDAO#areRelated(int, int)
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
     * @see JdbcListDAO#isWatched(int, int)
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
     * @see JdbcListDAO#isWatched(int, int)
     */
    @Test
    public void isWatched_InvalidInput_Fail() {
        boolean status = listDaoJdbc.isWatched(0, 0);
        Assert.assertFalse(status);
    }

    /**
     * @see JdbcListDAO#isPlanned(int, int)
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
     * @see JdbcListDAO#isPlanned(int, int)
     */
    @Test
    public void isPlanned_InvalidInput_Fail() {
        boolean status = listDaoJdbc.isPlanned(filmId, userId);
        Assert.assertFalse(status);
    }

    /**
     * @see JdbcListDAO#totalNumberOfWatched(int)
     */
    @Test
    public void totalNumberWatched_Multiple_Success() {
        for (int i = 1; i <= 7; i++) {
            jdbcFilmDAO.addFilm(film);
            listDaoJdbc.insertWatched(film.getId(), userId, true);
        }

        int size = listDaoJdbc.totalNumberOfWatched(userId);
        Assert.assertEquals(7, size);
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
        for (int i = 1; i <= 5; i++) {
            jdbcFilmDAO.addFilm(film);
            listDaoJdbc.insertPlanned(film.getId(), userId, true);
        }

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
