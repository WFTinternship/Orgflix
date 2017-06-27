package am.aca.orgflix.unit.service;

import am.aca.dao.jdbc.impljdbc.JdbcCastDAO;
import am.aca.dao.jdbc.impljdbc.JdbcFilmDAO;
import am.aca.entity.Cast;
import am.aca.entity.Film;
import am.aca.orgflix.BaseUnitTest;
import am.aca.service.CastService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

/**
 * Created by karine on 6/24/2017
 */
public class CastServiceMockTest extends BaseUnitTest {

    @Autowired
    private CastService castService;

    @Mock
    private JdbcCastDAO castMock;

    @Mock
    private JdbcFilmDAO filmMock;

    private Cast cast = new Cast("Cate Blanchett", true);
    private List<Cast> casts = new ArrayList<>();
    private Film film = new Film("Babel", 2006);
    private List<Film> films = new ArrayList<>();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(castService, "castDAO", castMock);
        ReflectionTestUtils.setField(castService, "filmDAO", filmMock);
    }

    @Test
    public void addCast_Success() {
        when(castMock.addCast(cast)).thenReturn(true);
        Assert.assertTrue(castService.addCast(cast));
    }

    @Test
    public void addCast_Fail() {
        when(castMock.addCast(cast)).thenThrow(RuntimeException.class);
        Assert.assertFalse(castService.addCast(cast));
    }

    @Test
    public void addCastToFilm_Success() {
        when(castMock.isStarringIn(cast.getId(), film.getId())).thenReturn(false);
        when(castMock.addCastToFilm(cast, film.getId())).thenReturn(true);
        Assert.assertTrue(castService.addCastToFilm(cast, film.getId()));
    }

    @Test
    public void addCastToFilmAlreadyStarring_Fail() {
        when(castMock.isStarringIn(cast.getId(), film.getId())).thenReturn(true);
        Assert.assertFalse(castService.addCastToFilm(cast, film.getId()));
    }

    @Test
    public void addCastToFilmInsertionError_Fail() {
        when(castMock.isStarringIn(cast.getId(), film.getId())).thenReturn(true);
        when(castMock.addCastToFilm(cast, film.getId())).thenReturn(false);
        Assert.assertFalse(castService.addCastToFilm(cast, film.getId()));
    }

    @Test
    public void editCast_Success() {
        when(castMock.exists(cast)).thenReturn(true);
        when(castMock.editCast(cast)).thenReturn(true);
        Assert.assertTrue(castService.editCast(cast));
    }

    @Test
    public void editCastNonExisting_Fail() {
        when(castMock.exists(cast)).thenReturn(true);
        Assert.assertFalse(castService.editCast(cast));
    }

    @Test
    public void editCastUpdateError_Fail() {
        when(castMock.exists(cast)).thenReturn(true);
        when(castMock.editCast(cast)).thenReturn(false);
        Assert.assertFalse(castService.editCast(cast));
    }

    @Test
    public void listCasts_Success() {
        casts.add(cast);
        when(castMock.listCast()).thenReturn(casts);
        Assert.assertEquals(1, castService.listCasts().size());
    }

    @Test
    public void listCasts_Fail() {
        when(castMock.listCast()).thenThrow(RuntimeException.class);
        Assert.assertEquals(null, castService.listCasts());
    }

    @Test
    public void listFilmsByCast_Success() {
        films.add(film);
        when(filmMock.getFilmsByCast(cast.getId())).thenReturn(films);
        Assert.assertEquals(1, castService.listFilmsByCast(cast.getId()).size());
    }

    @Test
    public void listFilmsByCast_Fail() {
        when(filmMock.getFilmsByCast(cast.getId())).thenThrow(RuntimeException.class);
        Assert.assertEquals(null, castService.listFilmsByCast(cast.getId()));
    }
}
