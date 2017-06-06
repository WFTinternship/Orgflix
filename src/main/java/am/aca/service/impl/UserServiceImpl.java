package am.aca.service.impl;

import am.aca.dao.UserDao;
import am.aca.dao.impljdbc.UserDaoJdbc;
import am.aca.entity.User;
import am.aca.service.UserService;

/**
 * Created by David on 6/7/2017
 */
public class UserServiceImpl implements UserService {
    private UserDao userDao;

    public UserServiceImpl() {
        userDao = new UserDaoJdbc();
    }

    @Override
    public int addUser(User user) {
        return userDao.addUser(user);
    }

    @Override
    public User getUser(int id) {
        return userDao.getUser(id);
    }

    @Override
    public User getUser(String email) {
        return userDao.getUser(email);
    }

    @Override
    public boolean editUser(User user) {
        return userDao.editUser(user);
    }
}
