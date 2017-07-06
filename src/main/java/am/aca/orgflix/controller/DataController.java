package am.aca.orgflix.controller;

import am.aca.orgflix.entity.Cast;
import am.aca.orgflix.service.CastService;
import am.aca.orgflix.service.FilmService;
import am.aca.orgflix.service.ListService;
import am.aca.orgflix.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
    public DataController(UserService userService, ListService listService,
                          FilmService filmService, CastService castService){
        this.userService = userService;
        this.listService = listService;
        this.filmService = filmService;
        this.castService = castService;
    }

    @PostMapping("/getActorsList")
    public ResponseEntity getActorsList() {
        List<Cast> actorsList = castService.listCasts();
        int size = actorsList.size();
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for(Cast cast : actorsList){
            sb.append("{");
            sb.append("\"id\":\"");
            sb.append(cast.getId());
            sb.append("\", \"name\":\"");
            sb.append(cast.getName());
            sb.append("\", \"oscar\":\"");
            sb.append(cast.isHasOscar());
            sb.append("\"}");
            sb.append( --size > 0 ? "," : "" );
        }
        sb.append("]");
        return new ResponseEntity(sb.toString(), HttpStatus.OK);
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
