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
    private UserDAO hibernateUserDAO;

    @Autowired
    private FilmDAO hibernateFilmDAO;

    @Autowired
    private ListDao hibernateListDAO;

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

        hibernateFilmDAO.addFilm(film);
        filmId = film.getId();
        //setUp User and User DAO

        userId = hibernateUserDAO.add(new User("MrSmith", "John Smith", "JhonSmith@gmail.com", "pass"));

    }

    /**
     * Rolls back all changes applied to the test DB resulted from tests
     */
    @After
    public void tearDown() throws SQLException, IOException, PropertyVetoException {
        helper.emptyTable(new String[]{"lists", "films", "users"});
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateListDAO#insertWatched(int, int, boolean)
     */
    @Test
    public void insertWatched_ValidInputs_Success() {
        boolean status = hibernateListDAO.insertWatched(filmId, userId, true);

        Assert.assertTrue(status);

        status = hibernateListDAO.isWatched(filmId, userId);
        Assert.assertTrue(status);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateListDAO#insertWatched(int, int, boolean)
     */
    @Test
    public void insertWatched_InvalidInputs_Success() {
        boolean status = hibernateListDAO.insertWatched(0, 0, true);

        Assert.assertFalse(status);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateListDAO#insertPlanned(int, int, boolean)
     */
    @Test
    public void insertPlanned_ValidInputs_Success() {
        boolean status = hibernateListDAO.insertPlanned(filmId, userId, true);
        Assert.assertTrue(status);

        status = hibernateListDAO.isPlanned(filmId, userId);
        Assert.assertTrue(status);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateListDAO#insertPlanned(int, int, boolean)
     */
    @Test
    public void insertPlanned_InvalidInputs_Success() {
        boolean status = hibernateListDAO.insertPlanned(0, 0, true);

        Assert.assertFalse(status);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateListDAO#showOwnWatched(int, int)
     */
    @Test
    public void showOwnWatched_ValidUser_Success() {
        hibernateListDAO.insertWatched(filmId, userId, false);

        List<Film> actualList = hibernateListDAO.showOwnWatched(userId, 0);
        Film actualFilm = actualList.get(0);
        int size = actualList.size();

        Assert.assertEquals(1, size);
        Assert.assertEquals(film, actualFilm);

        Film film = new Film("American Gangster", 2007);
        hibernateFilmDAO.addFilm(film);

        hibernateListDAO.insertWatched(film.getId(), userId, true);

        int sizePage2 = hibernateListDAO.showOwnWatched(userId, 1).size();
        Assert.assertEquals(0, sizePage2);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateListDAO#showOwnWatched(int, int)
     */
    @Test
    public void showOwnWatched_ValidUser_Empty() {
        boolean status = hibernateListDAO.showOwnWatched(userId, 0).isEmpty();
        Assert.assertTrue(status);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateListDAO#showOwnWatched(int, int)
     */
    @Test
    public void showOwnWatched_InvalidUser_Empty() {
        boolean status = hibernateListDAO.showOwnWatched(0, 0).isEmpty();
        Assert.assertTrue(status);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateListDAO#showOwnPlanned(int, int)
     */
    @Test
    public void showOwnPlanned_ValidUser_Success() {
        hibernateListDAO.insertPlanned(filmId, userId, false);

        List<Film> actualList = hibernateListDAO.showOwnPlanned(userId, 0);
        Film actualFilm = actualList.get(0);
        int size = actualList.size();

        Assert.assertEquals(1, size);
        Assert.assertEquals(film, actualFilm);

        Film film = new Film("American Gangster", 2007);
        hibernateFilmDAO.addFilm(film);
        hibernateListDAO.insertPlanned(film.getId(), userId, true);

        int sizePage2 = hibernateListDAO.showOwnPlanned(userId, 1).size();

        Assert.assertEquals(0, sizePage2);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateListDAO#showOwnPlanned(int, int)
     */
    @Test
    public void showOwnPlanned_ValidUser_Empty() {
        boolean status = hibernateListDAO.showOwnPlanned(userId, 0).isEmpty();
        Assert.assertTrue(status);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateListDAO#showOwnPlanned(int, int)
     */
    @Test
    public void showOwnPlanned_InvalidUser_Fail() {
        boolean status = hibernateListDAO.showOwnPlanned(0, 0).isEmpty();
        Assert.assertTrue(status);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateListDAO#showOthersWatched(int, int)
     */
    @Test
    public void showOthersWatched_ValidUser_Success() {
        hibernateListDAO.insertWatched(filmId, userId, false);

        List<Film> actualList = hibernateListDAO.showOthersWatched(userId, 0);
        int size = actualList.size();

        Assert.assertEquals(0, size);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateListDAO#showOthersWatched(int, int)
     */
    @Test
    public void showOthersWatched_ValidUser_Empty() {
        //setUp List
        hibernateListDAO.insertWatched(filmId, userId, false);

        int size = hibernateListDAO.showOthersWatched(userId, 0).size();
        Assert.assertEquals(0, size);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateListDAO#showOthersWatched(int, int)
     */
    @Test
    public void showOthersWatched_InvalidUser_Fail() {
        int size = hibernateListDAO.showOthersWatched(0, 0).size();
        Assert.assertEquals(0, size);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateListDAO#showOthersPlanned(int, int)
     */
    @Test
    public void showOthersPlanned_ValidUser_Success() {
        hibernateListDAO.insertPlanned(filmId, userId, false);

        List<Film> actualList = hibernateListDAO.showOthersPlanned(userId, 0);
        int size = actualList.size();

        Assert.assertEquals(0, size);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateListDAO#showOthersPlanned(int, int)
     */
    @Test
    public void showOthersPlanned_ValidUser_Empty() {
        //setUp List
        hibernateListDAO.insertPlanned(filmId, userId, false);

        int size = hibernateListDAO.showOthersPlanned(userId, 0).size();
        Assert.assertEquals(0, size);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateListDAO#showOthersPlanned(int, int)
     */
    @Test
    public void showOthersPlanned_InvalidUser_Fail() {
        int size = hibernateListDAO.showOthersPlanned(0, 0).size();
        Assert.assertEquals(0, size);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateListDAO#updateWatched(int, int)
     */
    @Test
    public void updateWatched_Success() {
        hibernateListDAO.insertPlanned(filmId, userId, true);
        boolean status = hibernateListDAO.updateWatched(filmId, userId);
        Assert.assertTrue(status);

        status = hibernateListDAO.isWatched(filmId, userId);
        Assert.assertTrue(status);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateListDAO#updateWatched(int, int)
     */
    @Test
    public void updateWatched_Fail() {
        boolean status = hibernateListDAO.updateWatched(filmId, userId);

        Assert.assertFalse(status);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateListDAO#updatePlanned(int, int)
     */
    @Test
    public void updatePlanned_Success() {
        hibernateListDAO.insertWatched(filmId, userId, true);
        boolean status = hibernateListDAO.updatePlanned(filmId, userId);
        Assert.assertTrue(status);

        status = hibernateListDAO.isPlanned(filmId, userId);
        Assert.assertTrue(status);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateListDAO#updatePlanned(int, int)
     */
    @Test
    public void updatePlanned_Fail() {
        hibernateListDAO.updatePlanned(filmId, userId);

        boolean status = hibernateListDAO.isPlanned(filmId, userId);
        Assert.assertFalse(status);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateListDAO#changePrivacy(am.aca.orgflix.entity.Film, int, boolean)
     */
    @Test
    public void changePrivacy_Success() {
        hibernateListDAO.insertPlanned(filmId, userId, true);

        boolean status = hibernateListDAO.changePrivacy(film, userId, false);
        Assert.assertTrue(status);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateListDAO#changePrivacy(am.aca.orgflix.entity.Film, int, boolean)
     */
    @Test
    public void changePrivacy_ToPrivate_Success() {
        hibernateListDAO.insertWatched(filmId, userId, true);
        hibernateListDAO.changePrivacy(film, userId, false);

        boolean status = hibernateListDAO.showOthersWatched(userId, 0).isEmpty();
        Assert.assertTrue(status);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateListDAO#changePrivacy(am.aca.orgflix.entity.Film, int, boolean)
     */
    @Test
    public void changePrivacy_ToPublic_Success() {
        hibernateListDAO.insertWatched(filmId, userId, false);
        hibernateListDAO.changePrivacy(film, userId, true);

        boolean status = hibernateListDAO.showOthersWatched(userId, 0).isEmpty();
        Assert.assertFalse(status);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateListDAO#changePrivacy(am.aca.orgflix.entity.Film, int, boolean)
     */
    @Test
    public void changePrivacy_Fail() {
        boolean status = hibernateListDAO.changePrivacy(film, userId, false);
        Assert.assertFalse(status);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateListDAO#resetWatched(int, int)
     */
    @Test
    public void resetWatched_Success() {
        hibernateListDAO.insertWatched(film.getId(), userId, true);
        boolean status = hibernateListDAO.resetWatched(filmId, userId);
        Assert.assertTrue(status);

        status = hibernateListDAO.isWatched(filmId, userId);
        Assert.assertFalse(status);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateListDAO#resetPlanned(int, int)
     */
    @Test
    public void resetPlanned_Success() {
        hibernateListDAO.insertPlanned(filmId, userId, false);
        boolean status = hibernateListDAO.resetPlanned(filmId, userId);
        Assert.assertTrue(status);

        status = hibernateListDAO.isPlanned(filmId, userId);
        Assert.assertFalse(status);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateListDAO#removeFilm(int, int)
     */
    @Test
    public void remove_Success() {
        hibernateListDAO.insertWatched(filmId, userId, true);
        boolean status = hibernateListDAO.removeFilm(filmId, userId);
        Assert.assertTrue(status);

        status = hibernateListDAO.isWatched(filmId, userId);
        Assert.assertFalse(status);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateListDAO#removeFilm(int, int)
     */
    @Test
    public void remove_One_Success() {
        hibernateListDAO.insertWatched(filmId, userId, true);
        hibernateListDAO.insertPlanned(filmId, userId, true);
        hibernateListDAO.removeFilm(filmId, userId);

        boolean status = hibernateListDAO.isWatched(filmId, userId);
        Assert.assertFalse(status);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateListDAO#areRelated(int, int)
     */
    @Test
    public void areRelated_Success() {
        hibernateListDAO.insertWatched(filmId, userId, true);

        boolean status = hibernateListDAO.areRelated(filmId, userId);
        Assert.assertTrue(status);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateListDAO#areRelated(int, int)
     */
    @Test
    public void areRelated_Fail() {
        boolean status = hibernateListDAO.areRelated(filmId, userId);
        Assert.assertFalse(status);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateListDAO#isWatched(int, int)
     */
    @Test
    public void isWatched_Success() {
        hibernateListDAO.insertWatched(filmId, userId, true);

        boolean status = hibernateListDAO.isWatched(filmId, userId);
        Assert.assertTrue(status);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateListDAO#isWatched(int, int)
     */
    @Test
    public void isWatched_Fail() {
        boolean status = hibernateListDAO.isWatched(filmId, userId);
        Assert.assertFalse(status);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateListDAO#isPlanned(int, int)
     */
    @Test
    public void isPlanned_Success() {
        hibernateListDAO.insertPlanned(filmId, userId, true);

        boolean status = hibernateListDAO.isPlanned(filmId, userId);
        Assert.assertTrue(status);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateListDAO#isPlanned(int, int)
     */
    @Test
    public void isPlanned_Fail() {
        boolean status = hibernateListDAO.isPlanned(filmId, userId);
        Assert.assertFalse(status);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateListDAO#totalNumberOfWatched(int)
     */
    @Test
    public void totalNumberWatched_Multiple_Success() {
        hibernateListDAO.insertWatched(film.getId(), userId, true);

        int size = hibernateListDAO.totalNumberOfWatched(userId);
        Assert.assertEquals(1, size);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateListDAO#totalNumberOfWatched(int)
     */
    @Test
    public void totalNumberWatched_Empty() {
        int size = hibernateListDAO.totalNumberOfWatched(userId);
        Assert.assertEquals(0, size);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateListDAO#totalNumberOfPlanned(int)
     */
    @Test
    public void totalNumberPlanned_Multiple_Success() {
        hibernateListDAO.insertPlanned(film.getId(), userId, true);
        int size = hibernateListDAO.totalNumberOfPlanned(userId);
        Assert.assertEquals(1, size);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateListDAO#totalNumberOfPlanned(int)
     */
    @Test
    public void totalNumberPlanned_Empty() {
        int size = hibernateListDAO.totalNumberOfPlanned(userId);
        Assert.assertEquals(0, size);
    }
}
