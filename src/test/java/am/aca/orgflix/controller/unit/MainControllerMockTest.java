package am.aca.orgflix.controller.unit;

import am.aca.orgflix.BaseUnitTest;
import am.aca.orgflix.controller.MainController;
import am.aca.orgflix.controller.UserController;
import am.aca.orgflix.entity.Film;
import am.aca.orgflix.entity.User;
import am.aca.orgflix.service.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Unit tests for Main Controller
 */
public class MainControllerMockTest extends BaseUnitTest {
    @Autowired
    private MainController mainController;

    @Mock
    private UserService userServiceMock;
    @Mock
    private CastService castServiceMock;
    @Mock
    private FilmService filmServiceMock;
    @Mock
    private ListService listServiceMock;

    private List<Film> films = new ArrayList<>();
    private String[] ratings = new String[0];
    private User user = new User("hulk", "Bruce Banner", "bbanner@avengers.com", "natasha");

    /**
     * Configures Mockito
     */
    @Before
    @SuppressWarnings("Duplicates")
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(mainController, "userService", userServiceMock);
        ReflectionTestUtils.setField(mainController, "castService", castServiceMock);
        ReflectionTestUtils.setField(mainController, "filmService", filmServiceMock);
        ReflectionTestUtils.setField(mainController, "listService", listServiceMock);

