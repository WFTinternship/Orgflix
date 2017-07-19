package am.aca.orgflix.controller.integration;

import am.aca.orgflix.BaseIntegrationTest;
import am.aca.orgflix.controller.MainController;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Integration tests for Data controller
 */
public class MainControllerIntegrationTest extends BaseIntegrationTest{

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
    private List<Cast> casts = new ArrayList<>();
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
     * @see MainController#index()
     * */
    @Test
    public void asncjadkm(){
        Assert.assertTrue(true);
    }
}
