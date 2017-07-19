package am.aca.orgflix.controller;

import am.aca.orgflix.service.CastService;
import am.aca.orgflix.service.FilmService;
import am.aca.orgflix.service.ListService;
import am.aca.orgflix.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;


/**
 * Super class for created custom ModelAndView controllers
 */
public class MVController {

    //common logger for all the classes in the Service layer
    protected Logger LOGGER = LOGGER = Logger.getLogger(MVController.class);

    protected UserService userService;
    protected ListService listService;
    protected FilmService filmService;
    protected CastService castService;

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

    // support method for guest modelAndView
    protected ModelAndView getGuestMV(ModelAndView modelAndView){
        try{
            modelAndView.addObject("films", filmService.getAll(0, 12));
            modelAndView.addObject("ratings", filmService.getAllRatings(0, 12));
            modelAndView.addObject("currPage", 0);
            modelAndView.addObject("page", "index");
            modelAndView.addObject("numOfPages", filmService.getTotalNumber()/12);
        }catch (RuntimeException e){
            modelAndView = new ModelAndView("error","message",e);
        }
        return modelAndView;
    }
}
