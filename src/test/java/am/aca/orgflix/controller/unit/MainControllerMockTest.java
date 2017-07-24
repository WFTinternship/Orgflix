package am.aca.orgflix.controller.unit;

import am.aca.orgflix.BaseUnitTest;
import am.aca.orgflix.controller.MainController;
import am.aca.orgflix.entity.Film;
import am.aca.orgflix.entity.User;
import am.aca.orgflix.service.FilmService;
import am.aca.orgflix.service.ListService;
import am.aca.orgflix.service.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Unit tests for Main Controller
 */
public class MainControllerMockTest extends BaseUnitTest {
    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private MockHttpSession session;

    @Autowired
    private MainController mainController;

    @Mock
    private UserService userServiceMock;

    @Mock
    private FilmService filmServiceMock;

    @Mock
    private ListService listServiceMock;

    private MockMvc mockMvc;

    private List<Film> films = new ArrayList<>();
    private Film film = new Film("Snowpiercer", 2013);
    private String[] ratings = new String[8];
    private int[] totalPrivacy = {0, 1, 1, 0, 1, 0, 0, 0};
    private User user = new User("hulk", "Bruce Banner", "bbanner@avengers.com", "natasha");

    /**
     * Configures Mockito
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(mainController, "userService", userServiceMock);
        ReflectionTestUtils.setField(mainController, "filmService", filmServiceMock);
        ReflectionTestUtils.setField(mainController, "listService", listServiceMock);
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

        user.setId(1);
    }

    /**
     * Assures no more methods of Service mocks are invoked
     */
    @After
    public void tearDown() {
        verifyNoMoreInteractions(userServiceMock);
        verifyNoMoreInteractions(filmServiceMock);
        verifyNoMoreInteractions(listServiceMock);
    }

    /**
     * @see MainController#indexPageFirtVisit(HttpSession)
     */
    @Test
    public void index_Success() throws Exception {
        when(filmServiceMock.getAll(0, 12)).thenReturn(films);
        when(filmServiceMock.getTotalNumber()).thenReturn(0);
        when(filmServiceMock.getAllRatings(0, 12)).thenReturn(ratings);

        mockMvc.perform(get("/")).andExpect(view().name("home"))
                .andExpect(model().attribute("films", films))
                .andExpect(model().attribute("ratings", ratings))
                .andExpect(model().attribute("currentPage", 0))
                .andExpect(model().attribute("page", "index"))
                .andExpect(model().attribute("numOfPages", 0));

        verify(filmServiceMock, times(1)).getAll(0, 12);
        verify(filmServiceMock, times(1)).getTotalNumber();
        verify(filmServiceMock, times(1)).getAllRatings(0, 12);
    }

    /**
     * @see MainController#indexPage(int)
     */
    @Test
    public void paging_Valid_Success() throws Exception {
        when(filmServiceMock.getAll(12, 12)).thenReturn(films);
        when(filmServiceMock.getTotalNumber()).thenReturn(films.size());
        when(filmServiceMock.getAllRatings(1, 12)).thenReturn(ratings);

        mockMvc.perform(get("/index?currentPage=1")).andExpect(view().name("home"))
                .andExpect(model().attribute("films", films))
                .andExpect(model().attribute("numOfPages", films.size()))
                .andExpect(model().attribute("ratings", ratings))
                .andExpect(model().attribute("currentPage", 1))
                .andExpect(model().attribute("page", "index"));

        verify(filmServiceMock, times(1)).getAll(12, 12);
        verify(filmServiceMock, times(1)).getTotalNumber();
        verify(filmServiceMock, times(1)).getAllRatings(1, 12);
    }

    /**
     * @see MainController#indexPage(int)
     */
    @Test
    public void paging_Exception_Success() throws Exception {
        when(filmServiceMock.getAll(12, 12)).thenReturn(films);
        when(filmServiceMock.getTotalNumber()).thenReturn(films.size());
        when(filmServiceMock.getAllRatings(1, 12)).thenThrow(RuntimeException.class);

        mockMvc.perform(get("/index?currentPage=1")).andExpect(view().name("error"));

        verify(filmServiceMock, times(1)).getAll(12, 12);
        verify(filmServiceMock, times(1)).getTotalNumber();
        verify(filmServiceMock, times(1)).getAllRatings(1, 12);
    }

