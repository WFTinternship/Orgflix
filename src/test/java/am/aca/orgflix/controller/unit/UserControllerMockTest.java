package am.aca.orgflix.controller.unit;

import am.aca.orgflix.BaseUnitTest;
import am.aca.orgflix.controller.UserController;
import am.aca.orgflix.entity.Film;
import am.aca.orgflix.entity.User;
import am.aca.orgflix.exception.ServiceException;
import am.aca.orgflix.service.FilmService;
import am.aca.orgflix.service.UserService;
import org.junit.After;
import org.junit.Assert;
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
public class UserControllerMockTest extends BaseUnitTest {
    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private MockHttpSession session;

    @Autowired
    private UserController userController;

    @Mock
    private UserService userServiceMock;

    @Mock
    private FilmService filmServiceMock;

    private MockMvc mockMvc;

    private List<Film> films = new ArrayList<>();
    private String[] ratings = new String[8];
    private User user = new User("hulk", "Bruce Banner", "bbanner@avengers.com", "natasha");

    /**
     * Configures Mockito
     */
    @Before
    @SuppressWarnings("Duplicates")
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(userController, "userService", userServiceMock);
        ReflectionTestUtils.setField(userController, "filmService", filmServiceMock);
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
    }

    /**
     * @see UserController#signup()
     */
    @Test
    public void signUp_Valid_Success() throws Exception {
        when(filmServiceMock.getAll(0, 12)).thenReturn(films);
        when(filmServiceMock.getTotalNumber()).thenReturn(0);
        when(filmServiceMock.getAllRatings(0, 12)).thenReturn(ratings);

        mockMvc.perform(get("/signup")).andExpect(view().name("register"))
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
     * @see UserController#signup()
     */
    @Test
    public void signUp_Exception_Fail() throws Exception {
        when(filmServiceMock.getAll(0, 12)).thenThrow(ServiceException.class);

        mockMvc.perform(get("/signup")).andExpect(view().name("error"));

        verify(filmServiceMock, times(1)).getAll(0, 12);
    }

    /**
     * @see UserController#signupResult(javax.servlet.http.HttpSession, String, String, String, String)
     */
    @Test
    public void signUpResult_Success() throws Exception {
        when(userServiceMock.add(user)).thenReturn(2);
        when(filmServiceMock.getAll(0, 12)).thenReturn(films);
        when(filmServiceMock.getTotalNumber()).thenReturn(0);


        mockMvc.perform(get("/signed?nick=hulk&userName=Bruce%20Banner&email=bbanner@avengers.com&pass=natasha"))
                .andExpect(view().name("home"))
                .andExpect(model().attribute("films", films))
                .andExpect(model().attribute("numOfPages", 0))
                .andExpect(model().attribute("currentPage", 0))
                .andExpect(model().attribute("page", "index"));

        verify(filmServiceMock, times(1)).getAll(0, 12);
        verify(filmServiceMock, times(1)).getTotalNumber();
        verify(userServiceMock, times(1)).add(user);
    }

    /**
     * @see UserController#signupResult(javax.servlet.http.HttpSession, String, String, String, String)
     */
    @Test
    public void signUpResult_AuthenticationError_Fail() throws Exception {
        when(userServiceMock.add(user)).thenReturn(-1);

        mockMvc.perform(get("/signed?nick=hulk&userName=Bruce%20Banner&email=bbanner@avengers.com&pass=natasha"))
                .andExpect(view().name("error"))
                .andExpect(model().attribute("message", "You are not logged in, please first login"));

        verify(userServiceMock, times(1)).add(user);

    }

    /**
     * @see UserController#signupResult(javax.servlet.http.HttpSession, String, String, String, String)
     */
    @Test
    public void signUpResult_Exception_Fail() throws Exception {
        when(userServiceMock.add(user)).thenReturn(2);
        when(filmServiceMock.getAll(0, 12)).thenReturn(films);
        when(filmServiceMock.getTotalNumber()).thenThrow(RuntimeException.class);

        mockMvc.perform(get("/signed?nick=hulk&userName=Bruce%20Banner&email=bbanner@avengers.com&pass=natasha"))
                .andExpect(view().name("error"));

        verify(filmServiceMock, times(1)).getAll(0, 12);
        verify(filmServiceMock, times(1)).getTotalNumber();
        verify(userServiceMock, times(1)).add(user);
    }

    /**
     * @see UserController#login()
     */
    @Test
    public void login_Valid_Success() throws Exception {
        mockMvc.perform(get("/login")).andExpect(view().name("signIn"));
    }

    /**
     * @see UserController#logout(HttpSession)
     */
    @Test
    public void logout_Valid_Success() throws Exception {
        session.setAttribute("userId", 1);

        mockMvc.perform(get("/logout").session(session))
                .andExpect(view().name("signIn"));

        Assert.assertEquals(-1, (int) session.getAttribute("userId"));
    }

    /**
     * @see UserController#paging(HttpSession, String, String)
     */
    @Test
    public void paging_Valid_Success() throws Exception {

        when(userServiceMock.authenticate(user.getEmail(), user.getPass().hashCode() + user.getEmail())).thenReturn(user);
        when(filmServiceMock.getAll(0, 12)).thenReturn(films);
        when(filmServiceMock.getTotalNumber()).thenReturn(0);

        mockMvc.perform(get("/loggedIn?email=bbanner@avengers.com&pass=natasha")
                .session(session))
                .andExpect(view().name("home"))
        .andExpect(model().attribute("films", films))
        .andExpect(model().attribute("numOfPages", 0))
        .andExpect(model().attribute("currentPage", 0))
        .andExpect(model().attribute("page", "index"));

        Assert.assertEquals(user.getId(), session.getAttribute("userId"));
        Assert.assertEquals("hulk (bbanner@avengers.com)", session.getAttribute("user"));

        verify(filmServiceMock, times(1)).getAll(0, 12);
        verify(filmServiceMock, times(1)).getTotalNumber();
        verify(userServiceMock, times(1)).authenticate(user.getEmail(), user.getPass().hashCode() + user.getEmail());
    }

    /**
     * @see UserController#paging(HttpSession, String, String)
     */
    @Test
    public void paging_InvalidUser_Fail() throws Exception {

        when(userServiceMock.authenticate(user.getEmail(), user.getPass().hashCode() + user.getEmail())).thenReturn(null);

        mockMvc.perform(get("/loggedIn?email=bbanner@avengers.com&pass=natasha")
                .session(session))
                .andExpect(view().name("error"))
                .andExpect(model().attribute("message", "Wrong email/or password, please try again"));

        Assert.assertEquals(-1, session.getAttribute("userId"));
        Assert.assertEquals(" ", session.getAttribute("user"));

        verify(userServiceMock, times(1)).authenticate(user.getEmail(), user.getPass().hashCode() + user.getEmail());
    }

    /**
     * @see UserController#paging(HttpSession, String, String)
     */
    @Test
    public void paging_Exception_Fail() throws Exception {

        when(userServiceMock.authenticate(user.getEmail(), user.getPass().hashCode() + user.getEmail())).thenReturn(user);
        when(filmServiceMock.getAll(0, 12)).thenReturn(films);
        when(filmServiceMock.getTotalNumber()).thenThrow(RuntimeException.class);

        mockMvc.perform(get("/loggedIn?email=bbanner@avengers.com&pass=natasha")
                .session(session))
                .andExpect(view().name("error"));

        verify(filmServiceMock, times(1)).getAll(0, 12);
        verify(filmServiceMock, times(1)).getTotalNumber();
        verify(userServiceMock, times(1)).authenticate(user.getEmail(), user.getPass().hashCode() + user.getEmail());
    }
}
