package am.aca.service.impl;

import am.aca.dao.DaoException;
import am.aca.dao.jdbc.UserDAO;
import am.aca.dao.jdbc.impljdbc.JdbcUserDAO;
import am.aca.entity.User;
import am.aca.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * Service layer for user related methods
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = Logger.getLogger(UserServiceImpl.class.getName());

    private UserDAO userDao;

    @Autowired
    public UserServiceImpl(ApplicationContext ctx) {
        userDao = ctx.getBean("jdbcUserDAO", JdbcUserDAO.class);
    }

    @Override
    public int add(User user) {
        int id = -1;
        try {
            id = userDao.add(user);
        }catch (RuntimeException e){
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        }
        return id;
    }

    @Override
    public User get(int id) {
        User user = null;
        try {
            user = userDao.get(id);
        }catch (RuntimeException e){
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        }
        return user;
    }

    @Override
    public User get(String email) {
        User user = null;
        try {
            user = userDao.get(email);
            System.out.println(user);
        }catch (RuntimeException e){
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        }
        return user;
    }

    @Override
    public boolean edit(User user) {
        boolean state = false;
        try {
            state = userDao.edit(user);
        }catch (RuntimeException e){
            LOGGER.warn(e.getMessage());
            throw new DaoException(e.getMessage());
        }
        return state;
    }
}
