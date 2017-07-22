package am.aca.orgflix.controller;

import am.aca.orgflix.entity.Cast;
import am.aca.orgflix.entity.Film;
import am.aca.orgflix.entity.Genre;
import am.aca.orgflix.service.CastService;
import am.aca.orgflix.service.FilmService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * ModelAndView controller on film CRUD related pages
 */
@Controller
@RequestMapping("/")
public class FilmController {

    private Logger LOGGER = Logger.getLogger(FilmController.class);

    private FilmService filmService;
    private CastService castService;

    private static void doImageUpload(HttpSession session, CommonsMultipartFile file, int filmId)
            throws IOException {

        final String UPLOAD_DIRECTORY = "resources/images";
        final String UPLOAD_FILE_DIRECTORY = UPLOAD_DIRECTORY + File.separator + filmId;

        String uploadPath = session.getServletContext().getRealPath("") + File.separator + UPLOAD_FILE_DIRECTORY;

        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists())
            uploadDir.mkdirs();

        byte[] bytes = file.getBytes();

        BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(
                new File(uploadPath + File.separator + filmId + ".jpg")));

        stream.write(bytes);
        stream.flush();
        stream.close();
    }

    @Autowired
    public void setFilmService(FilmService filmService) {
        this.filmService = filmService;
    }

    @Autowired
    public void setCastService(CastService castService) {
        this.castService = castService;
    }

    @RequestMapping("/addFilm")
    public ModelAndView addFilm(HttpSession session) {

        ModelAndView modelAndView;
        try {
            int userId = (int) session.getAttribute("userId");
            if (userId != -1) {
                modelAndView = new ModelAndView("newFilm");
                modelAndView.addObject("actors", castService.getAll());
                modelAndView.addObject("genres", Genre.values());
            } else {
                modelAndView = new ModelAndView("error", "message", "You are not logged in, please first login");
            }
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            modelAndView = new ModelAndView("error", "message", e);
        }
        return modelAndView;
    }

    @RequestMapping("/addFilmResult")
    public ModelAndView createFilm(@RequestParam CommonsMultipartFile file, HttpSession session,
                                   @RequestParam("title") String title, @RequestParam("year") int year,
                                   @RequestParam("stars") int star, @RequestParam("hasOscar") int hasOscar,
                                   @RequestParam("actorId") int[] actorIds, @RequestParam("director") String director,
                                   @RequestParam("genre") String[] genres) throws IOException {

        ModelAndView modelAndView;

        try {
            int userId = (int) session.getAttribute("userId");

            if (userId != -1) {

                Film film = new Film(title, year);
                film.setDirector(director);
                film.setHasOscar(hasOscar == 1);

                filmService.add(film);
                int filmId = film.getId();
                filmService.rate(filmId, star);

                List<Cast> castList = castService.addCastsToFilm(filmId, actorIds);
                film.setCasts(castList);

                List<Genre> genreList = new ArrayList<>();
                for (String g : genres) {
                    genreList.add(Genre.valueOf(g));
                }
                film.setGenres(genreList);
                film.setImage(filmId + "/" + filmId + ".jpg");
                // write updated fields of film into DB
                filmService.edit(film);

                doImageUpload(session, file, filmId);

                modelAndView = new ModelAndView("addFilmResult", "fileSuccess", "Film successfully saved!");
                modelAndView.addObject("currentPage", 0);
            } else {
                modelAndView = new ModelAndView("error", "message", "You are not logged in, please first login");
            }
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            modelAndView = new ModelAndView("error", "message", e);
        }
        return modelAndView;
    }

    @RequestMapping("/searchFilm")
    public ModelAndView searchFilm(HttpSession session) {

        ModelAndView modelAndView;
        try {
            int userId = (int) session.getAttribute("userId");
            if (userId != -1) {
                modelAndView = new ModelAndView("findFilm");
                modelAndView.addObject("actors", castService.getAll());
                modelAndView.addObject("genres", Genre.values());

            } else {
                modelAndView = new ModelAndView("error", "message", "You are not logged in, please first login");
            }
        } catch (RuntimeException e) {
            LOGGER.warn(e.getMessage());
            modelAndView = new ModelAndView("error", "message", e);
        }
        return modelAndView;
    }

    @RequestMapping("/searchFilmResult")
    public ModelAndView searchFilm(HttpSession session, @RequestParam("title") String title,
                                   @RequestParam("startYear") int startYear, @RequestParam("finishYear") int finishYear,
                                   @RequestParam("hasOscar") int hasOscar, @RequestParam("director") String director,
                                   @RequestParam("actorId") int actorId, @RequestParam("genre") int genre) {

        ModelAndView modelAndView;

        try {
            int userId = (int) session.getAttribute("userId");

            if (userId != -1) {
                modelAndView = new ModelAndView("index");
                modelAndView.addObject("films", filmService.getFilteredFilms(title, startYear, finishYear,
                        hasOscar == 1, director, actorId, genre));
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
}
