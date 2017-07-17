package am.aca.orgflix.service.unit;

import am.aca.orgflix.BaseUnitTest;
import am.aca.orgflix.dao.DaoException;
import am.aca.orgflix.dao.ListDao;
import am.aca.orgflix.entity.Film;
import am.aca.orgflix.entity.User;
import am.aca.orgflix.service.ListService;
import am.aca.orgflix.service.ServiceException;
import am.aca.orgflix.service.impl.ListServiceImpl;
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
 * Unit tests for Lists Service layer
 */
public class ListServiceMockTest extends BaseUnitTest {

    @Autowired
    private ListService listService;

    @Mock
    private ListDao listDaoMock;

    private Film film = new Film("Babel", 2006);
    private User user = new User("hulk", "Bruce Banner", "bbanner@avenger.com", "natasha");
    private List<Film> films = new ArrayList<>();

    /**
     * Configures Mockito
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(listService, "listDao", listDaoMock);
    }

    /**
     * Assures no more methods of DAO mocks are invoked
     */
    @After
    public void tearDown() {
        verifyNoMoreInteractions(listDaoMock);
    }

    /**
     * @see ListServiceImpl#addToWatched(int, boolean, int)
     */
    @Test
    public void addToWatched_Existing_Success() {
        when(listDaoMock.areRelated(film.getId(), user.getId())).thenReturn(false);
        when(listDaoMock.insertWatched(film.getId(), user.getId(), true)).thenReturn(true);

        boolean status = listService.addToWatched(film.getId(), true, user.getId());
        Assert.assertTrue(status);

        verify(listDaoMock, times(1)).areRelated(film.getId(), user.getId());
        verify(listDaoMock, times(1)).insertWatched(film.getId(), user.getId(), true);
    }

    /**
     * @see ListServiceImpl#addToWatched(int, boolean, int)
     */
    @Test
    public void addToWatched_WhenPlanned_Success() {
        when(listDaoMock.areRelated(film.getId(), user.getId())).thenReturn(true);
        when(listDaoMock.updateWatched(film.getId(), user.getId())).thenReturn(true);

        boolean status = listService.addToWatched(film.getId(), true, user.getId());
        Assert.assertTrue(status);

        verify(listDaoMock, times(1)).areRelated(film.getId(), user.getId());
        verify(listDaoMock, times(1)).updateWatched(film.getId(), user.getId());
    }

    /**
     * @see ListServiceImpl#addToWatched(int, boolean, int)
     */
    @Test
    public void addToWatched_WhenPlanned_Fail() {
        when(listDaoMock.areRelated(film.getId(), user.getId())).thenReturn(true);
        when(listDaoMock.updateWatched(film.getId(), user.getId())).thenReturn(false);

        boolean status = listService.addToWatched(film.getId(), true, user.getId());
        Assert.assertFalse(status);

        verify(listDaoMock, times(1)).areRelated(film.getId(), user.getId());
        verify(listDaoMock, times(1)).updateWatched(film.getId(), user.getId());
    }

    /**
     * @see ListServiceImpl#addToWatched(int, boolean, int)
     */
    @Test
    public void addToWatched_InsertionError_Fail() {
        when(listDaoMock.areRelated(film.getId(), user.getId())).thenReturn(false);
        when(listDaoMock.insertWatched(film.getId(), user.getId(), true)).thenReturn(false);

        boolean status = listService.addToWatched(film.getId(), true, user.getId());
        Assert.assertFalse(status);

        verify(listDaoMock, times(1)).areRelated(film.getId(), user.getId());
        verify(listDaoMock, times(1)).insertWatched(film.getId(), user.getId(), true);
    }

