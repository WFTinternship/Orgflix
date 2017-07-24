package am.aca.orgflix.controller;

import am.aca.orgflix.service.FilmService;
import am.aca.orgflix.service.ListService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

/**
 * Controller for REST request
 */
@RestController
@RequestMapping("/data/film")
public class FilmDataController{

    protected Logger LOGGER = Logger.getLogger(FilmDataController.class);

    private ListService listService;
    private FilmService filmService;

    @Autowired
    public void setListService(ListService listService) {
        this.listService = listService;
    }

    @Autowired
    public void setFilmService(FilmService filmService) {
        this.filmService = filmService;
    }

    /**
     * Adding film to watched list
     * @param session current session
     * @param filmId id of the film to be added
     * @param isPublic flag whether the film should be visible in the list for others
     * @return Http OK status if succeeded or Http bad request otherwise
     */
    @RequestMapping("/watch/addToList")
    public ResponseEntity addFilmToWatchList(
            HttpSession session,
            @RequestParam("film") int filmId,
            @RequestParam("isPublic") boolean isPublic) {
        try {
            boolean state = listService.addToWatched(filmId, isPublic, (int) session.getAttribute("userId"));
            if (state)
                return new ResponseEntity("Film added to watch list", HttpStatus.OK);
            else
                return new ResponseEntity("Not succeeded", HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Adding film to wish list
     * @param session current session
     * @param filmId id of the film to be added
     * @param isPublic flag whether the film should be visible in the list for others
     * @return Http OK status if succeeded or Http bad request otherwise
     */
    @RequestMapping("/wish/addToList")
    public ResponseEntity addFilmToWishList(
            HttpSession session,
            @RequestParam("film") int filmId,
            @RequestParam("isPublic") boolean isPublic) {
        try {
            boolean state = listService.addToPlanned(filmId, isPublic, (int) session.getAttribute("userId"));
            if (state)
                return new ResponseEntity("Film added to wish list", HttpStatus.OK);
            else
                return new ResponseEntity("Not succeeded", HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Remove film from wish list
     * @param session current session
     * @param filmId id of the film to be added
     * @return Http OK status if succeeded or Http bad request otherwise
     */
    @RequestMapping("/wish/removeFromList")
    public ResponseEntity removeFromWishList(
            HttpSession session,
            @RequestParam("film") int filmId) {
        try {
            boolean state = listService.removeFromPlanned(filmId, (int) session.getAttribute("userId"));
            if (state)
                return new ResponseEntity("Film removed from wish list", HttpStatus.OK);
            else
                return new ResponseEntity("Not succeeded", HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Remove film from watched list
     * @param session current session
     * @param filmId id of the film to be added
     * @return Http OK status if succeeded or Http bad request otherwise
     */
    @RequestMapping("/watch/removeFromList")
    public ResponseEntity removeFromWatchList(
            HttpSession session,
            @RequestParam("film") int filmId) {
        try {
            boolean state = listService.removeFromWatched(filmId, (int) session.getAttribute("userId"));
            if (state)
                return new ResponseEntity("Film removed from watch list", HttpStatus.OK);
            else
                return new ResponseEntity("Not succeeded", HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Rate the film by number of stars
     * @param filmId the id of the film to be rated
     * @param star number of stars assigned to its rate
     * @return Http OK status if succeeded or Http bad request otherwise
     */
    @RequestMapping("/star")
    public ResponseEntity starFilm(@RequestParam("film") int filmId,
                                   @RequestParam("star") int star) {
        try {
            boolean state = filmService.rate(filmId, star);
            if (state)
                return new ResponseEntity("Film is rated", HttpStatus.OK);
            else
                return new ResponseEntity("Not succeeded", HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Updates the visibility of the film in list for other users
     * @param session current session
     * @param filmId the id of the films which visibility is changed
     * @param isPublic flag whether the visibility should be set to true or false
     * @return Http OK status if succeeded or Http bad request otherwise
     */
    @PostMapping("/privacy")
    public ResponseEntity setFilmPrivacy(HttpSession session, @RequestParam("filmId") int filmId,
                                   @RequestParam("isPublic") boolean isPublic) {
        try {
            boolean state;
            if(!isPublic)
                state = listService.makePrivate((int) session.getAttribute("userId"), filmService.getById(filmId));
            else
                state = listService.makePublic((int) session.getAttribute("userId"), filmService.getById(filmId));

            if (state)
                return new ResponseEntity("Film privacy is updated", HttpStatus.OK);
            else
                return new ResponseEntity("Not succeeded", HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

}
