package am.aca.orgflix.dao;

import am.aca.orgflix.BaseIntegrationTest;
import am.aca.orgflix.entity.User;
import am.aca.orgflix.util.TestHelper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


/**
 * Integration tests for user DAO
 */

public class UserDaoTest extends BaseIntegrationTest {

    private final User standardUser = new User("Davit", "Davit Abovyan", "davit.abovyan@gmail.com", "123456");

    @Autowired
    private UserDAO jdbcUserDAO;

    @Autowired
    private TestHelper testHelper;

    private User user;

    /**
     * Rolls back all changes applied to the test DB resulted from tests
     */
    @After
    public void tearDown() {
        testHelper.emptyTable(new String[]{"USERS"});
        user = null;
    }

    /**
     * @see UserDAO#add(am.aca.orgflix.entity.User)
     */
    @Test
    public void addUser_Success() {
        int id = jdbcUserDAO.add(standardUser);
        Assert.assertTrue(id > 0);
    }

    /**
     * @see UserDAO#add(am.aca.orgflix.entity.User)
     */
    @Test
    public void addUser_EmptyUserName_Success() {
        user = new User("scarface", "", "scarface@miami.com", "elvira");
        int id = jdbcUserDAO.add(user);
        Assert.assertTrue(id > 0);
    }

    /**
     * @see UserDAO#add(am.aca.orgflix.entity.User)
     */
    @Test
    public void addUser_NullNick_Fail() {
        try {
            jdbcUserDAO.add(new User(null, "Tony Montana", "scarface@miami.com", "elvira"));
        } catch (RuntimeException e) {
            Assert.assertTrue(e instanceof RuntimeException);
        }
    }

    /**
     * @see UserDAO#add(am.aca.orgflix.entity.User)
     */
    @Test
    public void addUser_NullEmail_Fail() {
        try {
            jdbcUserDAO.add(new User("scarface", "Tony Montana", null, "elvira"));
        } catch (RuntimeException e) {
            Assert.assertTrue(e instanceof RuntimeException);
        }
    }

    /**
     * @see UserDAO#add(am.aca.orgflix.entity.User)
     */
    @Test(expected = RuntimeException.class)
    public void addUser_NullPass_Fail() {
        jdbcUserDAO.add(new User("scarface", "Tony Montana", "scarface@miami.com", null));
    }

    /**
     * @see UserDAO#add(am.aca.orgflix.entity.User)
     */
    @Test
    public void addUser_EmailAlreadyExists_Fail() {
        try {
            jdbcUserDAO.add(standardUser);
            jdbcUserDAO.add(new User("scarface", "Tony Montana", "davit.abovyan@gmail.com", "elvira"));
        } catch (RuntimeException e) {
            Assert.assertTrue(e instanceof RuntimeException);
        }
    }

    /**
     * @see UserDAO#getById(int)
     */
    @Test
    public void getUser_ById_Success() {
        int id = jdbcUserDAO.add(standardUser);
        User actualUser = jdbcUserDAO.getById(id);

        Assert.assertEquals(standardUser, actualUser);
    }

    /**
     * @see UserDAO#getById(int)
     */
    @Test
    public void getUser_ByWrongId_Fail() {
        try {
            jdbcUserDAO.add(standardUser);
            jdbcUserDAO.getById(-1);
        } catch (RuntimeException e) {
            Assert.assertTrue(e instanceof RuntimeException);
        }
    }

    /**
     * @see UserDAO#getByEmail(String)
     */
    @Test
    public void getUser_ByEmail_Success() {
        jdbcUserDAO.add(standardUser);

        User actualUser = jdbcUserDAO.getByEmail("davit.abovyan@gmail.com");
        Assert.assertEquals(standardUser, actualUser);
    }

    /**
     * @see UserDAO#getByEmail(String)
     */
    @Test
    public void getUser_ByWrongEmail_Fail() {
        try {
            jdbcUserDAO.add(standardUser);
            jdbcUserDAO.getByEmail("Sscarface@miami.com");
        } catch (RuntimeException e) {
            Assert.assertTrue(e instanceof RuntimeException);
        }
    }

