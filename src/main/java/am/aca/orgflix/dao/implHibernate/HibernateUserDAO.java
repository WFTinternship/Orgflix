package am.aca.orgflix.dao.implHibernate;

import am.aca.orgflix.dao.UserDAO;
import am.aca.orgflix.entity.User;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Objects;

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
    @Override
    public int add(User user) {
        try {
            em.getTransaction().begin();
            em.persist(user);
            em.flush();
            em.getTransaction().commit();
            return user.getId();
        } catch (RuntimeException e) {
            em.getTransaction().rollback();
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
            List<User> users = em.createQuery("from User", User.class).getResultList();
            for (User user : users) {
                if (Objects.equals(user.getEmail(), email))
                    return user;
            }
            return null;
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
            List<User> users = em.createQuery("from User", User.class).getResultList();
            for (User user : users) {
                if (Objects.equals(user.getNick(), nick))
                    return user;
            }
            return null;
        } catch (RuntimeException e) {
            return null;
        }
    }


    /**
     * @see UserDAO#edit(User)
     */
    @Override
    public boolean edit(User user) {
        try {
            em.getTransaction().begin();
            em.merge(user);
            em.flush();
            em.getTransaction().commit();
            return true;
        } catch (RuntimeException e) {
            em.getTransaction().rollback();
            return false;
        }
    }

    /**
     * @see UserDAO#remove(int)
     */
    @Override
    public boolean remove(int id) {
        try {
            em.getTransaction().begin();
            User actualUser = em.find(User.class, id);
            em.remove(actualUser);
            em.flush();
            em.getTransaction().commit();
            return true;
        } catch (RuntimeException e) {
            em.getTransaction().rollback();
            return false;
        }
    }

    /**
     * @see UserDAO#authenticate(String, String)
     */
    @Override
    public User authenticate(String email, String pass) {
        try {
            List<User> users = em.createQuery("from User", User.class).getResultList();
            for (User user : users) {
                if ((user.getEmail().equals(email)) && user.getPass().equals(pass))
                    return user;
            }
            return null;
        } catch (RuntimeException e) {
            return null;
        }
    }

}
