package am.aca.orgflix.controller;

import am.aca.orgflix.entity.Cast;
import am.aca.orgflix.entity.Film;
import am.aca.orgflix.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * ModelAndView controller on film CRUD related pages
 */
@Component
@RequestMapping("/")
public class FilmController extends MVController {

    private static final String UPLOAD_DIRECTORY = "resources/images";

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

        Film film = new Film(title, year);
        film.setDirector(director);
        film.setHasOscar(hasOscar == 1);
        if (star == 5) film.setRate_5star(1);
        else if (star == 4) film.setRate_4star(1);
        else if (star == 3) film.setRate_3star(1);
        else if (star == 2) film.setRate_2star(1);
        else film.setRate_1star(1);

        filmService.addFilm(film);
        int filmId = film.getId();
        List<Cast> castList = new ArrayList<>();
        for (int id : actorIds) {
            Cast cast = castService.getCastById(id);
            castList.add(cast);
            castService.addCastToFilm(cast, filmId);
        }
        film.setCasts(castList);
        film.setImage(filmId + "/" + filmId + ".jpg");
        filmService.editFilm(film);

        final String UPLOAD_FILE_DIRECTORY = UPLOAD_DIRECTORY + File.separator + filmId;

        String uploadPath = session.getServletContext().getRealPath("") + File.separator + UPLOAD_FILE_DIRECTORY;

        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        byte[] bytes = file.getBytes();

        BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(
                new File(uploadPath + File.separator + filmId + ".jpg")));

        stream.write(bytes);
        stream.flush();
        stream.close();

        User selUser = userService.get(userId);
        String user = selUser.getNick() + " (" + selUser.getEmail() + ")";

        ModelAndView modelAndView = new ModelAndView("uploadResult", "fileSuccess", "Film successfully saved!");
//        modelAndView.addObject("actors", castService.listCasts());
        modelAndView.addObject("userId", userId);
        modelAndView.addObject("user", user);
        modelAndView.addObject("userAuth", userAuth);
        modelAndView.addObject("currPage", 0);
        return modelAndView;
    }
}
