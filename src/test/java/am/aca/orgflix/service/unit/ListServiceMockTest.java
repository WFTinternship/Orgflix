package am.aca.orgflix.service.unit;

import am.aca.orgflix.BaseUnitTest;
import am.aca.orgflix.dao.ListDao;
import am.aca.orgflix.entity.Film;
import am.aca.orgflix.entity.User;
import am.aca.orgflix.service.ListService;
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
import java.util.Arrays;
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

    private Integer[] totalFilms = {0, 1, 1, 1, 0, 1, 1, 0};

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
        when(listDaoMock.setFilmAsWatched(film.getId(), user.getId())).thenReturn(true);

        boolean status = listService.addToWatched(film.getId(), true, user.getId());
        Assert.assertTrue(status);

        verify(listDaoMock, times(1)).areRelated(film.getId(), user.getId());
        verify(listDaoMock, times(1)).setFilmAsWatched(film.getId(), user.getId());
    }

    /**
     * @see ListServiceImpl#addToWatched(int, boolean, int)
     */
    @Test
    public void addToWatched_WhenPlanned_Fail() {
        when(listDaoMock.areRelated(film.getId(), user.getId())).thenReturn(true);
        when(listDaoMock.setFilmAsWatched(film.getId(), user.getId())).thenReturn(false);

        boolean status = listService.addToWatched(film.getId(), true, user.getId());
        Assert.assertFalse(status);

        verify(listDaoMock, times(1)).areRelated(film.getId(), user.getId());
        verify(listDaoMock, times(1)).setFilmAsWatched(film.getId(), user.getId());
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
        when(listDaoMock.setFilmAsPlanned(film.getId(), user.getId())).thenReturn(true);

        boolean status = listService.addToPlanned(film.getId(), true, user.getId());
        Assert.assertTrue(status);

        verify(listDaoMock, times(1)).areRelated(film.getId(), user.getId());
        verify(listDaoMock, times(1)).setFilmAsPlanned(film.getId(), user.getId());
    }

    /**
     * @see ListServiceImpl#addToPlanned(int, boolean, int)
     */
    @Test
    public void addToPlanned_WhenWatched_Fail() {
        when(listDaoMock.areRelated(film.getId(), user.getId())).thenReturn(true);
        when(listDaoMock.setFilmAsPlanned(film.getId(), user.getId())).thenReturn(false);

        boolean status = listService.addToPlanned(film.getId(), true, user.getId());
        Assert.assertFalse(status);

        verify(listDaoMock, times(1)).areRelated(film.getId(), user.getId());
        verify(listDaoMock, times(1)).setFilmAsPlanned(film.getId(), user.getId());
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
        when(listDaoMock.getOwnWatched(user.getId(), 0, 12)).thenReturn(films);

        List<Film> actualFilms = listService.showOwnWatched(user.getId(), 0);
        Assert.assertEquals(films, actualFilms);

        verify(listDaoMock, times(1)).getOwnWatched(user.getId(), film.getId(), 12);
    }

    /**
     * @see ListServiceImpl#showOwnWatched(int, int)
     */
    @Test
    public void showOwnWatched_Fail() {
        when(listDaoMock.getOwnWatched(user.getId(), 0, 12)).thenThrow(RuntimeException.class);

        List<Film> actualFilms = listService.showOwnWatched(user.getId(), 0);
        Assert.assertTrue(actualFilms.isEmpty());

        verify(listDaoMock, times(1)).getOwnWatched(user.getId(), film.getId(), 12);
    }

    /**
     * @see ListServiceImpl#showOwnPlanned(int, int)
     */
    @Test
    public void showOwnPlanned_Success() {
        when(listDaoMock.getOwnPlanned(user.getId(), 0, 12)).thenReturn(films);

        List<Film> actualFilms = listService.showOwnPlanned(user.getId(), 0);
        Assert.assertEquals(films, actualFilms);

        verify(listDaoMock, times(1)).getOwnPlanned(user.getId(), film.getId(), 12);
    }

    /**
     * @see ListServiceImpl#showOwnPlanned(int, int)
     */
    @Test
    public void showOwnPlanned_Fail() {
        when(listDaoMock.getOwnPlanned(user.getId(), 0, 12)).thenThrow(RuntimeException.class);

        List<Film> acualFilms = listService.showOwnPlanned(user.getId(), 0);
        Assert.assertTrue(acualFilms.isEmpty());
        verify(listDaoMock, times(1)).getOwnPlanned(user.getId(), film.getId(), 12);
    }

    /**
     * @see ListServiceImpl#showOthersWatched(int, int)
     */
    @Test
    public void showOthersWatched_Success() {
        when(listDaoMock.getOthersWatched(user.getId(), 0, 12)).thenReturn(films);

        List<Film> actualFilms = listService.showOthersWatched(user.getId(), 0);
        Assert.assertEquals(films, actualFilms);

        verify(listDaoMock, times(1)).getOthersWatched(user.getId(), film.getId(), 12);
    }

    /**
     * @see ListServiceImpl#showOthersWatched(int, int)
     */
    @Test
    public void showOthersWatched_Fail() {
        when(listDaoMock.getOthersWatched(user.getId(), 0, 12)).thenThrow(RuntimeException.class);

        List<Film> actualFilms = listService.showOthersWatched(user.getId(), 0);
        Assert.assertTrue(actualFilms.isEmpty());
        verify(listDaoMock, times(1)).getOthersWatched(user.getId(), film.getId(), 12);
    }

    /**
     * @see ListServiceImpl#showOthersPlanned(int, int)
     */
    @Test
    public void showOthersPlanned_Success() {
        when(listDaoMock.getOthersPlanned(user.getId(), 0, 12)).thenReturn(films);

        List<Film> actualFilms = listService.showOthersPlanned(user.getId(), 0);
        Assert.assertEquals(films, actualFilms);

        verify(listDaoMock, times(1)).getOthersPlanned(user.getId(), film.getId(), 12);
    }

    /**
     * @see ListServiceImpl#showOthersPlanned(int, int)
     */
    @Test
    public void showOthersPlanned_Fail() {
        when(listDaoMock.getOthersPlanned(user.getId(), 0, 12)).thenThrow(RuntimeException.class);

        List<Film> actualFilms = listService.showOthersPlanned(user.getId(), 0);
        Assert.assertTrue(actualFilms.isEmpty());
        verify(listDaoMock, times(1)).getOthersPlanned(user.getId(), film.getId(), 12);
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
        when(listDaoMock.setFilmAsNotWatched(film.getId(), user.getId())).thenReturn(true);

        boolean status = listService.removeFromWatched(film.getId(), user.getId());
        Assert.assertTrue(status);

        verify(listDaoMock, times(1)).isWatched(film.getId(), user.getId());
        verify(listDaoMock, times(1)).isPlanned(film.getId(), user.getId());
        verify(listDaoMock, times(1)).setFilmAsNotWatched(film.getId(), user.getId());
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
        when(listDaoMock.setFilmAsNotPlanned(film.getId(), user.getId())).thenReturn(true);

        boolean status = listService.removeFromPlanned(film.getId(), user.getId());
        Assert.assertTrue(status);

        verify(listDaoMock, times(1)).isWatched(film.getId(), user.getId());
        verify(listDaoMock, times(1)).isPlanned(film.getId(), user.getId());
        verify(listDaoMock, times(1)).setFilmAsNotPlanned(film.getId(), user.getId());
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
        when(listDaoMock.setFilmPrivacy(film, user.getId(), false)).thenReturn(true);

        boolean status = listService.makePrivate(user.getId(), film);
        Assert.assertTrue(status);

        verify(listDaoMock, times(1)).areRelated(film.getId(), user.getId());
        verify(listDaoMock, times(1)).setFilmPrivacy(film, user.getId(), false);
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
        when(listDaoMock.setFilmPrivacy(film, user.getId(), false)).thenReturn(false);

        boolean status = listService.makePrivate(user.getId(), film);
        Assert.assertFalse(status);

        verify(listDaoMock, times(1)).areRelated(film.getId(), user.getId());
        verify(listDaoMock, times(1)).setFilmPrivacy(film, user.getId(), false);
    }

    /**
     * @see ListServiceImpl#makePublic(int, am.aca.orgflix.entity.Film)
     */
    @Test
    public void makePublic_Success() {
        when(listDaoMock.areRelated(film.getId(), user.getId())).thenReturn(true);
        when(listDaoMock.setFilmPrivacy(film, user.getId(), true)).thenReturn(true);

        boolean status = listService.makePublic(user.getId(), film);
        Assert.assertTrue(status);

        verify(listDaoMock, times(1)).areRelated(film.getId(), user.getId());
        verify(listDaoMock, times(1)).setFilmPrivacy(film, user.getId(), true);
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
        when(listDaoMock.setFilmPrivacy(film, user.getId(), true)).thenReturn(false);

        boolean status = listService.makePublic(user.getId(), film);
        Assert.assertFalse(status);

        verify(listDaoMock, times(1)).areRelated(film.getId(), user.getId());
        verify(listDaoMock, times(1)).setFilmPrivacy(film, user.getId(), true);
    }

    /**
     * @see ListServiceImpl#filmPrivacyList(int, boolean)
     */
    @Test
    public void totalNumber_Watched_Success() {
        when(listDaoMock.watchedFilmPrivacyList(user.getId())).thenReturn(Arrays.asList(totalFilms));

        int[] result = listService.filmPrivacyList(user.getId(), true);
        Assert.assertEquals(8, result.length);

        verify(listDaoMock, times(1)).watchedFilmPrivacyList(user.getId());
    }

    /**
     * @see ListServiceImpl#filmPrivacyList(int, boolean)
     */
    @Test
    public void totalNumber_Watched_Fail() {
        when(listDaoMock.watchedFilmPrivacyList(user.getId())).thenThrow(RuntimeException.class);

        int[] result = listService.filmPrivacyList(user.getId(), true);
        Assert.assertEquals(0, result.length);
        verify(listDaoMock, times(1)).watchedFilmPrivacyList(user.getId());
    }

    /**
     * @see ListServiceImpl#filmPrivacyList(int, boolean)
     */
    @Test
    public void totalNumber_Planned_Success() {
        when(listDaoMock.plannedFilmPrivacyList(user.getId())).thenReturn(Arrays.asList(totalFilms));

        int[] result = listService.filmPrivacyList(user.getId(), false);
        Assert.assertEquals(8, result.length);

        verify(listDaoMock, times(1)).plannedFilmPrivacyList(user.getId());
    }

    /**
     * @see ListServiceImpl#filmPrivacyList(int, boolean)
     */
    @Test
    public void totalNumber_Planned_Fail() {
        when(listDaoMock.plannedFilmPrivacyList(user.getId())).thenThrow(RuntimeException.class);

        int[] result = listService.filmPrivacyList(user.getId(), false);
        Assert.assertEquals(0, result.length);
        verify(listDaoMock, times(1)).plannedFilmPrivacyList(user.getId());
    }
}
