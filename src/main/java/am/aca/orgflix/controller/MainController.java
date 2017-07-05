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

    private UserService userService;
    private ListService listService;
    private FilmService filmService;
    private CastService castService;
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

    private ModelAndView getGuestMV(ModelAndView modelAndView){
        modelAndView.addObject("films", filmService.getFilmsList(0));
        modelAndView.addObject("userId", -1);
        modelAndView.addObject("userAuth", 0);
        modelAndView.addObject("currPage", 0);
        modelAndView.addObject("page", "main");
        return modelAndView;
    }
    //Upload
    private static final String UPLOAD_DIRECTORY = "resources/images";

    private static final String UPLOAD_FILE_DIRECTORY = UPLOAD_DIRECTORY + File.separator + "New Folder";

    private ServletContext context;

    @RequestMapping("uploadForm")
    public ModelAndView uploadForm() {
        return new ModelAndView("uploadForm");
    }

    @PostMapping(value = "filmSaved")
    public ModelAndView saveImage(@RequestParam CommonsMultipartFile file, HttpSession session) throws Exception {
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));


        //ServletFileUpload upload = new ServletFileUpload(factory);
        context = session.getServletContext();

        String uploadPath = context.getRealPath("") + File.separator + UPLOAD_FILE_DIRECTORY;

        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        byte[] bytes = file.getBytes();

        BufferedOutputStream stream;
        stream = new BufferedOutputStream(new FileOutputStream(new File(uploadPath + File.separator + 1 + ".jpg")));

        stream.write(bytes);
        stream.flush();
        stream.close();

        return new ModelAndView("uploadResult", "fileSuccess", "Film successfully saved!");
    }

    public static HttpSession session() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getSession(true);
    }
}
