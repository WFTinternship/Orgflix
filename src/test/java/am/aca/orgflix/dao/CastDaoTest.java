package am.aca.orgflix.dao;

import am.aca.orgflix.BaseIntegrationTest;
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
        helper.emptyTable(new String[]{"FILM_TO_CAST", "CASTS", "FILMS"});
        cast = null;
    }

    /**
     * @see CastDAO#add(am.aca.orgflix.entity.Cast)
     */
    @Test
    public void addCast_ValidCast_Success() {
        cast = new Cast("Woody Allen", true);
        jdbcCastDAO.add(cast);

        int id = cast.getId();
        Assert.assertTrue(id > 0);
    }

    /**
     * @see CastDAO#add(am.aca.orgflix.entity.Cast)
     */
    @Test
    public void addCast_EmptyOscar_Success() {
        cast = new Cast("Woody Allen");
        jdbcCastDAO.add(cast);

        int id = cast.getId();
        Assert.assertTrue(id > 0);
    }

    /**
     * @see CastDAO#add(am.aca.orgflix.entity.Cast)
     */
    @Test(expected = RuntimeException.class)
    public void addCast_NameRequired_Fail() {
        cast = new Cast(null, false);
        jdbcCastDAO.add(cast);
    }

    /**
     * @see CastDAO#addToFilm(am.aca.orgflix.entity.Cast, int)
     */
    @Test
    public void addCastToFilm_ValidInput_Success() {
        cast = new Cast("Edward Norton");
        jdbcCastDAO.add(cast);
        Film film = new Film("Fight Club", 1997);
        jdbcFilmDAO.add(film);

        boolean status = jdbcCastDAO.addToFilm(cast, film.getId());
        Assert.assertTrue(status);

        int id = jdbcFilmDAO.getByCast(cast.getId()).get(0).getId();
        Assert.assertEquals(film.getId(), id);
    }

    /**
     * @see CastDAO#addToFilm(am.aca.orgflix.entity.Cast, int)
     */
    @Test(expected = RuntimeException.class)
    public void addCastToFilm_SameAssociationTwice_Fail() {
        cast = new Cast("Edward Norton");
        jdbcCastDAO.add(cast);
        Film film = new Film("Fight Club", 1997);
        jdbcFilmDAO.add(film);

        boolean status = jdbcCastDAO.addToFilm(cast, film.getId());
        Assert.assertTrue(status);

        jdbcCastDAO.addToFilm(cast, film.getId());
    }

    /**
     * @see CastDAO#addToFilm(am.aca.orgflix.entity.Cast, int)
     */
    @Test(expected = RuntimeException.class)
    public void addCastToFilm_FilmNotExisting_Fail() {
        cast = new Cast("Edward Norton");
        jdbcCastDAO.add(cast);

        jdbcCastDAO.addToFilm(cast, 0);
    }

    /**
     * @see CastDAO#addToFilm(am.aca.orgflix.entity.Cast, int)
     */
    @Test(expected = RuntimeException.class)
    public void addCastToFilm_CastNotExisting_Fail() {
        Film film = new Film("Fight Club", 1997);
        jdbcFilmDAO.add(film);

        jdbcCastDAO.addToFilm(cast, film.getId());
    }

    /**
     * @see CastDAO#getAll()
     */
    @Test
    public void listCasts_Success() {
        cast = new Cast("Jesse Eisenberg");
        jdbcCastDAO.add(cast);
        cast = new Cast("Steve Carell");
        jdbcCastDAO.add(cast);
        cast = new Cast("Woody Allen");
        jdbcCastDAO.add(cast);

        int size = jdbcCastDAO.getAll().size();
        Assert.assertEquals(3, size);

        Cast actualCast = jdbcCastDAO.getAll().get(2);
        Assert.assertEquals(cast, actualCast);
    }

    /**
     * @see CastDAO#getAll()
     */
    @Test
    public void listCast_EmptyList() {
        int size = jdbcCastDAO.getAll().size();
        Assert.assertEquals(0, size);
    }

    /**
     * @see CastDAO#getByFilm(int)
     */
    @Test
    public void getCasts_ByFilm_Success() {
        cast = new Cast("Tom Hanks", true);
        jdbcCastDAO.add(cast);
        Film film = new Film("Forrest Gump", 1990);
        ArrayList<Cast> list = new ArrayList<>();
        list.add(cast);
        film.setCasts(list);
        jdbcFilmDAO.add(film);
        jdbcCastDAO.addToFilm(cast, film.getId());

        int size = jdbcCastDAO.getByFilm(film.getId()).size();
        Assert.assertEquals(1, size);

        Cast actualCast = jdbcCastDAO.getByFilm(film.getId()).get(0);
        Assert.assertEquals(cast, actualCast);

        cast = new Cast("Robin Wright");
        jdbcCastDAO.add(cast);
        jdbcCastDAO.addToFilm(cast, film.getId());

        size = jdbcCastDAO.getByFilm(film.getId()).size();
        Assert.assertEquals(2, size);
    }

    /**
     * @see CastDAO#getByFilm(int)
     */
    @Test
    public void getCastsByFilm_EmptyCastList_Fail() {
        Film film = new Film("Forrest Gump", 1990);
        film.addCast(cast);
        jdbcFilmDAO.add(film);

        int size = jdbcCastDAO.getByFilm(film.getId()).size();
        Assert.assertEquals(0, size);
    }

    /**
     * @see CastDAO#getById(int)
     */
    @Test
    public void getCastById_ValidId_Success() {
        cast = new Cast("Tom Hanks", true);
        jdbcCastDAO.add(cast);

        Cast actualCast = jdbcCastDAO.getById(cast.getId());
        Assert.assertEquals(cast, actualCast);
    }

    /**
     * @see CastDAO#getById(int)
     */
    @Test(expected = RuntimeException.class)
    public void getCastById_InvalidId_Success() {
        jdbcCastDAO.getById(0);
    }

    /**
     * @see CastDAO#edit(am.aca.orgflix.entity.Cast)
     */
    @Test
    public void editCast_ValidUpdate_Success() {
        cast = new Cast("Tom Hanks", true);
        jdbcCastDAO.add(cast);
        cast.setName("Russel Crowe");

        boolean status = jdbcCastDAO.edit(cast);
        Assert.assertTrue(status);

        int size = jdbcCastDAO.getAll().size();
        Assert.assertEquals(1, size);

        Cast actualCast = jdbcCastDAO.getAll().get(0);
        Assert.assertEquals(cast, actualCast);
    }

    /**
     * @see CastDAO#edit(am.aca.orgflix.entity.Cast)
     */
    @Test(expected = RuntimeException.class)
    public void editCast_NameNull_Fail() {
        cast = new Cast("Woody Allen");
        jdbcCastDAO.add(cast);

        cast.setName(null);
        jdbcCastDAO.edit(cast);
    }

    /**
     * @see CastDAO#edit(am.aca.orgflix.entity.Cast)
     */
    @Test
    public void editCast_InvalidCast_Fail() {
        cast = new Cast("Woody Allen");
        boolean status = jdbcCastDAO.edit(cast);
        Assert.assertFalse(status);
    }

    /**
     * @see CastDAO#remove(am.aca.orgflix.entity.Cast)
     */
    @Test
    public void remove_ValidCast_Success() {
        cast = new Cast("Woody Allen");
        jdbcCastDAO.add(cast);

        boolean status = jdbcCastDAO.remove(cast);
        Assert.assertTrue(status);

        java.util.List<Cast> casts = jdbcCastDAO.getAll();
        Assert.assertTrue(casts.isEmpty());
    }

    /**
     * @see CastDAO#remove(am.aca.orgflix.entity.Cast)
     */
    @Test
    public void remove_InvalidCast_Fail() {
        cast = new Cast("Woody Allen");

        boolean status = jdbcCastDAO.remove(cast);
        Assert.assertFalse(status);
    }

    /**
     * @see CastDAO#isRelatedToFilm(int, int)
     */
    @Test
    public void isStarringIn_ValidInput_Success() {
        cast = new Cast("Woody Allen");
        jdbcCastDAO.add(cast);
        Film film = new Film("Fading Gigolo", 2013);
        jdbcFilmDAO.add(film);
        jdbcCastDAO.addToFilm(cast, film.getId());

        boolean status = jdbcCastDAO.isRelatedToFilm(cast.getId(), film.getId());
        Assert.assertTrue(status);

        film = new Film("Lord of the Rings", 2001);
        jdbcFilmDAO.add(film);

        status = jdbcCastDAO.isRelatedToFilm(cast.getId(), film.getId());
        Assert.assertFalse(status);
    }

    /**
     * @see CastDAO#isRelatedToFilm(int, int)
     */
    @Test
    public void isStarringIn_InvalidCast_Fail() {
        Film film = new Film("Lord of the Rings", 2001);
        jdbcFilmDAO.add(film);

        boolean status = jdbcCastDAO.isRelatedToFilm(0, film.getId());
        Assert.assertFalse(status);
    }

    /**
     * @see CastDAO#isRelatedToFilm(int, int)
     */
    @Test
    public void isStarringIn_InvalidFilm_Fail() {
        Cast cast = new Cast("Woody Allen", true);
        jdbcCastDAO.add(cast);

        boolean status = jdbcCastDAO.isRelatedToFilm(cast.getId(), 0);
        Assert.assertFalse(status);
    }

    /**
     * @see CastDAO#exists(am.aca.orgflix.entity.Cast)
     */
    @Test
    public void exists_ValidCast_Success() {
        cast = new Cast("Woody Allen");
        jdbcCastDAO.add(cast);

        boolean status = jdbcCastDAO.exists(cast);
        Assert.assertTrue(status);
    }

    /**
     * @see CastDAO#exists(am.aca.orgflix.entity.Cast)
     */
    @Test
    public void exists_InvalidCast_Fail() {
        cast = new Cast("Woody Allen");

        boolean status = jdbcCastDAO.exists(cast);
        Assert.assertFalse(status);
    }
}
