package am.aca.orgflix.integration.service;

import am.aca.entity.Cast;
import am.aca.entity.Film;
import am.aca.entity.Genre;
import am.aca.orgflix.BaseIntegrationTest;
import am.aca.service.CastService;
import am.aca.service.FilmService;
import am.aca.util.TestHelper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Test for DAO services
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
        Assert.assertTrue(filmService.addFilm(film));
        Assert.assertTrue(film.getId() > 0);
    }

    @Test
    public void addFilmWithGenre_Success() {
        film = new Film("In Bruges", 2008);
        film.addGeners(Genre.ACTION);
        Assert.assertTrue(filmService.addFilm(film));
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

        castService.addCastToFilm(cast, film.getId());
        Assert.assertEquals(film, filmService.getFilmsByCast(cast.getId()).get(0));
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
        Assert.assertEquals(4.0, filmService.getRating(film.getId()), 0.01);
    }

    @Test
    public void getRatingDouble_Succeeded() {
        film = new Film("Fury", 2014);
        film.addGeners(Genre.WAR);
        film.setRate_4star(1);
        film.setRate_5star(1);
        filmService.addFilm(film);
        Assert.assertEquals(4.5, filmService.getRating(film.getId()), 0.01);
    }

    @Test
    public void getRatingNaN_Failed() {
        film = new Film("Fury", 2014);
        film.addGeners(Genre.WAR);
        filmService.addFilm(film);
        Assert.assertEquals(0, filmService.getRating(film.getId()), 0.01);
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
        film.setHasOscar(false);
        filmService.addFilm(film);

        cast = new Cast("Matt Damon", true);
        film.addCast(cast);
        filmService.editFilm(film);

        Assert.assertEquals(film, filmService.getFilmsByCast(cast.getId()).get(0));
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
        Assert.assertTrue(filmService.rateFilm(film.getId(), 5));
        Assert.assertTrue(filmService.rateFilm(film.getId(), 5));
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
        Assert.assertTrue(filmService.addGenreToFilm(Genre.MUSICAL, film.getId()));
        Assert.assertEquals(film, filmService.getFilmsByGenre(Genre.MUSICAL).get(0));
    }

    @Test
    public void addGenreToFilmByIdByFilm_Succeeded() {
        film = new Film("La La Land", 2016);
        filmService.addFilm(film);
        Assert.assertTrue(filmService.addGenreToFilm(Genre.MUSICAL, film.getId()));
        Assert.assertEquals(film, filmService.getFilmsByGenre(Genre.MUSICAL).get(0));
    }

    @Test
    public void searchTitle_Success() {
        film = new Film("In Bruges", 2008);
        filmService.addFilm(film);

        film = new Film("Trainspotting", 1996);
        filmService.addFilm(film);

        Assert.assertEquals(1, filmService.getFilteredFilms("Trainspotting", 1000, 3000, false, "%", 0, Genre.ADVENTURE).size());
    }

    @Test
    public void searchTitleNotExisting_Empty() {
        film = new Film("In Bruges", 2008);
        filmService.addFilm(film);
        Assert.assertTrue(filmService.getFilteredFilms("City of Angels", 1000, 3000, true, "%", 0, Genre.BIOGRAPHY).isEmpty());
    }

    @Test
    public void filterByCast_Success() {
        cast = new Cast("Collin Farrell");
        castService.addCast(cast);

        film = new Film("In Bruges", 2008);
        filmService.addFilm(film);

        castService.addCastToFilm(cast, film.getId());

        Assert.assertEquals(film, filmService.getFilteredFilms("", 1000, 3000, false, "%", cast.getId(), Genre.MYSTERY).get(0));
    }

    @Test
    public void filterByCast_Empty() {
        Assert.assertTrue(filmService.getFilteredFilms("Trainspotting", 1996, 1996, false, "Danny Boyle", 0, Genre.ANIMATION).isEmpty());
    }

    @Test
    public void filterByAllParameters_Success() {
        film = new Film("In Bruges", 2008);
        filmService.addFilm(film);

        film = new Film("Trainspotting", 1996);
        cast = new Cast("Ewan McGregor");
        film.addCast(cast);
        film.setDirector("Danny Boyle");
        film.addGeners(Genre.CRIME);
        filmService.addFilm(film);
        Assert.assertEquals(1, filmService.getFilteredFilms("Trainspotting", 1996, 1996, false, "Danny Boyle", cast.getId(), Genre.CRIME).size());
        Assert.assertEquals(film, filmService.getFilteredFilms("Trainspotting", 1996, 1996, false, "Danny Boyle", cast.getId(), Genre.CRIME).get(0));
    }
}
