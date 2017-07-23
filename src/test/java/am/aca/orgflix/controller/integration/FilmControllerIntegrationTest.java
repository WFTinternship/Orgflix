package am.aca.orgflix.controller.integration;

import am.aca.orgflix.BaseIntegrationTest;
import am.aca.orgflix.controller.FilmController;
import am.aca.orgflix.entity.Cast;
import am.aca.orgflix.entity.Film;
import am.aca.orgflix.entity.Genre;
import am.aca.orgflix.service.CastService;
import am.aca.orgflix.service.FilmService;
import am.aca.orgflix.util.TestHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Integration tests for Film Controller
 */
public class FilmControllerIntegrationTest extends BaseIntegrationTest {
    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private MockHttpSession session;

    @Autowired
    private TestHelper helper;

    @Autowired
    private FilmService filmService;

    @Autowired
    private CastService castService;

    private MockMvc mockMvc;

    private Cast cast = new Cast("Jennifer Connelly", true);
    private Film film = new Film("A Beautiful Mind", 2001);
    private List<Cast> casts = new ArrayList<>();
    private List<Film> films = new ArrayList<>();

    /**
     * Initializes mockMvc
     */
    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    /**
     * Reverts all the changes occurred in the DB due to tests
     */
    @After
    public void tearDown() {
        helper.emptyTable(new String[]{
                "CASTS",
                "FILMS"});
    }

    /**
     * @see FilmController#addFilm(HttpSession)
     */
    @Test
    public void addFilm_Valid_Success() throws Exception {
        castService.add(cast);
        casts.add(cast);

        session.setAttribute("userId", 1);

        mockMvc.perform(get("/addFilm").session(session))
                .andExpect(view().name("newFilm"))
                .andExpect(model().attribute("actors", casts));
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
     * @see FilmController#searchFilm(HttpSession)
     */
    @Test
    public void searchFilm_Valid_Success() throws Exception {
        castService.add(cast);
        casts.add(cast);

        session.setAttribute("userId", 1);

        mockMvc.perform(post("/searchFilm").session(session))
                .andExpect(view().name("findFilm"))
                .andExpect(model().attribute("actors", casts))
                .andExpect(model().attribute("genres", Genre.values()));
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
     * @see FilmController#searchFilm(HttpSession, String, int, int, int, String, int, int)
     */
    @Test
    public void searchFilmResult_Valid_Success() throws Exception {
        session.setAttribute("userId", 1);

        film.setHasOscar(true);
        film.setDirector("Ron Howard");
        filmService.add(film);
        films.add(film);

        //filters by title only
        mockMvc.perform(post("/searchFilmResult").param("title", "a beautiful mind")
                .param("startYear", "0").param("finishYear", "0")
                .param("hasOscar", "0")
                .param("director", "")
                .param("actorId", "0")
                .param("genre", "0")
                .session(session)).andExpect(view().name("home"))
                .andExpect(model().attribute("films", films))
                .andExpect(model().attribute("page", "index"));

        //filters by production year only
        mockMvc.perform(post("/searchFilmResult").param("title", "")
                .param("startYear", "2001").param("finishYear", "2001")
                .param("hasOscar", "0")
                .param("director", "")
                .param("actorId", "0")
                .param("genre", "0")
                .session(session)).andExpect(view().name("home"))
                .andExpect(model().attribute("films", films))
                .andExpect(model().attribute("page", "index"));

        //filters by oscar wins only
        mockMvc.perform(post("/searchFilmResult").param("title", "")
                .param("startYear", "0").param("finishYear", "0")
                .param("hasOscar", "1")
                .param("director", "")
                .param("actorId", "0")
                .param("genre", "0")
                .session(session)).andExpect(view().name("home"))
                .andExpect(model().attribute("films", films))
                .andExpect(model().attribute("page", "index"));

        //filters by director only
        mockMvc.perform(post("/searchFilmResult").param("title", "")
                .param("startYear", "0").param("finishYear", "0")
                .param("hasOscar", "0")
                .param("director", "Ron Howard")
                .param("actorId", "0")
                .param("genre", "0")
                .session(session)).andExpect(view().name("home"))
                .andExpect(model().attribute("films", films))
                .andExpect(model().attribute("page", "index"));

        films.remove(film);
        //filters by cast only, empty result expected
        mockMvc.perform(post("/searchFilmResult").param("title", "")
                .param("startYear", "0").param("finishYear", "0")
                .param("hasOscar", "0")
                .param("director", "")
                .param("actorId", "7")
                .param("genre", "0")
                .session(session)).andExpect(view().name("home"))
                .andExpect(model().attribute("films", films))
                .andExpect(model().attribute("page", "index"));
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
}
