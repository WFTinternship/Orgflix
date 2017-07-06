package am.aca.orgflix.controller;

import am.aca.orgflix.entity.User;
import am.aca.orgflix.service.CastService;
import am.aca.orgflix.service.FilmService;
import am.aca.orgflix.service.ListService;
import am.aca.orgflix.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Main page mapper
 */
@Component
@Scope("session")
@RequestMapping("/")
public class MainController {

    private static final String UPLOAD_DIRECTORY = "resources/images";

    private UserService userService;
    private ListService listService;
    private FilmService filmService;
    private CastService castService;

    @Autowired
    public MainController(UserService userService, ListService listService,
                          FilmService filmService, CastService castService){
        this.userService = userService;
        this.listService = listService;
        this.filmService = filmService;
        this.castService = castService;
    }

    @ModelAttribute("User")
    public User populateUser() {
        return new User();
    }

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

        int userId = userService.add(user);
        if (userId != -1) {
            modelAndView = new ModelAndView("index");
            modelAndView.addObject("films", filmService.getFilmsList(0));
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
            User selUser = userService.get(userId);
            user = selUser.getNick() + " (" + selUser.getEmail() + ")";
        }
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("films", filmService.getFilmsList(page * 12));
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
        User user = userService.authenticate(email, pass);
        if (user == null) {
            modelAndView = new ModelAndView("error");
        } else {
            modelAndView = new ModelAndView("index");
            modelAndView.addObject("films", filmService.getFilmsList(0));
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
                               @RequestParam("userAuth") int userAuth,
                                  @RequestParam("currPage") int currPage) {

        User selUser = userService.get(userId);
        String user = selUser.getNick() + " (" + selUser.getEmail() + ")";

        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("films", listService.showOwnWatched(userId,currPage));
        modelAndView.addObject("userId", userId);
        modelAndView.addObject("user", user);
        modelAndView.addObject("userAuth", userAuth);
        modelAndView.addObject("currPage", 0);
        modelAndView.addObject("page", "watch");
        return modelAndView;
    }

    @RequestMapping("/wish_list")
    public ModelAndView wishList(@RequestParam("userId") int userId,
                               @RequestParam("userAuth") int userAuth,
                                 @RequestParam("currPage") int currPage) {

        User selUser = userService.get(userId);
        String user = selUser.getNick() + " (" + selUser.getEmail() + ")";

        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("films", listService.showOwnPlanned(userId,currPage));
        modelAndView.addObject("userId", userId);
        modelAndView.addObject("user", user);
        modelAndView.addObject("userAuth", userAuth);
        modelAndView.addObject("currPage", 0);
        modelAndView.addObject("page", "wish");
        return modelAndView;
    }

    //Upload
    @RequestMapping("/newFilm")
    public ModelAndView addFilm(@RequestParam("userId") int userId,
                                   @RequestParam("userAuth") int userAuth) {

        User selUser = userService.get(userId);
        String user = selUser.getNick() + " (" + selUser.getEmail() + ")";

        ModelAndView modelAndView = new ModelAndView("newFilm");
        modelAndView.addObject("actors", castService.listCasts());
        modelAndView.addObject("userId", userId);
        modelAndView.addObject("user", user);
        modelAndView.addObject("userAuth", userAuth);
        return modelAndView;
    }

    @RequestMapping("/newFilmResult")
    public ModelAndView saveImage(@RequestParam CommonsMultipartFile file, HttpSession session)
            throws Exception {

        final String UPLOAD_FILE_DIRECTORY = UPLOAD_DIRECTORY + File.separator + "New Folder";

        String uploadPath = session.getServletContext().getRealPath("") + File.separator + UPLOAD_FILE_DIRECTORY;

        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        byte[] bytes = file.getBytes();

        BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(
                new File(uploadPath + File.separator + 1 + ".jpg") ));

        stream.write(bytes);
        stream.flush();
        stream.close();

        return new ModelAndView("uploadResult", "fileSuccess", "Film successfully saved!");
    }

    // support method for guest modelAndView
    private ModelAndView getGuestMV(ModelAndView modelAndView){
        modelAndView.addObject("films", filmService.getFilmsList(0));
        modelAndView.addObject("userId", -1);
        modelAndView.addObject("userAuth", 0);
        modelAndView.addObject("currPage", 0);
        modelAndView.addObject("page", "main");
        return modelAndView;
    }

    public static HttpSession session() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getSession(true);
    }
}
