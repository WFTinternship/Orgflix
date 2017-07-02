package am.aca.orgflix.service.impl;

import am.aca.orgflix.dao.UserDAO;
import am.aca.orgflix.dao.impljdbc.JdbcUserDAO;
import am.aca.orgflix.entity.User;
import am.aca.orgflix.service.ServiceException;
import am.aca.orgflix.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service layer for user related methods
 */
@Service
public class UserServiceImpl implements UserService {

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
        try {
            return userDao.add(user);
        } catch (RuntimeException e) {
            throw new ServiceException(e.getMessage());
        }
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
        User user;
        try {
            user = userDao.get(id);
        } catch (RuntimeException e) {
            throw new ServiceException(e.getMessage());
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
        User user;
        try {
            user = userDao.get(email);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return null;
        } catch (RuntimeException e) {
            throw new ServiceException(e.getMessage());
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
            throw new ServiceException(e.getMessage());
        }
        return user;
    }


    @Override
    public boolean edit(User user) {
        boolean state;
        try {
            state = userDao.edit(user);
        } catch (RuntimeException e) {
            throw new ServiceException(e.getMessage());
        }
        return state;
    }
}
