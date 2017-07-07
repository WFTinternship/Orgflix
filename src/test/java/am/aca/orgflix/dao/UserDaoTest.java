package am.aca.orgflix.dao;

import am.aca.orgflix.BaseIntegrationTest;
import am.aca.orgflix.dao.impljdbc.JdbcUserDAO;
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

    private final User standardUser = new User("gago", "Gagik Petrosyan", "davit.abovyan@gmail.com", "pass");

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
        testHelper.emptyTable(new String[]{"users"});
        user = null;
    }

    /**
     * @see JdbcUserDAO#add(am.aca.orgflix.entity.User)
     */
    @Test
    public void addUser_Success() {
        boolean status = jdbcUserDAO.add(standardUser) > 0;
        Assert.assertTrue(status);
    }

    /**
     * @see JdbcUserDAO#add(am.aca.orgflix.entity.User)
     */
    @Test
    public void addUser_EmptyUserName_Success() {
        user = new User("gago", "", "gagik1@gmail.com", "pass");
        boolean status = jdbcUserDAO.add(user) > 0;
        Assert.assertTrue(status);
    }

    /**
     * @see JdbcUserDAO#add(am.aca.orgflix.entity.User)
     */
    @Test(expected = DaoException.class)
    public void addUser_EmptyNick_Fail() {
        jdbcUserDAO.add(new User("", "Gagik Petrosyan", "gagik2@gmail.com", "pass"));
    }

    /**
     * @see JdbcUserDAO#add(am.aca.orgflix.entity.User)
     */
    @Test(expected = DaoException.class)
    public void addUser_EmptyEmail_Fail() {
        jdbcUserDAO.add(new User("gago", "Gagik Petrosyan", "", "pass"));
    }

    /**
     * @see JdbcUserDAO#add(am.aca.orgflix.entity.User)
     */
    @Test(expected = DaoException.class)
    public void addUser_EmptyPass_Fail() {
        jdbcUserDAO.add(new User("gago", "Gagik Petrosyan", "gagik3@gmail.com", ""));
    }

    /**
     * @see JdbcUserDAO#add(am.aca.orgflix.entity.User)
     */
    @Test(expected = DaoException.class)
    public void addUser_NullNick_Fail() {
        jdbcUserDAO.add(new User(null, "Gagik Petrosyan", "gagik2@gmail.com", "pass"));
    }

    /**
     * @see JdbcUserDAO#add(am.aca.orgflix.entity.User)
     */
    @Test(expected = DaoException.class)
    public void addUser_NullEmail_Fail() {
        jdbcUserDAO.add(new User("gago", "Gagik Petrosyan", null, "pass"));
    }

    /**
     * @see JdbcUserDAO#add(am.aca.orgflix.entity.User)
     */
    @Test(expected = DaoException.class)
    public void addUser_NullPass_Fail() {
        jdbcUserDAO.add(new User("gago", "Gagik Petrosyan", "gagik3@gmail.com", null));
    }

    /**
     * @see JdbcUserDAO#add(am.aca.orgflix.entity.User)
     */
    @Test(expected = org.springframework.dao.DuplicateKeyException.class)
    public void addUser_EmailAlreadyExists_Fail() {
        jdbcUserDAO.add(standardUser);
        jdbcUserDAO.add(new User("armen", "Armen Hakobyan", "davit.abovyan@gmail.com", "pass1"));
    }

    /**
     * @see JdbcUserDAO#get(int)
     */
    @Test
    public void getUser_ById_Success() {
        int id = jdbcUserDAO.add(standardUser);
        User gotUser = jdbcUserDAO.get(id);

        boolean status = standardUser.equals(gotUser);
        Assert.assertTrue(status);
    }

    /**
     * @see JdbcUserDAO#get(int)
     */
    @Test
    public void getUser_ByWrongId_Fail() {
        jdbcUserDAO.add(standardUser);
        int id = jdbcUserDAO.add(new User("gago", "Gagik Petrosyan", "davit.abovyan1@gmail.com", "pass"));

        boolean status = standardUser.equals(jdbcUserDAO.get(id));
        Assert.assertFalse(status);
    }

    /**
     * @see JdbcUserDAO#get(String)
     */
    @Test
    public void getUser_ByEmail_Success() {
        jdbcUserDAO.add(standardUser);

        boolean status = standardUser.equals(jdbcUserDAO.get("davit.abovyan@gmail.com"));
        Assert.assertTrue(status);
    }

    /**
     * @see JdbcUserDAO#get(String)
     */
    @Test(expected = org.springframework.dao.EmptyResultDataAccessException.class)
    public void getUser_ByWrongEmail_Fail() {
        jdbcUserDAO.add(standardUser);
        Assert.assertFalse(user.equals(jdbcUserDAO.get("davit.abovyan1@gmail.com")));
    }

    /**
     * @see JdbcUserDAO#authenticate(java.lang.String, java.lang.String)
     */
    @Test
    public void authenticate_Success() {
        jdbcUserDAO.add(standardUser);

        boolean status = standardUser.equals(jdbcUserDAO.authenticate(standardUser.getEmail(), standardUser.getPass()));
        Assert.assertTrue(status);
    }

    /**
     * @see JdbcUserDAO#authenticate(java.lang.String, java.lang.String)
     */
    @Test(expected = org.springframework.dao.EmptyResultDataAccessException.class)
    public void authenticate_WrongPass_Fail() {
        jdbcUserDAO.add(standardUser);
        jdbcUserDAO.authenticate(standardUser.getEmail(), "xxx");
    }

    /**
     * @see JdbcUserDAO#edit(am.aca.orgflix.entity.User)
     */
    @Test
    public void editUser_Email_Success() {
        user = new User("gago", "Gagik Petrosyan", "davit.abovyan@gmail.com", "pass");
        jdbcUserDAO.add(user);
        final String newEmail = "gagik@gmail.com";
        user.setEmail(newEmail);
        jdbcUserDAO.edit(user);
        String actualEmail = jdbcUserDAO.get(newEmail).getEmail();
        Assert.assertEquals(newEmail, actualEmail);
    }

    /**
     * @see JdbcUserDAO#edit(am.aca.orgflix.entity.User)
     */
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

    /**
     * @see JdbcUserDAO#edit(am.aca.orgflix.entity.User)
     */
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

    /**
     * @see JdbcUserDAO#edit(am.aca.orgflix.entity.User)
     */
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

    /**
     * @see JdbcUserDAO#edit(am.aca.orgflix.entity.User)
     */
    @Test(expected = RuntimeException.class)
    public void editUser_EmailNonUnique_Fail() {
        jdbcUserDAO.add(standardUser);
        User otherUser = new User("gago", "Gagik Petrosyan", "gagik.petrosyan@gmail.com", "pass");
        jdbcUserDAO.add(otherUser);
        otherUser.setEmail("davit.abovyan@gmail.com");
        boolean status = jdbcUserDAO.edit(otherUser);
        Assert.assertFalse(status);
    }

    /**
     * @see JdbcUserDAO#edit(am.aca.orgflix.entity.User)
     */
    @Test(expected = DaoException.class)
    public void editUser_EmailNULL_Fail() {
        user = new User("davit", "Davit Abvoyan", "davit.abovyan@gmail.com", "pass");
        jdbcUserDAO.add(user);
        user.setEmail(null);
        boolean status = jdbcUserDAO.edit(user);
        Assert.assertFalse(status);
    }

    /**
     * @see JdbcUserDAO#edit(am.aca.orgflix.entity.User)
     */
    @Test(expected = DaoException.class)
    public void editUser_NickNULL_Fail() {
        user = new User("davit", "Davit Abvoyan", "davit.abovyan@gmail.com", "pass");
        jdbcUserDAO.add(user);
        user.setNick(null);
        boolean status = jdbcUserDAO.edit(user);
        Assert.assertFalse(status);
    }

    /**
     * @see JdbcUserDAO#edit(am.aca.orgflix.entity.User)
     */
    @Test(expected = DaoException.class)
    public void editUser_PassNULL_Fail() {
        user = new User("davit", "Davit Abvoyan", "davit.abovyan@gmail.com", "pass");
        jdbcUserDAO.add(user);
        user.setPass(null);
        boolean status = jdbcUserDAO.edit(user);
        Assert.assertFalse(status);
    }

    /**
     * @see JdbcUserDAO#edit(am.aca.orgflix.entity.User)
     */
    @Test(expected = DaoException.class)
    public void editUser_EmailEmpty_Fail() {
        user = new User("davit", "Davit Abvoyan", "davit.abovyan@gmail.com", "pass");
        jdbcUserDAO.add(user);
        user.setEmail("");
        boolean status = jdbcUserDAO.edit(user);
        Assert.assertFalse(status);
    }

    /**
     * @see JdbcUserDAO#edit(am.aca.orgflix.entity.User)
     */
    @Test(expected = DaoException.class)
    public void editUser_NickEmpty_Fail() {
        user = new User("davit", "Davit Abvoyan", "davit.abovyan@gmail.com", "pass");
        jdbcUserDAO.add(user);
        user.setNick("");
        boolean status = jdbcUserDAO.edit(user);
        Assert.assertFalse(status);
    }

    /**
     * @see JdbcUserDAO#edit(am.aca.orgflix.entity.User)
     */
    @Test(expected = DaoException.class)
    public void editUser_PassEmpty_Fail() {
        user = new User("davit", "Davit Abvoyan", "davit.abovyan@gmail.com", "pass");
        jdbcUserDAO.add(user);
        user.setPass("");
        boolean status = jdbcUserDAO.edit(user);
        Assert.assertFalse(status);
    }

    /**
     * @see JdbcUserDAO#remove(int)
     */
    @Test
    public void Remove_Success() {
        jdbcUserDAO.add(standardUser);
        boolean status = jdbcUserDAO.remove(standardUser.getId());
        Assert.assertTrue(status);
    }

    /**
     * @see JdbcUserDAO#remove(int)
     */
    @Test
    public void Remove_Fail() {
        boolean status = jdbcUserDAO.remove(standardUser.getId());
        Assert.assertFalse(status);
    }

    /**
     * @see JdbcUserDAO#remove(int)
     */
    @Test(expected = RuntimeException.class)
    public void Remove_CheckByGet_Success() {
        jdbcUserDAO.add(standardUser);
        jdbcUserDAO.remove(standardUser.getId());
        jdbcUserDAO.get(standardUser.getId());
    }

    /**
     * @see JdbcUserDAO#remove(int)
     */
    @Test
    public void Remove_NotExisting_Fail() {
        boolean status = jdbcUserDAO.remove(standardUser.getId());
        Assert.assertFalse(status);
    }
}
