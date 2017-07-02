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
    public void setUp() {
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
    public void addToWatched_WhenNotPlanned_Success() {
        boolean status = listService.addToWatched(filmId, true, user.getId());
        Assert.assertTrue(status);

        int size = listService.showOwnWatched(user.getId(), 0).size();
        Assert.assertEquals(1, size);
    }

    @Test
    public void addToWatched_WhenPlanned_Success() {
        //setUp List
        listService.addToPlanned(filmId, true, user.getId());

        boolean status = listService.addToWatched(filmId, true, user.getId());
        Assert.assertTrue(status);

        int size = listService.showOwnWatched(user.getId(), 0).size();
        Assert.assertEquals(1, size);
    }

    @Test
    public void addToPlanned_WhenNotWatched_Success() {
        boolean status = listService.addToPlanned(filmId, true, user.getId());
        Assert.assertTrue(status);

        int size = listService.showOwnPlanned(user.getId(), 0).size();
        Assert.assertEquals(1, size);
    }

    @Test
    public void addToPlanned_WhenWatchedSuccess() {
        //setUp List
        listService.addToWatched(filmId, true, user.getId());

        boolean status = listService.addToPlanned(filmId, true, user.getId());
        Assert.assertTrue(status);

        int size = listService.showOwnWatched(user.getId(), 0).size();
        Assert.assertEquals(1, size);
    }

    @Test
    public void removeFromPlanned_WhenNotWatched_Success() {
        //setUp List
        listService.addToPlanned(filmId, true, user.getId());

        boolean status = listService.removeFromPlanned(film.getId(), user.getId());
        Assert.assertTrue(status);

        int size = listService.showOwnPlanned(user.getId(), 0).size();
        Assert.assertEquals(0, size);
    }

    @Test
    public void removeFromPlanned_WhenWatched_Success() {
        //setUp List
        listService.addToPlanned(filmId, true, user.getId());
        listService.addToWatched(filmId, true, user.getId());

        boolean status = listService.removeFromPlanned(film.getId(), user.getId());
        Assert.assertTrue(status);

        int size = listService.showOwnWatched(user.getId(), 0).size();
        Assert.assertEquals(1, size);
    }

    @Test
    public void removeFromWatched_WhenNotPlanned_Success() {
        //setUp List
        listService.addToWatched(filmId, true, user.getId());

        boolean status = listService.removeFromWatched(film.getId(), user.getId());
        Assert.assertTrue(status);

        int size = listService.showOwnWatched(user.getId(), 0).size();
        Assert.assertEquals(0, size);
    }

    @Test
    public void removeFromWatched_WhenPlanned_Success() {
        //setUp List
        listService.addToPlanned(filmId, true, user.getId());
        listService.addToWatched(filmId, true, user.getId());

        boolean status = listService.removeFromWatched(film.getId(), user.getId());
        Assert.assertTrue(status);

        int size = listService.showOwnPlanned(user.getId(), 0).size();
        Assert.assertEquals(1, size);
    }

    @Test
    public void removeFromPlanned_WhenNotPlanned_Fail() {
        boolean status = listService.removeFromPlanned(film.getId(), user.getId());
        Assert.assertFalse(status);
    }

    @Test
    public void removeFromPlanned_WhenWatched_Fail() {
        //setUp List
        listService.addToWatched(filmId, true, user.getId());

        boolean status = listService.removeFromPlanned(film.getId(), user.getId());
        Assert.assertFalse(status);
    }

    @Test
    public void removeFromWatched_WhenNotPlanned_Fail() {
        //List setUp, empty list

        Assert.assertFalse(listService.removeFromWatched(film.getId(), user.getId()));
    }

    @Test
    public void removeFromWatched_WhenPlanned_Fail() {
        //setUp List
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
    public void showOthersPlanned_Fail() {
        //setUp List
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
    public void showOthersWatched_Fail() {
        //setUp List
        listService.addToWatched(filmId, false, user.getId());

        boolean status = listService.showOthersWatched(user.getId(), 0).isEmpty();
        Assert.assertTrue(status);
    }

    @Test
    public void showOwnPlanned_Success() {
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
