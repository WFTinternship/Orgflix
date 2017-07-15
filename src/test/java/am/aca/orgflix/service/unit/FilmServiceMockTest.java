package am.aca.orgflix.service.unit;

import am.aca.orgflix.BaseUnitTest;
import am.aca.orgflix.dao.FilmDAO;
import am.aca.orgflix.entity.Cast;
import am.aca.orgflix.entity.Film;
import am.aca.orgflix.entity.Genre;
import am.aca.orgflix.service.FilmService;
import am.aca.orgflix.service.ServiceException;
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
        ReflectionTestUtils.setField(filmService, "filmDao", filmDaoMock);
    }

    /**
     * Assures no more methods of DAO mocks are invoked
     */
    @After
    public void tearDown() {
        verifyNoMoreInteractions(filmDaoMock);
    }

    /**
     * @see FilmServiceImpl#addFilm(am.aca.orgflix.entity.Film)
     */
    @Test
    public void addFilm_WithGenresAndCasts_Success() {
        film.addCast(cast);
        film.addGeners(Genre.DRAMA);

        when(filmDaoMock.addFilm(film)).thenReturn(true);

        boolean status = filmService.addFilm(film);
        Assert.assertTrue(status);

        verify(filmDaoMock, times(1)).addFilm(film);
    }

    /**
     * @see FilmServiceImpl#addFilm(am.aca.orgflix.entity.Film)
     */
    @Test
    public void addFilm_WithoutGenresAndCasts_Success() {
        when(filmDaoMock.addFilm(film)).thenReturn(true);

        boolean status = filmService.addFilm(film);
        Assert.assertTrue(status);

        verify(filmDaoMock, times(1)).addFilm(film);
    }

    /**
     * @see FilmServiceImpl#addFilm(am.aca.orgflix.entity.Film)
     */
    @Test(expected = ServiceException.class)
    public void addFilm_EmptyTitle_Fail() {
        Film badFilm = new Film("", 2000);
        filmService.addFilm(badFilm);
    }

    /**
     * @see FilmServiceImpl#addFilm(am.aca.orgflix.entity.Film)
     */
    @Test(expected = ServiceException.class)
    public void addFilm_NullTitle_Fail() {
        Film badFilm = new Film(null, 2000);
        filmService.addFilm(badFilm);
    }

    /**
     * @see FilmServiceImpl#addFilm(am.aca.orgflix.entity.Film)
     */
    @Test(expected = ServiceException.class)
    public void addFilm_YearTooOld_Fail() {
        Film badFilm = new Film("Old", 1256);
        filmService.addFilm(badFilm);
    }

    /**
     * @see FilmServiceImpl#addFilm(am.aca.orgflix.entity.Film)
     */
    @Test(expected = ServiceException.class)
    public void addFilm_YearTooRecent_Fail() {
        Film badFilm = new Film("Film from the Future", 12560);
        filmService.addFilm(badFilm);
    }

    /**
     * @see FilmServiceImpl#editFilm(am.aca.orgflix.entity.Film)
     */
    @Test
    public void editFilm_WithGenresAndCasts_Success() {
        film.addCast(cast);
        film.addGeners(Genre.DRAMA);

        when(filmDaoMock.editFilm(film)).thenReturn(true);

        boolean status = filmService.editFilm(film);
        Assert.assertTrue(status);

        verify(filmDaoMock, times(1)).editFilm(film);
    }

    /**
     * @see FilmServiceImpl#editFilm(am.aca.orgflix.entity.Film)
     */
    @Test(expected = ServiceException.class)
    public void EditFilm_EmptyName_Fail() {
        Film badFilm = new Film("", 2000);
        filmService.editFilm(badFilm);
    }

    /**
     * @see FilmServiceImpl#editFilm(am.aca.orgflix.entity.Film)
     */
    @Test(expected = ServiceException.class)
    public void EditFilm_NullName_Fail() {
        Film badFilm = new Film(null, 2000);
        filmService.editFilm(badFilm);
    }

    /**
     * @see FilmServiceImpl#editFilm(am.aca.orgflix.entity.Film)
     */
    @Test(expected = ServiceException.class)
    public void EditFilm_YearTooOld_Fail() {
        Film badFilm = new Film("Old", 1000);
        filmService.editFilm(badFilm);
    }

    /**
     * @see FilmServiceImpl#editFilm(am.aca.orgflix.entity.Film)
     */
    @Test(expected = ServiceException.class)
    public void EditFilm_YearTooRecent_Fail() {
        Film badFilm = new Film("", 2000);
        filmService.editFilm(badFilm);
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
        List<Film> emptyList = new ArrayList<>();
        when(filmDaoMock.getFilmsByCast(film.getId())).thenReturn(emptyList);

        Film actualFilm = filmService.getFilmById(film.getId());
        Assert.assertEquals(emptyList, actualFilm);

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
        List<Film> emptyList = new ArrayList<>();
        when(filmDaoMock.getFilmsByCast(cast.getId())).thenReturn(emptyList);

        List<Film> actualFilms = filmService.getFilmsByCast(cast.getId());

        Assert.assertEquals(emptyList, actualFilms);
        verify(filmDaoMock, times(1)).getFilmsByCast(cast.getId());

    }

    /**
     * @see FilmServiceImpl#getFilmsList(int)
     */
    @Test
    public void getFilmsList_Success() {
        films.add(film);

        when(filmDaoMock.getFilmsList(0)).thenReturn(films);
        when(filmDaoMock.getCastsByFilm(film.getId())).thenReturn(casts);

        int size = filmService.getFilmsList(0).size();
        Assert.assertEquals(1, size);

        verify(filmDaoMock, times(1)).getFilmsList(0);
        verify(filmDaoMock, times(1)).getCastsByFilm(film.getId());
    }

    /**
     * @see FilmServiceImpl#getFilmsList(int)
     */
    @Test
    public void getFilmsList_Fail() {
        List<Film> emptyList = new ArrayList<>();
        when(filmDaoMock.getFilmsList(0)).thenReturn(emptyList);

        try {
            filmService.getFilmsList(0);
        } catch (RuntimeException e) {
            verify(filmDaoMock, times(1)).getFilmsList(0);
        }
    }

