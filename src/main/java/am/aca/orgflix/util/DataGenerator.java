package am.aca.orgflix.util;

import am.aca.orgflix.entity.Cast;
import am.aca.orgflix.entity.Film;
import am.aca.orgflix.entity.Genre;
import am.aca.orgflix.service.FilmService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by karine on 6/27/2017
 */
@Component
public class DataGenerator implements ServletContextListener {

    private final FilmService filmService;

    @Autowired
    public DataGenerator(FilmService filmService) {
        this.filmService = filmService;
    }

    private List<Film> generate() throws IOException {
        Document doc = Jsoup.connect("http://www.imdb.com/search/title?groups=top_250&sort=user_rating").get();
        Elements movieDivs = doc.getElementsByClass("lister-item");

        List<Film> movieList = new ArrayList<>();

        for (Element movieDiv : movieDivs) {
            Film film = new Film();
            film.setTitle(movieDiv.select("a[href]").get(1).text());
            film.setDirector(movieDiv.select("a[href]").get(13).text());
            film.addCast(new Cast(movieDiv.select("a[href]").get(14).text(), Math.random() < 0.5));
            film.addCast(new Cast(movieDiv.select("a[href]").get(15).text(), Math.random() < 0.5));
            film.addCast(new Cast(movieDiv.select("a[href]").get(16).text(), Math.random() < 0.5));
            film.addCast(new Cast(movieDiv.select("a[href]").get(17).text(), Math.random() < 0.5));
            film.setProdYear(Integer.parseInt(movieDiv.getElementsByClass("lister-item-year").text().substring(1, 5)));
            for (String s : Arrays.asList(movieDiv.getElementsByClass("genre").text().split("\\s*,\\s*"))) {
                film.addGeners(Genre.getByTitle(s));
            }
            film.setHasOscar(Math.random() < 0.5);
            movieList.add(film);
            filmService.add(film);
        }
        return movieList;
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            generate();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
