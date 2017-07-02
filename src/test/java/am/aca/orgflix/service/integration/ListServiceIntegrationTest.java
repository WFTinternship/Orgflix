package am.aca.orgflix.service.integration;

import am.aca.orgflix.BaseIntegrationTest;
import am.aca.orgflix.entity.Film;
import am.aca.orgflix.entity.User;
import am.aca.orgflix.service.FilmService;
import am.aca.orgflix.service.ListService;
import am.aca.orgflix.service.UserService;
import am.aca.orgflix.util.TestHelper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by karine on 6/21/2017
 */
public class ListServiceIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private ListService listService;

    @Autowired
    private FilmService filmService;

    @Autowired
    private UserService userService;

    @Autowired
    private TestHelper helper;

    private Film film;
    private int filmId;

    private User user;

    @Before
    public void setup() {
        film = new Film("Title", 1999);
        filmService.addFilm(film);
        filmId = film.getId();

        user = new User("hulk", "Bruce Banner", "brucebanner@avengers.com", "natasha");
        userService.add(user);

    }

    @After
    public void tearDown() {
        helper.emptyTable(new String[]{
                "lists", "films", "users"
        });
    }

    @Test
    public void addToWatchedWhenNotPlanned_Succeeded() {
        boolean status = listService.addToWatched(filmId, true, user.getId());
        Assert.assertTrue(status);

        int size = listService.showOwnWatched(user.getId(), 0).size();
        Assert.assertEquals(1, size);
    }

    @Test
    public void addToWatchedWhenPlanned_Succeeded() {
        //setup List
        listService.addToPlanned(filmId, true, user.getId());

        boolean status = listService.addToWatched(filmId, true, user.getId());
        Assert.assertTrue(status);

        int size = listService.showOwnWatched(user.getId(), 0).size();
        Assert.assertEquals(1, size);
    }

    @Test
    public void addToPlannedWhenNotWatched_Succeeded() {
        boolean status = listService.addToPlanned(filmId, true, user.getId());
        Assert.assertTrue(status);

        int size = listService.showOwnPlanned(user.getId(), 0).size();
        Assert.assertEquals(1, size);
    }

    @Test
    public void addToPlannedWhenWatchedSucceeded() {
        //setup List
        listService.addToWatched(filmId, true, user.getId());

        boolean status = listService.addToPlanned(filmId, true, user.getId());
        Assert.assertTrue(status);

        int size = listService.showOwnWatched(user.getId(), 0).size();
        Assert.assertEquals(1, size);
    }

    @Test
    public void removeFromPlannedWhenNotWatched_Succeeded() {
        //setup List
        listService.addToPlanned(filmId, true, user.getId());

        boolean status = listService.removeFromPlanned(film.getId(), user.getId());
        Assert.assertTrue(status);

        int size = listService.showOwnPlanned(user.getId(), 0).size();
        Assert.assertEquals(0, size);
    }

    @Test
    public void removeFromPlannedWhenWatched_Succeeded() {
        //setup List
        listService.addToPlanned(filmId, true, user.getId());
        listService.addToWatched(filmId, true, user.getId());

        boolean status = listService.removeFromPlanned(film.getId(), user.getId());
        Assert.assertTrue(status);

        int size = listService.showOwnWatched(user.getId(), 0).size();
        Assert.assertEquals(1, size);
    }

    @Test
    public void removeFromWatchedWhenNotPlanned_Succeeded() {
        //setup List
        listService.addToWatched(filmId, true, user.getId());

        boolean status = listService.removeFromWatched(film.getId(), user.getId());
        Assert.assertTrue(status);

        int size = listService.showOwnWatched(user.getId(), 0).size();
        Assert.assertEquals(0, size);
    }

    @Test
    public void removeFromWatchedWhenPlanned_Succeeded() {
        //setup List
        listService.addToPlanned(filmId, true, user.getId());
        listService.addToWatched(filmId, true, user.getId());

        boolean status = listService.removeFromWatched(film.getId(), user.getId());
        Assert.assertTrue(status);

        int size = listService.showOwnPlanned(user.getId(), 0).size();
        Assert.assertEquals(1, size);
    }

    @Test
    public void removeFromPlannedWhenNotPlanned_Failed() {
        boolean status = listService.removeFromPlanned(film.getId(), user.getId());
        Assert.assertFalse(status);
    }

    @Test
    public void removeFromPlannedWhenWatched_Failed() {
        //setup List
        listService.addToWatched(filmId, true, user.getId());

        boolean status = listService.removeFromPlanned(film.getId(), user.getId());
        Assert.assertFalse(status);
    }

    @Test
    public void removeFromWatchedWhenNotPlanned_Failed() {
        //List setup, empty list

        Assert.assertFalse(listService.removeFromWatched(film.getId(), user.getId()));
    }

    @Test
    public void removeFromWatchedWhenPlanned_Failed() {
        //setup List
        listService.addToPlanned(filmId, true, user.getId());

        boolean status = listService.removeFromWatched(film.getId(), user.getId());
        Assert.assertFalse(status);
    }

    @Test
    public void showOthersPlanned_Success() {
        listService.addToPlanned(filmId, true, user.getId());
        filmService.addFilm(film);
        listService.addToPlanned(filmId, false, user.getId());

        int size = listService.showOthersPlanned(user.getId(), 0).size();
        Assert.assertEquals(1, size);
    }

    @Test
    public void showOthersPlannedFailed() {
        //setup List
        listService.addToPlanned(filmId, false, user.getId());

        int size = listService.showOthersPlanned(user.getId(), 0).size();
        Assert.assertEquals(0, size);
    }

    @Test
    public void showOthersWatched_Success() {
        listService.addToWatched(filmId, true, user.getId());
        filmService.addFilm(film);
        listService.addToWatched(filmId, false, user.getId());

        int size = listService.showOthersWatched(user.getId(), 0).size();
        Assert.assertEquals(1, size);
    }

    @Test
    public void showOthersWatchedFailed() {
        //setup List
        listService.addToWatched(filmId, false, user.getId());

        boolean status = listService.showOthersWatched(user.getId(), 0).isEmpty();
        Assert.assertTrue(status);
    }

    @Test
    public void showOwnPlannedSuccess() {
        listService.addToPlanned(filmId, false, user.getId());

        int size = listService.showOwnPlanned(user.getId(), 0).size();
        Assert.assertEquals(1, size);
    }

    @Test
    public void showOwnPlanned_Empty() {
        boolean status = listService.showOwnPlanned(user.getId(), 0).isEmpty();
        Assert.assertTrue(status);
    }

    @Test
    public void showOwnWatched_Success() {
        listService.addToWatched(filmId, false, user.getId());

        int size = listService.showOwnWatched(user.getId(), 0).size();
        Assert.assertEquals(1, size);
    }

    @Test
    public void showOwnWatched_Empty() {
        boolean status = listService.showOwnWatched(user.getId(), 0).isEmpty();
        Assert.assertTrue(status);
    }

    @Test
    public void makePrivate_Success() {
        listService.addToWatched(filmId, true, user.getId());

        boolean status = listService.makePrivate(user.getId(), film);
        Assert.assertTrue(status);

        status = listService.showOthersWatched(user.getId(), 0).isEmpty();
        Assert.assertTrue(status);
    }

    @Test
    public void makePublic_Success() {
        listService.addToWatched(filmId, false, user.getId());

        boolean status = listService.makePublic(user.getId(), film);
        Assert.assertTrue(status);

        int size = listService.showOthersWatched(user.getId(), 0).size();
        Assert.assertEquals(1, size);
    }
}
