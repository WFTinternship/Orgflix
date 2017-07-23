package am.aca.orgflix.service.unit;

import am.aca.orgflix.BaseUnitTest;
import am.aca.orgflix.dao.CastDAO;
import am.aca.orgflix.entity.Cast;
import am.aca.orgflix.entity.Film;
import am.aca.orgflix.exception.ServiceException;
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
    private Film film = new Film("Babel", 2006);

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
     * @see CastServiceImpl#add(am.aca.orgflix.entity.Cast)
     */
    @Test
    public void addCast_ValidCast_Success() {
        when(castDaoMock.exists(cast.getName())).thenReturn(false);
        when(castDaoMock.add(cast)).thenReturn(true);
        when(castDaoMock.exists(cast.getName())).thenReturn(false);

        boolean status = castService.add(cast);
        Assert.assertTrue(status);

        verify(castDaoMock, times(1)).add(cast);
        verify(castDaoMock, times(1)).exists(cast.getName());
    }

    /**
     * @see CastServiceImpl#add(am.aca.orgflix.entity.Cast)
     */
    @Test(expected = ServiceException.class)
    public void addCast_NameNull_Fail() {
        cast.setName(null);
        boolean status = castService.add(cast);
        Assert.assertFalse(status);
    }

    /**
     * @see CastServiceImpl#add(am.aca.orgflix.entity.Cast)
     */
    @Test(expected = ServiceException.class)
    public void addCast_NameEmpty_Fail() {
        cast.setName("");
        boolean status = castService.add(cast);
        Assert.assertFalse(status);
    }

    /**
     * @see CastServiceImpl#add(am.aca.orgflix.entity.Cast)
     */
    @Test
    public void addCast_DaoException_Fail() {
        when(castDaoMock.exists(cast.getName())).thenReturn(false);
        when(castDaoMock.add(cast)).thenThrow(RuntimeException.class);

        boolean status = castService.add(cast);
        Assert.assertFalse(status);

        verify(castDaoMock, times(1)).add(cast);
        verify(castDaoMock, times(1)).exists(cast.getName());
    }

    /**
     * @see CastServiceImpl#addToFilm(am.aca.orgflix.entity.Cast, int)
     */
    @Test
    public void addCastToFilm_ValidInput_Success() {
        when(castDaoMock.isRelatedToFilm(cast.getId(), film.getId())).thenReturn(false);
        when(castDaoMock.addToFilm(cast, film.getId())).thenReturn(true);

        boolean status = castService.addToFilm(cast, film.getId());
        Assert.assertTrue(status);

        verify(castDaoMock, times(1)).isRelatedToFilm(cast.getId(), film.getId());
        verify(castDaoMock, times(1)).addToFilm(cast, film.getId());
    }

    /**
     * @see CastServiceImpl#addToFilm(am.aca.orgflix.entity.Cast, int)
     */
    @Test
    public void addCastToFilm_AlreadyStarring_Fail() {
        when(castDaoMock.isRelatedToFilm(cast.getId(), film.getId())).thenReturn(true);

        boolean status = castService.addToFilm(cast, film.getId());
        Assert.assertFalse(status);

        verify(castDaoMock, times(1)).isRelatedToFilm(cast.getId(), film.getId());
    }

    /**
     * @see CastServiceImpl#addToFilm(am.aca.orgflix.entity.Cast, int)
     */
    @Test
    public void addCastToFilm_InsertionError_Fail() {
        when(castDaoMock.isRelatedToFilm(cast.getId(), film.getId())).thenReturn(false);
        when(castDaoMock.addToFilm(cast, film.getId())).thenReturn(false);

        boolean status = castService.addToFilm(cast, film.getId());
        Assert.assertFalse(status);

        verify(castDaoMock, times(1)).isRelatedToFilm(cast.getId(), film.getId());
        verify(castDaoMock, times(1)).addToFilm(cast, film.getId());
    }

    /**
     * @see CastServiceImpl#addToFilm(am.aca.orgflix.entity.Cast, int)
     */
    @Test
    public void addCastToFilm_DaoException_Fail() {
        when(castDaoMock.isRelatedToFilm(cast.getId(), film.getId())).thenThrow(RuntimeException.class);

        boolean status = castService.addToFilm(cast, film.getId());
        Assert.assertFalse(status);

        verify(castDaoMock, times(1)).isRelatedToFilm(cast.getId(), film.getId());
    }

    /**
     * @see CastServiceImpl#edit(am.aca.orgflix.entity.Cast)
     */
    @Test
    public void editCast_ValidInput_Success() {
        when(castDaoMock.exists(cast)).thenReturn(true);
        when(castDaoMock.edit(cast)).thenReturn(true);

        boolean status = castService.edit(cast);
        Assert.assertTrue(status);

        verify(castDaoMock, times(1)).exists(cast);
        verify(castDaoMock, times(1)).edit(cast);
    }

    /**
     * @see CastServiceImpl#edit(am.aca.orgflix.entity.Cast)
     */
    @Test
    public void editCast_NonExisting_Fail() {
        when(castDaoMock.exists(cast)).thenReturn(false);

        boolean status = castService.edit(cast);
        Assert.assertFalse(status);

        verify(castDaoMock, times(1)).exists(cast);
    }

    /**
     * @see CastServiceImpl#edit(am.aca.orgflix.entity.Cast)
     */
    @Test
    public void editCast_UpdateError_Fail() {
        when(castDaoMock.exists(cast)).thenReturn(true);
        when(castDaoMock.edit(cast)).thenReturn(false);

        boolean status = castService.edit(cast);
        Assert.assertFalse(status);

        verify(castDaoMock, times(1)).exists(cast);
        verify(castDaoMock, times(1)).edit(cast);
    }

    /**
     * @see CastServiceImpl#edit(am.aca.orgflix.entity.Cast)
     */
    @Test(expected = RuntimeException.class)
    public void editCast_NameNull_Fail() {
        cast.setName(null);

        castService.edit(cast);
    }

    /**
     * @see CastServiceImpl#edit(am.aca.orgflix.entity.Cast)
     */
    @Test(expected = RuntimeException.class)
    public void editCast_NameEmpty_Fail() {
        cast.setName("");

        castService.edit(cast);
    }

    /**
     * @see CastServiceImpl#getAll()
     */
    @Test
    public void listCasts_Success() {
        when(castDaoMock.getAll()).thenReturn(casts);

        List<Cast> actualCasts = castService.getAll();
        Assert.assertEquals(casts, actualCasts);

        verify(castDaoMock, times(1)).getAll();
    }

    /**
     * @see CastServiceImpl#getAll()
     */
    @Test
    public void listCasts_Fail() {
        when(castDaoMock.getAll()).thenThrow(RuntimeException.class);

        List<Cast> casts = castService.getAll();
        Assert.assertTrue(casts.isEmpty());
        verify(castDaoMock, times(1)).getAll();
    }

    /**
     * @see CastServiceImpl#getByFilm(int)
     */
    @Test
    public void getCastsByFilm_Success() {
        when(castDaoMock.getByFilm(film.getId())).thenReturn(casts);

        List<Cast> actualCasts = castService.getByFilm(film.getId());
        Assert.assertEquals(casts, actualCasts);

        verify(castDaoMock, times(1)).getByFilm(film.getId());
    }

    /**
     * @see CastServiceImpl#getByFilm(int)
     */
    @Test
    public void getCastsByFilm_Fail() {
        when(castDaoMock.getByFilm(film.getId())).thenThrow(RuntimeException.class);

        List<Cast> actualCast = castService.getByFilm(film.getId());
        Assert.assertTrue(actualCast.isEmpty());

        verify(castDaoMock, times(1)).getByFilm(film.getId());
    }
}
