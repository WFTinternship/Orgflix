package am.aca.orgflix.service.integration;

import am.aca.orgflix.BaseIntegrationTest;
import am.aca.orgflix.entity.Cast;
import am.aca.orgflix.entity.Film;
import am.aca.orgflix.entity.Genre;
import am.aca.orgflix.service.CastService;
import am.aca.orgflix.service.FilmService;
import am.aca.orgflix.service.ServiceException;
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
     * @see FilmService#addFilm(Film)
     */
    @Test
    public void addFilm_ValidInput_Success() {
        film = new Film("In Bruges", 2008);

        boolean status = filmService.addFilm(film);
        Assert.assertTrue(status);

        status = film.getId() > 0;
        Assert.assertTrue(status);

        film.addGeners(Genre.ACTION);

        status = filmService.addFilm(film);
        Assert.assertTrue(status);

        Film actualFilm = filmService.getFilmsByGenre(Genre.ACTION).get(0);
        Assert.assertEquals(film, actualFilm);

        cast = new Cast("Ralph Fiennes");
        film.addCast(cast);
        castService.addCast(cast);
        status = filmService.addFilm(film);
        Assert.assertTrue(status);

        actualFilm = filmService.getFilmsByCast(cast.getId()).get(0);
        Assert.assertEquals(film, actualFilm);
    }

    /**
     * @see FilmService#addFilm(Film)
     */
    @Test(expected = ServiceException.class)
    public void addFilm_NameNull_Fail() {
        film = new Film(null, 2008);

        filmService.addFilm(film);
    }

    /**
     * @see FilmService#addFilm(Film)
     */
    @Test(expected = ServiceException.class)
    public void addFilm_NameEmpty_Fail() {
        film = new Film(null, 2066);
        filmService.addFilm(film);
    }

    /**
     * @see FilmService#addFilm(Film)
     */
    @Test (expected = ServiceException.class)
    public void addFilm_InvalidYear() {
        film = new Film("In Bruges", 456);
        filmService.addFilm(film);
    }

    /**
     * @see FilmService#getFilmById(int)
     */
    @Test
    public void getFilmById_ValidId_Success() {
        film = new Film("In Bruges", 2008);
        filmService.addFilm(film);

        int id = film.getId();

        Film actualFilm = filmService.getFilmById(id);
        Assert.assertEquals(film, actualFilm);

        film.setTitle("Vicky Christina Barcelona");
        filmService.addFilm(film);

        actualFilm = filmService.getFilmById(id);
        Assert.assertEquals("In Bruges", actualFilm.getTitle());

        actualFilm = filmService.getFilmById(film.getId());
        Assert.assertEquals(film, actualFilm);
    }

    /**
     * @see FilmService#getFilmById(int)
     */
    @Test
    public void getFilmById_InvalidId_Fail() {
        Film actualFilm = filmService.getFilmById(-1);

        Assert.assertEquals(null, actualFilm);
    }

    /**
     * @see FilmService#getFilmsList(int, int)
     */
    @Test
    public void getFilmsList_ValidPagination_Success() {
        Film film = new Film("Forrest Gump", 1990);
        filmService.addFilm(film);

        List<Film> actualFilms = filmService.getFilmsList(0, 12);
        Assert.assertEquals(1, actualFilms.size());
        Assert.assertEquals(film, actualFilms.get(0));

        film = new Film("In Bruges", 2008);
        for (int i = 0; i < 19; i++) {
            filmService.addFilm(film);
        }

        actualFilms = filmService.getFilmsList(0, 12);
        Assert.assertEquals(12, actualFilms.size());

        actualFilms = filmService.getFilmsList(12, 12);
        Assert.assertEquals(20 - 12, actualFilms.size());

        actualFilms = filmService.getFilmsList(24, 12);
        Assert.assertEquals(0, actualFilms.size());
    }

    /**
     * @see FilmService#getFilmsList(int, int)
     */
    @Test
    public void getFilmsList_Empty() {
        List<Film> actualFilms = filmService.getFilmsList(0, 12);
        Assert.assertTrue(actualFilms.isEmpty());
    }

    /**
     * @see FilmService#getFilmsByCast(int)
     */
    @Test
    public void getFilmsByCast_Success() {
        cast = new Cast("Collin Farrell");
        castService.addCast(cast);

        film = new Film("In Bruges", 2008);
        filmService.addFilm(film);

        castService.addCastToFilm(cast, film.getId());

        List<Film> actualFilms = filmService.getFilmsByCast(cast.getId());
        Assert.assertEquals(film, actualFilms.get(0));
    }

    /**
     * @see FilmService#getFilmsByCast(int)
     */
    @Test
    public void getFilmsByCast_Empty() {
        int size = filmService.getFilmsByCast(0).size();
        Assert.assertEquals(0, size);
    }

    /**
     * @see FilmService#getFilmsByGenre(Genre)
     */
    @Test
    public void getFilmsByGenre_ValidGenre_Success() {
        film = new Film("Fury", 2014);
        film.addGeners(Genre.WAR);
        filmService.addFilm(film);

        film = new Film("Schindler's List", 1993);
        film.addGeners(Genre.WAR);
        filmService.addFilm(film);

        film = new Film("LION", 2016);
        film.addGeners(Genre.BIOGRAPHY);
        filmService.addFilm(film);

        List<Film> actualFilms = filmService.getFilmsByGenre(Genre.WAR);
        Assert.assertEquals(2, actualFilms.size());
    }

    /**
     * @see FilmService#getFilmsByGenre(Genre)
     */
    @Test
    public void getFilmsByGenre_Empty() {
        List<Film> actualFilms = filmService.getFilmsByGenre(Genre.WAR);
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
        filmService.addFilm(film);

        double rating = filmService.getRating(film.getId());
        Assert.assertEquals(4.0, rating, 0.01);

        filmService.rateFilm(film.getId(), 5);

        rating = filmService.getRating(film.getId());
        Assert.assertEquals(4.5, rating, 0.01);

        filmService.rateFilm(film.getId(), 5);
        filmService.rateFilm(film.getId(), 5);
        filmService.rateFilm(film.getId(), 5);
        filmService.rateFilm(film.getId(), 5);

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
        filmService.addFilm(film);

        double rating = filmService.getRating(film.getId());
        Assert.assertEquals(0, rating, 0.01);
    }

    /**
     * @see FilmService#getAllRatings(int)
     */
    @Test
    public void getAllRatings_CorrectSize_Success() {
        Film film1 = new Film("Film 1", 1990);
        Film film2 = new Film("Film 2", 1990);
        filmService.addFilm(film1);
        filmService.addFilm(film2);
        Assert.assertEquals(2, filmService.getAllRatings(0).length);
    }

    /**
     * @see FilmService#getAllRatings(int)
     */
    @Test
    public void getAllRatings_CorrectAverages_Success() {
        Film film1 = new Film("Film 1", 1990);
        Film film2 = new Film("Film 2", 1990);
        filmService.addFilm(film1);
        filmService.rateFilm(film1.getId(), 3);
        filmService.rateFilm(film1.getId(), 4);
        filmService.addFilm(film2);
        filmService.rateFilm(film2.getId(), 5);
        filmService.rateFilm(film2.getId(), 1);
        Assert.assertEquals("3.5", filmService.getAllRatings(0)[0]);
        Assert.assertEquals("3.0", filmService.getAllRatings(0)[1]);
    }

    /**
     * @see FilmService#totalNumberOfFilms()
     */
    @Test
    public void totalNumber_Success() {
        film = new Film("In Bruges", 2008);
        for (int i = 0; i < 6; i++) {
            filmService.addFilm(film);
        }

        int size = filmService.totalNumberOfFilms();
        Assert.assertEquals(6, size);
    }

    /**
     * @see FilmService#totalNumberOfFilms()
     */
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
        Assert.assertTrue(status);

        int size = filmService.totalNumberOfFilms();
        Assert.assertEquals(1, size);
    }

    /**
     * @see FilmService#editFilm(Film)
     */
    @Test
    public void editFilm_ValidFilm_Success() {
        film = new Film("The Departed", 2004);
        filmService.addFilm(film);
        film.setHasOscar(true);
        filmService.editFilm(film);

        int size = filmService.totalNumberOfFilms();
        Assert.assertEquals(1, size);

        Film actualFilm = filmService.getFilmsList(0, 12).get(0);
        Assert.assertEquals(film, actualFilm);
    }

    /**
     * @see FilmService#editFilm(Film)
     */
    @Test
    public void editFilm_InvalidFilm_Success() {
        film = new Film("The Departed", 2004);
        film.setHasOscar(true);

        boolean status = filmService.editFilm(film);
        Assert.assertFalse(status);
    }

    /**
     * @see FilmService#editFilm(Film)
     */
    @Test(expected = ServiceException.class)
    public void editFilm_NameNull_Fail() {
        film = new Film("The Departed", 2004);
        filmService.addFilm(film);

        film.setTitle(null);
        filmService.editFilm(film);
    }

    /**
     * @see FilmService#editFilm(Film)
     */
    @Test(expected = ServiceException.class)
    public void editFilm_NameEmpty_Fail() {
        film = new Film("The Departed", 2004);
        filmService.addFilm(film);

        film.setTitle("");
        filmService.editFilm(film);
    }

    /**
     * @see FilmService#editFilm(Film)
     */
    @Test(expected = ServiceException.class)
    public void editFilm_InvalidYear_Fail() {
        film = new Film("The Departed", 2004);
        filmService.addFilm(film);

        film.setProdYear(-85);
        filmService.editFilm(film);
    }

    /**
     * @see FilmService#rateFilm(int, int)
     */
    @Test
    public void rateFilm_ValidRating_Success() {
        film = new Film("The Departed", 2004);
        filmService.addFilm(film);

        boolean status = filmService.rateFilm(film.getId(), 5);
        Assert.assertTrue(status);

        status = filmService.rateFilm(film.getId(), 5);
        Assert.assertTrue(status);

        int size = filmService.getFilmById(film.getId()).getRate_5star();
        Assert.assertEquals(2, size);

        status = filmService.rateFilm(0, 5);
        Assert.assertFalse(status);
    }

    /**
     * @see FilmService#rateFilm(int, int)
     */
    @Test(expected = ServiceException.class)
    public void rateFilm_InvalidRating_Fail() {
        film = new Film("The Departed", 2004);
        filmService.addFilm(film);

        boolean status = filmService.rateFilm(film.getId(), 9);
        Assert.assertFalse(status);
    }

    /**
     * @see FilmService#addGenreToFilm(Genre, int)
     */
    @Test
    public void addGenreToFilm_ValidInput_Success() {
        film = new Film("La La Land", 2016);
        filmService.addFilm(film);

        boolean status = filmService.addGenreToFilm(Genre.MUSICAL, film.getId());
        Assert.assertTrue(status);

        Film actualFilm = filmService.getFilmsByGenre(Genre.MUSICAL).get(0);
        Assert.assertEquals(film, actualFilm);
    }

    /**
     * @see FilmService#getFilteredFilms(String, int, int, boolean, String, String, int)
     */
    @Test
    public void filterFilms_ValidInput_Success() {
        film = new Film();
        film.setTitle("The Lost City of Z");
        film.setProdYear(2016);
        film.setDirector("James Gray");
        film.addGeners(Genre.ADVENTURE);
        film.addGeners(Genre.BIOGRAPHY);
        filmService.addFilm(film);
        cast = new Cast("Charlie Hunnam");
        castService.addCast(cast);
        castService.addCastToFilm(cast, film.getId());

        film = new Film();
        film.setTitle("City of God");
        film.setProdYear(2002);
        film.setDirector("Fernando Meirelles");
        film.addGeners(Genre.DRAMA);
        filmService.addFilm(film);

        film = new Film();
        film.setTitle("7");
        film.setProdYear(1995);
        film.setDirector("David Fincher");
        film.addGeners(Genre.DRAMA);
        film.addGeners(Genre.CRIME);
        film.addGeners(Genre.THRILLER);
        filmService.addFilm(film);
        cast = new Cast("Brad Pitt");
        castService.addCast(cast);
        castService.addCastToFilm(cast, film.getId());

        film = new Film();
        film.setTitle("Fight Club");
        film.setProdYear(1997);
        film.setDirector("David Fincher");
        film.addGeners(Genre.THRILLER);
        film.addGeners(Genre.MYSTERY);
        filmService.addFilm(film);
        castService.addCastToFilm(cast, film.getId());

        film = new Film();
        film.setTitle("Crash");
        film.setProdYear(2004);
        film.setHasOscar(true);
        film.setDirector("Paul Haggis");
        film.addGeners(Genre.CRIME);
        film.addGeners(Genre.DRAMA);
        film.addGeners(Genre.THRILLER);
        filmService.addFilm(film);
        cast = new Cast("Sandra Bullock", true);
        castService.addCast(cast);
        castService.addCastToFilm(cast, film.getId());

        film = new Film();
        film.setTitle("Gone Girl");
        film.setProdYear(2014);
        film.setDirector("David Fincher");
        film.addGeners(Genre.CRIME);
        film.addGeners(Genre.DRAMA);
        film.addGeners(Genre.MYSTERY);
        filmService.addFilm(film);
        cast = new Cast("Rosamund Pike");
        castService.addCast(cast);
        castService.addCastToFilm(cast, film.getId());

        //filters by nothing
        List<Film> filteredFilms = filmService.getFilteredFilms(null, 0, 0, false, null, null, 0);
        Assert.assertEquals(6, filteredFilms.size());

        //filters by title only
        filteredFilms = filmService.getFilteredFilms("city of", 0, 0, false, null, null, 0);
        Assert.assertEquals(2, filteredFilms.size());

        //filters by production year constraints only
        filteredFilms = filmService.getFilteredFilms(null, 2000, 0, false, null, null, 0);
        Assert.assertEquals(4, filteredFilms.size());

        //filters by oscar wins only
        filteredFilms = filmService.getFilteredFilms(null, 0, 0, true, null, null, 0);
        Assert.assertEquals(1, filteredFilms.size());

        //filters by director only
        filteredFilms = filmService.getFilteredFilms(null, 0, 0, false, "david fincher", null, 0);
        Assert.assertEquals(3, filteredFilms.size());

        //filters by cast only
        filteredFilms = filmService.getFilteredFilms(null, 0, 0, false, null, "brad pitt", 0);
        Assert.assertEquals(2, filteredFilms.size());

        //filters by genre only
        filteredFilms = filmService.getFilteredFilms(null, 0, 0, false, null, null, Genre.DRAMA.getValue());
        Assert.assertEquals(4, filteredFilms.size());

        //filters by everything
        filteredFilms = filmService.getFilteredFilms("gone girl", 2014, 2014, false, "david fincher", "rosamund pike", Genre.MYSTERY.getValue());
        Assert.assertEquals(1, filteredFilms.size());
        Assert.assertEquals(film, filteredFilms.get(0));
    }

    /**
     * @see FilmService#getFilteredFilms(String, int, int, boolean, String, String, int)
     */
    @Test
    public void searchTitle_NotExisting_Empty() {
        film = new Film("In Bruges", 2008);
        filmService.addFilm(film);

        boolean status = filmService.getFilteredFilms("", 3000, 3000, false, null, null, 0).isEmpty();
        Assert.assertTrue(status);
    }
}
