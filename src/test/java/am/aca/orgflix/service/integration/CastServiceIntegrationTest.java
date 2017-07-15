package am.aca.orgflix.service.integration;

import am.aca.orgflix.BaseIntegrationTest;
import am.aca.orgflix.entity.Cast;
import am.aca.orgflix.service.CastService;
import am.aca.orgflix.service.ServiceException;
import am.aca.orgflix.service.impl.CastServiceImpl;
import am.aca.orgflix.util.TestHelper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Integration tests for Cast Service Layer
 */
public class CastServiceIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private CastService castService;

    @Autowired
    private TestHelper helper;

    private Cast cast;

    /**
     * Rolls back all changes applied to the test DB resulted from tests
     */
    @After
    public void tearDown() {
        helper.emptyTable(new String[]{
                "film_to_cast", "casts", "films"
        });
    }

    /**
     * @see CastServiceImpl#addCast(am.aca.orgflix.entity.Cast)
     */
    @Test
    public void addCast_WithOscar_Success() {
        castService.addCast(new Cast("Edward Norton", true));
        int size = castService.listCasts().size();
        Assert.assertEquals(1, size);
    }

    /**
     * @see CastServiceImpl#addCast(am.aca.orgflix.entity.Cast)
     */
    @Test
    public void addCast_WithoutOscar_Success() {
        castService.addCast(new Cast("Edward Norton"));
        int size = castService.listCasts().size();
        Assert.assertEquals(1, size);
    }

    /**
     * @see CastServiceImpl#addCast(am.aca.orgflix.entity.Cast)
     */
    @Test
    public void addCast_Success() {
        cast = new Cast("Edward Norton", false);
        castService.addCast(cast);
        int size = castService.listCasts().size();
        Assert.assertEquals(1, size);
    }

    /**
     * @see CastServiceImpl#addCast(am.aca.orgflix.entity.Cast)
     */
    @Test
    public void addCast_NoOscar_Success() {
        cast = new Cast("Edward Norton");
        castService.addCast(cast);
        int size = castService.listCasts().size();
        Assert.assertEquals(1, size);
    }

    /**
     * @see CastServiceImpl#addCast(am.aca.orgflix.entity.Cast)
     */
    @Test(expected = ServiceException.class)
    public void addCast_NullName_Fail() {
        cast = new Cast(null);
        castService.addCast(cast);
    }

    /**
     * @see CastServiceImpl#addCast(am.aca.orgflix.entity.Cast)
     */
    @Test(expected = ServiceException.class)
    public void addCast_EmptyName_Fail() {
        cast = new Cast("");
        castService.addCast(cast);
    }

//    /**
//     * @see CastServiceImpl#addCastToFilm(am.aca.orgflix.entity.Cast, int)
//     */
//    @Test
//    public void addCastToFilm_Success() {
//        cast = new Cast("Edward Norton");
//        castService.addCast(cast);
//        film = new Film("Fight Club", 1997);
//        filmService.addFilm(film);
//        boolean status = castService.addCastToFilm(cast, film.getId());
//        Assert.assertTrue(status);
//        int id = castService.listFilmsByCast(cast.getId()).get(0).getId();
//        Assert.assertEquals(film.getId(), id);
//    }
//
//    /**
//     * @see CastServiceImpl#addCastToFilm(am.aca.orgflix.entity.Cast, int)
//     */
//    @Test
//    public void addCastToFilm_Fail() {
//        cast = new Cast("Edward Norton");
//        castService.addCast(cast);
//        film = new Film("Fight Club", 1997);
//        filmService.addFilm(film);
//        boolean status = castService.addCastToFilm(cast, film.getId());
//        Assert.assertTrue(status);
//        status = castService.addCastToFilm(cast, film.getId());
//        Assert.assertFalse(status);
//        int id = castService.listFilmsByCast(cast.getId()).get(0).getId();
//        Assert.assertEquals(film.getId(), id);
//    }
//
//    /**
//     * @see CastServiceImpl#addCastToFilm(am.aca.orgflix.entity.Cast, int)
//     */
//    @Test
//    public void addCastToFilm_ById_Success() {
//        cast = new Cast("Edward Norton");
//        castService.addCast(cast);
//        film = new Film("Fight Club", 1997);
//        filmService.addFilm(film);
//        castService.addCastToFilm(cast, film.getId());
//        int id = castService.listFilmsByCast(cast.getId()).get(0).getId();
//        Assert.assertEquals(film.getId(), id);
//    }
//
//    /**
//     * @see CastServiceImpl#addCastToFilm(am.aca.orgflix.entity.Cast, int)
//     */
//    @Test
//    public void addCastToFilm_ById_Fail() {
//        cast = new Cast("Edward Norton");
//        castService.addCast(cast);
//        film = new Film("Fight Club", 1997);
//        filmService.addFilm(film);
//        castService.addCastToFilm(cast, film.getId());
//        castService.addCastToFilm(cast, film.getId());
//        int id = castService.listFilmsByCast(cast.getId()).get(0).getId();
//        Assert.assertEquals(film.getId(), id);
//    }

    /**
     * @see CastServiceImpl#editCast(am.aca.orgflix.entity.Cast)
     */
    @Test
    public void editCast_Success() {
        cast = new Cast("Edward Norton");
        castService.addCast(cast);

        cast.setHasOscar(true);
        boolean status = castService.editCast(cast);
        int size = castService.listCasts().size();

        Assert.assertTrue(status);
        Assert.assertEquals(1, size);
    }

    /**
     * @see CastServiceImpl#editCast(am.aca.orgflix.entity.Cast)
     */
    @Test(expected = ServiceException.class)
    public void editCast_BadName_Fail() {
        cast = new Cast("Edward Norton");
        castService.addCast(cast);

        cast.setName(null);
        castService.editCast(cast);
    }

    /**
     * @see CastServiceImpl#editCast(am.aca.orgflix.entity.Cast)
     */
    @Test
    public void editCast_NotExisting_Fail() {
        cast = new Cast();
        boolean status = castService.editCast(cast);
        Assert.assertFalse(status);
    }

    /**
     * @see CastServiceImpl#listCasts()
     */
    @Test
    public void listCasts_Success() {
        cast = new Cast("Edward Norton");
        castService.addCast(cast);
        cast = new Cast("Emma Stone", true);
        castService.addCast(cast);

        List<Cast> actualCast = castService.listCasts();
        Assert.assertEquals(2, actualCast.size());
        Assert.assertEquals(cast, actualCast.get(1));
    }

    /**
     * @see CastServiceImpl#listCasts()
     */
    @Test
    public void listCasts_Empty_Fail() {
        int size = castService.listCasts().size();
        Assert.assertEquals(0, size);
    }

