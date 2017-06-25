package am.aca.orgflix.unit.service;

import am.aca.dao.jdbc.impljdbc.JdbcCastDAO;
import am.aca.dao.jdbc.impljdbc.JdbcFilmDAO;
import am.aca.entity.Cast;
import am.aca.entity.Film;
import am.aca.entity.Genre;
import am.aca.orgflix.BaseUnitTest;
import am.aca.service.FilmService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import static org.mockito.Mockito.when;

/**
 * Created by karine on 6/25/2017
 */
public class FilmTest extends BaseUnitTest {

    @Autowired
    @InjectMocks
    @Qualifier("filmServiceImpl")
    private FilmService filmService;

    @Mock
    private JdbcFilmDAO filmMock;
    @Mock
    private JdbcCastDAO castMock;
    private Cast cast;
    private Film film;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        film = new Film("Babel", 2006);
        cast = new Cast("Cate Blanchett", true);
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
            when(castMock.addCastToFilm(cast, film)).thenReturn(true);
        }
        Assert.assertTrue(filmService.addFilm(film));
    }

    @Test
    public void addFilmWOGenresAndCasts_Success() {
        when(filmMock.addFilm(film)).thenReturn(true);
        for (Genre genre : film.getGenres()) {
            when(filmMock.addGenreToFilm(genre, film)).thenReturn(true);
        }
        for (Cast cast : film.getCasts()) {
            when(castMock.addCast(cast)).thenReturn(true);
            when(castMock.addCastToFilm(cast, film)).thenReturn(true);
        }
        Assert.assertTrue(filmService.addFilm(film));
    }

    @Test
    public void addFilmInsertionError_Fail() {
        when(filmMock.addFilm(film)).thenReturn(false);
        for (Genre genre : film.getGenres()) {
            when(filmMock.addGenreToFilm(genre, film)).thenReturn(true);
        }
        for (Cast cast : film.getCasts()) {
            when(castMock.addCast(cast)).thenReturn(true);
            when(castMock.addCastToFilm(cast, film)).thenReturn(true);
        }
        Assert.assertFalse(filmService.addFilm(film));
    }

    @Test
    public void addFilmOptimizationError_Fail() {
        when(filmMock.addFilm(film)).thenReturn(false);
        for (Genre genre : film.getGenres()) {
            when(filmMock.addGenreToFilm(genre, film)).thenReturn(true);
        }
        for (Cast cast : film.getCasts()) {
            when(castMock.addCast(cast)).thenReturn(false);
            when(castMock.addCastToFilm(cast, film)).thenReturn(true);
        }
        Assert.assertFalse(filmService.addFilm(film));
    }

    @Ignore
    @Test
    public void addFilmResetError_Fail() {
        when(filmMock.addFilm(film)).thenReturn(true);
//        when(filmMock.resetRelationCasts(film))
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
            when(castMock.addCastToFilm(cast, film)).thenReturn(true);
        }
        Assert.assertTrue(filmService.editFilm(film));
    }

    @Test
    public void editFilmWOGenresAndCasts_Success() {
        when(filmMock.editFilm(film)).thenReturn(true);
        for (Genre genre : film.getGenres()) {
            when(filmMock.addGenreToFilm(genre, film)).thenReturn(true);
        }
        for (Cast cast : film.getCasts()) {
            when(castMock.addCast(cast)).thenReturn(true);
            when(castMock.addCastToFilm(cast, film)).thenReturn(true);
        }
        Assert.assertTrue(filmService.editFilm(film));
    }

    @Test
    public void editFilmInsertionError_Fail() {
        when(filmMock.editFilm(film)).thenReturn(false);
        for (Genre genre : film.getGenres()) {
            when(filmMock.addGenreToFilm(genre, film)).thenReturn(true);
        }
        for (Cast cast : film.getCasts()) {
            when(castMock.addCast(cast)).thenReturn(true);
            when(castMock.addCastToFilm(cast, film)).thenReturn(true);
        }
        Assert.assertFalse(filmService.editFilm(film));
    }

    @Test
    public void editFilmOptimizationError_Fail() {
        film.addCast(cast);
        film.addGeners(Genre.DRAMA);
        when(filmMock.editFilm(film)).thenReturn(false);
        for (Genre genre : film.getGenres()) {
            when(filmMock.addGenreToFilm(genre, film)).thenReturn(true);
        }
        for (Cast cast : film.getCasts()) {
            when(castMock.addCast(cast)).thenReturn(false);
            when(castMock.addCastToFilm(cast, film)).thenReturn(true);
        }
        Assert.assertFalse(filmService.addFilm(film));
    }

    @Ignore
    @Test
    public void editFilmResetError_Fail() {
        when(filmMock.editFilm(film)).thenReturn(true);
//        when(filmMock.resetRelationCasts(film)).thenThrow()
    }
}
