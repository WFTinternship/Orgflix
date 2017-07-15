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

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

/**
 * Integration tests for film DAO methods
 */
public class FilmDaoTest extends BaseIntegrationTest {

    @Autowired
    private FilmDAO hibernateFilmDAO;

    @Autowired
    private TestHelper helper;

    private Film film = new Film();
    private Cast cast;

    /**
     * Rolls back all changes applied to the test DB resulted from tests
     */
    @After
    public void tearDown() {
        helper.emptyTable(new String[]{"genre_to_film", "film_to_cast", "films", "casts"});
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateFilmDAO#addFilm(am.aca.orgflix.entity.Film)
     */
    @Test
    public void addFilm_Valid_Success() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        hibernateFilmDAO.addFilm(film);

        boolean status = film.getId() > 0;
        int size = hibernateFilmDAO.getFilmsList(0).size();
        boolean positiveId = film.getId() > 0;

        Assert.assertTrue(status);
        Assert.assertEquals(1, size);
        Assert.assertTrue(positiveId);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateFilmDAO#addFilm(am.aca.orgflix.entity.Film)
     */
    @Test
    public void addFilm_Fail() {
        boolean status = hibernateFilmDAO.addFilm(film);
        Assert.assertFalse(status);
    }

//    /**
//     * @see am.aca.orgflix.dao.implHibernate.HibernateFilmDAO#addGenreToFilm(am.aca.orgflix.entity.Genre, int)
//     */
//    @Test
//    public void addGenreToFilm_BySize_Success() {
//        film.setTitle("Captain Fantastic");
//        film.setProdYear(2016);
//        hibernateFilmDAO.addFilm(film);
//
//        boolean status = hibernateFilmDAO.addGenreToFilm(Genre.COMEDY, film.getId());
//        Assert.assertTrue(status);
//
//        int size = hibernateFilmDAO.getFilmsByGenre(Genre.COMEDY).size();
//        Assert.assertEquals(1, size);
//    }
//
//    /**
//     * @see am.aca.orgflix.dao.implHibernate.HibernateFilmDAO#addGenreToFilm(am.aca.orgflix.entity.Genre, int)
//     */
//    @Test
//    public void addGenreToFilm_ByFilm_Success() {
//        film.setTitle("Captain Fantastic");
//        film.setProdYear(2016);
//        hibernateFilmDAO.addFilm(film);
//        hibernateFilmDAO.addGenreToFilm(Genre.COMEDY, film.getId());
//
//        Film actualFilm = hibernateFilmDAO.getFilmsByGenre(Genre.COMEDY).get(0);
//        Assert.assertEquals(film, actualFilm);
//    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateFilmDAO#rateFilm(int, int)
     */
    @Test
    public void rateFilm_ValidRating_Success() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        hibernateFilmDAO.addFilm(film);
        hibernateFilmDAO.rateFilm(film.getId(), 5);
        hibernateFilmDAO.rateFilm(film.getId(), 5);

        int size = hibernateFilmDAO.getFilmById(film.getId()).getRate_5star();
        Assert.assertEquals(2, size);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateFilmDAO#rateFilm(int, int)
     */
    @Test
    public void rateFilm_InvalidRating_Fail() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        hibernateFilmDAO.addFilm(film);

        boolean status = hibernateFilmDAO.rateFilm(film.getId(), 9);
        Assert.assertFalse(status);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateFilmDAO#getFilmById(int)
     */
    @Test
    public void getFilmById_ValidId_Success() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        hibernateFilmDAO.addFilm(film);

        Film actualFilm = hibernateFilmDAO.getFilmById(film.getId());
        Assert.assertEquals(film, actualFilm);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateFilmDAO#getFilmById(int)
     */
    @Test
    public void getFilmById_InvalidId_Fail() {
        Film actualFilm = hibernateFilmDAO.getFilmById(-1);
        Assert.assertEquals(null, actualFilm);
    }

    @Test
    public void getCasts_ValidFilm_Success() {
        cast = new Cast("Tom Hanks");
        Film film = new Film("Forrest Gump", 1990);
        ArrayList<Cast> list = new ArrayList<>();
        list.add(cast);
        film.setCasts(list);
        hibernateFilmDAO.addFilm(film);

        java.util.List<Cast> actualCast = hibernateFilmDAO.getCastsByFilm(film.getId());
        int size = actualCast.size();
        Cast actualActor = actualCast.get(0);

        Assert.assertEquals(1, size);
        Assert.assertEquals(cast, actualActor);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateFilmDAO#getCastsByFilm(int)
     */
    @Test
    public void getCasts_ValidFilmEmptyCast_Success() {
        Film film = new Film("Forest Gump", 1990);
        hibernateFilmDAO.addFilm(film);

        int size = hibernateFilmDAO.getCastsByFilm(film.getId()).size();
        Assert.assertEquals(0, size);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateFilmDAO#getCastsByFilm(int)
     */
    @Test
    public void getCasts_ValidFilmEmptyCast_Fail() {
        int size = hibernateFilmDAO.getCastsByFilm(film.getId()).size();
        Assert.assertEquals(0, size);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateFilmDAO#getFilmsList(int)
     */
    @Test
    public void getFilmsList_Page1And2_Success() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        for (int i = 0; i < 7; i++) {
            hibernateFilmDAO.addFilm(film);
        }

        int size = hibernateFilmDAO.getFilmsList(0).size();
        Assert.assertEquals(7, size);

        for (int i = 0; i < 20 - 7; i++) {
            hibernateFilmDAO.addFilm(film);
        }

        size = hibernateFilmDAO.getFilmsList(12).size();
        Assert.assertEquals(20 - 12, size);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateFilmDAO#getFilmsList(int)
     */
    @Test
    public void getFilmsList_EmptyList_Success() {
        int size = hibernateFilmDAO.getFilmsList(0).size();
        Assert.assertEquals(0, size);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateFilmDAO#getFilmsList(int)
     */
    @Test
    public void getFilmsList_BadIndex_Fail() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        hibernateFilmDAO.addFilm(film);

        int size = hibernateFilmDAO.getFilmsList(1).size();
        Assert.assertEquals(0, size);
    }

//    /**
//     * @see am.aca.orgflix.dao.implHibernate.HibernateFilmDAO#getFilmsByGenre(am.aca.orgflix.entity.Genre)
//     */
//    @Test
//    public void getFilmsByGenre_Size_Success() {
//        film.setTitle("Captain Fantastic");
//        film.setProdYear(2016);
//        hibernateFilmDAO.addFilm(film);
//        hibernateFilmDAO.addGenreToFilm(Genre.COMEDY, film.getId());
//        film.setTitle("Deadpool");
//        film.setProdYear(2016);
//        hibernateFilmDAO.addFilm(film);
//        hibernateFilmDAO.addGenreToFilm(Genre.COMEDY, film.getId());
//
//        int size = hibernateFilmDAO.getFilmsByGenre(Genre.COMEDY).size();
//        Assert.assertEquals(2, size);
//    }

//    /**
//     * @see am.aca.orgflix.dao.implHibernate.HibernateFilmDAO#getFilmsByGenre(am.aca.orgflix.entity.Genre)
//     */
//    @Test
//    public void getFilmsByGenre_Fail() {
//        int size = hibernateFilmDAO.getFilmsByGenre(Genre.WAR).size();
//        Assert.assertEquals(0, size);
//    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateFilmDAO#getFilmsByCast(int)
     */
    @Test
    public void getFilmsByCast_ValidCast_Success() {
        cast = new Cast("Viggo Mortensen", false);

        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        film.addCast(cast);
        hibernateFilmDAO.addFilm(film);

        Film actualFilm = hibernateFilmDAO.getFilmsByCast(cast.getId()).get(0);
        Assert.assertEquals(film, actualFilm);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateFilmDAO#getFilmsByCast(int)
     */
    @Test
    public void getFilmsByCast__InvalidCast_Fail() {
        int size = hibernateFilmDAO.getFilmsByCast(0).size();
        Assert.assertEquals(0, size);
    }

//    /**
//     * @see am.aca.orgflix.dao.implHibernate.HibernateFilmDAO#getRating(int)
//     */
//    @Test
//    public void getRating_Int_Success() {
//        film.setTitle("Captain Fantastic");
//        film.setProdYear(2016);
//        hibernateFilmDAO.addFilm(film);
//        hibernateFilmDAO.rateFilm(film.getId(), 4);
//        hibernateFilmDAO.rateFilm(film.getId(), 4);
//        hibernateFilmDAO.rateFilm(film.getId(), 3);
//        hibernateFilmDAO.rateFilm(film.getId(), 5);
//
//        double rating = hibernateFilmDAO.getRating(film.getId());
//        Assert.assertEquals(4.0, rating, 0.01);
//    }

//    /**
//     * @see am.aca.orgflix.dao.implHibernate.HibernateFilmDAO#getRating(int)
//     */
//    @Test
//    public void getRating_Double_Success() {
//        film.setTitle("Captain Fantastic");
//        film.setProdYear(2016);
//        hibernateFilmDAO.addFilm(film);
//        hibernateFilmDAO.rateFilm(film.getId(), 4);
//        hibernateFilmDAO.rateFilm(film.getId(), 5);
//
//        double rating = hibernateFilmDAO.getRating(film.getId());
//        Assert.assertEquals(4.5, rating, 0.01);
//    }

//    /**
//     * @see am.aca.orgflix.dao.implHibernate.HibernateFilmDAO#getRating(int)
//     */
//    @Test
//    public void getRating_NaN_Fail() {
//        film.setTitle("Captain Fantastic");
//        film.setProdYear(2016);
//        hibernateFilmDAO.addFilm(film);
//
//        double rating = hibernateFilmDAO.getRating(film.getId());
//        Assert.assertEquals(0, rating, 0.01);
//    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateFilmDAO#totalNumberOfFilms()
     */
    @Test
    public void totalNumber_Success() {
        film.setTitle("Captain Fantastic");
        film.setProdYear(2016);
        for (int i = 0; i < 6; i++) {
            hibernateFilmDAO.addFilm(film);
        }

        int size = hibernateFilmDAO.totalNumberOfFilms();
        Assert.assertEquals(6, size);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateFilmDAO#totalNumberOfFilms()
     */
    @Test
    public void totalNumber_Fail() {
        int size = hibernateFilmDAO.totalNumberOfFilms();
        Assert.assertEquals(0, size);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateFilmDAO#getFilteredFilms(String, int, int, boolean, String, int, int)
     */
    @Test
    public void searchFilter_TitleOnly_Success() {
        film.setTitle("The Lost City of Z");
        film.setProdYear(2016);
        hibernateFilmDAO.addFilm(film);

        film.setTitle("City of God");
        film.setProdYear(2002);
        hibernateFilmDAO.addFilm(film);

        int size = hibernateFilmDAO.getFilteredFilms
                ("The Lost City of Z", 0, 0, false, null, 0, 0).size();
        Assert.assertEquals(2, size);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateFilmDAO#getFilteredFilms(String, int, int, boolean, String, int, int)
     */
    @Test
    public void search_TitleOnlyNotExisting_Empty() {
        film.setTitle("City of God");
        film.setProdYear(2002);
        hibernateFilmDAO.addFilm(film);

        boolean status = hibernateFilmDAO.getFilteredFilms
                ("City of Angels", 0, 0, false, null, 0, 0).isEmpty();
        Assert.assertTrue(status);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateFilmDAO#getFilteredFilms(String, int, int, boolean, String, int, int)
     */
    @Test
    public void filter_ByCastOnly_Success() {
        film.setTitle("City of God");
        film.setProdYear(2002);
        cast = new Cast("Alice Braga", false);
        film.addCast(cast);
        hibernateFilmDAO.addFilm(film);

        Film actualFilm = hibernateFilmDAO.getFilteredFilms
                (null, 0, 0, false, null, cast.getId(), 0).get(0);
        Assert.assertEquals(film, actualFilm);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateFilmDAO#getFilteredFilms(String, int, int, boolean, String, int, int)
     */
    @Test
    public void filter_ByCastOnly_Fail() {
        boolean status = hibernateFilmDAO.getFilteredFilms
                (null, 0, 0, false, null, 8, 0).isEmpty();
        Assert.assertTrue(status);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateFilmDAO#getFilteredFilms(String, int, int, boolean, String, int, int)
     */
    @Test
    public void filter_ByGenreOnly_Success() {
        film.setTitle("City of God");
        film.setProdYear(2002);
//        film.setGenres();
        hibernateFilmDAO.addFilm(film);

        Film actualFilm = hibernateFilmDAO.getFilteredFilms
                (null, 0, 0, false, null, 0, 8).get(0);
        Assert.assertEquals(film, actualFilm);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateFilmDAO#getFilteredFilms(String, int, int, boolean, String, int, int)
     */
    @Test
    public void filter_ByAllParameters_Success() {
        film.setTitle("Lion");
        film.setProdYear(2016);
        hibernateFilmDAO.addFilm(film);

        film = new Film();
        cast = new Cast("Matt Damon");
        film.setTitle("The Departed");
        film.setProdYear(2002);
        film.setDirector("Martin Scorsese");
//        film.setGenres();
        film.setHasOscar(true);
        hibernateFilmDAO.addFilm(film);

        java.util.List<Film> filteredFilms = hibernateFilmDAO.getFilteredFilms(
                "the departed", 2002, 2002, true, "martin scorsese",
                cast.getId(), Genre.CRIME.getValue());
        int size = filteredFilms.size();
        Film actualFilm = filteredFilms.get(0);

        Assert.assertEquals(1, size);
        Assert.assertEquals(film, actualFilm);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateFilmDAO#editFilm(am.aca.orgflix.entity.Film)
     */
    @Test
    public void editFilm_Success() {
        film.setTitle("Spotlight");
        film.setProdYear(2015);
        film.setDirector("Tom McCarthy");
        hibernateFilmDAO.addFilm(film);
        film.setHasOscar(true);

        boolean status = hibernateFilmDAO.editFilm(film);
        int size = hibernateFilmDAO.totalNumberOfFilms();

        assertTrue(status);
        Assert.assertEquals(1, size);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateFilmDAO#editFilm(am.aca.orgflix.entity.Film)
     */
    @Test
    public void editFilm_Fail() {
        boolean status = hibernateFilmDAO.editFilm(film);
        Assert.assertFalse(status);
    }

//    /**
//     * @see am.aca.orgflix.dao.implHibernate.HibernateFilmDAO#resetRelationCasts(am.aca.orgflix.entity.Film)
//     */
//    @Test
//    public void resetCasts_ByStatus_Success() {
//        film.setTitle("Captain Fantastic");
//        film.setProdYear(2016);
//        hibernateFilmDAO.addFilm(film);
//
//        cast = new Cast("Viggo Mortensen");
//        hibernateCastDAO.addCast(cast);
//
//        hibernateCastDAO.addCastToFilm(cast, film.getId());
//
//        boolean status = hibernateFilmDAO.resetRelationCasts(film);
//        Assert.assertTrue(status);
//    }

//    /**
//     * @see am.aca.orgflix.dao.implHibernate.HibernateFilmDAO#resetRelationCasts(am.aca.orgflix.entity.Film)
//     */
//    @Test
//    public void resetCasts_ByCheck_Success() {
//        film.setTitle("Captain Fantastic");
//        film.setProdYear(2016);
//        hibernateFilmDAO.addFilm(film);
//
//        cast = new Cast("Viggo Mortensen");
//        hibernateCastDAO.addCast(cast);
//
//        hibernateCastDAO.addCastToFilm(cast, film.getId());
//        hibernateFilmDAO.resetRelationCasts(film);
//
//        int size = hibernateFilmDAO.getFilmsByCast(cast.getId()).size();
//        Assert.assertEquals(0, size);
//    }

//    /**
//     * @see am.aca.orgflix.dao.implHibernate.HibernateFilmDAO#resetRelationCasts(am.aca.orgflix.entity.Film)
//     */
//    @Test
//    public void resetCasts_KeepOthers_Success() {
//        film.setTitle("Captain Fantastic");
//        film.setProdYear(2016);
//        hibernateFilmDAO.addFilm(film);
//
//        cast = new Cast("Viggo Mortensen", false);
//        hibernateCastDAO.addCast(cast);
//        hibernateCastDAO.addCastToFilm(cast, film.getId());
//
//        hibernateFilmDAO.resetRelationCasts(film);
//
//        film.setTitle("City of God");
//        film.setProdYear(2002);
//        hibernateFilmDAO.addFilm(film);
//
//        cast = new Cast("Alice Braga", false);
//        hibernateCastDAO.addCast(cast);
//        hibernateCastDAO.addCastToFilm(cast, film.getId());
//
//        Film actualFilm = hibernateFilmDAO.getFilmsByCast(cast.getId()).get(0);
//        Assert.assertEquals(film, actualFilm);
//    }

//    /**
//     * @see am.aca.orgflix.dao.implHibernate.HibernateFilmDAO#resetRelationCasts(am.aca.orgflix.entity.Film)
//     */
//    @Test
//    public void resetCasts_Fail() {
//        film.setTitle("Captain Fantastic");
//        film.setProdYear(2016);
//        hibernateFilmDAO.addFilm(film);
//
//        boolean status = hibernateFilmDAO.resetRelationCasts(film);
//        Assert.assertFalse(status);
//    }

//    /**
//     * @see am.aca.orgflix.dao.implHibernate.HibernateFilmDAO#resetRelationGenres(am.aca.orgflix.entity.Film)
//     */
//    @Test
//    public void resetGenres_ByStatus_Success() {
//        film.setTitle("Captain Fantastic");
//        film.setProdYear(2016);
//        hibernateFilmDAO.addFilm(film);
//        hibernateFilmDAO.addGenreToFilm(Genre.DRAMA, film.getId());
//
//        boolean status = hibernateFilmDAO.resetRelationGenres(film);
//        Assert.assertTrue(status);
//    }

//    /**
//     * @see am.aca.orgflix.dao.implHibernate.HibernateFilmDAO#resetRelationGenres(am.aca.orgflix.entity.Film)
//     */
//    @Test
//    public void resetGenres_Check_Success() {
//        film.setTitle("Captain Fantastic");
//        film.setProdYear(2016);
//        hibernateFilmDAO.addFilm(film);
//        hibernateFilmDAO.addGenreToFilm(Genre.DRAMA, film.getId());
//        hibernateFilmDAO.resetRelationGenres(film);
//
//        boolean status = hibernateFilmDAO.getFilmsByGenre(Genre.DRAMA).isEmpty();
//        Assert.assertTrue(status);
//    }

//    /**
//     * @see am.aca.orgflix.dao.implHibernate.HibernateFilmDAO#resetRelationGenres(am.aca.orgflix.entity.Film)
//     */
//    @Test
//    public void resetGenres_KeepsOthers_Success() {
//        film.setTitle("American History X");
//        film.setProdYear(1998);
//        hibernateFilmDAO.addFilm(film);
//        hibernateFilmDAO.addGenreToFilm(Genre.CRIME, film.getId());
//        hibernateFilmDAO.addGenreToFilm(Genre.DRAMA, film.getId());
//        hibernateFilmDAO.resetRelationGenres(film);
//
//        film.setTitle("City of God");
//        film.setProdYear(2002);
//        hibernateFilmDAO.addFilm(film);
//        hibernateFilmDAO.addGenreToFilm(Genre.CRIME, film.getId());
//
//        Film actualFilm = hibernateFilmDAO.getFilmsByGenre(Genre.CRIME).get(0);
//        Assert.assertEquals(film, actualFilm);
//    }

//    /**
//     * @see am.aca.orgflix.dao.implHibernate.HibernateFilmDAO#resetRelationGenres(am.aca.orgflix.entity.Film)
//     */
//    @Test
//    public void resetGenres_Fail() {
//        film.setTitle("City of God");
//        film.setProdYear(2002);
//        hibernateFilmDAO.addFilm(film);
//
//        boolean status = hibernateFilmDAO.resetRelationGenres(film);
//        Assert.assertFalse(status);
//    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateFilmDAO#remove(int)
     */
    @Test
    public void remove_Success() {
        film.setTitle("City of God");
        film.setProdYear(2002);
        hibernateFilmDAO.addFilm(film);

        boolean status = hibernateFilmDAO.remove(film.getId());

        int size = hibernateFilmDAO.totalNumberOfFilms();

        Assert.assertTrue(status);
        Assert.assertEquals(0, size);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateFilmDAO#remove(int)
     */
    @Test
    public void remove_Fail() {
        film.setTitle("City of God");
        film.setProdYear(2002);
        hibernateFilmDAO.addFilm(film);
        boolean status = hibernateFilmDAO.remove(0);

        int size = hibernateFilmDAO.totalNumberOfFilms();
        Assert.assertFalse(status);
        Assert.assertEquals(1, size);
    }
}