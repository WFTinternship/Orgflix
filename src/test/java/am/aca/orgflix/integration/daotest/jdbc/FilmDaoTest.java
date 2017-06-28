package am.aca.orgflix.integration.daotest.jdbc;

import am.aca.dao.DaoException;
import am.aca.dao.jdbc.CastDAO;
import am.aca.dao.jdbc.FilmDAO;
import am.aca.entity.Cast;
import am.aca.entity.Film;
import am.aca.entity.Genre;
import am.aca.orgflix.BaseIntegrationTest;
import am.aca.util.TestHelper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertTrue;

/**
 * Test for film DAO methods
 */
public class FilmDaoTest extends BaseIntegrationTest {

    @Autowired
    private FilmDAO filmDAO;

    @Autowired
    private CastDAO castDAO;

    @Autowired
    private TestHelper helper;

    private Film film;
    private Cast cast;

    @Before
    public void setUp() {
        film = new Film();
    }

    @After
    public void tearDown() {
        helper.emptyTable(new String[]{"genre_to_film", "film_to_cast", "films", "casts"});
    }

    @Test
    public void addFilmGetId_Succeeded() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        filmDAO.addFilm(film);
        Assert.assertTrue(film.getId() > 0);
    }

    @Test
    public void addFilm_Succeeded() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        filmDAO.addFilm(film);
        Assert.assertEquals(1, filmDAO.getFilmsList(0).size());
    }


    @Test(expected = DaoException.class)
    public void addFilm_Fail() {
        filmDAO.addFilm(film);
    }

    @Test
    public void addGenreToFilmBySize_Succeeded() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        filmDAO.addFilm(film);
        Assert.assertTrue(filmDAO.addGenreToFilm(Genre.COMEDY, film.getId()));
        Assert.assertEquals(1, filmDAO.getFilmsByGenre(Genre.COMEDY).size());
    }

    @Test
    public void addGenreToFilmByFilm_Succeeded() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        filmDAO.addFilm(film);
        filmDAO.addGenreToFilm(Genre.COMEDY, film.getId());
        Assert.assertEquals(film, filmDAO.getFilmsByGenre(Genre.COMEDY).get(0));
    }

    @Test
    public void rateFilm_Succeeded() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        filmDAO.addFilm(film);
        filmDAO.rateFilm(film.getId(), 5);
        filmDAO.rateFilm(film.getId(), 5);
        Assert.assertEquals(2, filmDAO.getFilmById(film.getId()).getRate_5star());
    }

    @Test(expected = DaoException.class)
    public void rateFilm_Fail() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        filmDAO.addFilm(film);
        filmDAO.rateFilm(film.getId(), 9);
    }

    @Test
    public void getFilmById_Succeeded() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        filmDAO.addFilm(film);
        Assert.assertEquals(film, filmDAO.getFilmById(film.getId()));
    }

    @Test
    public void getFilmById_Failed() {
        Assert.assertEquals(null, filmDAO.getFilmById(-1));
    }

    @Test
    public void getFilmsList_Succeeded() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        for (int i = 0; i < 7; i++) {
            filmDAO.addFilm(film);
        }
        Assert.assertEquals(7, filmDAO.getFilmsList(0).size());
    }

    @Test
    public void getFilmsListPage2_Succeeded() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        for (int i = 0; i < 20; i++) {
            filmDAO.addFilm(film);
        }
        Assert.assertEquals(20 - 12, filmDAO.getFilmsList(12).size());
    }

    @Test
    public void getFilmsList_EmptyList() {
        Assert.assertEquals(0, filmDAO.getFilmsList(0).size());
    }

    @Test
    public void getFilmsListIndex_Failed() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        filmDAO.addFilm(film);
        Assert.assertEquals(0, filmDAO.getFilmsList(1).size());
    }

    @Test
    public void getFilmsByGenreSize_Succeeded() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        filmDAO.addFilm(film);
        filmDAO.addGenreToFilm(Genre.COMEDY, film.getId());
        film.setTitle("Deadpool");
        film.setProdYear(2016);
        filmDAO.addFilm(film);
        filmDAO.addGenreToFilm(Genre.COMEDY, film.getId());
        Assert.assertEquals(2, filmDAO.getFilmsByGenre(Genre.COMEDY).size());
    }

    @Test
    public void getFilmsByGenre_Fail() {
        Assert.assertEquals(0, filmDAO.getFilmsByGenre(Genre.WAR).size());
    }

    @Test
    public void getFilmsByCast_Succeeded() {
        cast = new Cast("Viggo Mortensen", false);
        castDAO.addCast(cast);

        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        filmDAO.addFilm(film);

        castDAO.addCastToFilm(cast, film.getId());
        Assert.assertEquals(film, filmDAO.getFilmsByCast(cast.getId()).get(0));
    }


    @Test
    public void getFilmsByCast_Failed() {
        Assert.assertEquals(0, filmDAO.getFilmsByCast(0).size());
    }

    @Test
    public void getRatingInt_Succeeded() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        filmDAO.addFilm(film);
        filmDAO.rateFilm(film.getId(), 4);
        filmDAO.rateFilm(film.getId(), 4);
        filmDAO.rateFilm(film.getId(), 3);
        filmDAO.rateFilm(film.getId(), 5);
        Assert.assertEquals(4.0, filmDAO.getRating(film.getId()), 0.01);
    }

    @Test
    public void getRatingDouble_Succeeded() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        filmDAO.addFilm(film);
        filmDAO.rateFilm(film.getId(), 4);
        filmDAO.rateFilm(film.getId(), 5);
        Assert.assertEquals(4.5, filmDAO.getRating(film.getId()), 0.01);
    }

    @Test
    public void getRatingNaN_Failed() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        filmDAO.addFilm(film);
        Assert.assertEquals(0, filmDAO.getRating(film.getId()), 0.01);
    }

    @Test
    public void totalNumber_Succeeded() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
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
    public void searchTitle_Success() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        filmDAO.addFilm(film);

        film.setTitle("City of God");
        film.setProdYear(2002);
        filmDAO.addFilm(film);

        Assert.assertEquals(1, filmDAO.getFilteredFilms("City of God", 1000, 3000, "%", "%", "%", "%").size());
    }

    @Test
    public void searchTitleNotExisting_Empty() {
        film.setTitle("City of God");
        film.setProdYear(2002);
        filmDAO.addFilm(film);

        Assert.assertTrue(filmDAO.getFilteredFilms("City of Angels", 1000, 3000, "%", "%", "%", "%").isEmpty());
    }

    @Test
    public void filterByCast_Success() {
        film.setTitle("City of God");
        film.setProdYear(2002);
        filmDAO.addFilm(film);

        cast = new Cast("Alice Braga", false);
        castDAO.addCast(cast);
        castDAO.addCastToFilm(cast, film.getId());

        Assert.assertEquals(film, filmDAO.getFilteredFilms("", 1000, 3000, "%", "%", String.valueOf(cast.getId()), "%").get(0));
    }

    @Test
    public void filterByCast_Empty() {
        Assert.assertTrue(filmDAO.getFilteredFilms("", 1000, 3000, "%", "%", "8", "%").isEmpty());
    }

    @Test
    public void filterByAllParameters_Success() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        filmDAO.addFilm(film);
        filmDAO.addGenreToFilm(Genre.COMEDY, film.getId());

        film.setTitle("City of God");
        film.setProdYear(2002);
        filmDAO.addFilm(film);
        filmDAO.addGenreToFilm(Genre.CRIME, film.getId());

        cast = new Cast("Alice Braga", false);
        castDAO.addCast(cast);
        castDAO.addCastToFilm(cast, film.getId());

        Assert.assertEquals(1, filmDAO.getFilteredFilms("City of God", 2002, 2002, String.valueOf(0), "%", String.valueOf(cast.getId()), String.valueOf(Genre.CRIME.getValue())).size());
        Assert.assertEquals(film, filmDAO.getFilteredFilms("City of God", 2002, 2002, String.valueOf(0), "%", String.valueOf(cast.getId()), String.valueOf(Genre.CRIME.getValue())).get(0));
    }

    @Test
    public void editFilm_Succeeded() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        film.setDirector("Matthew Ross");
        filmDAO.addFilm(film);
        film.setHasOscar(true);
        assertTrue(filmDAO.editFilm(film));
    }

    @Test
    public void editFilmSizeCheck_Succeeded() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        film.setDirector("Matthew Ross");
        filmDAO.addFilm(film);
        film.setDirector("Matt Ross");
        filmDAO.editFilm(film);
        Assert.assertEquals(1, filmDAO.totalNumberOfFilms());
    }

    @Test(expected = DaoException.class)
    public void editFilm_Fail() {
        filmDAO.editFilm(film);
    }

    @Test
    public void resetCastsByReturn_Success() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        filmDAO.addFilm(film);

        cast = new Cast("Viggo Mortensen");
        castDAO.addCast(cast);

        castDAO.addCastToFilm(cast, film.getId());
        Assert.assertTrue(filmDAO.resetRelationCasts(film));
    }

    @Test
    public void resetCastsByCheck_Success() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        filmDAO.addFilm(film);

        cast = new Cast("Viggo Mortensen");
        castDAO.addCast(cast);

        castDAO.addCastToFilm(cast, film.getId());
        filmDAO.resetRelationCasts(film);
        Assert.assertEquals(0, filmDAO.getFilmsByCast(cast.getId()).size());
    }

    @Test
    public void resetCastsKeepOthers_Success() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        filmDAO.addFilm(film);

        cast = new Cast("Viggo Mortensen", false);
        castDAO.addCast(cast);
        castDAO.addCastToFilm(cast, film.getId());

        filmDAO.resetRelationCasts(film);

        film.setTitle("City of God");
        film.setProdYear(2002);
        filmDAO.addFilm(film);

        cast = new Cast("Alice Braga", false);
        castDAO.addCast(cast);
        castDAO.addCastToFilm(cast, film.getId());

        Assert.assertEquals(film, filmDAO.getFilmsByCast(cast.getId()).get(0));
    }

    @Test
    public void resetCasts_Fail() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        filmDAO.addFilm(film);
        Assert.assertFalse(filmDAO.resetRelationCasts(film));
    }

    @Test
    public void resetGenresReturn_Success() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        filmDAO.addFilm(film);
        filmDAO.addGenreToFilm(Genre.DRAMA, film.getId());
        Assert.assertTrue(filmDAO.resetRelationGenres(film));
    }

    @Test
    public void resetGenresCheck_Success() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        filmDAO.addFilm(film);
        filmDAO.addGenreToFilm(Genre.DRAMA, film.getId());
        filmDAO.resetRelationGenres(film);
        Assert.assertTrue(filmDAO.getFilmsByGenre(Genre.DRAMA).isEmpty());
    }

    @Test
    public void resetGenresKeepsOthers_Success() {
        film.setTitle("American History X");
        film.setProdYear(1998);
        filmDAO.addFilm(film);
        filmDAO.addGenreToFilm(Genre.CRIME, film.getId());
        filmDAO.addGenreToFilm(Genre.DRAMA, film.getId());
        filmDAO.resetRelationGenres(film);

        film.setTitle("City of God");
        film.setProdYear(2002);
        filmDAO.addFilm(film);
        filmDAO.addGenreToFilm(Genre.CRIME, film.getId());

        Assert.assertEquals(film, filmDAO.getFilmsByGenre(Genre.CRIME).get(0));
    }

    @Test
    public void resetGenres_Fail() {
        film.setTitle("City of God");
        film.setProdYear(2002);
        filmDAO.addFilm(film);
        Assert.assertFalse(filmDAO.resetRelationGenres(film));
    }

    @Test
    public void remove_Success() {
        film.setTitle("City of God");
        film.setProdYear(2002);
        filmDAO.addFilm(film);
        filmDAO.remove(film);
        Assert.assertEquals(0, filmDAO.totalNumberOfFilms());
    }

    @Test
    public void remove_Fail() {
        film.setTitle("City of God");
        film.setProdYear(2002);
        filmDAO.addFilm(film);
        film = new Film();
        filmDAO.remove(film);
        Assert.assertEquals(1, filmDAO.totalNumberOfFilms());
    }
}