package am.aca.service;

import am.aca.entity.User;

/**
 * Created by David on 5/27/2017
 */
public interface UserService {

    int addUser(User user);

    User getUser(int id);

    User getUser(String email);

    boolean editUser(User user);

}
