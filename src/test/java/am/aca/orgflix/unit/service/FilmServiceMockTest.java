package am.aca.orgflix.unit.service;

import am.aca.dao.DaoException;
import am.aca.dao.jdbc.impljdbc.JdbcCastDAO;
import am.aca.dao.jdbc.impljdbc.JdbcFilmDAO;
import am.aca.entity.Cast;
import am.aca.entity.Film;
import am.aca.entity.Genre;
import am.aca.orgflix.BaseUnitTest;
import am.aca.service.FilmService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

/**
 * Created by karine on 6/25/2017
 */
public class FilmServiceMockTest extends BaseUnitTest {

    @Autowired
    private FilmService filmService;

    @Mock
    private JdbcFilmDAO filmMock;
    @Mock
    private JdbcCastDAO castMock;

    private Cast cast = new Cast("Cate Blanchett", true);
    private Film film = new Film("Babel", 2006);
    private List<Film> films = new ArrayList<>();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(filmService, "castDao", castMock);
        ReflectionTestUtils.setField(filmService, "filmDao", filmMock);
    }

    @Test
    public void addFilmWGenresAndCasts_Success() {
        film.addCast(cast);
        film.addGeners(Genre.DRAMA);
        when(filmMock.addFilm(film)).thenReturn(true);
        for (Genre genre : film.getGenres()) {
            when(filmMock.addGenreToFilm(genre, film.getId())).thenReturn(true);
        }
        for (Cast cast : film.getCasts()) {
            when(castMock.addCast(cast)).thenReturn(true);
            when(castMock.addCastToFilm(cast, film.getId())).thenReturn(true);
        }
        Assert.assertTrue(filmService.addFilm(film));
    }

    @Test
    public void addFilmWOGenresAndCasts_Success() {
        when(filmMock.addFilm(film)).thenReturn(true);
        Assert.assertTrue(filmService.addFilm(film));
    }

    @Test
    public void addFilmInsertionError_Fail() {
        when(filmMock.addFilm(film)).thenReturn(false);
        for (Genre genre : film.getGenres()) {
            when(filmMock.addGenreToFilm(genre, film.getId())).thenReturn(true);
        }
        for (Cast cast : film.getCasts()) {
            when(castMock.addCast(cast)).thenReturn(true);
            when(castMock.addCastToFilm(cast, film.getId())).thenReturn(true);
        }
        Assert.assertFalse(filmService.addFilm(film));
    }

    @Test
    public void addFilmOptimizationError_Fail() {
        when(filmMock.addFilm(film)).thenReturn(false);
        for (Genre genre : film.getGenres()) {
            when(filmMock.addGenreToFilm(genre, film.getId())).thenReturn(true);
        }
        for (Cast cast : film.getCasts()) {
            when(castMock.addCast(cast)).thenReturn(false);
            when(castMock.addCastToFilm(cast, film.getId())).thenReturn(true);
        }
        Assert.assertFalse(filmService.addFilm(film));
    }

    @Test
    public void editFilmWGenresAndCasts_Success() {
        film.addCast(cast);
        film.addGeners(Genre.DRAMA);
        when(filmMock.editFilm(film)).thenReturn(true);
        when(filmMock.resetRelationGenres(film)).thenReturn(true);
        when(filmMock.resetRelationCasts(film)).thenReturn(true);
        for (Genre genre : film.getGenres()) {
            when(filmMock.addGenreToFilm(genre, film.getId())).thenReturn(true);
        }
        for (Cast cast : film.getCasts()) {
            when(castMock.addCast(cast)).thenReturn(true);
            when(castMock.addCastToFilm(cast, film.getId())).thenReturn(true);
        }
        Assert.assertTrue(filmService.editFilm(film));
    }

    @Test
    public void editFilmWOGenresAndCasts_Success() {
        when(filmMock.editFilm(film)).thenReturn(true);
        Assert.assertTrue(filmService.editFilm(film));
    }

    @Test
    public void editFilmInsertionError_Fail() {
        when(filmMock.editFilm(film)).thenReturn(false);
        for (Genre genre : film.getGenres()) {
            when(filmMock.addGenreToFilm(genre, film.getId())).thenReturn(true);
        }
        for (Cast cast : film.getCasts()) {
            when(castMock.addCast(cast)).thenReturn(true);
            when(castMock.addCastToFilm(cast, film.getId())).thenReturn(true);
        }
        Assert.assertFalse(filmService.editFilm(film));
    }

    @Test
    public void editFilmOptimizationError_Fail() {
        film.addCast(cast);
        film.addGeners(Genre.DRAMA);
        when(filmMock.editFilm(film)).thenReturn(false);
        for (Genre genre : film.getGenres()) {
            when(filmMock.addGenreToFilm(genre, film.getId())).thenReturn(true);
        }
        for (Cast cast : film.getCasts()) {
            when(castMock.addCast(cast)).thenReturn(false);
            when(castMock.addCastToFilm(cast, film.getId())).thenReturn(true);
        }
        Assert.assertFalse(filmService.addFilm(film));
    }

    @Test
    public void getFilmById_Success() {
        when(filmMock.getFilmById(cast.getId())).thenReturn(film);
        Assert.assertEquals(film, filmService.getFilmById(cast.getId()));
    }

    @Test
    public void getFilmById_Fail() {
        when(filmMock.getFilmsByCast(cast.getId())).thenThrow(DaoException.class);
        Assert.assertEquals(null, filmService.getFilmById(cast.getId()));
    }

    @Test
    public void getFilmsByCast_Success() {
        when(filmMock.getFilmsByCast(cast.getId())).thenReturn(films);
        Assert.assertEquals(films, filmService.getFilmsByCast(cast.getId()));
    }

    @Test
    public void getFilmsByCast_Fail() {
        when(filmMock.getFilmsByCast(cast.getId())).thenThrow(DaoException.class);
        Assert.assertEquals(null, filmService.getFilmById(cast.getId()));
    }

    @Test
    public void getFilmsList_Success() {
        films.add(film);
        when(filmMock.getFilmsList(0)).thenReturn(films);
        Assert.assertEquals(1, filmService.getFilmsList(0).size());
    }

    @Test
    public void getFilmsList_Fail() {
        when(filmMock.getFilmsList(0)).thenThrow(DaoException.class);
        Assert.assertEquals(null, filmService.getFilmsList(0));
    }

    @Test
    public void getFilmsByGenre_Success() {
        when(filmMock.getFilmsByGenre(Genre.MUSIC)).thenReturn(films);
        Assert.assertEquals(films, filmService.getFilmsByGenre(Genre.MUSIC));
    }

    @Test
    public void getFilmsByGenre_Fail() {
        when(filmMock.getFilmsByGenre(Genre.MUSIC)).thenThrow(DaoException.class);
        Assert.assertEquals(null, filmService.getFilmsByGenre(Genre.MUSIC));
    }

    @Test
    public void rateFilm_Success() {
        when(filmMock.rateFilm(film.getId(), 5)).thenReturn(true);
        Assert.assertTrue(filmService.rateFilm(film.getId(), 5));
    }

    @Test
    public void rateFilm_Fail() {
        when(filmMock.rateFilm(film.getId(), 0)).thenThrow(DaoException.class);
        Assert.assertFalse(filmService.rateFilm(film.getId(), 0));
    }

    @Test
    public void addGenreToFilm_Sucess() {
        when(filmMock.addGenreToFilm(Genre.DRAMA, film.getId())).thenReturn(true);
        Assert.assertTrue(filmService.addGenreToFilm(Genre.DRAMA, film.getId()));
    }

    @Test
    public void addGenreToFilm_Fail() {
        when(filmMock.addGenreToFilm(Genre.MUSICAL, film.getId())).thenThrow(DaoException.class);
        Assert.assertFalse(filmService.addGenreToFilm(Genre.MUSICAL, film.getId()));
    }

    @Test
    public void getRating_Success() {
        when(filmMock.getRating(film.getId())).thenReturn(4.3);
        Assert.assertEquals(4.3, filmService.getRating(film.getId()), 0.01);
    }

    @Test
    public void getRating_Fail() {
        when(filmMock.getRating(film.getId())).thenThrow(DaoException.class);
        Assert.assertEquals(0.0, filmService.getRating(film.getId()), 0.01);
    }

    @Test
    public void totalNumber_Success() {
        when(filmMock.totalNumberOfFilms()).thenReturn(100);
        Assert.assertEquals(100, filmService.totalNumberOfFilms());
    }

    @Test
    public void totalNumber_Fail() {
        when(filmMock.totalNumberOfFilms()).thenThrow(DaoException.class);
        Assert.assertEquals(0, filmService.totalNumberOfFilms());
    }

    @Test
    public void filterByEverything_Success() {
        films.add(film);
        System.out.println(cast.getId());
        when(filmMock.getFilteredFilms("test", 1000, 3000, "%", "testDir", "0", "1")).thenReturn(films);
        Assert.assertEquals(films, filmService.getFilteredFilms("test", 1000, 3000, false, "testDir", cast.getId(), Genre.FAMILY));
    }
}
