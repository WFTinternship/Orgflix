package am.aca.orgflix.service.integration;

import am.aca.orgflix.BaseIntegrationTest;
import am.aca.orgflix.entity.Cast;
import am.aca.orgflix.entity.Film;
import am.aca.orgflix.entity.Genre;
import am.aca.orgflix.service.FilmService;
import am.aca.orgflix.service.ServiceException;
import am.aca.orgflix.util.TestHelper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Integration tests for Film Service Layer
 */
public class FilmServiceIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private FilmService filmService;

    @Autowired
    private TestHelper helper;

    private Film film;
    private Cast cast;

    /**
     * Rolls back all changes applied to the test DB resulted from tests
     */
    @After
    public void tearDown() {
        helper.emptyTable(new String[]{"film_to_cast", "film_to_genre", "films", "casts"});
    }

    @Test
    public void addFilm_Success() {
        film = new Film("In Bruges", 2008);

        boolean status = filmService.addFilm(film);
        Assert.assertTrue(status);

        status = film.getId() > 0;
        Assert.assertTrue(status);
    }

    @Test
    public void addFilm_WithRelations_Success() {
        film = new Film("In Bruges", 2008);
        film.addGeners(Genre.ACTION);

        boolean status = filmService.addFilm(film);
        Assert.assertTrue(status);
    }

    @Test(expected = ServiceException.class)
    public void addFilm_NullName_Fail() {
        film = new Film(null, 2000);
        filmService.addFilm(film);
    }

    @Test(expected = ServiceException.class)
    public void addFilm_EmptyName_Fail() {
        film = new Film("", 2000);
        filmService.addFilm(film);
    }

    @Test(expected = ServiceException.class)
    public void addFilm_BadYear_Fail() {
        film = new Film("Some Imaginary Thing", 2066);
        filmService.addFilm(film);
    }

    @Test
    public void getFilmsList_Success() {
        film = new Film("In Bruges", 2008);
        filmService.addFilm(film);
        film = new Film("London Boulevard", 2010);
        filmService.addFilm(film);
        film = new Film("The Lobster", 2015);
        filmService.addFilm(film);
        film.setGenres(new ArrayList<>());

        java.util.List<Film> actualFilms = filmService.getFilmsList(0);
        Assert.assertEquals(film, actualFilms.get(2));

        List<Film> badIndexList = filmService.getFilmsList(8);
        Assert.assertTrue(badIndexList.isEmpty());
    }

    @Test
    public void getFilmById_Success() {
        film = new Film("In Bruges", 2008);
        filmService.addFilm(film);

        Film actualFilm = filmService.getFilmById(film.getId());
        Assert.assertEquals(film, actualFilm);
    }

    @Test
    public void getFilmById_Fail() {
        Film actualFilm = filmService.getFilmById(-1);

        Assert.assertNull(actualFilm);
    }

    @Test
    public void getFilmsByCast_Success() {
        cast = new Cast("Collin Farrell");

        film = new Film("In Bruges", 2008);
        film.addCast(cast);
        filmService.addFilm(film);

        Film actualFilm = filmService.getFilmsByCast(cast.getId()).get(0);
        Assert.assertEquals(film, actualFilm);
    }

    @Test
    public void getFilmsByCast_Empty() {
        List<Film> films = filmService.getFilmsByCast(0);
        Assert.assertTrue(films.isEmpty());
    }

    @Test
    public void getRating_Success() {
        film = new Film("Fury", 2014);
        film.setRate_4star(1);
        filmService.addFilm(film);

        double rating = filmService.getRating(film.getId());
        Assert.assertEquals(4.0, rating, 0.01);
    }

    @Test
    public void getRating_NaN_Success() {
        film = new Film("Fury", 2014);
        filmService.addFilm(film);

        double rating = filmService.getRating(film.getId());
        Assert.assertEquals(0, rating, 0.01);
    }

    @Test
    public void getRating_InvalidFilm_Fail() {
        double rating = filmService.getRating(0);
        Assert.assertEquals(0, rating, 0.01);
    }

    @Test
    public void totalNumber_Success() {
        film = new Film("In Bruges", 2008);
        filmService.addFilm(film);

        int size = filmService.totalNumberOfFilms();
        Assert.assertEquals(1, size);
    }

    @Test
    public void totalNumber_Empty() {
        int size = filmService.totalNumberOfFilms();
        Assert.assertEquals(0, size);
    }

    @Test
    public void editFilm_Success() {
        film = new Film("The Departed", 2004);
        filmService.addFilm(film);
        film.setHasOscar(true);

        boolean status = filmService.editFilm(film);
        int size = filmService.totalNumberOfFilms();
        Film actualFilm = filmService.getFilmById(film.getId());

        Assert.assertTrue(status);
        Assert.assertEquals(1, size);
        Assert.assertTrue(film.isHasOscar());
        Assert.assertEquals(film, actualFilm);
    }

    @Test(expected = ServiceException.class)
    public void editFilm_EmptyName_Fail() {
        film = new Film("The Departed", 2004);
        film.setHasOscar(true);
        filmService.addFilm(film);

        film.setTitle("");

        filmService.editFilm(film);
    }

    @Test(expected = ServiceException.class)
    public void editFilm_NullName_Fail() {
        film = new Film("The Departed", 2004);
        film.setHasOscar(true);
        filmService.addFilm(film);

        film.setTitle(null);

        filmService.editFilm(film);
    }

    @Test(expected = ServiceException.class)
    public void editFilm_BadYear_Fail() {
        film = new Film("The Departed", 2004);
        film.setHasOscar(true);
        filmService.addFilm(film);

        film.setProdYear(80);

        filmService.editFilm(film);
    }

    @Test
    public void rateFilm_Success() {
        film = new Film("The Departed", 2004);
        filmService.addFilm(film);

        boolean status = filmService.rateFilm(film.getId(), 5);
        Assert.assertTrue(status);

        status = filmService.rateFilm(film.getId(), 5);
        Assert.assertTrue(status);

        int size = filmService.getFilmById(film.getId()).getRate_5star();
        Assert.assertEquals(2, size);
    }

    @Test
    public void rateFilm_Fail() {
        film = new Film("The Departed", 2004);
        filmService.addFilm(film);

        boolean status = filmService.rateFilm(film.getId(), 10);
        Assert.assertFalse(status);
    }

    @Test
    public void searchTitle_Success() {
        film = new Film();
        film.setTitle("Trainspotting");
        film.setProdYear(1996);
        cast = new Cast("Ewan McGregor");
        film.addCast(cast);
        filmService.addFilm(film);

        film = new Film();
        film.setTitle("The Girl on the Train");
        film.setProdYear(2016);
        film.setDirector("Tate Taylor");
        cast = new Cast("Emily Blunt");
        film.addCast(cast);
        film.addGeners(Genre.DRAMA);
        filmService.addFilm(film);

        List<Film> trainspottingResultList = filmService.getFilteredFilms
                ("Trainspotting", 0, 0, false, null, 0, 0);
        Assert.assertEquals(1, trainspottingResultList.size());
    }
}
