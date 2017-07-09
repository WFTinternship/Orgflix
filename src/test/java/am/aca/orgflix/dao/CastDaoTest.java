package am.aca.orgflix.dao;

import am.aca.orgflix.BaseIntegrationTest;
import am.aca.orgflix.dao.impljdbc.JdbcCastDAO;
import am.aca.orgflix.entity.Cast;
import am.aca.orgflix.entity.Film;
import am.aca.orgflix.util.TestHelper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

/**
 * Integration tests for cast DAO methods
 */
public class CastDaoTest extends BaseIntegrationTest {

    @Autowired
    private CastDAO jdbcCastDAO;

    @Autowired
    private FilmDAO jdbcFilmDAO;

    @Autowired
    private TestHelper helper;

    private Cast cast;

    /**
     * Rolls back all changes applied to the test DB resulted from tests
     */
    @After
    public void tearDown() {
        helper.emptyTable(new String[]{"film_to_cast", "casts"});
        cast = null;
    }

    /**
     * @see JdbcCastDAO#addCast(am.aca.orgflix.entity.Cast)
     */
    @Test
    public void addCast_Success() {
        cast = new Cast("Woody Allen", true);
        jdbcCastDAO.addCast(cast);

        boolean status = cast.getId() > 0;
        Assert.assertTrue(status);
    }

    /**
     * @see JdbcCastDAO#addCast(am.aca.orgflix.entity.Cast)
     */
    @Test
    public void addCast_EmptyOscar_Success() {
        cast = new Cast("Woody Allen");
        jdbcCastDAO.addCast(cast);

        boolean status = cast.getId() > 0;
        Assert.assertTrue(status);
    }

    /**
     * @see JdbcCastDAO#addCast(am.aca.orgflix.entity.Cast)
     */
    @Test(expected = DaoException.class)
    public void addCast_NameRequired_Fail() {
        cast = new Cast(null, false);
        jdbcCastDAO.addCast(cast);
    }

    /**
     * @see JdbcCastDAO#addCastToFilm(am.aca.orgflix.entity.Cast, int)
     */
    @Test
    public void addCastToFilm_Success() {
        cast = new Cast("Edward Norton");
        jdbcCastDAO.addCast(cast);
        Film film = new Film("Fight Club", 1997);
        jdbcFilmDAO.addFilm(film);

        boolean status = jdbcCastDAO.addCastToFilm(cast, film.getId());
        Assert.assertTrue(status);

        int id = jdbcFilmDAO.getFilmsByCast(cast.getId()).get(0).getId();
        Assert.assertEquals(film.getId(), id);
    }

    /**
     * @see JdbcCastDAO#addCastToFilm(am.aca.orgflix.entity.Cast, int)
     */
    @Test
    public void addCastToFilm_Fail() {
        cast = new Cast("Edward Norton");
        jdbcCastDAO.addCast(cast);
        Film film = new Film("Fight Club", 1997);
        jdbcFilmDAO.addFilm(film);
        jdbcCastDAO.addCastToFilm(cast, film.getId());
        jdbcCastDAO.addCastToFilm(cast, film.getId());

        int id = jdbcFilmDAO.getFilmsByCast(cast.getId()).get(0).getId();
        Assert.assertEquals(film.getId(), id);
    }

    /**
     * @see JdbcCastDAO#listCast()
     */
    @Test
    public void listCasts_Success() {
        cast = new Cast("Poghos");
        jdbcCastDAO.addCast(cast);
        cast = new Cast("Petros");
        jdbcCastDAO.addCast(cast);
        cast = new Cast("Martiros");
        jdbcCastDAO.addCast(cast);

        int size = jdbcCastDAO.listCast().size();
        Assert.assertEquals(3, size);
    }

    /**
     * @see JdbcCastDAO#listCast()
     */
    @Test
    public void listCast_ByName_Success() {
        cast = new Cast("Woody Allen");
        jdbcCastDAO.addCast(cast);

        String actualName = jdbcCastDAO.listCast().get(0).getName();
        Assert.assertEquals("Woody Allen", actualName);
    }

    /**
     * @see JdbcCastDAO#listCast()
     */
    @Test
    public void listCast_EmptyList_Fail() {
        int size = jdbcCastDAO.listCast().size();
        Assert.assertEquals(0, size);
    }

    /**
     * @see JdbcCastDAO#getCastsByFilm(int)
     */
    @Test
    public void getCasts_ByFilm_Success() {
        cast = new Cast("Tom Henks");
        jdbcCastDAO.addCast(cast);
        Film film = new Film("Forest Gump", 1990);
        ArrayList<Cast> list = new ArrayList<>();
        list.add(cast);
        film.setCasts(list);
        jdbcFilmDAO.addFilm(film);
        jdbcCastDAO.addCastToFilm(cast, film.getId());

        int size = jdbcCastDAO.getCastsByFilm(film.getId()).size();
        Assert.assertEquals(1, size);
    }

