package am.aca.orgflix.controller.unit;

import am.aca.orgflix.BaseUnitTest;
import am.aca.orgflix.controller.MainController;
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
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
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
     * @see MainController#indexPageFirtVisit(HttpSession)
     */
    @Test
    public void index_Success() {
        when(filmServiceMock.getAll(0, 12)).thenReturn(films);
        when(filmServiceMock.getTotalNumber()).thenReturn(0);
        when(filmServiceMock.getAllRatings(0, 12)).thenReturn(ratings);

        ModelAndView actualMV = mainController.indexPageFirtVisit(new MockHttpSession());

        Assert.assertEquals("index", actualMV.getViewName());
        Assert.assertEquals(films, actualMV.getModel().get("films"));
        Assert.assertEquals(ratings, actualMV.getModel().get("ratings"));
        Assert.assertEquals(-1, actualMV.getModel().get("userId"));
        Assert.assertEquals(0, actualMV.getModel().get("userAuth"));
        Assert.assertEquals(0, actualMV.getModel().get("currPage"));
        Assert.assertEquals("index", actualMV.getModel().get("page"));
        Assert.assertEquals(0, actualMV.getModel().get("numOfPages"));

        verify(filmServiceMock, times(1)).getAll(0, 12);
        verify(filmServiceMock, times(1)).getTotalNumber();
        verify(filmServiceMock, times(1)).getAllRatings(0, 12);
    }

    /**
     * @see MainController#indexPageFirtVisit(HttpSession)
     */
    @Test
    public void index_ExceptionThrown_Fail() {
        when(filmServiceMock.getAll(0, 12)).thenThrow(ServiceException.class);

        ModelAndView actualMV = mainController.indexPageFirtVisit(new MockHttpSession());

        Assert.assertEquals("error", actualMV.getViewName());

        verify(filmServiceMock, times(1)).getAll(0, 12);
    }


    /**
     * @see MainController#indexPage( int)
     */
    @Test
    public void paging_Authenticated_Success() {
        when(filmServiceMock.getAll(12, 12)).thenReturn(films);
        when(filmServiceMock.getTotalNumber()).thenReturn(0);
        when(userServiceMock.getById(1)).thenReturn(user);

        ModelAndView actualMV = mainController.indexPage( 1);

        Assert.assertEquals("index", actualMV.getViewName());
        Assert.assertEquals(films, actualMV.getModel().get("films"));
        Assert.assertEquals(1, actualMV.getModel().get("userId"));
        Assert.assertEquals("hulk (bbanner@avengers.com)", actualMV.getModel().get("user"));
        Assert.assertEquals(100, actualMV.getModel().get("userAuth"));
        Assert.assertEquals(1, actualMV.getModel().get("currPage"));
        Assert.assertEquals("index", actualMV.getModel().get("page"));

        verify(filmServiceMock, times(1)).getAll(12, 12);
        verify(filmServiceMock, times(1)).getTotalNumber();
        verify(userServiceMock, times(1)).getById(1);
    }

    /**
     * @see MainController#indexPage(int)
     */
    @Test
    public void paging_UnAuthenticated_Success() {
        when(filmServiceMock.getAll(24, 12)).thenReturn(films);
        when(filmServiceMock.getTotalNumber()).thenReturn(0);
        when(userServiceMock.getById(1)).thenReturn(user);

        ModelAndView actualMV = mainController.indexPage(2);

        Assert.assertEquals("index", actualMV.getViewName());
        Assert.assertEquals(films, actualMV.getModel().get("films"));
        Assert.assertEquals(-1, actualMV.getModel().get("userId"));
        Assert.assertEquals("", actualMV.getModel().get("user"));
        Assert.assertEquals(100, actualMV.getModel().get("userAuth"));
        Assert.assertEquals(2, actualMV.getModel().get("currPage"));
        Assert.assertEquals("index", actualMV.getModel().get("page"));

        verify(filmServiceMock, times(1)).getAll(24, 12);
        verify(filmServiceMock, times(1)).getTotalNumber();
    }

    /**
     * @see MainController#indexPage(int)
     */
    @Test
    public void paging_InternalError_Fail() {
        when(userServiceMock.getById(1)).thenThrow(ServiceException.class);

        ModelAndView actualMV = mainController.indexPage(2);

        Assert.assertEquals("error", actualMV.getViewName());

        verify(userServiceMock, times(1)).getById(1);
    }

    /**
     * @see MainController#indexPage(int)
     */
    @Test
    public void pagingWAuth_Success() {
        when(userServiceMock.getById(user.getId())).thenReturn(user);
        when(filmServiceMock.getAll(0, 12)).thenReturn(films);
        when(filmServiceMock.getTotalNumber()).thenReturn(0);

        ModelAndView actualMV = mainController.indexPage(0);

        Assert.assertEquals("index", actualMV.getViewName());
        Assert.assertEquals(films, actualMV.getModel().get("films"));
        Assert.assertEquals(user.getId(), actualMV.getModel().get("userId"));
        Assert.assertEquals("hulk (bbanner@avengers.com)", actualMV.getModel().get("user"));
        Assert.assertEquals(100, actualMV.getModel().get("userAuth"));
        Assert.assertEquals(0, actualMV.getModel().get("currPage"));
        Assert.assertEquals("index", actualMV.getModel().get("page"));

        verify(filmServiceMock, times(1)).getAll(0, 12);
        verify(filmServiceMock, times(1)).getTotalNumber();
        verify(userServiceMock, times(1)).getById(user.getId());
    }

    /**
     * @see MainController#indexPage(int)
     */
    @Test
    public void pagingWAuth_AuthenticationError_Fail() {
        when(userServiceMock.getById(user.getId())).thenReturn(null);

        ModelAndView actualMV = mainController.indexPage(0);

        Assert.assertEquals("error", actualMV.getViewName());

        verify(userServiceMock, times(1)).getById(user.getId());
    }

    /**
     * @see MainController#indexPage(int)
     */
    @Test
    public void pagingWAuth_InternalError_Fail() {
        when(userServiceMock.getById(user.getId())).thenReturn(user);
        when(filmServiceMock.getAll(0, 12)).thenThrow(ServiceException.class);

        ModelAndView actualMV = mainController.indexPage(0);

        Assert.assertEquals("error", actualMV.getViewName());

        verify(userServiceMock, times(1)).getById(user.getId());
        verify(filmServiceMock, times(1)).getAll(0, 12);
    }

    /**
     * @see MainController#watchList(javax.servlet.http.HttpSession, int)
     */
    @Test
    public void watchList_Success() {
        when(listServiceMock.showOwnWatched(1, 12)).thenReturn(films);
        when(listServiceMock.filmPrivacyList(1, true)).thenReturn(new int[0]);
        when(userServiceMock.getById(1)).thenReturn(user);

        ModelAndView actualMV = mainController.watchList(new MockHttpSession(), 1);

        Assert.assertEquals("index", actualMV.getViewName());
        Assert.assertEquals(films, actualMV.getModel().get("films"));
        Assert.assertEquals(1, actualMV.getModel().get("userId"));
        Assert.assertEquals("hulk (bbanner@avengers.com)", actualMV.getModel().get("user"));
        Assert.assertEquals(100, actualMV.getModel().get("userAuth"));
        Assert.assertEquals(1, actualMV.getModel().get("currPage"));
        Assert.assertEquals("watch_list", actualMV.getModel().get("page"));

        verify(listServiceMock, times(1)).showOwnWatched(1, 12);
        verify(listServiceMock, times(1)).filmPrivacyList(1, true);
        verify(userServiceMock, times(1)).getById(1);
    }

    /**
     * @see MainController#watchList(javax.servlet.http.HttpSession, int)
     */
    @Test
    public void watchList_InternalError_Fail() {
        when(userServiceMock.getById(1)).thenThrow(ServiceException.class);

        ModelAndView actualMV = mainController.watchList(new MockHttpSession(), 1);
        Assert.assertEquals("error", actualMV.getViewName());

        verify(userServiceMock, times(1)).getById(1);
    }

    /**
     * @see MainController#wishList(javax.servlet.http.HttpSession, int)
     */
    @Test
    public void wishList_Success() {
        when(listServiceMock.showOwnPlanned(1, 12)).thenReturn(films);
        when(userServiceMock.getById(1)).thenReturn(user);

        ModelAndView actualMV = mainController.wishList(new MockHttpSession(), 1);

        Assert.assertEquals("index", actualMV.getViewName());
        Assert.assertEquals(films, actualMV.getModel().get("films"));
        Assert.assertEquals(1, actualMV.getModel().get("userId"));
        Assert.assertEquals("hulk (bbanner@avengers.com)", actualMV.getModel().get("user"));
        Assert.assertEquals(100, actualMV.getModel().get("userAuth"));
        Assert.assertEquals(1, actualMV.getModel().get("currPage"));
        Assert.assertEquals("wish_list", actualMV.getModel().get("page"));

        verify(listServiceMock, times(1)).showOwnPlanned(1, 12);
        verify(listServiceMock, times(1)).filmPrivacyList(1, false);
        verify(userServiceMock, times(1)).getById(1);
    }

    /**
     * @see MainController#wishList(javax.servlet.http.HttpSession, int)
     */
    @Test
    public void wishList_InternalError_Fail() {
        when(userServiceMock.getById(1)).thenThrow(ServiceException.class);

        ModelAndView actualMV = mainController.wishList(new MockHttpSession(), 1);
        Assert.assertEquals("error", actualMV.getViewName());

        verify(userServiceMock, times(1)).getById(1);
    }

    /**
     * @see MainController#getWatchedByOtherUser(javax.servlet.http.HttpSession, int, int)
     */
    @Test
    public void othersWatched_Success() {
        when(userServiceMock.getById(1)).thenReturn(user);
//        when(userServiceMock.getByNick("hulk")).thenReturn(user);
        when(listServiceMock.showOthersWatched(user.getId(), 2)).thenReturn(films);

        ModelAndView actualMV = mainController.getWatchedByOtherUser(new MockHttpSession(), 1, 2);

        Assert.assertEquals("index", actualMV.getViewName());
        Assert.assertEquals(films, actualMV.getModel().get("films"));
        Assert.assertEquals(1, actualMV.getModel().get("userId"));
        Assert.assertEquals("hulk (bbanner@avengers.com)", actualMV.getModel().get("user"));
        Assert.assertEquals(100, actualMV.getModel().get("userAuth"));
        Assert.assertEquals(2, actualMV.getModel().get("currPage"));
        Assert.assertEquals("OthersWatched", actualMV.getModel().get("page"));

        verify(userServiceMock, times(1)).getById(1);
//        verify(userServiceMock, times(1)).getByNick("hulk");
        verify(listServiceMock, times(1)).showOthersWatched(user.getId(), 2);
    }

    /**
     * @see MainController#getWatchedByOtherUser(javax.servlet.http.HttpSession, int, int)
     */
    @Test
    public void othersWatched_InternalError_Fail() {
        when(userServiceMock.getById(1)).thenThrow(ServiceException.class);

        ModelAndView actualMV = mainController.getWatchedByOtherUser(new MockHttpSession(), 1, 1);
        Assert.assertEquals("error", actualMV.getViewName());

        verify(userServiceMock, times(1)).getById(1);
    }

    /**
     * @see MainController#getPlannedByOtherUser(javax.servlet.http.HttpSession, int, int)
     */
    @Test
    public void othersPlanneded_Success() {
        when(userServiceMock.getById(1)).thenReturn(user);
//        when(userServiceMock.getByNick("hulk")).thenReturn(user);
        when(listServiceMock.showOthersPlanned(user.getId(), 2)).thenReturn(films);

        ModelAndView actualMV = mainController.getPlannedByOtherUser(new MockHttpSession(), 1, 2);

        Assert.assertEquals("index", actualMV.getViewName());
        Assert.assertEquals(films, actualMV.getModel().get("films"));
        Assert.assertEquals(1, actualMV.getModel().get("userId"));
        Assert.assertEquals("hulk (bbanner@avengers.com)", actualMV.getModel().get("user"));
        Assert.assertEquals(100, actualMV.getModel().get("userAuth"));
        Assert.assertEquals(2, actualMV.getModel().get("currPage"));
        Assert.assertEquals("OthersPlanned", actualMV.getModel().get("page"));

        verify(userServiceMock, times(1)).getById(1);
//        verify(userServiceMock, times(1)).getByNick("hulk");
        verify(listServiceMock, times(1)).showOthersPlanned(user.getId(), 2);
    }

    /**
     * @see MainController#getPlannedByOtherUser(javax.servlet.http.HttpSession, int, int)
     */
    @Test
    public void othersPlanned_InternalError_Fail() {
        when(userServiceMock.getById(1)).thenThrow(ServiceException.class);

        ModelAndView actualMV = mainController.getPlannedByOtherUser(new MockHttpSession(), 1, 1);
        Assert.assertEquals("error", actualMV.getViewName());

        verify(userServiceMock, times(1)).getById(1);
    }
}