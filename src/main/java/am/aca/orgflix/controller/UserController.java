package am.aca.orgflix.controller;

import am.aca.orgflix.entity.User;
import am.aca.orgflix.service.FilmService;
import am.aca.orgflix.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

/**
 * ModelAndView controller for user related actions
 */
@Controller
@RequestMapping("/")
public class UserController {

    private Logger LOGGER = Logger.getLogger(UserController.class);

    private UserService userService;
    private FilmService filmService;

    private static final int FIXED_ITEMS_PER_PAGE = 12;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setFilmService(FilmService filmService) {
        this.filmService = filmService;
    }


    /**
     * View for sign-up page
     */
    @RequestMapping("/signup")
    public ModelAndView signup() {
        ModelAndView modelAndView;
        try {
            modelAndView = new ModelAndView("register");
            modelAndView.addObject("films", filmService.getAll(0, FIXED_ITEMS_PER_PAGE));
            modelAndView.addObject("ratings", filmService.getAllRatings(0, FIXED_ITEMS_PER_PAGE));
            modelAndView.addObject("currentPage", 0);
            modelAndView.addObject("page", "index");
            modelAndView.addObject("numOfPages", filmService.getTotalNumber() / FIXED_ITEMS_PER_PAGE);
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            modelAndView = new ModelAndView("error", "message", e);
        }
        return modelAndView;
    }

    /**
     * Adding new user
     * @param session current session
     * @param nick new user's nick name
     * @param userName new user's name
     * @param email new user's email
     * @param pass new user's password
     */
    @RequestMapping("/signed")
    public ModelAndView signupResult(HttpSession session,
                                     @RequestParam("nick") String nick,
                                     @RequestParam("userName") String userName,
                                     @RequestParam("email") String email,
                                     @RequestParam("pass") String pass) {

        ModelAndView modelAndView;
        User user = new User(nick, userName, email, pass.hashCode() + email);

        try {
            int userId = userService.add(user);
            if (userId != -1) {
                session.setAttribute("userId", userId);
                session.setAttribute("user", user.getNick() + " (" + email + ")");

                modelAndView = new ModelAndView("home");
                modelAndView.addObject("films", filmService.getAll(0, FIXED_ITEMS_PER_PAGE));
                modelAndView.addObject("numOfPages", filmService.getTotalNumber() / FIXED_ITEMS_PER_PAGE);
                modelAndView.addObject("currentPage", 0);
                modelAndView.addObject("page", "index");
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
     * View for login page
     */
    @RequestMapping("/login")
    public ModelAndView login() {
        ModelAndView modelAndView;
        try {
            modelAndView = new ModelAndView("signIn");
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            modelAndView = new ModelAndView("error", "message", e);
        }
        return modelAndView;
    }

    /**
     * Log-outing from current session
     * @param session current session
     */
    @RequestMapping("/logout")
    public ModelAndView logout(HttpSession session) {
        ModelAndView modelAndView;
        try {
            session.setAttribute("userId", -1);
            session.removeAttribute("user");
            modelAndView = new ModelAndView("signIn");
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            modelAndView = new ModelAndView("error", "message", e);
        }
        return modelAndView;
    }

    /**
     * View after successful login
     * @param session current session
     * @param email user's email
     * @param pass user's password
     */
    @RequestMapping("/loggedIn")
    public ModelAndView paging(HttpSession session,
                               @RequestParam("email") String email,
                               @RequestParam("pass") String pass) {

        ModelAndView modelAndView;
        try {
            User user = userService.authenticate(email, pass.hashCode() + email);
            if (user == null) {
                modelAndView = new ModelAndView("error", "message", "Wrong email/or password, please try again");
                session.setAttribute("userId", -1);
                session.setAttribute("user", " ");
            } else {
                modelAndView = new ModelAndView("home");
                modelAndView.addObject("films", filmService.getAll(0, FIXED_ITEMS_PER_PAGE));
                modelAndView.addObject("numOfPages", filmService.getTotalNumber() / FIXED_ITEMS_PER_PAGE);
                modelAndView.addObject("currentPage", 0);
                modelAndView.addObject("page", "index");

                session.setAttribute("userId", user.getId());
                session.setAttribute("user", user.getNick() + " (" + user.getEmail() + ")");
            }
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            modelAndView = new ModelAndView("error", "message", "Authentication failed");
        }
        return modelAndView;
    }
}