    /**
     * @see UserDAO#getByNick(String)
     */
    @Test
    public void getByNick_Success() {
        jdbcUserDAO.add(standardUser);
        User actualUser = jdbcUserDAO.getByNick("Davit");
        Assert.assertEquals(standardUser, actualUser);
    }

    /**
     * @see UserDAO#getByNick(String)
     */
    @Test
    public void getByNick_NotExisting_Fail() {
        try {
            jdbcUserDAO.getByNick("scarface");
        } catch (RuntimeException e) {
            Assert.assertTrue(e instanceof RuntimeException);
        }
    }

    /**
     * @see UserDAO#getAll()
     */
    @Test
    public void getAll_OneUser_Success() {
        jdbcUserDAO.add(standardUser);
        List<User> list = jdbcUserDAO.getAll();
        Assert.assertEquals("Davit", list.get(0).getNick());
    }

    /**
     * @see UserDAO#authenticate(java.lang.String, java.lang.String)
     */
    @Test
    public void authenticate_Success() {
        jdbcUserDAO.add(standardUser);

        boolean status = jdbcUserDAO.authenticate(standardUser.getEmail(), standardUser.getPass());
        Assert.assertTrue(status);
    }

    /**
     * @see UserDAO#authenticate(java.lang.String, java.lang.String)
     */
    @Test
    public void authenticate_WrongPass_Fail() {
        jdbcUserDAO.add(standardUser);
        boolean status = jdbcUserDAO.authenticate(standardUser.getEmail(), "xxx");
        Assert.assertFalse(status);
    }

    /**
     * @see UserDAO#edit(am.aca.orgflix.entity.User)
     */
    @Test
    public void editUser_Email_Success() {
        user = new User("scarface", "Tony Montana", "scarface@miami.com", "elvira");
        int id = jdbcUserDAO.add(user);

        final String newEmail = "scarface@miami.com";
        user.setEmail(newEmail);
        boolean status = jdbcUserDAO.edit(user);

        String actualEmail = jdbcUserDAO.getById(id).getEmail();
        Assert.assertTrue(status);
        Assert.assertEquals(newEmail, actualEmail);
    }

    /**
     * @see UserDAO#edit(am.aca.orgflix.entity.User)
     */
    @Test
    public void editUser_Nick_Success() {
        user = new User("scarface", "Tony Montana", "scarface@miami.com", "elvira");
        int id = jdbcUserDAO.add(user);

        final String newNick = "scarface Tony";
        user.setNick(newNick);
        boolean status = jdbcUserDAO.edit(user);

        String actualNick = jdbcUserDAO.getById(id).getNick();
        Assert.assertTrue(status);
        Assert.assertEquals(newNick, actualNick);

    }

    /**
     * @see UserDAO#edit(am.aca.orgflix.entity.User)
     */
    @Test
    public void editUser_UserName_Success() {
        user = new User("scarface", "Tony Montana", "scarface@miami.com", "elvira");
        int id = jdbcUserDAO.add(user);

        final String newUserName = "Tony Montana Jr";
        user.setUserName(newUserName);
        boolean status = jdbcUserDAO.edit(user);

        String actualName = jdbcUserDAO.getById(id).getUserName();
        Assert.assertTrue(status);
        Assert.assertEquals(newUserName, actualName);

    }

    /**
     * @see UserDAO#edit(am.aca.orgflix.entity.User)
     */
    @Test
    public void editUser_Pass_Success() {
        user = new User("scarface", "Tony Montana", "scarface@miami.com", "elvira");
        int id = jdbcUserDAO.add(user);

        final String newPass = "elvira<3";
        user.setPass(newPass);
        boolean status = jdbcUserDAO.edit(user);

        String actualPass = jdbcUserDAO.getById(id).getPass();
        Assert.assertTrue(status);
        Assert.assertEquals(newPass, actualPass);

    }

