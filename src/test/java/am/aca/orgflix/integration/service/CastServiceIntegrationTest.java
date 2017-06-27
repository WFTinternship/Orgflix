package am.aca.orgflix.integration.service;

import am.aca.entity.Cast;
import am.aca.entity.Film;
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
 * Test for Cast services
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

    @Before
    public void setUp() {
    }

    @After
    public void revert() {
        helper.emptyTable(new String[]{
                "film_to_cast", "casts", "films"
        });
    }

    @Test
    public void addCastWOscar_Success() {
        castService.addCast(new Cast("Edward Norton", true));
        Assert.assertEquals(1, castService.listCasts().size());
    }

    @Test
    public void addCastWoOscar_Success() {
        castService.addCast(new Cast("Edward Norton"));
        Assert.assertEquals(1, castService.listCasts().size());
    }

    @Test
    public void addCast_Success() {
        cast = new Cast("Edward Norton", false);
        castService.addCast(cast);
        Assert.assertEquals(1, castService.listCasts().size());
    }

    @Test
    public void addCastNoOscar_Success() {
        cast = new Cast("Edward Norton");
        castService.addCast(cast);
        Assert.assertEquals(1, castService.listCasts().size());
    }

    @Test()
    public void addCastNoName_Fail() {
        cast = new Cast(null);
        Assert.assertFalse(castService.addCast(cast));
    }

    @Test
    public void addCastToFilm_Success() {
        cast = new Cast("Edward Norton");
        castService.addCast(cast);
        film = new Film("Fight Club", 1997);
        filmService.addFilm(film);
        Assert.assertTrue(castService.addCastToFilm(cast, film.getId()));
        Assert.assertEquals(film.getId(), castService.listFilmsByCast(cast.getId()).get(0).getId());
    }

    @Test
    public void addCastToFilm_Failed() {
        cast = new Cast("Edward Norton");
        castService.addCast(cast);
        film = new Film("Fight Club", 1997);
        filmService.addFilm(film);
        Assert.assertTrue(castService.addCastToFilm(cast, film.getId()));
        Assert.assertFalse(castService.addCastToFilm(cast, film.getId()));
        Assert.assertEquals(film.getId(), castService.listFilmsByCast(cast.getId()).get(0).getId());
    }

    @Test
    public void addCastToFilmById_Success() {
        cast = new Cast("Edward Norton");
        castService.addCast(cast);
        film = new Film("Fight Club", 1997);
        filmService.addFilm(film);
        castService.addCastToFilm(cast, film.getId());
        Assert.assertEquals(film.getId(), castService.listFilmsByCast(cast.getId()).get(0).getId());
    }

    @Test
    public void addCastToFilmById_Failed() {
        cast = new Cast("Edward Norton");
        castService.addCast(cast);
        film = new Film("Fight Club", 1997);
        filmService.addFilm(film);
        castService.addCastToFilm(cast, film.getId());
        castService.addCastToFilm(cast, film.getId());
        Assert.assertEquals(film.getId(), castService.listFilmsByCast(cast.getId()).get(0).getId());
    }

    @Test
    public void editCast_Success() {
        cast = new Cast("Edward Norton");
        castService.addCast(cast);
        cast.setHasOscar(true);
        castService.editCast(cast);
        Assert.assertEquals(1, castService.listCasts().size());
    }

    @Test
    public void editCastBadName_Fail() {
        cast = new Cast("Edward Norton");
        castService.addCast(cast);
        cast.setName(null);
        Assert.assertFalse(castService.editCast(cast));
    }

    @Test
    public void editCastNotExisting_Fail() {
        cast = new Cast();
        Assert.assertFalse(castService.editCast(cast));
    }

    @Test
    public void listCastsBySize_Success() {
        cast = new Cast("Edward Norton");
        castService.addCast(cast);
        cast = new Cast("Emma Stone", true);
        castService.addCast(cast);
        cast = new Cast("");
        castService.addCast(cast);
        Assert.assertEquals(2, castService.listCasts().size());
    }

    @Test
    public void listCastsById_Success() {
        cast = new Cast("Edward Norton");
        castService.addCast(cast);
        cast = new Cast("Emma Stone", true);
        castService.addCast(cast);
        Assert.assertEquals(cast.getId(), castService.listCasts().get(1).getId());
    }

    @Test
    public void listCastsEmpty_Fail() {
        Assert.assertEquals(0, castService.listCasts().size());
    }

    @Test
    public void listFilmsByCastBySize_Success() {
        cast = new Cast("Edward Norton");
        castService.addCast(cast);
        film = new Film("Fight Club", 1997);
        filmService.addFilm(film);
        castService.addCastToFilm(cast, film.getId());
        Assert.assertEquals(1, castService.listFilmsByCast(cast.getId()).size());
    }

    @Test
    public void listFilmsByCastById_Success() {
        cast = new Cast("Edward Norton");
        castService.addCast(cast);
        film = new Film("Fight Club", 1997);
        filmService.addFilm(film);
        castService.addCastToFilm(cast, film.getId());
        film = new Film("Primal Fear", 1996);
        filmService.addFilm(film);
        castService.addCastToFilm(cast, film.getId());
        Assert.assertEquals(film.getId(), castService.listFilmsByCast(cast.getId()).get(1).getId());
    }

    @Test
    public void listFilmsByCastEmpty_Fail() {
        cast = new Cast();
        Assert.assertTrue(castService.listFilmsByCast(cast.getId()).isEmpty());
    }
}
