package am.aca.orgflix.integration.service;

import am.aca.dao.DaoException;
import am.aca.entity.User;
import am.aca.orgflix.BaseIntegrationTest;
import am.aca.service.UserService;
import am.aca.util.TestHelper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;


/**
 * Test for user DAO
 */

public class UserTest extends BaseIntegrationTest {

    @Autowired
    @Qualifier("userService")
    private UserService userService;

    @Autowired
    @Qualifier("testHelper")
    private TestHelper testHelper;

    private User user;
    private final User standardUser = new User("gago", "Gagik Petrosyan", "davit.abovyan@gmail.com", "pass");

    @Before
    public void setUp() {
    }

    @After
    public void end() {
        testHelper.emptyTable(new String[]{"users"});
        user = null;
    }

    @Test
    public void addUser_Successed() {
        Assert.assertTrue(userService.add(standardUser) > 0);
    }

    @Test
    public void addUser_Successed_EmptyUserName() {
        user = new User("gago", "", "gagik1@gmail.com", "pass");
        Assert.assertTrue(userService.add(user) > 0);
    }

    @Test(expected = RuntimeException.class)
    public void addUser_Fail_EmptyNick() {
        userService.add(new User("", "Gagik Petrosyan", "gagik2@gmail.com", "pass"));
    }

    @Test(expected = DaoException.class)
    public void addUser_Fail_EmptyEmail() {
        userService.add(new User("gago", "Gagik Petrosyan", "", "pass"));
    }

    @Test(expected = DaoException.class)
    public void addUser_Fail_EmptyPass() {
        userService.add(new User("gago", "Gagik Petrosyan", "gagik3@gmail.com", ""));
    }

    @Test(expected = DaoException.class)
    public void addUser_Fail_NullNick() {
        userService.add(new User(null, "Gagik Petrosyan", "gagik2@gmail.com", "pass"));
    }

    @Test(expected = DaoException.class)
    public void addUser_Fail_NullEmail() {
        userService.add(new User("gago", "Gagik Petrosyan", null, "pass"));
    }

    @Test(expected = DaoException.class)
    public void addUser_Fail_NullPass() {
        userService.add(new User("gago", "Gagik Petrosyan", "gagik3@gmail.com", null));
    }


    @Test(expected = DaoException.class) //org.springframework.dao.DuplicateKeyException
    public void addUser_Fail_EmailAlreadyExists() {
        userService.add(standardUser);
        userService.add(new User("armen", "Armen Hakobyan", "davit.abovyan@gmail.com", "pass1"));
    }

    @Test
    public void getUser_Succeed_ById() {
        int id = userService.add(standardUser);
        User gotUser = userService.get(id);
        Assert.assertTrue(standardUser.equals(gotUser));
    }

    @Test
    public void getUser_Fail_ByWrongId() {
        userService.add(standardUser);
        int id = userService.add(new User("gago", "Gagik Petrosyan", "davit.abovyan1@gmail.com", "pass"));
        Assert.assertFalse(standardUser.equals(userService.get(id)));
    }

    @Test
    public void getUser_Succed_ByEmail() {
        userService.add(standardUser);
        Assert.assertTrue(standardUser.equals(userService.get("davit.abovyan@gmail.com")));
    }

    @Test(expected = DaoException.class) //org.springframework.dao.EmptyResultDataAccessException
    public void getUser_Fail_ByWrongEmail() {
        userService.add(standardUser);
        Assert.assertFalse(user.equals(userService.get("davit.abovyan1@gmail.com")));
    }

    @Test
    public void editUser_Succed_Email() {
        user = new User("gago", "Gagik Petrosyan", "davit.abovyan@gmail.com", "pass");
        userService.add(user);
        final String newEmail = "gagik@gmail.com";
        user.setEmail(newEmail);
        if (userService.edit(user)) {
            Assert.assertEquals(newEmail, userService.get(newEmail).getEmail());
        }
    }

    @Test
    public void editUser_Succed_Nick() {
        user = new User("gago", "Gagik Petrosyan", "davit.abovyan@gmail.com", "pass");
        int id = userService.add(user);
        final String newNick = "davo";
        user.setNick(newNick);
        if (userService.edit(user)) {
            Assert.assertEquals(newNick, userService.get(id).getNick());
        }
    }

    @Test
    public void editUser_Succed_UserName() {
        user = new User("gago", "Gagik Petrosyan", "davit.abovyan@gmail.com", "pass");
        int id = userService.add(user);
        final String newUserName = "Davit Abovyan";
        user.setUserName(newUserName);
        if (userService.edit(user)) {
            Assert.assertEquals(newUserName, userService.get(id).getUserName());
        }
    }

    @Test
    public void editUser_Succed_Pass() {
        user = new User("gago", "Gagik Petrosyan", "davit.abovyan@gmail.com", "pass");
        int id = userService.add(user);
        final String newPass = "password";
        user.setPass(newPass);
        if (userService.edit(user)) {
            Assert.assertEquals(newPass, userService.get(id).getPass());
        }
    }

    @Test(expected = DaoException.class)
    public void editUser_Fail_EmailNonUnic() {
        userService.add(standardUser);
        User otherUser = new User("gago", "Gagik Petrosyan", "gagik.petrosyan@gmail.com", "pass");
        userService.add(otherUser);
        otherUser.setEmail("davit.abovyan@gmail.com");
        Assert.assertFalse(userService.edit(otherUser));
    }

    @Test(expected = DaoException.class)
    public void editUser_Fail_EmailNULL() {
        user = new User("davit", "Davit Abvoyan", "davit.abovyan@gmail.com", "pass");
        userService.add(user);
        user.setEmail(null);
        Assert.assertFalse(userService.edit(user));
    }

    @Test(expected = DaoException.class)
    public void editUser_Fail_NickNULL() {
        user = new User("davit", "Davit Abvoyan", "davit.abovyan@gmail.com", "pass");
        userService.add(user);
        user.setNick(null);
        Assert.assertFalse(userService.edit(user));
    }

    @Test(expected = DaoException.class)
    public void editUser_Fail_PassNULL() {
        user = new User("davit", "Davit Abvoyan", "davit.abovyan@gmail.com", "pass");
        userService.add(user);
        user.setPass(null);
        Assert.assertFalse(userService.edit(user));
    }

    @Test(expected = DaoException.class)
    public void editUser_Fail_EmailEmpty() {
        user = new User("davit", "Davit Abvoyan", "davit.abovyan@gmail.com", "pass");
        userService.add(user);
        user.setEmail("");
        Assert.assertFalse(userService.edit(user));
    }

    @Test(expected = DaoException.class)
    public void editUser_Fail_NickEmpty() {
        user = new User("davit", "Davit Abvoyan", "davit.abovyan@gmail.com", "pass");
        userService.add(user);
        user.setNick("");
        Assert.assertFalse(userService.edit(user));
    }

    @Test(expected = DaoException.class)
    public void editUser_Fail_PassEmpty() {
        user = new User("davit", "Davit Abvoyan", "davit.abovyan@gmail.com", "pass");
        userService.add(user);
        user.setPass("");
        Assert.assertFalse(userService.edit(user));
    }
}
