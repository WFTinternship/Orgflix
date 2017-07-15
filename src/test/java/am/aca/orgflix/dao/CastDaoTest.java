package am.aca.orgflix.dao;

import am.aca.orgflix.BaseIntegrationTest;
import am.aca.orgflix.entity.Cast;
import am.aca.orgflix.entity.Film;
import am.aca.orgflix.util.TestHelper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Integration tests for cast DAO methods
 */
public class CastDaoTest extends BaseIntegrationTest {

    @Autowired
    private CastDAO hibernateCastDAO;

    @Autowired
    private FilmDAO hibernateFilmDAO;

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
     * @see am.aca.orgflix.dao.implHibernate.HibernateCastDAO#addCast(am.aca.orgflix.entity.Cast)
     */
    @Test
    public void addCast_Success() {
        cast = new Cast("Woody Allen", true);
        hibernateCastDAO.addCast(cast);

        boolean status = cast.getId() > 0;
        Assert.assertTrue(status);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateCastDAO#addCast(am.aca.orgflix.entity.Cast)
     */
    @Test
    public void addCast_EmptyOscar_Success() {
        cast = new Cast("Woody Allen");
        hibernateCastDAO.addCast(cast);

        boolean status = cast.getId() > 0;
        Assert.assertTrue(status);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateCastDAO#addCast(am.aca.orgflix.entity.Cast)
     */
    @Test
    public void addCast_NameRequired_Fail() {
        cast = new Cast(null, false);
        boolean status = hibernateCastDAO.addCast(cast);
        Assert.assertFalse(status);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateCastDAO#listCast()
     */
    @Test
    public void listCasts_Success() {
        cast = new Cast("Jack Nicholson");
        hibernateCastDAO.addCast(cast);
        cast = new Cast("Matt Damon");
        hibernateCastDAO.addCast(cast);
        cast = new Cast("Mark Wahlberg");
        hibernateCastDAO.addCast(cast);

        java.util.List<Cast> result = hibernateCastDAO.listCast();

        int size = result.size();
        String name0 = "Jack Nicholson";
        String name1 = "Matt Damon";
        String name2 = "Mark Wahlberg";

        Assert.assertEquals(3, size);
        Assert.assertEquals(name0, result.get(0).getName());
        Assert.assertEquals(name1, result.get(1).getName());
        Assert.assertEquals(name2, result.get(2).getName());
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateCastDAO#listCast()
     */
    @Test
    public void listCast_EmptyList_Fail() {
        int size = hibernateCastDAO.listCast().size();
        Assert.assertEquals(0, size);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateCastDAO#getCastById(int)
     */
    @Test
    public void getCastByID_ValidId_Success() {
        cast = new Cast("Tilda Swinton", true);
        hibernateCastDAO.addCast(cast);

        int id = cast.getId(); ///////////////THIS NEEDS TO BE REFRESHED
        Cast actualCast = hibernateCastDAO.getCastById(id);
        String actualCastName = actualCast.getName();
        boolean actualCastHasOscar = actualCast.isHasOscar();

        Assert.assertNotNull(actualCast);
        Assert.assertEquals("Tilda Swinton", actualCastName);
        Assert.assertTrue(actualCastHasOscar);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateCastDAO#getCastById(int)
     */
    @Test
    public void getCastByID_InvalidId_Fail() {
        Cast actualCast = hibernateCastDAO.getCastById(-1);
        Assert.assertNull(actualCast);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateCastDAO#editCast(am.aca.orgflix.entity.Cast)
     */
    @Test
    public void editCast_Success() {
        cast = new Cast("Woody Allen");
        hibernateCastDAO.addCast(cast);
        cast.setName("Hugh Jackman");

        boolean status = hibernateCastDAO.editCast(cast);
        int size = hibernateCastDAO.listCast().size();
        int id = cast.getId();
        String actualCastName = hibernateCastDAO.getCastById(id).getName();

        Assert.assertTrue(status);
        Assert.assertEquals(1, size);
        Assert.assertEquals("Hugh Jackman", actualCastName);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateCastDAO#editCast(am.aca.orgflix.entity.Cast)
     */
    @Test
    public void editCast_NameNull_Fail() {
        cast = new Cast("Woody Allen");
        hibernateCastDAO.addCast(cast);
        cast.setName(null);

        boolean status = hibernateCastDAO.editCast(cast);
        Assert.assertFalse(status);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateCastDAO#addCastToFilm(am.aca.orgflix.entity.Cast, int)
     */
    @Test
    public void addCastToFilm_Success() {
        cast = new Cast("Edward Norton");
        hibernateCastDAO.addCast(cast);
        Film film = new Film("Fight Club", 1997);
        hibernateFilmDAO.addFilm(film);

        boolean status = hibernateCastDAO.addCastToFilm(cast, film.getId());
        Assert.assertTrue(status);

        int id = hibernateFilmDAO.getFilmsByCast(cast.getId()).get(0).getId();
        Assert.assertEquals(film.getId(), id);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateCastDAO#addCastToFilm(am.aca.orgflix.entity.Cast, int)
     */
    @Test
    public void addCastToFilm_Fail() {
        cast = new Cast("Edward Norton");
        hibernateCastDAO.addCast(cast);
        Film film = new Film("Fight Club", 1997);
        hibernateFilmDAO.addFilm(film);
        hibernateCastDAO.addCastToFilm(cast, film.getId());
        hibernateCastDAO.addCastToFilm(cast, film.getId());

        int id = hibernateFilmDAO.getFilmsByCast(cast.getId()).get(0).getId();
        Assert.assertEquals(film.getId(), id);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateCastDAO#remove(am.aca.orgflix.entity.Cast)
     */
    @Test
    public void remove_Valid_Success() {
        cast = new Cast("Woody Allen");
        hibernateCastDAO.addCast(cast);
        int id = cast.getId();

        boolean status = hibernateCastDAO.remove(cast);
        Cast actualCast = hibernateCastDAO.getCastById(id);
        boolean castsEmpty = hibernateCastDAO.listCast().isEmpty();

        Assert.assertTrue(status);
        Assert.assertTrue(castsEmpty);
        Assert.assertNull(actualCast);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateCastDAO#remove(am.aca.orgflix.entity.Cast)
     */
    @Test
    public void remove_Invalid_Fail() {
        cast = new Cast("Woody Allen");

        boolean status = hibernateCastDAO.remove(cast);
        Assert.assertFalse(status);
    }

//    /**
//     * @see am.aca.orgflix.dao.implHibernate.HibernateCastDAO#isStarringIn(int, int)
//     */
//    @Test
//    public void isStarringIn_Success() {
//        cast = new Cast("Woody Allen");
//        hibernateCastDAO.addCast(cast);
//        Film film = new Film("Fading Gigolo", 2013);
//        hibernateFilmDAO.addFilm(film);
//        hibernateCastDAO.addCastToFilm(cast, film.getId());
//
//        boolean status = hibernateCastDAO.isStarringIn(cast.getId(), film.getId());
//        Assert.assertTrue(status);
//    }
//
//    /**
//     * @see am.aca.orgflix.dao.implHibernate.HibernateCastDAO#isStarringIn(int, int)
//     */
//    @Test
//    public void isStarringIn_Fail() {
//        cast = new Cast("Woody Allen");
//        hibernateCastDAO.addCast(cast);
//        Film film = new Film("Lord of the Rings", 2001);
//        hibernateFilmDAO.addFilm(film);
//
//        boolean status = hibernateCastDAO.isStarringIn(cast.getId(), film.getId());
//        Assert.assertFalse(status);
//    }
//
//    /**
//     * @see am.aca.orgflix.dao.implHibernate.HibernateCastDAO#exists(am.aca.orgflix.entity.Cast)
//     */
//    @Test
//    public void exists_Success() {
//        cast = new Cast("Woody Allen");
//        hibernateCastDAO.addCast(cast);
//
//        boolean status = hibernateCastDAO.exists(cast);
//        Assert.assertTrue(status);
//    }
//
//    /**
//     * @see am.aca.orgflix.dao.implHibernate.HibernateCastDAO#exists(am.aca.orgflix.entity.Cast)
//     */
//    @Test
//    public void exists_Fail() {
//        cast = new Cast("Woody Allen");
//
//        boolean status = hibernateCastDAO.exists(cast);
//        Assert.assertFalse(status);
//    }
}
