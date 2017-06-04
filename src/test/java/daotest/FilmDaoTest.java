package daotest;

import am.aca.dao.DirectorDaoJdbc;
import am.aca.dao.FilmDao;
import am.aca.dao.FilmDaoJdbc;
import am.aca.entity.Director;
import am.aca.entity.Film;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Vardan on 04.06.2017.
 */
public class FilmDaoTest {
    FilmDao filmDao = new FilmDaoJdbc();
    Film film;
    Director director;
    List<Director> directorList;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        TestHelper.emptyTable(new String[]{"film_to_director","films","directors"});

    }

    @Test
    public void addFilm_Succeeded() throws SQLException {

        film = new Film();
        director= new DirectorDaoJdbc().addDirector("Matt Ross",false);
        directorList= new ArrayList<>();

        film.setTitle("Captain Fantastic");
        directorList.add(director);
        film.setDirectors(directorList);
        assertTrue(filmDao.addFilm(film));

    }
    @Test
    public void addFilm_Fail() throws SQLException {

        film = new Film();
        director= new DirectorDaoJdbc().addDirector("",false);
        directorList= new ArrayList<>();

        directorList.add(director);
        film.setDirectors(directorList);
        assertFalse(filmDao.addFilm(film));

    }

    @Test
    public void editFilm_Succeeded() throws Exception {
        film = new Film();
        director= new DirectorDaoJdbc().addDirector("Matt Ross",false);
        directorList= new ArrayList<>();

        film.setTitle("Captain Fantastic");
        directorList.add(director);
        film.setDirectors(directorList);
        filmDao.addFilm(film);
        film.setHasOscar(true);
        assertTrue(filmDao.editFilm(film));


    }
    @Test
    public void editFilm_Fail() throws Exception {
        film = new Film();
        director= new DirectorDaoJdbc().addDirector("",false);
        directorList= new ArrayList<>();

        directorList.add(director);
        film.setDirectors(directorList);
        filmDao.addFilm(film);
        assertFalse(filmDao.editFilm(film));
    }

    @Test
    public void getFilmById_Succeeded() throws Exception {
        film = new Film();
        director= new DirectorDaoJdbc().addDirector("Matt Ross",false);
        directorList= new ArrayList<>();
        film.setTitle("Captain Fantastic");
        directorList.add(director);
        film.setDirectors(directorList);
        filmDao.addFilm(film);
        int id = film.getId();
        assertTrue(filmDao.getFilmById(id).equals(film) );

    }

    @Test
    public void getFilmById_Fail() throws Exception {
        /*
        film = new Film();
        director= new DirectorDaoJdbc().addDirector("Matt Ross",false);
        directorList= new ArrayList<>();
        film.setTitle("Captain Fantastic");
        directorList.add(director);
        film.setDirectors(directorList);
        filmDao.addFilm(film);
        int id = film.getId();
        */
        //Film film = filmDao.getFilmById(-1);
       // assertNotEquals(0,film.getId());

    }

    @Test
    public void rateFilm() throws Exception {
    }

    @Test
    public void addGenreToFilm() throws Exception {
    }

}