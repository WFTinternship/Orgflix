package daotest;

import am.aca.dao.CastDao;
import am.aca.dao.impljdbc.CastDaoJdbc;
import am.aca.dao.FilmDao;
import am.aca.dao.impljdbc.FilmDaoJdbc;
import am.aca.entity.Cast;
import am.aca.entity.Film;
import am.aca.entity.Genre;
import am.aca.util.ConnType;
import am.aca.util.DbManager;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Vardan on 04.06.2017
 */
public class FilmTest {
    private FilmDao filmDao = new FilmDaoJdbc(ConnType.PRODUCTION);
    private CastDao castDao = new CastDaoJdbc(ConnType.PRODUCTION);
    private Film film;
    private Cast cast;
    private List<Cast> castList;

    @Before
    public void setUp() {
        filmDao.setDataSource(DbManager.getInstance().getDataSource());
        castDao.setDataSource(DbManager.getInstance().getDataSource());
        film = new Film();
    }

    @After
    public void tearDown() {
        DbManager.emptyTestTables(new String[]{"genre_to_film", "film_to_cast", "films", "casts"});
    }

    @Test
    public void addFilm_Succeeded() {
        film.setTitle("Captain Fantastic");
        filmDao.addFilm(film);
        Assert.assertEquals(1, filmDao.getFilmsList(0).size());
    }


    @Test
    public void addFilm_Fail() {
        assertFalse(filmDao.addFilm(film));
    }

    @Test
    public void editFilmChangeCheck_Succeeded() {
        cast = castDao.addCast("Matt Ross", false);
        castList = new ArrayList<>();

        film.setTitle("Captain Fantastic");
        castList.add(cast);
        film.setCasts(castList);
        film.setDirector("Matthew Ross");
        filmDao.addFilm(film);
        film.setHasOscar(true);
        assertTrue(filmDao.editFilm(film));


    }

    @Test
    public void editFilmSizeCheck_Succeeded() {
        cast = castDao.addCast("Matt Ross", false);
        castList = new ArrayList<>();

        film.setTitle("Captain Fantastic");
        castList.add(cast);
        film.setCasts(castList);
        film.setDirector("Matthew Ross");
        filmDao.addFilm(film);
        film.setDirector("Matt Ross");
        filmDao.editFilm(film);
        Assert.assertEquals(1, filmDao.totalNumberOfFilms());
    }


    @Test
    public void editFilm_Fail() {
        filmDao.addFilm(film);
        assertFalse(filmDao.editFilm(film));
    }

    @Test
    public void getFilmById_Succeeded() {
        film.setTitle("Captain Fantastic");
        filmDao.addFilm(film);
        Assert.assertEquals(film, filmDao.getFilmById(film.getId()));
    }

    @Test
    public void getFilmById_Failed() {
        Assert.assertEquals(null, filmDao.getFilmById(-1));
    }

    @Test
    public void getFilmsByCast_Succeeded() {
        cast = castDao.addCast("Matt Ross", false);
        castList = new ArrayList<>();
        film.setTitle("Captain Fantastic");
        castList.add(cast);
        film.setCasts(castList);
        filmDao.addFilm(film);
        filmDao.addCastToFilm(cast, film);
        Assert.assertEquals(film.getId(), filmDao.getFilmsByCast(cast).get(0));
    }

    @Test
    public void getFilmsByCast_Failed() {
        Assert.assertEquals(0, filmDao.getFilmsByCast(0).size());
    }


    @Test
    public void addGenreToFilmBySize_Succeeded() {
        film.setTitle("Captain Fantastic");
        filmDao.addFilm(film);
        filmDao.addGenreToFilm(Genre.COMEDY, film);
        Assert.assertEquals(1, filmDao.getFilmsByGenre(Genre.COMEDY).size());
    }

