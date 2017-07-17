package am.aca.orgflix.service.unit;

import am.aca.orgflix.BaseUnitTest;
import am.aca.orgflix.dao.CastDAO;
import am.aca.orgflix.dao.DaoException;
import am.aca.orgflix.dao.FilmDAO;
import am.aca.orgflix.entity.Cast;
import am.aca.orgflix.entity.Film;
import am.aca.orgflix.entity.Genre;
import am.aca.orgflix.service.FilmService;
import am.aca.orgflix.service.impl.FilmServiceImpl;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Unit tests for Film Service layer
 */
public class FilmServiceMockTest extends BaseUnitTest {

    @Autowired
    private FilmService filmService;

    @Mock
    private FilmDAO filmDaoMock;
    @Mock
    private CastDAO castDaoMock;

    private Cast cast = new Cast("Cate Blanchett", true);
    private Film film = new Film("Babel", 2006);
    private List<Film> films = new ArrayList<>();
    private List<Cast> casts = new ArrayList<>();

    /**
     * Configures Mockito
     */
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(filmService, "castDao", castDaoMock);
        ReflectionTestUtils.setField(filmService, "filmDao", filmDaoMock);
    }

    /**
     * Assures no more methods of DAO mocks are invoked
     */
    @After
    public void tearDown() {
        verifyNoMoreInteractions(filmDaoMock);
        verifyNoMoreInteractions(castDaoMock);
    }

    /**
     * @see FilmServiceImpl#addFilm(am.aca.orgflix.entity.Film)
     */
    @Test
    public void addFilm_WGenresAndCasts_Success() {
        film.addCast(cast);
        film.addGeners(Genre.DRAMA);

        when(filmDaoMock.addFilm(film)).thenReturn(true);

        for (Genre genre : film.getGenres()) {
            when(filmDaoMock.addGenreToFilm(genre, film.getId())).thenReturn(true);
        }

        for (Cast cast : film.getCasts()) {
            when(castDaoMock.addCast(cast)).thenReturn(true);
            when(castDaoMock.addCastToFilm(cast, film.getId())).thenReturn(true);
        }

        boolean status = filmService.addFilm(film);
        Assert.assertTrue(status);

        verify(filmDaoMock, times(1)).addFilm(film);
        verify(filmDaoMock, times(1)).addGenreToFilm(Genre.DRAMA, film.getId());
        verify(castDaoMock, times(1)).addCast(cast);
        verify(castDaoMock, times(1)).addCastToFilm(cast, film.getId());
    }

    /**
     * @see FilmServiceImpl#addFilm(am.aca.orgflix.entity.Film)
     */
    @Test
    public void addFilm_WOGenresAndCasts_Success() {
        when(filmDaoMock.addFilm(film)).thenReturn(true);

        boolean status = filmService.addFilm(film);
        Assert.assertTrue(status);

        verify(filmDaoMock, times(1)).addFilm(film);
    }

    /**
     * @see FilmServiceImpl#addFilm(am.aca.orgflix.entity.Film)
     */
    @Test
    public void addFilm_InsertionError_Fail() {
        when(filmDaoMock.addFilm(film)).thenReturn(false);

        boolean status = filmService.addFilm(film);
        Assert.assertFalse(status);

        verify(filmDaoMock, times(1)).addFilm(film);
    }

    /**
     * @see FilmServiceImpl#addFilm(am.aca.orgflix.entity.Film)
     */
    @Test
    public void addFilm_OptimizationError_Fail() {
        film.addCast(cast);

        when(filmDaoMock.addFilm(film)).thenReturn(true);

        for (Cast cast : film.getCasts()) {
            when(castDaoMock.addCast(cast)).thenReturn(false);
            when(castDaoMock.addCastToFilm(cast, film.getId())).thenReturn(true);
        }

        boolean status = filmService.addFilm(film);
        Assert.assertFalse(status);

        verify(filmDaoMock, times(1)).addFilm(film);
        verify(castDaoMock, times(1)).addCast(cast);
    }

    /**
     * @see FilmServiceImpl#editFilm(am.aca.orgflix.entity.Film)
     */
    @Test
    public void editFilm_WGenresAndCasts_Success() {
        film.addCast(cast);
        film.addGeners(Genre.DRAMA);

        when(filmDaoMock.editFilm(film)).thenReturn(true);
        when(filmDaoMock.resetRelationGenres(film)).thenReturn(true);
        when(filmDaoMock.resetRelationCasts(film)).thenReturn(true);

        for (Genre genre : film.getGenres()) {
            when(filmDaoMock.addGenreToFilm(genre, film.getId())).thenReturn(true);
        }

        for (Cast cast : film.getCasts()) {
            when(castDaoMock.addCast(cast)).thenReturn(true);
            when(castDaoMock.addCastToFilm(cast, film.getId())).thenReturn(true);
        }

        boolean status = filmService.editFilm(film);
        Assert.assertTrue(status);

        verify(filmDaoMock, times(1)).editFilm(film);
        verify(filmDaoMock, times(1)).resetRelationGenres(film);
        verify(filmDaoMock, times(1)).resetRelationCasts(film);
        verify(filmDaoMock, times(1)).addGenreToFilm(Genre.DRAMA, film.getId());
        verify(castDaoMock, times(1)).addCast(cast);
        verify(castDaoMock, times(1)).addCastToFilm(cast, film.getId());
    }

    /**
     * @see FilmServiceImpl#editFilm(am.aca.orgflix.entity.Film)
     */
    @Test
    public void editFilm_WOGenresAndCasts_Success() {
        when(filmDaoMock.editFilm(film)).thenReturn(true);
        when(filmDaoMock.resetRelationGenres(film)).thenReturn(true);
        when(filmDaoMock.resetRelationCasts(film)).thenReturn(true);

        boolean status = filmService.editFilm(film);
        Assert.assertTrue(status);

        verify(filmDaoMock, times(1)).editFilm(film);
        verify(filmDaoMock, times(1)).resetRelationGenres(film);
        verify(filmDaoMock, times(1)).resetRelationCasts(film);
    }

    /**
     * @see FilmServiceImpl#editFilm(am.aca.orgflix.entity.Film)
     */
    @Test
    public void editFilm_InsertionError_Fail() {
        when(filmDaoMock.editFilm(film)).thenReturn(false);

        boolean status = filmService.editFilm(film);
        Assert.assertFalse(status);

        verify(filmDaoMock, times(1)).editFilm(film);
    }

    /**
     * @see FilmServiceImpl#editFilm(am.aca.orgflix.entity.Film)
     */
    @Test
    public void editFilm_OptimizationError_Fail() {
        film.addCast(cast);
        film.addGeners(Genre.DRAMA);

        when(filmDaoMock.editFilm(film)).thenReturn(true);
        when(filmDaoMock.resetRelationCasts(film)).thenReturn(true);
        when(filmDaoMock.resetRelationGenres(film)).thenReturn(true);

        for (Genre genre : film.getGenres()) {
            when(filmDaoMock.addGenreToFilm(genre, film.getId())).thenReturn(true);
        }

        for (Cast cast : film.getCasts()) {
            when(castDaoMock.addCast(cast)).thenReturn(false);
            when(castDaoMock.addCastToFilm(cast, film.getId())).thenReturn(true);
        }

        boolean status = filmService.editFilm(film);
        Assert.assertFalse(status);

        verify(filmDaoMock, times(1)).editFilm(film);
        verify(filmDaoMock, times(1)).resetRelationCasts(film);
        verify(filmDaoMock, times(1)).resetRelationGenres(film);
        verify(filmDaoMock, times(1)).addGenreToFilm(Genre.DRAMA, film.getId());
        verify(castDaoMock, times(1)).addCast(cast);
    }

    /**
     * @see FilmServiceImpl#getFilmById(int)
     */
    @Test
    public void getFilmById_Success() {
        when(filmDaoMock.getFilmById(film.getId())).thenReturn(film);

        Film actualFilm = filmService.getFilmById(film.getId());
        Assert.assertEquals(film, actualFilm);

        verify(filmDaoMock, times(1)).getFilmById(film.getId());
    }

    /**
     * @see FilmServiceImpl#getFilmById(int)
     */
    @Test
    public void getFilmById_Fail() {
        when(filmDaoMock.getFilmsByCast(film.getId())).thenThrow(DaoException.class);

        Film actualFilm = filmService.getFilmById(film.getId());
        Assert.assertEquals(null, actualFilm);

        verify(filmDaoMock, times(1)).getFilmById(film.getId());
    }

    /**
     * @see FilmServiceImpl#getFilmsByCast(int)
     */
    @Test
    public void getFilmsByCast_Success() {
        when(filmDaoMock.getFilmsByCast(cast.getId())).thenReturn(films);

        List<Film> actualFilms = filmService.getFilmsByCast(cast.getId());
        Assert.assertEquals(films, actualFilms);

        verify(filmDaoMock, times(1)).getFilmsByCast(cast.getId());
    }

    /**
     * @see FilmServiceImpl#getFilmsByCast(int)
     */
    @Test
    public void getFilmsByCast_Fail() {
        when(filmDaoMock.getFilmsByCast(cast.getId())).thenThrow(DaoException.class);

        try {
            filmService.getFilmsByCast(cast.getId());
        } catch (RuntimeException e) {
            verify(filmDaoMock, times(1)).getFilmsByCast(cast.getId());
        }

    }

    /**
     * @see FilmServiceImpl#getFilmsList(int)
     */
    @Test
    public void getFilmsList_Success() {
        films.add(film);

        when(filmDaoMock.getFilmsList(0, 12)).thenReturn(films);
        when(castDaoMock.getCastsByFilm(film.getId())).thenReturn(casts);

        int size = filmService.getFilmsList(0).size();
        Assert.assertEquals(1, size);

        verify(filmDaoMock, times(1)).getFilmsList(0, 12);
        verify(castDaoMock, times(1)).getCastsByFilm(film.getId());
    }

    /**
     * @see FilmServiceImpl#getFilmsList(int)
     */
    @Test
    public void getFilmsList_Fail() {
        when(filmDaoMock.getFilmsList(0, 12)).thenThrow(DaoException.class);

        try {
            filmService.getFilmsList(0);
        } catch (RuntimeException e) {
            verify(filmDaoMock, times(1)).getFilmsList(0, 12);
        }
    }

    /**
     * @see FilmServiceImpl#getFilmsByGenre(am.aca.orgflix.entity.Genre)
     */
    @Test
    public void getFilmsByGenre_Success() {
        when(filmDaoMock.getFilmsByGenre(Genre.MUSIC)).thenReturn(films);

        List<Film> actualFilms = filmService.getFilmsByGenre(Genre.MUSIC);
        Assert.assertEquals(films, actualFilms);

        verify(filmDaoMock, times(1)).getFilmsByGenre(Genre.MUSIC);
    }

    /**
     * @see FilmServiceImpl#getFilmsByGenre(am.aca.orgflix.entity.Genre)
     */
    @Test
    public void getFilmsByGenre_Fail() {
        when(filmDaoMock.getFilmsByGenre(Genre.MUSIC)).thenThrow(DaoException.class);

        try {
            filmService.getFilmsByGenre(Genre.MUSIC);
        } catch (RuntimeException e) {
            verify(filmDaoMock, times(1)).getFilmsByGenre(Genre.MUSIC);
        }
    }

    /**
     * @see FilmServiceImpl#rateFilm(int, int)
     */
    @Test
    public void rateFilm_Success() {
        when(filmDaoMock.rateFilm(film.getId(), 5)).thenReturn(true);

        boolean status = filmService.rateFilm(film.getId(), 5);
        Assert.assertTrue(status);

        verify(filmDaoMock, times(1)).rateFilm(film.getId(), 5);
    }

    /**
     * @see FilmServiceImpl#rateFilm(int, int)
     */
    @Test
    public void rateFilm_Fail() {
        when(filmDaoMock.rateFilm(film.getId(), 0)).thenThrow(DaoException.class);

        boolean status = filmService.rateFilm(film.getId(), 5);
        Assert.assertFalse(status);

        verify(filmDaoMock, times(1)).rateFilm(film.getId(), 5);
    }

    /**
     * @see FilmServiceImpl#addGenreToFilm(am.aca.orgflix.entity.Genre, int)
     */
    @Test
    public void addGenreToFilm_Success() {
        when(filmDaoMock.addGenreToFilm(Genre.DRAMA, film.getId())).thenReturn(true);

        boolean status = filmService.addGenreToFilm(Genre.DRAMA, film.getId());
        Assert.assertTrue(status);

        verify(filmDaoMock, times(1)).addGenreToFilm(Genre.DRAMA, film.getId());
    }

    /**
     * @see FilmServiceImpl#addGenreToFilm(am.aca.orgflix.entity.Genre, int)
     */
    @Test
    public void addGenreToFilm_Fail() {
        when(filmDaoMock.addGenreToFilm(Genre.MUSICAL, film.getId())).thenThrow(DaoException.class);

        boolean status = filmService.addGenreToFilm(Genre.DRAMA, film.getId());
        Assert.assertFalse(status);

        verify(filmDaoMock, times(1)).addGenreToFilm(Genre.DRAMA, film.getId());
    }

    /**
     * @see FilmServiceImpl#getRating(int)
     */
    @Test
    public void getRating_Success() {
        when(filmDaoMock.getRating(film.getId())).thenReturn(4.3);

        double actualRating = filmService.getRating(film.getId());
        Assert.assertEquals(4.3, actualRating, 0.01);

        verify(filmDaoMock, times(1)).getRating(film.getId());
    }

    /**
     * @see FilmServiceImpl#getRating(int)
     */
    @Test
    public void getRating_Fail() {
        when(filmDaoMock.getRating(film.getId())).thenThrow(DaoException.class);

        double actualRating = filmService.getRating(film.getId());
        Assert.assertEquals(0.0, actualRating, 0.01);

        verify(filmDaoMock, times(1)).getRating(film.getId());
    }

    /**
     * @see FilmServiceImpl#totalNumberOfFilms()
     */
    @Test
    public void totalNumber_Success() {
        when(filmDaoMock.totalNumberOfFilms()).thenReturn(100);

        int size = filmService.totalNumberOfFilms();
        Assert.assertEquals(100, size);

        verify(filmDaoMock, times(1)).totalNumberOfFilms();
    }

    /**
     * @see FilmServiceImpl#totalNumberOfFilms()
     */
    @Test
    public void totalNumber_Fail() {
        when(filmDaoMock.totalNumberOfFilms()).thenThrow(DaoException.class);

        int size = filmService.totalNumberOfFilms();
        Assert.assertEquals(0, size);

        verify(filmDaoMock, times(1)).totalNumberOfFilms();
    }

