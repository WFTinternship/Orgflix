package am.aca.orgflix.service.unit;

import am.aca.orgflix.BaseUnitTest;
import am.aca.orgflix.dao.DaoException;
import am.aca.orgflix.dao.UserDAO;
import am.aca.orgflix.entity.User;
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
 * Created by karine on 6/27/2017
 */
public class UserServiceMockTest extends BaseUnitTest {

    @Autowired
    private UserService userService;

    @Mock
    private UserDAO userDaoMock;
    private User user = new User("hulk", "Bruce Banner", "bbanner@avenger.com", "natasha");

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(userService, "userDao", userDaoMock);
    }

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

        int id = userService.add(user);
        Assert.assertEquals(-1, id);

        verify(userDaoMock, times(1)).add(user);
    }

    @Test
    public void getUserById_Success() {
        when(userDaoMock.get(52)).thenReturn(user);

        User actualUser = userService.get(52);
        Assert.assertEquals(user, actualUser);

        verify(userDaoMock, times(1)).get(52);
    }

    @Test
    public void getUserById_Fail() {
        when(userDaoMock.get(-1)).thenThrow(DaoException.class);

        User actualUser = userService.get(-1);
        Assert.assertEquals(null, actualUser);

        verify(userDaoMock, times(1)).get(-1);
    }

    @Test
    public void getUserByEmail_Success() {
        when(userDaoMock.get("bbanner@avengers.com")).thenReturn(user);

        User actualUser = userService.get("bbanner@avengers.com");
        Assert.assertEquals(user, actualUser);

        verify(userDaoMock, times(1)).get("bbanner@avengers.com");
    }

    @Test
    public void getUserByEmail_Fail() {
        when(userDaoMock.get("bot@mail.ru")).thenThrow(DaoException.class);

        User actualUser = userService.get("bot@mail.ru");
        Assert.assertEquals(null, actualUser);

        verify(userDaoMock, times(1)).get("bot@mail.ru");
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

        boolean status = userService.edit(user);
        Assert.assertFalse(status);

        verify(userDaoMock, times(1)).edit(user);
    }
}