//    /**
//     * @see CastServiceImpl#listFilmsByCast(int)
//     */
//    @Test
//    public void listFilmsByCast_BySize_Success() {
//        cast = new Cast("Edward Norton");
//        castService.addCast(cast);
//        film = new Film("Fight Club", 1997);
//        filmService.addFilm(film);
//        castService.addCastToFilm(cast, film.getId());
//        int size = castService.listFilmsByCast(cast.getId()).size();
//        Assert.assertEquals(1, size);
//    }
//
//    /**
//     * @see CastServiceImpl#listFilmsByCast(int)
//     */
//    @Test
//    public void listFilmsByCast_ById_Success() {
//        cast = new Cast("Edward Norton");
//        castService.addCast(cast);
//        film = new Film("Fight Club", 1997);
//        filmService.addFilm(film);
//        castService.addCastToFilm(cast, film.getId());
//        film = new Film("Primal Fear", 1996);
//        filmService.addFilm(film);
//        castService.addCastToFilm(cast, film.getId());
//        int id = castService.listFilmsByCast(cast.getId()).get(1).getId();
//        Assert.assertEquals(film.getId(), id);
//    }
//
//    /**
//     * @see CastServiceImpl#listFilmsByCast(int)
//     */
//    @Test
//    public void listFilmsByCast_Empty_Fail() {
//        cast = new Cast();
//        boolean status = castService.listFilmsByCast(cast.getId()).isEmpty();
//        Assert.assertTrue(status);
//    }
//
//    /**
//     * @see CastServiceImpl#getCastsByFilm(int)
//     */
//    @Test
//    public void getCastsByFilm_CheckByCast_Success() {
//        cast = new Cast("Edward Norton");
//        castService.addCast(cast);
//        film = new Film("Fight Club", 1997);
//        filmService.addFilm(film);
//        castService.addCastToFilm(cast, film.getId());
//
//        List<Cast> casts = castService.getCastsByFilm(film.getId());
//        Assert.assertEquals(cast, casts.get(0));
//    }
//
//    /**
//     * @see CastServiceImpl#getCastsByFilm(int)
//     */
//    @Test
//    public void getCastsByFilm_MoreThanOne_Success() {
//        cast = new Cast("Edward Norton");
//        castService.addCast(cast);
//        film = new Film("Fight Club", 1997);
//        filmService.addFilm(film);
//        castService.addCastToFilm(cast, film.getId());
//        cast = new Cast("Brad Pitt");
//        castService.addCast(cast);
//        castService.addCastToFilm(cast, film.getId());
//
//        List<Cast> casts = castService.getCastsByFilm(film.getId());
//        Assert.assertEquals(2, casts.size());
//    }
//
//    /**
//     * @see CastServiceImpl#getCastsByFilm(int)
//     */
//    @Test
//    public void getCastsByFilm_Empty_Fail() {
//        film = new Film("Fight Club", 1997);
//        List<Cast> casts = castService.getCastsByFilm(film.getId());
//        Assert.assertTrue(casts.isEmpty());
//    }
}
