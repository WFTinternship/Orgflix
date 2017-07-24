package am.aca.orgflix.service.unit;

import am.aca.orgflix.BaseUnitTest;
import am.aca.orgflix.dao.UserDAO;
import am.aca.orgflix.entity.User;
import am.aca.orgflix.exception.ServiceException;
import am.aca.orgflix.service.UserService;
import am.aca.orgflix.service.impl.UserServiceImpl;
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

    /**
     * @see UserServiceImpl#add(am.aca.orgflix.entity.User)
     */
    @Test
    public void addUser_Success() {
        when(userDaoMock.emailIsUsed(user.getEmail())).thenReturn(false);
        when(userDaoMock.nickIsUsed(user.getNick())).thenReturn(false);
        when(userDaoMock.add(user)).thenReturn(1);

        int id = userService.add(user);
        Assert.assertEquals(1, id);

        verify(userDaoMock, times(1)).add(user);
        verify(userDaoMock, times(1)).emailIsUsed(user.getEmail());
        verify(userDaoMock, times(1)).nickIsUsed(user.getNick());
    }

    /**
     * @see UserServiceImpl#add(am.aca.orgflix.entity.User)
     */
    @Test
    public void addUser_EmptyEmail_Fail() {
        try {
            user.setEmail("");
            userService.add(user);
        } catch (ServiceException e) {
            Assert.assertTrue(e instanceof ServiceException);
        }
    }

    /**
     * @see UserServiceImpl#add(am.aca.orgflix.entity.User)
     */
    @Test
    public void addUser_NullEmail_Fail() {
        try {
            user.setEmail(null);
            userService.add(user);
        } catch (ServiceException e) {
            Assert.assertTrue(e instanceof ServiceException);
        }
    }

    /**
     * @see UserServiceImpl#add(am.aca.orgflix.entity.User)
     */
    @Test
    public void addUser_InvalidEmail_Fail() {
        try {
            user.setEmail("invalid string");
            userService.add(user);
        } catch (ServiceException e) {
            Assert.assertTrue(e instanceof ServiceException);
        }
    }

    /**
     * @see UserServiceImpl#add(am.aca.orgflix.entity.User)
     */
    @Test
    public void addUser_NotUniqueEmail_Fail() {
        when(userDaoMock.emailIsUsed(user.getEmail())).thenReturn(true);

        try {
            userService.add(user);
        } catch (ServiceException e) {
            verify(userDaoMock, times(1)).emailIsUsed(user.getEmail());
        }
    }

    @Test
    public void addUser_EmptyPass_Fail() {
        try {
            user.setPass("");
            userService.add(user);
        } catch (ServiceException e) {
            Assert.assertTrue(e instanceof ServiceException);
        }
    }

    /**
     * @see UserServiceImpl#add(am.aca.orgflix.entity.User)
     */
    @Test
    public void addUser_NullPass_Fail() {
        try {
            user.setPass(null);
            userService.add(user);
        } catch (ServiceException e) {
            Assert.assertTrue(e instanceof ServiceException);
        }
    }

    /**
     * @see UserServiceImpl#add(am.aca.orgflix.entity.User)
     */
    @Test
    public void addUser_InvalidPass_Fail() {
        try {
            user.setPass("invalid string");
            userService.add(user);
        } catch (ServiceException e) {
            Assert.assertTrue(e instanceof ServiceException);
        }
    }

    @Test
    public void addUser_EmptyNick_Fail() {
        try {
            user.setEmail("");
            userService.add(user);
        } catch (ServiceException e) {
            Assert.assertTrue(e instanceof ServiceException);
        }
    }

    /**
     * @see UserServiceImpl#add(am.aca.orgflix.entity.User)
     */
    @Test
    public void addUser_NullNick_Fail() {
        try {
            user.setEmail(null);
            userService.add(user);
        } catch (ServiceException e) {
            Assert.assertTrue(e instanceof ServiceException);
        }
    }

    /**
     * @see UserServiceImpl#add(am.aca.orgflix.entity.User)
     */
    @Test
    public void addUser_NotUniqueNick_Fail() {
        when(userDaoMock.emailIsUsed(user.getEmail())).thenReturn(false);
        when(userDaoMock.nickIsUsed(user.getNick())).thenReturn(true);

        try {
            userService.add(user);
        } catch (ServiceException e) {
            verify(userDaoMock, times(1)).emailIsUsed(user.getEmail());
            verify(userDaoMock, times(1)).nickIsUsed(user.getNick());
        }
    }

    /**
     * @see UserServiceImpl#getById(int)
     */
    @Test
    public void getUser_ById_Success() {
        when(userDaoMock.getById(52)).thenReturn(user);

        User actualUser = userService.getById(52);
        Assert.assertEquals(user, actualUser);

        verify(userDaoMock, times(1)).getById(52);
    }

    /**
     * @see UserServiceImpl#getById(int)
     */
    @Test
    public void getUser_ById_Fail() {
        when(userDaoMock.getById(-1)).thenReturn(null);

        User actualUser = userService.getById(-1);
        Assert.assertNull(actualUser);
        verify(userDaoMock, times(1)).getById(-1);
    }

    /**
     * @see UserServiceImpl#getByEmail(String)
     */
    @Test
    public void getUser_ByEmail_Success() {
        when(userDaoMock.getByEmail("bbanner@avengers.com")).thenReturn(user);

        User actualUser = userService.getByEmail("bbanner@avengers.com");
        Assert.assertEquals(user, actualUser);

        verify(userDaoMock, times(1)).getByEmail("bbanner@avengers.com");
    }

    /**
     * @see UserServiceImpl#getByEmail(String)
     */
    @Test
    public void getUser_ByEmail_Fail() {
        when(userDaoMock.getByEmail("bot@mail.ru")).thenReturn(null);

        User actualUser = userService.getByEmail("bot@mail.ru");

        Assert.assertNull(actualUser);
        verify(userDaoMock, times(1)).getByEmail("bot@mail.ru");
    }

    /**
     * @see UserServiceImpl#getByNick(String)
     */
    @Test
    public void getUser_ByNick_Success() {
        when(userDaoMock.getByNick("hulk")).thenReturn(user);

        User actualUser = userService.getByNick("hulk");
        Assert.assertEquals(user, actualUser);

        verify(userDaoMock, times(1)).getByNick("hulk");
    }

    /**
     * @see UserServiceImpl#getByNick(String)
     */
    @Test
    public void getUser_ByNick_Fail() {
        when(userDaoMock.getByNick("bot")).thenReturn(null);

        User actualUser = userService.getByNick("bot");
        Assert.assertNull(actualUser);
        verify(userDaoMock, times(1)).getByNick("bot");
    }

    /**
     * @see UserServiceImpl#edit(User)
     */
    @Test
    public void editUser_ValidUser_Success() {
        when(userDaoMock.emailIsUsed(user.getEmail())).thenReturn(false);
        when(userDaoMock.nickIsUsed(user.getNick())).thenReturn(false);
        when(userDaoMock.edit(user)).thenReturn(true);

        boolean status = userService.edit(user);
        Assert.assertTrue(status);

        verify(userDaoMock, times(1)).edit(user);
        verify(userDaoMock, times(1)).nickIsUsed(user.getNick());
        verify(userDaoMock, times(1)).emailIsUsed(user.getEmail());
        verify(userDaoMock, times(1)).reset(user.getId());
    }

    /**
     * @see UserServiceImpl#edit(User)
     */
    @Test
    public void editUser_InvalidEmail_Fail() {
        user.setEmail("some invalid string");
        try {
            userService.edit(user);
        } catch (RuntimeException e) {
            verify(userDaoMock, times(1)).reset(user.getId());
        }
    }

    /**
     * @see UserServiceImpl#edit(User)
     */
    @Test
    public void editUser_EmptyEmail_Fail() {
        user.setEmail("");
        try {
            userService.edit(user);
        } catch (RuntimeException e) {
            verify(userDaoMock, times(1)).reset(user.getId());
        }
    }

    /**
     * @see UserServiceImpl#edit(User)
     */
    @Test
    public void editUser_NullEmail_Fail() {
        user.setEmail(null);
        try {
            userService.edit(user);
        } catch (RuntimeException e) {
            verify(userDaoMock, times(1)).reset(user.getId());
        }
    }

    /**
     * @see UserServiceImpl#edit(User)
     */
    @Test
    public void editUser_EmailNotUnique_Fail() {
        when(userDaoMock.emailIsUsed(user.getEmail())).thenReturn(true);
        try {
            userService.edit(user);
        } catch (RuntimeException e) {
            verify(userDaoMock, times(1)).emailIsUsed(user.getEmail());
            verify(userDaoMock, times(1)).reset(user.getId());
        }
    }

    /**
     * @see UserServiceImpl#edit(User)
     */
    @Test
    public void editUser_EmptyNick_Fail() {
        user.setNick("");
        try {
            userService.edit(user);
        } catch (RuntimeException e) {
            verify(userDaoMock, times(1)).reset(user.getId());
        }
    }

    /**
     * @see UserServiceImpl#edit(User)
     */
    @Test
    public void editUser_NullNick_Fail() {
        user.setNick(null);
        try {
            userService.edit(user);
        } catch (RuntimeException e) {
            verify(userDaoMock, times(1)).reset(user.getId());
        }
    }

    /**
     * @see UserServiceImpl#edit(User)
     */
    @Test
    public void editUser_NotUniqueNick_Fail() {
        when(userDaoMock.emailIsUsed(user.getEmail())).thenReturn(false);
        when(userDaoMock.nickIsUsed(user.getNick())).thenReturn(true);
        try {
            userService.edit(user);
        } catch (RuntimeException e) {
            verify(userDaoMock, times(1)).emailIsUsed(user.getEmail());
            verify(userDaoMock, times(1)).nickIsUsed(user.getNick());
            verify(userDaoMock, times(1)).reset(user.getId());
        }
    }

    /**
     * @see UserServiceImpl#edit(User)
     */
    @Test
    public void editUser_InvalidPass_Fail() {
        user.setPass("some invalid string");
        try {
            userService.edit(user);
        } catch (RuntimeException e) {
            verify(userDaoMock, times(1)).reset(user.getId());
        }
    }

    /**
     * @see UserServiceImpl#edit(User)
     */
    @Test
    public void editUser_EmptyPass_Fail() {
        user.setPass("");
        try {
            userService.edit(user);
        } catch (RuntimeException e) {
            verify(userDaoMock, times(1)).reset(user.getId());
        }
    }

    /**
     * @see UserServiceImpl#edit(User)
     */
    @Test
    public void editUser_NullPass_Fail() {
        user.setPass(null);
        try {
            userService.edit(user);
        } catch (RuntimeException e) {
            verify(userDaoMock, times(1)).reset(user.getId());
        }
    }
}
