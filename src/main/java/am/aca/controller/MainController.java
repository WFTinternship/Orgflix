package am.aca.controller;

import am.aca.entity.User;
import am.aca.servlet.BeanProvider;
import org.springframework.stereotype.Component;
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
        modelAndView.addObject("films", BeanProvider.getFilmService().getFilmsList(0));
        modelAndView.addObject("userId", -1);
        modelAndView.addObject("userAuth", 0);
        modelAndView.addObject("currPage", 0);
        modelAndView.addObject("type", "all");
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
        modelAndView.addObject("type", "all");
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
            modelAndView.addObject("types", "all");
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
//        modelAndView.addObject("type", "watch");
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
//        modelAndView.addObject("type", "watch");
        return modelAndView;
    }
}
