package am.aca.orgflix.dao.implHibernate;

import am.aca.orgflix.dao.FilmDAO;
import am.aca.orgflix.entity.Cast;
import am.aca.orgflix.entity.Film;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.transaction.Transactional;
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

    @Transactional
    @Override
    public boolean addFilm(Film film) {
        try {
            em.persist(film);
            em.flush();
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    @Transactional
    @Override
    public boolean rateFilm(int filmId, int starType) {
        try {
            Query query;
            switch (starType) {
                case 1:
                    query = em.createQuery("update FILMS set rate_1star = rate_1star + 1 where id = ?1")
                            .setParameter(1, filmId);
                    break;
                case 2:
                    query = em.createQuery("update FILMS set rate_2star = rate_2star + 1 where id = ?1")
                            .setParameter(1, filmId);
                    break;
                case 3:
                    query = em.createQuery("update FILMS set rate_3star = rate_3star + 1 where id = ?1")
                            .setParameter(1, filmId);
                    break;
                case 4:
                    query = em.createQuery("update FILMS set rate_4star = rate_4star + 1 where id = ?1")
                            .setParameter(1, filmId);
                    break;
                case 5:
                    query = em.createQuery("update FILMS set rate_5star = rate_5star + 1 where id = ?1")
                            .setParameter(1, filmId);
                    break;
                default:
                    return false;

            }
            return query.executeUpdate() == 1;
        } catch (RuntimeException e) {
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
        return film.getCasts();
    }

    @Override
    public List<Film> getFilmsList(int startIndex) {
        try {
            Query query = em.createQuery("from FILMS");
            query.setFirstResult(startIndex);
            query.setMaxResults(FIXED_NUMBER_OF_FILMS_PER_PAGE);
            return query.getResultList();
        } catch (RuntimeException e) {
            return new ArrayList<>();
        }
    }

//    /**
//     * @see FilmDAO#getFilmsByGenre(Genre)
//     */
//    @Override
//    public List<Film> getFilmsByGenre(Genre genre) {
//        List<Film> result = new ArrayList<>();
//        try {
//            Query query = em.createQuery("select film from FILMS film " +
//                    "join film.genres genre where genre = :givenGenre");
//            query.setParameter("givenGenre", genre);
//            result = query.getResultList();
//        } catch (RuntimeException ignored) {
//        }
//        return result;
//    }

    /**
     * @see FilmDAO#getFilmsByCast(int)
     */
    @Override
    public List<Film> getFilmsByCast(int actorId) {
        List<Film> result = new ArrayList<>();
        try {
            Query query = em.createQuery("select film from FILMS film " +
                    "join film.casts cast where cast.id = :givenCast");
            query.setParameter("givenCast", actorId);
            result = query.getResultList();
        } catch (RuntimeException ignored) {
        }
        return result;
    }

//    @Override
//    public double getRating(int filmId) {
//        try {
//            Film actualFilm = em.find(Film.class, filmId);
//            int ratingSum = actualFilm.getRate_1star() + 2 * actualFilm.getRate_2star() +
//                    3 * actualFilm.getRate_3star() + 4 * actualFilm.getRate_4star() + 5 * actualFilm.getRate_5star();
//            if (ratingSum == 0)
//                return 0;
//
//            int ratingCount = actualFilm.getRate_1star() + actualFilm.getRate_2star() +
//                    actualFilm.getRate_3star() + actualFilm.getRate_4star() + actualFilm.getRate_5star();
//
//            return ratingSum / ratingCount;
//        } catch (RuntimeException e) {
//            return 0;
//        }
//    }

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
            Query query = em.createQuery("SELECT count(*) FROM FILMS");
            return (int) query.getSingleResult();
        } catch (RuntimeException e) {
            return 0;
        }
    }

    @Override
    public List<Film> getFilteredFilms(String title, int startYear, int finishYear, boolean hasOscar,
                                       String director, int castId, int genreId) {
        List<Film> resultList = new ArrayList<>();
        StringBuilder queryBuilder = new StringBuilder();

        queryBuilder.append("select film from FILMS film " +
                "join film.casts cast " +
                "join film.genres genre " +
                "where film.title like :title ");
        if (startYear != 0)
            queryBuilder.append("and film.prodYear <= :startYear ");
        if (finishYear != 0)
            queryBuilder.append("and film.prodYear >= :finishYear ");
        if (hasOscar)
            queryBuilder.append("and film.hasOscar = :hasOscar ");
        if (director != null)
            queryBuilder.append("and film.director like :director ");
        if (castId > 0)
            queryBuilder.append("and cast.id = :castId ");
        if (genreId > 0)
            queryBuilder.append("and genre.id = :genreId ");
        try {
            Query query = em.createQuery(queryBuilder.toString());

//            Query query = em.createQuery("select film from FILMS film " +
//                    "join film.casts cast " +
//                    "join film.genres genre " +
//                    "where film.title like :title " +
//                    "and film.prodYear <= :startYear " +
//                    "and film.prodYear >= :finishYear " +
//                    "and film.hasOscar = :hasOscar " +
//                    "and film.director like :director " +
//                    "and cast.id = :castId " +
//                    "and genre.id = :genreId");
            query.setParameter("title", "%" + title + "%");

            if (startYear != 0)
                query.setParameter("startYear", startYear);
            if (finishYear != 0)
                query.setParameter("finishYear", finishYear);
            if (hasOscar)
                query.setParameter("hasOscar", true);
            if (director != null)
                query.setParameter("director", "%" + director + "%");
            if (castId > 0)
                query.setParameter("castId", castId);
            if (genreId > 0)
                query.setParameter("genreId", genreId);

            resultList = query.getResultList();
            return resultList;
        } catch (RuntimeException e) {
            return resultList;
        }
    }

    @Transactional
    @Override
    public boolean editFilm(Film film) {
        try {
            em.merge(film);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    /**
     * @see FilmDAO#remove(int)
     */
    @Override
    public boolean remove(int filmId) {
        try {
            Film actualFilm = em.find(Film.class, filmId);
            em.remove(actualFilm);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

//    @Transactional
//    @Override
//    public boolean remove(Film film) {
//        try {
//            em.remove(film);
//            em.flush();
//            return true;
//        } catch (RuntimeException e) {
//            return false;
//        }
//    }
}
