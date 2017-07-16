package am.aca.orgflix.controller;

import am.aca.orgflix.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

/**
 * Main page mapper
 */
@Component
@RequestMapping("/")
public class MainController extends MVController {

    @RequestMapping("/")
    public ModelAndView index(HttpSession session) {
        if(!session.getAttributeNames().hasMoreElements() )
            session.setAttribute("userId", -1);
        ModelAndView modelAndView = new ModelAndView("index");
        return getGuestMV(modelAndView);
    }

    @RequestMapping("/index")
    public ModelAndView paging(HttpSession session,
                               @RequestParam("currPage") int page) {
        ModelAndView modelAndView;
        try {
            modelAndView = new ModelAndView("index");
            String user = "";
            int userId = (int) session.getAttribute("userId");
            if ( userId != -1) {
                User selUser = userService.get(userId);
                user = selUser.getNick() + " (" + selUser.getEmail() + ")";
            }
            modelAndView.addObject("films", filmService.getFilmsList(page * 12));
            modelAndView.addObject("numOfPages", filmService.totalNumberOfFilms() / 12);
            modelAndView.addObject("ratings", filmService.getAllRatings(page));
            modelAndView.addObject("currPage", page);
            modelAndView.addObject("page", "index");
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            modelAndView = new ModelAndView("error","message",e);
        }
        return modelAndView;
    }

    @RequestMapping("/watch_list")
    public ModelAndView watchList(HttpSession session,
                                  @RequestParam("currPage") int currPage) {

        ModelAndView modelAndView;
        try {
            int userId = (int) session.getAttribute("userId");
            if (userId != -1) {
                User selUser = userService.get(userId);
                String user = selUser.getNick() + " (" + selUser.getEmail() + ")";

                modelAndView = new ModelAndView("index");
                modelAndView.addObject("films", listService.showOwnWatched(userId, currPage * 12));
                modelAndView.addObject("numOfPages", listService.totalNumberOfFilmsInAList(userId, true) / 12);
                modelAndView.addObject("user", user);
                modelAndView.addObject("currPage", currPage);
                modelAndView.addObject("page", "watch_list");
            }else{
                modelAndView = new ModelAndView("error","message","You are not logged in, please first login");
            }
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            modelAndView = new ModelAndView("error","message",e);
        }
        return modelAndView;
    }

    @RequestMapping("/wish_list")
    public ModelAndView wishList(HttpSession session,
                                 @RequestParam("currPage") int currPage) {

        ModelAndView modelAndView = new ModelAndView("index");

        try {
            int userId = (int) session.getAttribute("userId");
            if (userId != -1) {
                User selUser = userService.get(userId);
                String user = selUser.getNick() + " (" + selUser.getEmail() + ")";

                modelAndView.addObject("films", listService.showOwnPlanned(userId, currPage * 12));
                modelAndView.addObject("numOfPages", listService.totalNumberOfFilmsInAList(userId, false) / 12);
                modelAndView.addObject("user", user);
                modelAndView.addObject("currPage", currPage);
                modelAndView.addObject("page", "wish_list");
            }else{
                modelAndView = new ModelAndView("error","message","You are not logged in, please first login");
            }
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            modelAndView = new ModelAndView("error","message",e);
        }
        return modelAndView;
    }

    @RequestMapping("/watch_list/getWatchedBy{nick}/{page}")
    public ModelAndView getWatchedByOtherUser(HttpSession session,
                                              @PathVariable("nick") String nick,
                                              @PathVariable("page") int page) {
        ModelAndView modelAndView = new ModelAndView("index");

        try {
            int userId = (int) session.getAttribute("userId");
            if (userId != -1) {
                User selUser = userService.get(userId);
                String user = selUser.getNick() + " (" + selUser.getEmail() + ")";

                modelAndView.addObject("films", listService.showOthersWatched(userService.getByNick(nick).getId(), page));
                modelAndView.addObject("user", user);
                modelAndView.addObject("currPage", page);
                modelAndView.addObject("page", "OthersWatched");
            }else{
                modelAndView = new ModelAndView("error","message","You are not logged in, please first login");
            }
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            modelAndView = new ModelAndView("error","message",e);
        }
        return modelAndView;
    }

    @RequestMapping("/wish_list/getPlannedBy{nick}/{page}")
    public ModelAndView getPlannedByOtherUser(HttpSession session,
                                              @PathVariable("nick") String nick,
                                              @PathVariable("page") int page) {
        ModelAndView modelAndView = new ModelAndView("index");

        try {
            int userId = (int) session.getAttribute("userId");
            if (userId != -1) {

                User selUser = userService.get(userId);
                String user = selUser.getNick() + " (" + selUser.getEmail() + ")";

                modelAndView.addObject("films", listService.showOthersPlanned(userService.getByNick(nick).getId(), page));
                modelAndView.addObject("user", user);
                modelAndView.addObject("currPage", page);
                modelAndView.addObject("page", "OthersPlanned");
            }else{
                modelAndView = new ModelAndView("error","message","You are not logged in, please first login");
            }
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            modelAndView = new ModelAndView("error","message",e);
        }

        return modelAndView;
    }
}
