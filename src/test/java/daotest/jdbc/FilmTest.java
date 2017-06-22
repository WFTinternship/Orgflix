package daotest.jdbc;

import am.aca.dao.jdbc.CastDAO;
import am.aca.dao.jdbc.impljdbc.JdbcCastDAO;
import am.aca.dao.jdbc.FilmDAO;
import am.aca.dao.jdbc.impljdbc.JdbcFilmDAO;
import am.aca.entity.Cast;
import am.aca.entity.Film;
import am.aca.entity.Genre;
import am.aca.util.TestHelper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Vardan on 04.06.2017
 */
public class FilmTest {
    private ApplicationContext ctx =
            new ClassPathXmlApplicationContext("applicationContext-persistance-test.xml");

    private FilmDAO filmDAO = ctx.getBean("jdbcFilmDAO", JdbcFilmDAO.class);
    private CastDAO castDAO = ctx.getBean("jdbcCastDAO", JdbcCastDAO.class);
    private TestHelper helper = ctx.getBean("testHelper", TestHelper.class);

    private Film film;
    private Cast cast;
    private List<Cast> castList;

    @Before
    public void setUp() {
        film = new Film();
    }

    @After
    public void tearDown() {
        helper.emptyTable(new String[]{"genre_to_film", "film_to_cast", "films", "casts"});
    }

    @Test
    public void addFilm_Succeeded() {
        film.setTitle("Captain Fantastic");
        filmDAO.addFilm(film);
        Assert.assertEquals(1, filmDAO.getFilmsList(0).size());
    }


    @Test
    public void addFilm_Fail() {
        assertFalse(filmDAO.addFilm(film));
    }

    @Test
    public void editFilmChangeCheck_Succeeded() {
        cast = new Cast("Matt Ross");
        castDAO.addCast(cast);
        castList = new ArrayList<>();

        film.setTitle("Captain Fantastic");
        castList.add(cast);
        film.setCasts(castList);
        film.setDirector("Matthew Ross");
        filmDAO.addFilm(film);
        film.setHasOscar(true);
        assertTrue(filmDAO.editFilm(film));


    }

    @Test
    public void editFilmSizeCheck_Succeeded() {
        cast = new Cast("Matt Ross", false);
        castDAO.addCast(cast);
        castList = new ArrayList<>();

        film.setTitle("Captain Fantastic");
        castList.add(cast);
        film.setCasts(castList);
        film.setDirector("Matthew Ross");
        filmDAO.addFilm(film);
        film.setDirector("Matt Ross");
        filmDAO.editFilm(film);
        Assert.assertEquals(1, filmDAO.totalNumberOfFilms());
    }


    @Test
    public void editFilm_Fail() {
        filmDAO.addFilm(film);
        assertFalse(filmDAO.editFilm(film));
    }

    @Test
    public void getFilmById_Succeeded() {
        film.setTitle("Captain Fantastic");
        filmDAO.addFilm(film);
        Assert.assertEquals(film, filmDAO.getFilmById(film.getId()));
    }

    @Test
    public void getFilmById_Failed() {
        Assert.assertEquals(null, filmDAO.getFilmById(-1));
    }

    @Test
    public void getFilmsByCast_Succeeded() {
        cast = new Cast("Matt Ross", false);
        castDAO.addCast(cast);
        castList = new ArrayList<>();
        film.setTitle("Captain Fantastic");
        castList.add(cast);
        film.setCasts(castList);
        filmDAO.addFilm(film);
        filmDAO.addCastToFilm(cast, film);
        Assert.assertEquals(film.getId(), filmDAO.getFilmsByCast(cast).get(0));
    }

    @Test
    public void getFilmsByCast_Failed() {
        Assert.assertEquals(0, filmDAO.getFilmsByCast(0).size());
    }


    @Test
    public void addGenreToFilmBySize_Succeeded() {
        film.setTitle("Captain Fantastic");
        filmDAO.addFilm(film);
        filmDAO.addGenreToFilm(Genre.COMEDY, film);
        Assert.assertEquals(1, filmDAO.getFilmsByGenre(Genre.COMEDY).size());
    }

