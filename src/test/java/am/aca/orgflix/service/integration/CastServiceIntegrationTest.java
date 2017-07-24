package am.aca.orgflix.service.integration;

import am.aca.orgflix.BaseIntegrationTest;
import am.aca.orgflix.entity.Cast;
import am.aca.orgflix.entity.Film;
import am.aca.orgflix.exception.ServiceException;
import am.aca.orgflix.service.CastService;
import am.aca.orgflix.service.FilmService;
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
                "FILM_TO_CAST", "CASTS", "FILMS"
        });
    }

    /**
     * @see CastServiceImpl#add(am.aca.orgflix.entity.Cast)
     */
    @Test
    public void addCast_ValidCast_Success() {
        cast = new Cast("Edward Norton");

        boolean status = castService.add(cast);
        Assert.assertTrue(status);

        List<Cast> casts = castService.getAll();
        Assert.assertEquals(1, casts.size());

        cast = new Cast("Anthony Hopkins", true);
        status = castService.add(cast);
        Assert.assertTrue(status);
    }

    /**
     * @see CastServiceImpl#add(am.aca.orgflix.entity.Cast)
     */
    @Test
    public void addCast_NameNull_Fail() {
        cast = new Cast(null);
        try {
            castService.add(cast);

        } catch (ServiceException e) {
            Assert.assertTrue(e instanceof ServiceException);
        }
    }

    /**
     * @see CastServiceImpl#add(am.aca.orgflix.entity.Cast)
     */
    @Test
    public void addCast_NameEmpty_Fail() {
        cast = new Cast("");

        try {
            castService.add(cast);
        } catch (ServiceException e) {
            Assert.assertTrue(e instanceof ServiceException);
        }
    }

    /**
     * @see CastServiceImpl#addToFilm(am.aca.orgflix.entity.Cast, int)
     */
    @Test
    public void addCastToFilm_ValidInput_Success() {
        cast = new Cast("Edward Norton");
        castService.add(cast);
        film = new Film("Fight Club", 1997);
        filmService.add(film);

        boolean status = castService.addToFilm(cast, film.getId());
        Assert.assertTrue(status);

        int id = filmService.getByCast(cast.getId()).get(0).getId();
        Assert.assertEquals(film.getId(), id);

        status = castService.addToFilm(cast, film.getId());
        Assert.assertFalse(status);
    }

    /**
     * @see CastServiceImpl#addToFilm(am.aca.orgflix.entity.Cast, int)
     */
    @Test
    public void addCastToFilm_InvalidCast_Fail() {
        film = new Film("American History X", 1998);
        filmService.add(film);
        boolean status = castService.addToFilm(cast, film.getId());
        Assert.assertFalse(status);
    }

    /**
     * @see CastServiceImpl#addToFilm(am.aca.orgflix.entity.Cast, int)
     */
    @Test
    public void addCastToFilm_InvalidFilm_Fail() {
        cast = new Cast("Edward Norton");
        boolean status = castService.addToFilm(cast, 0);
        Assert.assertFalse(status);
    }

    /**
     * @see CastServiceImpl#edit(am.aca.orgflix.entity.Cast)
     */
    @Test
    public void editCast_Success() {
        cast = new Cast("Edward Norton");
        castService.add(cast);
        cast.setHasOscar(true);
        castService.edit(cast);
        int size = castService.getAll().size();
        Assert.assertEquals(1, size);
    }

    /**
     * @see CastServiceImpl#edit(am.aca.orgflix.entity.Cast)
     */
    @Test
    public void editCast_NameNull_Fail() {
        cast = new Cast("Edward Norton");
        try {
            castService.add(cast);
            cast.setName(null);
            castService.edit(cast);
        } catch (ServiceException e) {
            Assert.assertTrue(e instanceof ServiceException);
        }
    }

    /**
     * @see CastServiceImpl#edit(am.aca.orgflix.entity.Cast)
     */
    @Test
    public void editCast_NameEmpty_Fail() {
        cast = new Cast("Edward Norton");
        try {
            castService.add(cast);
            cast.setName("");
            castService.edit(cast);
        } catch (ServiceException e) {
            Assert.assertTrue(e instanceof ServiceException);
        }
    }

    /**
     * @see CastServiceImpl#edit(am.aca.orgflix.entity.Cast)
     */
    @Test
    public void editCast_NotExisting_Fail() {
        cast = new Cast("Edward Norton");
        boolean status = castService.edit(cast);
        Assert.assertFalse(status);
    }

    /**
     * @see CastServiceImpl#getAll()
     */
    @Test
    public void listCasts_BySize_Success() {
        List<Cast> casts = castService.getAll();
        Assert.assertTrue(casts.isEmpty());

        cast = new Cast("Edward Norton");
        castService.add(cast);
        cast = new Cast("Emma Stone", true);
        castService.add(cast);

        casts = castService.getAll();
        Assert.assertEquals(2, casts.size());

        Cast actualCast = casts.get(1);
        Assert.assertEquals(cast, actualCast);
    }


    /**
     * @see FilmService#getByCast(int)
     */
    @Test
    public void listFilmsByCast_ValidCast_Success() {
        cast = new Cast("Edward Norton");
        castService.add(cast);
        film = new Film("Fight Club", 1997);
        filmService.add(film);
        castService.addToFilm(cast, film.getId());
        film = new Film("Primal Fear", 1996);
        filmService.add(film);
        castService.addToFilm(cast, film.getId());

        List<Film> films = filmService.getByCast(cast.getId());
        Assert.assertEquals(2, films.size());

        Film actualFilm = films.get(1);
        Assert.assertEquals(film, actualFilm);
    }

    /**
     * @see CastServiceImpl#getByFilm(int)
     */
    @Test
    public void getCastsByFilm_ValidFilm_Success() {
        cast = new Cast("Edward Norton");
        castService.add(cast);
        film = new Film("Fight Club", 1997);
        filmService.add(film);
        castService.addToFilm(cast, film.getId());

        List<Cast> casts = castService.getByFilm(film.getId());
        Assert.assertEquals(1, casts.size());

        cast = new Cast("Brad Pitt");
        castService.add(cast);
        castService.addToFilm(cast, film.getId());

        casts = castService.getByFilm(film.getId());
        Assert.assertEquals(2, casts.size());

        Cast actualCast = casts.get(1);
        Assert.assertEquals(cast, actualCast);
    }

    /**
     * @see CastServiceImpl#getByFilm(int)
     */
    @Test
    public void getCastsByFilm_InvalidFilm_Empty() {
        film = new Film("Fight Club", 1997);
        List<Cast> casts = castService.getByFilm(film.getId());
        Assert.assertTrue(casts.isEmpty());
    }
}
