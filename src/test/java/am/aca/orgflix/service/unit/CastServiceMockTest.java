package am.aca.orgflix.service.unit;

import am.aca.orgflix.BaseUnitTest;
import am.aca.orgflix.dao.CastDAO;
import am.aca.orgflix.dao.DaoException;
import am.aca.orgflix.dao.FilmDAO;
import am.aca.orgflix.entity.Cast;
import am.aca.orgflix.entity.Film;
import am.aca.orgflix.service.CastService;
import am.aca.orgflix.service.ServiceException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Created by karine on 6/24/2017
 */
public class CastServiceMockTest extends BaseUnitTest {

    @Autowired
    private CastService castService;

    @Mock
    private CastDAO castDaoMock;

    @Mock
    private FilmDAO filmDaoMock;

    private Cast cast = new Cast("Cate Blanchett", true);
    private List<Cast> casts = new ArrayList<>();
    private Film film = new Film("Babel", 2006);
    private List<Film> films = new ArrayList<>();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(castService, "castDAO", castDaoMock);
        ReflectionTestUtils.setField(castService, "filmDAO", filmDaoMock);
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(castDaoMock);
        verifyNoMoreInteractions(filmDaoMock);
    }

    @Test
    public void addCast_Success() {
        when(castDaoMock.addCast(cast)).thenReturn(true);

        boolean status = castService.addCast(cast);
        Assert.assertTrue(status);

        verify(castDaoMock, times(1)).addCast(cast);
    }

    @Test
    public void addCast_Fail() {
        when(castDaoMock.addCast(cast)).thenThrow(DaoException.class);

        try {
            castService.addCast(cast);
        } catch (ServiceException e) {
            verify(castDaoMock, times(1)).addCast(cast);
        }

    }

    @Test
    public void addCastToFilm_Success() {
        when(castDaoMock.isStarringIn(cast.getId(), film.getId())).thenReturn(false);
        when(castDaoMock.addCastToFilm(cast, film.getId())).thenReturn(true);

        boolean status = castService.addCastToFilm(cast, film.getId());
        Assert.assertTrue(status);

        verify(castDaoMock, times(1)).isStarringIn(cast.getId(), film.getId());
        verify(castDaoMock, times(1)).addCastToFilm(cast, film.getId());
    }

    @Test
    public void addCastToFilm_AlreadyStarring_Fail() {
        when(castDaoMock.isStarringIn(cast.getId(), film.getId())).thenReturn(true);

        boolean status = castService.addCastToFilm(cast, film.getId());
        Assert.assertFalse(status);

        verify(castDaoMock, times(1)).isStarringIn(cast.getId(), film.getId());
    }

    @Test
    public void addCastToFilm_InsertionError_Fail() {
        when(castDaoMock.isStarringIn(cast.getId(), film.getId())).thenReturn(false);
        when(castDaoMock.addCastToFilm(cast, film.getId())).thenReturn(false);

        boolean status = castService.addCastToFilm(cast, film.getId());
        Assert.assertFalse(status);

        verify(castDaoMock, times(1)).isStarringIn(cast.getId(), film.getId());
        verify(castDaoMock, times(1)).addCastToFilm(cast, film.getId());
    }

    @Test
    public void editCast_Success() {
        when(castDaoMock.exists(cast)).thenReturn(true);
        when(castDaoMock.editCast(cast)).thenReturn(true);

        boolean status = castService.editCast(cast);
        Assert.assertTrue(status);

        verify(castDaoMock, times(1)).exists(cast);
        verify(castDaoMock, times(1)).editCast(cast);
    }

    @Test
    public void editCast_NonExisting_Fail() {
        when(castDaoMock.exists(cast)).thenReturn(false);

        boolean status = castService.editCast(cast);
        Assert.assertFalse(status);

        verify(castDaoMock, times(1)).exists(cast);
    }

    @Test
    public void editCast_UpdateError_Fail() {
        when(castDaoMock.exists(cast)).thenReturn(true);
        when(castDaoMock.editCast(cast)).thenReturn(false);

        boolean status = castService.editCast(cast);
        Assert.assertFalse(status);

        verify(castDaoMock, times(1)).exists(cast);
        verify(castDaoMock, times(1)).editCast(cast);
    }

    @Test
    public void listCasts_Success() {
        when(castDaoMock.listCast()).thenReturn(casts);

        List<Cast> actualCasts = castService.listCasts();
        Assert.assertEquals(casts, actualCasts);

        verify(castDaoMock, times(1)).listCast();
    }

    @Test
    public void listCasts_Fail() {
        when(castDaoMock.listCast()).thenThrow(DaoException.class);

        try {
            castService.listCasts();
        } catch (ServiceException e) {
            verify(castDaoMock, times(1)).listCast();
        }

    }

    @Test
    public void listFilmsByCast_Success() {
        when(filmDaoMock.getFilmsByCast(cast.getId())).thenReturn(films);

        List<Film> actualFilms = castService.listFilmsByCast(cast.getId());
        Assert.assertEquals(films, actualFilms);

        verify(filmDaoMock, times(1)).getFilmsByCast(cast.getId());
    }

    @Test
    public void listFilmsByCast_Fail() {
        when(filmDaoMock.getFilmsByCast(cast.getId())).thenThrow(DaoException.class);

        try {
            castService.listFilmsByCast(cast.getId());
        } catch (ServiceException e) {
            verify(filmDaoMock, times(1)).getFilmsByCast(cast.getId());
        }
    }
}
