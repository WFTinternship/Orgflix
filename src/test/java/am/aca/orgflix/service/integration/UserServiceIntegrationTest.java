package am.aca.orgflix.service.integration;

import am.aca.orgflix.BaseIntegrationTest;
import am.aca.orgflix.entity.User;
import am.aca.orgflix.service.ServiceException;
import am.aca.orgflix.service.UserService;
import am.aca.orgflix.service.impl.UserServiceImpl;
import am.aca.orgflix.util.TestHelper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Integration Tests for User Service Layer
 */

public class UserServiceIntegrationTest extends BaseIntegrationTest {

    private final User standardUser = new User("dave", "Gagik Petrosyan", "davit.abovyan@gmail.com", "pass");

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
        boolean status = userService.add(standardUser) > 0;
        Assert.assertTrue(status);
    }

    /**
     * @see UserServiceImpl#add(am.aca.orgflix.entity.User)
     */
    @Test
    public void addUser_EmptyUserName_Success() {
        user = new User("gago", "", "gagik1@gmail.com", "pass");
        boolean status = userService.add(user) > 0;
        Assert.assertTrue(status);
    }

    /**
     * @see UserServiceImpl#add(am.aca.orgflix.entity.User)
     */
    @Test(expected = ServiceException.class)
    public void addUser_EmptyNick_Fail() {
        userService.add(new User("", "Gagik Petrosyan", "gagik2@gmail.com", "pass"));
    }

    /**
     * @see UserServiceImpl#add(am.aca.orgflix.entity.User)
     */
    @Test(expected = ServiceException.class)
    public void addUser_EmptyEmail_Fail() {
        userService.add(new User("gago", "Gagik Petrosyan", "", "pass"));
    }

    /**
     * @see UserServiceImpl#add(am.aca.orgflix.entity.User)
     */
    @Test(expected = ServiceException.class)
    public void addUser_EmptyPass_Fail() {
        userService.add(new User("gago", "Gagik Petrosyan", "gagik3@gmail.com", ""));
    }

    /**
     * @see UserServiceImpl#add(am.aca.orgflix.entity.User)
     */
    @Test(expected = ServiceException.class)
    public void addUser_NullNick_Fail() {
        userService.add(new User(null, "Gagik Petrosyan", "gagik2@gmail.com", "pass"));
    }

    /**
     * @see UserServiceImpl#add(am.aca.orgflix.entity.User)
     */
    @Test(expected = ServiceException.class)
    public void addUser_NullEmail_Fail() {
        userService.add(new User("gago", "Gagik Petrosyan", null, "pass"));
    }

    /**
     * @see UserServiceImpl#add(am.aca.orgflix.entity.User)
     */
    @Test(expected = ServiceException.class)
    public void addUser_NullPass_Fail() {
        userService.add(new User("gago", "Gagik Petrosyan", "gagik3@gmail.com", null));
    }

    /**
     * @see UserServiceImpl#add(am.aca.orgflix.entity.User)
     */
    @Test(expected = ServiceException.class)
    public void addUser_EmailAlreadyExists_Fail() {
        userService.add(standardUser);

        userService.add(new User("armen", "Armen Hakobyan", "davit.abovyan@gmail.com", "pass1"));
    }

    /**
     * @see UserServiceImpl#get(int)
     */
    @Test
    public void getUser_ById_Success() {
        int id = userService.add(standardUser);
        User gotUser = userService.get(id);

        boolean status = standardUser.equals(gotUser);
        Assert.assertTrue(status);
    }

    /**
     * @see UserServiceImpl#get(int)
     */
    @Test
    public void getUser_ByWrongId_Fail() {
        userService.add(standardUser);
        int id = userService.add(new User("gago", "Gagik Petrosyan", "davit.abovyan1@gmail.com", "pass"));

        boolean status = standardUser.equals(userService.get(id));
        Assert.assertFalse(status);
    }

    /**
     * @see UserServiceImpl#get(java.lang.String)
     */
    @Test
    public void getUser_ByEmail_Success() {
        userService.add(standardUser);

        boolean status = standardUser.equals(userService.get("davit.abovyan@gmail.com"));
        Assert.assertTrue(status);
    }

    /**
     * @see UserServiceImpl#get(java.lang.String)
     */
    @Test(expected = RuntimeException.class)
    public void getUser_ByWrongEmail_Fail() {
        userService.add(standardUser);

        boolean status = user.equals(userService.get("davit.abovyan1@gmail.com"));
        Assert.assertFalse(status);
    }

    /**
     * @see UserServiceImpl#getByNick(String)
     */
    @Test
    public void getByNick_Success() {
        userService.add(standardUser);

        User actualUser = userService.getByNick("dave");
        Assert.assertEquals(standardUser, actualUser);
    }

    /**
     * @see UserServiceImpl#getByNick(String)
     */
    @Test
    public void getByNick_Empty_Fail() {
        User actualUser = userService.getByNick("gabo");
        Assert.assertNull(actualUser);
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
        user = new User("gago1", "Gagik Petrosyan", "davit.abovyan@gmail.com", "pass");
        userService.add(user);
        final String newEmail = "gagik@gmail.com";
        user.setEmail(newEmail);
        userService.edit(user);

        String actualEmail = userService.get(newEmail).getEmail();
        Assert.assertEquals(newEmail, actualEmail);
    }

    /**
     * @see UserServiceImpl#edit(am.aca.orgflix.entity.User)
     */
    @Test
    public void editUser_Nick_Success() {
        user = new User("gago1", "Gagik Petrosyan", "davit.abovyan@gmail.com", "pass");
        int id = userService.add(user);

        final String newNick = "davo";
        user.setNick(newNick);
        userService.edit(user);

        String actualNick = userService.get(id).getNick();
        Assert.assertEquals(newNick, actualNick);

    }

    /**
     * @see UserServiceImpl#edit(am.aca.orgflix.entity.User)
     */
    @Test
    public void editUser_UserName_Success() {
        user = new User("gago1", "Gagik Petrosyan", "davit.abovyan@gmail.com", "pass");
        int id = userService.add(user);

        final String newUserName = "Davit Abovyan";
        user.setUserName(newUserName);
        userService.edit(user);

        String actualUserName = userService.get(id).getUserName();
        Assert.assertEquals(newUserName, actualUserName);
    }

    /**
     * @see UserServiceImpl#edit(am.aca.orgflix.entity.User)
     */
    @Test
    public void editUser_Pass_Success() {
        user = new User("gago1", "Gagik Petrosyan", "davit.abovyan@gmail.com", "pass");
        int id = userService.add(user);

        final String newPass = "password";
        user.setPass(newPass);
        userService.edit(user);

        String actualPass = userService.get(id).getPass();
        Assert.assertEquals(newPass, actualPass);
    }

    /**
     * @see UserServiceImpl#edit(am.aca.orgflix.entity.User)
     */
    @Test(expected = ServiceException.class)
    public void editUser_EmailNonUnique_Fail() {
        userService.add(standardUser);
        User otherUser = new User("gago1", "Gagik Petrosyan", "gagik.petrosyan@gmail.com", "pass");

        userService.add(otherUser);
        otherUser.setEmail("davit.abovyan@gmail.com");

        userService.edit(otherUser);
    }

    /**
     * @see UserServiceImpl#edit(am.aca.orgflix.entity.User)
     */
    @Test(expected = ServiceException.class)
    public void editUser_EmailNULL_Fail() {
        user = new User("davit", "Davit Abvoyan", "davit.abovyan@gmail.com", "pass");
        userService.add(user);
        user.setEmail(null);

        userService.edit(user);
    }

    /**
     * @see UserServiceImpl#edit(am.aca.orgflix.entity.User)
     */
    @Test(expected = ServiceException.class)
    public void editUser_NickNULL_Fail() {
        user = new User("davit", "Davit Abvoyan", "davit.abovyan@gmail.com", "pass");
        userService.add(user);
        user.setNick(null);

        userService.edit(user);
    }

    /**
     * @see UserServiceImpl#edit(am.aca.orgflix.entity.User)
     */
    @Test(expected = ServiceException.class)
    public void editUser_PassNULL_Fail() {
        user = new User("davit", "Davit Abvoyan", "davit.abovyan@gmail.com", "pass");
        userService.add(user);
        user.setPass(null);

        userService.edit(user);
    }

    /**
     * @see UserServiceImpl#edit(am.aca.orgflix.entity.User)
     */
    @Test(expected = ServiceException.class)
    public void editUser_EmailEmpty_Fail() {
        user = new User("davit", "Davit Abvoyan", "davit.abovyan@gmail.com", "pass");
        userService.add(user);
        user.setEmail("");

        userService.edit(user);
    }

    /**
     * @see UserServiceImpl#edit(am.aca.orgflix.entity.User)
     */
    @Test(expected = ServiceException.class)
    public void editUser_NickEmpty_Fail() {
        user = new User("davit", "Davit Abvoyan", "davit.abovyan@gmail.com", "pass");
        userService.add(user);
        user.setNick("");

        userService.edit(user);
    }

    /**
     * @see UserServiceImpl#edit(am.aca.orgflix.entity.User)
     */
    @Test(expected = ServiceException.class)
    public void editUser_PassEmpty_Fail() {
        user = new User("davit", "Davit Abvoyan", "davit.abovyan@gmail.com", "pass");
        userService.add(user);
        user.setPass("");

        userService.edit(user);
    }
}