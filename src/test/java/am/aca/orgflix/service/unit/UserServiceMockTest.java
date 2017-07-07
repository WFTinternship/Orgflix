package am.aca.orgflix.service.unit;

import am.aca.orgflix.BaseUnitTest;
import am.aca.orgflix.dao.DaoException;
import am.aca.orgflix.dao.UserDAO;
import am.aca.orgflix.entity.User;
import am.aca.orgflix.service.ServiceException;
import am.aca.orgflix.service.UserService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.*;

/**
 * Unit tests for User Service Layer
 */
public class UserServiceMockTest extends BaseUnitTest {

    @Autowired
    private UserService userService;

    @Mock
    private UserDAO userDaoMock;
    private User user = new User("hulk", "Bruce Banner", "bbanner@avenger.com", "natasha");

    /**
     * Configures Mockito
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(userService, "userDao", userDaoMock);
    }

    /**
     * Assures no more methods of DAO mocks are invoked
     */
    @After
    public void tearDown() {
        verifyNoMoreInteractions(userDaoMock);
    }

    @Test
    public void addUser_Success() {
        when(userDaoMock.add(user)).thenReturn(1);

        int id = userService.add(user);
        Assert.assertEquals(1, id);

        verify(userDaoMock, times(1)).add(user);
    }

    @Test
    public void addUser_Fail() {
        when(userDaoMock.add(user)).thenThrow(DaoException.class);

        try {
            userService.add(user);
        } catch (ServiceException e) {
            verify(userDaoMock, times(1)).add(user);
        }
    }

    @Test
    public void getUser_ById_Success() {
        when(userDaoMock.get(52)).thenReturn(user);

        User actualUser = userService.get(52);
        Assert.assertEquals(user, actualUser);

        verify(userDaoMock, times(1)).get(52);
    }

    @Test
    public void getUser_ById_Fail() {
        when(userDaoMock.get(-1)).thenThrow(DaoException.class);

        try {
            userService.get(-1);
        } catch (ServiceException e) {
            verify(userDaoMock, times(1)).get(-1);
        }
    }

    @Test
    public void getUser_ByEmail_Success() {
        when(userDaoMock.get("bbanner@avengers.com")).thenReturn(user);

        User actualUser = userService.get("bbanner@avengers.com");
        Assert.assertEquals(user, actualUser);

        verify(userDaoMock, times(1)).get("bbanner@avengers.com");
    }

    @Test
    public void getUser_ByEmail_Fail() {
        when(userDaoMock.get("bot@mail.ru")).thenThrow(DaoException.class);

        try {
            userService.get("bot@mail.ru");
        } catch (ServiceException e) {
            verify(userDaoMock, times(1)).get("bot@mail.ru");
        }
    }

    @Test
    public void editUser_Success() {
        when(userDaoMock.edit(user)).thenReturn(true);

        boolean status = userService.edit(user);
        Assert.assertTrue(status);

        verify(userDaoMock, times(1)).edit(user);
    }

    @Test
    public void editUser_Fail() {
        when(userDaoMock.edit(user)).thenThrow(DaoException.class);

        try {
            userService.edit(user);
        } catch (ServiceException e) {
            verify(userDaoMock, times(1)).edit(user);
        }
    }
}
