package daotest;

import am.aca.dao.FilmDao;
import am.aca.dao.impljdbc.DirectorDaoJdbc;
import am.aca.dao.impljdbc.FilmDaoJdbc;
import am.aca.entity.Director;
import am.aca.entity.Film;
import am.aca.entity.Genre;
import org.junit.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Vardan on 07.06.2017
 */
public class FilmTest {

    private FilmDao filmDao = new FilmDaoJdbc();
    private Film film;
    private Director director;
    private List<Director> directorList;

    @Before
    public void setUp() {
        film = new Film();
    }

    @After
    public void tearDown() {

        TestHelper.emptyTable(new String[]{"film_to_director", "genre_to_film", "films", "directors", });
    }

    @Test
    public void addFilm_Fail() {

        director = new DirectorDaoJdbc().addDirector("", false);
        directorList = new ArrayList<>();

        directorList.add(director);
        film.setDirectors(directorList);

        assertFalse(filmDao.addFilm(film));

    }

    @Test
    public void editFilm_Succeeded() {

        director = new DirectorDaoJdbc().addDirector("Matt Ross", false);
        directorList = new ArrayList<>();

        film.setTitle("Captain Fantastic");
        directorList.add(director);
        film.setDirectors(directorList);
        filmDao.addFilm(film);
        film.setHasOscar(true);

        assertTrue(filmDao.editFilm(film));

    }

    @Test
    public void editFilm_Fail() {

        director = new DirectorDaoJdbc().addDirector("", false);
        directorList = new ArrayList<>();

        directorList.add(director);
        film.setDirectors(directorList);
        filmDao.addFilm(film);

        assertFalse(filmDao.editFilm(film));
    }

    @Test
    public void getFilmById_Succeeded() {

        director = new DirectorDaoJdbc().addDirector("Matt Ross", false);
        directorList = new ArrayList<>();

        film.setTitle("Captain Fantastic");
        directorList.add(director);
        film.setDirectors(directorList);
        filmDao.addFilm(film);
        int id = film.getId();

        assertTrue(filmDao.getFilmById(id).equals(film));

    }

    @Test
    public void getFilmsByDirector_byId() {

        director = new DirectorDaoJdbc().addDirector("Matt Ross", false);
        directorList = new ArrayList<>();
        film.setTitle("Captain Fantastic");
        Film film1 = new Film();
        film1.setTitle("American Psycho");
        directorList.add(director);
        film.setDirectors(directorList);
        film1.setDirectors(directorList);
        filmDao.addFilm(film1);
        filmDao.addFilm(film);
        assertEquals(filmDao.getFilmsByDirector(film.getId()),
                filmDao.getFilmsByDirector(film1.getId()));


    }

    @Test
    public void getFilmsByDirector_byDirector() {

        director = new DirectorDaoJdbc().addDirector("Matt Ross", false);
        directorList = new ArrayList<>();
        film.setTitle("Captain Fantastic");
        Film film1 = new Film();
        film1.setTitle("American Psycho");
        directorList.add(director);
        film.setDirectors(directorList);
        film1.setDirectors(directorList);
        filmDao.addFilm(film1);
        filmDao.addFilm(film);
        assertEquals(filmDao.getFilmsByDirector(film1.getDirectors().get(0)),
                filmDao.getFilmsByDirector(film.getDirectors().get(0)));
    }

    @Test
    public void rateFilm() {

        director = new DirectorDaoJdbc().addDirector("Matt Ross", false);
        directorList = new ArrayList<>();
        film.setTitle("Captain Fantastic");
        directorList.add(director);
        film.setDirectors(directorList);
        //film.setRate_5star(1);
        filmDao.addFilm(film);

        Assert.assertTrue(filmDao.rateFilm(film.getId(), 5));
    }

    @Test
    public void addGenreToFilm_ByGenreAndFilmId() {

        director = new DirectorDaoJdbc().addDirector("Matt Ross", false);
        film.setTitle("Captain Fantastic");
        film.addDirector(director);
        film.addGeners(Genre.DRAMA);
        filmDao.addFilm(film);
        filmDao.addGenreToFilm(Genre.COMEDY, film.getId());
        assertTrue(filmDao.addGenreToFilm(film.getGeners().get(0), film.getId()));

    }

    @Test
    public void addGenreToFilm_ByGenreAndFilm() {

        director = new DirectorDaoJdbc().addDirector("Matt Ross", false);
        film.setTitle("Captain Fantastic");
        film.addDirector(director);
        film.addGeners(Genre.DRAMA);
        filmDao.addFilm(film);

        assertTrue(filmDao.addGenreToFilm(film.getGeners().get(0), film));
    }

}