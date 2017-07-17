package am.aca.orgflix.service.integration;

import am.aca.orgflix.BaseIntegrationTest;
import am.aca.orgflix.entity.Cast;
import am.aca.orgflix.entity.Film;
import am.aca.orgflix.service.CastService;
import am.aca.orgflix.service.FilmService;
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
    private FilmService filmService;

    @Autowired
    private TestHelper helper;

    private Cast cast;
    private Film film;

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
    public void addCast_ValidCast_Success() {
        cast = new Cast("Edward Norton");

        boolean status = castService.addCast(cast);
        Assert.assertTrue(status);

        List<Cast> casts = castService.listCasts();
        Assert.assertEquals(1, casts.size());

        cast = new Cast("Anthony Hopkins", true);
        status = castService.addCast(cast);
        Assert.assertTrue(status);
    }

    /**
     * @see CastServiceImpl#addCast(am.aca.orgflix.entity.Cast)
     */
    @Test(expected = ServiceException.class)
    public void addCast_NameNull_Fail() {
        cast = new Cast(null);
        castService.addCast(cast);
    }

    /**
     * @see CastServiceImpl#addCast(am.aca.orgflix.entity.Cast)
     */
    @Test(expected = ServiceException.class)
    public void addCast_NameEmpty_Fail() {
        cast = new Cast("");
        castService.addCast(cast);
    }

    /**
     * @see CastServiceImpl#addCastToFilm(am.aca.orgflix.entity.Cast, int)
     */
    @Test
    public void addCastToFilm_ValidInput_Success() {
        cast = new Cast("Edward Norton");
        castService.addCast(cast);
        film = new Film("Fight Club", 1997);
        filmService.addFilm(film);

        boolean status = castService.addCastToFilm(cast, film.getId());
        Assert.assertTrue(status);

        int id = castService.listFilmsByCast(cast.getId()).get(0).getId();
        Assert.assertEquals(film.getId(), id);

        status = castService.addCastToFilm(cast, film.getId());
        Assert.assertFalse(status);
    }

    /**
     * @see CastServiceImpl#addCastToFilm(am.aca.orgflix.entity.Cast, int)
     */
    @Test
    public void addCastToFilm_InvalidCast_Fail() {
        film = new Film("American History X", 1998);
        filmService.addFilm(film);
        boolean status = castService.addCastToFilm(cast, film.getId());
        Assert.assertFalse(status);
    }

    /**
     * @see CastServiceImpl#addCastToFilm(am.aca.orgflix.entity.Cast, int)
     */
    @Test
    public void addCastToFilm_InvalidFilm_Fail() {
        cast = new Cast("Edward Norton");
        boolean status = castService.addCastToFilm(cast, 0);
        Assert.assertFalse(status);
    }

    /**
     * @see CastServiceImpl#editCast(am.aca.orgflix.entity.Cast)
     */
    @Test
    public void editCast_Success() {
        cast = new Cast("Edward Norton");
        castService.addCast(cast);
        cast.setHasOscar(true);
        castService.editCast(cast);
        int size = castService.listCasts().size();
        Assert.assertEquals(1, size);
    }

    /**
     * @see CastServiceImpl#editCast(am.aca.orgflix.entity.Cast)
     */
    @Test(expected = ServiceException.class)
    public void editCast_NameNull_Fail() {
        cast = new Cast("Edward Norton");
        castService.addCast(cast);
        cast.setName(null);
        castService.editCast(cast);
    }

    /**
     * @see CastServiceImpl#editCast(am.aca.orgflix.entity.Cast)
     */
    @Test(expected = ServiceException.class)
    public void editCast_NameEmpty_Fail() {
        cast = new Cast("Edward Norton");
        castService.addCast(cast);
        cast.setName("");
        castService.editCast(cast);
    }

    /**
     * @see CastServiceImpl#editCast(am.aca.orgflix.entity.Cast)
     */
    @Test
    public void editCast_NotExisting_Fail() {
        cast = new Cast("Edward Norton");
        boolean status = castService.editCast(cast);
        Assert.assertFalse(status);
    }

    /**
     * @see CastServiceImpl#listCasts()
     */
    @Test
    public void listCasts_BySize_Success() {
        List<Cast> casts = castService.listCasts();
        Assert.assertTrue(casts.isEmpty());

        cast = new Cast("Edward Norton");
        castService.addCast(cast);
        cast = new Cast("Emma Stone", true);
        castService.addCast(cast);

        casts = castService.listCasts();
        Assert.assertEquals(2, casts.size());

        Cast actualCast = casts.get(1);
        Assert.assertEquals(cast, actualCast);
    }


    /**
     * @see CastServiceImpl#listFilmsByCast(int)
     */
    @Test
    public void listFilmsByCast_ValidCast_Success() {
        cast = new Cast("Edward Norton");
        castService.addCast(cast);
        film = new Film("Fight Club", 1997);
        filmService.addFilm(film);
        castService.addCastToFilm(cast, film.getId());
        film = new Film("Primal Fear", 1996);
        filmService.addFilm(film);
        castService.addCastToFilm(cast, film.getId());

        List<Film> films = castService.listFilmsByCast(cast.getId());
        Assert.assertEquals(2, films.size());

        Film actualFilm = films.get(1);
        Assert.assertEquals(film, actualFilm);
    }

    /**
     * @see CastServiceImpl#listFilmsByCast(int)
     */
    @Test
    public void listFilmsByCast_InvalidCast_Empty() {
        cast = new Cast();
        List<Film> films = castService.listFilmsByCast(cast.getId());
        Assert.assertTrue(films.isEmpty());
    }

    /**
     * @see CastServiceImpl#getCastsByFilm(int)
     */
    @Test
    public void getCastsByFilm_ValidFilm_Success() {
        cast = new Cast("Edward Norton");
        castService.addCast(cast);
        film = new Film("Fight Club", 1997);
        filmService.addFilm(film);
        castService.addCastToFilm(cast, film.getId());

        List<Cast> casts = castService.getCastsByFilm(film.getId());
        Assert.assertEquals(1, casts.size());

        cast = new Cast("Brad Pitt");
        castService.addCast(cast);
        castService.addCastToFilm(cast, film.getId());

        casts = castService.getCastsByFilm(film.getId());
        Assert.assertEquals(2, casts.size());

        Cast actualCast = casts.get(1);
        Assert.assertEquals(cast, actualCast);
    }

    /**
     * @see CastServiceImpl#getCastsByFilm(int)
     */
    @Test
    public void getCastsByFilm_InvalidFilm_Empty() {
        film = new Film("Fight Club", 1997);
        List<Cast> casts = castService.getCastsByFilm(film.getId());
        Assert.assertTrue(casts.isEmpty());
    }
}
