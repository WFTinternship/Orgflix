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

import java.util.ArrayList;

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
        helper.emptyTable(new String[]{"film_to_cast", "genre_to_film", "films", "casts"});
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
    public void addFilm_WithGenre_Success() {
        film = new Film("In Bruges", 2008);
        film.addGeners(Genre.ACTION);

        boolean status = filmService.addFilm(film);
        Assert.assertTrue(status);

        Film actualFilm = filmService.getFilmsByGenre(Genre.ACTION).get(0);
        Assert.assertEquals(film, actualFilm);
    }

    @Test(expected = ServiceException.class)
    public void addFilm_EmptyName_Fail() {
        film = new Film(null, 2066);
        filmService.addFilm(film);
    }

    @Test
    public void getFilmsList_Success() {
        cast = new Cast("Tom Hanks");
        ArrayList<Cast> list = new ArrayList<>();
        list.add(cast);

        Film film = new Film("Forrest Gump", 1990);
        film.setCasts(list);
        filmService.addFilm(film);
        Film film1 = filmService.getFilmsList(0).get(0);
        Assert.assertEquals(film1, film);
    }

    @Test
    public void getFilmsList_WIndex_Success() {
        film = new Film("In Bruges", 2008);
        for (int i = 0; i < 5; i++) {
            filmService.addFilm(film);
        }

        int size = filmService.getFilmsList(2).size();
        Assert.assertEquals(5 - 2, size);
    }

    @Test
    public void getFilmsList_Empty() {
        boolean status = filmService.getFilmsList(0).isEmpty();
        Assert.assertTrue(status);
    }

    @Test
    public void getFilmsList_BadIndex_Fail() {
        film = new Film("In Bruges", 2008);
        for (int i = 0; i < 5; i++) {
            filmService.addFilm(film);
        }

        boolean status = filmService.getFilmsList(8).isEmpty();
        Assert.assertTrue(status);
    }

    @Test
    public void getFilmsList_ById_Success() {
        film = new Film("In Bruges", 2008);
        filmService.addFilm(film);
        film = new Film("London Boulevard", 2010);
        filmService.addFilm(film);
        film = new Film("The Lobster", 2015);
        filmService.addFilm(film);

        int id = filmService.getFilmsList(0).get(2).getId();
        Assert.assertEquals(film.getId(), id);
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

        Assert.assertEquals(null, actualFilm);
    }

    @Test
    public void getFilmsByCast_Success() {
        cast = new Cast("Collin Farrell");
        castService.addCast(cast);

        film = new Film("In Bruges", 2008);
        filmService.addFilm(film);

        castService.addCastToFilm(cast, film.getId());

        Film actualFilm = filmService.getFilmsByCast(cast.getId()).get(0);
        Assert.assertEquals(film, actualFilm);
    }


    @Test
    public void getFilmsByCast_Empty() {
        int size = filmService.getFilmsByCast(0).size();
        Assert.assertEquals(0, size);
    }

    @Test
    public void getFilmsByGenre_Size_Success() {
        film = new Film("Fury", 2014);
        film.addGeners(Genre.WAR);
        filmService.addFilm(film);

        film = new Film("Schindler's List", 1993);
        film.addGeners(Genre.WAR);
        filmService.addFilm(film);

        int size = filmService.getFilmsByGenre(Genre.WAR).size();
        Assert.assertEquals(2, size);
    }

    @Test
    public void getFilmsByGenre_Fail() {
        int size = filmService.getFilmsByGenre(Genre.WAR).size();
        Assert.assertEquals(0, size);
    }

    @Test
    public void getRating_Int_Success() {
        film = new Film("Fury", 2014);
        film.addGeners(Genre.WAR);
        film.setRate_4star(1);
        filmService.addFilm(film);

        double rating = filmService.getRating(film.getId());
        Assert.assertEquals(4.0, rating, 0.01);
    }

    @Test
    public void getRating_Double_Success() {
        film = new Film("Fury", 2014);
        film.addGeners(Genre.WAR);
        film.setRate_4star(1);
        film.setRate_5star(1);
        filmService.addFilm(film);

        double rating = filmService.getRating(film.getId());
        Assert.assertEquals(4.5, rating, 0.01);
    }

    @Test
    public void getRating_NaN_Fail() {
        film = new Film("Fury", 2014);
        film.addGeners(Genre.WAR);
        filmService.addFilm(film);

        double rating = filmService.getRating(film.getId());
        Assert.assertEquals(0, rating, 0.01);
    }

    @Test
    public void getAllRatings_CorrectSize_Success() {
        Film film1 = new Film("Film 1", 1990);
        Film film2 = new Film("Film 2", 1990);
        filmService.addFilm(film1);
        filmService.addFilm(film2);
        Assert.assertEquals(2, filmService.getAllRatings(0).length);
    }

    @Test
    public void getAllRatings_CorrectAverages_Success() {
        Film film1 = new Film("Film 1", 1990);
        Film film2 = new Film("Film 2", 1990);
        filmService.addFilm(film1);
        filmService.rateFilm(film1.getId(),3);
        filmService.rateFilm(film1.getId(),4);
        filmService.addFilm(film2);
        filmService.rateFilm(film2.getId(),5);
        filmService.rateFilm(film2.getId(),1);
        Assert.assertEquals("3.5", filmService.getAllRatings(0)[0]);
        Assert.assertEquals("3.0", filmService.getAllRatings(0)[1]);
    }

    @Test
    public void totalNumber_Success() {
        film = new Film("In Bruges", 2008);
        for (int i = 0; i < 6; i++) {
            filmService.addFilm(film);
        }

        int size = filmService.totalNumberOfFilms();
        Assert.assertEquals(6, size);
    }

    @Test
    public void totalNumber_Fail() {
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
    }

    @Test
    public void editFilm_SizeCheck_Success() {
        film = new Film("The Departed", 2004);
        filmService.addFilm(film);
        film.setHasOscar(true);
        filmService.editFilm(film);

        int size = filmService.totalNumberOfFilms();
        Assert.assertEquals(1, size);
    }

    @Test
    public void editFilm_ObjCheck_Success() {
        film = new Film("The Departed", 2004);
        film.setHasOscar(false);
        filmService.addFilm(film);

        cast = new Cast("Matt Damon", true);
        film.addCast(cast);
        filmService.editFilm(film);

        Film actualFilm = filmService.getFilmsByCast(cast.getId()).get(0);
        Assert.assertEquals(film, actualFilm);
    }

    @Test(expected = ServiceException.class)
    public void editFilm_Fail() {
        film = new Film();

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

        boolean status = filmService.rateFilm(film.getId(), 9);
        Assert.assertFalse(status);
    }

    @Test
    public void addGenreToFilm_BySize_Success() {
        film = new Film("La La Land", 2016);
        filmService.addFilm(film);

        boolean status = filmService.addGenreToFilm(Genre.MUSICAL, film.getId());
        Assert.assertTrue(status);
    }

    @Test
    public void addGenreToFilm_ByIdByFilm_Success() {
        film = new Film("La La Land", 2016);
        filmService.addFilm(film);

        boolean status = filmService.addGenreToFilm(Genre.MUSICAL, film.getId());
        Assert.assertTrue(status);

        Film actualFilm = filmService.getFilmsByGenre(Genre.MUSICAL).get(0);
        Assert.assertEquals(film, actualFilm);
    }

    @Test
    public void searchTitle_Success() {
        film = new Film("In Bruges", 2008);
        filmService.addFilm(film);

        film = new Film("Trainspotting", 1996);
        filmService.addFilm(film);

        int size = filmService.getFilteredFilms("Trainspotting", 1000, 3000, false, "%", 0, Genre.ADVENTURE).size();
        Assert.assertEquals(1, size);
    }

    @Test
    public void searchTitle_NotExisting_Empty() {
        film = new Film("In Bruges", 2008);
        filmService.addFilm(film);

        boolean status = filmService.getFilteredFilms("City of Angels", 1000, 3000, true, "%", 0, Genre.BIOGRAPHY).isEmpty();
        Assert.assertTrue(status);
    }

    @Test
    public void filterByCast_Success() {
        cast = new Cast("Collin Farrell");
        castService.addCast(cast);

        film = new Film("In Bruges", 2008);
        filmService.addFilm(film);

        castService.addCastToFilm(cast, film.getId());

        Film actualFilm = filmService.getFilteredFilms("", 1000, 3000, false, "%", cast.getId(), Genre.MYSTERY).get(0);
        Assert.assertEquals(film, actualFilm);
    }

    @Test
    public void filterByCast_Empty() {
        boolean status = filmService.getFilteredFilms("Trainspotting", 1996, 1996, false, "Danny Boyle", 0, Genre.ANIMATION).isEmpty();
        Assert.assertTrue(status);
    }

    @Test
    public void filter_ByAllParameters_Success() {
        film = new Film("In Bruges", 2008);
        filmService.addFilm(film);

        film = new Film("Trainspotting", 1996);
        cast = new Cast("Ewan McGregor");
        film.addCast(cast);
        film.setDirector("Danny Boyle");
        film.addGeners(Genre.CRIME);
        filmService.addFilm(film);

        int size = filmService.getFilteredFilms("Trainspotting", 1996, 1996, false, "Danny Boyle", cast.getId(), Genre.CRIME).size();
        Assert.assertEquals(1, size);

        Film actualFilm = filmService.getFilteredFilms("Trainspotting", 1996, 1996, false, "Danny Boyle", cast.getId(), Genre.CRIME).get(0);
        Assert.assertEquals(film, actualFilm);
    }
}
