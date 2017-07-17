package am.aca.orgflix.controller;

import am.aca.orgflix.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

/**
 * ModelAndView controller for user related actions
 */
@Component
@RequestMapping("/")
public class UserController extends MVController{

    @RequestMapping("/signup")
    public ModelAndView signup() {
        ModelAndView modelAndView;
        try{
            modelAndView = new ModelAndView("signup");
            modelAndView = getGuestMV(modelAndView);
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            modelAndView = new ModelAndView("error","message",e);
        }
        return modelAndView;
    }

    @RequestMapping("/signed")
    public ModelAndView signupResult(HttpSession session,
                                     @RequestParam("nick") String nick,
                                     @RequestParam("userName") String userName,
                                     @RequestParam("email") String email,
                                     @RequestParam("pass") String pass) {

        ModelAndView modelAndView;
        User user = new User(nick, userName, email, pass);

        try {
            int userId = userService.add(user);
            if (userId != -1) {
                session.setAttribute("userId", userId);
                session.setAttribute("user", user.getNick() + " (" + email + ")");

                modelAndView = new ModelAndView("index");
                modelAndView.addObject("films", filmService.getFilmsList(0));
                modelAndView.addObject("numOfPages", filmService.totalNumberOfFilms() / 12);
                modelAndView.addObject("currPage", 0);
                modelAndView.addObject("page", "index");
            } else {
                modelAndView = new ModelAndView("error", "message", "You are not logged in, please first login");
            }
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            modelAndView = new ModelAndView("error","message",e);
        }
        return modelAndView;
    }


    @RequestMapping("/login")
    public ModelAndView login() {
        ModelAndView modelAndView;
        try{
            modelAndView = new ModelAndView("login");
        }catch (RuntimeException e){
            LOGGER.warn(e.getMessage());
            modelAndView = new ModelAndView("error","message",e);
        }
        return modelAndView;
    }

    @RequestMapping("/logout")
    public ModelAndView logout(HttpSession session) {
        ModelAndView modelAndView;
        try{
            session.setAttribute("userId", -1);
            session.removeAttribute("user");
            modelAndView = new ModelAndView("login");
        }catch (RuntimeException e){
            LOGGER.warn(e.getMessage());
            modelAndView = new ModelAndView("error","message",e);
        }
        return modelAndView;
    }

    @RequestMapping("/loggedIn")
    public ModelAndView paging(HttpSession session,
                               @RequestParam("email") String email,
                               @RequestParam("pass") String pass) {

        ModelAndView modelAndView;
        try {
            User user = userService.authenticate(email, pass);
            if (user == null) {
                modelAndView = new ModelAndView("error","message","Wrong email/or password, please try again");
                session.setAttribute("userId", -1);
                session.setAttribute("user", "");
            } else {
                modelAndView = new ModelAndView("index");
                modelAndView.addObject("films", filmService.getFilmsList(0));
                modelAndView.addObject("numOfPages", filmService.totalNumberOfFilms() / 12);
                modelAndView.addObject("currPage", 0);
                modelAndView.addObject("page", "index");

                session.setAttribute("userId", user.getId());
                session.setAttribute("user", user.getNick() + " (" + user.getEmail() + ")");
            }
        }catch (RuntimeException e){
            LOGGER.warn(e.getMessage());
            modelAndView = new ModelAndView("error","message",e);
        }
        return modelAndView;
    }
}
