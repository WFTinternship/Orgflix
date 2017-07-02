package am.aca.orgflix.service.unit;

import am.aca.orgflix.BaseUnitTest;
import am.aca.orgflix.dao.DaoException;
import am.aca.orgflix.dao.impljdbc.JdbcUserDAO;
import am.aca.orgflix.entity.User;
import am.aca.orgflix.service.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.Mockito.when;

/**
 * Created by karine on 6/27/2017
 */
public class UserServiceMockTest extends BaseUnitTest {

    @Autowired
    private UserService userService;

    @Mock
    private JdbcUserDAO userMock;
    private User user = new User("hulk", "Bruce Banner", "bbanner@avenger.com", "natasha");

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(userService, "userDao", userMock);
    }

    @Test
    public void addUser_Success() {
        when(userMock.add(user)).thenReturn(1);
        Assert.assertEquals(1, userService.add(user));
    }

    @Test
    public void addUser_Fail() {
        when(userMock.add(user)).thenThrow(DaoException.class);
        Assert.assertEquals(-1, userService.add(user));
    }

    @Test
    public void getUserById_Success() {
        when(userMock.get(52)).thenReturn(user);
        Assert.assertEquals(user, userService.get(52));
    }

    @Test
    public void getUserById_Fail() {
        when(userMock.get(-1)).thenThrow(DaoException.class);
        Assert.assertEquals(null, userService.get(-1));
    }

    @Test
    public void getUserByEmail_Success() {
        when(userMock.get("bbanner@avengers.com")).thenReturn(user);
        Assert.assertEquals(user, userService.get("bbanner@avengers.com"));
    }

    @Test
    public void getUserByEmail_Fail() {
        when(userMock.get("bot@mail.ru")).thenThrow(DaoException.class);
        Assert.assertEquals(null, userService.get("bot@mail.ru"));
    }

    @Test
    public void editUser_Success() {
        when(userMock.edit(user)).thenReturn(true);
        Assert.assertTrue(userService.edit(user));
    }

    @Test
    public void editUser_Fail() {
        when(userMock.edit(user)).thenThrow(DaoException.class);
        Assert.assertFalse(userService.edit(user));
    }
}
