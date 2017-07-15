package am.aca.orgflix.service.unit;

import am.aca.orgflix.BaseUnitTest;
import am.aca.orgflix.dao.CastDAO;
import am.aca.orgflix.entity.Cast;
import am.aca.orgflix.service.CastService;
import am.aca.orgflix.service.impl.CastServiceImpl;
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
 * Unit tests for Cast Service layer
 */
public class CastServiceMockTest extends BaseUnitTest {

    @Autowired
    private CastService castService;

    @Mock
    private CastDAO castDaoMock;

    private Cast cast = new Cast("Cate Blanchett", true);
    private List<Cast> casts = new ArrayList<>();

    /**
     * Configures Mockito
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(castService, "castDAO", castDaoMock);
    }

    /**
     * Assures no more methods of DAO mocks are invoked
     */
    @After
    public void tearDown() {
        verifyNoMoreInteractions(castDaoMock);
    }

    /**
     * @see CastServiceImpl#addCast(am.aca.orgflix.entity.Cast)
     */
    @Test
    public void addCast_Success() {
        when(castDaoMock.addCast(cast)).thenReturn(true);

        boolean status = castService.addCast(cast);
        Assert.assertTrue(status);

        verify(castDaoMock, times(1)).addCast(cast);
    }

    /**
     * @see CastServiceImpl#addCast(am.aca.orgflix.entity.Cast)
     */
    @Test
    public void addCast_InvalidName_Success() {
        Cast badCast = new Cast();
//        when(castDaoMock.addCast(badCast)).thenReturn(true);

        boolean status = castService.addCast(badCast);
        Assert.assertFalse(status);

//        verify(castDaoMock, times(1)).addCast(cast);
    }

//    /**
//     * @see CastServiceImpl#addCastToFilm(am.aca.orgflix.entity.Cast, int)
//     */
//    @Test
//    public void addCastToFilm_Success() {
//        when(castDaoMock.isStarringIn(cast.getId(), film.getId())).thenReturn(false);
//        when(castDaoMock.addCastToFilm(cast, film.getId())).thenReturn(true);
//
//        boolean status = castService.addCastToFilm(cast, film.getId());
//        Assert.assertTrue(status);
//
//        verify(castDaoMock, times(1)).isStarringIn(cast.getId(), film.getId());
//        verify(castDaoMock, times(1)).addCastToFilm(cast, film.getId());
//    }
//
//    /**
//     * @see CastServiceImpl#addCastToFilm(am.aca.orgflix.entity.Cast, int)
//     */
//    @Test
//    public void addCastToFilm_AlreadyStarring_Fail() {
//        when(castDaoMock.isStarringIn(cast.getId(), film.getId())).thenReturn(true);
//
//        boolean status = castService.addCastToFilm(cast, film.getId());
//        Assert.assertFalse(status);
//
//        verify(castDaoMock, times(1)).isStarringIn(cast.getId(), film.getId());
//    }
//
//    /**
//     * @see CastServiceImpl#addCastToFilm(am.aca.orgflix.entity.Cast, int)
//     */
//    @Test
//    public void addCastToFilm_InsertionError_Fail() {
//        when(castDaoMock.isStarringIn(cast.getId(), film.getId())).thenReturn(false);
//        when(castDaoMock.addCastToFilm(cast, film.getId())).thenReturn(false);
//
//        boolean status = castService.addCastToFilm(cast, film.getId());
//        Assert.assertFalse(status);
//
//        verify(castDaoMock, times(1)).isStarringIn(cast.getId(), film.getId());
//        verify(castDaoMock, times(1)).addCastToFilm(cast, film.getId());
//    }

    /**
     * @see CastServiceImpl#editCast(am.aca.orgflix.entity.Cast)
     */
    @Test
    public void editCast_Success() {
        when(castDaoMock.editCast(cast)).thenReturn(true);

        boolean status = castService.editCast(cast);
        Assert.assertTrue(status);

        verify(castDaoMock, times(1)).editCast(cast);
    }

    /**
     * @see CastServiceImpl#editCast(am.aca.orgflix.entity.Cast)
     */
    @Test
    public void editCast_NonExisting_Fail() {
        when(castDaoMock.editCast(cast)).thenReturn(false);

        boolean status = castService.editCast(cast);
        Assert.assertFalse(status);

        verify(castDaoMock, times(1)).editCast(cast);

    }

    /**
     * @see CastServiceImpl#editCast(am.aca.orgflix.entity.Cast)
     */
    @Test
    public void editCast_InvalidName_Fail() {
        Cast badCast = new Cast("");
        boolean status = castService.editCast(badCast);
        Assert.assertFalse(status);

    }

    /**
     * @see CastServiceImpl#listCasts()
     */
    @Test
    public void listCasts_Success() {
        when(castDaoMock.listCast()).thenReturn(casts);

        List<Cast> actualCasts = castService.listCasts();
        Assert.assertEquals(casts, actualCasts);

        verify(castDaoMock, times(1)).listCast();
    }

    /**
     * @see CastServiceImpl#listCasts()
     */
    @Test
    public void listCasts_Fail() {
        List<Cast> emptyList = new ArrayList<>();
        when(castDaoMock.listCast()).thenReturn(emptyList);

        List<Cast> actualList = castService.listCasts();

        Assert.assertEquals(emptyList, actualList);
        verify(castDaoMock, times(1)).listCast();

    }

//    /**
//     * @see CastServiceImpl#listFilmsByCast(int)
//     */
//    @Test
//    public void listFilmsByCast_Success() {
//        when(filmDaoMock.getFilmsByCast(cast.getId())).thenReturn(films);
//
//        List<Film> actualFilms = castService.listFilmsByCast(cast.getId());
//        Assert.assertEquals(films, actualFilms);
//
//        verify(filmDaoMock, times(1)).getFilmsByCast(cast.getId());
//    }
//
//    /**
//     * @see CastServiceImpl#listFilmsByCast(int)
//     */
//    @Test
//    public void listFilmsByCast_Fail() {
//        when(filmDaoMock.getFilmsByCast(cast.getId())).thenThrow(DaoException.class);
//
//        try {
//            castService.listFilmsByCast(cast.getId());
//        } catch (ServiceException e) {
//            verify(filmDaoMock, times(1)).getFilmsByCast(cast.getId());
//        }
//    }
//
//    /**
//     * @see CastServiceImpl#getCastsByFilm(int)
//     */
//    @Test
//    public void getCastsByFilm_Success() {
//        when(castDaoMock.getCastsByFilm(film.getId())).thenReturn(casts);
//
//        List<Cast> actualCasts = castService.getCastsByFilm(film.getId());
//        Assert.assertEquals(casts, actualCasts);
//
//        verify(castDaoMock, times(1)).getCastsByFilm(film.getId());
//    }
//
//    /**
//     * @see CastServiceImpl#getCastsByFilm(int)
//     */
//    @Test
//    public void getCastsByFilm_Fail() {
//        when(castDaoMock.getCastsByFilm(film.getId())).thenThrow(DaoException.class);
//
//        try {
//            castService.getCastsByFilm(film.getId());
//        } catch (ServiceException e) {
//            verify(castDaoMock, times(1)).getCastsByFilm(film.getId());
//        }
//    }
}
