package am.aca.orgflix.dao.implHibernate;

import am.aca.orgflix.dao.UserDAO;
import am.aca.orgflix.entity.User;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.transaction.Transactional;

/**
 * JPA Hibernate implementation for User DAO
 */
@Component
public class HibernateUserDAO implements UserDAO {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("Orgflix_PU");
    private EntityManager em = emf.createEntityManager();

    /**
     * @see UserDAO#add(User)
     */
    @Transactional
    @Override
    public int add(User user) {
        try {
            em.persist(user);
            return user.getId();
        } catch (RuntimeException e) {
            return -1;
        }
    }

    /**
     * @see UserDAO#getById(int)
     */
    @Override
    public User getById(int id) {
        try {
            return em.find(User.class, id);
        } catch (RuntimeException e) {
            return null;
        }
    }

    /**
     * @see UserDAO#getByEmail(String)
     */
    @Override
    public User getByEmail(String email) {
        try {
            Query query = em.createQuery("SELECT user FROM USERS user where user.EMAIL = :email");
            query.setParameter("email", email);
            return (User) query.getSingleResult();
        } catch (RuntimeException e) {
            return null;
        }
    }

    /**
     * @see UserDAO#getByNick(String)
     */
    @Override
    public User getByNick(String nick) {
        try {
            Query query = em.createQuery("SELECT user FROM USERS user where user.NICK = :nick");
            query.setParameter("nick", nick);
            return (User) query.getSingleResult();
        } catch (RuntimeException e) {
            return null;
        }
    }

//    @Override

    //    @Transactional
//    public boolean edit(int id, String nick, String name, String pass, String email) {
////        checkRequiredFields(pass, nick, email);
////        if (!validateEmail(email))
////            throw new DaoException("Email Invalid");
////        if (!validatePassword(pass))
////            throw new DaoException("Password too weak, must be at least 6 characters long");
//        ////////////////////////////UNIQUE CONSTRAINT FAILURE EXCEPTION NAME FOR ERROR MSG
//        try {
//            User actualUser = em.find(User.class, id);
//            actualUser.setEmail(email);
//            actualUser.setNick(nick);
//            actualUser.setUserName(name);
//            actualUser.setPass(pass);
//
//            em.merge(actualUser);
//
//            return true;
//        } catch (RuntimeException e) {
//            return false;
//        }
//    }

    /**
     * @see UserDAO#edit(User)
     */
    @Override
    @Transactional
    public boolean edit(User user) {
        try {
            em.merge(user);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

//    @Override

    //    public boolean edit(int id, String nick, String pass, String email) {
//        try {
//            User actualUser = em.find(User.class, id);
//            actualUser.setEmail(email);
//            actualUser.setNick(nick);
//            actualUser.setPass(pass);
//
//            em.merge(actualUser);
//            return true;
//        } catch (RuntimeException e) {
//            return false;
//        }
//    }

    /**
     * @see UserDAO#remove(int)
     */
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

//    @Override

    //    public boolean remove(User user) {
//        try {
//            em.remove(user);
//            return true;
//        } catch (RuntimeException e) {
//            return false;
//        }
//    }
//    public boolean insertWatched(int userId, int filmId, boolean isPublic) {
//        List list = new List();
//        list.setFilmId(filmId);
//        list.setUserId(userId);
//        list.setPublic(isPublic);
//        list.setWatched(true);
//
//        try {
//            User user = em.find(User.class, userId);
//            user.getLists().add(list);
//            return true;
//        } catch (RuntimeException e) {
//            return false;
//        }
//    }
//
//    public boolean insertPlanned(int userId, int filmId, boolean isPublic) {
//        List list = new List();
//        list.setFilmId(filmId);
//        list.setUserId(userId);
//        list.setPublic(isPublic);
//        list.setPlanned(true);
//
//        try {
//            User user = em.find(User.class, userId);
//            user.getLists().add(list);
//            return true;
//        } catch (RuntimeException e) {
//            return false;
//        }
//    }

    /**
     * @see UserDAO#authenticate(String, String)
     */
    @Override
    public User authenticate(String email, String pass) {
        try {
            Query query = em.createQuery("SELECT user FROM USERS user where user.EMAIL = :email and user.USER_PASS = :pass");
            query.setParameter("email", email);
            query.setParameter("pass", pass);
            return (User) query.getSingleResult();      ///////NoResultException
        } catch (RuntimeException e) {
            return null;
        }
    }

}
