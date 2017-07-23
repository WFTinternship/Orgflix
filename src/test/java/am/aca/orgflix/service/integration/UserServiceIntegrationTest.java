package am.aca.orgflix.service.integration;

import am.aca.orgflix.BaseIntegrationTest;
import am.aca.orgflix.entity.User;
import am.aca.orgflix.exception.ServiceException;
import am.aca.orgflix.service.UserService;
import am.aca.orgflix.service.impl.UserServiceImpl;
import am.aca.orgflix.util.TestHelper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


/**
 * Integration Tests for User Service Layer
 */

public class UserServiceIntegrationTest extends BaseIntegrationTest {

    private final User standardUser = new User("Davit", "Davit Abovyan", "davit.abovyan@gmail.com", "123456");

    @Autowired
    private UserService userService;

    @Autowired
    private TestHelper testHelper;

    private User user;

    /**
     * Rolls back all changes applied to the test DB resulted from tests
     */
    @After
    public void tearDown() {
        testHelper.emptyTable(new String[]{"users"});
        user = null;
    }

    /**
     * @see UserServiceImpl#add(am.aca.orgflix.entity.User)
     */
    @Test
    public void addUser_Success() {
        int id = userService.add(standardUser);
        Assert.assertTrue(id > 0);
    }

    /**
     * @see UserServiceImpl#add(am.aca.orgflix.entity.User)
     */
    @Test
    public void addUser_EmptyUserName_Success() {
        user = new User("scarface", "", "scarface@miami.com", "elvira");
        int id = userService.add(user);
        Assert.assertTrue(id > 0);
    }

    /**
     * @see UserServiceImpl#add(am.aca.orgflix.entity.User)
     */
    @Test(expected = ServiceException.class)
    public void addUser_EmptyNick_Fail() {
        userService.add(new User("", "Tony Montana", "scarface@miami.com", "elvira"));
    }

    /**
     * @see UserServiceImpl#add(am.aca.orgflix.entity.User)
     */
    @Test(expected = ServiceException.class)
    public void addUser_EmptyEmail_Fail() {
        userService.add(new User("scarface", "Tony Montana", "", "elvira"));
    }

    /**
     * @see UserServiceImpl#add(am.aca.orgflix.entity.User)
     */
    @Test(expected = ServiceException.class)
    public void addUser_EmptyPass_Fail() {
        userService.add(new User("scarface", "Tony Montana", "scarface@gmail.com", ""));
    }

    /**
     * @see UserServiceImpl#add(am.aca.orgflix.entity.User)
     */
    @Test(expected = ServiceException.class)
    public void addUser_NullNick_Fail() {
        userService.add(new User(null, "Tony Montana", "scarface@miami.com", "elvira"));
    }

    /**
     * @see UserServiceImpl#add(am.aca.orgflix.entity.User)
     */
    @Test(expected = ServiceException.class)
    public void addUser_NullEmail_Fail() {
        userService.add(new User("scarface", "Tony Montana", null, "elvira"));
    }

    /**
     * @see UserServiceImpl#add(am.aca.orgflix.entity.User)
     */
    @Test(expected = ServiceException.class)
    public void addUser_NullPass_Fail() {
        userService.add(new User("scarface", "Tony Montana", "scarface@miami.com", null));
    }

    /**
     * @see UserServiceImpl#add(am.aca.orgflix.entity.User)
     */
    @Test(expected = ServiceException.class)
    public void addUser_EmailAlreadyExists_Fail() {
        userService.add(standardUser);

        userService.add(new User("smurf", "Manny Ribera", "davit.abovyan@gmail.com", "elvira"));
    }

    /**
     * @see UserServiceImpl#add(am.aca.orgflix.entity.User)
     */
    @Test(expected = ServiceException.class)
    public void addUser_BadEmailFormat_Fail() {
        userService.add(standardUser);

        userService.add(new User("scarface", "Tony Montana", "someFakeEmail", "elvira"));
    }

