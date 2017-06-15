package daotest;

import am.aca.dao.*;
import am.aca.dao.impljdbc.UserDaoJdbc;
import am.aca.entity.*;
import am.aca.util.ConnType;
import am.aca.util.DbManager;
import org.junit.*;
import org.junit.Test;


/**
 * Created by David on 5/29/2017
 */
public class UserTest {
    private UserDao userDao = new UserDaoJdbc(ConnType.TEST);
    private User user;

    @Before
    public void setUp(){}

    @After
    public void end(){
        DbManager.emptyTestTables(new String[]{"lists","users"});
        user = null;
    }

    @Test
    public void addUser_Successed(){
        user = new User("gago","Gagik Petrosyan","gagik@gmail.com","pass");
        Assert.assertNotEquals( userDao.addUser(user) , -1);
    }

    @Test
    public void addUser_Successed_EmptyUserName(){
        user = new User("gago","","gagik1@gmail.com","pass");
        Assert.assertNotEquals( -1, userDao.addUser(user) );
    }

    @Test
    public void addUser_Succed_UpdatedId(){
        user = new User("gago","","gagik1@gmail.com","pass");
        Assert.assertEquals(userDao.addUser(user),user.getId());
    }
    @Test
    public void addUser_Fail_EmptyNick(){
        user = new User("","Gagik Petrosyan","gagik2@gmail.com","pass");
        Assert.assertEquals(-1 , userDao.addUser(user) );
    }

    @Test
    public void addUser_Fail_EmptyEmail(){
        user = new User("gago","Gagik Petrosyan","","pass");
        Assert.assertEquals(-1, userDao.addUser(user) );
    }

    @Test
    public void addUser_Fail_EmptyPass(){
        user = new User("gago","Gagik Petrosyan","gagik3@gmail.com","");
        Assert.assertEquals( -1, userDao.addUser(user) );
    }

    @Test (expected = DaoException.class)
    public void addUser_Fail_EmailAlreadyExists(){
        user = new User("gago","Gagik Petrosyan","davit.abovyan@gmail.com","pass");
        userDao.addUser(user);
        User user1 = new User("armen","Armen Hakobyan","davit.abovyan@gmail.com","pass1");
        int id = userDao.addUser(user1);
        Assert.assertEquals( -1, id );
    }

    @Test
    public void getUser_Succed_ById(){
        user = new User("gago","Gagik Petrosyan","davit.abovyan@gmail.com","pass");
        int id = userDao.addUser(user);
        User gotUser = userDao.getUser(id);
        Assert.assertTrue(user.equals(gotUser));
    }

    @Test
    public void getUser_Fail_ByWrongId(){
        user = new User("gago","Gagik Petrosyan","davit.abovyan@gmail.com","pass");
        userDao.addUser(user);
        User user1 = new User("gago","Gagik Petrosyan","davit.abovyan1@gmail.com","pass");
        int id = userDao.addUser(user1);
        Assert.assertFalse( user.equals( userDao.getUser(id) ) );
    }

    @Test
    public void getUser_Succed_ByEmail(){
        String email = "davit.abovyan@gmail.com";
        user = new User("gago","Gagik Petrosyan",email,"pass");
        userDao.addUser(user);
        Assert.assertTrue( user.equals( userDao.getUser(email) ) );
    }

    @Test
    public void editUser_Succed_Email(){
        user = new User("gago","Gagik Petrosyan","davit.abovyan@gmail.com","pass");
        userDao.addUser(user);
        final String newEmail = "gagik@gmail.com";
        user.setEmail(newEmail);
        if( userDao.editUser(user) ){
            Assert.assertEquals(user.getEmail(),userDao.getUser(newEmail).getEmail());
        }
    }

    @Test
    public void editUser_Succed_Nick(){
        user = new User("gago","Gagik Petrosyan","davit.abovyan@gmail.com","pass");
        int id = userDao.addUser(user);
        final String newNick = "davo";
        user.setNick(newNick);
        if( userDao.editUser(user) ){
            Assert.assertEquals(user.getNick(),userDao.getUser(id).getNick());
        }
    }

    @Test
    public void editUser_Succed_UserName(){
        user = new User("gago","Gagik Petrosyan","davit.abovyan@gmail.com","pass");
        int id = userDao.addUser(user);
        final String newUserName = "Davit Abovyan";
        user.setUserName(newUserName);
        if( userDao.editUser(user) ){
            Assert.assertEquals(user.getUserName(),userDao.getUser(id).getUserName());
        }
    }

    @Test
    public void editUser_Succed_Pass(){
        user = new User("gago","Gagik Petrosyan","davit.abovyan@gmail.com","pass");
        int id = userDao.addUser(user);
        final String newPass = "password";
        user.setPass(newPass);
        if( userDao.editUser(user) ){
            Assert.assertEquals(user.getPass(),userDao.getUser(id).getPass());
        }
    }

    @Test (expected = DaoException.class)
    public void editUser_Fail_EmailNonUnic(){
        final String email = "davit.abovyan@gmail.com";
        user = new User("davit","Davit Abvoyan",email,"pass");
        userDao.addUser(user);
        User otherUser = new User("gago","Gagik Petrosyan","gagik.petrosyan@gmail.com","pass");
        userDao.addUser(otherUser);
        otherUser.setEmail(email);
        userDao.editUser(otherUser);
    }

    @Test
    public void editUser_Fail_EmailNULL(){
        user = new User("davit","Davit Abvoyan","davit.abovyan@gmail.com","pass");
        userDao.addUser(user);
        user.setEmail(null);
        Assert.assertFalse( userDao.editUser(user) );
    }

    @Test
    public void editUser_Fail_NickNULL(){
        user = new User("davit","Davit Abvoyan","davit.abovyan@gmail.com","pass");
        userDao.addUser(user);
        user.setNick(null);
        Assert.assertFalse( userDao.editUser(user) );
    }

    @Test
    public void editUser_Fail_PassNULL(){
        user = new User("davit","Davit Abvoyan","davit.abovyan@gmail.com","pass");
        userDao.addUser(user);
        user.setPass(null);
        Assert.assertFalse( userDao.editUser(user) );
    }

    @Test
    public void editUser_Fail_EmailEmpty(){
        user = new User("davit","Davit Abvoyan","davit.abovyan@gmail.com","pass");
        userDao.addUser(user);
        user.setEmail("");
        Assert.assertFalse( userDao.editUser(user) );
    }

    @Test
    public void editUser_Fail_NickEmpty(){
        user = new User("davit","Davit Abvoyan","davit.abovyan@gmail.com","pass");
        userDao.addUser(user);
        user.setNick("");
        Assert.assertFalse( userDao.editUser(user) );
    }

    @Test
    public void editUser_Fail_PassEmpty(){
        user = new User("davit","Davit Abvoyan","davit.abovyan@gmail.com","pass");
        userDao.addUser(user);
        user.setPass("");
        Assert.assertFalse( userDao.editUser(user) );
    }
}
