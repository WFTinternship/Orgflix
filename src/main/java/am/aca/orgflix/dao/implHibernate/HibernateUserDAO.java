package am.aca.orgflix.dao.implHibernate;

import am.aca.orgflix.dao.DaoException;
import am.aca.orgflix.dao.UserDAO;
import am.aca.orgflix.entity.User;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 * JPA Hibernate implementation for User DAO
 */
public class HibernateUserDAO extends HibernateBaseDAO implements UserDAO {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("Orgflix_PU");
    private EntityManager em = emf.createEntityManager();

    @Transactional
    @Override
    public int add(User user) {
        checkRequiredFields(user.getPass(), user.getNick(), user.getEmail());
        if (!validateEmail(user.getEmail()))
            throw new DaoException("Email Invalid");
        if (!validatePassword(user.getPass()))
            throw new DaoException("Password too weak, must be at least 6 characters long");
        //////////////////////////////UNIQUE CONSTRAINT FAILURE EXCEPTION NAME FOR ERROR MSG

        try {
            em.persist(user);
            return user.getId();
        } catch (RuntimeException e) {
            return -1;
        }
    }

    @Override
    public User get(int id) {
        try {
            User user = em.find(User.class, id);
            if (user == null)
                return null;
            return user;
        } catch (RuntimeException e) {
            return null;
        }
    }

    @Override
    public User get(String email) {
        try {
            Query query = em.createQuery("SELECT c FROM USERS c where c.EMAIL = ?1");
            query.setParameter(1, email);
            return (User) query.getSingleResult();
        } catch (RuntimeException e) {
            return null;
        }
    }

    @Override
    public User getByNick(String nick) {
        try {
            Query query = em.createQuery("SELECT c FROM USERS c where c.NICK = ?1");
            query.setParameter(1, nick);
            return (User) query.getSingleResult();
        } catch (RuntimeException e) {
            return null;
        }
    }

    @Override
    public User authenticate(String email, String pass) {
        try {
            Query query = em.createQuery("SELECT c FROM USERS c where c.EMAIL = ?1 and c.USER_PASS = ?2");
            query.setParameter(1, email);
            query.setParameter(2, pass);
            return (User) query.getSingleResult();
        } catch (RuntimeException e) {
            return null;
        }
    }

    @Override
    public boolean edit(int id, String nick, String name, String pass, String email) {
        checkRequiredFields(pass, nick, email);
        if (!validateEmail(email))
            throw new DaoException("Email Invalid");
        if (!validatePassword(pass))
            throw new DaoException("Password too weak, must be at least 6 characters long");
        //////////////////////////////UNIQUE CONSTRAINT FAILURE EXCEPTION NAME FOR ERROR MSG
        try {
            User actualUser = em.find(User.class, id);
//            actualUser.setEmail(email);
//            actualUser.setNick(nick);
//            actualUser.setUserName(name);
//            actualUser.setPass(pass);

            em.refresh(actualUser);

            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    @Override
    public boolean edit(User user) {
        checkRequiredFields(user.getPass(), user.getNick(), user.getEmail());
        if (!validateEmail(user.getEmail()))
            throw new DaoException("Email Invalid");
        if (!validatePassword(user.getPass()))
            throw new DaoException("Password too weak, must be at least 6 characters long");
        //////////////////////////////UNIQUE CONSTRAINT FAILURE EXCEPTION NAME FOR ERROR MSG

        try {
            User actualUser = em.find(User.class, user.getId());
            actualUser.setEmail(user.getEmail());
            actualUser.setNick(user.getNick());
            actualUser.setUserName(user.getUserName());
            actualUser.setPass(user.getPass());
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    @Override
    public boolean edit(int id, String nick, String pass, String email) {
        checkRequiredFields(pass, nick, email);
        if (!validateEmail(email))
            throw new DaoException("Email Invalid");
        if (!validatePassword(pass))
            throw new DaoException("Password too weak, must be at least 6 characters long");
        //////////////////////////////UNIQUE CONSTRAINT FAILURE EXCEPTION NAME FOR ERROR MSG
        try {
            User actualUser = em.find(User.class, id);
            actualUser.setEmail(email);
            actualUser.setNick(nick);
            actualUser.setPass(pass);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    @Override
    public boolean remove(int id) {
        try {
            User actualUser = em.find(User.class, id);
            em.remove(actualUser);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    @Override
    public boolean remove(User user) {
        try {
            em.remove(user);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }
}
