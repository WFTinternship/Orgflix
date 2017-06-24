package am.aca.orgflix.integration.daotest.jdbc;

import am.aca.dao.DaoException;
import am.aca.dao.jdbc.UserDAO;
import am.aca.entity.User;
import am.aca.orgflix.BaseIntegrationTest;
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
    @Qualifier("jdbcUserDAO")
    private UserDAO userDao;

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
        Assert.assertTrue(userDao.add(standardUser) > 0);
    }

    @Test
    public void addUser_Successed_EmptyUserName() {
        user = new User("gago", "", "gagik1@gmail.com", "pass");
        Assert.assertTrue(userDao.add(user) > 0);
    }

    @Test(expected = DaoException.class)
    public void addUser_Fail_EmptyNick() {
        userDao.add(new User("", "Gagik Petrosyan", "gagik2@gmail.com", "pass"));
    }

    @Test(expected = DaoException.class)
    public void addUser_Fail_EmptyEmail() {
        userDao.add(new User("gago", "Gagik Petrosyan", "", "pass"));
    }

    @Test(expected = DaoException.class)
    public void addUser_Fail_EmptyPass() {
        userDao.add(new User("gago", "Gagik Petrosyan", "gagik3@gmail.com", ""));
    }

    @Test(expected = DaoException.class)
    public void addUser_Fail_NullNick() {
        userDao.add(new User(null, "Gagik Petrosyan", "gagik2@gmail.com", "pass"));
    }

    @Test(expected = DaoException.class)
    public void addUser_Fail_NullEmail() {
        userDao.add(new User("gago", "Gagik Petrosyan", null, "pass"));
    }

    @Test(expected = DaoException.class)
    public void addUser_Fail_NullPass() {
        userDao.add(new User("gago", "Gagik Petrosyan", "gagik3@gmail.com", null));
    }


    @Test(expected = org.springframework.dao.DuplicateKeyException.class)
    public void addUser_Fail_EmailAlreadyExists() {
        userDao.add(standardUser);
        userDao.add(new User("armen", "Armen Hakobyan", "davit.abovyan@gmail.com", "pass1"));
    }

    @Test
    public void getUser_Succeed_ById() {
        int id = userDao.add(standardUser);
        User gotUser = userDao.get(id);
        Assert.assertTrue(standardUser.equals(gotUser));
    }

    @Test
    public void getUser_Fail_ByWrongId() {
        userDao.add(standardUser);
        int id = userDao.add(new User("gago", "Gagik Petrosyan", "davit.abovyan1@gmail.com", "pass"));
        Assert.assertFalse(standardUser.equals(userDao.get(id)));
    }

    @Test
    public void getUser_Succed_ByEmail() {
        userDao.add(standardUser);
        Assert.assertTrue(standardUser.equals(userDao.get("davit.abovyan@gmail.com")));
    }

    @Test(expected = org.springframework.dao.EmptyResultDataAccessException.class)
    public void getUser_Fail_ByWrongEmail() {
        userDao.add(standardUser);
        Assert.assertFalse(user.equals(userDao.get("davit.abovyan1@gmail.com")));
    }

    @Test
    public void editUser_Succed_Email() {
        user = new User("gago", "Gagik Petrosyan", "davit.abovyan@gmail.com", "pass");
        userDao.add(user);
        final String newEmail = "gagik@gmail.com";
        user.setEmail(newEmail);
        if (userDao.edit(user)) {
            Assert.assertEquals(newEmail, userDao.get(newEmail).getEmail());
        }
    }

    @Test
    public void editUser_Succed_Nick() {
        user = new User("gago", "Gagik Petrosyan", "davit.abovyan@gmail.com", "pass");
        int id = userDao.add(user);
        final String newNick = "davo";
        user.setNick(newNick);
        if (userDao.edit(user)) {
            Assert.assertEquals(newNick, userDao.get(id).getNick());
        }
    }

    @Test
    public void editUser_Succed_UserName() {
        user = new User("gago", "Gagik Petrosyan", "davit.abovyan@gmail.com", "pass");
        int id = userDao.add(user);
        final String newUserName = "Davit Abovyan";
        user.setUserName(newUserName);
        if (userDao.edit(user)) {
            Assert.assertEquals(newUserName, userDao.get(id).getUserName());
        }
    }

    @Test
    public void editUser_Succed_Pass() {
        user = new User("gago", "Gagik Petrosyan", "davit.abovyan@gmail.com", "pass");
        int id = userDao.add(user);
        final String newPass = "password";
        user.setPass(newPass);
        if (userDao.edit(user)) {
            Assert.assertEquals(newPass, userDao.get(id).getPass());
        }
    }

    @Test(expected = RuntimeException.class)
    public void editUser_Fail_EmailNonUnic() {
        userDao.add(standardUser);
        User otherUser = new User("gago", "Gagik Petrosyan", "gagik.petrosyan@gmail.com", "pass");
        userDao.add(otherUser);
        otherUser.setEmail("davit.abovyan@gmail.com");
        Assert.assertFalse(userDao.edit(otherUser));
    }

    @Test(expected = DaoException.class)
    public void editUser_Fail_EmailNULL() {
        user = new User("davit", "Davit Abvoyan", "davit.abovyan@gmail.com", "pass");
        userDao.add(user);
        user.setEmail(null);
        Assert.assertFalse(userDao.edit(user));
    }

    @Test(expected = DaoException.class)
    public void editUser_Fail_NickNULL() {
        user = new User("davit", "Davit Abvoyan", "davit.abovyan@gmail.com", "pass");
        userDao.add(user);
        user.setNick(null);
        Assert.assertFalse(userDao.edit(user));
    }

    @Test(expected = DaoException.class)
    public void editUser_Fail_PassNULL() {
        user = new User("davit", "Davit Abvoyan", "davit.abovyan@gmail.com", "pass");
        userDao.add(user);
        user.setPass(null);
        Assert.assertFalse(userDao.edit(user));
    }

    @Test(expected = DaoException.class)
    public void editUser_Fail_EmailEmpty() {
        user = new User("davit", "Davit Abvoyan", "davit.abovyan@gmail.com", "pass");
        userDao.add(user);
        user.setEmail("");
        Assert.assertFalse(userDao.edit(user));
    }

    @Test(expected = DaoException.class)
    public void editUser_Fail_NickEmpty() {
        user = new User("davit", "Davit Abvoyan", "davit.abovyan@gmail.com", "pass");
        userDao.add(user);
        user.setNick("");
        Assert.assertFalse(userDao.edit(user));
    }

    @Test(expected = DaoException.class)
    public void editUser_Fail_PassEmpty() {
        user = new User("davit", "Davit Abvoyan", "davit.abovyan@gmail.com", "pass");
        userDao.add(user);
        user.setPass("");
        Assert.assertFalse(userDao.edit(user));
    }
}
