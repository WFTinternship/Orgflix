package am.aca.orgflix.dao.implHibernate;

import am.aca.orgflix.dao.CastDAO;
import am.aca.orgflix.entity.Cast;
import am.aca.orgflix.entity.Film;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

/**
 * JPA Hibernate implementation for Cast DAO
 */
@Component
public class HibernateCastDAO implements CastDAO {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("Orgflix_PU");
    private EntityManager em = emf.createEntityManager();

    //CREATE

    /**
     * @see CastDAO#addCast(Cast)
     */
    @Override
    @SuppressWarnings({"duplicates", "duplicate"})
    public boolean addCast(Cast cast) {
        try {
            em.getTransaction().begin();
            em.persist(cast);
            em.flush();
            em.getTransaction().commit();
            return true;
        } catch (RuntimeException e) {
            e.printStackTrace();
            em.getTransaction().rollback();
            return false;
        }
    }

    //RETRIEVE

    /**
     * @see CastDAO#listCast()
     */
    @Override
    public List<Cast> listCast() {
        return em.createQuery("from Cast", Cast.class)
                .getResultList();
    }

    /**
     * @see CastDAO#getCastById(int)
     */
    @Override
    public Cast getCastById(int castId) {
        try {
            return em.find(Cast.class, castId);
        } catch (RuntimeException e) {
            return null;
        }
    }

    //UPDATE

    /**
     * @see CastDAO#editCast(Cast)
     */
    @Override
    public boolean editCast(Cast cast) {
        try {
            em.getTransaction().begin();
            Cast actualCast = em.find(Cast.class, cast.getId());
            em.merge(actualCast);
            em.flush();
            em.getTransaction().commit();
            return true;
        } catch (RuntimeException e) {
            em.getTransaction().rollback();
            return false;
        }
    }

    /**
     * @see CastDAO#addCastToFilm(Cast, Film)
     */
    @Override
    public boolean addCastToFilm(Cast cast, Film film) {
        try {
            em.getTransaction().begin();
            film.addCast(cast);
            em.merge(film);
            em.flush();
            em.getTransaction().commit();
            return true;
        } catch (RuntimeException e) {
            em.getTransaction().rollback();
            return false;
        }
    }

    //DELETE

    /**
     * @see CastDAO#remove(Cast)
     */
    @Override
    public boolean remove(Cast cast) {
        try {
            em.getTransaction().begin();
            Cast actualCast = em.find(Cast.class, cast.getId());
            em.remove(actualCast);
            em.flush();
            em.getTransaction().commit();
            return true;
        } catch (RuntimeException e) {
            em.getTransaction().rollback();
            return false;
        }
    }
}
