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
public class UserControllerMockTest extends BaseUnitTest {
    @Autowired
    private UserController userController;

    @Mock
    private UserService userServiceMock;
    @Mock
    private FilmService filmServiceMock;

    private List<Film> films = new ArrayList<>();
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
    public void signUp_Success() {
        when(filmServiceMock.getFilmsList(0)).thenReturn(films);
        when(filmServiceMock.totalNumberOfFilms()).thenReturn(0);

        ModelAndView actualMV = userController.signup();

        Assert.assertEquals("signup", actualMV.getViewName());
        Assert.assertEquals(films, actualMV.getModel().get("films"));
        Assert.assertEquals(-1, actualMV.getModel().get("userId"));
        Assert.assertEquals(0, actualMV.getModel().get("userAuth"));
        Assert.assertEquals(0, actualMV.getModel().get("currPage"));
        Assert.assertEquals("index", actualMV.getModel().get("page"));

        verify(filmServiceMock, times(1)).getFilmsList(0);
        verify(filmServiceMock, times(1)).totalNumberOfFilms();
    }

    /**
     * @see UserController#signup()
     */
    @Test
    public void signUp_Exception_Fail() {
        when(filmServiceMock.getFilmsList(0)).thenThrow(ServiceException.class);

        ModelAndView actualMV = userController.signup();

        Assert.assertEquals("error", actualMV.getViewName());

        verify(filmServiceMock, times(1)).getFilmsList(0);
    }

    /**
     * @see UserController#signupResult(String, String, String, String)
     */
    @Test
    public void signUpResult_Success() {
        when(userServiceMock.add(user)).thenReturn(2);
        when(filmServiceMock.getFilmsList(0)).thenReturn(films);

        ModelAndView actualMV = userController.signupResult
                (user.getNick(), user.getUserName(), user.getEmail(), user.getPass());

        Assert.assertEquals("index", actualMV.getViewName());
        Assert.assertEquals(films, actualMV.getModel().get("films"));
        Assert.assertEquals(2, actualMV.getModel().get("userId"));
        Assert.assertEquals("hulk (bbanner@avengers.com)", actualMV.getModel().get("user"));
        Assert.assertEquals(user.getPass().hashCode() + user.getEmail().hashCode(), actualMV.getModel().get("userAuth"));
        Assert.assertEquals(0, actualMV.getModel().get("currPage"));
        Assert.assertEquals("index", actualMV.getModel().get("page"));

        verify(filmServiceMock, times(1)).getFilmsList(0);
        verify(userServiceMock, times(1)).add(user);
    }

    /**
     * @see UserController#signupResult(String, String, String, String)
     */
    @Test
    public void signUpResult_AuthenticationError_Fail() {
        when(userServiceMock.add(user)).thenReturn(-1);

        ModelAndView actualMV = userController.signupResult
                (user.getNick(), user.getUserName(), user.getEmail(), user.getPass());
        Assert.assertEquals("error", actualMV.getViewName());

        verify(userServiceMock, times(1)).add(user);

    }

    /**
     * @see UserController#signupResult(String, String, String, String)
     */
    @Test
    public void signUpResult_InternalError_Fail() {
        when(userServiceMock.add(user)).thenThrow(ServiceException.class);

        ModelAndView actualMV = userController.signupResult
                (user.getNick(), user.getUserName(), user.getEmail(), user.getPass());
        Assert.assertEquals("error", actualMV.getViewName());

        verify(userServiceMock, times(1)).add(user);
    }

    /**
     * @see UserController#login()
     */
    @Test
    public void login_Success() {
        ModelAndView actualMV = userController.login();
        Assert.assertEquals(("login"), actualMV.getViewName());
    }


}
