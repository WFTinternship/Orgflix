package am.aca.orgflix.unit.service;

import am.aca.dao.jdbc.impljdbc.JdbcListDAO;
import am.aca.entity.Film;
import am.aca.entity.User;
import am.aca.orgflix.BaseUnitTest;
import am.aca.service.ListService;
import am.aca.service.impl.FilmServiceImpl;
import am.aca.service.impl.ListServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.aop.framework.Advised;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

/**
 * Created by karine on 6/25/2017
 */
public class ListTest extends BaseUnitTest {

    @Autowired
    @InjectMocks
    @Qualifier("listService")
    private ListService listService;

    @Mock
    private JdbcListDAO listMock;
    @Mock
    private FilmServiceImpl filmMock;

    private Film film = new Film("Babel", 2006);
    private User user = new User("hulk", "Bruce Banner", "bbanner@avenger.com", "natasha");
    private List<Film> films = new ArrayList<>();

    public final Object unwrapProxy(Object bean) throws Exception {
        if (AopUtils.isAopProxy(bean) && bean instanceof Advised) {

            Advised advised = (Advised) bean;
            bean = advised.getTargetSource().getTarget();
        }

        return bean;
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
//        listService = (ListServiceImpl) unwrapProxy(listService);
//        ReflectionTestUtils.setField(listService, "listDAO", listMock);
//        ReflectionTestUtils.setField(listService, "filmService", filmMock);
    }

    @Test
    public void addToWatchedExisting_Success() {
        when(filmMock.getFilmById(film.getId())).thenReturn(film);
        when(listMock.areRelated(film, user.getId())).thenReturn(false);
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
    public void showOwnWatched_Success() {
        when(listMock.showOwnWatched(user.getId())).thenReturn(films);
        Assert.assertEquals(films, listService.showOwnWatched(user.getId()));
    }

    @Test
    public void showOwnWatched_Fail() {
        when(listMock.showOwnWatched(user.getId())).thenThrow(RuntimeException.class);
        Assert.assertEquals(null, listService.showOwnWatched(user.getId()));
    }

    @Test
    public void showOwnPlanned_Success() {
        when(listMock.showOwnPlanned(user.getId())).thenReturn(films);
        Assert.assertEquals(films, listService.showOwnPlanned(user.getId()));
    }

    @Test
    public void showOwnPlanned_Fail() {
        when(listMock.showOwnPlanned(user.getId())).thenThrow(RuntimeException.class);
        Assert.assertEquals(null, listService.showOwnPlanned(user.getId()));
    }

    @Test
    public void showOthersWatched_Success() {
        when(listMock.showOthersWatched(user.getId())).thenReturn(films);
        Assert.assertEquals(films, listService.showOthersWatched(user.getId()));
    }

    @Test
    public void showOthersWatched_Fail() {
        when(listMock.showOthersWatched(user.getId())).thenThrow(RuntimeException.class);
        Assert.assertEquals(null, listService.showOthersWatched(user.getId()));
    }

    @Test
    public void showOthersPLanned_Success() {
        when(listMock.showOthersPlanned(user.getId())).thenReturn(films);
        Assert.assertEquals(films, listService.showOthersPlanned(user.getId()));
    }

    @Test
    public void showOthersPlanned_Fail() {
        when(listMock.showOthersPlanned(user.getId())).thenThrow(RuntimeException.class);
        Assert.assertEquals(null, listService.showOthersPlanned(user.getId()));
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
