package am.aca.orgflix.controller.unit;

import am.aca.orgflix.BaseUnitTest;
import am.aca.orgflix.controller.ActorDataController;
import am.aca.orgflix.entity.Cast;
import am.aca.orgflix.service.CastService;
import am.aca.orgflix.exception.ServiceException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit tests for Actor Data Controller
 */
public class ActorDataControllerMockTest extends BaseUnitTest {

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private ActorDataController actorDataController;

    private MockMvc mockMvc;

    @Mock
    private CastService castServiceMock;

    private Cast cast = new Cast("Thandie Newton");
    private List<Cast> casts = new ArrayList<>();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(actorDataController, "castService", castServiceMock);
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @After
    public void tearDown() {
        verifyNoMoreInteractions(castServiceMock);
    }

    /**
     * @see ActorDataController#getActorsList()
     */
    @Test
    public void getActorsList_Success() throws Exception {
        casts.add(cast);
        casts.add(new Cast("Gerard Butler"));
        casts.add(new Cast("Mark Strong"));

        when(castServiceMock.getAll()).thenReturn(casts);

        mockMvc.perform(get("/data/actor/getList"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$[0].id", is("0")))
                .andExpect(jsonPath("$[0].name", is("Thandie Newton")))
                .andExpect(jsonPath("$[1].id", is("0")))
                .andExpect(jsonPath("$[1].name", is("Gerard Butler")))
                .andExpect(jsonPath("$[2].id", is("0")))
                .andExpect(jsonPath("$[2].name", is("Mark Strong")));

        verify(castServiceMock, times(1)).getAll();
    }

    /**
     * @see ActorDataController#getActorsList()
     */
    @Test
    public void getActorsList_ServiceException_Fail() throws Exception {
        casts.add(cast);
        when(castServiceMock.getAll()).thenThrow(ServiceException.class);

        mockMvc.perform(get("/data/actor/getList"))
                .andExpect(status().isBadRequest());

        verify(castServiceMock, times(1)).getAll();
    }

    /**
     * @see ActorDataController#getActorsList()
     */
    @Test
    public void getActorsList_Empty_Success() throws Exception {
        when(castServiceMock.getAll()).thenReturn(casts);

        mockMvc.perform(get("/data/actor/getList"))
                .andExpect(status().isOk());

        verify(castServiceMock, times(1)).getAll();
    }
}