//    /**
//     * @see FilmServiceImpl#getFilmsByGenre(am.aca.orgflix.entity.Genre)
//     */
//    @Test
//    public void getFilmsByGenre_Success() {
//        when(filmDaoMock.getFilmsByGenre(Genre.MUSIC)).thenReturn(films);
//
//        List<Film> actualFilms = filmService.getFilmsByGenre(Genre.MUSIC);
//        Assert.assertEquals(films, actualFilms);
//
//        verify(filmDaoMock, times(1)).getFilmsByGenre(Genre.MUSIC);
//    }
//
//    /**
//     * @see FilmServiceImpl#getFilmsByGenre(am.aca.orgflix.entity.Genre)
//     */
//    @Test
//    public void getFilmsByGenre_Fail() {
//        when(filmDaoMock.getFilmsByGenre(Genre.MUSIC)).thenThrow(DaoException.class);
//
//        try {
//            filmService.getFilmsByGenre(Genre.MUSIC);
//        } catch (RuntimeException e) {
//            verify(filmDaoMock, times(1)).getFilmsByGenre(Genre.MUSIC);
//        }
//    }

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
        when(filmDaoMock.rateFilm(film.getId(), 0)).thenReturn(false);

        boolean status = filmService.rateFilm(film.getId(), 5);
        Assert.assertFalse(status);

        verify(filmDaoMock, times(1)).rateFilm(film.getId(), 5);
    }

