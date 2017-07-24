package am.aca.orgflix.service.integration;

import am.aca.orgflix.BaseIntegrationTest;
import am.aca.orgflix.entity.Cast;
import am.aca.orgflix.entity.Film;
import am.aca.orgflix.entity.Genre;
import am.aca.orgflix.exception.ServiceException;
import am.aca.orgflix.service.CastService;
import am.aca.orgflix.service.FilmService;
import am.aca.orgflix.util.TestHelper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Integration tests for Film Service Layer
 */
public class FilmServiceIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private FilmService filmService;

    @Autowired
    private CastService castService;

    @Autowired
    private TestHelper helper;

    private Film film;
    private Cast cast;

    /**
     * Rolls back all changes applied to the test DB resulted from tests
     */
    @After
    public void tearDown() {
        helper.emptyTable(new String[]{"FILM_TO_CAST", "FILM_TO_GENRE", "FILMS", "CASTS"});
    }

    /**
     * @see FilmService#add(Film)
     */
    @Test
    public void addFilm_ValidInput_Success() {
        film = new Film("In Bruges", 2008);

        boolean status = filmService.add(film);
        Assert.assertTrue(status);

        status = film.getId() > 0;
        Assert.assertTrue(status);

        film.addGeners(Genre.ACTION);

        status = filmService.add(film);
        Assert.assertTrue(status);

        Film actualFilm = filmService.getByGenre(Genre.ACTION).get(0);
        Assert.assertEquals(film, actualFilm);

        cast = new Cast("Ralph Fiennes");
        film.addCast(cast);
        castService.add(cast);
        status = filmService.add(film);
        Assert.assertTrue(status);

        actualFilm = filmService.getByCast(cast.getId()).get(0);
        Assert.assertEquals(film, actualFilm);
    }

    /**
     * @see FilmService#add(Film)
     */
    @Test
    public void addFilm_NameNull_Fail() {
        film = new Film(null, 2008);

        try {
            filmService.add(film);
        } catch (ServiceException e) {
            Assert.assertTrue(e instanceof ServiceException);
        }
    }

    /**
     * @see FilmService#add(Film)
     */
    @Test
    public void addFilm_NameEmpty_Fail() {
        film = new Film(null, 2066);
        try {
            filmService.add(film);
        } catch (ServiceException e) {
            Assert.assertTrue(e instanceof ServiceException);
        }
    }

    /**
     * @see FilmService#add(Film)
     */
    @Test
    public void addFilm_InvalidYear() {
        film = new Film("In Bruges", 456);
        try {
            filmService.add(film);
        } catch (ServiceException e) {
            Assert.assertTrue(e instanceof ServiceException);
        }
    }

    /**
     * @see FilmService#getById(int)
     */
    @Test
    public void getFilmById_ValidId_Success() {
        film = new Film("In Bruges", 2008);
        filmService.add(film);

        int id = film.getId();

        Film actualFilm = filmService.getById(id);
        Assert.assertEquals(film, actualFilm);

        film.setTitle("Vicky Christina Barcelona");
        filmService.add(film);

        actualFilm = filmService.getById(id);
        Assert.assertEquals("In Bruges", actualFilm.getTitle());

        actualFilm = filmService.getById(film.getId());
        Assert.assertEquals(film, actualFilm);
    }

    /**
     * @see FilmService#getById(int)
     */
    @Test
    public void getFilmById_InvalidId_Fail() {
        Film actualFilm = filmService.getById(-1);

        Assert.assertEquals(null, actualFilm);
    }

    /**
     * @see FilmService#getAll(int, int)
     */
    @Test
    public void getFilmsList_ValidPagination_Success() {
        Film film = new Film("Forrest Gump", 1990);
        filmService.add(film);

        List<Film> actualFilms = filmService.getAll(0, 12);
        Assert.assertEquals(1, actualFilms.size());
        Assert.assertEquals(film, actualFilms.get(0));

        film = new Film("In Bruges", 2008);
        for (int i = 0; i < 19; i++) {
            filmService.add(film);
        }

        actualFilms = filmService.getAll(0, 12);
        Assert.assertEquals(12, actualFilms.size());

        actualFilms = filmService.getAll(12, 12);
        Assert.assertEquals(20 - 12, actualFilms.size());

        actualFilms = filmService.getAll(24, 12);
        Assert.assertEquals(0, actualFilms.size());
    }

    /**
     * @see FilmService#getAll(int, int)
     */
    @Test
    public void getFilmsList_Empty() {
        List<Film> actualFilms = filmService.getAll(0, 12);
        Assert.assertTrue(actualFilms.isEmpty());
    }

    /**
     * @see FilmService#getByCast(int)
     */
    @Test
    public void getFilmsByCast_Success() {
        cast = new Cast("Collin Farrell");
        castService.add(cast);

        film = new Film("In Bruges", 2008);
        filmService.add(film);

        castService.addToFilm(cast, film.getId());

        List<Film> actualFilms = filmService.getByCast(cast.getId());
        Assert.assertEquals(film, actualFilms.get(0));
    }

    /**
     * @see FilmService#getByCast(int)
     */
    @Test
    public void getFilmsByCast_Empty() {
        int size = filmService.getByCast(0).size();
        Assert.assertEquals(0, size);
    }

    /**
     * @see FilmService#getByGenre(Genre)
     */
    @Test
    public void getFilmsByGenre_ValidGenre_Success() {
        film = new Film("Fury", 2014);
        film.addGeners(Genre.WAR);
        filmService.add(film);

        film = new Film("Schindler's List", 1993);
        film.addGeners(Genre.WAR);
        filmService.add(film);

        film = new Film("LION", 2016);
        film.addGeners(Genre.BIOGRAPHY);
        filmService.add(film);

        List<Film> actualFilms = filmService.getByGenre(Genre.WAR);
        Assert.assertEquals(2, actualFilms.size());
    }

    /**
     * @see FilmService#getByGenre(Genre)
     */
    @Test
    public void getFilmsByGenre_Empty() {
        List<Film> actualFilms = filmService.getByGenre(Genre.WAR);
        Assert.assertTrue(actualFilms.isEmpty());
    }

    /**
     * @see FilmService#getRating(int)
     */
    @Test
    public void getRating_ValidFilm_Success() {
        film = new Film("Fury", 2014);
        film.addGeners(Genre.WAR);
        film.setRate_4star(1);
        filmService.add(film);

        double rating = filmService.getRating(film.getId());
        Assert.assertEquals(4.0, rating, 0.01);

        filmService.rate(film.getId(), 5);

        rating = filmService.getRating(film.getId());
        Assert.assertEquals(4.5, rating, 0.01);

        filmService.rate(film.getId(), 5);
        filmService.rate(film.getId(), 5);
        filmService.rate(film.getId(), 5);
        filmService.rate(film.getId(), 5);

        rating = filmService.getRating(film.getId());
        Assert.assertEquals(4.8, rating, 0.1);
    }

    /**
     * @see FilmService#getRating(int)
     */
    @Test
    public void getRating_NaN_Success() {
        film = new Film("Fury", 2014);
        film.addGeners(Genre.WAR);
        filmService.add(film);

        double rating = filmService.getRating(film.getId());
        Assert.assertEquals(0, rating, 0.01);
    }

    /**
     * @see FilmService#getAllRatings(int, int)
     */
    @Test
    public void getAllRatings_CorrectSize_Success() {
        Film film1 = new Film("Film 1", 1990);
        Film film2 = new Film("Film 2", 1990);
        filmService.add(film1);
        filmService.add(film2);
        Assert.assertEquals(2, filmService.getAllRatings(0, 12).length);
    }

    /**
     * @see FilmService#getAllRatings(int, int)
     */
    @Test
    public void getAllRatings_CorrectAverages_Success() {
        Film film1 = new Film("Film 1", 1990);
        Film film2 = new Film("Film 2", 1990);
        filmService.add(film1);
        filmService.rate(film1.getId(), 3);
        filmService.rate(film1.getId(), 4);
        filmService.add(film2);
        filmService.rate(film2.getId(), 5);
        filmService.rate(film2.getId(), 1);
        Assert.assertEquals("3.5", filmService.getAllRatings(0, 12)[0]);
        Assert.assertEquals("3.0", filmService.getAllRatings(0, 12)[1]);
    }

    /**
     * @see FilmService#getTotalNumber()
     */
    @Test
    public void totalNumber_Success() {
        film = new Film("In Bruges", 2008);
        for (int i = 0; i < 6; i++) {
            filmService.add(film);
        }

        int size = filmService.getTotalNumber();
        Assert.assertEquals(6, size);
    }

    /**
     * @see FilmService#getTotalNumber()
     */
    @Test
    public void totalNumber_Empty() {
        int size = filmService.getTotalNumber();
        Assert.assertEquals(0, size);
    }

    @Test
    public void editFilm_Success() {
        film = new Film("The Departed", 2004);
        filmService.add(film);
        film.setHasOscar(true);

        boolean status = filmService.edit(film);
        Assert.assertTrue(status);

        int size = filmService.getTotalNumber();
        Assert.assertEquals(1, size);
    }

    /**
     * @see FilmService#edit(Film)
     */
    @Test
    public void editFilm_ValidFilm_Success() {
        film = new Film("The Departed", 2004);
        filmService.add(film);
        film.setHasOscar(true);
        filmService.edit(film);

        int size = filmService.getTotalNumber();
        Assert.assertEquals(1, size);

        Film actualFilm = filmService.getAll(0, 12).get(0);
        Assert.assertEquals(film, actualFilm);
    }

    /**
     * @see FilmService#edit(Film)
     */
    @Test
    public void editFilm_InvalidFilm_Success() {
        film = new Film("The Departed", 2004);
        film.setHasOscar(true);

        boolean status = filmService.edit(film);
        Assert.assertFalse(status);
    }

    /**
     * @see FilmService#edit(Film)
     */
    @Test
    public void editFilm_NameNull_Fail() {
        film = new Film("The Departed", 2004);
        try {
            filmService.add(film);
            film.setTitle(null);
            filmService.edit(film);
        } catch (ServiceException e) {
            Assert.assertTrue(e instanceof ServiceException);
        }

    }

    /**
     * @see FilmService#edit(Film)
     */
    @Test
    public void editFilm_NameEmpty_Fail() {
        film = new Film("The Departed", 2004);
        try {
            filmService.add(film);
            film.setTitle("");
            filmService.edit(film);
        } catch (ServiceException e) {
            Assert.assertTrue(e instanceof ServiceException);
        }

    }

    /**
     * @see FilmService#edit(Film)
     */
    @Test
    public void editFilm_InvalidYear_Fail() {
        film = new Film("The Departed", 2004);
        try {
            filmService.add(film);
            film.setProdYear(-85);
            filmService.edit(film);
        } catch (ServiceException e) {
            Assert.assertTrue(e instanceof ServiceException);
        }

    }

    /**
     * @see FilmService#rate(int, int)
     */
    @Test
    public void rateFilm_ValidRating_Success() {
        film = new Film("The Departed", 2004);
        filmService.add(film);

        boolean status = filmService.rate(film.getId(), 5);
        Assert.assertTrue(status);

        status = filmService.rate(film.getId(), 5);
        Assert.assertTrue(status);

        int size = filmService.getById(film.getId()).getRate_5star();
        Assert.assertEquals(2, size);

        status = filmService.rate(0, 5);
        Assert.assertFalse(status);
    }

    /**
     * @see FilmService#rate(int, int)
     */
    @Test
    public void rateFilm_InvalidRating_Fail() {
        film = new Film("The Departed", 2004);
        try {
            filmService.add(film);
            boolean status = filmService.rate(film.getId(), 9);
            Assert.assertFalse(status);
        } catch (ServiceException e) {
            Assert.assertTrue(e instanceof ServiceException);
        }

    }

    /**
     * @see FilmService#getFilteredFilms(String, int, int, boolean, String, int, int)
     */
    @Test
    public void filterFilms_ValidInput_Success() {
        film = new Film();
        film.setTitle("The Lost City of Z");
        film.setProdYear(2016);
        film.setDirector("James Gray");
        film.addGeners(Genre.ADVENTURE);
        film.addGeners(Genre.BIOGRAPHY);
        filmService.add(film);
        cast = new Cast("Charlie Hunnam");
        castService.add(cast);
        castService.addToFilm(cast, film.getId());

        film = new Film();
        film.setTitle("City of God");
        film.setProdYear(2002);
        film.setDirector("Fernando Meirelles");
        film.addGeners(Genre.DRAMA);
        filmService.add(film);

        film = new Film();
        film.setTitle("7");
        film.setProdYear(1995);
        film.setDirector("David Fincher");
        film.addGeners(Genre.DRAMA);
        film.addGeners(Genre.CRIME);
        film.addGeners(Genre.THRILLER);
        filmService.add(film);
        cast = new Cast("Brad Pitt");
        castService.add(cast);
        int id = cast.getId();
        castService.addToFilm(cast, film.getId());

        film = new Film();
        film.setTitle("Fight Club");
        film.setProdYear(1997);
        film.setDirector("David Fincher");
        film.addGeners(Genre.THRILLER);
        film.addGeners(Genre.MYSTERY);
        filmService.add(film);
        castService.addToFilm(cast, film.getId());

        film = new Film();
        film.setTitle("Crash");
        film.setProdYear(2004);
        film.setHasOscar(true);
        film.setDirector("Paul Haggis");
        film.addGeners(Genre.CRIME);
        film.addGeners(Genre.DRAMA);
        film.addGeners(Genre.THRILLER);
        filmService.add(film);
        cast = new Cast("Sandra Bullock", true);
        castService.add(cast);
        castService.addToFilm(cast, film.getId());

        film = new Film();
        film.setTitle("Gone Girl");
        film.setProdYear(2014);
        film.setDirector("David Fincher");
        film.addGeners(Genre.CRIME);
        film.addGeners(Genre.DRAMA);
        film.addGeners(Genre.MYSTERY);
        filmService.add(film);
        cast = new Cast("Rosamund Pike");
        castService.add(cast);
        castService.addToFilm(cast, film.getId());

        //filters by nothing
        List<Film> filteredFilms = filmService.getFilteredFilms("", 0, 0, false, "", 0, 0);
        Assert.assertEquals(6, filteredFilms.size());

        //filters by title only
        filteredFilms = filmService.getFilteredFilms("city of", 0, 0, false, "", 0, 0);
        Assert.assertEquals(2, filteredFilms.size());

        //filters by production year constraints only
        filteredFilms = filmService.getFilteredFilms("", 2000, 0, false, "", 0, 0);
        Assert.assertEquals(4, filteredFilms.size());

        //filters by oscar wins only
        filteredFilms = filmService.getFilteredFilms("", 0, 0, true, "", 0, 0);
        Assert.assertEquals(1, filteredFilms.size());

        //filters by director only
        filteredFilms = filmService.getFilteredFilms("", 0, 0, false, "david fincher", 0, 0);
        Assert.assertEquals(3, filteredFilms.size());

        //filters by cast only
        filteredFilms = filmService.getFilteredFilms("", 0, 0, false, "", id, 0);
        Assert.assertEquals(2, filteredFilms.size());

        //filters by genre only
        filteredFilms = filmService.getFilteredFilms("", 0, 0, false, "", 0, Genre.DRAMA.getValue());
        Assert.assertEquals(4, filteredFilms.size());

        //filters by everything
        filteredFilms = filmService.getFilteredFilms("gone girl", 2014, 2014, false, "david fincher", cast.getId(), Genre.MYSTERY.getValue());
        Assert.assertEquals(1, filteredFilms.size());
        Assert.assertEquals(film, filteredFilms.get(0));
    }

    /**
     * @see FilmService#getFilteredFilms(String, int, int, boolean, String, int, int)
     */
    @Test
    public void searchTitle_NotExisting_Empty() {
        film = new Film("In Bruges", 2008);
        filmService.add(film);

        boolean status = filmService.getFilteredFilms("", 3000, 3000, false, null, 0, 0).isEmpty();
        Assert.assertTrue(status);
    }
}
