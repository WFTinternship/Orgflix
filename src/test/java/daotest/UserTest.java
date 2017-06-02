package daotest;

import am.aca.dao.*;
import am.aca.entity.*;
import org.junit.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by David on 5/29/2017.
 */
public class UserTest {
    Connection connection;
    UserDao userDao = new UserDaoJdbc();

    @Before
    public void setUp() throws SQLException {
        getConnection();
        TestHelper.emptyTable(new String[]{"lists","users"},connection);
    }

    @After
    public void end() throws SQLException {
        connection.close();
        connection = null;
    }

    @Test
    public void addUser_Successed() throws SQLException{
        User user = new User("gago","Gagik Petrosyan","gagik@gmail.com","pass");
        Assert.assertNotEquals( userDao.addUser(user,connection) , -1);
    }

    @Test
    public void addUser_Successed_EmptyUserName() throws SQLException{
        User user = new User("gago","","gagik1@gmail.com","pass");
        Assert.assertNotEquals( -1, userDao.addUser(user,connection) );
    }

    @Test
    public void addUser_Succed_UpdaedId() throws SQLException {
        User user = new User("gago","","gagik1@gmail.com","pass");
        Assert.assertEquals(userDao.addUser(user,connection),user.getId());
    }
    @Test
    public void addUser_Fail_EmptyNick() throws SQLException{
        User user = new User("","Gagik Petrosyan","gagik2@gmail.com","pass");
        Assert.assertEquals(-1 , userDao.addUser(user,connection) );
    }

    @Test
    public void addUser_Fail_EmptyEmail() throws SQLException{
        User user = new User("gago","Gagik Petrosyan","","pass");
        Assert.assertEquals(-1, userDao.addUser(user,connection) );
    }

    @Test
    public void addUser_Fail_EmptyPass() throws SQLException{
        User user = new User("gago","Gagik Petrosyan","gagik3@gmail.com","");
        Assert.assertEquals( -1, userDao.addUser(user,connection) );
    }

    @Test(expected = SQLException.class)
    public void addUser_Fail_EmailAlreadyExists() throws SQLException{
        User user = new User("gago","Gagik Petrosyan","davit.abovyan@gmail.com","pass");
        userDao.addUser(user,connection);
        User user1 = new User("armen","Armen Hakobyan","davit.abovyan@gmail.com","pass1");
        int id=-1;
        id = userDao.addUser(user1,connection);
        Assert.assertEquals( -1, id );
    }

    @Test
    public void getUser_Succed_ById() throws SQLException {
        User user = new User("gago","Gagik Petrosyan","davit.abovyan@gmail.com","pass");
        int id = userDao.addUser(user,connection);
        User gotUser = userDao.getUser(id,connection);
        Assert.assertTrue(user.equals(gotUser));
    }

    @Test
    public void getUser_Fail_ByWrongId() throws SQLException {
        User user = new User("gago","Gagik Petrosyan","davit.abovyan@gmail.com","pass");
        userDao.addUser(user,connection);
        User user1 = new User("gago","Gagik Petrosyan","davit.abovyan1@gmail.com","pass");
        int id = userDao.addUser(user1,connection);
        User gotUser = userDao.getUser(id,connection);
        Assert.assertFalse(user.equals(gotUser));
    }

    @Test
    public void getUser_Succed_ByEmail() throws SQLException {
        String email = "davit.abovyan@gmail.com";
        User user = new User("gago","Gagik Petrosyan",email,"pass");
        userDao.addUser(user,connection);
        User gotUser = userDao.getUser(email,connection);
        Assert.assertTrue(user.equals(gotUser));
    }

    @Test
    public void editUser_Succed_Email() throws SQLException {
        User user = new User("gago","Gagik Petrosyan","davit.abovyan@gmail.com","pass");
        int id = userDao.addUser(user,connection);
        final String newEmail = "gagik@gmail.com";
        user.setEmail(newEmail);
        if( userDao.editUser(user,connection) ){
            Assert.assertEquals(user.getEmail(),userDao.getUser(newEmail,connection).getEmail());
        }
    }

