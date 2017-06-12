package daotest;

import am.aca.dao.FilmDao;
import am.aca.dao.impljdbc.CastDaoJdbc;
import am.aca.dao.impljdbc.FilmDaoJdbc;
import am.aca.entity.Cast;
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
    private Cast cast;
    private List<Cast> castList;

    @Before
    public void setUp() {
        film = new Film();
    }

    @After
    public void tearDown() {

        TestHelper.emptyTable(new String[]{"film_to_cast","genre_to_film" ,"films", "casts"});
    }

    @Test
    public void addFilm_Fail() {

        cast = new CastDaoJdbc().addCast("", false);
        castList = new ArrayList<>();

        castList.add(cast);
        film.setCasts(castList);

        assertFalse(filmDao.addFilm(film));

    }

    @Test
    public void editFilm_Succeeded() {

        cast = new CastDaoJdbc().addCast("Matt Ross", false);
        castList = new ArrayList<>();

        film.setTitle("Captain Fantastic");
        castList.add(cast);
        film.setCasts(castList);
        filmDao.addFilm(film);
        film.setHasOscar(true);

        assertTrue(filmDao.editFilm(film));

    }

    @Test
    public void editFilm_Fail() {

        cast = new CastDaoJdbc().addCast("", false);
        castList = new ArrayList<>();

        castList.add(cast);
        film.setCasts(castList);
        filmDao.addFilm(film);

        assertFalse(filmDao.editFilm(film));
    }

    @Test
    public void getFilmById_Succeeded() {

        cast = new CastDaoJdbc().addCast("Matt Ross", false);
        castList = new ArrayList<>();

        film.setTitle("Captain Fantastic");
        castList.add(cast);
        film.setCasts(castList);
        filmDao.addFilm(film);
        int id = film.getId();

        assertTrue(filmDao.getFilmById(id).equals(film));

    }

    @Test
    public void getFilmsByDirector_byId() {

        cast = new CastDaoJdbc().addCast("Matt Ross", false);
        castList = new ArrayList<>();
        film.setTitle("Captain Fantastic");
        Film film1 = new Film();
        film1.setTitle("American Psycho");
        castList.add(cast);
        film.setCasts(castList);
        film1.setCasts(castList);
        filmDao.addFilm(film1);
        filmDao.addFilm(film);
        assertEquals(filmDao.getFilmsByCast(film.getId()),
                filmDao.getFilmsByCast(film1.getId()));


    }

    @Test
    public void getFilmsByDirector_byDirector() {

        cast = new CastDaoJdbc().addCast("Matt Ross", false);
        castList = new ArrayList<>();
        film.setTitle("Captain Fantastic");
        Film film1 = new Film();
        film1.setTitle("American Psycho");
        castList.add(cast);
        film.setCasts(castList);
        film1.setCasts(castList);
        filmDao.addFilm(film1);
        filmDao.addFilm(film);
        assertEquals(filmDao.getFilmsByCast(film1.getCasts().get(0)),
                filmDao.getFilmsByCast(film.getCasts().get(0)));
    }

    @Test
    public void rateFilm() {

        cast = new CastDaoJdbc().addCast("Matt Ross", false);
        castList = new ArrayList<>();
        film.setTitle("Captain Fantastic");
        castList.add(cast);
        film.setCasts(castList);
        filmDao.addFilm(film);

        Assert.assertTrue(filmDao.rateFilm(film.getId(), 5));
    }

    @Test
    public void addGenreToFilm_ByGenreAndFilmId() {

        film.setTitle("Captain Fantastic");
        filmDao.addFilm(film);
        assertTrue(filmDao.addGenreToFilm(Genre.DRAMA, film.getId()));

    }

    @Test
    public void addGenreToFilm_ByGenreAndFilm() {

        film.setTitle("Captain Fantastic");
        filmDao.addFilm(film);

        assertTrue(filmDao.addGenreToFilm(Genre.DRAMA, film));
    }

}