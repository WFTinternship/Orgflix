package am.aca.orgflix.controller.unit;

import am.aca.orgflix.BaseUnitTest;
import am.aca.orgflix.controller.ActorDataController;
import am.aca.orgflix.entity.Cast;
import am.aca.orgflix.service.CastService;
import am.aca.orgflix.service.ServiceException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CorsFilter;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Unit tests for Actor Data Controller
 */
public class ActorDataControllerMockTest extends BaseUnitTest {

//    @Autowired
//    WebApplicationContext wac;
    //    @Autowired
//    MockHttpSession session;
//    @Autowired
//    MockHttpServletRequest request;
    @Autowired
    private ActorDataController actorDataController;

    private MockMvc mockMvc;

    @Mock
    private CastService castServiceMock;

    private Cast cast = new Cast("Thandie Newton");
    private List<Cast> casts = new ArrayList<>();

    @Before
    public void setUp() {
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();

        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(actorDataController, "castService", castServiceMock);
        mockMvc = MockMvcBuilders.standaloneSetup(actorDataController).build();
    }

    /**
     * @see ActorDataController#getActorsList()
     */
    @Test
    public void getActorsList_Success() {
        casts.add(cast);
        when(castServiceMock.getAll()).thenReturn(casts);

        ResponseEntity actualRE = actorDataController.getActorsList();
        Assert.assertEquals(new ResponseEntity("[{\"id\":\"0\", \"name\":\"Thandie Newton\", \"oscar\":\"false\"}]", HttpStatus.OK), actualRE);

        verify(castServiceMock, times(1)).getAll();
    }

    /**
     * @see ActorDataController#getActorsList()
     */
    @Test
    public void getActorsList_ServiceException_Fail() {
        casts.add(cast);
        when(castServiceMock.getAll()).thenThrow(ServiceException.class);

        ResponseEntity actualRE = actorDataController.getActorsList();
        Assert.assertEquals(new ResponseEntity(HttpStatus.BAD_REQUEST), actualRE);

        verify(castServiceMock, times(1)).getAll();
    }

    /**
     * @see ActorDataController#getActorsList()
     */
    @Test
    public void getActorsList_Empty_Success() {
        when(castServiceMock.getAll()).thenReturn(casts);

        ResponseEntity actualRE = actorDataController.getActorsList();
        Assert.assertEquals(new ResponseEntity("[]", HttpStatus.OK), actualRE);

        verify(castServiceMock, times(1)).getAll();
    }
}
