package am.aca.orgflix.controller.unit;

import am.aca.orgflix.BaseUnitTest;
import am.aca.orgflix.controller.FilmDataController;
import am.aca.orgflix.service.FilmService;
import am.aca.orgflix.service.ListService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpSession;

import static org.mockito.Mockito.*;
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

    /**
     * Configures Mockito
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(filmDataController, "listService", listServiceMock);
        ReflectionTestUtils.setField(filmDataController, "filmService", filmServiceMock);
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    /**
     * Assures no more methods of Service mocks are invoked
     */
    @After
    public void tearDown() {
        verifyNoMoreInteractions(filmServiceMock);
        verifyNoMoreInteractions(listServiceMock);
    }

    /**
     * @see FilmDataController#addFilmToWatchList(HttpSession, int, boolean)
     */
    @Test
    public void addToWatched_Valid_Success() throws Exception {
        session.setAttribute("userId", 1);

        when(listServiceMock.addToWatched(1, true, 1)).thenReturn(true);

        MvcResult result = mockMvc.perform(post("/data/film/watch/addToList")
                .param("film", "1")
                .param("isPublic", "true")
                .session(session))
                .andExpect(status().isOk())
                .andReturn();

        Assert.assertEquals("Film added to watch list", result.getResponse().getContentAsString());

        verify(listServiceMock, times(1)).addToWatched(1, true, 1);
    }

    /**
     * @see FilmDataController#addFilmToWatchList(HttpSession, int, boolean)
     */
    @Test
    public void addToWatched_AddingFailed_Fail() throws Exception {
        session.setAttribute("userId", 1);

        when(listServiceMock.addToWatched(1, true, 1)).thenReturn(false);

        MvcResult result = mockMvc.perform(post("/data/film/watch/addToList")
                .param("film", "1")
                .param("isPublic", "true")
                .session(session))
                .andExpect(status().isBadRequest())
                .andReturn();

        Assert.assertEquals("Not succeeded", result.getResponse().getContentAsString());

        verify(listServiceMock, times(1)).addToWatched(1, true, 1);
    }

    /**
     * @see FilmDataController#addFilmToWatchList(HttpSession, int, boolean)
     */
    @Test
    public void addToWatched_ServiceException_Fail() throws Exception {
        session.setAttribute("userId", 1);

        when(listServiceMock.addToWatched(1, true, 1)).thenThrow(RuntimeException.class);

        mockMvc.perform(post("/data/film/watch/addToList")
                .param("film", "1")
                .param("isPublic", "true")
                .session(session))
                .andExpect(status().isBadRequest());

        verify(listServiceMock, times(1)).addToWatched(1, true, 1);
    }

    /**
     * @see FilmDataController#addFilmToWishList(HttpSession, int, boolean)
     */
    @Test
    public void addToPlanned_Valid_Success() throws Exception {
        session.setAttribute("userId", 1);

        when(listServiceMock.addToPlanned(1, true, 1)).thenReturn(true);

        MvcResult result = mockMvc.perform(post("/data/film/wish/addToList")
                .param("film", "1")
                .param("isPublic", "true")
                .session(session))
                .andExpect(status().isOk())
                .andReturn();

        Assert.assertEquals("Film added to wish list", result.getResponse().getContentAsString());

        verify(listServiceMock, times(1)).addToPlanned(1, true, 1);
    }

    /**
     * @see FilmDataController#addFilmToWishList(HttpSession, int, boolean)
     */
    @Test
    public void addToPlanned_AddingFailed_Fail() throws Exception {
        session.setAttribute("userId", 1);

        when(listServiceMock.addToPlanned(1, true, 1)).thenReturn(false);

        MvcResult result = mockMvc.perform(post("/data/film/wish/addToList")
                .param("film", "1")
                .param("isPublic", "true")
                .session(session))
                .andExpect(status().isBadRequest())
                .andReturn();

        Assert.assertEquals("Not succeeded", result.getResponse().getContentAsString());

        verify(listServiceMock, times(1)).addToPlanned(1, true, 1);
    }

    /**
     * @see FilmDataController#addFilmToWishList(HttpSession, int, boolean)
     */
    @Test
    public void addToPlanned_ServiceException_Fail() throws Exception {
        session.setAttribute("userId", 1);

        when(listServiceMock.addToPlanned(1, true, 1)).thenThrow(RuntimeException.class);

        mockMvc.perform(post("/data/film/wish/addToList")
                .param("film", "1")
                .param("isPublic", "true")
                .session(session))
                .andExpect(status().isBadRequest());

        verify(listServiceMock, times(1)).addToPlanned(1, true, 1);
    }

    /**
     * @see FilmDataController#removeFromWatchList(HttpSession, int)
     */
    @Test
    public void removeFromWatched_Valid_Success() throws Exception {
        session.setAttribute("userId", 1);

        when(listServiceMock.removeFromWatched(1, 1)).thenReturn(true);

        MvcResult result = mockMvc.perform(post("/data/film/watch/removeFromList")
                .param("film", "1")
                .param("isPublic", "true")
                .session(session))
                .andExpect(status().isOk())
                .andReturn();

        Assert.assertEquals("Film removed from watch list", result.getResponse().getContentAsString());

        verify(listServiceMock, times(1)).removeFromWatched(1, 1);
    }

    /**
     * @see FilmDataController#removeFromWatchList(HttpSession, int)
     */
    @Test
    public void removeFromWatched_InvalidUser_Success() throws Exception {
        session.setAttribute("userId", -1);

        when(listServiceMock.removeFromWatched(1, -1)).thenReturn(false);

        MvcResult result = mockMvc.perform(post("/data/film/watch/removeFromList")
                .param("film", "1")
                .param("isPublic", "true")
                .session(session))
                .andExpect(status().isBadRequest())
                .andReturn();

        Assert.assertEquals("Not succeeded", result.getResponse().getContentAsString());

        verify(listServiceMock, times(1)).removeFromWatched(1, -1);
    }

    /**
     * @see FilmDataController#removeFromWatchList(HttpSession, int)
     */
    @Test
    public void removeFromWatched_ServiceException_Fail() throws Exception {
        session.setAttribute("userId", 1);

        when(listServiceMock.removeFromWatched(1, 1)).thenThrow(RuntimeException.class);

        mockMvc.perform(post("/data/film/watch/removeFromList")
                .param("film", "1")
                .param("isPublic", "true")
                .session(session))
                .andExpect(status().isBadRequest());

        verify(listServiceMock, times(1)).removeFromWatched(1, 1);
    }

    /**
     * @see FilmDataController#removeFromWishList(HttpSession, int)
     */
    @Test
    public void removeFromPlanned_Valid_Success() throws Exception {
        session.setAttribute("userId", 1);

        when(listServiceMock.removeFromPlanned(1, 1)).thenReturn(true);

        MvcResult result = mockMvc.perform(post("/data/film/wish/removeFromList")
                .param("film", "1")
                .param("isPublic", "true")
                .session(session))
                .andExpect(status().isOk())
                .andReturn();

        Assert.assertEquals("Film removed from wish list", result.getResponse().getContentAsString());

        verify(listServiceMock, times(1)).removeFromPlanned(1, 1);
    }

    /**
     * @see FilmDataController#removeFromWatchList(HttpSession, int)
     */
    @Test
    public void removeFromPlanned_InvalidUser_Success() throws Exception {
        session.setAttribute("userId", -1);

        when(listServiceMock.removeFromPlanned(1, -1)).thenReturn(false);

        MvcResult result = mockMvc.perform(post("/data/film/wish/removeFromList")
                .param("film", "1")
                .param("isPublic", "true")
                .session(session))
                .andExpect(status().isBadRequest())
                .andReturn();

        Assert.assertEquals("Not succeeded", result.getResponse().getContentAsString());

        verify(listServiceMock, times(1)).removeFromPlanned(1, -1);
    }

    /**
     * @see FilmDataController#removeFromWishList(HttpSession, int)
     */
    @Test
    public void removeFromPlanned_ServiceException_Fail() throws Exception {
        session.setAttribute("userId", 1);

        when(listServiceMock.removeFromPlanned(1, 1)).thenThrow(RuntimeException.class);

        mockMvc.perform(post("/data/film/wish/removeFromList")
                .param("film", "1")
                .param("isPublic", "true")
                .session(session))
                .andExpect(status().isBadRequest());

        verify(listServiceMock, times(1)).removeFromPlanned(1, 1);
    }

    /**
     * @see FilmDataController#starFilm(int, int)
     */
    @Test
    public void star_Valid_Success() throws Exception {
        session.setAttribute("userId", 1);

        when(filmServiceMock.rate(1, 1)).thenReturn(true);

        MvcResult result = mockMvc.perform(post("/data/film/star")
                .param("film", "1")
                .param("star", "1")
                .session(session))
                .andExpect(status().isOk())
                .andReturn();

        Assert.assertEquals("Film is rated", result.getResponse().getContentAsString());

        verify(filmServiceMock, times(1)).rate(1, 1);
    }

    /**
     * @see FilmDataController#starFilm(int, int)
     */
    @Test
    public void star_invalidRating_Success() throws Exception {
        session.setAttribute("userId", 1);

        when(filmServiceMock.rate(1, 1)).thenReturn(false);

        MvcResult result = mockMvc.perform(post("/data/film/star")
                .param("film", "1")
                .param("star", "1")
                .session(session))
                .andExpect(status().isBadRequest())
                .andReturn();

        Assert.assertEquals("Not succeeded", result.getResponse().getContentAsString());

        verify(filmServiceMock, times(1)).rate(1, 1);
    }

    /**
     * @see FilmDataController#starFilm(int, int)
     */
    @Test
    public void star_ServiceException_Success() throws Exception {
        session.setAttribute("userId", 1);

        when(filmServiceMock.rate(1, 1)).thenThrow(RuntimeException.class);

        mockMvc.perform(post("/data/film/star")
                .param("film", "1")
                .param("star", "1")
                .session(session))
                .andExpect(status().isBadRequest())
                .andReturn();

        verify(filmServiceMock, times(1)).rate(1, 1);
    }
}
