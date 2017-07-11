package am.aca.orgflix.controller;

import am.aca.orgflix.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * Main page mapper
 */
@Component
@RequestMapping("/")
public class MainController extends MVController {

    @RequestMapping("/")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("index");
        return getGuestMV(modelAndView);
    }

    @RequestMapping("/index")
    public ModelAndView paging(@RequestParam("currPage") int page,
                               @RequestParam("userId") int userId,
                               @RequestParam("userAuth") int userAuth) {
        ModelAndView modelAndView = new ModelAndView("index");
        try {
            String user = "";
            if (userId != -1) {
                User selUser = userService.get(userId);
                user = selUser.getNick() + " (" + selUser.getEmail() + ")";
            }
            modelAndView.addObject("films", filmService.getFilmsList(page * 12));
            modelAndView.addObject("numOfPages", filmService.totalNumberOfFilms() / 12);
            modelAndView.addObject("userId", userId);
            modelAndView.addObject("user", user);
            modelAndView.addObject("userAuth", userAuth);
            modelAndView.addObject("currPage", page);
            modelAndView.addObject("page", "index");
        } catch (RuntimeException e) {
            modelAndView = new ModelAndView("error");
        }
        return modelAndView;
    }

    @RequestMapping("/watch_list")
    public ModelAndView watchList(@RequestParam("userId") int userId,
                                  @RequestParam("userAuth") int userAuth,
                                  @RequestParam("currPage") int currPage) {

        ModelAndView modelAndView;
        try {
            User selUser = userService.get(userId);
            String user = selUser.getNick() + " (" + selUser.getEmail() + ")";

            modelAndView = new ModelAndView("index");
            modelAndView.addObject("films", listService.showOwnWatched(userId, currPage * 12));
            modelAndView.addObject("numOfPages", listService.totalNumberOfFilmsInAList(userId, true) / 12);
            modelAndView.addObject("userId", userId);
            modelAndView.addObject("user", user);
            modelAndView.addObject("userAuth", userAuth);
            modelAndView.addObject("currPage", currPage);
            modelAndView.addObject("page", "watch_list");
        } catch (RuntimeException
                e) {
            modelAndView = new ModelAndView("error");
        }
        return modelAndView;
    }

    @RequestMapping("/wish_list")
    public ModelAndView wishList(@RequestParam("userId") int userId,
                                 @RequestParam("userAuth") int userAuth,
                                 @RequestParam("currPage") int currPage) {

        ModelAndView modelAndView = new ModelAndView("index");

        try {
            User selUser = userService.get(userId);
            String user = selUser.getNick() + " (" + selUser.getEmail() + ")";

            modelAndView.addObject("films", listService.showOwnPlanned(userId, currPage * 12));
            modelAndView.addObject("numOfPages", listService.totalNumberOfFilmsInAList(userId, false) / 12);
            modelAndView.addObject("userId", userId);
            modelAndView.addObject("user", user);
            modelAndView.addObject("userAuth", userAuth);
            modelAndView.addObject("currPage", currPage);
            modelAndView.addObject("page", "wish_list");
        } catch (RuntimeException e) {
            modelAndView = new ModelAndView("error");
        }
        return modelAndView;
    }

    @RequestMapping("/watch_list/getWatchedBy{nick}/{page}")
    public ModelAndView getWatchedByOtherUser(@RequestParam("userId") int userId,
                                              @RequestParam("userAuth") int userAuth,
                                              @PathVariable("nick") String nick,
                                              @PathVariable("page") int page) {
        ModelAndView modelAndView = new ModelAndView("index");

        try {

            User selUser = userService.get(userId);
            String user = selUser.getNick() + " (" + selUser.getEmail() + ")";

            modelAndView.addObject("films", listService.showOthersWatched(userService.getByNick(nick).getId(), page));
            modelAndView.addObject("userId", userId);
            modelAndView.addObject("user", user);
            modelAndView.addObject("userAuth", userAuth);
            modelAndView.addObject("currPage", page);
            modelAndView.addObject("page", "OthersWatched");
        } catch (RuntimeException e) {
            modelAndView = new ModelAndView("error");
        }

        return modelAndView;
    }

    @RequestMapping("/wish_list/getPlannedBy{nick}/{page}")
    public ModelAndView getPlannedByOtherUser(@RequestParam("userId") int userId,
                                              @RequestParam("userAuth") int userAuth,
                                              @PathVariable("nick") String nick,
                                              @PathVariable("page") int page) {
        ModelAndView modelAndView = new ModelAndView("index");

        try {
            User selUser = userService.get(userId);
            String user = selUser.getNick() + " (" + selUser.getEmail() + ")";

            modelAndView.addObject("films", listService.showOthersPlanned(userService.getByNick(nick).getId(), page));
            modelAndView.addObject("userId", userId);
            modelAndView.addObject("user", user);
            modelAndView.addObject("userAuth", userAuth);
            modelAndView.addObject("currPage", page);
            modelAndView.addObject("page", "OthersPlanned");
        } catch (RuntimeException e) {
            modelAndView = new ModelAndView("error");
        }

        return modelAndView;
    }
}