//    /**
//     * @see FilmServiceImpl#getFilteredFilms(java.lang.String, int, int, boolean, java.lang.String, int, am.aca.orgflix.entity.Genre)
//     */
//    @Test
//    public void filter_ByEverything_Success() {
//        films.add(film);
//
//        when(filmDaoMock.getFilteredFilms("test", 1000, 3000, "%", "testDir", "0", "1")).thenReturn(films);
//
//        List<Film> actualFilms = filmService.getFilteredFilms("test", 1000, 3000, false, "testDir", cast.getId(), Genre.FAMILY);
//        Assert.assertEquals(films, actualFilms);
//
//        verify(filmDaoMock, times(1)).getFilteredFilms("test", 1000, 3000, "%", "testDir", "0", "1");
//    }
//
//    /**
//     * @see FilmServiceImpl#getFilteredFilms(java.lang.String, int, int, boolean, java.lang.String, int, am.aca.orgflix.entity.Genre)
//     */
//
//    @Test
//    public void filter_ByEverything_Fail() {
//        when(filmDaoMock.getFilteredFilms("test", 1000, 3000, "%", "testDir", "0", "1")).thenThrow(DaoException.class);
//
//        try {
//            List<Film> actualFilms = filmService.getFilteredFilms("test", 1000, 3000, false, "testDir", cast.getId(), Genre.FAMILY);
//        } catch (ServiceException e) {
//            verify(filmDaoMock, times(1)).getFilteredFilms("test", 1000, 3000, "%", "testDir", "0", "1");
//        }
//    }
}
