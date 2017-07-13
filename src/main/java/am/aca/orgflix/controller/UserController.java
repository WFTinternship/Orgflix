package am.aca.orgflix.controller;

import am.aca.orgflix.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * ModelAndView controller for user related actions
 */
@Component
@RequestMapping("/")
public class UserController extends MVController{

    @RequestMapping("/signup")
    public ModelAndView signup() {
        ModelAndView modelAndView = new ModelAndView("signup");
        return getGuestMV(modelAndView);
    }

    @RequestMapping("/signed")
    public ModelAndView signupResult(@RequestParam("nick") String nick,
                                     @RequestParam("userName") String userName,
                                     @RequestParam("email") String email,
                                     @RequestParam("pass") String pass) {

        ModelAndView modelAndView;
        User user = new User(nick, userName, email, pass);

        try {
            int userId = userService.add(user);
            if (userId != -1) {
                modelAndView = new ModelAndView("index");
                modelAndView.addObject("films", filmService.getFilmsList(0));
                modelAndView.addObject("userId", userId);
                modelAndView.addObject("user", nick + " (" + email + ")");
                modelAndView.addObject("userAuth", pass.hashCode() + email.hashCode());
                modelAndView.addObject("currPage", 0);
                modelAndView.addObject("page", "index");
            } else {
                modelAndView = new ModelAndView("error");
            }
        } catch (RuntimeException e) {
            modelAndView = new ModelAndView("error");
        }

        return modelAndView;
    }


    @RequestMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("login");
    }

    @RequestMapping("/logedIn")
    public ModelAndView paging(@RequestParam("email") String email,
                               @RequestParam("pass") String pass) {

        ModelAndView modelAndView;
        try {
            User user = userService.authenticate(email, pass);
            if (user == null) {
                modelAndView = new ModelAndView("error");
            } else {
                modelAndView = new ModelAndView("index");
                modelAndView.addObject("films", filmService.getFilmsList(0));
                modelAndView.addObject("numOfPages", filmService.totalNumberOfFilms() / 12);
                modelAndView.addObject("userId", user.getId());
                modelAndView.addObject("user", user.getNick() + " (" + email + ")");
                modelAndView.addObject("userAuth", user.getPass().hashCode() + email.hashCode());
                modelAndView.addObject("currPage", 0);
                modelAndView.addObject("page", "index");
            }
        }catch (RuntimeException e){
            modelAndView = new ModelAndView("error");
        }
        return modelAndView;
    }
}
