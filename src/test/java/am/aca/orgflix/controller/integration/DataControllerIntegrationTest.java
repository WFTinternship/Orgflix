package am.aca.orgflix.controller.integration;

import am.aca.orgflix.BaseIntegrationTest;
import am.aca.orgflix.controller.ActorDataController;
import am.aca.orgflix.controller.DataController;
import am.aca.orgflix.controller.FilmDataController;
import am.aca.orgflix.entity.Cast;
import am.aca.orgflix.entity.Film;
import am.aca.orgflix.entity.User;
import am.aca.orgflix.service.CastService;
import am.aca.orgflix.service.FilmService;
import am.aca.orgflix.service.UserService;
import am.aca.orgflix.util.TestHelper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Integration tests for Data controller
 */
public class DataControllerIntegrationTest extends BaseIntegrationTest {
    @Autowired
    private FilmDataController filmDataController;
    @Autowired
    private ActorDataController actorDataController;

    @Autowired
    private CastService castService;

    @Autowired
    private FilmService filmService;

    @Autowired
    private UserService userService;

    @Autowired
    private TestHelper helper;

    private Cast cast = new Cast("Thandie Newton");
    private Film film = new Film("Rocknrolla", 2008);
    private User user = new User("IronMan", "Tony Stark", "tstark@avengers.com", "pepper<3");

    /**
     * Rolls back all changes applied to the test DB resulted from tests
     */
    @After
    public void tearDown() {
        helper.emptyTable(new String[]{
                "lists",
                "users",
                "genre_to_film",
                "film_to_cast",
                "casts",
                "films"});
    }

    /**
     * @see ActorDataController#getActorsList()
     */
    @Test
    public void getActorsList_OneMember_Success() {
        castService.addCast(cast);

        ResponseEntity actualRE = actorDataController.getActorsList();
        String expectedRE = "[{\"id\":\"" + cast.getId() + "\", \"name\":\"" + cast.getName() + "\", \"oscar\":\"" + cast.isHasOscar() + "\"}]";
        Assert.assertEquals(new ResponseEntity(expectedRE, HttpStatus.OK), actualRE);
    }

    /**
     * @see ActorDataController#getActorsList()
     */
    @Test
    public void getActorsList_ThreeMembers_Success() {
        Cast cast1 = new Cast("Mark Strong");
        Cast cast2 = new Cast("Idris Elba");

        castService.addCast(cast);
        castService.addCast(cast1);
        castService.addCast(cast2);

        ResponseEntity actualRE = actorDataController.getActorsList();

        String expectedRE = "[{\"id\":\"" + cast2.getId() + "\", \"name\":\"" + cast2.getName() + "\", \"oscar\":\"" + cast2.isHasOscar() + "\"}," +
                "{\"id\":\"" + cast1.getId() + "\", \"name\":\"" + cast1.getName() + "\", \"oscar\":\"" + cast1.isHasOscar() + "\"}," +
                "{\"id\":\"" + cast.getId() + "\", \"name\":\"" + cast.getName() + "\", \"oscar\":\"" + cast.isHasOscar() + "\"}" +
                "]";
        Assert.assertEquals(new ResponseEntity(expectedRE, HttpStatus.OK), actualRE);
    }

    /**
     * @see ActorDataController#getActorsList()
     */
    @Test
    public void getActorsList_Empty_Fail() {
        ResponseEntity actualRE = actorDataController.getActorsList();

        Assert.assertEquals(new ResponseEntity("[]", HttpStatus.OK), actualRE);
    }

    /**
     * @see FilmDataController#addFilmToWatchList(int, int, boolean)
     */
    @Test
    public void addToWatched_Success() {
        filmService.addFilm(film);
        userService.add(user);

        ResponseEntity actualRE = filmDataController.addFilmToWatchList(user.getId(), film.getId(), true);

        Assert.assertEquals(new ResponseEntity("Film added to watch list", HttpStatus.OK), actualRE);
    }

    /**
     * @see FilmDataController#addFilmToWatchList(int, int, boolean)
     */
    @Test
    public void addToWatched_Fail() {
        ResponseEntity actualRE = filmDataController.addFilmToWatchList(user.getId(), film.getId(), true);

        Assert.assertEquals(new ResponseEntity(HttpStatus.BAD_REQUEST), actualRE);
    }

    /**
     * @see FilmDataController#addFilmToWishList(int, int, boolean) շ
     */
    @Test
    public void addToPlanned_Success() {
        filmService.addFilm(film);
        userService.add(user);

        ResponseEntity actualRE = filmDataController.addFilmToWishList(user.getId(), film.getId(),true);

        Assert.assertEquals(new ResponseEntity("Film added to wish list", HttpStatus.OK), actualRE);
    }

    /**
     * @see FilmDataController#addFilmToWishList(int, int, boolean)
     */
    @Test
    public void addToPlanned_Fail() {
        ResponseEntity actualRE = filmDataController.addFilmToWishList(user.getId(), film.getId(),true);

        Assert.assertEquals(new ResponseEntity(HttpStatus.BAD_REQUEST), actualRE);
    }

    /**
     * @see FilmDataController#removeFromWishList(int, int)
     */
    @Test
    public void removePlanned_Success() {
        filmService.addFilm(film);
        userService.add(user);
        filmDataController.addFilmToWishList(user.getId(), film.getId(),true);

        ResponseEntity actualRE = filmDataController.removeFromWishList(user.getId(), film.getId());

        Assert.assertEquals(new ResponseEntity("Film removed from wish list", HttpStatus.OK), actualRE);
    }

    /**
     * @see FilmDataController#removeFromWishList(int, int)
     */
    @Test
    public void removePlanned_NotExisting_Fail() {
        ResponseEntity actualRE = filmDataController.removeFromWishList(user.getId(), film.getId());

        Assert.assertEquals(new ResponseEntity("Not succeeded", HttpStatus.BAD_REQUEST), actualRE);
    }

    /**
     * @see FilmDataController#removeFromWishList(int, int) (int, int)
     */
    @Test
    public void removeWatched_Success() {
        filmService.addFilm(film);
        userService.add(user);
        filmDataController.addFilmToWatchList(user.getId(), film.getId(), true);

        ResponseEntity actualRE = filmDataController.removeFromWatchList(user.getId(), film.getId());

        Assert.assertEquals(new ResponseEntity("Film removed from watch list", HttpStatus.OK), actualRE);
    }

    /**
     * @see FilmDataController#removeFromWatchList(int, int) (int, int) (int, int)
     */
    @Test
    public void removeWatched_NotExisting_Fail() {
        ResponseEntity actualRE = filmDataController.removeFromWishList(user.getId(), film.getId());

        Assert.assertEquals(new ResponseEntity("Not succeeded", HttpStatus.BAD_REQUEST), actualRE);
    }
}