    /**
     * @see ListServiceImpl#addToPlanned(int, boolean, int)
     */
    @Test
    public void addToPlanned_Existing_Success() {
        when(listDaoMock.areRelated(film.getId(), user.getId())).thenReturn(false);
        when(listDaoMock.insertPlanned(film.getId(), user.getId(), true)).thenReturn(true);

        boolean status = listService.addToPlanned(film.getId(), true, user.getId());
        Assert.assertTrue(status);

        verify(listDaoMock, times(1)).areRelated(film.getId(), user.getId());
        verify(listDaoMock, times(1)).insertPlanned(film.getId(), user.getId(), true);
    }

    /**
     * @see ListServiceImpl#addToPlanned(int, boolean, int)
     */
    @Test
    public void addToPlanned_WhenWatched_Success() {
        when(listDaoMock.areRelated(film.getId(), user.getId())).thenReturn(true);
        when(listDaoMock.updatePlanned(film.getId(), user.getId())).thenReturn(true);

        boolean status = listService.addToPlanned(film.getId(), true, user.getId());
        Assert.assertTrue(status);

        verify(listDaoMock, times(1)).areRelated(film.getId(), user.getId());
        verify(listDaoMock, times(1)).updatePlanned(film.getId(), user.getId());
    }

    /**
     * @see ListServiceImpl#addToPlanned(int, boolean, int)
     */
    @Test
    public void addToPlanned_WhenWatched_Fail() {
        when(listDaoMock.areRelated(film.getId(), user.getId())).thenReturn(true);
        when(listDaoMock.updatePlanned(film.getId(), user.getId())).thenReturn(false);

        boolean status = listService.addToPlanned(film.getId(), true, user.getId());
        Assert.assertFalse(status);

        verify(listDaoMock, times(1)).areRelated(film.getId(), user.getId());
        verify(listDaoMock, times(1)).updatePlanned(film.getId(), user.getId());
    }

    /**
     * @see ListServiceImpl#addToPlanned(int, boolean, int)
     */
    @Test
    public void addToPlanned_InsertionError_Fail() {
        when(listDaoMock.areRelated(film.getId(), user.getId())).thenReturn(false);
        when(listDaoMock.insertPlanned(film.getId(), user.getId(), true)).thenReturn(false);

        boolean status = listService.addToPlanned(film.getId(), true, user.getId());
        Assert.assertFalse(status);

        verify(listDaoMock, times(1)).areRelated(film.getId(), user.getId());
        verify(listDaoMock, times(1)).insertPlanned(film.getId(), user.getId(), true);
    }

    /**
     * @see ListServiceImpl#showOwnWatched(int, int)
     */
    @Test
    public void showOwnWatched_Success() {
        when(listDaoMock.showOwnWatched(user.getId(), 0, 12)).thenReturn(films);

        List<Film> actualFilms = listService.showOwnWatched(user.getId(), 0);
        Assert.assertEquals(films, actualFilms);

        verify(listDaoMock, times(1)).showOwnWatched(user.getId(), film.getId(), 12);
    }

    /**
     * @see ListServiceImpl#showOwnWatched(int, int)
     */
    @Test
    public void showOwnWatched_Fail() {
        when(listDaoMock.showOwnWatched(user.getId(), 0, 12)).thenThrow(DaoException.class);

        try {
            listService.showOwnWatched(user.getId(), 0);
        } catch (ServiceException e) {
            verify(listDaoMock, times(1)).showOwnWatched(user.getId(), film.getId(), 12);
        }
    }

    /**
     * @see ListServiceImpl#showOwnPlanned(int, int)
     */
    @Test
    public void showOwnPlanned_Success() {
        when(listDaoMock.showOwnPlanned(user.getId(), 0, 12)).thenReturn(films);

        List<Film> actualFilms = listService.showOwnPlanned(user.getId(), 0);
        Assert.assertEquals(films, actualFilms);

        verify(listDaoMock, times(1)).showOwnPlanned(user.getId(), film.getId(), 12);
    }