    /**
     * @see MainController#watchList(javax.servlet.http.HttpSession, int)
     */
    @Test
    public void watchList_Valid_Success() throws Exception {
        for (int i = 0; i < 8; i++) {
            films.add(film);
        }
        session.setAttribute("userId", 1);

        when(listServiceMock.showOwnWatched(1, 12)).thenReturn(films);
        when(listServiceMock.filmPrivacyList(1, true)).thenReturn(totalPrivacy);
        when(listServiceMock.filmPrivacyList(1, false)).thenReturn(totalPrivacy);

        mockMvc.perform(get("/watch_list?currentPage=1")
                .session(session)).andExpect(view().name("home"))
                .andExpect(model().attribute("films", films))
                .andExpect(model().attribute("numOfPages", totalPrivacy.length / 12))
                .andExpect(model().attribute("privacyList", totalPrivacy))
                .andExpect(model().attribute("currentPage", 1))
                .andExpect(model().attribute("page", "watch_list"));

        verify(listServiceMock, times(1)).showOwnWatched(1, 12);
        verify(listServiceMock, times(1)).filmPrivacyList(1, true);
        verify(listServiceMock, times(1)).filmPrivacyList(1, false);
    }

    /**
     * @see MainController#watchList(javax.servlet.http.HttpSession, int)
     */
    @Test
    public void watchList_InvalidUser_Fail() throws Exception {
        session.setAttribute("userId", -1);

        mockMvc.perform(get("/watch_list?currentPage=1")
                .session(session)).andExpect(view().name("error"))
                .andExpect(model().attribute("message", "You are not logged in, please first login"));
    }

    /**
     * @see MainController#watchList(javax.servlet.http.HttpSession, int)
     */
    @Test
    public void watchList_Exception_Fail() throws Exception {
        for (int i = 0; i < 8; i++) {
            films.add(film);
        }
        session.setAttribute("userId", 1);

        when(listServiceMock.showOwnWatched(1, 12)).thenReturn(films);
        when(listServiceMock.filmPrivacyList(1, true)).thenReturn(totalPrivacy);
        when(listServiceMock.filmPrivacyList(1, false)).thenThrow(RuntimeException.class);

        mockMvc.perform(get("/watch_list?currentPage=1")
                .session(session)).andExpect(view().name("error"));

        verify(listServiceMock, times(1)).showOwnWatched(1, 12);
        verify(listServiceMock, times(1)).filmPrivacyList(1, true);
        verify(listServiceMock, times(1)).filmPrivacyList(1, false);
    }

    /**
     * @see MainController#wishList(javax.servlet.http.HttpSession, int)
     */
    @Test
    public void wishList_Valid_Success() throws Exception {
        for (int i = 0; i < 8; i++) {
            films.add(film);
        }
        session.setAttribute("userId", 1);

        when(listServiceMock.showOwnPlanned(1, 12)).thenReturn(films);
        when(listServiceMock.filmPrivacyList(1, false)).thenReturn(totalPrivacy);

        mockMvc.perform(get("/wish_list?currentPage=1")
                .session(session)).andExpect(view().name("home"))
                .andExpect(model().attribute("films", films))
                .andExpect(model().attribute("numOfPages", totalPrivacy.length / 12))
                .andExpect(model().attribute("privacyList", totalPrivacy))
                .andExpect(model().attribute("currentPage", 1))
                .andExpect(model().attribute("page", "wish_list"));

        verify(listServiceMock, times(1)).showOwnPlanned(1, 12);
        verify(listServiceMock, times(2)).filmPrivacyList(1, false);
    }

    /**
     * @see MainController#wishList(javax.servlet.http.HttpSession, int)
     */
    @Test
    public void wishList_InvalidUser_Fail() throws Exception {
        session.setAttribute("userId", -1);

        mockMvc.perform(get("/wish_list?currentPage=1")
                .session(session)).andExpect(view().name("error"))
                .andExpect(model().attribute("message", "You are not logged in, please first login"));
    }

    /**
     * @see MainController#wishList(javax.servlet.http.HttpSession, int)
     */
    @Test
    public void wishList_Exception_Fail() throws Exception {
        for (int i = 0; i < 8; i++) {
            films.add(film);
        }
        session.setAttribute("userId", 1);

        when(listServiceMock.showOwnPlanned(1, 12)).thenReturn(films);
        when(listServiceMock.filmPrivacyList(1, false)).thenThrow(RuntimeException.class);

        mockMvc.perform(get("/wish_list?currentPage=1")
                .session(session)).andExpect(view().name("error"));

        verify(listServiceMock, times(1)).showOwnPlanned(1, 12);
        verify(listServiceMock, times(1)).filmPrivacyList(1, false);
    }

