package daotest;

import am.aca.dao.*;
import am.aca.entity.*;
import org.junit.*;

import java.sql.SQLException;

/**
 * Created by David on 5/29/2017.
 */
public class UserTest {
    UserDao userDao = new UserDaoJdbc();

    @Before
    public void setUp() throws SQLException {
        TestHelper.emptyTable(new String[]{"lists","users"});
    }

    @After
    public void end() throws SQLException {
    }

    @Test
    public void addUser_Successed() throws SQLException{
        User user = new User("gago","Gagik Petrosyan","gagik@gmail.com","pass");
        Assert.assertNotEquals( userDao.addUser(user) , -1);
    }

    @Test
    public void addUser_Successed_EmptyUserName() throws SQLException{
        User user = new User("gago","","gagik1@gmail.com","pass");
        Assert.assertNotEquals( -1, userDao.addUser(user) );
    }

    @Test
    public void addUser_Succed_UpdaedId() throws SQLException {
        User user = new User("gago","","gagik1@gmail.com","pass");
        Assert.assertEquals(userDao.addUser(user),user.getId());
    }
    @Test
    public void addUser_Fail_EmptyNick() throws SQLException{
        User user = new User("","Gagik Petrosyan","gagik2@gmail.com","pass");
        Assert.assertEquals(-1 , userDao.addUser(user) );
    }

    @Test
    public void addUser_Fail_EmptyEmail() throws SQLException{
        User user = new User("gago","Gagik Petrosyan","","pass");
        Assert.assertEquals(-1, userDao.addUser(user) );
    }

    @Test
    public void addUser_Fail_EmptyPass() throws SQLException{
        User user = new User("gago","Gagik Petrosyan","gagik3@gmail.com","");
        Assert.assertEquals( -1, userDao.addUser(user) );
    }

    @Test(expected = SQLException.class)
    public void addUser_Fail_EmailAlreadyExists() throws SQLException{
        User user = new User("gago","Gagik Petrosyan","davit.abovyan@gmail.com","pass");
        userDao.addUser(user);
        User user1 = new User("armen","Armen Hakobyan","davit.abovyan@gmail.com","pass1");
        int id=-1;
        id = userDao.addUser(user1);
        Assert.assertEquals( -1, id );
    }

    @Test
    public void getUser_Succed_ById() throws SQLException {
        User user = new User("gago","Gagik Petrosyan","davit.abovyan@gmail.com","pass");
        int id = userDao.addUser(user);
        User gotUser = userDao.getUser(id);
        Assert.assertTrue(user.equals(gotUser));
    }

    @Test
    public void getUser_Fail_ByWrongId() throws SQLException {
        User user = new User("gago","Gagik Petrosyan","davit.abovyan@gmail.com","pass");
        userDao.addUser(user);
        User user1 = new User("gago","Gagik Petrosyan","davit.abovyan1@gmail.com","pass");
        int id = userDao.addUser(user1);
        User gotUser = userDao.getUser(id);
        Assert.assertFalse(user.equals(gotUser));
    }

    @Test
    public void getUser_Succed_ByEmail() throws SQLException {
        String email = "davit.abovyan@gmail.com";
        User user = new User("gago","Gagik Petrosyan",email,"pass");
        userDao.addUser(user);
        User gotUser = userDao.getUser(email);
        Assert.assertTrue(user.equals(gotUser));
    }

    @Test
    public void editUser_Succed_Email() throws SQLException {
        User user = new User("gago","Gagik Petrosyan","davit.abovyan@gmail.com","pass");
        int id = userDao.addUser(user);
        final String newEmail = "gagik@gmail.com";
        user.setEmail(newEmail);
        if( userDao.editUser(user) ){
            Assert.assertEquals(user.getEmail(),userDao.getUser(newEmail).getEmail());
        }
    }

    @Test
    public void editUser_Succed_Nick() throws SQLException {
        User user = new User("gago","Gagik Petrosyan","davit.abovyan@gmail.com","pass");
        int id = userDao.addUser(user);
        final String newNick = "davo";
        user.setNick(newNick);
        if( userDao.editUser(user) ){
            Assert.assertEquals(user.getNick(),new UserDaoJdbc().getUser(id).getNick());
        }
    }

    @Test
    public void editUser_Succed_UserName() throws SQLException {
        User user = new User("gago","Gagik Petrosyan","davit.abovyan@gmail.com","pass");
        int id = userDao.addUser(user);
        final String newUserName = "Davit Abovyan";
        user.setUserName(newUserName);
        if( userDao.editUser(user) ){
            Assert.assertEquals(user.getUserName(),userDao.getUser(id).getUserName());
        }
    }

    @Test
    public void editUser_Succed_Pass() throws SQLException {
        User user = new User("gago","Gagik Petrosyan","davit.abovyan@gmail.com","pass");
        int id = userDao.addUser(user);
        final String newPass = "password";
        user.setPass(newPass);
        if( userDao.editUser(user) ){
            Assert.assertEquals(user.getPass(),userDao.getUser(id).getPass());
        }
    }

    @Test(expected = SQLException.class)
    public void editUser_Fail_EmailNonUnic() throws SQLException {
        final String email = "davit.abovyan@gmail.com";
        User user = new User("davit","Davit Abvoyan",email,"pass");
        userDao.addUser(user);
        User otherUser = new User("gago","Gagik Petrosyan","gagik.petrosyan@gmail.com","pass");
        int otherUserId = userDao.addUser(otherUser);
        otherUser.setEmail(email);
        userDao.editUser(otherUser);
    }

    @Test
    public void editUser_Fail_EmailNULL() throws SQLException {
        User user = new User("davit","Davit Abvoyan","davit.abovyan@gmail.com","pass");
        int id = userDao.addUser(user);
        user.setEmail(null);
        Assert.assertFalse( userDao.editUser(user) );
    }

    @Test
    public void editUser_Fail_NickNULL() throws SQLException {
        User user = new User("davit","Davit Abvoyan","davit.abovyan@gmail.com","pass");
        int id = userDao.addUser(user);
        user.setNick(null);
        Assert.assertFalse( userDao.editUser(user) );
    }

    @Test
    public void editUser_Fail_PassNULL() throws SQLException {
        User user = new User("davit","Davit Abvoyan","davit.abovyan@gmail.com","pass");
        int id = userDao.addUser(user);
        user.setPass(null);
        Assert.assertFalse( userDao.editUser(user) );
    }

    @Test
    public void editUser_Fail_EmailEmpty() throws SQLException {
        User user = new User("davit","Davit Abvoyan","davit.abovyan@gmail.com","pass");
        int id = userDao.addUser(user);
        user.setEmail("");
        Assert.assertFalse( userDao.editUser(user) );
    }

    @Test
    public void editUser_Fail_NickEmpty() throws SQLException {
        User user = new User("davit","Davit Abvoyan","davit.abovyan@gmail.com","pass");
        int id = userDao.addUser(user);
        user.setNick("");
        Assert.assertFalse( userDao.editUser(user) );
    }

    @Test
    public void editUser_Fail_PassEmpty() throws SQLException {
        User user = new User("davit","Davit Abvoyan","davit.abovyan@gmail.com","pass");
        int id = userDao.addUser(user);
        user.setPass("");
        Assert.assertFalse( userDao.editUser(user) );
    }
}