    /**
     * @see UserServiceImpl#add(am.aca.orgflix.entity.User)
     */
    @Test(expected = ServiceException.class)
    public void addUser_ShortPassword_Fail() {
        userService.add(standardUser);

        userService.add(new User("scarface", "Tony Montana", "scarface@miami.com", "1"));
    }

    /**
     * @see UserServiceImpl#add(am.aca.orgflix.entity.User)
     */
    @Test(expected = ServiceException.class)
    public void addUser_SpaceInPassword_Fail() {
        userService.add(standardUser);

        userService.add(new User("scarface", "Tony Montana", "scarface@miami.com", "elvira <3"));
    }

    /**
     * @see UserServiceImpl#getById(int)
     */
    @Test
    public void getUser_ById_Success() {
        int id = userService.add(standardUser);
        User actualUser = userService.getById(id);

        Assert.assertEquals(standardUser, actualUser);
    }

    /**
     * @see UserServiceImpl#getById(int)
     */
    @Test
    public void getUser_ByWrongId_Fail() {
        userService.add(standardUser);
        int id = userService.add(new User("scarface1", "Tony Montana", "scarface1@miami.com", "elvira"));

        User actualUser = userService.getById(id);
        Assert.assertNotEquals(standardUser, actualUser);
    }

    /**
     * @see UserServiceImpl#getByEmail(String)
     */
    @Test
    public void getUser_ByEmail_Success() {
        userService.add(standardUser);

        User actualUser = userService.getByEmail("davit.abovyan@gmail.com");
        Assert.assertEquals(standardUser, actualUser);
    }

    /**
     * @see UserServiceImpl#getByEmail(String)
     */
    @Test
    public void getUser_ByWrongEmail_Fail() {
        userService.add(standardUser);

        User actualUser = userService.getByEmail("scarface@gmail.com");
        Assert.assertNotEquals(standardUser, actualUser);
    }

    /**
     * @see UserServiceImpl#getByNick(String)
     */
    @Test
    public void getByNick_Success() {
        userService.add(standardUser);

        User actualUser = userService.getByNick("Davit");
        Assert.assertEquals(standardUser, actualUser);
    }

    /**
     * @see UserServiceImpl#getByNick(String)
     */
    @Test
    public void getByNick_Empty_Fail() {
        User actualUser = userService.getByNick("smurf");
        Assert.assertNotEquals(standardUser, actualUser);
    }

    /**
     * @see UserServiceImpl#getAll()
     */
    @Test
    public void getAll_OneUser_Success() {
        userService.add(standardUser);
        List<User> list = userService.getAll();
        Assert.assertEquals("Davit",list.get(0).getNick());
    }

    /**
     * @see UserServiceImpl#authenticate(java.lang.String, java.lang.String)
     */
    @Test
    public void authenticate_Success() {
        userService.add(standardUser);

        boolean status = standardUser.equals(userService.authenticate(standardUser.getEmail(), standardUser.getPass()));
        Assert.assertTrue(status);
    }

    /**
     * @see UserServiceImpl#authenticate(java.lang.String, java.lang.String)
     */
    @Test
    public void authenticate_WrongPass_Fail() {
        userService.add(standardUser);

        User actualUser = userService.authenticate(standardUser.getEmail(), "xxx");
        Assert.assertNull(actualUser);
    }

    /**
     * @see UserServiceImpl#edit(am.aca.orgflix.entity.User)
     */
    @Test
    public void editUser_Email_Success() {
        user = new User("scarface", "Tony Montana", "scarface@miami.com", "elvira");
        userService.add(user);
        final String newEmail = "scarface@gmail.com";
        user.setEmail(newEmail);

        boolean status = userService.edit(user);
        Assert.assertTrue(status);

        String actualEmail = userService.getByEmail(newEmail).getEmail();
        Assert.assertEquals(newEmail, actualEmail);
    }

