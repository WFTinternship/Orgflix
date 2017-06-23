package service;

import am.aca.entity.Cast;
import am.aca.entity.Film;
import am.aca.entity.Genre;
import am.aca.service.CastService;
import am.aca.service.FilmService;
import am.aca.service.impl.CastServiceImpl;
import am.aca.service.impl.FilmServiceImpl;
import am.aca.util.TestHelper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by karine on 6/22/2017
 */
public class FilmTest {

    private ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext-persistance-test.xml");
    private FilmService filmService = ctx.getBean("filmServiceImpl", FilmServiceImpl.class);
    private CastService castService = ctx.getBean("castServiceImpl", CastServiceImpl.class);
    private TestHelper helper = ctx.getBean("testHelper", TestHelper.class);
    private Film film;
    private Cast cast;


    @Before
    public void setUp() {

    }

    @After
    public void revert() {
        helper.emptyTable(new String[]{"film_to_cast", "genre_to_film", "films", "casts"});
    }

    @Test
    public void addFilm_Success() {
        film = new Film("In Bruges", 2008);
        filmService.addFilm(film);
        Assert.assertTrue(film.getId() > 0);
    }

    @Test
    public void addFilmWithGenre_Success() {
        film = new Film("In Bruges", 2008);
        film.addGeners(Genre.ACTION);
        filmService.addFilm(film);
        Assert.assertEquals(film, filmService.getFilmsByGenre(Genre.ACTION).get(0));
    }

    @Test
    public void addFilmEmptyName_Fail() {
        film = new Film(null, 2066);
        Assert.assertFalse(filmService.addFilm(film));
    }

    @Test
    public void getFilmsList_Success() {
        film = new Film("In Bruges", 2008);
        filmService.addFilm(film);
        Assert.assertEquals(film, filmService.getFilmsList(0).get(0));
    }

    @Test
    public void getFilmsListWIndex_Success() {
        film = new Film("In Bruges", 2008);
        for (int i = 0; i < 5; i++) {
            filmService.addFilm(film);
        }
        Assert.assertEquals(5 - 2, filmService.getFilmsList(2).size());
    }

    @Test
    public void getFilmsList_Empty() {
        Assert.assertTrue(filmService.getFilmsList(0).isEmpty());
    }

    @Test
    public void getFilmsListBadIndex_Fail() {
        film = new Film("In Bruges", 2008);
        for (int i = 0; i < 5; i++) {
            filmService.addFilm(film);
        }
        Assert.assertTrue(filmService.getFilmsList(8).isEmpty());
    }

    @Test
    public void getFilmsListById_Success() {
        film = new Film("In Bruges", 2008);
        filmService.addFilm(film);
        film = new Film("London Boulevard", 2010);
        filmService.addFilm(film);
        film = new Film("The Lobster", 2015);
        filmService.addFilm(film);
        Assert.assertEquals(film.getId(), filmService.getFilmsList(0).get(2).getId());
    }

    @Test
    public void getFilmById_Success() {
        film = new Film("In Bruges", 2008);
        filmService.addFilm(film);
        Assert.assertEquals(film, filmService.getFilmById(film.getId()));
    }

    @Test
    public void getFilmById_Fail() {
        Assert.assertEquals(null, filmService.getFilmById(-1));
    }

    @Test
    public void getFilmsByCast_Succeeded() {
        cast = new Cast("Collin Farrell");
        castService.addCast(cast);

        film = new Film("In Bruges", 2008);
        filmService.addFilm(film);

        castService.addCastToFilm(cast, film);
        Assert.assertEquals(film, filmService.getFilmsByCast(cast).get(0));
    }


    @Test
    public void getFilmsByCast_Empty() {
        Assert.assertEquals(0, filmService.getFilmsByCast(0).size());
    }

