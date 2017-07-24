package am.aca.orgflix.controller;

import am.aca.orgflix.entity.User;
import am.aca.orgflix.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for REST request
 */
@RestController
@RequestMapping("/data/user")
public class UserDataController {

    protected Logger LOGGER = Logger.getLogger(UserDataController.class);

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }


    /**
     * Provides user's list in JSON format
     * @return string in JSON format and Http OK status if succeeded
     */
    @RequestMapping("/getList")
    public ResponseEntity getUsersList() {
        try {
            String json = getUsersListJSON();
            return new ResponseEntity(json, HttpStatus.OK);
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Helper method the convert the list of user objects to string in JSON format
     * @return string in JSON format
     */
    private String getUsersListJSON(){
        List<User> usersList = userService.getAll();
        int size = usersList.size();
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (User user : usersList) {
            sb.append("{");
            sb.append("\"id\":\"");
            sb.append(user.getId());
            sb.append("\", \"nick\":\"");
            sb.append(user.getNick());
            sb.append("\"}");
            sb.append(--size > 0 ? "," : "");
        }
        sb.append("]");
        return sb.toString();
    }

}
