package am.aca.orgflix.dao;

import am.aca.orgflix.BaseIntegrationTest;
import am.aca.orgflix.entity.User;
import am.aca.orgflix.util.TestHelper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Test for user DAO
 */

public class UserDaoTest extends BaseIntegrationTest {

    private final User standardUser = new User("gago", "Gagik Petrosyan", "davit.abovyan@gmail.com", "pass");
    @Autowired
    private UserDAO jdbcUserDAO;
    @Autowired
    private TestHelper testHelper;
    private User user;

    @After
    public void end() {
        testHelper.emptyTable(new String[]{"users"});
        user = null;
    }

    @Test
    public void addUser_Success() {
        boolean status = jdbcUserDAO.add(standardUser) > 0;
        Assert.assertTrue(status);
    }

    @Test
    public void addUser_EmptyUserName_Success() {
        user = new User("gago", "", "gagik1@gmail.com", "pass");
        boolean status = jdbcUserDAO.add(user) > 0;
        Assert.assertTrue(status);
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

        boolean status = standardUser.equals(gotUser);
        Assert.assertTrue(status);
    }

    @Test
    public void getUser_Fail_ByWrongId() {
        jdbcUserDAO.add(standardUser);
        int id = jdbcUserDAO.add(new User("gago", "Gagik Petrosyan", "davit.abovyan1@gmail.com", "pass"));

        boolean status = standardUser.equals(jdbcUserDAO.get(id));
        Assert.assertFalse(status);
    }

    @Test
    public void getUser_ByEmail_Success() {
        jdbcUserDAO.add(standardUser);

        boolean status = standardUser.equals(jdbcUserDAO.get("davit.abovyan@gmail.com"));
        Assert.assertTrue(status);
    }

    @Test(expected = org.springframework.dao.EmptyResultDataAccessException.class)
    public void getUser_Fail_ByWrongEmail() {
        jdbcUserDAO.add(standardUser);
        Assert.assertFalse(user.equals(jdbcUserDAO.get("davit.abovyan1@gmail.com")));
    }

    @Test
    public void authenticate_succeeded() {
        jdbcUserDAO.add(standardUser);

        boolean status = standardUser.equals(jdbcUserDAO.authenticate(standardUser.getEmail(), standardUser.getPass()));
        Assert.assertTrue(status);
    }

    @Test(expected = org.springframework.dao.EmptyResultDataAccessException.class)
    public void authenticate_fail_WrongPass() {
        jdbcUserDAO.add(standardUser);
        jdbcUserDAO.authenticate(standardUser.getEmail(), "xxx");
    }

    @Test
    public void editUser_Email_Succed() {
        user = new User("gago", "Gagik Petrosyan", "davit.abovyan@gmail.com", "pass");
        jdbcUserDAO.add(user);
        final String newEmail = "gagik@gmail.com";
        user.setEmail(newEmail);
        jdbcUserDAO.edit(user);
        String actualEmail = jdbcUserDAO.get(newEmail).getEmail();
        Assert.assertEquals(newEmail, actualEmail);
    }

    @Test
    public void editUser_Nick_Success() {
        user = new User("gago", "Gagik Petrosyan", "davit.abovyan@gmail.com", "pass");
        int id = jdbcUserDAO.add(user);
        final String newNick = "davo";
        user.setNick(newNick);
        jdbcUserDAO.edit(user);

        String actualNick = jdbcUserDAO.get(id).getNick();
        Assert.assertEquals(newNick, actualNick);

    }

    @Test
    public void editUser_UserName_Success() {
        user = new User("gago", "Gagik Petrosyan", "davit.abovyan@gmail.com", "pass");
        int id = jdbcUserDAO.add(user);
        final String newUserName = "Davit Abovyan";
        user.setUserName(newUserName);
        jdbcUserDAO.edit(user);

        String actualName = jdbcUserDAO.get(id).getUserName();
        Assert.assertEquals(newUserName, actualName);

    }

    @Test
    public void editUser_Pass_Success() {
        user = new User("gago", "Gagik Petrosyan", "davit.abovyan@gmail.com", "pass");
        int id = jdbcUserDAO.add(user);
        final String newPass = "password";
        user.setPass(newPass);
        jdbcUserDAO.edit(user);

        String actualPass = jdbcUserDAO.get(id).getPass();
        Assert.assertEquals(newPass, actualPass);

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
