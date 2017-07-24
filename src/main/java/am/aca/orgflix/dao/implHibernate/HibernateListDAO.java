package am.aca.orgflix.dao.implHibernate;

import am.aca.orgflix.dao.ListDao;
import am.aca.orgflix.entity.Film;
import am.aca.orgflix.entity.User;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * JPA Hibernate implementation for List DAO
 */
@Component
public class HibernateListDAO implements ListDao {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("Orgflix_PU");
    private EntityManager em = emf.createEntityManager();

    /**
     * @see ListDao#insertWatched(int, int, boolean)
     */
    @Override
    public boolean insertWatched(int filmId, int userId, boolean isPublic) {
        try {
            User user = em.find(User.class, userId);
            Film film = em.find(Film.class, filmId);

            am.aca.orgflix.entity.List list = new am.aca.orgflix.entity.List();
            list.setWatched(true);
            list.setPublic(isPublic);
            list.setFilm(film);
            list.setUser(user);

            user.getLists().add(list);
            em.merge(user);
            return true;
        } catch (RuntimeException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @see ListDao#insertPlanned(int, int, boolean)
     */
    @Override
    public boolean insertPlanned(int filmId, int userId, boolean isPublic) {
        try {
            User user = em.find(User.class, userId);
            Film film = em.find(Film.class, filmId);

            am.aca.orgflix.entity.List list = new am.aca.orgflix.entity.List();
            list.setPlanned(true);
            list.setPublic(isPublic);
            list.setFilm(film);
            list.setUser(user);                                         ////////??????????????????????

            user.getLists().add(list);
            em.merge(user);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    @Override
    public List<Film> showOwnWatched(int userId, int page) {

//        //VERSION 1, pagination support not added
//        List<Film> resultList = new ArrayList<>();
//        try {
//            User user = em.find(User.class, userId);
//            List<am.aca.orgflix.entity.List> lists = new ArrayList(user.getLists());
//            for (am.aca.orgflix.entity.List list : lists) {
//                resultList.add(list.getFilm());
//            }
//        } catch (RuntimeException ignored) {
//        }
//        return resultList;

        //VERSION 2
        try {
            Query query = em.createQuery("select film from FILMS film " +
                    "join LISTS list " +
                    "where list.id.user.id = :userId and list.watched = true");
            query.setMaxResults(12);
            query.setFirstResult(page);
            query.setParameter("userId", userId);

            return query.getResultList();
        } catch (RuntimeException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Film> showOwnPlanned(int userId, int page) {
        try {
            Query query = em.createQuery("select film from FILMS film " +
                    "join LISTS list " +
                    "where list.id.user.id = :userId and list.planned = true");
            query.setMaxResults(12);
            query.setFirstResult(page);
            query.setParameter("userId", userId);

            return query.getResultList();
        } catch (RuntimeException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Film> showOthersWatched(int userId, int page) {
        try {
            Query query = em.createQuery("select film from FILMS film " +
                    "join LISTS list " +
                    "where list.id.user.id = :userId " +
                    "and list.watched = true " +
                    "and list.public = true");
            query.setMaxResults(12);
            query.setFirstResult(page);
            query.setParameter("userId", userId);

            return query.getResultList();
        } catch (RuntimeException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public List<Film> showOthersPlanned(int userId, int page) {
        try {
            Query query = em.createQuery("select film from FILMS film " +
                    "join LISTS list " +
                    "where list.id.user.id = :userId " +
                    "and list.planned = true " +
                    "and list.public = true");
            query.setMaxResults(12);
            query.setFirstResult(page);
            query.setParameter("userId", userId);

            return query.getResultList();
        } catch (RuntimeException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public boolean updateWatched(int filmId, int userId) {
        try {
            Query query = em.createQuery("update LISTS list " +
                    "set list.watched = true " +
                    "where list.id.user.id = :userId and list.id.film.id = :filmId");
            query.setParameter("userId", userId);
            query.setParameter("filmId", filmId);
            return (query.executeUpdate() == 1);
        } catch (RuntimeException e) {
            return false;
        }
    }

    @Override
    public boolean updatePlanned(int filmId, int userId) {
        try {
            Query query = em.createQuery("update LISTS list " +
                    "set list.planned = true " +
                    "where list.id.user.id = :userId and list.id.film.id = :filmId");
            query.setParameter("userId", userId);
            query.setParameter("filmId", filmId);
            return (query.executeUpdate() == 1);
        } catch (RuntimeException e) {
            return false;
        }
    }

    @Override
    public boolean changePrivacy(Film film, int userId, boolean isPublic) {
        try {
            em.refresh(film);
            Query query = em.createQuery("update LISTS list " +
                    "set list.public = :public " +
                    "where list.id.user.id = :userId and list.id.film.id = :filmId");
            query.setParameter("public", isPublic);
            query.setParameter("userId", userId);
            query.setParameter("filmId", film.getId());
            return (query.executeUpdate() == 1);
        } catch (RuntimeException e) {
            return false;
        }
    }

    @Override
    public boolean resetWatched(int filmId, int userId) {
        try {
            Query query = em.createQuery("update LISTS list " +
                    "set list.watched = false " +
                    "where list.id.user.id = :userId and list.id.film.id = :filmId");
            query.setParameter("userId", userId);
            query.setParameter("filmId", filmId);
            return (query.executeUpdate() == 1);
        } catch (RuntimeException e) {
            return false;
        }
    }

    @Override
    public boolean resetPlanned(int filmId, int userId) {
        try {
            Query query = em.createQuery("update LISTS list " +
                    "set list.planned = false " +
                    "where list.id.user.id = :userId and list.id.film.id = :filmId");
            query.setParameter("userId", userId);
            query.setParameter("filmId", filmId);
            return (query.executeUpdate() == 1);
        } catch (RuntimeException e) {
            return false;
        }
    }

    @Override
    public boolean removeFilm(int filmId, int userId) {
        try {
            Query query = em.createQuery("delete from LISTS list " +
                    "where list.id.user.id = :userId and list.id.film.id = :filmId");
            query.setParameter("userId", userId);
            query.setParameter("filmId", filmId);
            return (query.executeUpdate() == 1);
        } catch (RuntimeException e) {
            return false;
        }
    }

    @Override
    public boolean areRelated(int filmId, int userId) {
        try {
            Query query = em.createQuery("select count(list) from LISTS list " +
                    "where list.id.user.id = :userId and list.id.film.id = :filmId");
            query.setParameter("userId", userId);
            query.setParameter("filmId", filmId);
            return ((int) query.getSingleResult() == 1);
        } catch (RuntimeException e) {
            return false;
        }
    }

    @Override
    public boolean isWatched(int filmId, int userId) {
        try {
            Query query = em.createQuery("select count(list) from LISTS list " +
                    "where list.id.user.id = :userId and list.id.film.id = :filmId " +
                    "and list.watched = true ");
            query.setParameter("userId", userId);
            query.setParameter("filmId", filmId);
            return ((int) query.getSingleResult() == 1);
        } catch (RuntimeException e) {
            return false;
        }
    }

    @Override
    public boolean isPlanned(int filmId, int userId) {
        try {
            Query query = em.createQuery("select count(list) from LISTS list " +
                    "where list.id.user.id = :userId and list.id.film.id = :filmId " +
                    "and list.planned = true ");
            query.setParameter("userId", userId);
            query.setParameter("filmId", filmId);
            return ((int) query.getSingleResult() == 1);
        } catch (RuntimeException e) {
            return false;
        }
    }

    @Override
    public int totalNumberOfWatched(int userId) {
        try {
            Query query = em.createQuery("select count(film) from FILMS film " +
                    "join LISTS list " +
                    "where list.id.user.id = :userId and list.watched = true");
            query.setParameter("userId", userId);

            return (int) query.getSingleResult();
        } catch (RuntimeException e) {
            return 0;
        }
    }

    @Override
    public int totalNumberOfPlanned(int userId) {
        try {
            Query query = em.createQuery("select count(film) from FILMS film " +
                    "join LISTS list " +
                    "where list.id.user.id = :userId and list.planned = true");
            query.setParameter("userId", userId);

            return (int) query.getSingleResult();
        } catch (RuntimeException e) {
            return 0;
        }
    }

    public int totalNumberOfWatchedOthers(int userId) {
        try {
            Query query = em.createQuery("select count(film) from FILMS film " +
                    "join LISTS list " +
                    "where list.id.user.id = :userId and list.watched = true " +
                    "and list.public = true ");
            query.setParameter("userId", userId);

            return (int) query.getSingleResult();
        } catch (RuntimeException e) {
            return 0;
        }
    }

    public int totalNumberOfPlannedOthers(int userId) {
        try {
            Query query = em.createQuery("select count(film) from FILMS film " +
                    "join LISTS list " +
                    "where list.id.user.id = :userId and list.planned = true " +
                    "and list.public = true ");
            query.setParameter("userId", userId);

            return (int) query.getSingleResult();
        } catch (RuntimeException e) {
            return 0;
        }
    }
}
