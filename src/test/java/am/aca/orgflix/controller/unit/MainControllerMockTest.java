package am.aca.orgflix.controller.unit;

import am.aca.orgflix.BaseUnitTest;
import am.aca.orgflix.controller.MainController;
import am.aca.orgflix.entity.Film;
import am.aca.orgflix.service.CastService;
import am.aca.orgflix.service.FilmService;
import am.aca.orgflix.service.ListService;
import am.aca.orgflix.service.UserService;
import org.junit.*;
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

    /**
     * Configures Mockito
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(mainController, "userService", userServiceMock);
        ReflectionTestUtils.setField(mainController, "castService", castServiceMock);
        ReflectionTestUtils.setField(mainController, "filmService", filmServiceMock);
        ReflectionTestUtils.setField(mainController, "listService", listServiceMock);
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
     * */
    @Ignore
    @Test
    public void index_Success() {
        when(filmServiceMock.getFilmsList(0)).thenReturn(films);

        ModelAndView actualMV = mainController.index();

        ModelAndView otherMV = new ModelAndView("index");
        otherMV.addObject("films", films);
        otherMV.addObject("userId", -1);
        otherMV.addObject("userAuth", 0);
        otherMV.addObject("currPage", 0);
        otherMV.addObject("page", "main");

        ModelAndView expectedMV = new ModelAndView("index");
        expectedMV.addObject("films", films);
        expectedMV.addObject("userId", -1);
        expectedMV.addObject("userAuth", 0);
        expectedMV.addObject("currPage", 0);
        expectedMV.addObject("page", "main");
        Assert.assertEquals(expectedMV, otherMV );

        verify(filmServiceMock, times(1)).getFilmsList(0);
    }
}
