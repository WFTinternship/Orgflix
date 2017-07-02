package am.aca.orgflix.dao;

import am.aca.orgflix.BaseIntegrationTest;
import am.aca.orgflix.entity.Cast;
import am.aca.orgflix.entity.Film;
import am.aca.orgflix.entity.Genre;
import am.aca.orgflix.util.TestHelper;
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
    private FilmDAO jdbcFilmDAO;

    @Autowired
    private CastDAO jdbcCastDAO;

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
        jdbcFilmDAO.addFilm(film);

        boolean status = film.getId() > 0;
        Assert.assertTrue(status);
    }

    @Test
    public void addFilm_Succeeded() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        jdbcFilmDAO.addFilm(film);

        int size = jdbcFilmDAO.getFilmsList(0).size();
        Assert.assertEquals(1, size);
    }


    @Test(expected = DaoException.class)
    public void addFilm_Fail() {
        jdbcFilmDAO.addFilm(film);
    }

    @Test
    public void addGenreToFilmBySize_Succeeded() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        jdbcFilmDAO.addFilm(film);

        boolean status = jdbcFilmDAO.addGenreToFilm(Genre.COMEDY, film.getId());
        Assert.assertTrue(status);

        int size = jdbcFilmDAO.getFilmsByGenre(Genre.COMEDY).size();
        Assert.assertEquals(1, size);
    }

    @Test
    public void addGenreToFilmByFilm_Succeeded() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        jdbcFilmDAO.addFilm(film);
        jdbcFilmDAO.addGenreToFilm(Genre.COMEDY, film.getId());

        Film actualFilm = jdbcFilmDAO.getFilmsByGenre(Genre.COMEDY).get(0);
        Assert.assertEquals(film, actualFilm);
    }

    @Test
    public void rateFilm_Succeeded() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        jdbcFilmDAO.addFilm(film);
        jdbcFilmDAO.rateFilm(film.getId(), 5);
        jdbcFilmDAO.rateFilm(film.getId(), 5);

        int size = jdbcFilmDAO.getFilmById(film.getId()).getRate_5star();
        Assert.assertEquals(2, size);
    }

    @Test(expected = DaoException.class)
    public void rateFilm_Fail() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        jdbcFilmDAO.addFilm(film);
        jdbcFilmDAO.rateFilm(film.getId(), 9);
    }

    @Test
    public void getFilmById_Succeeded() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        jdbcFilmDAO.addFilm(film);

        Film actualFilm = jdbcFilmDAO.getFilmById(film.getId());
        Assert.assertEquals(film, actualFilm);
    }

    @Test
    public void getFilmById_Failed() {
        Film actualFilm = jdbcFilmDAO.getFilmById(-1);
        Assert.assertEquals(null, actualFilm);
    }

    @Test
    public void getFilmsList_Success() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        for (int i = 0; i < 7; i++) {
            jdbcFilmDAO.addFilm(film);
        }

        int size = jdbcFilmDAO.getFilmsList(0).size();
        Assert.assertEquals(7, size);
    }

    @Test
    public void getFilmsListPage2_Success() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        for (int i = 0; i < 20; i++) {
            jdbcFilmDAO.addFilm(film);
        }

        int size = jdbcFilmDAO.getFilmsList(12).size();
        Assert.assertEquals(20 - 12, size);
    }

    @Test
    public void getFilmsList_EmptyList() {
        int size = jdbcFilmDAO.getFilmsList(0).size();
        Assert.assertEquals(0, size);
    }

    @Test
    public void getFilmsListIndex_Failed() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        jdbcFilmDAO.addFilm(film);

        int size = jdbcFilmDAO.getFilmsList(1).size();
        Assert.assertEquals(0, size);
    }

    @Test
    public void getFilmsByGenreSize_Success() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        jdbcFilmDAO.addFilm(film);
        jdbcFilmDAO.addGenreToFilm(Genre.COMEDY, film.getId());
        film.setTitle("Deadpool");
        film.setProdYear(2016);
        jdbcFilmDAO.addFilm(film);
        jdbcFilmDAO.addGenreToFilm(Genre.COMEDY, film.getId());

        int size = jdbcFilmDAO.getFilmsByGenre(Genre.COMEDY).size();
        Assert.assertEquals(2, size);
    }

    @Test
    public void getFilmsByGenre_Fail() {
        int size = jdbcFilmDAO.getFilmsByGenre(Genre.WAR).size();
        Assert.assertEquals(0, size);
    }

    @Test
    public void getFilmsByCast_Success() {
        cast = new Cast("Viggo Mortensen", false);
        jdbcCastDAO.addCast(cast);

        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        jdbcFilmDAO.addFilm(film);

        jdbcCastDAO.addCastToFilm(cast, film.getId());

        Film actualFilm = jdbcFilmDAO.getFilmsByCast(cast.getId()).get(0);
        Assert.assertEquals(film, actualFilm);
    }


    @Test
    public void getFilmsByCast_Failed() {
        int size = jdbcFilmDAO.getFilmsByCast(0).size();
        Assert.assertEquals(0, size);
    }

    @Test
    public void getRatingInt_Succeeded() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        jdbcFilmDAO.addFilm(film);
        jdbcFilmDAO.rateFilm(film.getId(), 4);
        jdbcFilmDAO.rateFilm(film.getId(), 4);
        jdbcFilmDAO.rateFilm(film.getId(), 3);
        jdbcFilmDAO.rateFilm(film.getId(), 5);

        double rating = jdbcFilmDAO.getRating(film.getId());
        Assert.assertEquals(4.0, rating, 0.01);
    }

    @Test
    public void getRatingDouble_Succeeded() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        jdbcFilmDAO.addFilm(film);
        jdbcFilmDAO.rateFilm(film.getId(), 4);
        jdbcFilmDAO.rateFilm(film.getId(), 5);

        double rating = jdbcFilmDAO.getRating(film.getId());
        Assert.assertEquals(4.5, rating, 0.01);
    }

    @Test
    public void getRatingNaN_Failed() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        jdbcFilmDAO.addFilm(film);

        double rating = jdbcFilmDAO.getRating(film.getId());
        Assert.assertEquals(0, rating, 0.01);
    }

    @Test
    public void totalNumber_Succeeded() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        for (int i = 0; i < 6; i++) {
            jdbcFilmDAO.addFilm(film);
        }

        int size = jdbcFilmDAO.totalNumberOfFilms();
        Assert.assertEquals(6, size);
    }

    @Test
    public void totalNumber_Failed() {
        int size = jdbcFilmDAO.totalNumberOfFilms();
        Assert.assertEquals(0, size);
    }

    @Test
    public void searchTitle_Success() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        jdbcFilmDAO.addFilm(film);

        film.setTitle("City of God");
        film.setProdYear(2002);
        jdbcFilmDAO.addFilm(film);

        int size = jdbcFilmDAO.getFilteredFilms("City of God", 1000, 3000, "%", "%", "%", "%").size();
        Assert.assertEquals(1, size);
    }

    @Test
    public void searchTitleNotExisting_Empty() {
        film.setTitle("City of God");
        film.setProdYear(2002);
        jdbcFilmDAO.addFilm(film);

        boolean status = jdbcFilmDAO.getFilteredFilms("City of Angels", 1000, 3000, "%", "%", "%", "%").isEmpty();
        Assert.assertTrue(status);
    }

    @Test
    public void filterByCast_Success() {
        film.setTitle("City of God");
        film.setProdYear(2002);
        jdbcFilmDAO.addFilm(film);

        cast = new Cast("Alice Braga", false);
        jdbcCastDAO.addCast(cast);
        jdbcCastDAO.addCastToFilm(cast, film.getId());

        Film actualFilm = jdbcFilmDAO.getFilteredFilms("", 1000, 3000, "%", "%", String.valueOf(cast.getId()), "%").get(0);
        Assert.assertEquals(film, actualFilm);
    }

    @Test
    public void filterByCast_Empty() {
        boolean status = jdbcFilmDAO.getFilteredFilms("", 1000, 3000, "%", "%", "8", "%").isEmpty();
        Assert.assertTrue(status);
    }

    @Test
    public void filterByAllParameters_Success() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        jdbcFilmDAO.addFilm(film);
        jdbcFilmDAO.addGenreToFilm(Genre.COMEDY, film.getId());

        film.setTitle("City of God");
        film.setProdYear(2002);
        jdbcFilmDAO.addFilm(film);
        jdbcFilmDAO.addGenreToFilm(Genre.CRIME, film.getId());

        cast = new Cast("Alice Braga", false);
        jdbcCastDAO.addCast(cast);
        jdbcCastDAO.addCastToFilm(cast, film.getId());

        int size = jdbcFilmDAO.getFilteredFilms("City of God", 2002, 2002, String.valueOf(0), "%", String.valueOf(cast.getId()), String.valueOf(Genre.CRIME.getValue())).size();
        Assert.assertEquals(1, size);

        Film actualFilm = jdbcFilmDAO.getFilteredFilms("City of God", 2002, 2002, String.valueOf(0), "%", String.valueOf(cast.getId()), String.valueOf(Genre.CRIME.getValue())).get(0);
        Assert.assertEquals(film, actualFilm);
    }

    @Test
    public void editFilm_Succeeded() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        film.setDirector("Matthew Ross");
        jdbcFilmDAO.addFilm(film);
        film.setHasOscar(true);

        boolean status = jdbcFilmDAO.editFilm(film);
        assertTrue(status);
    }

    @Test
    public void editFilmSizeCheck_Succeeded() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        film.setDirector("Matthew Ross");
        jdbcFilmDAO.addFilm(film);
        film.setDirector("Matt Ross");
        jdbcFilmDAO.editFilm(film);

        int size = jdbcFilmDAO.totalNumberOfFilms();
        Assert.assertEquals(1, size);
    }

    @Test(expected = DaoException.class)
    public void editFilm_Fail() {
        jdbcFilmDAO.editFilm(film);
    }

    @Test
    public void resetCastsByReturn_Success() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        jdbcFilmDAO.addFilm(film);

        cast = new Cast("Viggo Mortensen");
        jdbcCastDAO.addCast(cast);

        jdbcCastDAO.addCastToFilm(cast, film.getId());

        boolean status = jdbcFilmDAO.resetRelationCasts(film);
        Assert.assertTrue(status);
    }

    @Test
    public void resetCastsByCheck_Success() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        jdbcFilmDAO.addFilm(film);

        cast = new Cast("Viggo Mortensen");
        jdbcCastDAO.addCast(cast);

        jdbcCastDAO.addCastToFilm(cast, film.getId());
        jdbcFilmDAO.resetRelationCasts(film);

        int size = jdbcFilmDAO.getFilmsByCast(cast.getId()).size();
        Assert.assertEquals(0, size);
    }

    @Test
    public void resetCastsKeepOthers_Success() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        jdbcFilmDAO.addFilm(film);

        cast = new Cast("Viggo Mortensen", false);
        jdbcCastDAO.addCast(cast);
        jdbcCastDAO.addCastToFilm(cast, film.getId());

        jdbcFilmDAO.resetRelationCasts(film);

        film.setTitle("City of God");
        film.setProdYear(2002);
        jdbcFilmDAO.addFilm(film);

        cast = new Cast("Alice Braga", false);
        jdbcCastDAO.addCast(cast);
        jdbcCastDAO.addCastToFilm(cast, film.getId());

        Film actualFilm = jdbcFilmDAO.getFilmsByCast(cast.getId()).get(0);
        Assert.assertEquals(film, actualFilm);
    }

    @Test
    public void resetCasts_Fail() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        jdbcFilmDAO.addFilm(film);

        boolean status = jdbcFilmDAO.resetRelationCasts(film);
        Assert.assertFalse(status);
    }

    @Test
    public void resetGenresReturn_Success() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        jdbcFilmDAO.addFilm(film);
        jdbcFilmDAO.addGenreToFilm(Genre.DRAMA, film.getId());

        boolean status = jdbcFilmDAO.resetRelationGenres(film);
        Assert.assertTrue(status);
    }

    @Test
    public void resetGenresCheck_Success() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        jdbcFilmDAO.addFilm(film);
        jdbcFilmDAO.addGenreToFilm(Genre.DRAMA, film.getId());
        jdbcFilmDAO.resetRelationGenres(film);

        boolean status = jdbcFilmDAO.getFilmsByGenre(Genre.DRAMA).isEmpty();
        Assert.assertTrue(status);
    }

    @Test
    public void resetGenresKeepsOthers_Success() {
        film.setTitle("American History X");
        film.setProdYear(1998);
        jdbcFilmDAO.addFilm(film);
        jdbcFilmDAO.addGenreToFilm(Genre.CRIME, film.getId());
        jdbcFilmDAO.addGenreToFilm(Genre.DRAMA, film.getId());
        jdbcFilmDAO.resetRelationGenres(film);

        film.setTitle("City of God");
        film.setProdYear(2002);
        jdbcFilmDAO.addFilm(film);
        jdbcFilmDAO.addGenreToFilm(Genre.CRIME, film.getId());

        Film actualFilm = jdbcFilmDAO.getFilmsByGenre(Genre.CRIME).get(0);
        Assert.assertEquals(film, actualFilm);
    }

    @Test
    public void resetGenres_Fail() {
        film.setTitle("City of God");
        film.setProdYear(2002);
        jdbcFilmDAO.addFilm(film);

        boolean status = jdbcFilmDAO.resetRelationGenres(film);
        Assert.assertFalse(status);
    }

    @Test
    public void remove_Success() {
        film.setTitle("City of God");
        film.setProdYear(2002);
        jdbcFilmDAO.addFilm(film);
        jdbcFilmDAO.remove(film);

        int size = jdbcFilmDAO.totalNumberOfFilms();
        Assert.assertEquals(0, size);
    }

    @Test
    public void remove_Fail() {
        film.setTitle("City of God");
        film.setProdYear(2002);
        jdbcFilmDAO.addFilm(film);
        film = new Film();
        jdbcFilmDAO.remove(film);

        int size = jdbcFilmDAO.totalNumberOfFilms();
        Assert.assertEquals(1, size);
    }
}