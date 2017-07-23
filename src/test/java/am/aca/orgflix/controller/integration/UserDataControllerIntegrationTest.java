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

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for User Data Controller
 */
public class UserDataControllerIntegrationTest extends BaseIntegrationTest {
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
    List<User> users = new ArrayList<>();
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
     * @see am.aca.orgflix.controller.UserDataController#getUsersList()
     */
    @Test
    public void getUsersList_Valid_Success() throws Exception {
        int id = userService.add(user);
        users.add(user);

        mockMvc.perform(post("/data/user/getList"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(Integer.toString(id))))
                .andExpect(jsonPath("$[0].nick", is("hulk")));

        user = new User("Iron Man", "Tony Stark", "tstark@avengers.com", "pepper");
        userService.add(user);
        users.add(user);

        mockMvc.perform(post("/data/user/getList"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(Integer.toString(id))))
                .andExpect(jsonPath("$[0].nick", is("hulk")))
                .andExpect(jsonPath("$[1].id", is(Integer.toString(user.getId()))))
                .andExpect(jsonPath("$[1].nick", is("Iron Man")));
    }
}