    /**
     * @see ListServiceImpl#showOwnPlanned(int, int)
     */
    @Test
    public void showOwnPlanned_Fail() {
        when(listDaoMock.showOwnPlanned(user.getId(), 0, 12)).thenThrow(DaoException.class);

        try {
            listService.showOwnPlanned(user.getId(), 0);
        } catch (ServiceException e) {
            verify(listDaoMock, times(1)).showOwnPlanned(user.getId(), film.getId(), 12);
        }
    }

    /**
     * @see ListServiceImpl#showOthersWatched(int, int)
     */
    @Test
    public void showOthersWatched_Success() {
        when(listDaoMock.showOthersWatched(user.getId(), 0, 12)).thenReturn(films);

        List<Film> actualFilms = listService.showOthersWatched(user.getId(), 0);
        Assert.assertEquals(films, actualFilms);

        verify(listDaoMock, times(1)).showOthersWatched(user.getId(), film.getId(), 12);
    }

    /**
     * @see ListServiceImpl#showOthersWatched(int, int)
     */
    @Test
    public void showOthersWatched_Fail() {
        when(listDaoMock.showOthersWatched(user.getId(), 0, 12)).thenThrow(DaoException.class);

        try {
            listService.showOthersWatched(user.getId(), 0);
        } catch (ServiceException e) {
            verify(listDaoMock, times(1)).showOthersWatched(user.getId(), film.getId(), 12);
        }
    }

    /**
     * @see ListServiceImpl#showOthersPlanned(int, int)
     */
    @Test
    public void showOthersPlanned_Success() {
        when(listDaoMock.showOthersPlanned(user.getId(), 0, 12)).thenReturn(films);

        List<Film> actualFilms = listService.showOthersPlanned(user.getId(), 0);
        Assert.assertEquals(films, actualFilms);

        verify(listDaoMock, times(1)).showOthersPlanned(user.getId(), film.getId(), 12);
    }

    /**
     * @see ListServiceImpl#showOthersPlanned(int, int)
     */
    @Test
    public void showOthersPlanned_Fail() {
        when(listDaoMock.showOthersPlanned(user.getId(), 0, 12)).thenThrow(DaoException.class);

        try {
            listService.showOthersPlanned(user.getId(), 0);
        } catch (ServiceException e) {
            verify(listDaoMock, times(1)).showOthersPlanned(user.getId(), film.getId(), 12);
        }
    }

    /**
     * @see ListServiceImpl#removeFromWatched(int, int)
     */
    @Test
    public void removeFromWatched_WhenNotWatched_Fail() {
        when(listDaoMock.isWatched(film.getId(), user.getId())).thenReturn(false);

        boolean status = listService.removeFromWatched(film.getId(), user.getId());
        Assert.assertFalse(status);

        verify(listDaoMock, times(1)).isWatched(film.getId(), user.getId());
    }

    /**
     * @see ListServiceImpl#removeFromWatched(int, int)
     */
    @Test
    public void removeFromWatched_WhenPlanned_Success() {
        when(listDaoMock.isWatched(film.getId(), user.getId())).thenReturn(true);
        when(listDaoMock.isPlanned(film.getId(), user.getId())).thenReturn(true);
        when(listDaoMock.resetWatched(film.getId(), user.getId())).thenReturn(true);

        boolean status = listService.removeFromWatched(film.getId(), user.getId());
        Assert.assertTrue(status);

        verify(listDaoMock, times(1)).isWatched(film.getId(), user.getId());
        verify(listDaoMock, times(1)).isPlanned(film.getId(), user.getId());
        verify(listDaoMock, times(1)).resetWatched(film.getId(), user.getId());
    }

