package daotest;

import am.aca.dao.*;
import am.aca.dao.impljdbc.CastDaoJdbc;
import am.aca.dao.impljdbc.FilmDaoJdbc;
import am.aca.entity.*;
import am.aca.util.ConnType;
import am.aca.util.DbManager;
import org.junit.*;
import org.junit.Test;

import java.sql.SQLException;

/**
 * Created by David on 6/3/2017
 */
public class CastTest {

    private CastDao castDao = new CastDaoJdbc(ConnType.PRODUCTION);
    private FilmDao filmDao = new FilmDaoJdbc(ConnType.PRODUCTION);
    private Cast cast;

    @Before
    public void setUp() {
        castDao.setDataSource(DbManager.getInstance().getDataSource());
        filmDao.setDataSource(DbManager.getInstance().getDataSource());
    }

    @After
    public void end(){
        DbManager.emptyTestTables(new String[]{"film_to_cast","casts", "films"});
        cast = null;
    }

    @Test
    public void addCast_Success(){
        cast = castDao.addCast("Poghos",false);
        Assert.assertTrue(cast.getId()>0);
    }

    @Test
    public void addCast_Success_EmptyOscar(){
        cast = castDao.addCast("Poghos");
        Assert.assertTrue(cast.getId()>0);
    }

    @Test(expected = DaoException.class)
    public void addCast_Fail_NameRequired(){
        castDao.addCast(null,false);
    }

    @Test
    public void editCast_Success(){
        cast = castDao.addCast("Poghos");
        cast.setName("Gagik");
        Assert.assertTrue( castDao.editCast(cast) );
    }

    @Test(expected = DaoException.class)
    public void editCast_Fail_NameNull(){
        cast = castDao.addCast("Poghos");
        cast.setName(null);
        castDao.editCast(cast);
    }

    @Test
    public void listCasts_Succeeded(){
        castDao.addCast("Poghos");
        castDao.addCast("Petros");
        castDao.addCast("Martiros");
        Assert.assertEquals(3, castDao.listCast().size());
    }

    @Test
    public void listCast_Emptylist(){
        Assert.assertEquals(0, castDao.listCast().size());
    }

    @Test
    public void listFilmsIdByCast_Succeeded() throws SQLException {
        Film f1, f2;
        filmDao.addFilm(f1 = new Film("Titanic",1990));
        filmDao.addFilm(f2 = new Film("Zoro",1992));

        Cast cast = castDao.addCast("Trantino");
        castDao.addCastToFilm(cast,f1);
        castDao.addCastToFilm(cast,f2);

        Assert.assertEquals(2, castDao.listFilmsIdByCast(cast.getId()).size());
    }

    @Test
    public void listFilmsIdByCast_EmptyList() throws SQLException {
        Cast cast = castDao.addCast("Trantino");
        Assert.assertEquals(0, castDao.listFilmsIdByCast(cast.getId()).size());
    }
}
