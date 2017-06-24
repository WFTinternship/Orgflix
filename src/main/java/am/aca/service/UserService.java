package am.aca.service;

import am.aca.entity.User;

/**
 * Interface for user service layer
 */
public interface UserService {

    int add(User user);

    User get(int id);

    User get(String email);

    boolean edit(User user);

}
