package am.aca.orgflix.controller;

import am.aca.orgflix.service.CastService;
import am.aca.orgflix.service.FilmService;
import am.aca.orgflix.service.ListService;
import am.aca.orgflix.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for REST request
 */
@RestController
@RequestMapping("/data")
public class DataController {

    private UserService userService;
    private ListService listService;
    private FilmService filmService;
    private CastService castService;
    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    @Autowired
    public void setListService(ListService listService) {
        this.listService = listService;
    }
    @Autowired
    public void setFilmService(FilmService filmService) {
        this.filmService = filmService;
    }
    @Autowired
    public void setCastService(CastService castService) {
        this.castService = castService;
    }


    @PostMapping("/addFilmToWatchList")
    public ResponseEntity addFilmToWatchList(@RequestParam("user") int userId, @RequestParam("film") int filmId) {
        boolean state = listService.addToWatched(filmId, true,userId );
        if (state)
            return new ResponseEntity("Film added to watch list", HttpStatus.OK);
        else
            return new ResponseEntity("Not succeeded", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/addFilmToWishList")
    public ResponseEntity addFilmToWishList(@RequestParam("user") int userId, @RequestParam("film") int filmId) {
        boolean state = listService.addToPlanned(filmId, true,userId );
        if (state)
            return new ResponseEntity("Film added to wish list", HttpStatus.OK);
        else
            return new ResponseEntity("Not succeeded", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/removeFilmFromWishList")
    public ResponseEntity removeFromWishList(@RequestParam("user") int userId, @RequestParam("film") int filmId) {
        boolean state = listService.removeFromPlanned(filmId, userId );
        if (state)
            return new ResponseEntity("Film removed from wish list", HttpStatus.OK);
        else
            return new ResponseEntity("Not succeeded", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/removeFilmFromWatchList")
    public ResponseEntity removeFromWatchList(@RequestParam("user") int userId, @RequestParam("film") int filmId) {
        boolean state = listService.removeFromWatched(filmId, userId );
        if (state)
            return new ResponseEntity("Film removed from watch list", HttpStatus.OK);
        else
            return new ResponseEntity("Not succeeded", HttpStatus.BAD_REQUEST);
    }
}