    /**
     * @see ListServiceImpl#removeFromWatched(int, int)
     */
    @Test
    public void removeFromWatched_WhenNotPlanned_Success() {
        when(listDaoMock.isWatched(film.getId(), user.getId())).thenReturn(true);
        when(listDaoMock.isPlanned(film.getId(), user.getId())).thenReturn(false);
        when(listDaoMock.removeFilm(film.getId(), user.getId())).thenReturn(true);

        boolean status = listService.removeFromWatched(film.getId(), user.getId());
        Assert.assertTrue(status);

        verify(listDaoMock, times(1)).isWatched(film.getId(), user.getId());
        verify(listDaoMock, times(1)).isPlanned(film.getId(), user.getId());
        verify(listDaoMock, times(1)).removeFilm(film.getId(), user.getId());
    }

    /**
     * @see ListServiceImpl#removeFromPlanned(int, int)
     */
    @Test
    public void removeFromPlanned_NotPlanned_Fail() {
        when(listDaoMock.isPlanned(film.getId(), user.getId())).thenReturn(false);

        boolean status = listService.removeFromPlanned(film.getId(), user.getId());
        Assert.assertFalse(status);

        verify(listDaoMock, times(1)).isPlanned(film.getId(), user.getId());
    }

    /**
     * @see ListServiceImpl#removeFromPlanned(int, int)
     */
    @Test
    public void removeFromPlanned_WhenWatched_Success() {
        when(listDaoMock.isPlanned(film.getId(), user.getId())).thenReturn(true);
        when(listDaoMock.isWatched(film.getId(), user.getId())).thenReturn(true);
        when(listDaoMock.resetPlanned(film.getId(), user.getId())).thenReturn(true);

        boolean status = listService.removeFromPlanned(film.getId(), user.getId());
        Assert.assertTrue(status);

        verify(listDaoMock, times(1)).isWatched(film.getId(), user.getId());
        verify(listDaoMock, times(1)).isPlanned(film.getId(), user.getId());
        verify(listDaoMock, times(1)).resetPlanned(film.getId(), user.getId());
    }

    /**
     * @see ListServiceImpl#removeFromPlanned(int, int)
     */
    @Test
    public void removeFromPlanned_WhenNotWatched_Success() {
        when(listDaoMock.isPlanned(film.getId(), user.getId())).thenReturn(true);
        when(listDaoMock.isWatched(film.getId(), user.getId())).thenReturn(false);
        when(listDaoMock.removeFilm(film.getId(), user.getId())).thenReturn(true);

        boolean status = listService.removeFromPlanned(film.getId(), user.getId());
        Assert.assertTrue(status);

        verify(listDaoMock, times(1)).isWatched(film.getId(), user.getId());
        verify(listDaoMock, times(1)).isPlanned(film.getId(), user.getId());
        verify(listDaoMock, times(1)).removeFilm(film.getId(), user.getId());
    }

    /**
     * @see ListServiceImpl#makePrivate(int, am.aca.orgflix.entity.Film)
     */
    @Test
    public void makePrivate_Success() {
        when(listDaoMock.areRelated(film.getId(), user.getId())).thenReturn(true);
        when(listDaoMock.changePrivacy(film, user.getId(), false)).thenReturn(true);

        boolean status = listService.makePrivate(user.getId(), film);
        Assert.assertTrue(status);

        verify(listDaoMock, times(1)).areRelated(film.getId(), user.getId());
        verify(listDaoMock, times(1)).changePrivacy(film, user.getId(), false);
    }

    /**
     * @see ListServiceImpl#makePrivate(int, am.aca.orgflix.entity.Film)
     */
    @Test
    public void makePrivate_NotRelated_Fail() {
        when(listDaoMock.areRelated(film.getId(), user.getId())).thenReturn(false);

        boolean status = listService.makePrivate(user.getId(), film);
        Assert.assertFalse(status);

        verify(listDaoMock, times(1)).areRelated(film.getId(), user.getId());
    }

    /**
     * @see ListServiceImpl#makePrivate(int, am.aca.orgflix.entity.Film)
     */
    @Test
    public void makePrivate_UpdateError_Fail() {
        when(listDaoMock.areRelated(film.getId(), user.getId())).thenReturn(true);
        when(listDaoMock.changePrivacy(film, user.getId(), false)).thenReturn(false);

        boolean status = listService.makePrivate(user.getId(), film);
        Assert.assertFalse(status);

        verify(listDaoMock, times(1)).areRelated(film.getId(), user.getId());
        verify(listDaoMock, times(1)).changePrivacy(film, user.getId(), false);
    }

