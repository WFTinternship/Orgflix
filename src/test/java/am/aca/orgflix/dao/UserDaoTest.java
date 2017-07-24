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
    private UserDAO hibernateUserDAO;

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
     * @see am.aca.orgflix.dao.implHibernate.HibernateUserDAO#add(am.aca.orgflix.entity.User)
     */
    @Test
    public void addUser_Success() {
        int id = hibernateUserDAO.add(standardUser);
        Assert.assertTrue(id > 0);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateUserDAO#add(am.aca.orgflix.entity.User)
     */
    @Test
    public void addUser_EmptyUserName_Success() {
        user = new User("scarface", "", "scarface@miami.com", "elvira");
        int id = hibernateUserDAO.add(user);
        Assert.assertTrue(id > 0);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateUserDAO#add(am.aca.orgflix.entity.User)
     */
    @Test
    public void addUser_NullNick_Fail() {
        int id = hibernateUserDAO.add(new User(null, "Tony Montana", "scarface@miami.com", "elvira"));
        Assert.assertEquals(-1, id);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateUserDAO#add(am.aca.orgflix.entity.User)
     */
    @Test
    public void addUser_NullEmail_Fail() {
        int id = hibernateUserDAO.add(new User("scarface", "Tony Montana", null, "elvira"));
        Assert.assertEquals(-1, id);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateUserDAO#add(am.aca.orgflix.entity.User)
     */
    @Test
    public void addUser_NullPass_Fail() {
        int id = hibernateUserDAO.add(new User("scarface", "Tony Montana", "scarface@miami.com", null));
        Assert.assertEquals(-1, id);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateUserDAO#getById(int)
     */
    @Test
    public void getUser_ById_Success() {
        int id = hibernateUserDAO.add(standardUser);
        User actualUser = hibernateUserDAO.getById(id);

        Assert.assertEquals(standardUser, actualUser);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateUserDAO#getById(int)
     */
    @Test
    public void getUser_ByWrongId_Fail() {
        hibernateUserDAO.add(standardUser);

        User actualUser = hibernateUserDAO.getById(-1);
        Assert.assertNull(actualUser);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateUserDAO#getByEmail(String)
     */
    @Test
    public void getUser_ByEmail_Success() {
        hibernateUserDAO.add(standardUser);

        User actualUser = hibernateUserDAO.getByEmail("scarface@miami.com");
        Assert.assertEquals(standardUser, actualUser);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateUserDAO#getByEmail(String)
     */
    @Test
    public void getUser_ByWrongEmail_Fail() {
        hibernateUserDAO.add(standardUser);
        User actualUser = hibernateUserDAO.getByEmail("Sscarface@miami.com");
        Assert.assertNull(actualUser);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateUserDAO#getByNick(String)
     */
    @Test
    public void getByNick_Success() {
        hibernateUserDAO.add(standardUser);
        User actualUser = hibernateUserDAO.getByNick("scarface");
        Assert.assertEquals(standardUser, actualUser);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateUserDAO#getByNick(String)
     */
    @Test
    public void getByNick_NotExisting_Fail() {
        User actualUser = hibernateUserDAO.getByNick("scarface");
        Assert.assertNull(actualUser);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateUserDAO#authenticate(java.lang.String, java.lang.String)
     */
    @Test
    public void authenticate_Success() {
        hibernateUserDAO.add(standardUser);

        User actualUser = hibernateUserDAO.authenticate(standardUser.getEmail(), standardUser.getPass());
        Assert.assertEquals(standardUser, actualUser);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateUserDAO#authenticate(java.lang.String, java.lang.String)
     */
    @Test
    public void authenticate_WrongPass_Fail() {
        hibernateUserDAO.add(standardUser);
        User actualUser = hibernateUserDAO.authenticate(standardUser.getEmail(), "xxx");
        Assert.assertNull(actualUser);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateUserDAO#edit(am.aca.orgflix.entity.User)
     */
    @Test
    public void editUser_Email_Success() {
        user = new User("scarface", "Tony Montana", "scarface@miami.com", "elvira");
        int id = hibernateUserDAO.add(user);

        final String newEmail = "scarface@miami.com";
        user.setEmail(newEmail);
        boolean status = hibernateUserDAO.edit(user);

        String actualEmail = hibernateUserDAO.getById(id).getEmail();
        Assert.assertTrue(status);
        Assert.assertEquals(newEmail, actualEmail);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateUserDAO#edit(am.aca.orgflix.entity.User)
     */
    @Test
    public void editUser_Nick_Success() {
        user = new User("scarface", "Tony Montana", "scarface@miami.com", "elvira");
        int id = hibernateUserDAO.add(user);

        final String newNick = "scarface Tony";
        user.setNick(newNick);
        boolean status = hibernateUserDAO.edit(user);

        String actualNick = hibernateUserDAO.getById(id).getNick();
        Assert.assertTrue(status);
        Assert.assertEquals(newNick, actualNick);

    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateUserDAO#edit(am.aca.orgflix.entity.User)
     */
    @Test
    public void editUser_UserName_Success() {
        user = new User("scarface", "Tony Montana", "scarface@miami.com", "elvira");
        int id = hibernateUserDAO.add(user);

        final String newUserName = "Tony Montana Jr";
        user.setUserName(newUserName);
        boolean status = hibernateUserDAO.edit(user);

        String actualName = hibernateUserDAO.getById(id).getUserName();
        Assert.assertTrue(status);
        Assert.assertEquals(newUserName, actualName);

    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateUserDAO#edit(am.aca.orgflix.entity.User)
     */
    @Test
    public void editUser_Pass_Success() {
        user = new User("scarface", "Tony Montana", "scarface@miami.com", "elvira");
        int id = hibernateUserDAO.add(user);

        final String newPass = "elvira<3";
        user.setPass(newPass);
        boolean status = hibernateUserDAO.edit(user);

        String actualPass = hibernateUserDAO.getById(id).getPass();
        Assert.assertTrue(status);
        Assert.assertEquals(newPass, actualPass);

    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateUserDAO#remove(int)
     */
    @Test
    public void Remove_ValidUser_Success() {
        hibernateUserDAO.add(standardUser);

        boolean status = hibernateUserDAO.remove(standardUser.getId());
        User actualUser = hibernateUserDAO.getById(standardUser.getId());
        Assert.assertTrue(status);
        Assert.assertNull(actualUser);
    }

    /**
     * @see am.aca.orgflix.dao.implHibernate.HibernateUserDAO#remove(int)
     */
    @Test
    public void Remove_InvalidUser_Fail() {
        boolean status = hibernateUserDAO.remove(standardUser.getId());
        Assert.assertFalse(status);
    }
}