    @Test
    public void getFilmsByGenreSize_Succeeded() {
        film = new Film("Fury", 2014);
        film.addGeners(Genre.WAR);
        filmService.addFilm(film);

        film = new Film("Schindler's List", 1993);
        film.addGeners(Genre.WAR);
        filmService.addFilm(film);
        Assert.assertEquals(2, filmService.getFilmsByGenre(Genre.WAR).size());
    }

    @Test
    public void getFilmsByGenre_Fail() {
        Assert.assertEquals(0, filmService.getFilmsByGenre(Genre.WAR).size());
    }

    @Test
    public void getRatingInt_Succeeded() {
        film = new Film("Fury", 2014);
        film.addGeners(Genre.WAR);
        film.setRate_4star(1);
        filmService.addFilm(film);
        Assert.assertEquals(4.0, filmService.getRating(film), 0.01);
    }

    @Test
    public void getRatingDouble_Succeeded() {
        film = new Film("Fury", 2014);
        film.addGeners(Genre.WAR);
        film.setRate_4star(1);
        film.setRate_5star(1);
        filmService.addFilm(film);
        Assert.assertEquals(4.5, filmService.getRating(film), 0.01);
    }

    @Test
    public void getRatingNaN_Failed() {
        film = new Film("Fury", 2014);
        film.addGeners(Genre.WAR);
        filmService.addFilm(film);
        Assert.assertEquals(0, filmService.getRating(film), 0.01);
    }

    @Test
    public void totalNumber_Succeeded() {
        film = new Film("In Bruges", 2008);
        for (int i = 0; i < 6; i++) {
            filmService.addFilm(film);
        }
        Assert.assertEquals(6, filmService.totalNumberOfFilms());
    }

    @Test
    public void totalNumber_Failed() {
        Assert.assertEquals(0, filmService.totalNumberOfFilms());
    }

    @Test
    public void editFilm_Succeeded() {
        film = new Film("The Departed", 2004);
        filmService.addFilm(film);
        film.setHasOscar(true);
        Assert.assertTrue(filmService.editFilm(film));
    }

    @Test
    public void editFilmSizeCheck_Succeeded() {
        film = new Film("The Departed", 2004);
        filmService.addFilm(film);
        film.setHasOscar(true);
        filmService.editFilm(film);
        Assert.assertEquals(1, filmService.totalNumberOfFilms());
    }

    @Test
    public void editFilmObjCheck_Success() {
        film = new Film("The Departed", 2004);
        film.setHasOscar(true);
        filmService.addFilm(film);

        cast = new Cast("Matt Damon", true);
        film.addCast(cast);
        filmService.editFilm(film);

        Assert.assertEquals(film, filmService.getFilmsByCast(cast).get(0));
    }

    @Test
    public void editFilm_Fail() {
        film = new Film();
        Assert.assertFalse(filmService.editFilm(film));
    }

    @Test
    public void rateFilm_Succeeded() {
        film = new Film("The Departed", 2004);
        filmService.addFilm(film);
        filmService.rateFilm(film.getId(), 5);
        filmService.rateFilm(film.getId(), 5);
        Assert.assertEquals(2, filmService.getFilmById(film.getId()).getRate_5star());
    }

    @Test
    public void rateFilm_Fail() {
        film = new Film("The Departed", 2004);
        filmService.addFilm(film);
        Assert.assertFalse(filmService.rateFilm(film.getId(), 9));
    }

    @Test
    public void addGenreToFilmBySize_Succeeded() {
        film = new Film("La La Land", 2016);
        filmService.addFilm(film);
        filmService.addGenreToFilm(Genre.MUSICAL, film);
        Assert.assertEquals(film, filmService.getFilmsByGenre(Genre.MUSICAL).get(0));
    }

    @Test
    public void addGenreToFilmByIdByFilm_Succeeded() {
        film = new Film("La La Land", 2016);
        filmService.addFilm(film);
        filmService.addGenreToFilm(Genre.MUSICAL, film.getId());
        Assert.assertEquals(film, filmService.getFilmsByGenre(Genre.MUSICAL).get(0));
    }
}
