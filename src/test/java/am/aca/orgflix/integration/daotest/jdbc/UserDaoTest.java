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


/**
 * Test for user DAO
 */

public class UserDaoTest extends BaseIntegrationTest {

    @Autowired
    private UserDAO jdbcUserDAO;

    @Autowired
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
        Assert.assertTrue(jdbcUserDAO.add(standardUser) > 0);
    }

    @Test
    public void addUser_Successed_EmptyUserName() {
        user = new User("gago", "", "gagik1@gmail.com", "pass");
        Assert.assertTrue(jdbcUserDAO.add(user) > 0);
    }

    @Test(expected = DaoException.class)
    public void addUser_Fail_EmptyNick() {
        jdbcUserDAO.add(new User("", "Gagik Petrosyan", "gagik2@gmail.com", "pass"));
    }

    @Test(expected = DaoException.class)
    public void addUser_Fail_EmptyEmail() {
        jdbcUserDAO.add(new User("gago", "Gagik Petrosyan", "", "pass"));
    }

    @Test(expected = DaoException.class)
    public void addUser_Fail_EmptyPass() {
        jdbcUserDAO.add(new User("gago", "Gagik Petrosyan", "gagik3@gmail.com", ""));
    }

    @Test(expected = DaoException.class)
    public void addUser_Fail_NullNick() {
        jdbcUserDAO.add(new User(null, "Gagik Petrosyan", "gagik2@gmail.com", "pass"));
    }

    @Test(expected = DaoException.class)
    public void addUser_Fail_NullEmail() {
        jdbcUserDAO.add(new User("gago", "Gagik Petrosyan", null, "pass"));
    }

    @Test(expected = DaoException.class)
    public void addUser_Fail_NullPass() {
        jdbcUserDAO.add(new User("gago", "Gagik Petrosyan", "gagik3@gmail.com", null));
    }


    @Test(expected = org.springframework.dao.DuplicateKeyException.class)
    public void addUser_Fail_EmailAlreadyExists() {
        jdbcUserDAO.add(standardUser);
        jdbcUserDAO.add(new User("armen", "Armen Hakobyan", "davit.abovyan@gmail.com", "pass1"));
    }

    @Test
    public void getUser_Succeed_ById() {
        int id = jdbcUserDAO.add(standardUser);
        User gotUser = jdbcUserDAO.get(id);
        Assert.assertTrue(standardUser.equals(gotUser));
    }

    @Test
    public void getUser_Fail_ByWrongId() {
        jdbcUserDAO.add(standardUser);
        int id = jdbcUserDAO.add(new User("gago", "Gagik Petrosyan", "davit.abovyan1@gmail.com", "pass"));
        Assert.assertFalse(standardUser.equals(jdbcUserDAO.get(id)));
    }

    @Test
    public void getUser_Succed_ByEmail() {
        jdbcUserDAO.add(standardUser);
        Assert.assertTrue(standardUser.equals(jdbcUserDAO.get("davit.abovyan@gmail.com")));
    }

    @Test(expected = org.springframework.dao.EmptyResultDataAccessException.class)
    public void getUser_Fail_ByWrongEmail() {
        jdbcUserDAO.add(standardUser);
        Assert.assertFalse(user.equals(jdbcUserDAO.get("davit.abovyan1@gmail.com")));
    }

    @Test
    public void authenticate_succeeded(){
        jdbcUserDAO.add(standardUser);
        Assert.assertTrue(
                standardUser.equals(jdbcUserDAO.authenticate(standardUser.getEmail(),standardUser.getPass()))
        );
    }

    @Test(expected = org.springframework.dao.EmptyResultDataAccessException.class)
    public void authenticate_fail_WrongPass(){
        jdbcUserDAO.add(standardUser);
        jdbcUserDAO.authenticate(standardUser.getEmail(),"xxx");
    }

    @Test
    public void editUser_Succed_Email() {
        user = new User("gago", "Gagik Petrosyan", "davit.abovyan@gmail.com", "pass");
        jdbcUserDAO.add(user);
        final String newEmail = "gagik@gmail.com";
        user.setEmail(newEmail);
        if (jdbcUserDAO.edit(user)) {
            Assert.assertEquals(newEmail, jdbcUserDAO.get(newEmail).getEmail());
        }
    }

    @Test
    public void editUser_Succed_Nick() {
        user = new User("gago", "Gagik Petrosyan", "davit.abovyan@gmail.com", "pass");
        int id = jdbcUserDAO.add(user);
        final String newNick = "davo";
        user.setNick(newNick);
        if (jdbcUserDAO.edit(user)) {
            Assert.assertEquals(newNick, jdbcUserDAO.get(id).getNick());
        }
    }

    @Test
    public void editUser_Succed_UserName() {
        user = new User("gago", "Gagik Petrosyan", "davit.abovyan@gmail.com", "pass");
        int id = jdbcUserDAO.add(user);
        final String newUserName = "Davit Abovyan";
        user.setUserName(newUserName);
        if (jdbcUserDAO.edit(user)) {
            Assert.assertEquals(newUserName, jdbcUserDAO.get(id).getUserName());
        }
    }

    @Test
    public void editUser_Succed_Pass() {
        user = new User("gago", "Gagik Petrosyan", "davit.abovyan@gmail.com", "pass");
        int id = jdbcUserDAO.add(user);
        final String newPass = "password";
        user.setPass(newPass);
        if (jdbcUserDAO.edit(user)) {
            Assert.assertEquals(newPass, jdbcUserDAO.get(id).getPass());
        }
    }

    @Test(expected = RuntimeException.class)
    public void editUser_Fail_EmailNonUnic() {
        jdbcUserDAO.add(standardUser);
        User otherUser = new User("gago", "Gagik Petrosyan", "gagik.petrosyan@gmail.com", "pass");
        jdbcUserDAO.add(otherUser);
        otherUser.setEmail("davit.abovyan@gmail.com");
        Assert.assertFalse(jdbcUserDAO.edit(otherUser));
    }

    @Test(expected = DaoException.class)
    public void editUser_Fail_EmailNULL() {
        user = new User("davit", "Davit Abvoyan", "davit.abovyan@gmail.com", "pass");
        jdbcUserDAO.add(user);
        user.setEmail(null);
        Assert.assertFalse(jdbcUserDAO.edit(user));
    }

    @Test(expected = DaoException.class)
    public void editUser_Fail_NickNULL() {
        user = new User("davit", "Davit Abvoyan", "davit.abovyan@gmail.com", "pass");
        jdbcUserDAO.add(user);
        user.setNick(null);
        Assert.assertFalse(jdbcUserDAO.edit(user));
    }

    @Test(expected = DaoException.class)
    public void editUser_Fail_PassNULL() {
        user = new User("davit", "Davit Abvoyan", "davit.abovyan@gmail.com", "pass");
        jdbcUserDAO.add(user);
        user.setPass(null);
        Assert.assertFalse(jdbcUserDAO.edit(user));
    }

    @Test(expected = DaoException.class)
    public void editUser_Fail_EmailEmpty() {
        user = new User("davit", "Davit Abvoyan", "davit.abovyan@gmail.com", "pass");
        jdbcUserDAO.add(user);
        user.setEmail("");
        Assert.assertFalse(jdbcUserDAO.edit(user));
    }

    @Test(expected = DaoException.class)
    public void editUser_Fail_NickEmpty() {
        user = new User("davit", "Davit Abvoyan", "davit.abovyan@gmail.com", "pass");
        jdbcUserDAO.add(user);
        user.setNick("");
        Assert.assertFalse(jdbcUserDAO.edit(user));
    }

    @Test(expected = DaoException.class)
    public void editUser_Fail_PassEmpty() {
        user = new User("davit", "Davit Abvoyan", "davit.abovyan@gmail.com", "pass");
        jdbcUserDAO.add(user);
        user.setPass("");
        Assert.assertFalse(jdbcUserDAO.edit(user));
    }
}
