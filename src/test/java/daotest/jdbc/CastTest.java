package daotest.jdbc;

import am.aca.dao.*;
import am.aca.dao.jdbc.impljdbc.JdbcCastDAO;
import am.aca.dao.jdbc.FilmDAO;
import am.aca.dao.jdbc.impljdbc.JdbcFilmDAO;
import am.aca.dao.jdbc.CastDAO;
import am.aca.entity.*;
import am.aca.util.TestHelper;
import org.junit.*;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.SQLException;

/**
 * Created by David on 6/3/2017
 */
public class CastTest {

    private ApplicationContext ctx =
            new ClassPathXmlApplicationContext("applicationContext-persistance-test.xml");

    private CastDAO castDAO = ctx.getBean("jdbcCastDAO", JdbcCastDAO.class);
    private FilmDAO filmDAO = ctx.getBean("jdbcFilmDAO", JdbcFilmDAO.class);

    private TestHelper helper = ctx.getBean("testHelper", TestHelper.class);

    private Cast cast;

    @Before
    public void setUp() {
    }

    @After
    public void end(){
        helper.emptyTable(new String[]{"film_to_cast","casts"});
        cast = null;
    }

    @Test
    public void addCast_Success(){
        cast = new Cast("Woody Allen", true);
        castDAO.addCast(cast);
//        cast = castDAO.addCast("Poghos",false);
        Assert.assertTrue(cast.getId()>0);
    }

    @Test
    public void addCast_Success_EmptyOscar(){
        cast = new Cast("Woody Allen");
        castDAO.addCast(cast);
//        cast = castDAO.addCast("Poghos");
        Assert.assertTrue(cast.getId()>0);
    }

    @Test(expected = DaoException.class)
    public void addCast_Fail_NameRequired(){
        cast = new Cast(null, false);
        castDAO.addCast(cast);
//        castDAO.addCast(null,false);
    }

    @Test
    public void addCastToFilm_Success() {
        cast = new Cast("Edward Norton");
        castDAO.addCast(cast);
        Film film = new Film("Fight Club", 1997);
        filmDAO.addFilm(film);
        castDAO.addCastToFilm(cast, film);
        Assert.assertEquals(film.getId(), castDAO.listFilmsByCast(cast.getId()).get(0).getId());
    }

    @Test
    public void addCastToFilm_Failed() {
        cast = new Cast("Edward Norton");
        castDAO.addCast(cast);
        Film film = new Film("Fight Club", 1997);
        filmDAO.addFilm(film);
        castDAO.addCastToFilm(cast, film);
        castDAO.addCastToFilm(cast, film);
        Assert.assertEquals(film.getId(), castDAO.listFilmsByCast(cast.getId()).get(0).getId());
    }

    @Test
    public void addCastToFilmById_Success() {
        cast = new Cast("Edward Norton");
        castDAO.addCast(cast);
        Film film = new Film("Fight Club", 1997);
        filmDAO.addFilm(film);
        castDAO.addCastToFilm(cast.getId(), film.getId());
        Assert.assertEquals(film.getId(), castDAO.listFilmsByCast(cast.getId()).get(0).getId());
    }

    @Test
    public void addCastToFilmById_Failed() {
        cast = new Cast("Edward Norton");
        castDAO.addCast(cast);
        Film film = new Film("Fight Club", 1997);
        filmDAO.addFilm(film);
        castDAO.addCastToFilm(cast.getId(), film.getId());
        castDAO.addCastToFilm(cast.getId(), film.getId());
        Assert.assertEquals(film.getId(), castDAO.listFilmsByCast(cast.getId()).get(0).getId());
    }

    @Test
    public void editCast_Success(){
        cast = new Cast("Woody Allen");
        castDAO.addCast(cast);
        cast.setName("Christopher Nolan");
//        cast = castDAO.addCast("Poghos");
//        cast.setName("Gagik");
        Assert.assertTrue( castDAO.editCast(cast) );
    }

    @Test
    public void editCastBySize_Success(){
        cast = new Cast("Woody Allen");
        castDAO.addCast(cast);
        cast.setName("Christopher Nolan");
        castDAO.editCast(cast);
        Assert.assertEquals(1, castDAO.listCast().size() );
    }

    @Test(expected = DaoException.class)
    public void editCast_Fail_NameNull(){
        cast = new Cast("Woody Allen");
        castDAO.addCast(cast);
        cast.setName(null);
        castDAO.editCast(cast);
//        cast = castDAO.addCast("Poghos");
//        cast.setName(null);
//        castDAO.editCast(cast);
    }

    @Test
    public void listCasts_Succeeded(){
        cast = new Cast("Poghos");
        castDAO.addCast(cast);
        cast = new Cast("Petros");
        castDAO.addCast(cast);
        cast = new Cast("Martiros");
        castDAO.addCast(cast);
//        castDAO.addCast("Poghos");
//        castDAO.addCast("Petros");
//        castDAO.addCast("Martiros");
        Assert.assertEquals(3, castDAO.listCast().size());
//        Cast cast = (Cast) castDAO.listCast().get(0);
//        Assert.assertEquals(new Cast("Poghos",false).getName(),cast.getName());
    }

    @Test
    public void listCastByName_Success() {
        cast = new Cast("Woody Allen");
        castDAO.addCast(cast);
        Assert.assertEquals("Woody Allen", castDAO.listCast().get(0).getName());
    }

    @Test
    public void listCast_Emptylist(){
        Assert.assertEquals(0, castDAO.listCast().size());
    }

    @Test
    public void listFilmsByCast_Succeeded() throws SQLException {
        Film f1, f2;
        filmDAO.addFilm(f1 = new Film("Titanic",1990));
        filmDAO.addFilm(f2 = new Film("Zoro",1992));
        cast = new Cast("Tarantino");
        castDAO.addCast(cast);
//        Cast cast = castDAO.addCast("Trantino");
        castDAO.addCastToFilm(cast,f1);
        castDAO.addCastToFilm(cast,f2);
        Assert.assertEquals(2, castDAO.listFilmsByCast(cast.getId()).size());
    }

    @Test
    public void listFilmsIdByCast_EmptyList() throws SQLException {
        cast = new Cast("Woody Allen");
        castDAO.addCast(cast);
//        Cast cast = castDAO.addCast("Trantino");
        Assert.assertEquals(0, castDAO.listFilmsByCast(cast.getId()).size());
    }

    @Test
    public void isStarringIn_Success() {
        cast = new Cast("Woody Allen");
        castDAO.addCast(cast);
        Film film = new Film("Fading Gigolo", 2013);
        filmDAO.addFilm(film);
        castDAO.addCastToFilm(cast, film);
        Assert.assertTrue(castDAO.isStarringIn(cast.getId(), film.getId()));
    }

    @Test
    public void isStarrintIn_Fail() {
        cast = new Cast("Woody Allen");
        castDAO.addCast(cast);
        Film film = new Film("Lord of the Rings", 2001);
        filmDAO.addFilm(film);
        Assert.assertFalse(castDAO.isStarringIn(cast.getId(), film.getId()));
    }

    @Test
    public void exists_Success() {
        cast = new Cast("Woody Allen");
        castDAO.addCast(cast);
        Assert.assertTrue(castDAO.exists(cast));
    }

    @Test
    public void exists_Fail() {
        cast = new Cast("Woody Allen");
        Assert.assertFalse(castDAO.exists(cast));
    }
}