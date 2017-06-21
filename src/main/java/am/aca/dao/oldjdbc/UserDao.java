package am.aca.dao.jdbc;

import am.aca.entity.*;

/**
 * Created by David on 5/27/2017
 */
public interface UserDao {

    int addUser(User user);

    User getUser(int id);

    User getUser(String email);

    boolean editUser(User user);

}
