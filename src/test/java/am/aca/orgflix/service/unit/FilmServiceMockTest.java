package am.aca.orgflix.service.unit;

import am.aca.orgflix.BaseUnitTest;
import am.aca.orgflix.dao.CastDAO;
import am.aca.orgflix.dao.FilmDAO;
import am.aca.orgflix.entity.Cast;
import am.aca.orgflix.entity.Film;
import am.aca.orgflix.entity.Genre;
import am.aca.orgflix.service.CastService;
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

    @Autowired
    private CastService castService;

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
        ReflectionTestUtils.setField(castService, "castDAO", castDaoMock);
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
     * @see FilmServiceImpl#add(am.aca.orgflix.entity.Film)
     */
    @Test
    public void addFilm_WithGenresAndCasts_Success() {
        film.addCast(cast);
        film.addGeners(Genre.DRAMA);

        when(filmDaoMock.add(film)).thenReturn(true);

        for (Genre genre : film.getGenres()) {
            when(filmDaoMock.addGenreToFilm(genre, film.getId())).thenReturn(true);
        }

        for (Cast cast : film.getCasts()) {
            when(castDaoMock.add(cast)).thenReturn(true);
            when(castDaoMock.isRelatedToFilm(cast.getId(), film.getId())).thenReturn(false);
            when(castDaoMock.addToFilm(cast, film.getId())).thenReturn(true);
        }

        boolean status = filmService.add(film);
        Assert.assertTrue(status);

        verify(filmDaoMock, times(1)).add(film);
        verify(filmDaoMock, times(1)).addGenreToFilm(Genre.DRAMA, film.getId());
        verify(castDaoMock, times(1)).isRelatedToFilm(cast.getId(), film.getId());
        verify(castDaoMock, times(1)).addToFilm(cast, film.getId());
    }

    /**
     * @see FilmServiceImpl#add(am.aca.orgflix.entity.Film)
     */
    @Test
    public void addFilm_WithoutGenresAndCasts_Success() {
        when(filmDaoMock.add(film)).thenReturn(true);

        boolean status = filmService.add(film);
        Assert.assertTrue(status);

        verify(filmDaoMock, times(1)).add(film);
    }

    /**
     * @see FilmServiceImpl#add(am.aca.orgflix.entity.Film)
     */
    @Test
    public void addFilm_InsertionError_Fail() {
        when(filmDaoMock.add(film)).thenReturn(false);

        boolean status = filmService.add(film);
        Assert.assertFalse(status);

        verify(filmDaoMock, times(1)).add(film);
    }

    /**
     * @see FilmServiceImpl#add(am.aca.orgflix.entity.Film)
     */
    @Test
    public void addFilm_OptimizationError_Fail() {
        film.addCast(cast);

        when(filmDaoMock.add(film)).thenReturn(true);

        for (Cast cast : film.getCasts()) {
            when(castDaoMock.isRelatedToFilm(cast.getId(), film.getId())).thenReturn(false);
            when(castDaoMock.addToFilm(cast, film.getId())).thenReturn(false);
        }

        boolean status = filmService.add(film);
        Assert.assertFalse(status);

        verify(filmDaoMock, times(1)).add(film);
        verify(castDaoMock, times(1)).isRelatedToFilm(cast.getId(), film.getId());
        verify(castDaoMock, times(1)).addToFilm(cast, film.getId());
    }

    /**
     * @see FilmServiceImpl#add(am.aca.orgflix.entity.Film)
     */
    @Test
    public void addFilm_RuntimeException_Fail() {
        when(filmDaoMock.add(film)).thenThrow(RuntimeException.class);

        boolean status = filmService.add(film);
        Assert.assertFalse(status);

        verify(filmDaoMock, times(1)).add(film);
    }

    /**
     * @see FilmServiceImpl#add(am.aca.orgflix.entity.Film)
     */
    @Test(expected = ServiceException.class)
    public void addFilm_NameNull_Fail() {
        film = new Film(null, 2000);
        filmService.add(film);
    }

    /**
     * @see FilmServiceImpl#add(am.aca.orgflix.entity.Film)
     */
    @Test(expected = ServiceException.class)
    public void addFilm_NameEmpty_Fail() {
        film = new Film("", 2000);
        filmService.add(film);
    }

    /**
     * @see FilmServiceImpl#add(am.aca.orgflix.entity.Film)
     */
    @Test(expected = ServiceException.class)
    public void addFilm_InvalidYear_Fail() {
        film = new Film("Valid", 68456);
        filmService.add(film);
    }

    /**
     * @see FilmServiceImpl#edit(am.aca.orgflix.entity.Film)
     */
    @Test
    public void editFilm_WithGenresAndCasts_Success() {
        film.addCast(cast);
        film.addGeners(Genre.DRAMA);

        when(filmDaoMock.edit(film)).thenReturn(true);
        when(filmDaoMock.removeGenres(film)).thenReturn(true);
        when(filmDaoMock.removeCasts(film)).thenReturn(true);

        for (Genre genre : film.getGenres()) {
            when(filmDaoMock.addGenreToFilm(genre, film.getId())).thenReturn(true);
        }

        for (Cast cast : film.getCasts()) {
            when(castDaoMock.isRelatedToFilm(cast.getId(), film.getId())).thenReturn(false);
            when(castDaoMock.addToFilm(cast, film.getId())).thenReturn(true);
        }

        boolean status = filmService.edit(film);
        Assert.assertTrue(status);

        verify(filmDaoMock, times(1)).edit(film);
        verify(filmDaoMock, times(1)).removeGenres(film);
        verify(filmDaoMock, times(1)).removeCasts(film);
        verify(filmDaoMock, times(1)).addGenreToFilm(Genre.DRAMA, film.getId());
        verify(castDaoMock, times(1)).isRelatedToFilm(cast.getId(), film.getId());
        verify(castDaoMock, times(1)).addToFilm(cast, film.getId());
    }

    /**
     * @see FilmServiceImpl#edit(am.aca.orgflix.entity.Film)
     */
    @Test
    public void editFilm_WithoutGenresAndCasts_Success() {
        when(filmDaoMock.edit(film)).thenReturn(true);
        when(filmDaoMock.removeGenres(film)).thenReturn(true);
        when(filmDaoMock.removeCasts(film)).thenReturn(true);

        boolean status = filmService.edit(film);
        Assert.assertTrue(status);

        verify(filmDaoMock, times(1)).edit(film);
        verify(filmDaoMock, times(1)).removeGenres(film);
        verify(filmDaoMock, times(1)).removeCasts(film);
    }

    /**
     * @see FilmServiceImpl#edit(am.aca.orgflix.entity.Film)
     */
    @Test
    public void editFilm_InsertionError_Fail() {
        when(filmDaoMock.edit(film)).thenReturn(false);

        boolean status = filmService.edit(film);
        Assert.assertFalse(status);

        verify(filmDaoMock, times(1)).edit(film);
    }

    /**
     * @see FilmServiceImpl#edit(am.aca.orgflix.entity.Film)
     */
    @Test
    public void editFilm_OptimizationError_Fail() {
        film.addCast(cast);
        film.addGeners(Genre.DRAMA);

        when(filmDaoMock.edit(film)).thenReturn(true);
        when(filmDaoMock.removeCasts(film)).thenReturn(true);
        when(filmDaoMock.removeGenres(film)).thenReturn(true);

        for (Genre genre : film.getGenres()) {
            when(filmDaoMock.addGenreToFilm(genre, film.getId())).thenReturn(true);
        }

        for (Cast cast : film.getCasts()) {
            when(castDaoMock.isRelatedToFilm(cast.getId(), film.getId())).thenReturn(false);
            when(castDaoMock.addToFilm(cast, film.getId())).thenReturn(true);
        }

        boolean status = filmService.edit(film);
        Assert.assertTrue(status);

        verify(filmDaoMock, times(1)).edit(film);
        verify(filmDaoMock, times(1)).removeCasts(film);
        verify(filmDaoMock, times(1)).removeGenres(film);
        verify(filmDaoMock, times(1)).addGenreToFilm(Genre.DRAMA, film.getId());
        verify(castDaoMock, times(1)).isRelatedToFilm(cast.getId(), film.getId());
        verify(castDaoMock, times(1)).addToFilm(cast, film.getId());
    }

    /**
     * @see FilmServiceImpl#edit(Film)
     */
    @Test
    public void editFilm_RuntimeException_Fail() {
        when(filmDaoMock.edit(film)).thenThrow(RuntimeException.class);

        boolean status = filmService.edit(film);
        Assert.assertFalse(status);

        verify(filmDaoMock, times(1)).edit(film);
    }

    /**
     * @see FilmServiceImpl#edit(Film)
     */
    @Test(expected = ServiceException.class)
    public void editFilm_NameNull_Fail() {
        film = new Film(null, 2000);
        filmService.edit(film);
    }

    /**
     * @see FilmServiceImpl#edit(Film)
     */
    @Test(expected = ServiceException.class)
    public void editFilm_NameEmpty_Fail() {
        film = new Film("", 2000);
        filmService.edit(film);
    }

    /**
     * @see FilmServiceImpl#edit(Film)
     */
    @Test(expected = ServiceException.class)
    public void editFilm_InvalidYear_Fail() {
        film = new Film("Valid", 68456);
        filmService.edit(film);
    }

    /**
     * @see FilmServiceImpl#getById(int)
     */
    @Test
    public void getFilmById_Success() {
        when(filmDaoMock.getById(film.getId())).thenReturn(film);

        Film actualFilm = filmService.getById(film.getId());
        Assert.assertEquals(film, actualFilm);

        verify(filmDaoMock, times(1)).getById(film.getId());
    }

    /**
     * @see FilmServiceImpl#getById(int)
     */
    @Test
    public void getFilmById_Fail() {
        when(filmDaoMock.getByCast(film.getId())).thenThrow(RuntimeException.class);

        Film actualFilm = filmService.getById(film.getId());
        Assert.assertEquals(null, actualFilm);

        verify(filmDaoMock, times(1)).getById(film.getId());
    }

    /**
     * @see FilmServiceImpl#getByCast(int)
     */
    @Test
    public void getFilmsByCast_Success() {
        when(filmDaoMock.getByCast(cast.getId())).thenReturn(films);

        List<Film> actualFilms = filmService.getByCast(cast.getId());
        Assert.assertEquals(films, actualFilms);

        verify(filmDaoMock, times(1)).getByCast(cast.getId());
    }

    /**
     * @see FilmServiceImpl#getByCast(int)
     */
    @Test
    public void getFilmsByCast_Fail() {
        when(filmDaoMock.getByCast(cast.getId())).thenThrow(RuntimeException.class);

        List<Film> actualFilms = filmService.getByCast(cast.getId());
        Assert.assertTrue(actualFilms.isEmpty());
        verify(filmDaoMock, times(1)).getByCast(cast.getId());
    }

    /**
     * @see FilmServiceImpl#getAll(int, int)
     */
    @Test
    public void getFilmsList_Success() {
        films.add(film);

        when(filmDaoMock.getAll(0, 12)).thenReturn(films);
        when(castDaoMock.getByFilm(film.getId())).thenReturn(casts);

        int size = filmService.getAll(0, 12).size();
        Assert.assertEquals(1, size);

        verify(filmDaoMock, times(1)).getAll(0, 12);
        verify(castDaoMock, times(1)).getByFilm(film.getId());
    }

    /**
     * @see FilmServiceImpl#getAll(int, int)
     */
    @Test
    public void getFilmsList_Fail() {
        when(filmDaoMock.getAll(0, 12)).thenThrow(RuntimeException.class);

        List<Film> actualFilms = filmService.getAll(0, 12);
        Assert.assertTrue(actualFilms.isEmpty());
        verify(filmDaoMock, times(1)).getAll(0, 12);
    }

    /**
     * @see FilmServiceImpl#getByGenre(am.aca.orgflix.entity.Genre)
     */
    @Test
    public void getFilmsByGenre_Success() {
        when(filmDaoMock.getByGenre(Genre.MUSIC)).thenReturn(films);

        List<Film> actualFilms = filmService.getByGenre(Genre.MUSIC);
        Assert.assertEquals(films, actualFilms);

        verify(filmDaoMock, times(1)).getByGenre(Genre.MUSIC);
    }

    /**
     * @see FilmServiceImpl#getByGenre(am.aca.orgflix.entity.Genre)
     */
    @Test
    public void getFilmsByGenre_Fail() {
        when(filmDaoMock.getByGenre(Genre.MUSIC)).thenThrow(RuntimeException.class);

        List<Film> actualFilms = filmService.getByGenre(Genre.MUSIC);
        Assert.assertTrue(actualFilms.isEmpty());
        verify(filmDaoMock, times(1)).getByGenre(Genre.MUSIC);
    }

    /**
     * @see FilmServiceImpl#rate(int, int)
     */
    @Test
    public void rateFilm_Success() {
        when(filmDaoMock.rate(film.getId(), 5)).thenReturn(true);

        boolean status = filmService.rate(film.getId(), 5);
        Assert.assertTrue(status);

        verify(filmDaoMock, times(1)).rate(film.getId(), 5);
    }

    /**
     * @see FilmServiceImpl#rate(int, int)
     */
    @Test
    public void rateFilm_Fail() {
        when(filmDaoMock.rate(film.getId(), 0)).thenThrow(RuntimeException.class);

        boolean status = filmService.rate(film.getId(), 5);
        Assert.assertFalse(status);

        verify(filmDaoMock, times(1)).rate(film.getId(), 5);
    }

    /**
     * @see FilmServiceImpl#getRating(int)
     */
    @Test
    public void getRating_ValidInput_Success() {
        when(filmDaoMock.getRating(film.getId())).thenReturn(new int[]{0, 1, 1, 20, 10});

        double actualRating = filmService.getRating(film.getId());
        Assert.assertEquals(4.2, actualRating, 0.1);

        verify(filmDaoMock, times(1)).getRating(film.getId());
    }

    @Test
    public void getRating_NoRatings_Success() {
        when(filmDaoMock.getRating(film.getId())).thenReturn(new int[]{0, 0, 0, 0, 0});

        double actualRating = filmService.getRating(film.getId());
        Assert.assertEquals(0, actualRating, 0.1);

        verify(filmDaoMock, times(1)).getRating(film.getId());
    }

    /**
     * @see FilmServiceImpl#getRating(int)
     */
    @Test
    public void getRating_Fail() {
        when(filmDaoMock.getRating(film.getId())).thenThrow(RuntimeException.class);

        double actualRating = filmService.getRating(film.getId());
        Assert.assertEquals(0, actualRating, 0.1);

        verify(filmDaoMock, times(1)).getRating(film.getId());
    }

    /**
     * @see FilmServiceImpl#getTotalNumber()
     */
    @Test
    public void totalNumber_Success() {
        when(filmDaoMock.getTotalNumber()).thenReturn(100);

        int size = filmService.getTotalNumber();
        Assert.assertEquals(100, size);

        verify(filmDaoMock, times(1)).getTotalNumber();
    }

    /**
     * @see FilmServiceImpl#getTotalNumber()
     */
    @Test
    public void totalNumber_Fail() {
        when(filmDaoMock.getTotalNumber()).thenThrow(RuntimeException.class);

        int size = filmService.getTotalNumber();
        Assert.assertEquals(0, size);

        verify(filmDaoMock, times(1)).getTotalNumber();
    }

    /**
     * @see FilmServiceImpl#getFilteredFilms(String, int, int, boolean, String, int, int)
     */
    @Test
    public void filterByEverything_ValidInput_Success() {
        when(filmDaoMock.getFilteredFilms("test", 1000, 3000, false, "testDir", 1, 1)).thenReturn(films);

        List<Film> actualFilms = filmService.getFilteredFilms("test", 1000, 3000, false, "testDir", 1, 1);
        Assert.assertEquals(films, actualFilms);

        verify(filmDaoMock, times(1)).getFilteredFilms("test", 1000, 3000, false, "testDir", 1, 1);
    }

    /**
     * @see FilmServiceImpl#getFilteredFilms(String, int, int, boolean, String, int, int)
     */

    @Test
    public void filterByEverything_RuntimeException_Fail() {
        when(filmDaoMock.getFilteredFilms("test", 1000, 3000, false, "testDir", 1, 1)).thenThrow(RuntimeException.class);

        List<Film> actualFilms = filmService.getFilteredFilms("test", 1000, 3000, false, "testDir", 1, 1);
        Assert.assertTrue(actualFilms.isEmpty());

        verify(filmDaoMock, times(1)).getFilteredFilms("test", 1000, 3000, false, "testDir", 1, 1);
    }

    /**
     * @see FilmServiceImpl#getFilteredFilms(String, int, int, boolean, String, int, int)
     */
    @Test
    public void filterByEverything_TitleNull_Success() {
        when(filmDaoMock.getFilteredFilms("", 1000, 3000, false, "testDir", 1, 1)).thenReturn(films);

        List<Film> actualFilms = filmService.getFilteredFilms(null, 1000, 3000, false, "testDir", 1, 1);
        Assert.assertEquals(films, actualFilms);

        verify(filmDaoMock, times(1)).getFilteredFilms("", 1000, 3000, false, "testDir", 1, 1);
    }
}
