package am.aca.controller;

import am.aca.entity.User;
import am.aca.servlet.BeanProvider;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * Main page mapper
 */
@Component
@RequestMapping("/")
public class MainController {

    @RequestMapping("/")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("index");
        return getGuestMV(modelAndView);
    }

    @GetMapping("/signup")
    public ModelAndView signup() {
        ModelAndView modelAndView = new ModelAndView("signup");
        return getGuestMV(modelAndView);
    }

    @PostMapping("/signup")
    public ModelAndView signupResult(@RequestParam("nick") String nick,
                                     @RequestParam("userName") String userName,
                                     @RequestParam("email") String email,
                                     @RequestParam("pass") String pass) {

        ModelAndView modelAndView = null;
        User user = new User(nick, userName, email, pass);

        int userId = BeanProvider.getUserService().add(user);
        if (userId != -1) {
            modelAndView = new ModelAndView("index");
            modelAndView.addObject("films", BeanProvider.getFilmService().getFilmsList(0));
            modelAndView.addObject("userId", userId);
            modelAndView.addObject("user", nick + " (" + email + ")");
            modelAndView.addObject("userAuth", pass.hashCode() + email.hashCode());
            modelAndView.addObject("currPage", 0);
            modelAndView.addObject("page", "main");
        } else{
            modelAndView = new ModelAndView("error");
        }

        return modelAndView;
    }

    @RequestMapping("/index")
    public ModelAndView paging(@RequestParam("currPage") int page,
                               @RequestParam("userId") int userId,
                               @RequestParam("userAuth") int userAuth) {
        String user = "";
        if (userId != -1) {
            User selUser = BeanProvider.getUserService().get(userId);
            user = selUser.getNick() + " (" + selUser.getEmail() + ")";
        }
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("films", BeanProvider.getFilmService().getFilmsList(page * 12));
        modelAndView.addObject("userId", userId);
        modelAndView.addObject("user", user);
        modelAndView.addObject("userAuth", userAuth);
        modelAndView.addObject("currPage", page);
        modelAndView.addObject("page", "main");
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
        User user = BeanProvider.getUserService().authenticate(email, pass);
        if (user == null) {
            modelAndView = new ModelAndView("error");
        } else {
            modelAndView = new ModelAndView("index");
            modelAndView.addObject("films", BeanProvider.getFilmService().getFilmsList(0));
            modelAndView.addObject("userId", user.getId());
            modelAndView.addObject("user", user.getNick() + " (" + email + ")");
            modelAndView.addObject("userAuth", user.getPass().hashCode() + email.hashCode());
            modelAndView.addObject("currPage", 0);
            modelAndView.addObject("page", "main");
        }
        return modelAndView;
    }

    @RequestMapping("/watch_list")
    public ModelAndView watchList(@RequestParam("userId") int userId,
                               @RequestParam("userAuth") int userAuth) {

        User selUser = BeanProvider.getUserService().get(userId);
        String user = selUser.getNick() + " (" + selUser.getEmail() + ")";

        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("films", BeanProvider.getListService().showOwnWatched(userId));
        modelAndView.addObject("userId", userId);
        modelAndView.addObject("user", user);
        modelAndView.addObject("userAuth", userAuth);
        modelAndView.addObject("currPage", 0);
        modelAndView.addObject("page", "watch");
        return modelAndView;
    }

    @RequestMapping("/wish_list")
    public ModelAndView wishList(@RequestParam("userId") int userId,
                               @RequestParam("userAuth") int userAuth) {

        User selUser = BeanProvider.getUserService().get(userId);
        String user = selUser.getNick() + " (" + selUser.getEmail() + ")";

        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("films", BeanProvider.getListService().showOwnPlanned(userId));
        modelAndView.addObject("userId", userId);
        modelAndView.addObject("user", user);
        modelAndView.addObject("userAuth", userAuth);
        modelAndView.addObject("currPage", 0);
        modelAndView.addObject("page", "wish");
        return modelAndView;
    }

    private ModelAndView getGuestMV(ModelAndView modelAndView){
        modelAndView.addObject("films", BeanProvider.getFilmService().getFilmsList(0));
        modelAndView.addObject("userId", -1);
        modelAndView.addObject("userAuth", 0);
        modelAndView.addObject("currPage", 0);
        modelAndView.addObject("page", "main");
        return modelAndView;
    }
}
