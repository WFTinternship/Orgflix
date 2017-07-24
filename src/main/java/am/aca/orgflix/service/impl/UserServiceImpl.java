package am.aca.orgflix.service.impl;

import am.aca.orgflix.dao.UserDAO;
import am.aca.orgflix.entity.User;
import am.aca.orgflix.exception.ServiceException;
import am.aca.orgflix.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service layer for user related methods
 */
@Service
public class UserServiceImpl extends BaseService implements UserService {

    private UserDAO userDao;

    public UserServiceImpl() {
        // class name to include in logging
        super(UserServiceImpl.class);
    }

    @Autowired
    public void setUserDao(UserDAO userDao) {
        this.userDao = userDao;
    }

    /**
     * @see UserService#add(am.aca.orgflix.entity.User)
     */
    @Override
    public int add(User user) {
        checkRequiredFields(user.getNick(), user.getEmail(), user.getPass());
        validateEmail(user.getEmail());
        validatePassword(user.getPass());

        if (userDao.emailIsUsed(user.getEmail()))
            throw new ServiceException("Email already used");
        if (userDao.nickIsUsed(user.getNick()))
            throw new ServiceException("Nickname already used");

        try {
            return userDao.add(user);
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            throw new ServiceException("Adding user failed");
        }
    }

    /**
     * @see UserService#getById(int)
     */
    @Override
    public User getById(int id) {
        User user;
        try {
            user = userDao.getById(id);
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            throw new ServiceException("No user with such ID");
        }
        return user;
    }

    /**
     * @see UserService#getAll()
     */
    @Override
    public List<User> getAll() {
        List<User> list;
        try {
            list = userDao.getAll();
            if(list.size() == 0) throw new ServiceException("There is no registered user");
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            return new ArrayList<>();
        }
        return list;
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
            throw new ServiceException("Request for user by email failed");
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
            throw new ServiceException("Request for user by nick failed");
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
            if (userDao.authenticate(email, pass))
                user = userDao.getByEmail(email);
            else
                user = null;
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            throw new ServiceException("Authentication failed!");
        }
        return user;
    }

    /**
     * @see UserService#edit(am.aca.orgflix.entity.User)
     */
    @Override
    public boolean edit(User user) {
        try {
            userDao.reset(user.getId());
        } catch (RuntimeException e) {
            return false;
        }
        checkRequiredFields(user.getNick(), user.getEmail(), user.getPass());
        validateEmail(user.getEmail());
        validatePassword(user.getPass());

        if (userDao.emailIsUsed(user.getEmail()))
            throw new ServiceException("Email already used");
        if (userDao.nickIsUsed(user.getNick()))
            throw new ServiceException("Nickname already used");
        boolean state;
        try {
            state = userDao.edit(user);
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            throw new ServiceException("Fail to edit user");
        }
        return state;
    }
}
