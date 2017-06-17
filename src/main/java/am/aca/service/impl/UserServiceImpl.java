package am.aca.service.impl;

import am.aca.dao.DaoException;
import am.aca.dao.UserDao;
import am.aca.dao.impljdbc.UserDaoJdbc;
import am.aca.entity.User;
import am.aca.service.UserService;
import org.apache.log4j.Logger;

/**
 * Created by David on 6/7/2017
 */
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = Logger.getLogger(UserServiceImpl.class.getName());
    private UserDao userDao;

    public UserServiceImpl() {
        userDao = new UserDaoJdbc();
    }

    @Override
    public int addUser(User user) {
        int id = -1;
        try {
            id = userDao.addUser(user);
        }catch (DaoException e){
            LOGGER.warn(e.getMessage());
        }
        return id;
    }

    @Override
    public User getUser(int id) {
        User user = null;
        try {
            user = userDao.getUser(id);
        }catch (DaoException e){
            LOGGER.warn(e.getMessage());
        }
        return user;
    }

    @Override
    public User getUser(String email) {
        User user = null;
        try {
            user = userDao.getUser(email);
        }catch (DaoException e){
            LOGGER.warn(e.getMessage());
        }
        return user;
    }

    @Override
    public boolean editUser(User user) {
        boolean state = false;
        try {
            state = userDao.editUser(user);
        }catch (DaoException e){
            LOGGER.warn(e.getMessage());
        }
        return state;
    }
}
