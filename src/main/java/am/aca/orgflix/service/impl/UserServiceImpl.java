package am.aca.orgflix.service.impl;

import am.aca.orgflix.dao.UserDAO;
import am.aca.orgflix.entity.User;
import am.aca.orgflix.service.ServiceException;
import am.aca.orgflix.service.UserService;
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
     * @see UserService#add(am.aca.orgflix.entity.User)
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
     * @see UserService#get(int)
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
     * @see UserService#get(java.lang.String)
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
     * @see UserService#authenticate(java.lang.String, java.lang.String)
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

    /**
     * @see UserService#edit(am.aca.orgflix.entity.User)
     */
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
