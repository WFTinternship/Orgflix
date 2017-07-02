package am.aca.orgflix.integration.daotest.jdbc;

import am.aca.dao.DaoException;
import am.aca.dao.jdbc.CastDAO;
import am.aca.dao.jdbc.FilmDAO;
import am.aca.entity.Cast;
import am.aca.entity.Film;
import am.aca.orgflix.BaseIntegrationTest;
import am.aca.util.TestHelper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Test for cast DAO methods
 */
public class CastDaoTest extends BaseIntegrationTest {

    @Autowired
    private CastDAO jdbcCastDAO;

    @Autowired
    private FilmDAO jdbcFilmDAO;

    @Autowired
    private TestHelper helper;

    private Cast cast;

    @Before
    public void setUp() {
    }

    @After
    public void end() {
        helper.emptyTable(new String[]{"film_to_cast", "casts"});
        cast = null;
    }

    @Test
    public void addCast_Success() {
        cast = new Cast("Woody Allen", true);
        jdbcCastDAO.addCast(cast);
        Assert.assertTrue(cast.getId() > 0);
    }

    @Test
    public void addCast_Success_EmptyOscar() {
        cast = new Cast("Woody Allen");
        jdbcCastDAO.addCast(cast);
        Assert.assertTrue(cast.getId() > 0);
    }

    @Test(expected = DaoException.class)
    public void addCast_Fail_NameRequired() {
        cast = new Cast(null, false);
        jdbcCastDAO.addCast(cast);
    }

    @Test
    public void addCastToFilm_Success() {
        cast = new Cast("Edward Norton");
        jdbcCastDAO.addCast(cast);
        Film film = new Film("Fight Club", 1997);
        jdbcFilmDAO.addFilm(film);
        Assert.assertTrue(jdbcCastDAO.addCastToFilm(cast, film.getId()));
        Assert.assertEquals(film.getId(), jdbcFilmDAO.getFilmsByCast(cast.getId()).get(0).getId());
    }

    @Test
    public void addCastToFilm_Failed() {
        cast = new Cast("Edward Norton");
        jdbcCastDAO.addCast(cast);
        Film film = new Film("Fight Club", 1997);
        jdbcFilmDAO.addFilm(film);
        jdbcCastDAO.addCastToFilm(cast, film.getId());
        jdbcCastDAO.addCastToFilm(cast, film.getId());
        Assert.assertEquals(film.getId(), jdbcFilmDAO.getFilmsByCast(cast.getId()).get(0).getId());
    }

    @Test
    public void listCasts_Succeeded() {
        cast = new Cast("Poghos");
        jdbcCastDAO.addCast(cast);
        cast = new Cast("Petros");
        jdbcCastDAO.addCast(cast);
        cast = new Cast("Martiros");
        jdbcCastDAO.addCast(cast);
        Assert.assertEquals(3, jdbcCastDAO.listCast().size());
    }

    @Test
    public void listCastByName_Success() {
        cast = new Cast("Woody Allen");
        jdbcCastDAO.addCast(cast);
        Assert.assertEquals("Woody Allen", jdbcCastDAO.listCast().get(0).getName());
    }

    @Test
    public void getCastsByFilm_succeeded(){
        cast = new Cast("Tom Henks");
        jdbcCastDAO.addCast(cast);
        Film film = new Film("Forest Gump",1990);
        ArrayList<Cast> list = new ArrayList<>();
        list.add(cast);
        film.setCasts(list);
        jdbcFilmDAO.addFilm(film);
        jdbcCastDAO.addCastToFilm(cast,film.getId());
        Assert.assertEquals(1, jdbcCastDAO.getCastsByFilm(film.getId()).size());
    }

    @Test
    public void getCastsByFilm_fail_EmptyCastList(){
        Film film = new Film("Forest Gump",1990);
        ArrayList<Cast> list = new ArrayList<>();
        film.setCasts(list);
        jdbcFilmDAO.addFilm(film);
        Assert.assertNotEquals(1, jdbcCastDAO.getCastsByFilm(film.getId()).size());
    }