    @Test
    public void editUser_Succed_Nick() throws SQLException {
        User user = new User("gago","Gagik Petrosyan","davit.abovyan@gmail.com","pass");
        int id = userDao.addUser(user,connection);
        final String newNick = "davo";
        user.setNick(newNick);
        if( userDao.editUser(user,connection) ){
            Assert.assertEquals(user.getNick(),new UserDaoJdbc().getUser(id,connection).getNick());
        }
    }

    @Test
    public void editUser_Succed_UserName() throws SQLException {
        User user = new User("gago","Gagik Petrosyan","davit.abovyan@gmail.com","pass");
        int id = userDao.addUser(user,connection);
        final String newUserName = "Davit Abovyan";
        user.setUserName(newUserName);
        if( userDao.editUser(user,connection) ){
            Assert.assertEquals(user.getUserName(),userDao.getUser(id,connection).getUserName());
        }
    }

    @Test
    public void editUser_Succed_Pass() throws SQLException {
        User user = new User("gago","Gagik Petrosyan","davit.abovyan@gmail.com","pass");
        int id = userDao.addUser(user,connection);
        final String newPass = "password";
        user.setPass(newPass);
        if( userDao.editUser(user,connection) ){
            Assert.assertEquals(user.getPass(),userDao.getUser(id,connection).getPass());
        }
    }

    @Test(expected = SQLException.class)
    public void editUser_Fail_EmailNonUnic() throws SQLException {
        final String email = "davit.abovyan@gmail.com";
        User user = new User("davit","Davit Abvoyan",email,"pass");
        userDao.addUser(user,connection);
        User otherUser = new User("gago","Gagik Petrosyan","gagik.petrosyan@gmail.com","pass");
        int otherUserId = userDao.addUser(otherUser,connection);
        otherUser.setEmail(email);
        userDao.editUser(otherUser,connection);
    }

    @Test
    public void editUser_Fail_EmailNULL() throws SQLException {
        User user = new User("davit","Davit Abvoyan","davit.abovyan@gmail.com","pass");
        int id = userDao.addUser(user,connection);
        user.setEmail(null);
        Assert.assertFalse( userDao.editUser(user,connection) );
    }

    @Test
    public void editUser_Fail_NickNULL() throws SQLException {
        User user = new User("davit","Davit Abvoyan","davit.abovyan@gmail.com","pass");
        int id = userDao.addUser(user,connection);
        user.setNick(null);
        Assert.assertFalse( userDao.editUser(user,connection) );
    }

    @Test
    public void editUser_Fail_PassNULL() throws SQLException {
        User user = new User("davit","Davit Abvoyan","davit.abovyan@gmail.com","pass");
        int id = userDao.addUser(user,connection);
        user.setPass(null);
        Assert.assertFalse( userDao.editUser(user,connection) );
    }

    @Test
    public void editUser_Fail_EmailEmpty() throws SQLException {
        User user = new User("davit","Davit Abvoyan","davit.abovyan@gmail.com","pass");
        int id = userDao.addUser(user,connection);
        user.setEmail("");
        Assert.assertFalse( userDao.editUser(user,connection) );
    }

    @Test
    public void editUser_Fail_NickEmpty() throws SQLException {
        User user = new User("davit","Davit Abvoyan","davit.abovyan@gmail.com","pass");
        int id = userDao.addUser(user,connection);
        user.setNick("");
        Assert.assertFalse( userDao.editUser(user,connection) );
    }

    @Test
    public void editUser_Fail_PassEmpty() throws SQLException {
        User user = new User("davit","Davit Abvoyan","davit.abovyan@gmail.com","pass");
        int id = userDao.addUser(user,connection);
        user.setPass("");
        Assert.assertFalse( userDao.editUser(user,connection) );
    }

    private Connection getConnection(){
        if( connection == null) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/project", "root", "");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return connection;
    }
}