    @Test
    public void addGenreToFilmByFilm_Succeeded() {
        film.setTitle("Captain Fantastic");
        filmDao.addFilm(film);
        filmDao.addGenreToFilm(Genre.COMEDY, film);
        Assert.assertEquals(film.getId(), filmDao.getFilmsByGenre(Genre.COMEDY).get(0));
    }

    @Test
    public void rateFilm_Succeeded() {
        cast = castDao.addCast("Matt Ross", false);
        castList = new ArrayList<>();
        film.setTitle("Captain Fantastic");
        castList.add(cast);
        film.setCasts(castList);
        filmDao.addFilm(film);
        filmDao.rateFilm(film.getId(), 5);
        filmDao.rateFilm(film.getId(), 5);
        Assert.assertEquals(2, filmDao.getFilmById(film.getId()).getRate_5star());
    }

    @Test
    public void getFilmsList_Succeeded() {
        film.setTitle("Captain Fantastic");
        for (int i = 0; i < 7; i++) {
            filmDao.addFilm(film);
        }
        Assert.assertEquals(7, filmDao.getFilmsList(0).size());
    }

    @Test
    public void getFilmsListPage2_Succeeded() {
        film.setTitle("Captain Fantastic");
        for (int i = 0; i < 20; i++) {
            filmDao.addFilm(film);
        }
        Assert.assertEquals(20-12, filmDao.getFilmsList(12).size());
    }

    @Test
    public void getFilmsList_Failed() {
        Assert.assertEquals(0, filmDao.getFilmsList(0).size());
    }

    @Test
    public void totalNumber_Succeeded() {
        film.setTitle("Captain Fantastic");
        for (int i = 0; i < 6; i++) {
            filmDao.addFilm(film);
        }
        Assert.assertEquals(6, filmDao.totalNumberOfFilms());
    }
    @Test
    public void totalNumber_Failed() {
        Assert.assertEquals(0, filmDao.totalNumberOfFilms());
    }

    @Test
    public void getFilmsListIndex_Failed() {
        film.setTitle("Captain Fantastic");
        filmDao.addFilm(film);
        Assert.assertEquals(0, filmDao.getFilmsList(1).size());
    }

    @Test
    public void getFilmsByGenreSize_Succeeded() {
        film.setTitle("Captain Fantastic");
        filmDao.addFilm(film);
        filmDao.addGenreToFilm(Genre.COMEDY, film);
        Assert.assertEquals(1, filmDao.getFilmsByGenre(Genre.COMEDY).size());
    }

    @Test
    public void getFilmsByGenreFilm_Succeeded() {
        film.setTitle("Captain Fantastic");
        filmDao.addFilm(film);
        filmDao.addGenreToFilm(Genre.COMEDY, film);
        Assert.assertEquals(film.getId(), filmDao.getFilmsByGenre(Genre.COMEDY).get(0));
    }


    @Test
    public void getFilmsByGenre_Fail() {
        Assert.assertEquals(0, filmDao.getFilmsByGenre(Genre.WAR).size());
    }

    @Test
    public void getRatingInt_Succeeded() {
        film.setTitle("Captain Fantastic");
        filmDao.addFilm(film);
        filmDao.rateFilm(film.getId(), 4);
        filmDao.rateFilm(film.getId(), 4);
        filmDao.rateFilm(film.getId(), 3);
        filmDao.rateFilm(film.getId(), 5);
        Assert.assertEquals(4.0, filmDao.getRating(film), 0.01);
    }

    @Test
    public void getRatingDouble_Succeeded() {
        film.setTitle("Captain Fantastic");
        filmDao.addFilm(film);
        filmDao.rateFilm(film.getId(), 4);
        filmDao.rateFilm(film.getId(), 5);
        Assert.assertEquals(4.5, filmDao.getRating(film), 0.01);
    }

    @Test
    public void getRatingNaN_Failed() {
        film.setTitle("Captain Fantastic");
        filmDao.addFilm(film);
        Assert.assertEquals(0, filmDao.getRating(film), 0.01);
    }
}