//    /**
//     * @see FilmServiceImpl#addGenreToFilm(am.aca.orgflix.entity.Genre, int)
//     */
//    @Test
//    public void addGenreToFilm_Success() {
//        when(filmDaoMock.addGenreToFilm(Genre.DRAMA, film.getId())).thenReturn(true);
//
//        boolean status = filmService.addGenreToFilm(Genre.DRAMA, film.getId());
//        Assert.assertTrue(status);
//
//        verify(filmDaoMock, times(1)).addGenreToFilm(Genre.DRAMA, film.getId());
//    }
//
//    /**
//     * @see FilmServiceImpl#addGenreToFilm(am.aca.orgflix.entity.Genre, int)
//     */
//    @Test
//    public void addGenreToFilm_Fail() {
//        when(filmDaoMock.addGenreToFilm(Genre.MUSICAL, film.getId())).thenThrow(DaoException.class);
//
//        boolean status = filmService.addGenreToFilm(Genre.DRAMA, film.getId());
//        Assert.assertFalse(status);
//
//        verify(filmDaoMock, times(1)).addGenreToFilm(Genre.DRAMA, film.getId());
//    }

    /**
     * @see FilmServiceImpl#getRating(int)
     */
    @Test
    public void getRating_Success() {
        when(filmDaoMock.getRating1(film.getId())).thenReturn(0);
        when(filmDaoMock.getRating2(film.getId())).thenReturn(1);
        when(filmDaoMock.getRating3(film.getId())).thenReturn(8);
        when(filmDaoMock.getRating4(film.getId())).thenReturn(20);
        when(filmDaoMock.getRating5(film.getId())).thenReturn(10);

        double actualRating = filmService.getRating(film.getId());
        Assert.assertEquals(4, actualRating, 0.01);

        verify(filmDaoMock, times(1)).getRating1(film.getId());
        verify(filmDaoMock, times(1)).getRating2(film.getId());
        verify(filmDaoMock, times(1)).getRating3(film.getId());
        verify(filmDaoMock, times(1)).getRating4(film.getId());
        verify(filmDaoMock, times(1)).getRating5(film.getId());
    }

    /**
     * @see FilmServiceImpl#getRating(int)
     */
    @Test
    public void getRating_Fail() {
        when(filmDaoMock.getRating1(film.getId())).thenReturn(0);
        when(filmDaoMock.getRating2(film.getId())).thenReturn(0);
        when(filmDaoMock.getRating3(film.getId())).thenReturn(0);
        when(filmDaoMock.getRating4(film.getId())).thenReturn(0);
        when(filmDaoMock.getRating5(film.getId())).thenReturn(0);

        double actualRating = filmService.getRating(film.getId());
        Assert.assertEquals(0, actualRating, 0.01);

        verify(filmDaoMock, times(1)).getRating1(film.getId());
        verify(filmDaoMock, times(1)).getRating2(film.getId());
        verify(filmDaoMock, times(1)).getRating3(film.getId());
        verify(filmDaoMock, times(1)).getRating4(film.getId());
        verify(filmDaoMock, times(1)).getRating5(film.getId());
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
        when(filmDaoMock.totalNumberOfFilms()).thenReturn(0);

        int size = filmService.totalNumberOfFilms();
        Assert.assertEquals(0, size);

        verify(filmDaoMock, times(1)).totalNumberOfFilms();
    }

    /**
     * @see FilmServiceImpl#getFilteredFilms(String, int, int, boolean, String, int, int)
     */
    @Test
    public void filter_ByEverything_Success() {
        films.add(film);

        when(filmDaoMock.getFilteredFilms("", 1888, 2025, false, "", 0, 0)).thenReturn(films);

        List<Film> actualFilms = filmService.getFilteredFilms("", 1888, 2025, false, "", 0, 0);
        Assert.assertEquals(films, actualFilms);

        verify(filmDaoMock, times(1))
                .getFilteredFilms("", 1888, 2025, false, "", 0, 0);
    }

    /**
     * @see FilmServiceImpl#getFilteredFilms(String, int, int, boolean, String, int, int)
     */

    @Test
    public void filter_ByEverything_Fail() {
        List<Film> emptyList = new ArrayList<>();
        when(filmDaoMock.getFilteredFilms("", 1888, 2025, false, "", 0, 0)).thenReturn(emptyList);
        List<Film> actualFilms = filmService.getFilteredFilms("", 1888, 2025, false, "", 0, 0);

        Assert.assertNotEquals(emptyList, actualFilms);
        verify(filmDaoMock, times(1)).getFilteredFilms("", 1888, 2025, false, "", 0, 0);
    }
}

