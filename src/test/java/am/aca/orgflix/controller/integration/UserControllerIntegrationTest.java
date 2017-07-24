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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Integration tests for User Controller
 */
public class UserControllerIntegrationTest extends BaseIntegrationTest {
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
     * @see am.aca.orgflix.controller.UserController#signup()
     */
    @Test
    public void signUp_Valid_Success() throws Exception {
        film.setRate_4star(2);
        filmService.add(film);
        films.add(film);

        String[] ratings = {"4.0"};

        mockMvc.perform(get("/signup")).andExpect(view().name("register"))
                .andExpect(model().attribute("films", films))
                .andExpect(model().attribute("ratings", ratings))
                .andExpect(model().attribute("currentPage", 0))
                .andExpect(model().attribute("page", "index"))
                .andExpect(model().attribute("numOfPages", 0));
    }

    /**
     * @see am.aca.orgflix.controller.UserController#signupResult(javax.servlet.http.HttpSession, String, String, String, String)
     */
    @Test
    public void signUpResult_ValidData_Success() throws Exception {
        filmService.add(film);
        films.add(film);

        mockMvc.perform(get("/signed?nick=hulk&userName=Bruce%20Banner&email=bbanner@avengers.com&pass=natasha"))
                .andExpect(view().name("home"))
                .andExpect(model().attribute("films", films))
                .andExpect(model().attribute("numOfPages", 0))
                .andExpect(model().attribute("currentPage", 0))
                .andExpect(model().attribute("page", "index"));
    }

    /**
     * @see am.aca.orgflix.controller.UserController#signupResult(javax.servlet.http.HttpSession, String, String, String, String)
     */
    @Test
    public void signUpResult_EmailUsed_Fail() throws Exception {
        filmService.add(film);
        films.add(film);
        user.setNick("unused");
        userService.add(user);

        mockMvc.perform(get("/signed?nick=hulk&userName=Bruce%20Banner&email=bbanner@avengers.com&pass=natasha"))
                .andExpect(view().name("error"));
    }

    /**
     * @see am.aca.orgflix.controller.UserController#signupResult(javax.servlet.http.HttpSession, String, String, String, String)
     */
    @Test
    public void signUpResult_NickUsed_Fail() throws Exception {
        filmService.add(film);
        films.add(film);
        user.setEmail("Unused@example.com");
        userService.add(user);

        mockMvc.perform(get("/signed?nick=hulk&userName=Bruce%20Banner&email=bbanner@avengers.com&pass=natasha"))
                .andExpect(view().name("error"));
    }

    /**
     * @see am.aca.orgflix.controller.UserController#login()
     */
    @Test
    public void login_Valid_Success() throws Exception {
        mockMvc.perform(get("/login")).andExpect(view().name("signIn"));
    }

    /**
     * @see am.aca.orgflix.controller.UserController#logout(HttpSession)
     */
    @Test
    public void logout_Valid_Success() throws Exception {
        userService.add(user);
        session.setAttribute("userId", user.getId());

        mockMvc.perform(get("/logout").session(session))
                .andExpect(view().name("signIn"));

        Assert.assertEquals(-1, (int) session.getAttribute("userId"));
    }

//    /**
//     * @see am.aca.orgflix.controller.UserController#paging(HttpSession, String, String)
//     */
//    @Test
//    public void paging_Valid_Success() throws Exception {
//        userService.add(user);
//        filmService.add(film);
//        films.add(film);
//
//        System.out.println(userService.authenticate(user.getEmail(), user.getPass()));
//
//        mockMvc.perform(get("/loggedIn?email=bbanner@avengers.com&pass=natasha")
//                .session(session))
//                .andExpect(view().name("home"))
//                .andExpect(model().attribute("films", films))
//                .andExpect(model().attribute("numOfPages", 0))
//                .andExpect(model().attribute("currentPage", 0))
//                .andExpect(model().attribute("page", "index"));
//
//        Assert.assertEquals(user.getId(), session.getAttribute("userId"));
//        Assert.assertEquals("hulk (bbanner@avengers.com)", session.getAttribute("user"));
//
//    }
}
