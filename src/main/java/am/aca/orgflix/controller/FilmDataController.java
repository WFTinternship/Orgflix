package am.aca.orgflix.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * Controller for REST request
 */
@RestController
@RequestMapping("/data/film")
public class FilmDataController extends DataController{

    @PostMapping("/watch/addToList")
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

    @PostMapping("/wish/addToList")
    public ResponseEntity addFilmToWishList(
            HttpSession session,
            @RequestParam("film") int filmId,
            @RequestParam("isPublic") boolean isPublic) {
        try {
            boolean state = listService.addToPlanned(filmId, true, (int) session.getAttribute("userId"));
            if (state)
                return new ResponseEntity("Film added to wish list", HttpStatus.OK);
            else
                return new ResponseEntity("Not succeeded", HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/wish/removeFromList")
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

    @PostMapping("/watch/removeFromList")
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

    @PostMapping("/star")
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
}
