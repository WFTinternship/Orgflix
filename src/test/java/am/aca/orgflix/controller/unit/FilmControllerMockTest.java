package am.aca.orgflix.controller.unit;

import am.aca.orgflix.BaseUnitTest;
import am.aca.orgflix.controller.FilmController;
import am.aca.orgflix.entity.Cast;
import am.aca.orgflix.entity.Film;
import am.aca.orgflix.entity.Genre;
import am.aca.orgflix.service.CastService;
import am.aca.orgflix.service.FilmService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Unit tests for Film Controller
 */
public class FilmControllerMockTest extends BaseUnitTest {
    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private MockHttpSession session;

    @Autowired
    private FilmController filmController;

    @Mock
    private FilmService filmServiceMock;

    @Mock
    private CastService castServiceMock;

    private MockMvc mockMvc;

    private Cast cast = new Cast("Thandie Newton");
    private List<Cast> casts = new ArrayList<>();
    private List<Film> films = new ArrayList<>();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(filmController, "castService", castServiceMock);
        ReflectionTestUtils.setField(filmController, "filmService", filmServiceMock);
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @After
    public void tearDown() {
        verifyZeroInteractions(castServiceMock);
        verifyZeroInteractions(filmServiceMock);
    }

    /**
     * @see FilmController#addFilm(HttpSession)
     */
    @Test
    public void addFilm_Valid_Success() throws Exception {
        casts.add(cast);

        session.setAttribute("userId", 1);

        when(castServiceMock.getAll()).thenReturn(casts);
        mockMvc.perform(get("/addFilm").session(session))
                .andExpect(view().name("newFilm"))
                .andExpect(model().attribute("actors", casts));

        verify(castServiceMock, times(1)).getAll();
    }

    /**
     * @see FilmController#addFilm(HttpSession)
     */
    @Test
    public void addFilm_InvalidUser_Fail() throws Exception {
        session.setAttribute("userId", -1);

        mockMvc.perform(get("/addFilm").session(session))
                .andExpect(view().name("error"))
                .andExpect(model().attribute("message", "You are not logged in, please first login"));
    }

    /**
     * @see FilmController#addFilm(HttpSession)
     */
    @Test
    public void addFilm_ServiceException_Fail() throws Exception {
        session.setAttribute("userId", 1);

        when(castServiceMock.getAll()).thenThrow(RuntimeException.class);

        mockMvc.perform(get("/addFilm").session(session))
                .andExpect(view().name("error"))
                .andExpect(model().attributeExists("message"));

        verify(castServiceMock, times(1)).getAll();
    }

    /**
     * @see FilmController#searchFilm(HttpSession)
     */
    @Test
    public void searchFilm_Valid_Success() throws Exception {
        session.setAttribute("userId", 1);

        when(castServiceMock.getAll()).thenReturn(casts);

        mockMvc.perform(post("/searchFilm").session(session))
                .andExpect(view().name("findFilm"))
                .andExpect(model().attribute("actors", casts))
                .andExpect(model().attribute("genres", Genre.values()));

        verify(castServiceMock, times(1)).getAll();
    }

    /**
     * @see FilmController#searchFilm(HttpSession)
     */
    @Test
    public void searchFilm_InvalidUser_Fail() throws Exception {
        session.setAttribute("userId", -1);

        mockMvc.perform(post("/searchFilm").session(session))
                .andExpect(view().name("error"))
                .andExpect(model().attribute("message", "You are not logged in, please first login"));
    }

    /**
     * @see FilmController#searchFilm(HttpSession)
     */
    @Test
    public void searchFilm_ServiceException_Fail() throws Exception {
        session.setAttribute("userId", 1);

        when(castServiceMock.getAll()).thenThrow(RuntimeException.class);

        mockMvc.perform(get("/searchFilm").session(session))
                .andExpect(view().name("error"))
                .andExpect(model().attributeExists("message"));

        verify(castServiceMock, times(1)).getAll();
    }

    /**
     * @see FilmController#searchFilm(HttpSession, String, int, int, int, String, int, int)
     */
    @Test
    public void searchFilmResult_Valid_Success() throws Exception {
        session.setAttribute("userId", 1);

        when(filmServiceMock.getFilteredFilms("test", 0, 0, false, "test", 0, 0)).thenReturn(films);

        mockMvc.perform(post("/searchFilmResult").param("title", "test")
                .param("startYear", "0").param("finishYear", "0")
                .param("hasOscar", "0")
                .param("director", "test").param("actorId", "0")
                .param("genre", "0")
                .session(session)).andExpect(view().name("home"))
                .andExpect(model().attribute("films", films))
                .andExpect(model().attribute("page", "home"));

        verify(filmServiceMock, times(1)).getFilteredFilms("test", 0, 0, false, "test", 0, 0);
    }

    /**
     * @see FilmController#searchFilm(HttpSession, String, int, int, int, String, int, int)
     */
    @Test
    public void searchFilmResult_InvalidUser_Fail() throws Exception {
        session.setAttribute("userId", -1);

        mockMvc.perform(post("/searchFilmResult").param("title", "test")
                .param("startYear", "0").param("finishYear", "0")
                .param("hasOscar", "0")
                .param("director", "test").param("actorId", "0")
                .param("genre", "0")
                .session(session)).andExpect(view().name("error"))
                .andExpect(model().attribute("message", "You are not logged in, please first login"));
    }

    /**
     * @see FilmController#searchFilm(HttpSession, String, int, int, int, String, int, int)
     */
    @Test
    public void searchFilmResult_ServiceError_Fail() throws Exception {
        session.setAttribute("userId", 1);

        when(filmServiceMock.getFilteredFilms("test", 0, 0, false, "test", 0, 0)).thenThrow(RuntimeException.class);

        mockMvc.perform(post("/searchFilmResult").param("title", "test")
                .param("startYear", "0").param("finishYear", "0")
                .param("hasOscar", "0")
                .param("director", "test").param("actorId", "0")
                .param("genre", "0")
                .session(session)).andExpect(view().name("error"));

        verify(filmServiceMock, times(1)).getFilteredFilms("test", 0, 0, false, "test", 0, 0);
    }
}
