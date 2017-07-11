package am.aca.orgflix.controller;

import am.aca.orgflix.entity.Cast;
import am.aca.orgflix.entity.Film;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Main page mapper
 */
@Component
@RequestMapping("/")
public class MainController {

    private static final String UPLOAD_DIRECTORY = "resources/images";

    private UserService userService;
    private ListService listService;
    private FilmService filmService;
    private CastService castService;

//    @Autowired
//    public MainController(UserService userService, ListService listService,
//                          FilmService filmService, CastService castService) {
//        this.userService = userService;
//        this.listService = listService;
//        this.filmService = filmService;
//        this.castService = castService;
//    }

    public static HttpSession session() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getSession(true);
    }

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

    @RequestMapping("/index")
    public ModelAndView paging(@RequestParam("currPage") int page,
                               @RequestParam("userId") int userId,
                               @RequestParam("userAuth") int userAuth) {
        ModelAndView modelAndView = new ModelAndView("index");
        try {
            String user = "";
            if (userId != -1) {
                User selUser = userService.get(userId);
                user = selUser.getNick() + " (" + selUser.getEmail() + ")";
            }
            modelAndView.addObject("films", filmService.getFilmsList(page * 12));
            modelAndView.addObject("numOfPages", filmService.totalNumberOfFilms() / 12);
            modelAndView.addObject("userId", userId);
            modelAndView.addObject("user", user);
            modelAndView.addObject("userAuth", userAuth);
            modelAndView.addObject("currPage", page);
            modelAndView.addObject("page", "index");
        }catch (RuntimeException e){
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

    @RequestMapping("/watch_list")
    public ModelAndView watchList(@RequestParam("userId") int userId,
                                  @RequestParam("userAuth") int userAuth,
                                  @RequestParam("currPage") int currPage) {

        ModelAndView modelAndView;
        try{
            User selUser = userService.get(userId);
        String user = selUser.getNick() + " (" + selUser.getEmail() + ")";

        modelAndView = new ModelAndView("index");
        modelAndView.addObject("films", listService.showOwnWatched(userId,currPage*12));
        modelAndView.addObject("numOfPages", listService.totalNumberOfFilmsInAList(userId,true)/12);
        modelAndView.addObject("userId", userId);
        modelAndView.addObject("user", user);
        modelAndView.addObject("userAuth", userAuth);
        modelAndView.addObject("currPage", currPage);
        modelAndView.addObject("page", "watch_list");
        }catch (RuntimeException
                e) {
            modelAndView = new ModelAndView("error");
        }
        return modelAndView;
    }

    @RequestMapping("/wish_list")
    public ModelAndView wishList(@RequestParam("userId") int userId,
                                 @RequestParam("userAuth") int userAuth,
                                 @RequestParam("currPage") int currPage) {

        ModelAndView modelAndView = new ModelAndView("index");

        try {
            User selUser = userService.get(userId);
            String user = selUser.getNick() + " (" + selUser.getEmail() + ")";

            modelAndView.addObject("films", listService.showOwnPlanned(userId,currPage*12));
            modelAndView.addObject("numOfPages", listService.totalNumberOfFilmsInAList(userId,false)/12);
            modelAndView.addObject("userId", userId);
            modelAndView.addObject("user", user);
            modelAndView.addObject("userAuth", userAuth);
            modelAndView.addObject("currPage", currPage);
            modelAndView.addObject("page", "wish_list");
        } catch (RuntimeException e) {
            modelAndView = new ModelAndView("error");
        }
        return modelAndView;
    }

    @RequestMapping("/watch_list/getWatchedBy{nick}/{page}")
    public ModelAndView getWatchedByOtherUser(@RequestParam("userId") int userId,
                                              @RequestParam("userAuth") int userAuth,
                                              @PathVariable("nick") String nick,
                                              @PathVariable("page") int page) {
        ModelAndView modelAndView = new ModelAndView("index");

        try {

            User selUser = userService.get(userId);
            String user = selUser.getNick() + " (" + selUser.getEmail() + ")";

            modelAndView.addObject("films", listService.showOthersWatched(userService.getByNick(nick).getId(), page));
            modelAndView.addObject("userId", userId);
            modelAndView.addObject("user", user);
            modelAndView.addObject("userAuth", userAuth);
            modelAndView.addObject("currPage", page);
            modelAndView.addObject("page", "OthersWatched");
        } catch (RuntimeException e) {
            modelAndView = new ModelAndView("error");
        }

        return modelAndView;
    }

    @RequestMapping("/wish_list/getPlannedBy{nick}/{page}")
    public ModelAndView getPlannedByOtherUser(@RequestParam("userId") int userId,
                                              @RequestParam("userAuth") int userAuth,
                                              @PathVariable("nick") String nick,
                                              @PathVariable("page") int page) {
        ModelAndView modelAndView = new ModelAndView("index");

        try {
            User selUser = userService.get(userId);
            String user = selUser.getNick() + " (" + selUser.getEmail() + ")";

            modelAndView.addObject("films", listService.showOthersPlanned(userService.getByNick(nick).getId(), page));
            modelAndView.addObject("userId", userId);
            modelAndView.addObject("user", user);
            modelAndView.addObject("userAuth", userAuth);
            modelAndView.addObject("currPage", page);
            modelAndView.addObject("page", "OthersPlanned");
        } catch (RuntimeException e) {
            modelAndView = new ModelAndView("error");
        }

        return modelAndView;
    }

    //Upload
    @RequestMapping("/newFilm")
    public ModelAndView addFilm(@RequestParam("userId") int userId,
                                @RequestParam("userAuth") int userAuth) {

        ModelAndView modelAndView = new ModelAndView("newFilm");

        try {
            User selUser = userService.get(userId);
            String user = selUser.getNick() + " (" + selUser.getEmail() + ")";

            modelAndView.addObject("actors", castService.listCasts());
            modelAndView.addObject("userId", userId);
            modelAndView.addObject("user", user);
            modelAndView.addObject("userAuth", userAuth);
        } catch (RuntimeException e) {
            modelAndView = new ModelAndView("error");
        }
        return modelAndView;
    }

    @RequestMapping("/newFilmResult")
    public ModelAndView saveImage(@RequestParam CommonsMultipartFile file, HttpSession session,
                                  @RequestParam("userId") int userId, @RequestParam("userAuth") int userAuth,
                                  @RequestParam("title") String title, @RequestParam("year") int year,
                                  @RequestParam("stars") int star, @RequestParam("hasOscar") int hasOscar,
                                  @RequestParam("actorId") int[] actorIds, @RequestParam("director") String director,
                                  @RequestParam("numOfActors") int numOfActors
                                  )
            throws Exception {

        Film film = new Film(title,year);
        film.setDirector(director);
        film.setHasOscar(hasOscar == 1);
        if(star==5) film.setRate_5star(1);
        else if(star==4) film.setRate_4star(1);
        else if(star==3) film.setRate_3star(1);
        else if(star==2) film.setRate_2star(1);
        else film.setRate_1star(1);

        filmService.addFilm(film);
        int filmId = film.getId();
        List<Cast> castList = new ArrayList<>();
        for(int id : actorIds){
            Cast cast = castService.getCastById(id);
            castList.add(cast);
            castService.addCastToFilm(cast,filmId);
        }
        film.setCasts(castList);
        film.setImage(filmId+"/"+filmId+".jpg");
        filmService.editFilm(film);

        final String UPLOAD_FILE_DIRECTORY = UPLOAD_DIRECTORY + File.separator + filmId;

        String uploadPath = session.getServletContext().getRealPath("") + File.separator + UPLOAD_FILE_DIRECTORY;

        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        byte[] bytes = file.getBytes();

        BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(
                new File(uploadPath + File.separator + filmId + ".jpg") ));

        stream.write(bytes);
        stream.flush();
        stream.close();

        User selUser = userService.get(userId);
        String user = selUser.getNick() + " (" + selUser.getEmail() + ")";

        ModelAndView modelAndView = new ModelAndView("uploadResult", "fileSuccess", "Film successfully saved!");
        modelAndView.addObject("actors", castService.listCasts());
        modelAndView.addObject("userId", userId);
        modelAndView.addObject("user", user);
        modelAndView.addObject("userAuth", userAuth);
        return modelAndView;
    }

    // support method for guest modelAndView
    private ModelAndView getGuestMV(ModelAndView modelAndView){
        try{
            modelAndView.addObject("films", filmService.getFilmsList(0));
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
