package daotest;

import am.aca.dao.impljdbc.CastDaoJdbc;
import am.aca.dao.FilmDao;
import am.aca.dao.impljdbc.FilmDaoJdbc;
import am.aca.entity.Cast;
import am.aca.entity.Film;
import am.aca.util.DbManager;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Vardan on 04.06.2017.
 *
 */
public class FilmTest {
    private FilmDao filmDao = new FilmDaoJdbc();
    private Film film;
    private Cast cast;
    private List<Cast> castList;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        DbManager.emptyTable(new String[]{"film_to_cast","films","casts"});
    }

    @Test
    public void addFilm_Succeeded() throws SQLException {

        film = new Film();
        cast = new CastDaoJdbc().addCast("Matt Ross",false);
        castList = new ArrayList<>();

        film.setTitle("Captain Fantastic");
        castList.add(cast);
        film.setCasts(castList);
        assertTrue(filmDao.addFilm(film));

    }
    @Test
    public void addFilm_Fail() throws SQLException {

        film = new Film();
        cast = new CastDaoJdbc().addCast("",false);
        castList = new ArrayList<>();

        castList.add(cast);
        film.setCasts(castList);
        assertFalse(filmDao.addFilm(film));

    }

    @Test
    public void editFilm_Succeeded() throws Exception {
        film = new Film();
        cast = new CastDaoJdbc().addCast("Matt Ross",false);
        castList = new ArrayList<>();

        film.setTitle("Captain Fantastic");
        castList.add(cast);
        film.setCasts(castList);
        filmDao.addFilm(film);
        film.setHasOscar(true);
        assertTrue(filmDao.editFilm(film));


    }
    @Test
    public void editFilm_Fail() throws Exception {
        film = new Film();
        cast = new CastDaoJdbc().addCast("",false);
        castList = new ArrayList<>();

        castList.add(cast);
        film.setCasts(castList);
        filmDao.addFilm(film);
        assertFalse(filmDao.editFilm(film));
    }

    @Test
    public void getFilmById_Succeeded() throws Exception {
        film = new Film();
        cast = new CastDaoJdbc().addCast("Matt Ross",false);
        castList = new ArrayList<>();
        film.setTitle("Captain Fantastic");
        castList.add(cast);
        film.setCasts(castList);
        filmDao.addFilm(film);
        int id = film.getId();
        assertTrue(filmDao.getFilmById(id).equals(film) );

    }

    @Test
    public void getFilmById_Fail() throws Exception {
        /*
        film = new Film();
        cast= new CastDaoJdbc().addCast("Matt Ross",false);
        castList= new ArrayList<>();
        film.setTitle("Captain Fantastic");
        castList.add(cast);
        film.setCasts(castList);
        filmDao.addFilm(film);
        int id = film.getId();
        */
        //Film film = filmDao.getFilmById(-1);
       // assertNotEquals(0,film.getId());

    }

    @Test
    public void rateFilm_Succeeded() throws Exception {
        film = new Film();
        cast = new CastDaoJdbc().addCast("Matt Ross",false);
        castList = new ArrayList<>();
        film.setTitle("Captain Fantastic");
        castList.add(cast);
        film.setCasts(castList);
        filmDao.addFilm(film);

            Assert.assertTrue(filmDao.rateFilm(film.getId(), 5));

    }

    @Test
    public void getFilmsList_Succed(){
        Film film = new Film("Film 1",2000,"Director 1");
        filmDao.addFilm(film);
        film = new Film("Film 2",2000,"Director 2");
        filmDao.addFilm(film);
        film = new Film("Film 3",2000,"Director 3");
        filmDao.addFilm(film);

        Assert.assertEquals(filmDao.getFilmsList(0).size(),3);
    }

    @Test
    public void addGenreToFilm() throws Exception {
    }

}