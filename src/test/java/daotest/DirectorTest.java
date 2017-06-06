package daotest;

import am.aca.dao.*;
import am.aca.dao.impljdbc.DirectorDaoJdbc;
import am.aca.dao.impljdbc.FilmDaoJdbc;
import am.aca.entity.*;
import org.apache.log4j.Logger;
import org.junit.*;
import org.junit.Test;

import java.sql.SQLException;

/**
 * Created by David on 6/3/2017
 */
public class DirectorTest {
    private static final Logger LOGGER = Logger.getLogger( DirectorTest.class );

    private DirectorDao directorDao = new DirectorDaoJdbc();
    private Director director;

    @Before
    public void setUp() {
    }

    @After
    public void end(){
        TestHelper.emptyTable(new String[]{"film_to_director","directors"});
        director = null;
    }

    @Test
    public void addDirector_Success(){
        director = directorDao.addDirector("Poghos",false);
        Assert.assertTrue(director.getId()>0);
    }

    @Test
    public void addDirector_Success_EmptyOscar(){
        director = directorDao.addDirector("Poghos");
        Assert.assertTrue(director.getId()>0);
    }

    @Test(expected = DaoException.class)
    public void addDirector_Fail_NameRequired(){
        directorDao.addDirector(null,false);
    }

    @Test
    public void editDirector_Success(){
        director = directorDao.addDirector("Poghos");
        director.setName("Gagik");
        Assert.assertTrue( directorDao.editDirector(director) );
    }

    @Test(expected = DaoException.class)
    public void editDirector_Fail_NameNull(){
        director = directorDao.addDirector("Poghos");
        director.setName(null);
        directorDao.editDirector(director);
    }

    @Test
    public void listDirectors_Succeeded(){
        directorDao.addDirector("Poghos");
        directorDao.addDirector("Petros");
        directorDao.addDirector("Martiros");
        Assert.assertEquals(3,directorDao.listDirectors().size());
    }

    @Test
    public void listDirector_Emptylist(){
        Assert.assertEquals(0,directorDao.listDirectors().size());
    }

    @Test
    public void listFilmsIdByDirector_Succeeded() throws SQLException {
        FilmDao filmDao = new FilmDaoJdbc();
        Film f1, f2;
        filmDao.addFilm(f1 = new Film("Titanic",1990));
        filmDao.addFilm(f2 = new Film("Zoro",1992));
        Director director = directorDao.addDirector("Trantino");
        directorDao.addDirectorToFilm(director,f1);
        directorDao.addDirectorToFilm(director,f2);
        Assert.assertEquals(2,directorDao.listFilmsIdByDirector(director.getId()).size());
    }

    @Test
    public void listFilmsIdByDirector_EMptyList() throws SQLException {
        Director director = directorDao.addDirector("Trantino");
        Assert.assertEquals(0,directorDao.listFilmsIdByDirector(director.getId()).size());
    }
}
