package am.aca.orgflix.dao;

import am.aca.orgflix.entity.User;

/**
 * Interface for user DAO
 */
public interface UserDAO {
    // Create
    int add(User user);

    //Read
    User get(int id);

    User get(String email);

    User authenticate(String email, String pass);

    //Update
    boolean edit(User user);

    boolean edit(int id, String nick, String name, String pass, String email);

    boolean edit(int id, String nick, String pass, String email);

    //Delete
    boolean remove(User user);

    boolean remove(int id);

}
