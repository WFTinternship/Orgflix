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
public class UserServiceImpl extends BaseService implements UserService {

    private UserDAO userDao;

    @Autowired
    public void setUserDao(UserDAO userDao) {
        this.userDao = userDao;
    }

    public UserServiceImpl() {
        // class name to include in logging
        super(UserServiceImpl.class);
    }

    /**
     * @see UserService#add(am.aca.orgflix.entity.User)
     */
    @Override
    public int add(User user) {
        checkRequiredFields(user.getNick(), user.getEmail(), user.getPass());
        validateEmail(user.getEmail());
        validatePassword(user.getPass());

        try {
            if (authenticate(user.getEmail(), user.getPass()) == null)
                return -1;
            return userDao.add(user);
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * @see UserService#getById(int)
     */
    @Override
    public User getById(int id) {
        User user;
        try {
            user = userDao.get(id);
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return user;
    }

    /**
     * @see UserService#getByEmail(java.lang.String)
     */
    @Override
    public User getByEmail(String email) {
        User user;
        try {
            user = userDao.getByEmail(email);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return null;
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return user;
    }

    /**
     * @see UserService#getByEmail(String)
     */
    @Override
    public User getByNick(String nick) {
        User user;
        try {
            user = userDao.getByNick(nick);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return null;
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
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
            LOGGER.warn(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return user;
    }

    /**
     * @see UserService#edit(am.aca.orgflix.entity.User)
     */
    @Override
    public boolean edit(User user) {
        checkRequiredFields(user.getNick(), user.getEmail(), user.getPass());
        validateEmail(user.getEmail());
        validatePassword(user.getPass());

        boolean state;
        try {
            state = userDao.edit(user);
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        return state;
    }
}
