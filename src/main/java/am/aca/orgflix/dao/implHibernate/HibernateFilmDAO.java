package am.aca.orgflix.dao.implHibernate;

import am.aca.orgflix.dao.FilmDAO;
import am.aca.orgflix.entity.Cast;
import am.aca.orgflix.entity.Film;
import am.aca.orgflix.entity.Genre;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * JPA Hibernate implementation for Film DAO
 */
@Component
public class HibernateFilmDAO implements FilmDAO {

    private static final int FIXED_NUMBER_OF_FILMS_PER_PAGE = 12;
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("Orgflix_PU");
    private EntityManager em = emf.createEntityManager();

    @Override
    @SuppressWarnings({"duplicates", "duplicate"})
    public boolean addFilm(Film film) {
        try {
            em.getTransaction().begin();
            em.persist(film);
            em.flush();
            em.getTransaction().commit();
            return true;
        } catch (RuntimeException e) {
            em.getTransaction().rollback();
            return false;
        }
    }

    @Override
    public boolean rateFilm(int filmId, int starType) {
        try {
            em.getTransaction().begin();
            Query query;
            Film film = em.find(Film.class, filmId);
            switch (starType) {
                case 1:
                    film.setRate_1star(film.getRate_1star() + 1);
                    break;
                case 2:
                    film.setRate_2star(film.getRate_2star() + 1);
                    break;
                case 3:
                    film.setRate_3star(film.getRate_3star() + 1);
                    break;
                case 4:
                    film.setRate_4star(film.getRate_4star() + 1);
                    break;
                case 5:
                    film.setRate_5star(film.getRate_5star() + 1);
                    break;
                default:
                    em.getTransaction().rollback();
                    return false;
            }

            em.merge(film);
            em.getTransaction().commit();
            return true;
        } catch (RuntimeException e) {
            em.getTransaction().rollback();
            return false;
        }
    }

    @Override
    public Film getFilmById(int id) {
        try {
            return em.find(Film.class, id);
        } catch (RuntimeException e) {
            return null;
        }
    }

    @Override
    public List<Cast> getCastsByFilm(int filmId) {
        Film film = em.find(Film.class, filmId);
        if (film == null)
            return null;
        return film.getCasts();
    }

