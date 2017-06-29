package am.aca.controller;

import am.aca.entity.Film;
import am.aca.service.FilmService;
import am.aca.service.ListService;
import am.aca.servlet.BeanProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for REST request
 */
@RestController
@RequestMapping("/data")
public class DataController {

    @PostMapping("/addFilmToWatchList")
    public ResponseEntity addFilmToWatchList(@RequestParam("user") int userId, @RequestParam("film") int filmId) {
        boolean state = BeanProvider.getListService().addToWatched(filmId, true,userId );
        if (state)
            return new ResponseEntity("Film added to watch list", HttpStatus.OK);
        else
            return new ResponseEntity("Not succeeded", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/addFilmToWishList")
    public ResponseEntity addFilmToWishList(@RequestParam("user") int userId, @RequestParam("film") int filmId) {
        boolean state = BeanProvider.getListService().addToPlanned(filmId, true,userId );
        if (state)
            return new ResponseEntity("Film added to wish list", HttpStatus.OK);
        else
            return new ResponseEntity("Not succeeded", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/removeFilmFromWishList")
    public ResponseEntity removeFromWishList(@RequestParam("user") int userId, @RequestParam("film") int filmId) {
        boolean state = BeanProvider.getListService().removeFromPlanned(filmId, userId );
        if (state)
            return new ResponseEntity("Film removed from wish list", HttpStatus.OK);
        else
            return new ResponseEntity("Not succeeded", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/removeFilmFromWatchList")
    public ResponseEntity removeFromWatchList(@RequestParam("user") int userId, @RequestParam("film") int filmId) {
        boolean state = BeanProvider.getListService().removeFromWatched(filmId, userId );
        if (state)
            return new ResponseEntity("Film removed from watch list", HttpStatus.OK);
        else
            return new ResponseEntity("Not succeeded", HttpStatus.BAD_REQUEST);
    }
}