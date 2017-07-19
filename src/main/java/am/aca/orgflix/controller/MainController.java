package am.aca.orgflix.controller;

import am.aca.orgflix.entity.User;
import am.aca.orgflix.service.FilmService;
import am.aca.orgflix.service.ListService;
import am.aca.orgflix.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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


    @RequestMapping("/")
    public ModelAndView indexPageFirtVisit(HttpSession session) {
        if(!session.getAttributeNames().hasMoreElements() )
            session.setAttribute("userId", -1);
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("films", filmService.getAll(0, 12));
        modelAndView.addObject("ratings", filmService.getAllRatings(0, 12));
        modelAndView.addObject("currPage", 0);
        modelAndView.addObject("page", "index");
        modelAndView.addObject("numOfPages", filmService.getTotalNumber()/12);
        return modelAndView;
    }

    @RequestMapping("/index")
    public ModelAndView indexPage(@RequestParam("currPage") int page) {
        ModelAndView modelAndView;
        try {
            modelAndView = new ModelAndView("index");
            modelAndView.addObject("films", filmService.getAll(page * 12, 12));
            modelAndView.addObject("numOfPages", filmService.getTotalNumber() / 12);
            modelAndView.addObject("ratings", filmService.getAllRatings(page, 12));
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
                modelAndView = new ModelAndView("index");
                modelAndView.addObject("films", listService.showOwnWatched(userId, currPage * 12));
                modelAndView.addObject("numOfPages", listService.totalNumberOfFilmsInAList(userId, true) / 12);
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
                modelAndView.addObject("films", listService.showOwnPlanned(userId, currPage * 12));
                modelAndView.addObject("numOfPages", listService.totalNumberOfFilmsInAList(userId, false) / 12);
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
                modelAndView.addObject("films",
                        listService.showOthersWatched(userService.getByNick(nick).getId(), page));
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
                modelAndView.addObject("films",
                        listService.showOthersPlanned(userService.getByNick(nick).getId(), page));
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