    /**
     * @see JdbcCastDAO#getCastsByFilm(int)
     */
    @Test
    public void getCasts_ByFilmEmptyCastList_Fail() {
        Film film = new Film("Forest Gump", 1990);
        ArrayList<Cast> list = new ArrayList<>();
        film.setCasts(list);
        jdbcFilmDAO.addFilm(film);

        int size = jdbcCastDAO.getCastsByFilm(film.getId()).size();
        Assert.assertNotEquals(1, size);
    }

    /**
     * @see JdbcCastDAO#editCast(am.aca.orgflix.entity.Cast)
     */
    @Test
    public void editCast_Success() {
        cast = new Cast("Woody Allen");
        jdbcCastDAO.addCast(cast);
        cast.setName("Christopher Nolan");

        boolean status = jdbcCastDAO.editCast(cast);
        Assert.assertTrue(status);
    }

    /**
     * @see JdbcCastDAO#editCast(am.aca.orgflix.entity.Cast)
     */
    @Test
    public void editCast_BySize_Success() {
        cast = new Cast("Woody Allen");
        jdbcCastDAO.addCast(cast);
        cast.setName("Christopher Nolan");
        jdbcCastDAO.editCast(cast);

        int size = jdbcCastDAO.listCast().size();
        Assert.assertEquals(1, size);
    }

    /**
     * @see JdbcCastDAO#editCast(am.aca.orgflix.entity.Cast)
     */
    @Test(expected = DaoException.class)
    public void editCast_NameNull_Fail() {
        cast = new Cast("Woody Allen");
        jdbcCastDAO.addCast(cast);
        cast.setName(null);
        jdbcCastDAO.editCast(cast);
    }

    /**
     * @see JdbcCastDAO#remove(am.aca.orgflix.entity.Cast)
     */
    @Test
    public void remove_Success() {
        cast = new Cast("Woody Allen");
        jdbcCastDAO.addCast(cast);

        boolean status = jdbcCastDAO.remove(cast);
        Assert.assertTrue(status);
    }

    /**
     * @see JdbcCastDAO#remove(am.aca.orgflix.entity.Cast)
     */
    @Test
    public void remove_CheckBySize_Success() {
        cast = new Cast("Woody Allen");
        jdbcCastDAO.addCast(cast);
        jdbcCastDAO.remove(cast);

        boolean status = jdbcCastDAO.listCast().isEmpty();
        Assert.assertTrue(status);
    }

    /**
     * @see JdbcCastDAO#remove(am.aca.orgflix.entity.Cast)
     */
    @Test
    public void remove_Fail() {
        cast = new Cast("Woody Allen");

        boolean status = jdbcCastDAO.remove(cast);
        Assert.assertFalse(status);
    }

    /**
     * @see JdbcCastDAO#isStarringIn(int, int)
     */
    @Test
    public void isStarringIn_Success() {
        cast = new Cast("Woody Allen");
        jdbcCastDAO.addCast(cast);
        Film film = new Film("Fading Gigolo", 2013);
        jdbcFilmDAO.addFilm(film);
        jdbcCastDAO.addCastToFilm(cast, film.getId());

        boolean status = jdbcCastDAO.isStarringIn(cast.getId(), film.getId());
        Assert.assertTrue(status);
    }

    /**
     * @see JdbcCastDAO#isStarringIn(int, int)
     */
    @Test
    public void isStarringIn_Fail() {
        cast = new Cast("Woody Allen");
        jdbcCastDAO.addCast(cast);
        Film film = new Film("Lord of the Rings", 2001);
        jdbcFilmDAO.addFilm(film);

        boolean status = jdbcCastDAO.isStarringIn(cast.getId(), film.getId());
        Assert.assertFalse(status);
    }

    /**
     * @see JdbcCastDAO#exists(am.aca.orgflix.entity.Cast)
     */
    @Test
    public void exists_Success() {
        cast = new Cast("Woody Allen");
        jdbcCastDAO.addCast(cast);

        boolean status = jdbcCastDAO.exists(cast);
        Assert.assertTrue(status);
    }

    /**
     * @see JdbcCastDAO#exists(am.aca.orgflix.entity.Cast)
     */
    @Test
    public void exists_Fail() {
        cast = new Cast("Woody Allen");

        boolean status = jdbcCastDAO.exists(cast);
        Assert.assertFalse(status);
    }
}