    /**
     * @see ListServiceImpl#makePublic(int, am.aca.orgflix.entity.Film)
     */
    @Test
    public void makePublic_Success() {
        when(listDaoMock.areRelated(film.getId(), user.getId())).thenReturn(true);
        when(listDaoMock.changePrivacy(film, user.getId(), true)).thenReturn(true);

        boolean status = listService.makePublic(user.getId(), film);
        Assert.assertTrue(status);

        verify(listDaoMock, times(1)).areRelated(film.getId(), user.getId());
        verify(listDaoMock, times(1)).changePrivacy(film, user.getId(), true);
    }

    /**
     * @see ListServiceImpl#makePublic(int, am.aca.orgflix.entity.Film)
     */
    @Test
    public void makePublic_NotRelated_Fail() {
        when(listDaoMock.areRelated(film.getId(), user.getId())).thenReturn(false);

        boolean status = listService.makePublic(user.getId(), film);
        Assert.assertFalse(status);

        verify(listDaoMock, times(1)).areRelated(film.getId(), user.getId());
    }

    /**
     * @see ListServiceImpl#makePublic(int, am.aca.orgflix.entity.Film)
     */
    @Test
    public void makePublic_UpdateError_Fail() {
        when(listDaoMock.areRelated(film.getId(), user.getId())).thenReturn(true);
        when(listDaoMock.changePrivacy(film, user.getId(), true)).thenReturn(false);

        boolean status = listService.makePublic(user.getId(), film);
        Assert.assertFalse(status);

        verify(listDaoMock, times(1)).areRelated(film.getId(), user.getId());
        verify(listDaoMock, times(1)).changePrivacy(film, user.getId(), true);
    }

    /**
     * @see ListServiceImpl#totalNumberOfFilmsInAList(int, boolean)
     */
    @Test
    public void totalNumber_Watched_Success() {
        when(listDaoMock.totalNumberOfWatched(user.getId())).thenReturn(8);

        int size = listService.totalNumberOfFilmsInAList(user.getId(), true);
        Assert.assertEquals(8, size);

        verify(listDaoMock, times(1)).totalNumberOfWatched(user.getId());
    }

    /**
     * @see ListServiceImpl#totalNumberOfFilmsInAList(int, boolean)
     */
    @Test
    public void totalNumber_Watched_Fail() {
        when(listDaoMock.totalNumberOfWatched(user.getId())).thenThrow(DaoException.class);

        try {
            listService.totalNumberOfFilmsInAList(user.getId(), true);
        } catch (ServiceException e) {
            verify(listDaoMock, times(1)).totalNumberOfWatched(user.getId());
        }
    }

    /**
     * @see ListServiceImpl#totalNumberOfFilmsInAList(int, boolean)
     */
    @Test
    public void totalNumber_Planned_Success() {
        when(listDaoMock.totalNumberOfPlanned(user.getId())).thenReturn(8);

        int size = listService.totalNumberOfFilmsInAList(user.getId(), false);
        Assert.assertEquals(8, size);

        verify(listDaoMock, times(1)).totalNumberOfPlanned(user.getId());
    }

    /**
     * @see ListServiceImpl#totalNumberOfFilmsInAList(int, boolean)
     */
    @Test
    public void totalNumber_Planned_Fail() {
        when(listDaoMock.totalNumberOfPlanned(user.getId())).thenThrow(DaoException.class);

        try {
            listService.totalNumberOfFilmsInAList(user.getId(), false);
        } catch (ServiceException e) {
            verify(listDaoMock, times(1)).totalNumberOfPlanned(user.getId());
        }
    }
}