    /**
     * @see UserDAO#edit(am.aca.orgflix.entity.User)
     */
    @Test
    public void editUser_EmailNonUnique_Fail() {
        User otherUser = new User("bot", "Tony Montana", "scarface@gmail.com", "elvira");

        try {
            jdbcUserDAO.add(standardUser);
            jdbcUserDAO.add(otherUser);

            otherUser.setEmail("scarface@miami.com");
            jdbcUserDAO.edit(user);
        } catch (RuntimeException e) {
            Assert.assertTrue(e instanceof RuntimeException);
        }

    }

    /**
     * @see UserDAO#edit(am.aca.orgflix.entity.User)
     */
    @Test
    public void editUser_EmailNULL_Fail() {
        user = new User("scarface", "Tony Montana", "scarface@miami.com", "pass");
        try {
            jdbcUserDAO.add(user);
            user.setEmail(null);
            jdbcUserDAO.edit(user);
        } catch (RuntimeException e) {
            Assert.assertTrue(e instanceof RuntimeException);
        }

    }

    /**
     * @see UserDAO#edit(am.aca.orgflix.entity.User)
     */
    @Test
    public void editUser_NickNULL_Fail() {
        user = new User("scarface", "Tony Montana", "scarface@miami.com", "pass");
        try {
            jdbcUserDAO.add(user);
            user.setNick(null);

            jdbcUserDAO.edit(user);
        } catch (RuntimeException e) {
            Assert.assertTrue(e instanceof RuntimeException);
        }
    }

    /**
     * @see UserDAO#edit(am.aca.orgflix.entity.User)
     */
    @Test
    public void editUser_PassNULL_Fail() {
        user = new User("scarface", "Tony Montana", "scarface@miami.com", "pass");
        try {
            jdbcUserDAO.add(user);
            user.setPass(null);

            jdbcUserDAO.edit(user);
        } catch (RuntimeException e) {
            Assert.assertTrue(e instanceof RuntimeException);
        }
    }

    /**
     * @see UserDAO#remove(int)
     */
    @Test
    public void Remove_ValidUser_Success() {
        jdbcUserDAO.add(standardUser);

        boolean status = jdbcUserDAO.remove(standardUser.getId());
        Assert.assertTrue(status);
    }

    /**
     * @see UserDAO#remove(int)
     */
    @Test
    public void Remove_InvalidUser_Fail() {
        boolean status = jdbcUserDAO.remove(standardUser.getId());
        Assert.assertFalse(status);
    }

    /**
     * @see UserDAO#emailIsUsed(String)
     */
    @Test
    public void emailIsUsed_ValidEmail_Success() {
        boolean status = jdbcUserDAO.emailIsUsed(standardUser.getEmail());
        Assert.assertFalse(status);

        jdbcUserDAO.add(standardUser);
        status = jdbcUserDAO.emailIsUsed(standardUser.getEmail());
        Assert.assertTrue(status);
    }

    /**
     * @see UserDAO#emailIsUsed(String)
     */
    @Test
    public void nickIsUsed_ValidEmail_Success() {
        boolean status = jdbcUserDAO.nickIsUsed(standardUser.getNick());
        Assert.assertFalse(status);

        jdbcUserDAO.add(standardUser);
        status = jdbcUserDAO.nickIsUsed(standardUser.getNick());
        Assert.assertTrue(status);
    }

    /**
     * @see UserDAO#reset(int)
     */
    @Test
    public void reset_ValidUser_Success() {
        int id = jdbcUserDAO.add(standardUser);

        boolean status = jdbcUserDAO.reset(id);
        Assert.assertTrue(status);

        User actualUser = jdbcUserDAO.getById(id);
        Assert.assertEquals("", actualUser.getEmail());
        Assert.assertEquals("", actualUser.getNick());
        Assert.assertEquals("", actualUser.getPass());
        Assert.assertEquals("", actualUser.getUserName());
    }

    /**
     * @see UserDAO#reset(int)
     */
    @Test
    public void reset_InvalidUser_Fail() {
        boolean status = jdbcUserDAO.reset(0);
        Assert.assertFalse(status);
    }
}
