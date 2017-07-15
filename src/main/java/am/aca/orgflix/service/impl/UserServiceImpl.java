package am.aca.orgflix.service.impl;

import am.aca.orgflix.dao.UserDAO;
import am.aca.orgflix.entity.User;
import am.aca.orgflix.service.BaseServiceImpl;
import am.aca.orgflix.service.ServiceException;
import am.aca.orgflix.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service layer for user related methods
 */
@Service
public class UserServiceImpl extends BaseServiceImpl implements UserService {

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
        checkRequiredFields(user.getNick());
        checkRequiredFields(user.getEmail());
        checkRequiredFields(user.getPass());
        validateEmail(user.getEmail());
        validatePassword(user.getPass());

        if (userDao.getByEmail(user.getEmail()) != null)
            throw new ServiceException("Email already used");
        if (userDao.getByNick(user.getNick()) != null)
            throw new ServiceException("Nickname already used");
        try {
            return userDao.add(user);
        } catch (RuntimeException e) {
            return -1;
        }
    }

    /**
     * @see UserService#get(int)
     */
    @Override
    public User get(int id) {
        User user = new User();
        try {
            user = userDao.getById(id);
        } catch (RuntimeException ignored) {
        }
        return user;
    }

    /**
     * @see UserService#get(java.lang.String)
     */
    @Override
    public User get(String email) {
        User user = new User();
        try {
            user = userDao.getByEmail(email);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return null;
        } catch (RuntimeException ignored) {
        }
        return user;
    }

    /**
     * @see UserService#get(String)
     */
    @Override
    public User getByNick(String nick) {
        User user = new User();
        try {
            user = userDao.getByNick(nick);
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return null;
        } catch (RuntimeException ignored) {
        }
        return user;
    }

    /**
     * @see UserService#authenticate(java.lang.String, java.lang.String)
     */
    @Override
    public User authenticate(String email, String pass) {
        User user = null;
        try {
            user = userDao.authenticate(email, pass);
        } catch (RuntimeException ignored) {
        }
        return user;
    }

    /**
     * @see UserService#edit(am.aca.orgflix.entity.User)
     */
    @Override
    public boolean edit(User user) {
        checkRequiredFields(user.getNick());
        checkRequiredFields(user.getEmail());
        checkRequiredFields(user.getPass());
        validateEmail(user.getEmail());
        validatePassword(user.getPass());

        if (userDao.getByEmail(user.getEmail()) != null)
            throw new ServiceException("Email already used");
        if (userDao.getByNick(user.getNick()) != null)
            throw new ServiceException("Nickname already used");

        boolean state = false;
        try {
            state = userDao.edit(user);
        } catch (RuntimeException ignored) {
        }
        return state;
    }
}
