package am.aca.orgflix.dao.implHibernate;

import am.aca.orgflix.dao.ListDao;
import am.aca.orgflix.entity.Film;
import am.aca.orgflix.entity.User;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
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
            em.getTransaction().begin();
            User user = em.find(User.class, userId);
            Film film = em.find(Film.class, filmId);

            am.aca.orgflix.entity.List list = new am.aca.orgflix.entity.List();
            list.setFilm(film);
            list.setUser(user);
            list.setWatched(true);
            list.setPublic(isPublic);
            em.persist(list);
            em.flush();
            em.getTransaction().commit();
            return true;
        } catch (RuntimeException e) {
            em.getTransaction().rollback();
            return false;
        }
    }

    /**
     * @see ListDao#insertPlanned(int, int, boolean)
     */
    @Override
    public boolean insertPlanned(int filmId, int userId, boolean isPublic) {
        try {
            em.getTransaction().begin();
            User user = em.find(User.class, userId);
            Film film = em.find(Film.class, filmId);
            am.aca.orgflix.entity.List list = new am.aca.orgflix.entity.List();
            list.setFilm(film);
            list.setUser(user);
            list.setPlanned(true);
            list.setPublic(isPublic);
            em.persist(list);
            em.flush();
            em.getTransaction().commit();
            return true;
        } catch (RuntimeException e) {
            em.getTransaction().rollback();
            return false;
        }
    }

    @Override
    public List<Film> showOwnWatched(int userId, int page) {

        //VERSION 1, pagination support not added
        List<Film> resultList = new ArrayList<>();
        try {
            List<am.aca.orgflix.entity.List> lists = em.createQuery("from List", am.aca.orgflix.entity.List.class).getResultList();
            for (int i = page * 12; i < page * 12 + 12; i++) {
                am.aca.orgflix.entity.List list = lists.get(i);
                if (list.getUser().getId() == userId && list.isWatched())
                    resultList.add(list.getFilm());
            }
        } catch (RuntimeException ignored) {
        }
        return resultList;
    }

    @Override
    public List<Film> showOwnPlanned(int userId, int page) {
        List<Film> resultList = new ArrayList<>();
        try {
            List<am.aca.orgflix.entity.List> lists = em.createQuery("from List", am.aca.orgflix.entity.List.class).getResultList();
            for (int i = page * 12; i < page * 12 + 12; i++) {
                am.aca.orgflix.entity.List list = lists.get(i);
                if (list.getUser().getId() == userId && list.isPlanned())
                    resultList.add(list.getFilm());
            }
        } catch (RuntimeException ignored) {
        }
        return resultList;
    }

    @Override
    public List<Film> showOthersWatched(int userId, int page) {
        List<Film> resultList = new ArrayList<>();
        try {
            List<am.aca.orgflix.entity.List> lists = em.createQuery("from List", am.aca.orgflix.entity.List.class).getResultList();
            for (int i = page * 12; i < page * 12 + 12; i++) {
                am.aca.orgflix.entity.List list = lists.get(i);
                if (list.getUser().getId() == userId && list.isWatched() && list.isPublic())
                    resultList.add(list.getFilm());
            }
        } catch (RuntimeException ignored) {
        }
        return resultList;
    }

    @Override
    public List<Film> showOthersPlanned(int userId, int page) {
        List<Film> resultList = new ArrayList<>();
        try {
            List<am.aca.orgflix.entity.List> lists = em.createQuery("from List", am.aca.orgflix.entity.List.class).getResultList();
            for (int i = page * 12; i < page * 12 + 12; i++) {
                am.aca.orgflix.entity.List list = lists.get(i);
                if (list.getUser().getId() == userId && list.isPlanned() && list.isPublic())
                    resultList.add(list.getFilm());
            }
        } catch (RuntimeException ignored) {
        }
        return resultList;
    }

    @Override
    public boolean updateWatched(int filmId, int userId) {
        try {
            em.getTransaction().begin();

            List<am.aca.orgflix.entity.List> lists = em.createQuery("from List", am.aca.orgflix.entity.List.class).getResultList();
            for (am.aca.orgflix.entity.List list : lists) {
                if (list.getUser().getId() == userId && list.getFilm().getId() == filmId) {
                    list.setWatched(true);
                    em.merge(list);
                    em.flush();
                    em.getTransaction().commit();
                    return true;
                }
            }
            em.getTransaction().rollback();
            return false;
        } catch (RuntimeException e) {
            em.getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean updatePlanned(int filmId, int userId) {
        try {
            em.getTransaction().begin();

            List<am.aca.orgflix.entity.List> lists = em.createQuery("from List", am.aca.orgflix.entity.List.class).getResultList();
            for (am.aca.orgflix.entity.List list : lists) {
                if (list.getUser().getId() == userId && list.getFilm().getId() == filmId) {
                    list.setPlanned(true);
                    em.merge(list);
                    em.flush();
                    em.getTransaction().commit();
                    return true;
                }
            }
            em.getTransaction().rollback();
            return false;
        } catch (RuntimeException e) {
            em.getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean changePrivacy(Film film, int userId, boolean isPublic) {
        try {
            em.getTransaction().begin();

            List<am.aca.orgflix.entity.List> lists = em.createQuery("from List", am.aca.orgflix.entity.List.class).getResultList();
            for (am.aca.orgflix.entity.List list : lists) {
                if (list.getUser().getId() == userId && list.getFilm().getId() == film.getId()) {
                    list.setPublic(isPublic);
                    em.merge(list);
                    em.flush();
                    em.getTransaction().commit();
                    return true;
                }
            }
            em.getTransaction().rollback();
            return false;
        } catch (RuntimeException e) {
            em.getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean resetWatched(int filmId, int userId) {
        try {
            em.getTransaction().begin();

            List<am.aca.orgflix.entity.List> lists = em.createQuery("from List", am.aca.orgflix.entity.List.class).getResultList();
            for (am.aca.orgflix.entity.List list : lists) {
                if (list.getUser().getId() == userId && list.getFilm().getId() == filmId) {
                    list.setWatched(false);
                    em.merge(list);
                    em.flush();
                    em.getTransaction().commit();
                    return true;
                }
            }
            em.getTransaction().rollback();
            return false;
        } catch (RuntimeException e) {
            em.getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean resetPlanned(int filmId, int userId) {
        try {
            em.getTransaction().begin();

            List<am.aca.orgflix.entity.List> lists = em.createQuery("from List", am.aca.orgflix.entity.List.class).getResultList();
            for (am.aca.orgflix.entity.List list : lists) {
                if (list.getUser().getId() == userId && list.getFilm().getId() == filmId) {
                    list.setPlanned(false);
                    em.merge(list);
                    em.flush();
                    em.getTransaction().commit();
                    return true;
                }
            }
            em.getTransaction().rollback();
            return false;
        } catch (RuntimeException e) {
            em.getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean removeFilm(int filmId, int userId) {
        try {
            em.getTransaction().begin();

            List<am.aca.orgflix.entity.List> lists = em.createQuery("from List", am.aca.orgflix.entity.List.class).getResultList();
            for (am.aca.orgflix.entity.List list : lists) {
                if (list.getUser().getId() == userId && list.getFilm().getId() == filmId) {
                    em.remove(list);
                    em.flush();
                    em.getTransaction().commit();
                    return true;
                }
            }
            em.getTransaction().rollback();
            return false;
        } catch (RuntimeException e) {
            em.getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean areRelated(int filmId, int userId) {
        try {
            List<am.aca.orgflix.entity.List> lists = em.createQuery("from List", am.aca.orgflix.entity.List.class).getResultList();
            for (am.aca.orgflix.entity.List list : lists) {
                if (list.getUser().getId() == userId && list.getFilm().getId() == filmId) {
                    return true;
                }
            }
            return false;
        } catch (RuntimeException e) {
            return false;
        }
    }

    @Override
    public boolean isWatched(int filmId, int userId) {
        try {
            List<am.aca.orgflix.entity.List> lists = em.createQuery("from List", am.aca.orgflix.entity.List.class).getResultList();
            for (am.aca.orgflix.entity.List list : lists) {
                if (list.getUser().getId() == userId && list.getFilm().getId() == filmId && list.isWatched()) {
                    return true;
                }
            }
            return false;
        } catch (RuntimeException e) {
            return false;
        }
    }

    @Override
    public boolean isPlanned(int filmId, int userId) {
        try {
            List<am.aca.orgflix.entity.List> lists = em.createQuery("from List", am.aca.orgflix.entity.List.class).getResultList();
            for (am.aca.orgflix.entity.List list : lists) {
                if (list.getUser().getId() == userId && list.getFilm().getId() == filmId && list.isPlanned()) {
                    return true;
                }
            }
            return false;
        } catch (RuntimeException e) {
            return false;
        }
    }

    @Override
    public int totalNumberOfWatched(int userId) {
        int count = 0;
        try {
            List<am.aca.orgflix.entity.List> lists = em.createQuery("from List", am.aca.orgflix.entity.List.class).getResultList();
            for (am.aca.orgflix.entity.List list : lists) {
                if (list.getUser().getId() == userId && list.isWatched()) {
                    count++;
                }
            }
            return count;
        } catch (RuntimeException e) {
            return count;
        }
    }

    @Override
    public int totalNumberOfPlanned(int userId) {
        int count = 0;
        try {
            List<am.aca.orgflix.entity.List> lists = em.createQuery("from List", am.aca.orgflix.entity.List.class).getResultList();
            for (am.aca.orgflix.entity.List list : lists) {
                if (list.getUser().getId() == userId && list.isPlanned()) {
                    count++;
                }
            }
            return count;
        } catch (RuntimeException e) {
            return count;
        }
    }

}
