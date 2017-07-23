package am.aca.orgflix.controller.integration;

import am.aca.orgflix.BaseIntegrationTest;
import am.aca.orgflix.controller.ActorDataController;
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
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;

/**
 * Integration tests for Data controller
 */
@Ignore
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
        castService.add(cast);

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

        castService.add(cast);
        castService.add(cast1);
        castService.add(cast2);

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
     * @see FilmDataController#addFilmToWatchList(javax.servlet.http.HttpSession, int, boolean)
     */
    @Test
    public void addToWatched_Success() {
        filmService.add(film);
        userService.add(user);

        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpSession session = new MockHttpSession();
        request.setSession(session);

        session.setAttribute("userId",1);
        ResponseEntity actualRE = filmDataController.addFilmToWatchList(session, film.getId(), true);

        Assert.assertEquals(new ResponseEntity("Film added to watch list", HttpStatus.OK), actualRE);
    }

    /**
     * @see FilmDataController#addFilmToWatchList(javax.servlet.http.HttpSession, int, boolean)
     */
    @Test
    public void addToWatched_Fail() {
        ResponseEntity actualRE = filmDataController.addFilmToWatchList(new MockHttpSession(), film.getId(), true);

        Assert.assertEquals(new ResponseEntity(HttpStatus.BAD_REQUEST), actualRE);
    }

    /**
     * @see FilmDataController#addFilmToWishList(javax.servlet.http.HttpSession, int, boolean) շ
     */
    @Test
    public void addToPlanned_Success() {
        filmService.add(film);
        userService.add(user);

        ResponseEntity actualRE = filmDataController.addFilmToWishList(new MockHttpSession(), film.getId(),true);

        Assert.assertEquals(new ResponseEntity("Film added to wish list", HttpStatus.OK), actualRE);
    }

    /**
     * @see FilmDataController#addFilmToWishList(javax.servlet.http.HttpSession, int, boolean)
     */
    @Test
    public void addToPlanned_Fail() {
        ResponseEntity actualRE = filmDataController.addFilmToWishList(new MockHttpSession(), film.getId(),true);

        Assert.assertEquals(new ResponseEntity(HttpStatus.BAD_REQUEST), actualRE);
    }

    /**
     * @see FilmDataController#removeFromWishList(javax.servlet.http.HttpSession, int)
     */
    @Test
    public void removePlanned_Success() {
        filmService.add(film);
        userService.add(user);
        filmDataController.addFilmToWishList(new MockHttpSession(), film.getId(),true);

        ResponseEntity actualRE = filmDataController.removeFromWishList(new MockHttpSession(), film.getId());

        Assert.assertEquals(new ResponseEntity("Film removed from wish list", HttpStatus.OK), actualRE);
    }

    /**
     * @see FilmDataController#removeFromWishList(javax.servlet.http.HttpSession, int)
     */
    @Test
    public void removePlanned_NotExisting_Fail() {
        ResponseEntity actualRE = filmDataController.removeFromWishList(new MockHttpSession(), film.getId());

        Assert.assertEquals(new ResponseEntity("Not succeeded", HttpStatus.BAD_REQUEST), actualRE);
    }

    /**
     * @see FilmDataController#removeFromWishList(javax.servlet.http.HttpSession, int)
     */
    @Test
    public void removeWatched_Success() {
        filmService.add(film);
        userService.add(user);
        filmDataController.addFilmToWatchList(new MockHttpSession(), film.getId(), true);

        ResponseEntity actualRE = filmDataController.removeFromWatchList(new MockHttpSession(), film.getId());

        Assert.assertEquals(new ResponseEntity("Film removed from watch list", HttpStatus.OK), actualRE);
    }

    /**
     * @see FilmDataController#removeFromWatchList(javax.servlet.http.HttpSession, int)
     */
    @Test
    public void removeWatched_NotExisting_Fail() {
        ResponseEntity actualRE = filmDataController.removeFromWishList(new MockHttpSession(), film.getId());

        Assert.assertEquals(new ResponseEntity("Not succeeded", HttpStatus.BAD_REQUEST), actualRE);
    }
}