    @Test
    public void listCast_Emptylist() {
        Assert.assertEquals(0, jdbcCastDAO.listCast().size());
    }

    @Test
    public void editCast_Success() {
        cast = new Cast("Woody Allen");
        jdbcCastDAO.addCast(cast);
        cast.setName("Christopher Nolan");
        Assert.assertTrue(jdbcCastDAO.editCast(cast));
    }

    @Test
    public void editCastBySize_Success() {
        cast = new Cast("Woody Allen");
        jdbcCastDAO.addCast(cast);
        cast.setName("Christopher Nolan");
        jdbcCastDAO.editCast(cast);
        Assert.assertEquals(1, jdbcCastDAO.listCast().size());
    }

    @Test(expected = DaoException.class)
    public void editCast_Fail_NameNull() {
        cast = new Cast("Woody Allen");
        jdbcCastDAO.addCast(cast);
        cast.setName(null);
        jdbcCastDAO.editCast(cast);
    }

    @Test
    public void remove_Success() {
        cast = new Cast("Woody Allen");
        jdbcCastDAO.addCast(cast);
        Assert.assertTrue(jdbcCastDAO.remove(cast));
    }

    @Test
    public void removeCheckBySize_Success() {
        cast = new Cast("Woody Allen");
        jdbcCastDAO.addCast(cast);
        jdbcCastDAO.remove(cast);
        Assert.assertTrue(jdbcCastDAO.listCast().isEmpty());
    }

    @Test
    public void remove_Fail() {
        cast = new Cast("Woody Allen");
        Assert.assertFalse(jdbcCastDAO.remove(cast));
    }

    @Test
    public void listFilmsByCast_Succeeded() throws SQLException {
        Film f1, f2;
        jdbcFilmDAO.addFilm(f1 = new Film("Titanic",1990));
        jdbcFilmDAO.addFilm(f2 = new Film("Zoro",1992));
        cast = new Cast("Tarantino");
        jdbcCastDAO.addCast(cast);
        jdbcCastDAO.addCastToFilm(cast,f1.getId());
        jdbcCastDAO.addCastToFilm(cast,f2.getId());
        Assert.assertEquals(2, jdbcFilmDAO.getFilmsByCast(cast.getId()).size());
    }

    @Test
    public void listFilmsIdByCast_EmptyList() throws SQLException {
        cast = new Cast("Woody Allen");
        jdbcCastDAO.addCast(cast);
        Assert.assertEquals(0, jdbcFilmDAO.getFilmsByCast(cast.getId()).size());
    }

    @Test
    public void isStarringIn_Success() {
        cast = new Cast("Woody Allen");
        jdbcCastDAO.addCast(cast);
        Film film = new Film("Fading Gigolo", 2013);
        jdbcFilmDAO.addFilm(film);
        jdbcCastDAO.addCastToFilm(cast, film.getId());
        Assert.assertTrue(jdbcCastDAO.isStarringIn(cast.getId(), film.getId()));
    }

    @Test
    public void isStarringIn_Fail() {
        cast = new Cast("Woody Allen");
        jdbcCastDAO.addCast(cast);
        Film film = new Film("Lord of the Rings", 2001);
        jdbcFilmDAO.addFilm(film);
        Assert.assertFalse(jdbcCastDAO.isStarringIn(cast.getId(), film.getId()));
    }

    @Test
    public void exists_Success() {
        cast = new Cast("Woody Allen");
        jdbcCastDAO.addCast(cast);
        Assert.assertTrue(jdbcCastDAO.exists(cast));
    }

    @Test
    public void exists_Fail() {
        cast = new Cast("Woody Allen");
        Assert.assertFalse(jdbcCastDAO.exists(cast));
    }
}
