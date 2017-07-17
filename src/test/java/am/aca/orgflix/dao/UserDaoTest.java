package am.aca.orgflix.dao;

import am.aca.orgflix.BaseIntegrationTest;
import am.aca.orgflix.entity.User;
import am.aca.orgflix.util.TestHelper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * Integration tests for user DAO
 */

public class UserDaoTest extends BaseIntegrationTest {

    private final User standardUser = new User("scarface", "Tony Montana", "scarface@miami.com", "elvira");

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
    @Test(expected = RuntimeException.class)
    public void addUser_NullNick_Fail() {
        jdbcUserDAO.add(new User(null, "Tony Montana", "scarface@miami.com", "elvira"));
    }

    /**
     * @see UserDAO#add(am.aca.orgflix.entity.User)
     */
    @Test(expected = RuntimeException.class)
    public void addUser_NullEmail_Fail() {
        jdbcUserDAO.add(new User("scarface", "Tony Montana", null, "elvira"));
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
    @Test(expected = RuntimeException.class)
    public void addUser_EmailAlreadyExists_Fail() {
        jdbcUserDAO.add(standardUser);
        jdbcUserDAO.add(new User("scarface6", "Tony Montana", "scarface@miami.com", "elvira"));
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
    @Test(expected = RuntimeException.class)
    public void getUser_ByWrongId_Fail() {
        jdbcUserDAO.add(standardUser);

        jdbcUserDAO.getById(-1);
    }

    /**
     * @see UserDAO#getByEmail(String)
     */
    @Test
    public void getUser_ByEmail_Success() {
        jdbcUserDAO.add(standardUser);

        User actualUser = jdbcUserDAO.getByEmail("scarface@miami.com");
        Assert.assertEquals(standardUser, actualUser);
    }

    /**
     * @see UserDAO#getByEmail(String)
     */
    @Test(expected = RuntimeException.class)
    public void getUser_ByWrongEmail_Fail() {
        jdbcUserDAO.add(standardUser);
        jdbcUserDAO.getByEmail("Sscarface@miami.com");
    }

    /**
     * @see UserDAO#getByNick(String)
     */
    @Test
    public void getByNick_Success() {
        jdbcUserDAO.add(standardUser);
        User actualUser = jdbcUserDAO.getByNick("scarface");
        Assert.assertEquals(standardUser, actualUser);
    }

    /**
     * @see UserDAO#getByNick(String)
     */
    @Test(expected = RuntimeException.class)
    public void getByNick_NotExisting_Fail() {
        jdbcUserDAO.getByNick("scarface");
    }

    /**
     * @see UserDAO#authenticate(java.lang.String, java.lang.String)
     */
    @Test
    public void authenticate_Success() {
        jdbcUserDAO.add(standardUser);

        User actualUser = jdbcUserDAO.authenticate(standardUser.getEmail(), standardUser.getPass());
        Assert.assertEquals(standardUser, actualUser);
    }

    /**
     * @see UserDAO#authenticate(java.lang.String, java.lang.String)
     */
    @Test(expected = RuntimeException.class)
    public void authenticate_WrongPass_Fail() {
        jdbcUserDAO.add(standardUser);
        jdbcUserDAO.authenticate(standardUser.getEmail(), "xxx");
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
    @Test(expected = RuntimeException.class)
    public void editUser_EmailNonUnique_Fail() {
        jdbcUserDAO.add(standardUser);

        User otherUser = new User("bot", "Tony Montana", "scarface@gmail.com", "elvira");
        jdbcUserDAO.add(otherUser);

        otherUser.setEmail("scarface@miami.com");
        jdbcUserDAO.edit(user);
    }

    /**
     * @see UserDAO#edit(am.aca.orgflix.entity.User)
     */
    @Test(expected = RuntimeException.class)
    public void editUser_EmailNULL_Fail() {
        user = new User("scarface", "Tony Montana", "scarface@miami.com", "pass");
        jdbcUserDAO.add(user);
        user.setEmail(null);

        jdbcUserDAO.edit(user);
    }

    /**
     * @see UserDAO#edit(am.aca.orgflix.entity.User)
     */
    @Test(expected = RuntimeException.class)
    public void editUser_NickNULL_Fail() {
        user = new User("scarface", "Tony Montana", "scarface@miami.com", "pass");
        jdbcUserDAO.add(user);
        user.setNick(null);

        jdbcUserDAO.edit(user);
    }

    /**
     * @see UserDAO#edit(am.aca.orgflix.entity.User)
     */
    @Test(expected = RuntimeException.class)
    public void editUser_PassNULL_Fail() {
        user = new User("scarface", "Tony Montana", "scarface@miami.com", "pass");
        jdbcUserDAO.add(user);
        user.setPass(null);

        jdbcUserDAO.edit(user);
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
}
