package am.aca.orgflix.dao;

import am.aca.orgflix.BaseIntegrationTest;
import am.aca.orgflix.entity.Cast;
import am.aca.orgflix.entity.Film;
import am.aca.orgflix.entity.Genre;
import am.aca.orgflix.util.TestHelper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Integration tests for film DAO methods
 */
public class FilmDaoTest extends BaseIntegrationTest {

    @Autowired
    private FilmDAO jdbcFilmDAO;

    @Autowired
    private CastDAO jdbcCastDAO;

    @Autowired
    private TestHelper helper;

    private Film film = new Film();
    private Cast cast;

    /**
     * Rolls back all changes applied to the test DB resulted from tests
     */
    @After
    public void tearDown() {
        helper.emptyTable(new String[]{"FILM_TO_GENRE", "FILM_TO_CAST", "FILMS", "CASTS"});
    }

    /**
     * @see FilmDAO#addFilm(am.aca.orgflix.entity.Film)
     */
    @Test
    public void addFilm_ValidFilm_Success() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        jdbcFilmDAO.addFilm(film);

        int id = film.getId();
        Assert.assertTrue(id > 0);

        int size = jdbcFilmDAO.getFilmsList(0, 12).size();
        Assert.assertEquals(1, size);

        Film actualFilm = jdbcFilmDAO.getFilmById(film.getId());
        Assert.assertEquals(film, actualFilm);
    }

    /**
     * @see FilmDAO#addFilm(am.aca.orgflix.entity.Film)
     */
    @Test(expected = RuntimeException.class)
    public void addFilm_InvalidFilm_Fail() {
        jdbcFilmDAO.addFilm(film);
    }

    /**
     * @see FilmDAO#addGenreToFilm(am.aca.orgflix.entity.Genre, int)
     */
    @Test
    public void addGenreToFilm_ValidInput_Success() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        jdbcFilmDAO.addFilm(film);

        boolean status = jdbcFilmDAO.addGenreToFilm(Genre.COMEDY, film.getId());
        Assert.assertTrue(status);

        int size = jdbcFilmDAO.getFilmsByGenre(Genre.COMEDY).size();
        Assert.assertEquals(1, size);

        Film actualFilm = jdbcFilmDAO.getFilmsByGenre(Genre.COMEDY).get(0);
        Assert.assertEquals(film, actualFilm);
    }

    /**
     * @see FilmDAO#addGenreToFilm(am.aca.orgflix.entity.Genre, int)
     */
    @Test(expected = RuntimeException.class)
    public void addGenreToFilm_SameAssociationTwice_Fail() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        jdbcFilmDAO.addFilm(film);
        jdbcFilmDAO.addGenreToFilm(Genre.COMEDY, film.getId());
        jdbcFilmDAO.addGenreToFilm(Genre.COMEDY, film.getId());
    }

    /**
     * @see FilmDAO#addGenreToFilm(am.aca.orgflix.entity.Genre, int)
     */
    @Test(expected = RuntimeException.class)
    public void addGenreToFilm_InvalidFilm_Fail() {
        jdbcFilmDAO.addGenreToFilm(Genre.COMEDY, film.getId());
    }

    /**
     * @see FilmDAO#rateFilm(int, int)
     */
    @Test
    public void rateFilm_ValidInput_Success() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        jdbcFilmDAO.addFilm(film);
        boolean status;

        status = jdbcFilmDAO.rateFilm(film.getId(), 5);
        Assert.assertTrue(status);
        status = jdbcFilmDAO.rateFilm(film.getId(), 5);
        Assert.assertTrue(status);

        int size = jdbcFilmDAO.getFilmById(film.getId()).getRate_5star();
        Assert.assertEquals(2, size);

        status = jdbcFilmDAO.rateFilm(film.getId(), 4);
        Assert.assertTrue(status);
        status = jdbcFilmDAO.rateFilm(film.getId(), 3);
        Assert.assertTrue(status);
        status = jdbcFilmDAO.rateFilm(film.getId(), 3);
        Assert.assertTrue(status);
        status = jdbcFilmDAO.rateFilm(film.getId(), 2);
        Assert.assertTrue(status);
        status = jdbcFilmDAO.rateFilm(film.getId(), 1);
        Assert.assertTrue(status);
    }

    /**
     * @see FilmDAO#rateFilm(int, int)
     */
    @Test(expected = RuntimeException.class)
    public void rateFilm_InvalidRating_Fail() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        jdbcFilmDAO.addFilm(film);
        jdbcFilmDAO.rateFilm(film.getId(), 9);
    }

    /**
     * @see FilmDAO#rateFilm(int, int)
     */
    @Test
    public void rateFilm_InvalidFilm_Fail() {
        boolean status = jdbcFilmDAO.rateFilm(0, 5);
        Assert.assertFalse(status);
    }

    /**
     * @see FilmDAO#getFilmById(int)
     */
    @Test
    public void getFilmById_ValidId_Success() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        jdbcFilmDAO.addFilm(film);

        Film actualFilm = jdbcFilmDAO.getFilmById(film.getId());
        Assert.assertEquals(film, actualFilm);
    }

    /**
     * @see FilmDAO#getFilmById(int)
     */
    @Test(expected = RuntimeException.class)
    public void getFilmById_InvalidId_Fail() {
        jdbcFilmDAO.getFilmById(-1);
    }

    /**
     * @see FilmDAO#getFilmsList(int, int)
     */
    @Test
    public void getFilmsList_ValidInput_Success() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        for (int i = 0; i < 7; i++) {
            jdbcFilmDAO.addFilm(film);
        }

        List<Film> list = jdbcFilmDAO.getFilmsList(0, 12);
        Assert.assertEquals(7, list.size());

        Film actualFilm = jdbcFilmDAO.getFilmsList(0, 12).get(6);
        Assert.assertEquals(film, actualFilm);

        for (int i = 0; i < 13; i++) {
            jdbcFilmDAO.addFilm(film);
        }

        list = jdbcFilmDAO.getFilmsList(12, 12);
        Assert.assertEquals(20 - 12, list.size());
    }

    /**
     * @see FilmDAO#getFilmsList(int, int)
     */
    @Test
    public void getFilmsList_EmptyList_Success() {
        List<Film> list = jdbcFilmDAO.getFilmsList(0, 12);
        Assert.assertTrue(list.isEmpty());
    }

    /**
     * @see FilmDAO#getFilmsList(int, int)
     */
    @Test
    public void getFilmsList_BadIndex_Fail() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        jdbcFilmDAO.addFilm(film);

        List<Film> films = jdbcFilmDAO.getFilmsList(1, 12);
        Assert.assertTrue(films.isEmpty());
    }

    /**
     * @see FilmDAO#getFilmsByGenre(am.aca.orgflix.entity.Genre)
     */
    @Test
    public void getFilmsByGenre_ValidInput_Success() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        film.addGeners(Genre.COMEDY);
        jdbcFilmDAO.addFilm(film);
        jdbcFilmDAO.addGenreToFilm(Genre.COMEDY, film.getId());
        film.setTitle("Deadpool");
        film.setProdYear(2016);
        jdbcFilmDAO.addFilm(film);
        jdbcFilmDAO.addGenreToFilm(Genre.COMEDY, film.getId());

        List<Film> comedyFilms = jdbcFilmDAO.getFilmsByGenre(Genre.COMEDY);
        Assert.assertEquals(2, comedyFilms.size());

        List<Film> warFilms = jdbcFilmDAO.getFilmsByGenre(Genre.WAR);
        Assert.assertTrue(warFilms.isEmpty());
    }

    /**
     * @see FilmDAO#getFilmsByGenre(am.aca.orgflix.entity.Genre)
     */
    @Test(expected = RuntimeException.class)
    public void getFilmsByGenre_InvalidGenre_Fail() {
        jdbcFilmDAO.getFilmsByGenre(Genre.getByTitle("invalid"));
    }

    /**
     * @see FilmDAO#getFilmsByCast(int)
     */
    @Test
    public void getFilmsByCast_ValidInput_Success() {
        cast = new Cast("Viggo Mortensen", false);
        jdbcCastDAO.addCast(cast);

        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        jdbcFilmDAO.addFilm(film);


        jdbcCastDAO.addCastToFilm(cast, film.getId());

        List<Film> actualFilms = jdbcFilmDAO.getFilmsByCast(cast.getId());
        Assert.assertEquals(1, actualFilms.size());

        Assert.assertEquals(film, actualFilms.get(0));

        film = new Film("Lord of the Rings", 2001);
        jdbcFilmDAO.addFilm(film);
        jdbcCastDAO.addCastToFilm(cast, film.getId());

        actualFilms = jdbcFilmDAO.getFilmsByCast(cast.getId());
        Assert.assertEquals(2, actualFilms.size());
    }

    /**
     * @see FilmDAO#getFilmsByCast(int)
     */
    @Test
    public void getFilmsByCast_Empty() {
        List<Film> films = jdbcFilmDAO.getFilmsByCast(0);
        Assert.assertTrue(films.isEmpty());
    }

    /**
     * @see FilmDAO#getRating(int, int)
     */
    @Test
    public void getRating_ValidInput_Success() {
        film = new Film("Captain Fantastic", 2016);
        jdbcFilmDAO.addFilm(film);
        for (int i = 0; i < 20; i++) {
            jdbcFilmDAO.rateFilm(film.getId(), 4);
        }

        int ratings = jdbcFilmDAO.getRating(film.getId(), 4);
        Assert.assertEquals(20, ratings);

        jdbcFilmDAO.rateFilm(film.getId(), 3);

        ratings = jdbcFilmDAO.getRating(film.getId(), 3);
        Assert.assertEquals(1, ratings);

        jdbcFilmDAO.rateFilm(film.getId(), 5);

        ratings = jdbcFilmDAO.getRating(film.getId(), 5);
        Assert.assertEquals(1, ratings);

        ratings = jdbcFilmDAO.getRating(film.getId(), 2);
        Assert.assertEquals(0, ratings);

        ratings = jdbcFilmDAO.getRating(film.getId(), 1);
        Assert.assertEquals(0, ratings);
    }

    /**
     * @see FilmDAO#getRating(int, int)
     */
    @Test(expected = RuntimeException.class)
    public void getRating_InvalidRating_Fail() {
        jdbcFilmDAO.getRating(film.getId(), 9);
    }

    /**
     * @see FilmDAO#getRating(int)
     */
    @Test
    public void getRating_Int_Success() {
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

    /**
     * @see FilmDAO#getRating(int)
     */
    @Test
    public void getRating_Double_Success() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        jdbcFilmDAO.addFilm(film);
        jdbcFilmDAO.rateFilm(film.getId(), 4);
        jdbcFilmDAO.rateFilm(film.getId(), 5);

        double rating = jdbcFilmDAO.getRating(film.getId());
        Assert.assertEquals(4.5, rating, 0.01);
    }

    /**
     * @see FilmDAO#getRating(int)
     */
    @Test
    public void getRating_NaN_Fail() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        jdbcFilmDAO.addFilm(film);

        double rating = jdbcFilmDAO.getRating(film.getId());
        Assert.assertEquals(0, rating, 0.01);
    }

    /**
     * @see FilmDAO#totalNumberOfFilms()
     */
    @Test
    public void totalNumber_ValidInput_Success() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        for (int i = 0; i < 6; i++) {
            jdbcFilmDAO.addFilm(film);
        }

        int size = jdbcFilmDAO.totalNumberOfFilms();
        Assert.assertEquals(6, size);
    }

    /**
     * @see FilmDAO#totalNumberOfFilms()
     */
    @Test
    public void totalNumber_Empty() {
        int size = jdbcFilmDAO.totalNumberOfFilms();
        Assert.assertEquals(0, size);
    }

    /**
     * @see FilmDAO#getFilteredFilms(String, int, int, boolean, String, String, int)
     */
    @Test
    public void searchFilter_ValidInputs_Success() {
        film.setTitle("The Lost City of Z");
        film.setProdYear(2016);
        film.setDirector("James Gray");
        jdbcFilmDAO.addFilm(film);
        jdbcFilmDAO.addGenreToFilm(Genre.ADVENTURE, film.getId());
        jdbcFilmDAO.addGenreToFilm(Genre.BIOGRAPHY, film.getId());
        cast = new Cast("Charlie Hunnam");
        jdbcCastDAO.addCast(cast);
        jdbcCastDAO.addCastToFilm(cast, film.getId());

        film = new Film();
        film.setTitle("City of God");
        film.setProdYear(2002);
        film.setDirector("Fernando Meirelles");
        jdbcFilmDAO.addFilm(film);
        jdbcFilmDAO.addGenreToFilm(Genre.DRAMA, film.getId());

        film = new Film();
        film.setTitle("7");
        film.setProdYear(1995);
        film.setDirector("David Fincher");
        jdbcFilmDAO.addFilm(film);
        jdbcFilmDAO.addGenreToFilm(Genre.DRAMA, film.getId());
        jdbcFilmDAO.addGenreToFilm(Genre.CRIME, film.getId());
        jdbcFilmDAO.addGenreToFilm(Genre.THRILLER, film.getId());
        cast = new Cast("Brad Pitt");
        jdbcCastDAO.addCast(cast);
        jdbcCastDAO.addCastToFilm(cast, film.getId());

        film = new Film();
        film.setTitle("Fight Club");
        film.setProdYear(1997);
        film.setDirector("David Fincher");
        jdbcFilmDAO.addFilm(film);
        jdbcFilmDAO.addGenreToFilm(Genre.THRILLER, film.getId());
        jdbcFilmDAO.addGenreToFilm(Genre.MYSTERY, film.getId());
        jdbcCastDAO.addCastToFilm(cast, film.getId());

        film = new Film();
        film.setTitle("Crash");
        film.setProdYear(2004);
        film.setHasOscar(true);
        film.setDirector("Paul Haggis");
        jdbcFilmDAO.addFilm(film);
        jdbcFilmDAO.addGenreToFilm(Genre.CRIME, film.getId());
        jdbcFilmDAO.addGenreToFilm(Genre.DRAMA, film.getId());
        jdbcFilmDAO.addGenreToFilm(Genre.THRILLER, film.getId());
        cast = new Cast("Sandra Bullock", true);
        jdbcCastDAO.addCast(cast);
        jdbcCastDAO.addCastToFilm(cast, film.getId());

        film = new Film();
        film.setTitle("Gone Girl");
        film.setProdYear(2014);
        film.setDirector("David Fincher");
        jdbcFilmDAO.addFilm(film);
        jdbcFilmDAO.addGenreToFilm(Genre.CRIME, film.getId());
        jdbcFilmDAO.addGenreToFilm(Genre.DRAMA, film.getId());
        jdbcFilmDAO.addGenreToFilm(Genre.MYSTERY, film.getId());
        cast = new Cast("Rosamund Pike");
        jdbcCastDAO.addCast(cast);
        jdbcCastDAO.addCastToFilm(cast, film.getId());

        //filters by nothing
        List<Film> filteredFilms = jdbcFilmDAO.getFilteredFilms("", 0, 0, false, null, null, 0);
        Assert.assertEquals(6, filteredFilms.size());

        //filters by title only
        filteredFilms = jdbcFilmDAO.getFilteredFilms("city of", 0, 0, false, null, null, 0);
        Assert.assertEquals(2, filteredFilms.size());

        //filters by production year constraints only
        filteredFilms = jdbcFilmDAO.getFilteredFilms("", 2000, 0, false, null, null, 0);
        Assert.assertEquals(4, filteredFilms.size());

        //filters by oscar wins only
        filteredFilms = jdbcFilmDAO.getFilteredFilms("", 0, 0, true, null, null, 0);
        Assert.assertEquals(1, filteredFilms.size());

        //filters by director only
        filteredFilms = jdbcFilmDAO.getFilteredFilms("", 0, 0, false, "david fincher", null, 0);
        Assert.assertEquals(3, filteredFilms.size());

        //filters by cast only
        filteredFilms = jdbcFilmDAO.getFilteredFilms("", 0, 0, false, null, "brad pitt", 0);
        Assert.assertEquals(2, filteredFilms.size());

        //filters by genre only
        filteredFilms = jdbcFilmDAO.getFilteredFilms("", 0, 0, false, null, null, Genre.DRAMA.getValue());
        Assert.assertEquals(4, filteredFilms.size());

        //filters by everything
        filteredFilms = jdbcFilmDAO.getFilteredFilms("gone girl", 2014, 2014, false, "david fincher", "rosamund pike", Genre.MYSTERY.getValue());
        Assert.assertEquals(1, filteredFilms.size());
        Assert.assertEquals(film, filteredFilms.get(0));
    }

    /**
     * @see FilmDAO#getFilteredFilms(String, int, int, boolean, String, String, int)
     */
    @Test
    public void getFilteredFilms_Empty_Success() {
        List<Film> filteredFilms = jdbcFilmDAO.getFilteredFilms("gone girl", 2014, 2014, false, "david fincher", "rosamund pike", Genre.MYSTERY.getValue());
        Assert.assertTrue(filteredFilms.isEmpty());
    }

    /**
     * @see FilmDAO#editFilm(am.aca.orgflix.entity.Film)
     */
    @Test
    public void editFilm_ValidInput_Success() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        film.setDirector("Matthew Ross");
        jdbcFilmDAO.addFilm(film);
        film.setHasOscar(true);

        boolean status = jdbcFilmDAO.editFilm(film);
        assertTrue(status);

        int size = jdbcFilmDAO.totalNumberOfFilms();
        Assert.assertEquals(1, size);
    }

    /**
     * @see FilmDAO#editFilm(am.aca.orgflix.entity.Film)
     */
    @Test(expected = RuntimeException.class)
    public void editFilm_InvalidTitle_Fail() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        film.setDirector("Matthew Ross");
        jdbcFilmDAO.addFilm(film);
        film.setTitle(null);
        jdbcFilmDAO.editFilm(film);
    }

    /**
     * @see FilmDAO#editFilm(am.aca.orgflix.entity.Film)
     */
    @Test
    public void editFilm_InvalidFilm_Fail() {
        boolean status = jdbcFilmDAO.editFilm(film);
        Assert.assertFalse(status);
    }

    /**
     * @see FilmDAO#resetRelationCasts(am.aca.orgflix.entity.Film)
     */
    @Test
    public void resetCasts_ValidInput_Success() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        jdbcFilmDAO.addFilm(film);

        cast = new Cast("Viggo Mortensen");
        jdbcCastDAO.addCast(cast);

        jdbcCastDAO.addCastToFilm(cast, film.getId());

        boolean status = jdbcFilmDAO.resetRelationCasts(film);
        Assert.assertTrue(status);

        int size = jdbcFilmDAO.getFilmsByCast(cast.getId()).size();
        Assert.assertEquals(0, size);
    }

    /**
     * @see FilmDAO#resetRelationCasts(am.aca.orgflix.entity.Film)
     */
    @Test
    public void resetCasts_KeepOtherCasts_Success() {
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

    /**
     * @see FilmDAO#resetRelationCasts(am.aca.orgflix.entity.Film)
     */
    @Test
    public void resetCasts_NoCasts_Fail() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        jdbcFilmDAO.addFilm(film);

        boolean status = jdbcFilmDAO.resetRelationCasts(film);
        Assert.assertFalse(status);
    }

    /**
     * @see FilmDAO#resetRelationCasts(am.aca.orgflix.entity.Film)
     */
    @Test
    public void resetCasts_InvalidFilm_Fail() {
        boolean status = jdbcFilmDAO.resetRelationCasts(film);
        Assert.assertFalse(status);
    }

    /**
     * @see FilmDAO#resetRelationGenres(am.aca.orgflix.entity.Film)
     */
    @Test
    public void resetGenres_ValidInput_Success() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        jdbcFilmDAO.addFilm(film);
        jdbcFilmDAO.addGenreToFilm(Genre.DRAMA, film.getId());

        boolean status = jdbcFilmDAO.resetRelationGenres(film);
        Assert.assertTrue(status);

        List<Film> actualFilms = jdbcFilmDAO.getFilmsByGenre(Genre.DRAMA);
        Assert.assertTrue(actualFilms.isEmpty());
    }

    /**
     * @see FilmDAO#resetRelationGenres(am.aca.orgflix.entity.Film)
     */
    @Test
    public void resetGenres_KeepsOtherGenres_Success() {
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

    /**
     * @see FilmDAO#resetRelationGenres(am.aca.orgflix.entity.Film)
     */
    @Test
    public void resetGenres_InvalidGenre_Fail() {
        film.setTitle("City of God");
        film.setProdYear(2002);
        jdbcFilmDAO.addFilm(film);

        boolean status = jdbcFilmDAO.resetRelationGenres(film);
        Assert.assertFalse(status);
    }

    /**
     * @see FilmDAO#resetRelationGenres(am.aca.orgflix.entity.Film)
     */
    @Test
    public void resetGenres_InvalidFilm_Fail() {
        boolean status = jdbcFilmDAO.resetRelationGenres(film);
        Assert.assertFalse(status);
    }

    /**
     * @see FilmDAO#remove(am.aca.orgflix.entity.Film)
     */
    @Test
    public void remove_ValidFilm_Success() {
        film.setTitle("City of God");
        film.setProdYear(2002);
        jdbcFilmDAO.addFilm(film);
        boolean status = jdbcFilmDAO.remove(film);
        Assert.assertTrue(status);

        int size = jdbcFilmDAO.totalNumberOfFilms();
        Assert.assertEquals(0, size);
    }

    /**
     * @see FilmDAO#remove(am.aca.orgflix.entity.Film)
     */
    @Test
    public void remove_Fail() {
        film.setTitle("City of God");
        film.setProdYear(2002);
        jdbcFilmDAO.addFilm(film);
        film = new Film();
        boolean status = jdbcFilmDAO.remove(film);
        Assert.assertFalse(status);

        int size = jdbcFilmDAO.totalNumberOfFilms();
        Assert.assertEquals(1, size);
    }
}