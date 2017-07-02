package am.aca.orgflix.service.integration;

import am.aca.orgflix.BaseIntegrationTest;
import am.aca.orgflix.entity.Cast;
import am.aca.orgflix.entity.Film;
import am.aca.orgflix.service.CastService;
import am.aca.orgflix.service.FilmService;
import am.aca.orgflix.util.TestHelper;
import org.junit.After;
import org.junit.Assert;
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

    @After
    public void revert() {
        helper.emptyTable(new String[]{
                "film_to_cast", "casts", "films"
        });
    }

    @Test
    public void addCastWOscar_Success() {
        castService.addCast(new Cast("Edward Norton", true));
        int size = castService.listCasts().size();
        Assert.assertEquals(1, size);
    }

    @Test
    public void addCastWoOscar_Success() {
        castService.addCast(new Cast("Edward Norton"));
        int size = castService.listCasts().size();
        Assert.assertEquals(1, size);
    }

    @Test
    public void addCast_Success() {
        cast = new Cast("Edward Norton", false);
        castService.addCast(cast);
        int size = castService.listCasts().size();
        Assert.assertEquals(1, size);
    }

    @Test
    public void addCastNoOscar_Success() {
        cast = new Cast("Edward Norton");
        castService.addCast(cast);
        int size = castService.listCasts().size();
        Assert.assertEquals(1, size);
    }

    @Test()
    public void addCastNoName_Fail() {
        cast = new Cast(null);
        boolean status = castService.addCast(cast);
        Assert.assertFalse(status);
    }

    @Test
    public void addCastToFilm_Success() {
        cast = new Cast("Edward Norton");
        castService.addCast(cast);
        film = new Film("Fight Club", 1997);
        filmService.addFilm(film);
        boolean status = castService.addCastToFilm(cast, film.getId());
        Assert.assertTrue(status);
        int id = castService.listFilmsByCast(cast.getId()).get(0).getId();
        Assert.assertEquals(film.getId(), id);
    }

    @Test
    public void addCastToFilm_Failed() {
        cast = new Cast("Edward Norton");
        castService.addCast(cast);
        film = new Film("Fight Club", 1997);
        filmService.addFilm(film);
        boolean status = castService.addCastToFilm(cast, film.getId());
        Assert.assertTrue(status);
        status = castService.addCastToFilm(cast, film.getId());
        Assert.assertFalse(status);
        int id = castService.listFilmsByCast(cast.getId()).get(0).getId();
        Assert.assertEquals(film.getId(), id);
    }

    @Test
    public void addCastToFilmById_Success() {
        cast = new Cast("Edward Norton");
        castService.addCast(cast);
        film = new Film("Fight Club", 1997);
        filmService.addFilm(film);
        castService.addCastToFilm(cast, film.getId());
        int id = castService.listFilmsByCast(cast.getId()).get(0).getId();
        Assert.assertEquals(film.getId(), id);
    }

    @Test
    public void addCastToFilmById_Failed() {
        cast = new Cast("Edward Norton");
        castService.addCast(cast);
        film = new Film("Fight Club", 1997);
        filmService.addFilm(film);
        castService.addCastToFilm(cast, film.getId());
        castService.addCastToFilm(cast, film.getId());
        int id = castService.listFilmsByCast(cast.getId()).get(0).getId();
        Assert.assertEquals(film.getId(), id);
    }

    @Test
    public void editCast_Success() {
        cast = new Cast("Edward Norton");
        castService.addCast(cast);
        cast.setHasOscar(true);
        castService.editCast(cast);
        int size = castService.listCasts().size();
        Assert.assertEquals(1, size);
    }

    @Test
    public void editCastBadName_Fail() {
        cast = new Cast("Edward Norton");
        castService.addCast(cast);
        cast.setName(null);
        boolean status = castService.editCast(cast);
        Assert.assertFalse(status);
    }

    @Test
    public void editCastNotExisting_Fail() {
        cast = new Cast();
        boolean status = castService.editCast(cast);
        Assert.assertFalse(status);
    }

    @Test
    public void listCastsBySize_Success() {
        cast = new Cast("Edward Norton");
        castService.addCast(cast);
        cast = new Cast("Emma Stone", true);
        castService.addCast(cast);
        cast = new Cast("");
        castService.addCast(cast);
        int size = castService.listCasts().size();
        Assert.assertEquals(2, size);
    }

    @Test
    public void listCastsById_Success() {
        cast = new Cast("Edward Norton");
        castService.addCast(cast);
        cast = new Cast("Emma Stone", true);
        castService.addCast(cast);
        int id = castService.listCasts().get(1).getId();
        Assert.assertEquals(cast.getId(), id);
    }

    @Test
    public void listCastsEmpty_Fail() {
        int size = castService.listCasts().size();
        Assert.assertEquals(0, size);
    }

    @Test
    public void listFilmsByCastBySize_Success() {
        cast = new Cast("Edward Norton");
        castService.addCast(cast);
        film = new Film("Fight Club", 1997);
        filmService.addFilm(film);
        castService.addCastToFilm(cast, film.getId());
        int size = castService.listFilmsByCast(cast.getId()).size();
        Assert.assertEquals(1, size);
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
        int id = castService.listFilmsByCast(cast.getId()).get(1).getId();
        Assert.assertEquals(film.getId(), id);
    }

    @Test
    public void listFilmsByCastEmpty_Fail() {
        cast = new Cast();
        boolean status = castService.listFilmsByCast(cast.getId()).isEmpty();
        Assert.assertTrue(status);
    }
}
