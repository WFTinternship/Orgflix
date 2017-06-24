package am.aca.orgflix.integration.service;

import am.aca.entity.Film;
import am.aca.entity.User;
import am.aca.orgflix.BaseIntegrationTest;
import am.aca.service.FilmService;
import am.aca.service.ListService;
import am.aca.service.UserService;
import am.aca.util.TestHelper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Created by karine on 6/21/2017
 */
public class ListTest extends BaseIntegrationTest{

    @Autowired
    @Qualifier("listServiceImpl")
    private ListService listService;

    @Autowired
    @Qualifier("filmServiceImpl")
    private FilmService filmService;

    @Autowired
    @Qualifier("userServiceImpl")
    private UserService userService;

    @Autowired
    @Qualifier("testHelper")
    private TestHelper helper;

    private Film film;
    private User user;

    @Before
    public void setup() {
        film = new Film("Title", 1999);
        filmService.addFilm(film);

        user = new User("hulk", "Bruce Banner", "brucebanner@avengers.com", "natasha");
        userService.add(user);

    }

    @After
    public void revert() {
        helper.emptyTable(new String[] {
            "lists", "films", "users"
        });
    }

    @Test
    public void addToWatchedWhenNotPlanned_Succeeded() {
        Assert.assertTrue(listService.addToWatched(film, true, user.getId()));
        Assert.assertEquals(1, listService.showOwnWatched(user.getId()).size());
    }

    @Test
    public void addToWatchedWhenPlanned_Succeeded() {
        //setup List
        listService.addToPlanned(film, true, user.getId());

        Assert.assertTrue(listService.addToWatched(film, true, user.getId()));
        Assert.assertEquals(1, listService.showOwnWatched(user.getId()).size());
    }

    @Test
    public void addToPlannedWhenNotWatched_Succeeded() {
        Assert.assertTrue(listService.addToPlanned(film, true, user.getId()));
        Assert.assertEquals(1, listService.showOwnPlanned(user.getId()).size());
    }

    @Test
    public void addToPlannedWhenWatchedSucceeded() {
        //setup List
        listService.addToWatched(film, true, user.getId());

        Assert.assertTrue(listService.addToPlanned(film, true, user.getId()));
        Assert.assertEquals(1, listService.showOwnWatched(user.getId()).size());
    }

    @Test
    public void removeFromPlannedWhenNotWatched_Succeeded() {
        //setup List
        listService.addToPlanned(film, true, user.getId());

        Assert.assertTrue(listService.removeFromPlanned(film, user.getId()));
        Assert.assertEquals(0,listService.showOwnPlanned(user.getId()).size());
    }

    @Test
    public void removeFromPlannedWhenWatched_Succeeded() {
        //setup List
        listService.addToPlanned(film, true, user.getId());
        listService.addToWatched(film, true, user.getId());

        Assert.assertTrue(listService.removeFromPlanned(film, user.getId()));
        Assert.assertEquals(1, listService.showOwnWatched(user.getId()).size());
    }

    @Test
    public void removeFromWatchedWhenNotPlanned_Succeeded() {
        //setup List
        listService.addToWatched(film, true, user.getId());

        Assert.assertTrue(listService.removeFromWatched(film, user.getId()));
        Assert.assertEquals(listService.showOwnWatched(user.getId()).size(), 0);
    }

    @Test
    public void removeFromWatchedWhenPlanned_Succeeded() {
        //setup List
        listService.addToPlanned(film, true, user.getId());
        listService.addToWatched(film, true, user.getId());

        Assert.assertTrue(listService.removeFromWatched(film, user.getId()));
        Assert.assertEquals(listService.showOwnPlanned(user.getId()).size(), 1);
    }

    @Test
    public void removeFromPlannedWhenNotPlanned_Failed() {
        Assert.assertFalse(listService.removeFromPlanned(film, user.getId()));
    }

    @Test
    public void removeFromPlannedWhenWatched_Failed() {
        //setup List
        listService.addToWatched(film, true, user.getId());

        Assert.assertFalse(listService.removeFromPlanned(film, user.getId()));
    }

    @Test
    public void removeFromWatchedWhenNotPlanned_Failed() {
        //List setup, empty list

        Assert.assertFalse(listService.removeFromWatched(film, user.getId()));
    }

    @Test
    public void removeFromWatchedWhenPlanned_Failed() {
        //setup List
        listService.addToPlanned(film, true, user.getId());

        Assert.assertFalse(listService.removeFromWatched(film, user.getId()));
    }

    @Test
    public void showOthersPlanned_Success() {
        listService.addToPlanned(film, true, user.getId());
        filmService.addFilm(film);
        listService.addToPlanned(film, false, user.getId());
        Assert.assertEquals(1, listService.showOthersPlanned(user.getId()).size());
    }

    @Test
    public void showOthersPlannedFailed() {
        //setup List
        listService.addToPlanned(film, false, user.getId());

        Assert.assertEquals(0, listService.showOthersPlanned(user.getId()).size());
    }

    @Test
    public void showOthersWatched_Success() {
        listService.addToWatched(film, true, user.getId());
        filmService.addFilm(film);
        listService.addToWatched(film, false, user.getId());
        Assert.assertEquals(1, listService.showOthersWatched(user.getId()).size());
    }

    @Test
    public void showOthersWatchedFailed() {
        //setup List
        listService.addToWatched(film, false, user.getId());

        Assert.assertEquals(0, listService.showOthersWatched(user.getId()).size());
    }

    @Test
    public void showOwnPlannedSuccess() {
        listService.addToPlanned(film, false, user.getId());
        Assert.assertEquals(1, listService.showOwnPlanned(user.getId()).size());
    }

    @Test
    public void showOwnPlanned_Empty() {
        Assert.assertTrue(listService.showOwnPlanned(user.getId()).isEmpty());
    }

    @Test
    public void showOwnWatched_Success() {
        listService.addToWatched(film, false, user.getId());
        Assert.assertEquals(1, listService.showOwnWatched(user.getId()).size());
    }

    @Test
    public void showOwnWatched_Empty() {
        Assert.assertTrue(listService.showOwnWatched(user.getId()).isEmpty());
    }

    @Test
    public void makePrivate_Success() {
        listService.addToWatched(film, true, user.getId());
        Assert.assertTrue(listService.makePrivate(user.getId(), film));
        Assert.assertTrue( listService.showOthersWatched(user.getId()).isEmpty());
    }

    @Test
    public void makePublic_Success() {
        listService.addToWatched(film, false, user.getId());
        Assert.assertTrue(listService.makePublic(user.getId(), film));
        Assert.assertEquals(1, listService.showOthersWatched(user.getId()).size());
    }
}
