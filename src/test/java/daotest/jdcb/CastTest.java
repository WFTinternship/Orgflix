package daotest.jdcb;

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

    ApplicationContext ctx =
            new ClassPathXmlApplicationContext("applicationContext-persistance-test.xml");

    private CastDAO castDAO = ctx.getBean("JdbcCastDAO", JdbcCastDAO.class);
    private FilmDAO filmDAO = ctx.getBean("JdbcFilmDAO", JdbcFilmDAO.class);

    private TestHelper helper = ctx.getBean("TestHelper", TestHelper.class);

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
        cast = castDAO.addCast("Poghos",false);
        Assert.assertTrue(cast.getId()>0);
    }

    @Test
    public void addCast_Success_EmptyOscar(){
        cast = castDAO.addCast("Poghos");
        Assert.assertTrue(cast.getId()>0);
    }

    @Test(expected = DaoException.class)
    public void addCast_Fail_NameRequired(){
        castDAO.addCast(null,false);
    }

    @Test
    public void editCast_Success(){
        cast = castDAO.addCast("Poghos");
        cast.setName("Gagik");
        Assert.assertTrue( castDAO.editCast(cast) );
    }

    @Test(expected = DaoException.class)
    public void editCast_Fail_NameNull(){
        cast = castDAO.addCast("Poghos");
        cast.setName(null);
        castDAO.editCast(cast);
    }

    @Test
    public void listCasts_Succeeded(){
        castDAO.addCast("Poghos");
        castDAO.addCast("Petros");
        castDAO.addCast("Martiros");
        Assert.assertEquals(3, castDAO.listCast().size());
        Cast cast = (Cast) castDAO.listCast().get(0);
        Assert.assertEquals(new Cast("Poghos",false).getName(),cast.getName());
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
        Cast cast = castDAO.addCast("Trantino");
        castDAO.addCastToFilm(cast,f1);
        castDAO.addCastToFilm(cast,f2);
        Assert.assertEquals(2, castDAO.listFilmsByCast(cast.getId()).size());
    }

    @Test
    public void listFilmsIdByCast_EmptyList() throws SQLException {
        Cast cast = castDAO.addCast("Trantino");
        Assert.assertEquals(0, castDAO.listFilmsByCast(cast.getId()).size());
    }
}
