package am.aca.orgflix.controller.unit;

import am.aca.orgflix.BaseUnitTest;
import am.aca.orgflix.controller.UserController;
import am.aca.orgflix.controller.UserDataController;
import am.aca.orgflix.entity.User;
import am.aca.orgflix.service.UserService;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit tests for User Data Controller
 */
public class UserDataControllerMockTest extends BaseUnitTest {
    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private MockHttpSession session;

    @Autowired
    private UserController userController;

    @Mock
    private UserService userServiceMock;

    private MockMvc mockMvc;

    private List<User> users = new ArrayList<>();
    private String[] ratings = new String[8];
    private User user = new User("hulk", "Bruce Banner", "bbanner@avengers.com", "natasha");

    /**
     * Configures Mockito
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(userController, "userService", userServiceMock);
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

        user.setId(1);
    }

    /**
     * Assures no more methods of Service mocks are invoked
     */
    @After
    public void tearDown() {
        verifyNoMoreInteractions(userServiceMock);
    }

    /**
     * @see UserDataController#getUsersList()
     */
    @Test
    public void getUsersList_Valid_Success() throws Exception {
        users.add(user);

        when(userServiceMock.getAll()).thenReturn(users);

        mockMvc.perform(post("/data/user/getList"));

        verify(userServiceMock, times(1)).getAll();
    }
}
