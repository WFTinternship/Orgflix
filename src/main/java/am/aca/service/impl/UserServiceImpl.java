package am.aca.service.impl;

import am.aca.dao.DaoException;
import am.aca.dao.jdbc.impljdbc.JdbcCastDAO;
import am.aca.dao.jdbc.impljdbc.JdbcUserDAO;
import am.aca.dao.jdbc.UserDAO;
import am.aca.dao.jdbc.impljdbc.JdbcUserDAO;
import am.aca.entity.User;
import am.aca.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

/**
 * Created by David on 6/7/2017
 */
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = Logger.getLogger(UserServiceImpl.class.getName());
    private ApplicationContext ctx = null;

    private UserDAO userDao;

    public UserServiceImpl(ApplicationContext ctx) {
        this.ctx = ctx;
        userDao = ctx.getBean("JdbcUserDAO", JdbcUserDAO.class);
    }

    @Override
    public int addUser(User user) {
        int id = -1;
        try {
            id = userDao.add(user);
        }catch (DaoException e){
            LOGGER.warn(e.getMessage());
        }
        return id;
    }

    @Override
    public User getUser(int id) {
        User user = null;
        try {
            user = userDao.get(id);
        }catch (DaoException e){
            LOGGER.warn(e.getMessage());
        }
        return user;
    }

    @Override
    public User getUser(String email) {
        User user = null;
        try {
            user = userDao.get(email);
            System.out.println(user);
        }catch (DaoException e){
            LOGGER.warn(e.getMessage());
        }
        return user;
    }

    @Override
    public boolean editUser(User user) {
        boolean state = false;
        try {
            state = userDao.edit(user);
        }catch (DaoException e){
            LOGGER.warn(e.getMessage());
        }
        return state;
    }
}
