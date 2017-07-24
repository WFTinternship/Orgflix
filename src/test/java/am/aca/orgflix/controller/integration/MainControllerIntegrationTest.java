package am.aca.orgflix.controller.integration;

import am.aca.orgflix.BaseIntegrationTest;
import am.aca.orgflix.entity.Film;
import am.aca.orgflix.entity.User;
import am.aca.orgflix.service.FilmService;
import am.aca.orgflix.service.ListService;
import am.aca.orgflix.service.UserService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Integration tests for Main Controller
 */
public class MainControllerIntegrationTest extends BaseIntegrationTest {
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

    @Autowired
    private ListService listService;

    private MockMvc mockMvc;

    private User user = new User("hulk", "Bruce Banner", "bbanner@avengers.com", "natasha");
    private Film film = new Film("Tropic Thunder", 2008);
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
                "LISTS",
                "USERS",
                "FILMS"});
    }

    /**
     * @see am.aca.orgflix.controller.MainController#indexPageFirtVisit(HttpSession)
     */
    @Test
    public void index_Success() throws Exception {
        film.setRate_4star(1);
        filmService.add(film);
        films.add(film);
        film = new Film("Chaplin", 1992);
        film.setRate_5star(1);
        filmService.add(film);
        films.add(film);

        String[] ratings = {"4.0", "5.0"};

        mockMvc.perform(get("/")).andExpect(view().name("home"))
                .andExpect(model().attribute("films", films))
                .andExpect(model().attribute("ratings", ratings))
                .andExpect(model().attribute("currentPage", 0))
                .andExpect(model().attribute("page", "index"))
                .andExpect(model().attribute("numOfPages", 0));
    }

    /**
     * @see am.aca.orgflix.controller.MainController#indexPage(int)
     */
    @Test
    public void paging_Valid_Success() throws Exception {
        film.setRate_4star(1);
        filmService.add(film);
        films.add(film);
        film = new Film("Chaplin", 1992);
        film.setRate_5star(1);
        filmService.add(film);
        films.add(film);

        String[] ratings = {"4.0", "5.0"};

        mockMvc.perform(get("/index?currentPage=0")).andExpect(view().name("home"))
                .andExpect(model().attribute("films", films))
                .andExpect(model().attribute("numOfPages", films.size() / 12))
                .andExpect(model().attribute("ratings", ratings))
                .andExpect(model().attribute("currentPage", 0))
                .andExpect(model().attribute("page", "index"));
    }

    /**
     * @see am.aca.orgflix.controller.MainController#watchList(javax.servlet.http.HttpSession, int)
     */
    @Test
    public void watchList_Valid_Success() throws Exception {
        userService.add(user);

        film.setRate_4star(1);
        filmService.add(film);
        films.add(film);
        listService.addToWatched(film.getId(), true, user.getId());
        film = new Film("Chaplin", 1992);
        film.setRate_5star(1);
        filmService.add(film);
        films.add(film);
        listService.addToWatched(film.getId(), false, user.getId());

        int[] totalPrivacy = {1, 0};

        session.setAttribute("userId", user.getId());

        mockMvc.perform(get("/watch_list?currentPage=0")
                .session(session)).andExpect(view().name("home"))
                .andExpect(model().attribute("films", films))
                .andExpect(model().attribute("numOfPages", totalPrivacy.length / 12))
//                .andExpect(model().attribute("privacyList", totalPrivacy))
                .andExpect(model().attribute("currentPage", 0))
                .andExpect(model().attribute("page", "watch_list"));

    }

    /**
     * @see am.aca.orgflix.controller.MainController#watchList(javax.servlet.http.HttpSession, int)
     */
    @Test
    public void watchList_InvalidUser_Fail() throws Exception {
        session.setAttribute("userId", user.getId());

        mockMvc.perform(get("/watch_list?currentPage=1")
                .session(session)).andExpect(view().name("error"))
                .andExpect(model().attribute("message", "You are not logged in, please first login"));
    }

    /**
     * @see am.aca.orgflix.controller.MainController#wishList(javax.servlet.http.HttpSession, int)
     */
    @Test
    public void wishList_Valid_Success() throws Exception {
        userService.add(user);

        film.setRate_4star(1);
        filmService.add(film);
        films.add(film);
        listService.addToPlanned(film.getId(), true, user.getId());
        film = new Film("Chaplin", 1992);
        film.setRate_5star(1);
        filmService.add(film);
        films.add(film);
        listService.addToPlanned(film.getId(), false, user.getId());

        int[] totalPrivacy = {1, 0};

        session.setAttribute("userId", user.getId());

        mockMvc.perform(get("/wish_list?currentPage=0")
                .session(session)).andExpect(view().name("home"))
                .andExpect(model().attribute("films", films))
                .andExpect(model().attribute("numOfPages", totalPrivacy.length / 12))
                .andExpect(model().attribute("privacyList", totalPrivacy))
                .andExpect(model().attribute("currentPage", 0))
                .andExpect(model().attribute("page", "wish_list"));

    }

    /**
     * @see am.aca.orgflix.controller.MainController#wishList(HttpSession, int)
     */
    @Test
    public void wishList_InvalidUser_Fail() throws Exception {
        session.setAttribute("userId", user.getId());

        mockMvc.perform(get("/wish_list?currentPage=1")
                .session(session)).andExpect(view().name("error"))
                .andExpect(model().attribute("message", "You are not logged in, please first login"));
    }

    /**
     * @see am.aca.orgflix.controller.MainController#getWatchedByOtherUser(javax.servlet.http.HttpSession, int, int)
     */
    @Test
    public void othersWatched_Valid_Success() throws Exception {
        userService.add(user);
        session.setAttribute("userId", user.getId());

        User otherUser = new User("Iron Man", "Tony Stark", "tstark@avengers.com", "pepper");
        int id = userService.add(otherUser);

        film.setRate_4star(1);
        filmService.add(film);
        films.add(film);
        listService.addToWatched(film.getId(), true, id);
        film = new Film("Chaplin", 1992);
        film.setRate_5star(1);
        filmService.add(film);
        listService.addToWatched(film.getId(), false, id);

        mockMvc.perform(get("/watchListUserOther?otherUser=" + id + "&currentPage=0").session(session))
                .andExpect(view().name("home"))
                .andExpect(model().attribute("films", films))
                .andExpect(model().attribute("currentPage", 0))
                .andExpect(model().attribute("page", "watchListUserOther"))
                .andExpect(model().attribute("otherUser", id))
                .andExpect(model().attribute("title", "Watch list of user Iron Man"));
    }

    /**
     * @see am.aca.orgflix.controller.MainController#getWatchedByOtherUser(javax.servlet.http.HttpSession, int, int)
     */
    @Test
    public void othersWatched_InvalidUser_Fail() throws Exception {
        session.setAttribute("userId", user.getId());

        mockMvc.perform(get("/watchListUserOther?otherUser=2&currentPage=1").session(session))
                .andExpect(view().name("error"));
    }

    /**
     * @see am.aca.orgflix.controller.MainController#getPlannedByOtherUser(javax.servlet.http.HttpSession, int, int)
     */
    @Test
    public void othersPlanned_Valid_Success() throws Exception {
        userService.add(user);
        session.setAttribute("userId", user.getId());

        User otherUser = new User("Iron Man", "Tony Stark", "tstark@avengers.com", "pepper");
        int id = userService.add(otherUser);

        film.setRate_4star(1);
        filmService.add(film);
        films.add(film);
        listService.addToPlanned(film.getId(), true, id);
        film = new Film("Chaplin", 1992);
        film.setRate_5star(1);
        filmService.add(film);
        listService.addToPlanned(film.getId(), false, id);

        mockMvc.perform(get("/wishListUserOther?otherUser=" + id + "&currentPage=0").session(session))
                .andExpect(view().name("home"))
                .andExpect(model().attribute("films", films))
                .andExpect(model().attribute("currentPage", 0))
                .andExpect(model().attribute("page", "wishListUserOther"))
                .andExpect(model().attribute("otherUser", id))
                .andExpect(model().attribute("title", "Wish list of user Iron Man"));
    }

    /**
     * @see am.aca.orgflix.controller.MainController#getPlannedByOtherUser(HttpSession, int, int)
     */
    @Test
    public void othersPlanned_InvalidUser_Fail() throws Exception {
        session.setAttribute("userId", user.getId());

        mockMvc.perform(get("/wishListUserOther?otherUser=2&currentPage=1").session(session))
                .andExpect(view().name("error"));
    }
}
