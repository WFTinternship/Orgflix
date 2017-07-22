package am.aca.orgflix.dao.implHibernate;

import am.aca.orgflix.dao.CastDAO;
import am.aca.orgflix.entity.Cast;
import am.aca.orgflix.entity.Film;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

/**
 * JPA Hibernate implementation for Cast DAO
 */
@Component
public class HibernateCastDAO implements CastDAO {

    private SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
    private EntityManager em;

    //CREATE

    /**
     * @see CastDAO#addCast(Cast)
     */
    @Override
    @Transactional
    public boolean addCast(Cast cast) {
        try {
            em.persist(cast);
            em.flush();
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    //RETRIEVE

    /**
     * @see CastDAO#listCast()
     */
    @Override
    public List<Cast> listCast() {
        return em.createQuery("from CASTS", Cast.class)
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
    @Transactional
    public boolean editCast(Cast cast) {
        try {
            Cast actualCast = em.find(Cast.class, cast.getId());
            em.merge(actualCast);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    /**
     * @see CastDAO#addCastToFilm(Cast, int)
     */
    @Override
    @Transactional
    public boolean addCastToFilm(Cast cast, int filmId) {
        try {
            Film actualFilm = em.find(Film.class, filmId);
            actualFilm.addCast(cast);
            em.merge(actualFilm);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    //DELETE

    /**
     * @see CastDAO#remove(Cast)
     */
    @Override
    @Transactional
    public boolean remove(Cast cast) {
        try {
            Cast actualCast = em.find(Cast.class, cast.getId());
            em.remove(actualCast);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

//    @Override
//    public boolean isStarringIn(int actorId, int filmId) {
//        return false;
//    }
//
//    @Override
//    public boolean exists(Cast cast) {
//        return false;
//    }

//    @Override
//    public boolean isStarringIn(int actorId, int filmId) {          //////////DO I EVEN NEED THIS?
//        try {
////            Cast actualCast = em.find(Cast.class, actorId);
////            for (Film film : actualCast.getFilms()) {
////                if (film.getId() == filmId)
////                    return true;
////            }
////            return false;
//            Query query = em.createQuery("select count(*) from FILM_TO_CAST where FILM_ID = :filmId and ACTOR_ID = :castId");
//            query.setParameter("filmId", filmId);
//            query.setParameter("castId", actorId);
//            return (int) query.getSingleResult() > 0 ;
//        } catch (RuntimeException e) {
//            return false;
//        }
//    }

//    @Override
//    public boolean exists(Cast cast) {
//        try {
//            Cast actualCast = em.find(Cast.class, cast.getId());
//            return actualCast != null;
//        } catch (RuntimeException e) {
//            return false;
//        }
//    }
}
