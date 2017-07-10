package am.aca.orgflix.dao.implHibernate;

import am.aca.orgflix.dao.DaoException;
import am.aca.orgflix.dao.FilmDAO;
import am.aca.orgflix.entity.Cast;
import am.aca.orgflix.entity.Film;
import am.aca.orgflix.entity.Genre;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * JPA Hibernate implementation for Film DAO
 */
public class HibernateFilmDAO extends HibernateBaseDAO implements FilmDAO {

    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("Orgflix_PU");
    private EntityManager em = emf.createEntityManager();

    @Transactional
    @Override
    public boolean addFilm(Film film) {
        checkRequiredFields(film.getTitle());
        if (1888 > film.getProdYear()
                || Calendar.getInstance().get(Calendar.YEAR) + 7 < film.getProdYear())
            throw new DaoException("Invalid production year");
        try {
            em.persist(film);           ///////////////////FLUSH?
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    @Override
    public boolean addGenreToFilm(Genre genre, int filmId) {
        return true;
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
        Film film = em.find(Film.class, id);
        if (film == null)
            throw new DaoException("Film Not Found");
        return film;
    }

    @Override
    public List<Film> getFilmsList(int startIndex) {
        try {
            Query query = em.createQuery("from FILMS");
            query.setFirstResult(startIndex);
            query.setMaxResults(12);
            return query.getResultList();
        } catch (RuntimeException e) {
            return new ArrayList<>();           ////////////////////WHAT TO RETURN HERE?
        }
    }

    @Override
    public List<Film> getFilmsByGenre(Genre genre) {
        List<Film> filteredFilms = new ArrayList<>();
        Query query = em.createQuery("from FILMS");
        List<Film> films = query.getResultList();

        outerLoop:
        for (Film film : films) {
            for (Genre genre1 : film.getGenres()) {         ///////////////IS THERE A FASTER WAY?
                if (genre1 == genre) {
                    filteredFilms.add(film);
                    continue outerLoop;
                }
            }
        }

        return filteredFilms;
    }

    @Override
    public List<Film> getFilmsByCast(int actorId) {
        List<Film> filteredFilms = new ArrayList<>();
        Query query = em.createQuery("from FILMS");
        List<Film> films = query.getResultList();

        outerLoop:
        for (Film film : films) {
            for (Cast cast : film.getCasts()) {         ///////////////IS THERE A FASTER WAY?
                if (cast.getId() == actorId) {
                    filteredFilms.add(film);
                    continue outerLoop;
                }
            }
        }

        return filteredFilms;
    }

    @Override
    public double getRating(int filmId) {
        try {
            Film actualFilm = em.find(Film.class, filmId);
            int ratingSum = actualFilm.getRate_1star() + 2 * actualFilm.getRate_2star() +
                    3 * actualFilm.getRate_3star() + 4 * actualFilm.getRate_4star() + 5 * actualFilm.getRate_5star();
            if (ratingSum == 0)
                return 0;

            int ratingCount = actualFilm.getRate_1star() + actualFilm.getRate_2star() +
                    actualFilm.getRate_3star() + actualFilm.getRate_4star() + actualFilm.getRate_5star();

            return ratingSum / ratingCount;
        } catch (RuntimeException e) {
            return 0;
        }
    }

    @Override
    public int totalNumberOfFilms() {
        try {
            Query query = em.createNativeQuery("SELECT count(*) FROM FILMS");
            return (int) query.getSingleResult();
        } catch (RuntimeException e) {
            return 0;
        }
    }

    @Override
    public List<Film> getFilteredFilms(String title, int startYear, int finishYear, String hasOscar,
                                       String director, String castId, String genreId) {
        List<Film> resultList = new ArrayList<>();
//        TypedQuery<Film> query = em.createQuery("select * from FILMS where title like ?1 and prodYear >= ?2 and prodYear <= ?3 and hasOscar like ?4 and (director like ?5 or director is null) and  ");
        Query query = em.createNativeQuery("SELECT FILMS.ID, FILMS.TITLE, FILMS.PROD_YEAR, " +
                "FILMS.HAS_OSCAR, FILMS.image_ref, FILMS.DIRECTOR, " +
                "FILMS.RATE_1STAR, FILMS.RATE_2STAR, FILMS.RATE_3STAR, " +
                "FILMS.RATE_4STAR, FILMS.RATE_5STAR FROM FILMS " +
                "LEFT JOIN FILM_TO_CAST " +
                "ON FILMS.ID = FILM_TO_CAST.FILM_ID " +
                "LEFT JOIN GENRE_TO_FILM " +
                "ON GENRE_TO_FILM.FILM_ID = FILMS.ID " +
                "WHERE FILMS.TITLE LIKE ?1 " +
                "AND FILMS.PROD_YEAR >= ?2 " +
                "AND FILMS.PROD_YEAR <= ?3 " +
                "AND FILMS.HAS_OSCAR LIKE ?4 " +
                "AND (FILMS.DIRECTOR LIKE ?5 " +
                "OR FILMS.DIRECTOR IS NULL) " +
                "AND (FILM_TO_CAST.Actor_ID LIKE ?6 " +
                "OR FILM_TO_CAST.Actor_ID IS NULL)" +
                "AND (GENRE_TO_FILM.GENRE_ID LIKE ?7 " +
                "OR GENRE_TO_FILM.GENRE_ID IS NULL) ");
        try {

            query.setParameter(1, "%" + title + "%");
            query.setParameter(2, startYear);
            query.setParameter(3, finishYear);
            query.setParameter(4, hasOscar);
            query.setParameter(5, "%" + director + "%");
            query.setParameter(6, castId);
            query.setParameter(7, genreId);

            resultList = query.getResultList();
            return resultList;
        } catch (RuntimeException e) {
            return resultList;
        }
    }

    @Transactional
    @Override
    public boolean editFilm(Film film) {
        checkRequiredFields(film.getTitle());
        if (1888 > film.getProdYear()
                || Calendar.getInstance().get(Calendar.YEAR) + 7 < film.getProdYear())
            throw new DaoException("Invalid production year");
        try {
//            Film actualFilm = em.find(Film.class, film.getId());
//            actualFilm.setCasts(film.getCasts());
//            actualFilm.setGenres(film.getGenres());
//            actualFilm.setDirector(film.getDirector());
//            actualFilm.setTitle(film.getTitle());
//            actualFilm.setProdYear(film.getProdYear());
//            actualFilm.setImage(film.getImage());
//            actualFilm.setRate_1star(film.getRate_1star());
//            actualFilm.setRate_2star(film.getRate_2star());
//            actualFilm.setRate_3star(film.getRate_3star());
//            actualFilm.setRate_4star(film.getRate_4star());
//            actualFilm.setRate_5star(film.getRate_5star());       CHOOSE THE RIGHT VERSION

            em.refresh(film);

            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    @Override
    public boolean resetRelationCasts(Film film) {
        return true;
    }

    @Override
    public boolean resetRelationGenres(Film film) {
        return true;
    }

    @Transactional
    @Override
    public boolean remove(Film film) {
        try {
            Film actualFilm = em.find(Film.class, film.getId());
            em.remove(actualFilm);
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }
}
