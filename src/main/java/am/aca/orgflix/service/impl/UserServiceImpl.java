package am.aca.orgflix.service.impl;

import am.aca.orgflix.dao.DaoException;
import am.aca.orgflix.dao.UserDAO;
import am.aca.orgflix.dao.impljdbc.JdbcUserDAO;
import am.aca.orgflix.entity.User;
import am.aca.orgflix.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service layer for user related methods
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = Logger.getLogger(UserServiceImpl.class.getName());

    @Autowired
    private UserDAO userDao;

    @Autowired
    public void setUserDao(UserDAO userDao) {
        this.userDao = userDao;
    }

    /**
     * @param user the user object to be added to DB
     * @return id of added new user in DB
     * @see JdbcUserDAO#add(am.aca.orgflix.entity.User)
     * <p>
     * Add new user to DB
     */
    @Override
    public int add(User user) {
        int id = -1;
        try {
            return userDao.add(user);
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
//            throw new DaoException(e.getMessage());
        }
        return id;
    }

    /**
     * @param id the id of the user in DB
     * @return user object with the matched id
     * @see JdbcUserDAO#get(int)
     * <p>
     * Return user by user Id
     */
    @Override
    public User get(int id) {
        User user = null;
        try {
            user = userDao.get(id);
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
//            throw new DaoException(e.getMessage());
        }
        return user;
    }

    /**
     * @param email the email of the user in DB
     * @return user object with the matched id
     * @see JdbcUserDAO#get(java.lang.String)
     * <p>
     * Return user by user user's email
     */
    @Override
    public User get(String email) {
        User user = null;
        try {
            user = userDao.get(email);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return null;
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
//            throw new DaoException(e.getMessage());
        }
        return user;
    }

    /**
     * @param email the email of the user in DB
     * @param pass  the password of the user in DB
     * @return user object with the matched details, otherwise null
     * @see JdbcUserDAO#authenticate(java.lang.String, java.lang.String)
     * <p>
     * Return user authenticated by user's email and password
     */
    @Override
    public User authenticate(String email, String pass) {
        User user;
        try {
            user = userDao.authenticate(email, pass);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return null;
        } catch (RuntimeException e) {
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
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
//            throw new DaoException(e.getMessage());
        }
        return state;
    }
}
