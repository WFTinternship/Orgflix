package am.aca.service;

import am.aca.entity.User;

/**
 * Interface for user service layer
 */
public interface UserService {

    int addUser(User user);

    User getUser(int id);

    User getUser(String email);

    boolean editUser(User user);

}
