package am.aca.orgflix.unit.service;

import am.aca.dao.jdbc.impljdbc.JdbcListDAO;
import am.aca.entity.Film;
import am.aca.entity.User;
import am.aca.orgflix.BaseUnitTest;
import am.aca.service.ListService;
import am.aca.service.impl.FilmServiceImpl;
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
 * Created by karine on 6/25/2017
 */
public class ListTest extends BaseUnitTest {

    @Autowired
    @InjectMocks
    @Qualifier("listServiceImpl")
    private ListService listService;

    @Mock
    private JdbcListDAO listMock;
    @Mock
    private FilmServiceImpl filmMock;
    private Film film;
    private User user;

    @Before
    public void setUp() {
        film = new Film("Babel", 2006);
        user = new User("hulk", "Bruce Banner", "bbanner@avenger.com", "natasha");
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void addToWatchedExisting_Success() {
        when(filmMock.getFilmById(film.getId())).thenReturn(film);
//        when(filmMock.addFilm(film)).thenReturn(true);
        when(listMock.areRelated(film, user.getId())).thenReturn(false);
//        when(listMock.updateWatched(film, user.getId())).thenReturn(false);
        when(listMock.insertWatched(film, user.getId(), true)).thenReturn(true);
        Assert.assertTrue(listService.addToWatched(film, true, user.getId()));
    }

    @Test
    public void addToWatchedNew_Success() {
        when(filmMock.getFilmById(film.getId())).thenReturn(null);
        when(filmMock.addFilm(film)).thenReturn(true);
        when(listMock.areRelated(film, user.getId())).thenReturn(false);
        when(listMock.insertWatched(film, user.getId(), true)).thenReturn(true);
        Assert.assertTrue(listService.addToWatched(film, true, user.getId()));
    }

    @Test
    public void addToWatchedWhenPlanned_Success() {
        when(filmMock.getFilmById(film.getId())).thenReturn(film);
        when(listMock.areRelated(film, user.getId())).thenReturn(true);
        when(listMock.updateWatched(film, user.getId())).thenReturn(true);
        Assert.assertTrue(listService.addToWatched(film, true, user.getId()));
    }

    @Test
    public void addToWatchedWhenPlanned_Fail() {
        when(filmMock.getFilmById(film.getId())).thenReturn(film);
        when(listMock.areRelated(film, user.getId())).thenReturn(true);
        when(listMock.updateWatched(film, user.getId())).thenReturn(false);
        Assert.assertFalse(listService.addToWatched(film, true, user.getId()));
    }

    @Test
    public void addToWatchedInsertionError_Fail() {
        when(filmMock.getFilmById(film.getId())).thenReturn(null);
        when(filmMock.addFilm(film)).thenReturn(false);
        Assert.assertFalse(listService.addToWatched(film, true, user.getId()));
    }

    @Test
    public void addToPlannedExisting_Success() {
        when(filmMock.getFilmById(film.getId())).thenReturn(film);
        when(listMock.areRelated(film, user.getId())).thenReturn(false);
        when(listMock.insertPlanned(film, user.getId(), true)).thenReturn(true);
        Assert.assertTrue(listService.addToPlanned(film, true, user.getId()));
    }

    @Test
    public void addToPlannedNew_Success() {
        when(filmMock.getFilmById(film.getId())).thenReturn(null);
        when(filmMock.addFilm(film)).thenReturn(true);
        when(listMock.areRelated(film, user.getId())).thenReturn(false);
        when(listMock.insertPlanned(film, user.getId(), true)).thenReturn(true);
        Assert.assertTrue(listService.addToPlanned(film, true, user.getId()));
    }

    @Test
    public void addToPlannedWhenWatched_Success() {
        when(filmMock.getFilmById(film.getId())).thenReturn(film);
        when(listMock.areRelated(film, user.getId())).thenReturn(true);
        when(listMock.updatePlanned(film, user.getId())).thenReturn(true);
        Assert.assertTrue(listService.addToPlanned(film, true, user.getId()));
    }

    @Test
    public void addToPlannedWhenWatched_Fail() {
        when(filmMock.getFilmById(film.getId())).thenReturn(film);
        when(listMock.areRelated(film, user.getId())).thenReturn(true);
        when(listMock.updatePlanned(film, user.getId())).thenReturn(false);
        Assert.assertFalse(listService.addToPlanned(film, true, user.getId()));
    }

    @Test
    public void addToPlannedInsertionError_Fail() {
        when(filmMock.getFilmById(film.getId())).thenReturn(null);
        when(filmMock.addFilm(film)).thenReturn(false);
        Assert.assertFalse(listService.addToPlanned(film, true, user.getId()));
    }

    @Test
    public void removeFromWatchedNotWatched_Fail() {
        when(listMock.isWatched(film, user.getId())).thenReturn(false);
        Assert.assertFalse(listService.removeFromWatched(film, user.getId()));
    }

    @Test
    public void removeFromWatchedWhenPlanned_Success() {
        when(listMock.isWatched(film, user.getId())).thenReturn(true);
        when(listMock.isPlanned(film, user.getId())).thenReturn(true);
        when(listMock.resetWatched(film, user.getId())).thenReturn(true);
        Assert.assertTrue(listService.removeFromWatched(film, user.getId()));
    }

    @Test
    public void removeFromWatchedWhenNotPlanned_Success() {
        when(listMock.isWatched(film, user.getId())).thenReturn(true);
        when(listMock.isPlanned(film, user.getId())).thenReturn(false);
        when(listMock.removeFilm(film, user.getId())).thenReturn(true);
        Assert.assertTrue(listService.removeFromWatched(film, user.getId()));
    }

    @Test
    public void removeFromPlannedNotPlanned_Fail() {
        when(listMock.isPlanned(film, user.getId())).thenReturn(false);
        Assert.assertFalse(listService.removeFromPlanned(film, user.getId()));
    }

    @Test
    public void removeFromPlannedWhenWatched_Success() {
        when(listMock.isPlanned(film, user.getId())).thenReturn(true);
        when(listMock.isWatched(film, user.getId())).thenReturn(true);
        when(listMock.resetPlanned(film, user.getId())).thenReturn(true);
        Assert.assertTrue(listService.removeFromPlanned(film, user.getId()));
    }

    @Test
    public void removeFromPlannedWhenNotWatched_Success() {
        when(listMock.isPlanned(film, user.getId())).thenReturn(true);
        when(listMock.isWatched(film, user.getId())).thenReturn(false);
        when(listMock.removeFilm(film, user.getId())).thenReturn(true);
        Assert.assertTrue(listService.removeFromPlanned(film, user.getId()));
    }

    @Test
    public void makePrivate_Success() {
        when(listMock.areRelated(film, user.getId())).thenReturn(true);
        when(listMock.changePrivacy(film, user.getId(), false)).thenReturn(true);
        Assert.assertTrue(listService.makePrivate(user.getId(), film));
    }

    @Test
    public void makePrivateNotRelated_Fail() {
        when(listMock.areRelated(film, user.getId())).thenReturn(false);
        when(listMock.changePrivacy(film, user.getId(), false)).thenReturn(true);
        Assert.assertFalse(listService.makePrivate(user.getId(), film));
    }

    @Test
    public void makePrivateUpdateError_Fail() {
        when(listMock.areRelated(film, user.getId())).thenReturn(true);
        when(listMock.changePrivacy(film, user.getId(), false)).thenReturn(false);
        Assert.assertFalse(listService.makePrivate(user.getId(), film));
    }

    @Test
    public void makePublic_Success() {
        when(listMock.areRelated(film, user.getId())).thenReturn(true);
        when(listMock.changePrivacy(film, user.getId(), true)).thenReturn(true);
        Assert.assertTrue(listService.makePublic(user.getId(), film));
    }

    @Test
    public void makePublicNotRelated_Fail() {
        when(listMock.areRelated(film, user.getId())).thenReturn(false);
        when(listMock.changePrivacy(film, user.getId(), true)).thenReturn(true);
        Assert.assertFalse(listService.makePublic(user.getId(), film));
    }

    @Test
    public void makePublicUpdateError_Fail() {
        when(listMock.areRelated(film, user.getId())).thenReturn(true);
        when(listMock.changePrivacy(film, user.getId(), true)).thenReturn(false);
        Assert.assertFalse(listService.makePublic(user.getId(), film));
    }
}