    /**
     * @see UserServiceImpl#edit(am.aca.orgflix.entity.User)
     */
    @Test
    public void editUser_Nick_Success() {
        user = new User("scarface", "Tony Montana", "scarface@miami.com", "elvira");
        int id = userService.add(user);
        final String newNick = "scarface/";
        user.setNick(newNick);

        boolean status = userService.edit(user);
        Assert.assertTrue(status);

        String actualNick = userService.getById(id).getNick();
        Assert.assertEquals(newNick, actualNick);

    }

    /**
     * @see UserServiceImpl#edit(am.aca.orgflix.entity.User)
     */
    @Test
    public void editUser_UserName_Success() {
        user = new User("scarface", "Tony Montana", "scarface@miami.com", "elvira");
        int id = userService.add(user);

        final String newUserName = "Tony Montana Jr";
        user.setUserName(newUserName);

        boolean status = userService.edit(user);
        Assert.assertTrue(status);

        String actualUserName = userService.getById(id).getUserName();
        Assert.assertEquals(newUserName, actualUserName);
    }

    /**
     * @see UserServiceImpl#edit(am.aca.orgflix.entity.User)
     */
    @Test
    public void editUser_Pass_Success() {
        user = new User("scarface", "Tony Montana", "scarface@miami.com", "elvira");
        int id = userService.add(user);

        final String newPass = "elvira<3";
        user.setPass(newPass);

        boolean status = userService.edit(user);
        Assert.assertTrue(status);

        String actualPass = userService.getById(id).getPass();
        Assert.assertEquals(newPass, actualPass);
    }

    /**
     * @see UserServiceImpl#edit(am.aca.orgflix.entity.User)
     */
    @Test(expected = ServiceException.class)
    public void editUser_EmailNonUnique_Fail() {
        userService.add(standardUser);
        User otherUser = new User("manny", "Manny Ribera", "manny@miami.com", "elvira");

        userService.add(otherUser);
        otherUser.setEmail("davit.abovyan@gmail.com");

        userService.edit(otherUser);
    }

    /**
     * @see UserServiceImpl#edit(am.aca.orgflix.entity.User)
     */
    @Test(expected = ServiceException.class)
    public void editUser_EmailNULL_Fail() {
        user = new User("scarface", "Tony Montana", "scarface@miami.com", "elvira");
        userService.add(user);
        user.setEmail(null);

        userService.edit(user);
    }

    /**
     * @see UserServiceImpl#edit(am.aca.orgflix.entity.User)
     */
    @Test(expected = ServiceException.class)
    public void editUser_NickNULL_Fail() {
        user = new User("scarface", "Tony Montana", "scarface@miami.com", "elvira");
        userService.add(user);
        user.setNick(null);

        userService.edit(user);
    }

    /**
     * @see UserServiceImpl#edit(am.aca.orgflix.entity.User)
     */
    @Test(expected = ServiceException.class)
    public void editUser_PassNULL_Fail() {
        user = new User("scarface", "Tony Montana", "scarface@miami.com", "elvira");
        userService.add(user);
        user.setPass(null);

        userService.edit(user);
    }

    /**
     * @see UserServiceImpl#edit(am.aca.orgflix.entity.User)
     */
    @Test(expected = ServiceException.class)
    public void editUser_EmailEmpty_Fail() {
        user = new User("scarface", "Tony Montana", "scarface@miami.com", "elvira");
        userService.add(user);
        user.setEmail("");

        userService.edit(user);
    }

    /**
     * @see UserServiceImpl#edit(am.aca.orgflix.entity.User)
     */
    @Test(expected = ServiceException.class)
    public void editUser_NickEmpty_Fail() {
        user = new User("scarface", "Tony Montana", "scarface@miami.com", "elvira");
        userService.add(user);
        user.setNick("");

        userService.edit(user);
    }

    /**
     * @see UserServiceImpl#edit(am.aca.orgflix.entity.User)
     */
    @Test(expected = ServiceException.class)
    public void editUser_PassEmpty_Fail() {
        user = new User("scarface", "Tony Montana", "scarface@miami.com", "elvira");
        userService.add(user);
        user.setPass("");

        userService.edit(user);
    }
}
