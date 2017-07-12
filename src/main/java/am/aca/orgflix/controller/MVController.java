package am.aca.orgflix.controller;

import am.aca.orgflix.service.CastService;
import am.aca.orgflix.service.FilmService;
import am.aca.orgflix.service.ListService;
import am.aca.orgflix.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;


/**
 * Super class for created custom ModelAndView controllers
 */
public class MVController {

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
            modelAndView.addObject("films", filmService.getFilmsList(0));
            modelAndView.addObject("ratings", filmService.getAllRatings(0));
            modelAndView.addObject("userId", -1);
            modelAndView.addObject("userAuth", 0);
            modelAndView.addObject("currPage", 0);
            modelAndView.addObject("page", "index");
            modelAndView.addObject("numOfPages", filmService.totalNumberOfFilms()/12);
        }catch (RuntimeException e){
            modelAndView = new ModelAndView("error");
        }
        return modelAndView;
    }
}
