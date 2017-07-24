package am.aca.orgflix.controller;

import am.aca.orgflix.service.FilmService;
import am.aca.orgflix.service.ListService;
import am.aca.orgflix.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

/**
 * Main page mapper
 */
@Controller
@RequestMapping("/")
public class MainController {

    private Logger LOGGER = Logger.getLogger(MainController.class);

    private UserService userService;
    private ListService listService;
    private FilmService filmService;

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


    /**
     * Page that is shown when first user first time visited for the current session
     * @param session current session
     */
    @RequestMapping("/")
    public ModelAndView indexPageFirtVisit(HttpSession session) {
        if (!session.getAttributeNames().hasMoreElements())
            session.setAttribute("userId", -1);
        ModelAndView modelAndView = new ModelAndView("home");
        modelAndView.addObject("films", filmService.getAll(0, 12));
        modelAndView.addObject("ratings", filmService.getAllRatings(0, 12));
        modelAndView.addObject("currentPage", 0);
        modelAndView.addObject("page", "index");
        modelAndView.addObject("numOfPages", filmService.getTotalNumber() / 12);
        return modelAndView;
    }

    /**
     * Main index page where the film lists are shown
     * @param page page number for pagination
     */
    @RequestMapping("/index")
    public ModelAndView indexPage(@RequestParam("currentPage") int page) {
        ModelAndView modelAndView;
        try {
            modelAndView = new ModelAndView("home");
            modelAndView.addObject("films", filmService.getAll(page * 12, 12));
            modelAndView.addObject("numOfPages", filmService.getTotalNumber() / 12);
            modelAndView.addObject("ratings", filmService.getAllRatings(page, 12));
            modelAndView.addObject("currentPage", page);
            modelAndView.addObject("page", "index");
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            modelAndView = new ModelAndView("error", "message", e);
        }
        return modelAndView;
    }

    /**
     * Watched list page
     * @param session current session
     * @param currentPage page number for pagination
     */
    @RequestMapping("/watch_list")
    public ModelAndView watchList(HttpSession session,
                                  @RequestParam("currentPage") int currentPage) {

        ModelAndView modelAndView;
        try {
            int userId = (int) session.getAttribute("userId");
            if (userId != -1) {
                modelAndView = new ModelAndView("home");
                modelAndView.addObject("films", listService.showOwnWatched(userId, currentPage * 12));
                modelAndView.addObject("numOfPages", listService.filmPrivacyList(userId, true).length / 12);
                modelAndView.addObject("privacyList", listService.filmPrivacyList(userId, false));
                modelAndView.addObject("currentPage", currentPage);
                modelAndView.addObject("page", "watch_list");
            } else {
                modelAndView = new ModelAndView("error", "message", "You are not logged in, please first login");
            }
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            modelAndView = new ModelAndView("error", "message", e);
        }
        return modelAndView;
    }

    /**
     * Wish list page
     * @param session current session
     * @param currentPage page number for pagination
     */
    @RequestMapping("/wish_list")
    public ModelAndView wishList(HttpSession session,
                                 @RequestParam("currentPage") int currentPage) {

        ModelAndView modelAndView = new ModelAndView("home");

        try {
            int userId = (int) session.getAttribute("userId");
            if (userId != -1) {
                modelAndView.addObject("films", listService.showOwnPlanned(userId, currentPage * 12));
                modelAndView.addObject("numOfPages", listService.filmPrivacyList(userId, false).length / 12);
                modelAndView.addObject("privacyList", listService.filmPrivacyList(userId, false));
                modelAndView.addObject("currentPage", currentPage);
                modelAndView.addObject("page", "wish_list");
            } else {
                modelAndView = new ModelAndView("error", "message", "You are not logged in, please first login");
            }
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            modelAndView = new ModelAndView("error", "message", e);
        }
        return modelAndView;
    }

    /**
     * Other users watched list page
     * @param session current session
     * @param otherUser the id of other user
     * @param currentPage page number for pagination
     */
    @RequestMapping("/watchListUserOther")
    public ModelAndView getWatchedByOtherUser(HttpSession session, @RequestParam("otherUser") int otherUser,
                                              @RequestParam("currentPage") int currentPage) {

        ModelAndView modelAndView = new ModelAndView("home");

        try {
            int userId = (int) session.getAttribute("userId");
            if (userId != -1) {
                modelAndView.addObject("films", listService.showOthersWatched(otherUser, currentPage));
                modelAndView.addObject("currentPage", currentPage);
                modelAndView.addObject("page", "watchListUserOther");
                modelAndView.addObject("otherUser", otherUser);
                modelAndView.addObject("title", "Watch list of user " + userService.getById(otherUser).getNick());
            } else {
                modelAndView = new ModelAndView("error", "message", "You are not logged in, please first login");
            }
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            modelAndView = new ModelAndView("error", "message", e);
        }
        return modelAndView;
    }

    /**
     * Other user's wished list page
     * @param session current session
     * @param otherUser the id of other user
     * @param currentPage page number for pagination
     */
    @RequestMapping("/wishListUserOther")
    public ModelAndView getPlannedByOtherUser(HttpSession session, @RequestParam("otherUser") int otherUser,
                                              @RequestParam("currentPage") int currentPage) {

        ModelAndView modelAndView = new ModelAndView("home");

        try {
            int userId = (int) session.getAttribute("userId");
            if (userId != -1) {
                modelAndView.addObject("films", listService.showOthersPlanned(otherUser, currentPage));
                modelAndView.addObject("currentPage", currentPage);
                modelAndView.addObject("page", "wishListUserOther");
                modelAndView.addObject("otherUser", otherUser);
                modelAndView.addObject("title", "Wish list of user " + userService.getById(otherUser).getNick());
            } else {
                modelAndView = new ModelAndView("error", "message", "You are not logged in, please first login");
            }
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            modelAndView = new ModelAndView("error", "message", e);
        }
        return modelAndView;
    }
}
