package daotest.springjdbc;

import am.aca.dao.DaoException;
import am.aca.dao.springjdbc.UserDAO;
import am.aca.dao.springjdbc.implspringjdbc.JdbcUserDAO;
import am.aca.entity.User;
import am.aca.util.TestHelper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * Created by David on 5/29/2017
 */
public class UserTest {
    ApplicationContext ctx =
            new ClassPathXmlApplicationContext("applicationContext-persistance-test.xml");

    private UserDAO userDao = ctx.getBean("JdbcUserDAO",JdbcUserDAO.class);
    private TestHelper helper = ctx.getBean("TestHelper",TestHelper.class);
    private User user;

    @Before
    public void setUp(){}

    @After
    public void end(){
        helper.emptyTable(new String[]{"users"});
        user = null;
    }

    @Test
    public void addUser_Successed(){
        user = new User("gago","Gagik Petrosyan","gagik@gmail.com","pass");
        Assert.assertEquals( userDao.add(user) , 1);
    }

    @Test
    public void addUser_Successed_EmptyUserName(){
        user = new User("gago","","gagik1@gmail.com","pass");
        Assert.assertEquals( 1, userDao.add(user) );
    }

    @Test
    public void addUser_Fail_EmptyNick(){
        user = new User("","Gagik Petrosyan","gagik2@gmail.com","pass");
        Assert.assertEquals(-1 , userDao.add(user) );
    }

    @Test
    public void addUser_Fail_EmptyEmail(){
        user = new User("gago","Gagik Petrosyan","","pass");
        Assert.assertEquals(-1, userDao.add(user) );
    }

    @Test
    public void addUser_Fail_EmptyPass(){
        user = new User("gago","Gagik Petrosyan","gagik3@gmail.com","");
        Assert.assertEquals( -1, userDao.add(user) );
    }

    @Test
    public void addUser_Fail_NullNick(){
        user = new User(null,"Gagik Petrosyan","gagik2@gmail.com","pass");
        Assert.assertEquals(-1 , userDao.add(user) );
    }

    @Test
    public void addUser_Fail_NullEmail(){
        user = new User("gago","Gagik Petrosyan",null,"pass");
        Assert.assertEquals(-1, userDao.add(user) );
    }

    @Test
    public void addUser_Fail_NullPass(){
        user = new User("gago","Gagik Petrosyan","gagik3@gmail.com",null);
        Assert.assertEquals( -1, userDao.add(user) );
    }


    @Test (expected = Exception.class)
    public void addUser_Fail_EmailAlreadyExists(){
        user = new User("gago","Gagik Petrosyan","davit.abovyan@gmail.com","pass");
        userDao.add(user);
        User user1 = new User("armen","Armen Hakobyan","davit.abovyan@gmail.com","pass1");
        int id = userDao.add(user1);
        Assert.assertEquals( 0, id );
    }

    @Test
    public void getUser_Succed_ById(){
        user = new User("gago","Gagik Petrosyan","davit.abovyan@gmail.com","pass");
        int id = userDao.add(user);
        User gotUser = userDao.get(id);
        Assert.assertTrue(user.equals(gotUser));
    }

    @Test
    public void getUser_Fail_ByWrongId(){
        user = new User("gago","Gagik Petrosyan","davit.abovyan@gmail.com","pass");
        userDao.add(user);
        User user1 = new User("gago","Gagik Petrosyan","davit.abovyan1@gmail.com","pass");
        int id = userDao.add(user1);
        Assert.assertFalse( user.equals( userDao.get(id) ) );
    }

    @Test
    public void getUser_Succed_ByEmail(){
        String email = "davit.abovyan@gmail.com";
        user = new User("gago","Gagik Petrosyan",email,"pass");
        userDao.add(user);
        Assert.assertTrue( user.equals( userDao.get(email) ) );
    }

    @Test
    public void editUser_Succed_Email(){
        user = new User("gago","Gagik Petrosyan","davit.abovyan@gmail.com","pass");
        userDao.add(user);
        final String newEmail = "gagik@gmail.com";
        user.setEmail(newEmail);
        if( userDao.edit(user) ){
            Assert.assertEquals(user.getEmail(),userDao.get(newEmail).getEmail());
        }
    }

    @Test
    public void editUser_Succed_Nick(){
        user = new User("gago","Gagik Petrosyan","davit.abovyan@gmail.com","pass");
        int id = userDao.add(user);
        final String newNick = "davo";
        user.setNick(newNick);
        if( userDao.edit(user) ){
            Assert.assertEquals(user.getNick(),userDao.get(id).getNick());
        }
    }

    @Test
    public void editUser_Succed_UserName(){
        user = new User("gago","Gagik Petrosyan","davit.abovyan@gmail.com","pass");
        int id = userDao.add(user);
        final String newUserName = "Davit Abovyan";
        user.setUserName(newUserName);
        if( userDao.edit(user) ){
            Assert.assertEquals(user.getUserName(),userDao.get(id).getUserName());
        }
    }

    @Test
    public void editUser_Succed_Pass(){
        user = new User("gago","Gagik Petrosyan","davit.abovyan@gmail.com","pass");
        int id = userDao.add(user);
        final String newPass = "password";
        user.setPass(newPass);
        if( userDao.edit(user) ){
            Assert.assertEquals(user.getPass(),userDao.get(id).getPass());
        }
    }

    @Test (expected = DaoException.class)
    public void editUser_Fail_EmailNonUnic(){
        final String email = "davit.abovyan@gmail.com";
        user = new User("davit","Davit Abvoyan",email,"pass");
        userDao.add(user);
        User otherUser = new User("gago","Gagik Petrosyan","gagik.petrosyan@gmail.com","pass");
        userDao.add(otherUser);
        otherUser.setEmail(email);
        userDao.edit(otherUser);
    }

    @Test
    public void editUser_Fail_EmailNULL(){
        user = new User("davit","Davit Abvoyan","davit.abovyan@gmail.com","pass");
        userDao.add(user);
        user.setEmail(null);
        Assert.assertFalse( userDao.edit(user) );
    }

    @Test
    public void editUser_Fail_NickNULL(){
        user = new User("davit","Davit Abvoyan","davit.abovyan@gmail.com","pass");
        userDao.add(user);
        user.setNick(null);
        Assert.assertFalse( userDao.edit(user) );
    }

    @Test
    public void editUser_Fail_PassNULL(){
        user = new User("davit","Davit Abvoyan","davit.abovyan@gmail.com","pass");
        userDao.add(user);
        user.setPass(null);
        Assert.assertFalse( userDao.edit(user) );
    }

    @Test
    public void editUser_Fail_EmailEmpty(){
        user = new User("davit","Davit Abvoyan","davit.abovyan@gmail.com","pass");
        userDao.add(user);
        user.setEmail("");
        Assert.assertFalse( userDao.edit(user) );
    }

    @Test
    public void editUser_Fail_NickEmpty(){
        user = new User("davit","Davit Abvoyan","davit.abovyan@gmail.com","pass");
        userDao.add(user);
        user.setNick("");
        Assert.assertFalse( userDao.edit(user) );
    }

    @Test
    public void editUser_Fail_PassEmpty(){
        user = new User("davit","Davit Abvoyan","davit.abovyan@gmail.com","pass");
        userDao.add(user);
        user.setPass("");
        Assert.assertFalse( userDao.edit(user) );
    }
}
