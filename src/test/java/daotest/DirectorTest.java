package daotest;

import am.aca.dao.*;
import am.aca.entity.*;
import org.junit.*;
import org.junit.Test;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by David on 6/3/2017.
 */
public class DirectorTest {
    private DirectorDao directorDao = new DirectorDaoJdbc();
    private Director director;

    @Before
    public void setUp() throws PropertyVetoException, SQLException, IOException {
        TestHelper.emptyTable(new String[]{"film_to_director","directors"});
    }

    @After
    public void end() throws SQLException, IOException, PropertyVetoException {
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
}
