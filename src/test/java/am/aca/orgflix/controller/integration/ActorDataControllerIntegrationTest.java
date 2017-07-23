package am.aca.orgflix.controller.integration;

import am.aca.orgflix.BaseIntegrationTest;
import am.aca.orgflix.controller.ActorDataController;
import am.aca.orgflix.entity.Cast;
import am.aca.orgflix.service.CastService;
import am.aca.orgflix.util.TestHelper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for Actor Data Controller
 */
public class ActorDataControllerIntegrationTest extends BaseIntegrationTest {
    @Autowired
    private CastService castService;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private TestHelper helper;

    private MockMvc mockMvc;

    private Cast cast = new Cast();

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @After
    public void tearDown() {
        helper.emptyTable(new String[]{
                "LISTS",
                "USERS",
                "FILM_TO_GENRE",
                "FILM_TO_CAST",
                "CASTS",
                "FILMS"});
    }

    /**
     * @see ActorDataController#getActorsList()
     */
    @Test
    public void getActorsList_Valid_Success() throws Exception {
        cast.setName("Thandie Newton");
        castService.add(cast);
        int id1 = cast.getId();
        cast.setName("Gerard Butler");
        castService.add(cast);
        int id2 = cast.getId();
        cast.setName("Mark Strong");
        castService.add(cast);
        int id3 = cast.getId();

        mockMvc.perform(get("/data/actor/getList"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is(Integer.toString(id2))))
                .andExpect(jsonPath("$[0].name", is("Gerard Butler")))
                .andExpect(jsonPath("$[1].id", is(Integer.toString(id3))))
                .andExpect(jsonPath("$[1].name", is("Mark Strong")))
                .andExpect(jsonPath("$[2].id", is(Integer.toString(id1))))
                .andExpect(jsonPath("$[2].name", is("Thandie Newton")));
    }

    /**
     * @see ActorDataController#getActorsList()
     */
    @Test
    public void getActorsList_Empty_Success() throws Exception {
        mockMvc.perform(get("/data/actor/getList"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(0)));
    }
}
