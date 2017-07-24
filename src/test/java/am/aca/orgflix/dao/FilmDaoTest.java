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
     * @see FilmDAO#add(am.aca.orgflix.entity.Film)
     */
    @Test
    public void addFilm_ValidFilm_Success() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        jdbcFilmDAO.add(film);

        int id = film.getId();
        Assert.assertTrue(id > 0);

        int size = jdbcFilmDAO.getAll(0, 12).size();
        Assert.assertEquals(1, size);

        Film actualFilm = jdbcFilmDAO.getById(film.getId());
        Assert.assertEquals(film, actualFilm);
    }

    /**
     * @see FilmDAO#add(am.aca.orgflix.entity.Film)
     */
    @Test
    public void addFilm_InvalidFilm_Fail() {
        try {
            jdbcFilmDAO.add(film);
        } catch (RuntimeException e) {
            Assert.assertTrue(e instanceof RuntimeException);
        }
    }

    /**
     * @see FilmDAO#addGenreToFilm(am.aca.orgflix.entity.Genre, int)
     */
    @Test
    public void addGenreToFilm_ValidInput_Success() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        jdbcFilmDAO.add(film);

        boolean status = jdbcFilmDAO.addGenreToFilm(Genre.COMEDY, film.getId());
        Assert.assertTrue(status);

        int size = jdbcFilmDAO.getByGenre(Genre.COMEDY).size();
        Assert.assertEquals(1, size);

        Film actualFilm = jdbcFilmDAO.getByGenre(Genre.COMEDY).get(0);
        Assert.assertEquals(film, actualFilm);
    }

    /**
     * @see FilmDAO#addGenreToFilm(am.aca.orgflix.entity.Genre, int)
     */
    @Test
    public void addGenreToFilm_SameAssociationTwice_Fail() {
        try {
            film.setTitle("Captain Fantastic");
            film.setProdYear(2016);
            jdbcFilmDAO.add(film);
            jdbcFilmDAO.addGenreToFilm(Genre.COMEDY, film.getId());
            jdbcFilmDAO.addGenreToFilm(Genre.COMEDY, film.getId());
        } catch (RuntimeException e) {
            Assert.assertTrue(e instanceof RuntimeException);
        }
    }

    /**
     * @see FilmDAO#addGenreToFilm(am.aca.orgflix.entity.Genre, int)
     */
    @Test
    public void addGenreToFilm_InvalidFilm_Fail() {
        try {
            jdbcFilmDAO.addGenreToFilm(Genre.COMEDY, film.getId());
        } catch (RuntimeException e) {
            Assert.assertTrue(e instanceof RuntimeException);
        }
    }

    /**
     * @see FilmDAO#rate(int, int)
     */
    @Test
    public void rateFilm_ValidInput_Success() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        jdbcFilmDAO.add(film);
        boolean status;

        status = jdbcFilmDAO.rate(film.getId(), 5);
        Assert.assertTrue(status);
        status = jdbcFilmDAO.rate(film.getId(), 5);
        Assert.assertTrue(status);

        int size = jdbcFilmDAO.getById(film.getId()).getRate_5star();
        Assert.assertEquals(2, size);

        status = jdbcFilmDAO.rate(film.getId(), 4);
        Assert.assertTrue(status);
        status = jdbcFilmDAO.rate(film.getId(), 3);
        Assert.assertTrue(status);
        status = jdbcFilmDAO.rate(film.getId(), 3);
        Assert.assertTrue(status);
        status = jdbcFilmDAO.rate(film.getId(), 2);
        Assert.assertTrue(status);
        status = jdbcFilmDAO.rate(film.getId(), 1);
        Assert.assertTrue(status);
    }

    /**
     * @see FilmDAO#rate(int, int)
     */
    @Test
    public void rateFilm_InvalidRating_Fail() {
        try {
            film.setTitle("Captain Fantastic");
            film.setProdYear(2016);
            jdbcFilmDAO.add(film);
            jdbcFilmDAO.rate(film.getId(), 9);
        } catch (RuntimeException e) {
            Assert.assertTrue(e instanceof RuntimeException);
        }
    }

    /**
     * @see FilmDAO#rate(int, int)
     */
    @Test
    public void rateFilm_InvalidFilm_Fail() {
        boolean status = jdbcFilmDAO.rate(0, 5);
        Assert.assertFalse(status);
    }

    /**
     * @see FilmDAO#getById(int)
     */
    @Test
    public void getFilmById_ValidId_Success() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        jdbcFilmDAO.add(film);

        Film actualFilm = jdbcFilmDAO.getById(film.getId());
        Assert.assertEquals(film, actualFilm);
    }

    /**
     * @see FilmDAO#getById(int)
     */
    @Test
    public void getFilmById_InvalidId_Fail() {
        try {
            jdbcFilmDAO.getById(-1);
        } catch (RuntimeException e) {
            Assert.assertTrue(e instanceof RuntimeException);
        }
    }

    /**
     * @see FilmDAO#getAll(int, int)
     */
    @Test
    public void getFilmsList_ValidInput_Success() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        for (int i = 0; i < 7; i++) {
            jdbcFilmDAO.add(film);
        }

        List<Film> list = jdbcFilmDAO.getAll(0, 12);
        Assert.assertEquals(7, list.size());

        Film actualFilm = jdbcFilmDAO.getAll(0, 12).get(6);
        Assert.assertEquals(film, actualFilm);

        for (int i = 0; i < 13; i++) {
            jdbcFilmDAO.add(film);
        }

        list = jdbcFilmDAO.getAll(12, 12);
        Assert.assertEquals(20 - 12, list.size());
    }

    /**
     * @see FilmDAO#getAll(int, int)
     */
    @Test
    public void getFilmsList_EmptyList_Success() {
        List<Film> list = jdbcFilmDAO.getAll(0, 12);
        Assert.assertTrue(list.isEmpty());
    }

    /**
     * @see FilmDAO#getAll(int, int)
     */
    @Test
    public void getFilmsList_BadIndex_Fail() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        jdbcFilmDAO.add(film);

        List<Film> films = jdbcFilmDAO.getAll(1, 12);
        Assert.assertTrue(films.isEmpty());
    }

    /**
     * @see FilmDAO#getByGenre(am.aca.orgflix.entity.Genre)
     */
    @Test
    public void getFilmsByGenre_ValidInput_Success() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        film.addGeners(Genre.COMEDY);
        jdbcFilmDAO.add(film);
        jdbcFilmDAO.addGenreToFilm(Genre.COMEDY, film.getId());
        film.setTitle("Deadpool");
        film.setProdYear(2016);
        jdbcFilmDAO.add(film);
        jdbcFilmDAO.addGenreToFilm(Genre.COMEDY, film.getId());

        List<Film> comedyFilms = jdbcFilmDAO.getByGenre(Genre.COMEDY);
        Assert.assertEquals(2, comedyFilms.size());

        List<Film> warFilms = jdbcFilmDAO.getByGenre(Genre.WAR);
        Assert.assertTrue(warFilms.isEmpty());
    }

    /**
     * @see FilmDAO#getByGenre(am.aca.orgflix.entity.Genre)
     */
    @Test
    public void getFilmsByGenre_InvalidGenre_Fail() {
        try {
            jdbcFilmDAO.getByGenre(Genre.getByTitle("invalid"));
        } catch (RuntimeException e) {
            Assert.assertTrue(e instanceof RuntimeException);
        }
    }

    /**
     * @see FilmDAO#getByCast(int)
     */
    @Test
    public void getFilmsByCast_ValidInput_Success() {
        cast = new Cast("Viggo Mortensen", false);
        jdbcCastDAO.add(cast);

        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        jdbcFilmDAO.add(film);


        jdbcCastDAO.addToFilm(cast, film.getId());

        List<Film> actualFilms = jdbcFilmDAO.getByCast(cast.getId());
        Assert.assertEquals(1, actualFilms.size());

        Assert.assertEquals(film, actualFilms.get(0));

        film = new Film("Lord of the Rings", 2001);
        jdbcFilmDAO.add(film);
        jdbcCastDAO.addToFilm(cast, film.getId());

        actualFilms = jdbcFilmDAO.getByCast(cast.getId());
        Assert.assertEquals(2, actualFilms.size());
    }

    /**
     * @see FilmDAO#getByCast(int)
     */
    @Test
    public void getFilmsByCast_Empty() {
        List<Film> films = jdbcFilmDAO.getByCast(0);
        Assert.assertTrue(films.isEmpty());
    }

    /**
     * @see FilmDAO#getRating(int)
     */
    @Test
    public void getRating_Int_Success() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        jdbcFilmDAO.add(film);
        jdbcFilmDAO.rate(film.getId(), 4);
        jdbcFilmDAO.rate(film.getId(), 4);
        jdbcFilmDAO.rate(film.getId(), 3);
        jdbcFilmDAO.rate(film.getId(), 5);

        int[] ratings = jdbcFilmDAO.getRating(film.getId());
        Assert.assertEquals(0, ratings[0]);
        Assert.assertEquals(0, ratings[1]);
        Assert.assertEquals(1, ratings[2]);
        Assert.assertEquals(2, ratings[3]);
        Assert.assertEquals(1, ratings[4]);
    }

    /**
     * @see FilmDAO#getRating(int)
     */
    @Test
    public void getRating_InvalidFilm_Fail() {
        int[] ratings = jdbcFilmDAO.getRating(0);

        Assert.assertEquals(0, ratings[0]);
        Assert.assertEquals(0, ratings[1]);
        Assert.assertEquals(0, ratings[2]);
        Assert.assertEquals(0, ratings[3]);
        Assert.assertEquals(0, ratings[4]);
    }

    /**
     * @see FilmDAO#getTotalNumber()
     */
    @Test
    public void totalNumber_ValidInput_Success() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        for (int i = 0; i < 6; i++) {
            jdbcFilmDAO.add(film);
        }

        int size = jdbcFilmDAO.getTotalNumber();
        Assert.assertEquals(6, size);
    }

    /**
     * @see FilmDAO#getTotalNumber()
     */
    @Test
    public void totalNumber_Empty() {
        int size = jdbcFilmDAO.getTotalNumber();
        Assert.assertEquals(0, size);
    }

    /**
     * @see FilmDAO#getFilteredFilms(String, int, int, boolean, String, int, int)
     */
    @Test
    public void searchFilter_ValidInputs_Success() {
        film.setTitle("The Lost City of Z");
        film.setProdYear(2016);
        film.setDirector("James Gray");
        jdbcFilmDAO.add(film);
        jdbcFilmDAO.addGenreToFilm(Genre.ADVENTURE, film.getId());
        jdbcFilmDAO.addGenreToFilm(Genre.BIOGRAPHY, film.getId());
        cast = new Cast("Charlie Hunnam");
        jdbcCastDAO.add(cast);
        jdbcCastDAO.addToFilm(cast, film.getId());

        film = new Film();
        film.setTitle("City of God");
        film.setProdYear(2002);
        film.setDirector("Fernando Meirelles");
        jdbcFilmDAO.add(film);
        jdbcFilmDAO.addGenreToFilm(Genre.DRAMA, film.getId());

        film = new Film();
        film.setTitle("7");
        film.setProdYear(1995);
        film.setDirector("David Fincher");
        jdbcFilmDAO.add(film);
        jdbcFilmDAO.addGenreToFilm(Genre.DRAMA, film.getId());
        jdbcFilmDAO.addGenreToFilm(Genre.CRIME, film.getId());
        jdbcFilmDAO.addGenreToFilm(Genre.THRILLER, film.getId());
        cast = new Cast("Brad Pitt");
        jdbcCastDAO.add(cast);
        int id = cast.getId();
        jdbcCastDAO.addToFilm(cast, film.getId());

        film = new Film();
        film.setTitle("Fight Club");
        film.setProdYear(1997);
        film.setDirector("David Fincher");
        jdbcFilmDAO.add(film);
        jdbcFilmDAO.addGenreToFilm(Genre.THRILLER, film.getId());
        jdbcFilmDAO.addGenreToFilm(Genre.MYSTERY, film.getId());
        jdbcCastDAO.addToFilm(cast, film.getId());

        film = new Film();
        film.setTitle("Crash");
        film.setProdYear(2004);
        film.setHasOscar(true);
        film.setDirector("Paul Haggis");
        jdbcFilmDAO.add(film);
        jdbcFilmDAO.addGenreToFilm(Genre.CRIME, film.getId());
        jdbcFilmDAO.addGenreToFilm(Genre.DRAMA, film.getId());
        jdbcFilmDAO.addGenreToFilm(Genre.THRILLER, film.getId());
        cast = new Cast("Sandra Bullock", true);
        jdbcCastDAO.add(cast);
        jdbcCastDAO.addToFilm(cast, film.getId());

        film = new Film();
        film.setTitle("Gone Girl");
        film.setProdYear(2014);
        film.setDirector("David Fincher");
        jdbcFilmDAO.add(film);
        jdbcFilmDAO.addGenreToFilm(Genre.CRIME, film.getId());
        jdbcFilmDAO.addGenreToFilm(Genre.DRAMA, film.getId());
        jdbcFilmDAO.addGenreToFilm(Genre.MYSTERY, film.getId());
        cast = new Cast("Rosamund Pike");
        jdbcCastDAO.add(cast);
        jdbcCastDAO.addToFilm(cast, film.getId());

        //filters by nothing
        List<Film> filteredFilms = jdbcFilmDAO.getFilteredFilms("", 0, 0, false, "", 0, 0);
        Assert.assertEquals(6, filteredFilms.size());

        //filters by title only
        filteredFilms = jdbcFilmDAO.getFilteredFilms("city of", 0, 0, false, "", 0, 0);
        Assert.assertEquals(2, filteredFilms.size());

        //filters by production year constraints only
        filteredFilms = jdbcFilmDAO.getFilteredFilms("", 2000, 0, false, "", 0, 0);
        Assert.assertEquals(4, filteredFilms.size());

        //filters by oscar wins only
        filteredFilms = jdbcFilmDAO.getFilteredFilms("", 0, 0, true, "", 0, 0);
        Assert.assertEquals(1, filteredFilms.size());

        //filters by director only
        filteredFilms = jdbcFilmDAO.getFilteredFilms("", 0, 0, false, "david fincher", 0, 0);
        Assert.assertEquals(3, filteredFilms.size());

        //filters by cast only
        filteredFilms = jdbcFilmDAO.getFilteredFilms("", 0, 0, false, "", id, 0);
        Assert.assertEquals(2, filteredFilms.size());

        //filters by genre only
        filteredFilms = jdbcFilmDAO.getFilteredFilms("", 0, 0, false, "", 0, Genre.DRAMA.getValue());
        Assert.assertEquals(4, filteredFilms.size());

        //filters by everything
        filteredFilms = jdbcFilmDAO.getFilteredFilms("gone girl", 2014, 2014, false, "david fincher", cast.getId(), Genre.MYSTERY.getValue());
        Assert.assertEquals(1, filteredFilms.size());
        Assert.assertEquals(film, filteredFilms.get(0));
    }

    /**
     * @see FilmDAO#getFilteredFilms(String, int, int, boolean, String, int, int)
     */
    @Test
    public void getFilteredFilms_Empty_Success() {
        List<Film> filteredFilms = jdbcFilmDAO.getFilteredFilms("gone girl", 2014, 2014, false, "david fincher", 1, Genre.MYSTERY.getValue());
        Assert.assertTrue(filteredFilms.isEmpty());
    }

    /**
     * @see FilmDAO#edit(am.aca.orgflix.entity.Film)
     */
    @Test
    public void editFilm_ValidInput_Success() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        film.setDirector("Matthew Ross");
        jdbcFilmDAO.add(film);
        film.setHasOscar(true);

        boolean status = jdbcFilmDAO.edit(film);
        assertTrue(status);

        int size = jdbcFilmDAO.getTotalNumber();
        Assert.assertEquals(1, size);
    }

    /**
     * @see FilmDAO#edit(am.aca.orgflix.entity.Film)
     */
    @Test(expected = RuntimeException.class)
    public void editFilm_InvalidTitle_Fail() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        film.setDirector("Matthew Ross");
        jdbcFilmDAO.add(film);
        film.setTitle(null);
        jdbcFilmDAO.edit(film);
    }

    /**
     * @see FilmDAO#edit(am.aca.orgflix.entity.Film)
     */
    @Test
    public void editFilm_InvalidFilm_Fail() {
        boolean status = jdbcFilmDAO.edit(film);
        Assert.assertFalse(status);
    }

    /**
     * @see FilmDAO#removeCasts(am.aca.orgflix.entity.Film)
     */
    @Test
    public void resetCasts_ValidInput_Success() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        jdbcFilmDAO.add(film);

        cast = new Cast("Viggo Mortensen");
        jdbcCastDAO.add(cast);

        jdbcCastDAO.addToFilm(cast, film.getId());

        boolean status = jdbcFilmDAO.removeCasts(film);
        Assert.assertTrue(status);

        int size = jdbcFilmDAO.getByCast(cast.getId()).size();
        Assert.assertEquals(0, size);
    }

    /**
     * @see FilmDAO#removeCasts(am.aca.orgflix.entity.Film)
     */
    @Test
    public void resetCasts_KeepOtherCasts_Success() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        jdbcFilmDAO.add(film);

        cast = new Cast("Viggo Mortensen", false);
        jdbcCastDAO.add(cast);
        jdbcCastDAO.addToFilm(cast, film.getId());

        jdbcFilmDAO.removeCasts(film);

        film.setTitle("City of God");
        film.setProdYear(2002);
        jdbcFilmDAO.add(film);

        cast = new Cast("Alice Braga", false);
        jdbcCastDAO.add(cast);
        jdbcCastDAO.addToFilm(cast, film.getId());

        Film actualFilm = jdbcFilmDAO.getByCast(cast.getId()).get(0);
        Assert.assertEquals(film, actualFilm);
    }

    /**
     * @see FilmDAO#removeCasts(am.aca.orgflix.entity.Film)
     */
    @Test
    public void resetCasts_NoCasts_Fail() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        jdbcFilmDAO.add(film);

        boolean status = jdbcFilmDAO.removeCasts(film);
        Assert.assertFalse(status);
    }

    /**
     * @see FilmDAO#removeCasts(am.aca.orgflix.entity.Film)
     */
    @Test
    public void resetCasts_InvalidFilm_Fail() {
        boolean status = jdbcFilmDAO.removeCasts(film);
        Assert.assertFalse(status);
    }

    /**
     * @see FilmDAO#removeGenres(am.aca.orgflix.entity.Film)
     */
    @Test
    public void resetGenres_ValidInput_Success() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        jdbcFilmDAO.add(film);
        jdbcFilmDAO.addGenreToFilm(Genre.DRAMA, film.getId());

        boolean status = jdbcFilmDAO.removeGenres(film);
        Assert.assertTrue(status);

        List<Film> actualFilms = jdbcFilmDAO.getByGenre(Genre.DRAMA);
        Assert.assertTrue(actualFilms.isEmpty());
    }

    /**
     * @see FilmDAO#removeGenres(am.aca.orgflix.entity.Film)
     */
    @Test
    public void resetGenres_KeepsOtherGenres_Success() {
        film.setTitle("American History X");
        film.setProdYear(1998);
        jdbcFilmDAO.add(film);
        jdbcFilmDAO.addGenreToFilm(Genre.CRIME, film.getId());
        jdbcFilmDAO.addGenreToFilm(Genre.DRAMA, film.getId());
        jdbcFilmDAO.removeGenres(film);

        film.setTitle("City of God");
        film.setProdYear(2002);
        jdbcFilmDAO.add(film);
        jdbcFilmDAO.addGenreToFilm(Genre.CRIME, film.getId());

        Film actualFilm = jdbcFilmDAO.getByGenre(Genre.CRIME).get(0);
        Assert.assertEquals(film, actualFilm);
    }

    /**
     * @see FilmDAO#removeGenres(am.aca.orgflix.entity.Film)
     */
    @Test
    public void resetGenres_InvalidGenre_Fail() {
        film.setTitle("City of God");
        film.setProdYear(2002);
        jdbcFilmDAO.add(film);

        boolean status = jdbcFilmDAO.removeGenres(film);
        Assert.assertFalse(status);
    }

    /**
     * @see FilmDAO#removeGenres(am.aca.orgflix.entity.Film)
     */
    @Test
    public void resetGenres_InvalidFilm_Fail() {
        boolean status = jdbcFilmDAO.removeGenres(film);
        Assert.assertFalse(status);
    }

    /**
     * @see FilmDAO#remove(am.aca.orgflix.entity.Film)
     */
    @Test
    public void remove_ValidFilm_Success() {
        film.setTitle("City of God");
        film.setProdYear(2002);
        jdbcFilmDAO.add(film);
        boolean status = jdbcFilmDAO.remove(film);
        Assert.assertTrue(status);

        int size = jdbcFilmDAO.getTotalNumber();
        Assert.assertEquals(0, size);
    }

    /**
     * @see FilmDAO#remove(am.aca.orgflix.entity.Film)
     */
    @Test
    public void remove_Fail() {
        film.setTitle("City of God");
        film.setProdYear(2002);
        jdbcFilmDAO.add(film);
        film = new Film();
        boolean status = jdbcFilmDAO.remove(film);
        Assert.assertFalse(status);

        int size = jdbcFilmDAO.getTotalNumber();
        Assert.assertEquals(1, size);
    }
}