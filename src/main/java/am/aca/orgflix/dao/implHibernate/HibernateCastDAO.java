package am.aca.orgflix.dao.implHibernate;

import am.aca.orgflix.dao.CastDAO;
import am.aca.orgflix.entity.Cast;
import am.aca.orgflix.entity.Film;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

/**
 * JPA Hibernate implementation for Cast DAO
 */
public class HibernateCastDAO extends HibernateBaseDAO implements CastDAO {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("Orgflix_PU");
    private EntityManager em = emf.createEntityManager();

    @Override
    @Transactional
    public boolean addCast(Cast cast) {
        checkRequiredFields(cast.getName());

        try {
            em.persist(cast);
            em.flush();
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    @Override
    public boolean addCastToFilm(Cast cast, int filmId) {
        return true;
    }

    @Override
//    @Transactional
    public List<Cast> listCast() {
        return em.createQuery("from CASTS", Cast.class)
                .getResultList();
    }

    @Override
//    @Transactional
    public List<Cast> getCastsByFilm(int filmId) {
        Film film = em.getReference(Film.class, filmId);
        return film.getCasts();
    }

    /**
     * @see CastDAO#getCastById(int)
     */
    public Cast getCastById(int castId){
        return new Cast();
    }

    @Override
    @Transactional
    public boolean editCast(Cast cast) {
        checkRequiredFields(cast.getName());

        try {
//            Cast actualCast = em.find(Cast.class, cast.getId());
//
//            actualCast.setName(cast.getName());
//            actualCast.setHasOscar(cast.isHasOscar());
//            actualCast.setFilms(cast.getFilms());

            em.refresh(cast);

            return true;                          ////////////////////CHECK WHICH VERSION TO CHOOSE
        } catch (RuntimeException e) {
            return false;
        }
    }

    @Override
    @Transactional
    public boolean remove(Cast cast) {
        try {
            Cast actualCast = em.find(Cast.class, cast.getId());            //////////DO I EVEN NEED THIS LINE?
            em.remove(actualCast);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    @Override
    public boolean isStarringIn(int actorId, int filmId) {
        try {
            Cast actualCast = em.find(Cast.class, actorId);
            for (Film film : actualCast.getFilms()) {
                if (film.getId() == filmId)
                    return true;
            }
            return false;
        } catch (RuntimeException e) {
            return false;
        }
    }

    @Override
    public boolean exists(Cast cast) {
        try {
            Cast actualCast = em.find(Cast.class, cast.getId());    ///////////SUPPRESS THIS?
            return actualCast != null;
        } catch (RuntimeException e) {
            return false;
        }
    }
}
