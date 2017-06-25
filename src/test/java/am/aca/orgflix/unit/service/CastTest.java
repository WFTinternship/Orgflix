package am.aca.orgflix.unit.service;

import am.aca.dao.jdbc.impljdbc.JdbcCastDAO;
import am.aca.entity.Cast;
import am.aca.entity.Film;
import am.aca.orgflix.BaseUnitTest;
import am.aca.service.CastService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import static org.mockito.Mockito.when;

/**
 * Created by karine on 6/24/2017
 */
public class CastTest extends BaseUnitTest {

    @Autowired
    @Qualifier("castServiceImpl")
    @InjectMocks
    private CastService castService;

    @Mock
    private JdbcCastDAO mock;
    private Cast cast = new Cast("Cate Blanchett", true);
    private Film film = new Film("Babel", 2006);

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void addCastToFilm_Success() {
        when(mock.isStarringIn(cast.getId(), film.getId())).thenReturn(false);
        when(mock.addCastToFilm(cast, film.getId())).thenReturn(true);
        Assert.assertTrue(castService.addCastToFilm(cast, film));
    }

    @Test
    public void addCastToFilmAlreadyStarring_Fail() {
        when(mock.isStarringIn(cast.getId(), film.getId())).thenReturn(true);
        Assert.assertFalse(castService.addCastToFilm(cast, film));
    }

    @Test
    public void addCastToFilmInsertionError_Fail() {
        when(mock.isStarringIn(cast.getId(), film.getId())).thenReturn(true);
        when(mock.addCastToFilm(cast, film)).thenReturn(false);
        Assert.assertFalse(castService.addCastToFilm(cast, film));
    }

    @Test
    public void editCast_Success() {
        when(mock.exists(cast)).thenReturn(true);
        when(mock.editCast(cast)).thenReturn(true);
        Assert.assertTrue(castService.editCast(cast));
    }

    @Test
    public void editCastNonExisting_Fail() {
        when(mock.exists(cast)).thenReturn(true);
        Assert.assertFalse(castService.editCast(cast));
    }

    @Test
    public void editCastUpdateError_Fail() {
        when(mock.exists(cast)).thenReturn(true);
        when(mock.editCast(cast)).thenReturn(false);
        Assert.assertFalse(castService.editCast(cast));
    }
}
