package am.aca.orgflix.controller.integration;

import am.aca.orgflix.BaseIntegrationTest;
import am.aca.orgflix.entity.Film;
import am.aca.orgflix.entity.User;
import am.aca.orgflix.service.FilmService;
import am.aca.orgflix.service.UserService;
import am.aca.orgflix.util.TestHelper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpSession;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for Film Data Controller
 */
public class FilmDataControllerIntegrationTest extends BaseIntegrationTest {
    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private MockHttpSession session;

    @Autowired
    private TestHelper helper;

    @Autowired
    private FilmService filmService;

    @Autowired
    private UserService userService;

    private MockMvc mockMvc;

    private User user = new User("hulk", "Bruce Banner", "bbanner@avengers.com", "natasha");
    private Film film = new Film("Tropic Thunder", 2008);

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
                "LISTS",
                "USERS",
                "FILMS"});
    }

    /**
     * @see am.aca.orgflix.controller.FilmDataController#addFilmToWatchList(HttpSession, int, boolean)
     */
    @Test
    public void addToWatched_Valid_Success() throws Exception {
        userService.add(user);
        filmService.add(film);

        session.setAttribute("userId", user.getId());

        MvcResult result = mockMvc.perform(post("/data/film/watch/addToList")
                .param("film", Integer.toString(film.getId()))
                .param("isPublic", "true")
                .session(session))
                .andExpect(status().isOk())
                .andReturn();

        Assert.assertEquals("Film added to watch list", result.getResponse().getContentAsString());

    }

    /**
     * @see am.aca.orgflix.controller.FilmDataController#addFilmToWatchList(HttpSession, int, boolean)
     */
    @Test
    public void addToWatched_InvalidFilm_Success() throws Exception {
        userService.add(user);

        session.setAttribute("userId", user.getId());

        MvcResult result = mockMvc.perform(post("/data/film/watch/addToList")
                .param("film", "0")
                .param("isPublic", "true")
                .session(session))
                .andExpect(status().isBadRequest())
                .andReturn();

        Assert.assertEquals("Not succeeded", result.getResponse().getContentAsString());
    }

    /**
     * @see am.aca.orgflix.controller.FilmDataController#addFilmToWatchList(HttpSession, int, boolean)
     */
    @Test
    public void addToWatched_InvalidUser_Success() throws Exception {
        filmService.add(film);

        session.setAttribute("userId", user.getId());

        MvcResult result = mockMvc.perform(post("/data/film/watch/addToList")
                .param("film", Integer.toString(film.getId()))
                .param("isPublic", "true")
                .session(session))
                .andExpect(status().isBadRequest())
                .andReturn();

        Assert.assertEquals("Not succeeded", result.getResponse().getContentAsString());
    }

    /**
     * @see am.aca.orgflix.controller.FilmDataController#addFilmToWishList(HttpSession, int, boolean)
     */
    @Test
    public void addToPlanned_Valid_Success() throws Exception {
        userService.add(user);
        filmService.add(film);

        session.setAttribute("userId", user.getId());

        MvcResult result = mockMvc.perform(post("/data/film/wish/addToList")
                .param("film", Integer.toString(film.getId()))
                .param("isPublic", "true")
                .session(session))
                .andExpect(status().isOk())
                .andReturn();

        Assert.assertEquals("Film added to wish list", result.getResponse().getContentAsString());

    }

    /**
     * @see am.aca.orgflix.controller.FilmDataController#addFilmToWishList(HttpSession, int, boolean)
     */
    @Test
    public void addToWished_InvalidFilm_Success() throws Exception {
        userService.add(user);

        session.setAttribute("userId", user.getId());

        MvcResult result = mockMvc.perform(post("/data/film/wish/addToList")
                .param("film", "0")
                .param("isPublic", "true")
                .session(session))
                .andExpect(status().isBadRequest())
                .andReturn();

        Assert.assertEquals("Not succeeded", result.getResponse().getContentAsString());
    }

    /**
     * @see am.aca.orgflix.controller.FilmDataController#addFilmToWishList(HttpSession, int, boolean)
     */
    @Test
    public void addToWished_InvalidUser_Success() throws Exception {
        filmService.add(film);

        session.setAttribute("userId", user.getId());

        MvcResult result = mockMvc.perform(post("/data/film/wish/addToList")
                .param("film", Integer.toString(film.getId()))
                .param("isPublic", "true")
                .session(session))
                .andExpect(status().isBadRequest())
                .andReturn();

        Assert.assertEquals("Not succeeded", result.getResponse().getContentAsString());
    }

    /**
     * @see am.aca.orgflix.controller.FilmDataController#removeFromWatchList(HttpSession, int)
     */
    @Test
    public void removeFromWatched_Valid_Success() throws Exception {
        userService.add(user);
        filmService.add(film);

        session.setAttribute("userId", user.getId());

        mockMvc.perform(post("/data/film/watch/addToList")
                .param("film", Integer.toString(film.getId()))
                .param("isPublic", "true")
                .session(session))
                .andExpect(status().isOk())
                .andReturn();

        MvcResult result = mockMvc.perform(post("/data/film/watch/removeFromList")
                .param("film", Integer.toString(film.getId()))
                .param("isPublic", "true")
                .session(session))
                .andExpect(status().isOk())
                .andReturn();

        Assert.assertEquals("Film removed from watch list", result.getResponse().getContentAsString());
    }

    /**
     * @see am.aca.orgflix.controller.FilmDataController#removeFromWatchList(HttpSession, int)
     */
    @Test
    public void removeFromWatched_InvalidUser_Fail() throws Exception {
        filmService.add(film);

        session.setAttribute("userId", user.getId());

        MvcResult result = mockMvc.perform(post("/data/film/watch/removeFromList")
                .param("film", Integer.toString(film.getId()))
                .param("isPublic", "true")
                .session(session))
                .andExpect(status().isBadRequest())
                .andReturn();

        Assert.assertEquals("Not succeeded", result.getResponse().getContentAsString());
    }

    /**
     * @see am.aca.orgflix.controller.FilmDataController#removeFromWatchList(HttpSession, int)
     */
    @Test
    public void removeFromWatched_InvalidFilm_Fail() throws Exception {
        userService.add(user);

        session.setAttribute("userId", user.getId());

        MvcResult result = mockMvc.perform(post("/data/film/watch/removeFromList")
                .param("film", Integer.toString(film.getId()))
                .param("isPublic", "true")
                .session(session))
                .andExpect(status().isBadRequest())
                .andReturn();

        Assert.assertEquals("Not succeeded", result.getResponse().getContentAsString());
    }

    /**
     * @see am.aca.orgflix.controller.FilmDataController#removeFromWatchList(HttpSession, int)
     */
    @Test
    public void removeFromPlanned_Valid_Success() throws Exception {
        userService.add(user);
        filmService.add(film);

        session.setAttribute("userId", user.getId());

        mockMvc.perform(post("/data/film/wish/addToList")
                .param("film", Integer.toString(film.getId()))
                .param("isPublic", "true")
                .session(session))
                .andExpect(status().isOk())
                .andReturn();

        MvcResult result = mockMvc.perform(post("/data/film/wish/removeFromList")
                .param("film", Integer.toString(film.getId()))
                .param("isPublic", "true")
                .session(session))
                .andExpect(status().isOk())
                .andReturn();

        Assert.assertEquals("Film removed from wish list", result.getResponse().getContentAsString());
    }

    /**
     * @see am.aca.orgflix.controller.FilmDataController#removeFromWishList(HttpSession, int)
     */
    @Test
    public void removeFromPlanned_InvalidUser_Fail() throws Exception {
        filmService.add(film);

        session.setAttribute("userId", user.getId());

        MvcResult result = mockMvc.perform(post("/data/film/wish/removeFromList")
                .param("film", Integer.toString(film.getId()))
                .param("isPublic", "true")
                .session(session))
                .andExpect(status().isBadRequest())
                .andReturn();

        Assert.assertEquals("Not succeeded", result.getResponse().getContentAsString());
    }

    /**
     * @see am.aca.orgflix.controller.FilmDataController#removeFromWishList(HttpSession, int)
     */
    @Test
    public void removeFromPlanned_InvalidFilm_Fail() throws Exception {
        userService.add(user);

        session.setAttribute("userId", user.getId());

        MvcResult result = mockMvc.perform(post("/data/film/wish/removeFromList")
                .param("film", Integer.toString(film.getId()))
                .param("isPublic", "true")
                .session(session))
                .andExpect(status().isBadRequest())
                .andReturn();

        Assert.assertEquals("Not succeeded", result.getResponse().getContentAsString());
    }

    /**
     * @see am.aca.orgflix.controller.FilmDataController#starFilm(int, int)
     */
    @Test
    public void star_Valid_Success() throws Exception {
        userService.add(user);
        filmService.add(film);
        session.setAttribute("userId", user.getId());

        MvcResult result = mockMvc.perform(post("/data/film/star")
                .param("film", Integer.toString(film.getId()))
                .param("star", "5")
                .session(session))
                .andExpect(status().isOk())
                .andReturn();

        Assert.assertEquals("Film is rated", result.getResponse().getContentAsString());

        result = mockMvc.perform(post("/data/film/star")
                .param("film", Integer.toString(film.getId()))
                .param("star", "4")
                .session(session))
                .andExpect(status().isOk())
                .andReturn();

        Assert.assertEquals("Film is rated", result.getResponse().getContentAsString());

        mockMvc.perform(post("/data/film/star")
                .param("film", Integer.toString(film.getId()))
                .param("star", "6")
                .session(session))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    /**
     * @see am.aca.orgflix.controller.FilmDataController#starFilm(int, int)
     */
    @Test
    public void star_InvalidFilm_Fail() throws Exception {
        userService.add(user);
        session.setAttribute("userId", user.getId());

        MvcResult result = mockMvc.perform(post("/data/film/star")
                .param("film", Integer.toString(film.getId()))
                .param("star", "5")
                .session(session))
                .andExpect(status().isBadRequest())
                .andReturn();

        Assert.assertEquals("Not succeeded", result.getResponse().getContentAsString());
    }
}