    /**
     * @see MainController#getWatchedByOtherUser(javax.servlet.http.HttpSession, int, int)
     */
    @Test
    public void othersWatched_Success() throws Exception {
        session.setAttribute("userId", 1);

        when(userServiceMock.getById(2)).thenReturn(user);
        when(listServiceMock.showOthersWatched(2, 1)).thenReturn(films);

        mockMvc.perform(get("/watchListUserOther?otherUser=2&currentPage=1").session(session))
                .andExpect(view().name("home"))
                .andExpect(model().attribute("films", films))
                .andExpect(model().attribute("currentPage", 1))
                .andExpect(model().attribute("page", "watchListUserOther"))
                .andExpect(model().attribute("otherUser", 2))
                .andExpect(model().attribute("title", "Watch list of user hulk"));


        verify(userServiceMock, times(1)).getById(2);
        verify(listServiceMock, times(1)).showOthersWatched(2, 1);
    }

    /**
     * @see MainController#getWatchedByOtherUser(javax.servlet.http.HttpSession, int, int)
     */
    @Test
    public void othersWatched_InvalidUser_Fail() throws Exception {
        session.setAttribute("userId", -1);

        mockMvc.perform(get("/watchListUserOther?otherUser=2&currentPage=1").session(session))
                .andExpect(view().name("error"));
    }

    /**
     * @see MainController#getWatchedByOtherUser(javax.servlet.http.HttpSession, int, int)
     */
    @Test
    public void othersWatched_Exception_Fail() throws Exception {
        session.setAttribute("userId", 1);

        when(userServiceMock.getById(2)).thenThrow(RuntimeException.class);
        when(listServiceMock.showOthersWatched(2, 1)).thenReturn(films);

        mockMvc.perform(get("/watchListUserOther?otherUser=2&currentPage=1").session(session))
                .andExpect(view().name("error"));


        verify(userServiceMock, times(1)).getById(2);
        verify(listServiceMock, times(1)).showOthersWatched(2, 1);
    }

    /**
     * @see MainController#getPlannedByOtherUser(javax.servlet.http.HttpSession, int, int)
     */
    @Test
    public void othersPlanned_Valid_Success() throws Exception {
        session.setAttribute("userId", 1);

        when(userServiceMock.getById(2)).thenReturn(user);
        when(listServiceMock.showOthersPlanned(2, 1)).thenReturn(films);

        mockMvc.perform(get("/wishListUserOther?otherUser=2&currentPage=1").session(session))
                .andExpect(view().name("home"))
                .andExpect(model().attribute("films", films))
                .andExpect(model().attribute("currentPage", 1))
                .andExpect(model().attribute("page", "wishListUserOther"))
                .andExpect(model().attribute("otherUser", 2))
                .andExpect(model().attribute("title", "Wish list of user hulk"));


        verify(userServiceMock, times(1)).getById(2);
        verify(listServiceMock, times(1)).showOthersPlanned(2, 1);
    }

    /**
     * @see MainController#getPlannedByOtherUser(javax.servlet.http.HttpSession, int, int)
     */
    @Test
    public void othersPlanned_InvalidUser_Fail() throws Exception {
        session.setAttribute("userId", -1);

        mockMvc.perform(get("/wishListUserOther?otherUser=2&currentPage=1").session(session))
                .andExpect(view().name("error"));
    }

    /**
     * @see MainController#getPlannedByOtherUser(javax.servlet.http.HttpSession, int, int)
     */
    @Test
    public void othersPlanned_InternalError_Fail() throws Exception {
        session.setAttribute("userId", 1);

        when(userServiceMock.getById(2)).thenThrow(RuntimeException.class);
        when(listServiceMock.showOthersPlanned(2, 1)).thenReturn(films);

        mockMvc.perform(get("/wishListUserOther?otherUser=2&currentPage=1").session(session))
                .andExpect(view().name("error"));


        verify(userServiceMock, times(1)).getById(2);
        verify(listServiceMock, times(1)).showOthersPlanned(2, 1);
    }
}