    @Test
    public void addGenreToFilmByFilm_Succeeded() {
        film.setTitle("Captain Fantastic");
        filmDAO.addFilm(film);
        filmDAO.addGenreToFilm(Genre.COMEDY, film);
        Assert.assertEquals(film.getId(), filmDAO.getFilmsByGenre(Genre.COMEDY).get(0));
    }

    @Test
    public void rateFilm_Succeeded() {
        cast = new Cast("Matt Ross", false);
        castDAO.addCast(cast);
        castList = new ArrayList<>();
        film.setTitle("Captain Fantastic");
        castList.add(cast);
        film.setCasts(castList);
        filmDAO.addFilm(film);
        filmDAO.rateFilm(film.getId(), 5);
        filmDAO.rateFilm(film.getId(), 5);
        Assert.assertEquals(2, filmDAO.getFilmById(film.getId()).getRate_5star());
    }

    @Test
    public void getFilmsList_Succeeded() {
        film.setTitle("Captain Fantastic");
        for (int i = 0; i < 7; i++) {
            filmDAO.addFilm(film);
        }
        Assert.assertEquals(7, filmDAO.getFilmsList(0).size());
    }

    @Test
    public void getFilmsListPage2_Succeeded() {
        film.setTitle("Captain Fantastic");
        for (int i = 0; i < 20; i++) {
            filmDAO.addFilm(film);
        }
        Assert.assertEquals(20-12, filmDAO.getFilmsList(12).size());
    }

    @Test
    public void getFilmsList_EmptyList() {
        Assert.assertEquals(0, filmDAO.getFilmsList(0).size());
    }

    @Test
    public void totalNumber_Succeeded() {
        film.setTitle("Captain Fantastic");
        for (int i = 0; i < 6; i++) {
            filmDAO.addFilm(film);
        }
        Assert.assertEquals(6, filmDAO.totalNumberOfFilms());
    }
    @Test
    public void totalNumber_Failed() {
        Assert.assertEquals(0, filmDAO.totalNumberOfFilms());
    }

    @Test
    public void getFilmsListIndex_Failed() {
        film.setTitle("Captain Fantastic");
        filmDAO.addFilm(film);
        Assert.assertEquals(0, filmDAO.getFilmsList(1).size());
    }

    @Test
    public void getFilmsByGenreSize_Succeeded() {
        film.setTitle("Captain Fantastic");
        filmDAO.addFilm(film);
        filmDAO.addGenreToFilm(Genre.COMEDY, film);
        Assert.assertEquals(1, filmDAO.getFilmsByGenre(Genre.COMEDY).size());
    }

    @Test
    public void getFilmsByGenreFilm_Succeeded() {
        film.setTitle("Captain Fantastic");
        filmDAO.addFilm(film);
        filmDAO.addGenreToFilm(Genre.COMEDY, film);
        Assert.assertEquals(film.getId(), filmDAO.getFilmsByGenre(Genre.COMEDY).get(0));
    }


    @Test
    public void getFilmsByGenre_Fail() {
        Assert.assertEquals(0, filmDAO.getFilmsByGenre(Genre.WAR).size());
    }

    @Test
    public void getRatingInt_Succeeded() {
        film.setTitle("Captain Fantastic");
        filmDAO.addFilm(film);
        filmDAO.rateFilm(film.getId(), 4);
        filmDAO.rateFilm(film.getId(), 4);
        filmDAO.rateFilm(film.getId(), 3);
        filmDAO.rateFilm(film.getId(), 5);
        Assert.assertEquals(4.0, filmDAO.getRating(film), 0.01);
    }

    @Test
    public void getRatingDouble_Succeeded() {
        film.setTitle("Captain Fantastic");
        filmDAO.addFilm(film);
        filmDAO.rateFilm(film.getId(), 4);
        filmDAO.rateFilm(film.getId(), 5);
        Assert.assertEquals(4.5, filmDAO.getRating(film), 0.01);
    }

    @Test
    public void getRatingNaN_Failed() {
        film.setTitle("Captain Fantastic");
        filmDAO.addFilm(film);
        Assert.assertEquals(0, filmDAO.getRating(film), 0.01);
    }
}