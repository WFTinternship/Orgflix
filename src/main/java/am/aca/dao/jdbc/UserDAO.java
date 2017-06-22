package am.aca.dao.jdbc;

import am.aca.entity.User;

/**
 * Created by David on 6/20/2017
 */
public interface UserDAO {
    // Create
    int add(User user);
    int add(String nick, String name, String pass, String email);
    int add(String nick, String pass, String email);

    //Read
    User get(int id);
    User get(String email);

    //Update
    boolean edit(User user);
    boolean edit(int id, String nick, String name, String pass, String email);
    boolean edit(int id, String nick, String pass, String email);

    //Delete
    boolean remove(User user);
    boolean remove(int id);

}
