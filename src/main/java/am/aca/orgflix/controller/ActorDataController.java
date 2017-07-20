package am.aca.orgflix.controller;

import am.aca.orgflix.entity.Cast;
import am.aca.orgflix.service.CastService;
import am.aca.orgflix.service.ServiceException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for REST request
 */
@RestController
@RequestMapping("/data/actor")
public class ActorDataController {

    protected Logger LOGGER = Logger.getLogger(ActorDataController.class);

    private CastService castService;

    @Autowired
    public void setCastService(CastService castService) {
        this.castService = castService;
    }


    @RequestMapping("/getList")
    public ResponseEntity getActorsList() {
        try {
            String json = getActorsListJSON();
            return new ResponseEntity(json, HttpStatus.OK);
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
    @RequestMapping("/add")
    public ResponseEntity addNewActor(@RequestParam("actor") String actorName,
                                      @RequestParam("hasOscar") boolean hasOscar) {
        try {
            castService.add(new Cast(actorName, hasOscar));

            String json = getActorsListJSON();
            return new ResponseEntity(json, HttpStatus.OK);

        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    private String getActorsListJSON(){
        List<Cast> actorsList = castService.getAll();
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
        return sb.toString();
    }

}