        user.setId(1);
    }

    /**
     * Assures no more methods of Service mocks are invoked
     */
    @After
    public void tearDown() {
        verifyNoMoreInteractions(userServiceMock);
        verifyNoMoreInteractions(castServiceMock);
        verifyNoMoreInteractions(filmServiceMock);
        verifyNoMoreInteractions(listServiceMock);
    }

    /**
     * @see MainController#index()
     */
    @Test
    public void index_Success() {
        when(filmServiceMock.getFilmsList(0)).thenReturn(films);
        when(filmServiceMock.totalNumberOfFilms()).thenReturn(0);
        when(filmServiceMock.getAllRatings(0)).thenReturn(ratings);

        ModelAndView actualMV = mainController.index();

        Assert.assertEquals("index", actualMV.getViewName());
        Assert.assertEquals(films, actualMV.getModel().get("films"));
        Assert.assertEquals(ratings, actualMV.getModel().get("ratings"));
        Assert.assertEquals(-1, actualMV.getModel().get("userId"));
        Assert.assertEquals(0, actualMV.getModel().get("userAuth"));
        Assert.assertEquals(0, actualMV.getModel().get("currPage"));
        Assert.assertEquals("index", actualMV.getModel().get("page"));
        Assert.assertEquals(0, actualMV.getModel().get("numOfPages"));

        verify(filmServiceMock, times(1)).getFilmsList(0);
        verify(filmServiceMock, times(1)).totalNumberOfFilms();
        verify(filmServiceMock, times(1)).getAllRatings(0);
    }

    /**
     * @see MainController#index()
     */
    @Test
    public void index_ExceptionThrown_Fail() {
        when(filmServiceMock.getFilmsList(0)).thenThrow(ServiceException.class);

        ModelAndView actualMV = mainController.index();

        Assert.assertEquals("error", actualMV.getViewName());

        verify(filmServiceMock, times(1)).getFilmsList(0);
    }


    /**
     * @see MainController#paging(int, int, int)
     */
    @Test
    public void paging_Authenticated_Success() {
        when(filmServiceMock.getFilmsList(12)).thenReturn(films);
        when(filmServiceMock.totalNumberOfFilms()).thenReturn(0);
        when(userServiceMock.get(1)).thenReturn(user);

        ModelAndView actualMV = mainController.paging(1, 1, 100);

        Assert.assertEquals("index", actualMV.getViewName());
        Assert.assertEquals(films, actualMV.getModel().get("films"));
        Assert.assertEquals(1, actualMV.getModel().get("userId"));
        Assert.assertEquals("hulk (bbanner@avengers.com)", actualMV.getModel().get("user"));
        Assert.assertEquals(100, actualMV.getModel().get("userAuth"));
        Assert.assertEquals(1, actualMV.getModel().get("currPage"));
        Assert.assertEquals("index", actualMV.getModel().get("page"));

        verify(filmServiceMock, times(1)).getFilmsList(12);
        verify(filmServiceMock, times(1)).totalNumberOfFilms();
        verify(userServiceMock, times(1)).get(1);
    }

    /**
     * @see MainController#paging(int, int, int)
     */
    @Test
    public void paging_UnAuthenticated_Success() {
        when(filmServiceMock.getFilmsList(24)).thenReturn(films);
        when(filmServiceMock.totalNumberOfFilms()).thenReturn(0);
        when(userServiceMock.get(1)).thenReturn(user);

        ModelAndView actualMV = mainController.paging(2, -1, 100);

        Assert.assertEquals("index", actualMV.getViewName());
        Assert.assertEquals(films, actualMV.getModel().get("films"));
        Assert.assertEquals(-1, actualMV.getModel().get("userId"));
        Assert.assertEquals("", actualMV.getModel().get("user"));
        Assert.assertEquals(100, actualMV.getModel().get("userAuth"));
        Assert.assertEquals(2, actualMV.getModel().get("currPage"));
        Assert.assertEquals("index", actualMV.getModel().get("page"));

        verify(filmServiceMock, times(1)).getFilmsList(24);
        verify(filmServiceMock, times(1)).totalNumberOfFilms();
    }

    /**
     * @see MainController#paging(int, int, int)
     */
    @Test
    public void paging_InternalError_Fail() {
        when(userServiceMock.get(1)).thenThrow(ServiceException.class);

        ModelAndView actualMV = mainController.paging(2, 1, 100);

        Assert.assertEquals("error", actualMV.getViewName());

        verify(userServiceMock, times(1)).get(1);
    }

    /**
     * @see MainController#paging(int, int, int)
     */
    @Test
    public void pagingWAuth_Success() {
        when(userServiceMock.authenticate(user.getEmail(), user.getPass())).thenReturn(user);
        when(filmServiceMock.getFilmsList(0)).thenReturn(films);
        when(filmServiceMock.totalNumberOfFilms()).thenReturn(0);

        ModelAndView actualMV = mainController.paging(0,user.getId(), 100);

        Assert.assertEquals("index", actualMV.getViewName());
        Assert.assertEquals(films, actualMV.getModel().get("films"));
        Assert.assertEquals(user.getId(), actualMV.getModel().get("userId"));
        Assert.assertEquals("hulk (bbanner@avengers.com)", actualMV.getModel().get("user"));
        Assert.assertEquals(100, actualMV.getModel().get("userAuth"));
        Assert.assertEquals(0, actualMV.getModel().get("currPage"));
        Assert.assertEquals("index", actualMV.getModel().get("page"));

        verify(filmServiceMock, times(1)).getFilmsList(0);
        verify(filmServiceMock, times(1)).totalNumberOfFilms();
        verify(userServiceMock, times(1)).authenticate(user.getEmail(), user.getPass());
    }

    /**
     * @see MainController#paging(int, int, int)
     */
    @Test
    public void pagingWAuth_AuthenticationError_Fail() {
        when(userServiceMock.authenticate(user.getEmail(), user.getPass())).thenReturn(null);

        ModelAndView actualMV = mainController.paging(0,1, 0);

        Assert.assertEquals("error", actualMV.getViewName());

        verify(userServiceMock, times(1)).authenticate(user.getEmail(), user.getPass());
    }

    /**
     * @see MainController#paging(int, int, int)
     */
    @Test
    public void pagingWAuth_InternalError_Fail() {
        when(userServiceMock.authenticate(user.getEmail(), user.getPass())).thenReturn(user);
        when(filmServiceMock.getFilmsList(0)).thenThrow(ServiceException.class);

        ModelAndView actualMV = mainController.paging(0,1, 100);

        Assert.assertEquals("error", actualMV.getViewName());

        verify(filmServiceMock, times(1)).getFilmsList(0);
        verify(userServiceMock, times(1)).authenticate(user.getEmail(), user.getPass());
    }

    /**
     * @see MainController#watchList(int, int, int)
     * */
    @Test
    public void watchList_Success() {
        when(listServiceMock.showOwnWatched(1, 12)).thenReturn(films);
        when(listServiceMock.totalNumberOfFilmsInAList(1, true)).thenReturn(0);
        when(userServiceMock.get(1)).thenReturn(user);

        ModelAndView actualMV = mainController.watchList(1, 100, 1);

        Assert.assertEquals("index", actualMV.getViewName());
        Assert.assertEquals(films, actualMV.getModel().get("films"));
        Assert.assertEquals(1, actualMV.getModel().get("userId"));
        Assert.assertEquals("hulk (bbanner@avengers.com)", actualMV.getModel().get("user"));
        Assert.assertEquals(100, actualMV.getModel().get("userAuth"));
        Assert.assertEquals(1, actualMV.getModel().get("currPage"));
        Assert.assertEquals("watch_list", actualMV.getModel().get("page"));

        verify(listServiceMock, times(1)).showOwnWatched(1, 12);
        verify(listServiceMock, times(1)).totalNumberOfFilmsInAList(1, true);
        verify(userServiceMock, times(1)).get(1);
    }

    /**
     * @see MainController#watchList(int, int, int)
     * */
    @Test
    public void watchList_InternalError_Fail() {
        when(userServiceMock.get(1)).thenThrow(ServiceException.class);

        ModelAndView actualMV = mainController.watchList(1, 100, 1);
        Assert.assertEquals("error", actualMV.getViewName());

        verify(userServiceMock, times(1)).get(1);
    }

    /**
     * @see MainController#wishList(int, int, int)
     * */
    @Test
    public void wishList_Success() {
        when(listServiceMock.showOwnPlanned(1, 12)).thenReturn(films);
        when(userServiceMock.get(1)).thenReturn(user);

        ModelAndView actualMV = mainController.wishList(1, 100, 1);

        Assert.assertEquals("index", actualMV.getViewName());
        Assert.assertEquals(films, actualMV.getModel().get("films"));
        Assert.assertEquals(1, actualMV.getModel().get("userId"));
        Assert.assertEquals("hulk (bbanner@avengers.com)", actualMV.getModel().get("user"));
        Assert.assertEquals(100, actualMV.getModel().get("userAuth"));
        Assert.assertEquals(1, actualMV.getModel().get("currPage"));
        Assert.assertEquals("wish_list", actualMV.getModel().get("page"));

        verify(listServiceMock, times(1)).showOwnPlanned(1, 12);
        verify(listServiceMock, times(1)).totalNumberOfFilmsInAList(1, false);
        verify(userServiceMock, times(1)).get(1);
    }

    /**
     * @see MainController#wishList(int, int, int)
     * */
    @Test
    public void wishList_InternalError_Fail() {
        when(userServiceMock.get(1)).thenThrow(ServiceException.class);

        ModelAndView actualMV = mainController.wishList(1, 100, 1);
        Assert.assertEquals("error", actualMV.getViewName());

        verify(userServiceMock, times(1)).get(1);
    }

    /**
     * @see MainController#getWatchedByOtherUser(int, int, java.lang.String, int)
     * */
    @Test
    public void othersWatched_Success() {
        when(userServiceMock.get(1)).thenReturn(user);
        when(userServiceMock.getByNick("hulk")).thenReturn(user);
        when(listServiceMock.showOthersWatched(user.getId(), 2)).thenReturn(films);

        ModelAndView actualMV = mainController.getWatchedByOtherUser(user.getId(), 100, "hulk", 2);

        Assert.assertEquals("index", actualMV.getViewName());
        Assert.assertEquals(films, actualMV.getModel().get("films"));
        Assert.assertEquals(1, actualMV.getModel().get("userId"));
        Assert.assertEquals("hulk (bbanner@avengers.com)", actualMV.getModel().get("user"));
        Assert.assertEquals(100, actualMV.getModel().get("userAuth"));
        Assert.assertEquals(2, actualMV.getModel().get("currPage"));
        Assert.assertEquals("OthersWatched", actualMV.getModel().get("page"));

        verify(userServiceMock, times(1)).get(1);
        verify(userServiceMock, times(1)).getByNick("hulk");
        verify(listServiceMock, times(1)).showOthersWatched(user.getId(), 2);
    }

    /**
     * @see MainController#getWatchedByOtherUser(int, int, java.lang.String, int)
     * */
    @Test
    public void othersWatched_InternalError_Fail() {
        when(userServiceMock.get(1)).thenThrow(ServiceException.class);

        ModelAndView actualMV = mainController.getWatchedByOtherUser(1, 100, "hulk", 1);
        Assert.assertEquals("error", actualMV.getViewName());

        verify(userServiceMock, times(1)).get(1);
    }

    /**
     * @see MainController#getPlannedByOtherUser(int, int, String, int)
     * */
    @Test
    public void othersPlanneded_Success() {
        when(userServiceMock.get(1)).thenReturn(user);
        when(userServiceMock.getByNick("hulk")).thenReturn(user);
        when(listServiceMock.showOthersPlanned(user.getId(), 2)).thenReturn(films);

        ModelAndView actualMV = mainController.getPlannedByOtherUser(user.getId(), 100, "hulk", 2);

        Assert.assertEquals("index", actualMV.getViewName());
        Assert.assertEquals(films, actualMV.getModel().get("films"));
        Assert.assertEquals(1, actualMV.getModel().get("userId"));
        Assert.assertEquals("hulk (bbanner@avengers.com)", actualMV.getModel().get("user"));
        Assert.assertEquals(100, actualMV.getModel().get("userAuth"));
        Assert.assertEquals(2, actualMV.getModel().get("currPage"));
        Assert.assertEquals("OthersPlanned", actualMV.getModel().get("page"));

        verify(userServiceMock, times(1)).get(1);
        verify(userServiceMock, times(1)).getByNick("hulk");
        verify(listServiceMock, times(1)).showOthersPlanned(user.getId(), 2);
    }

    /**
     * @see MainController#getPlannedByOtherUser(int, int, String, int)
     * */
    @Test
    public void othersPlanned_InternalError_Fail() {
        when(userServiceMock.get(1)).thenThrow(ServiceException.class);

        ModelAndView actualMV = mainController.getPlannedByOtherUser(1, 100, "hulk", 1);
        Assert.assertEquals("error", actualMV.getViewName());

        verify(userServiceMock, times(1)).get(1);
    }
}
