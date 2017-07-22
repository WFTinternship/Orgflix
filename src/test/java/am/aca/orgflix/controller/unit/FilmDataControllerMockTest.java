package am.aca.orgflix.controller.unit;

import am.aca.orgflix.BaseUnitTest;
import am.aca.orgflix.controller.FilmDataController;
import am.aca.orgflix.entity.Cast;
import am.aca.orgflix.service.FilmService;
import am.aca.orgflix.service.ListService;
import org.junit.Assert;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit tests for Film Data Controller
 */
public class FilmDataControllerMockTest extends BaseUnitTest {
    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private MockHttpSession session;

    @Autowired
    private FilmDataController filmDataController;

    @Mock
    private FilmService filmServiceMock;

    @Mock
    private ListService listServiceMock;

    private MockMvc mockMvc;

    private Cast cast = new Cast("Thandie Newton");
    private List<Cast> casts = new ArrayList<>();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(filmDataController, "listService", listServiceMock);
        ReflectionTestUtils.setField(filmDataController, "filmService", filmServiceMock);
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void addToWatched_Valid_Success() throws Exception {
        session.setAttribute("userId", 1);

        when(listServiceMock.addToWatched(1, true, 1)).thenReturn(true);

        mockMvc.perform(post("/data/film/watch/addToList")
                .param("film", "1").param("isPublic", "true")
                .session(session))
                .andExpect(status().isOk());

        Assert.assertTrue(true);
    }

    @Test
    public void addToWatched_AddingFailed_Fail() throws Exception {
        session.setAttribute("userId", 1);

        when(listServiceMock.addToWatched(1, true, 1)).thenReturn(false);

        mockMvc.perform(post("/data/film/watch/addToList?film=1&isPublic=true").session(session))
                .andExpect(status().isBadRequest());

        Assert.assertTrue(true);
    }

    @Test
    public void addToWatched_ServiceException_Fail() throws Exception {
        session.setAttribute("userId", 1);

        when(listServiceMock.addToWatched(1, true, 1)).thenThrow(RuntimeException.class);

        mockMvc.perform(post("/data/film/watch/addToList?film=1&isPublic=true").session(session))
                .andExpect(status().isBadRequest());

        Assert.assertTrue(true);
    }

    @Test
    public void removeFromWished_Valid_Success() throws Exception {
        session.setAttribute("userId", 1);

        when(listServiceMock.removeFromPlanned(1, 1)).thenReturn(true);

        mockMvc.perform(post("/data/film/wish/removeFromList?film=1").session(session))
                .andExpect(status().isOk());

        Assert.assertTrue(true);
    }
}
