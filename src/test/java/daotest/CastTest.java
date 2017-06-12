package daotest;

import am.aca.dao.*;
import am.aca.dao.impljdbc.CastDaoJdbc;
import am.aca.dao.impljdbc.FilmDaoJdbc;
import am.aca.entity.*;
import org.apache.log4j.Logger;
import org.junit.*;
import org.junit.Test;

import java.sql.SQLException;

/**
 * Created by David on 6/3/2017
 */
public class CastTest {
    private static final Logger LOGGER = Logger.getLogger( CastTest.class );

    private CastDao CastDao = new CastDaoJdbc();
    private Cast cast;

    @Before
    public void setUp() {
    }

    @After
    public void end(){
        TestHelper.emptyTable(new String[]{"film_to_director","directors"});
        cast = null;
    }

    @Test
    public void addDirector_Success(){
        cast = CastDao.addCast("Poghos",false);
        Assert.assertTrue(cast.getId()>0);
    }

    @Test
    public void addDirector_Success_EmptyOscar(){
        cast = CastDao.addCast("Poghos");
        Assert.assertTrue(cast.getId()>0);
    }

    @Test(expected = DaoException.class)
    public void addDirector_Fail_NameRequired(){
        CastDao.addCast(null,false);
    }

    @Test
    public void editDirector_Success(){
        cast = CastDao.addCast("Poghos");
        cast.setName("Gagik");
        Assert.assertTrue( CastDao.editCast(cast) );
    }

    @Test(expected = DaoException.class)
    public void editDirector_Fail_NameNull(){
        cast = CastDao.addCast("Poghos");
        cast.setName(null);
        CastDao.editCast(cast);
    }

    @Test
    public void listDirectors_Succeeded(){
        CastDao.addCast("Poghos");
        CastDao.addCast("Petros");
        CastDao.addCast("Martiros");
        Assert.assertEquals(3, CastDao.listCast().size());
    }

    @Test
    public void listDirector_Emptylist(){
        Assert.assertEquals(0, CastDao.listCast().size());
    }

    @Test
    public void listFilmsIdByDirector_Succeeded() throws SQLException {
        FilmDao filmDao = new FilmDaoJdbc();
        Film f1, f2;
        filmDao.addFilm(f1 = new Film("Titanic",1990));
        filmDao.addFilm(f2 = new Film("Zoro",1992));
        Cast cast = CastDao.addCast("Trantino");
        CastDao.addCastToFilm(cast,f1);
        CastDao.addCastToFilm(cast,f2);
        Assert.assertEquals(2, CastDao.listFilmsIdByCast(cast.getId()).size());
    }

    @Test
    public void listFilmsIdByDirector_EMptyList() throws SQLException {
        Cast cast = CastDao.addCast("Trantino");
        Assert.assertEquals(0, CastDao.listFilmsIdByCast(cast.getId()).size());
    }
}