    @Override
    public List<Film> getFilmsList(int startIndex) {
        try {
            Query query = em.createQuery("from Film", Film.class);
            query.setFirstResult(startIndex);
            query.setMaxResults(FIXED_NUMBER_OF_FILMS_PER_PAGE);
            return query.getResultList();
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    /**
     * @see FilmDAO#getFilmsByCast(int)
     */
    @Override
    public List<Film> getFilmsByCast(int actorId) {
        List<Film> result = new ArrayList<>();
        try {
            List<Film> resultList = em.createQuery("from Film", Film.class).getResultList();
            outerloop:
            for (Film film : resultList) {
                for (Cast cast : film.getCasts()) {
                    if (cast.getId() == actorId)
                        result.add(film);
                    continue outerloop;
                }
            }
        } catch (RuntimeException ignored) {
        }
        return result;
    }

    /**
     * @see FilmDAO#getRating5(int)
     */
    @Override
    public int getRating5(int filmId) {
        try {
            Film actualFilm = em.find(Film.class, filmId);
            return actualFilm.getRate_5star();
        } catch (RuntimeException e) {
            return 0;
        }
    }

    /**
     * @see FilmDAO#getRating4(int)
     */
    @Override
    public int getRating4(int filmId) {
        try {
            Film actualFilm = em.find(Film.class, filmId);
            return actualFilm.getRate_4star();
        } catch (RuntimeException e) {
            return 0;
        }
    }

    /**
     * @see FilmDAO#getRating3(int)
     */
    @Override
    public int getRating3(int filmId) {
        try {
            Film actualFilm = em.find(Film.class, filmId);
            return actualFilm.getRate_3star();
        } catch (RuntimeException e) {
            return 0;
        }
    }

    /**
     * @see FilmDAO#getRating2(int)
     */
    @Override
    public int getRating2(int filmId) {
        try {
            Film actualFilm = em.find(Film.class, filmId);
            return actualFilm.getRate_2star();
        } catch (RuntimeException e) {
            return 0;
        }
    }

    /**
     * @see FilmDAO#getRating1(int)
     */
    @Override
    public int getRating1(int filmId) {
        try {
            Film actualFilm = em.find(Film.class, filmId);
            return actualFilm.getRate_1star();
        } catch (RuntimeException e) {
            return 0;
        }
    }

    @Override
    public int totalNumberOfFilms() {
        try {
            return em.createQuery("from Film", Film.class).getResultList().size();
        } catch (RuntimeException e) {
            return 0;
        }
    }

    @Override
    public List<Film> getFilteredFilms(String title, int startYear, int finishYear, boolean hasOscar,
                                       String director, int castId, int genreId) {
        List<Film> resultList;
        List<Film> result = new ArrayList<>();
        resultList = em.createQuery("from Film", Film.class).getResultList();
        for (Film film : resultList) {
            if (title!= null && !title.equals(""))
                if (film.getTitle().equals(title))
                    result.add(film);
            if (startYear != 0)
                if (film.getProdYear() < startYear)
                    result.add(film);
            if (finishYear != 0)
                if (film.getProdYear() > finishYear)
                    result.add(film);
            if (hasOscar)
                if (film.isHasOscar())
                    result.add(film);
            if (director != null)
                if (film.getDirector().equals(director))
                    result.add(film);
            if (castId != 0) {
                for (Film actualFilm : resultList) {
                    for (Cast cast : film.getCasts()) {
                        if (cast.getId() == castId)
                            result.add(actualFilm);
                    }
                }
            }
            if (genreId != 0) {
                for (Film actualFilm : resultList) {
                    for (Genre genre : film.getGenres()) {
                        if (genre.getValue() == genreId)
                            result.add(actualFilm);
                    }
                }
            }
//        StringBuilder queryBuilder = new StringBuilder();
//
//        queryBuilder.append("select film from FILMS film " +
//                "join film.casts cast " +
//                "join film.genres genre " +
//                "where film.title like :title ");
//        if (startYear != 0)
//            queryBuilder.append("and film.prodYear <= :startYear ");
//        if (finishYear != 0)
//            queryBuilder.append("and film.prodYear >= :finishYear ");
//        if (hasOscar)
//            queryBuilder.append("and film.hasOscar = :hasOscar ");
//        if (director != null)
//            queryBuilder.append("and film.director like :director ");
//        if (castId > 0)
//            queryBuilder.append("and cast.id = :castId ");
//        if (genreId > 0)
//            queryBuilder.append("and genre.id = :genreId ");
//        try {
//            Query query = em.createQuery(queryBuilder.toString());
//
////            Query query = em.createQuery("select film from FILMS film " +
////                    "join film.casts cast " +
////                    "join film.genres genre " +
////                    "where film.title like :title " +
////                    "and film.prodYear <= :startYear " +
////                    "and film.prodYear >= :finishYear " +
////                    "and film.hasOscar = :hasOscar " +
////                    "and film.director like :director " +
////                    "and cast.id = :castId " +
////                    "and genre.id = :genreId");
//            query.setParameter("title", "%" + title + "%");
//
//            if (startYear != 0)
//                query.setParameter("startYear", startYear);
//            if (finishYear != 0)
//                query.setParameter("finishYear", finishYear);
//            if (hasOscar)
//                query.setParameter("hasOscar", true);
//            if (director != null)
//                query.setParameter("director", "%" + director + "%");
//            if (castId > 0)
//                query.setParameter("castId", castId);
//            if (genreId > 0)
//                query.setParameter("genreId", genreId);
//
//            resultList = query.getResultList();

//        } catch (RuntimeException e) {
//            e.printStackTrace();
//            return resultList;
        }
        return result;
    }

    @Override
    public boolean editFilm(Film film) {
        try {
            em.getTransaction().begin();
            em.merge(film);
            em.getTransaction().commit();
            return true;
        } catch (RuntimeException e) {
            em.getTransaction().rollback();
            return false;
        }
    }

    /**
     * @see FilmDAO#remove(int)
     */
    @Override
    public boolean remove(int filmId) {
        try {
            em.getTransaction().begin();
            Film actualFilm = em.find(Film.class, filmId);
            em.remove(actualFilm);
            em.getTransaction().commit();
            return true;
        } catch (RuntimeException e) {
            em.getTransaction().rollback();
            return false;
        }
    }
}
