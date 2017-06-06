package am.aca.dao;

import am.aca.dao.impljdbc.UserDaoJdbc;
import am.aca.entity.User;

/**
 * Created by David on 6/4/2017.
 */
public class Test {
    private UserDao userDao = new UserDaoJdbc();

    public static void main(String[] args) {
        Test test = new Test();
        User user = new User("gago","Gagik Petrosyan","gagik6@gmail.com","pass");
        test.userDao.addUser(user);
        System.out.println(1);
        user.setUserName("Gago");
        test.userDao.editUser(user);
        System.out.println(2);
        User otheruser = test.userDao.getUser(user.getId());
        System.out.println(3);
        otheruser.setUserName("Other name");
        test.userDao.editUser(otheruser);
        System.out.println(4);
        User anotherUser = test.userDao.getUser(user.getEmail());
        System.out.println(5);
        anotherUser.setPass("44444");
        test.userDao.editUser(anotherUser);
        System.out.println(6);
    }
}
