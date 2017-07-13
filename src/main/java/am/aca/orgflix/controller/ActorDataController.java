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
@RequestMapping("/data/actor")
public class ActorDataController extends DataController {

    @RequestMapping("/getList")
    public ResponseEntity getActorsList() {
        try {
            List<Cast> actorsList = castService.listCasts();
            int size = actorsList.size();
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for (Cast cast : actorsList) {
                sb.append("{");
                sb.append("\"id\":\"");
                sb.append(cast.getId());
                sb.append("\", \"name\":\"");
                sb.append(cast.getName());
                sb.append("\", \"oscar\":\"");
                sb.append(cast.isHasOscar());
                sb.append("\"}");
                sb.append(--size > 0 ? "," : "");
            }
            sb.append("]");
            return new ResponseEntity(sb.toString(), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
}
