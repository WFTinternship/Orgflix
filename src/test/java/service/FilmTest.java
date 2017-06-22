package service;

import am.aca.dao.DaoException;
import am.aca.entity.Cast;
import am.aca.entity.Film;
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
        helper.emptyTable(new String[] {"film_to_cast", "genre_to_film", "films", "casts"});
    }

    @Test
    public void addFilm_Success() {
        film = new Film("In Bruges", 2008);
        filmService.addFilm(film);
        Assert.assertTrue(film.getId() > 0);
    }

    @Test
    public void addFilmEmptyName_Fail() {
        film = new Film(null, 2066);
        Assert.assertFalse(filmService.addFilm(film));
    }

    @Test
    public void getFilmsListBySize_Success() {
        film = new Film("In Bruges", 2008);
        filmService.addFilm(film);
        film = new Film("London Boulevard", 2010);
        filmService.addFilm(film);
        film = new Film("The Lobster", 2015);
        filmService.addFilm(film);
        Assert.assertEquals(3, filmService.getFilmsList(0).size());
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
}
